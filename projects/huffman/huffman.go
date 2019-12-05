package huffman

import (
	"bytes"
	"container/heap"
	"fmt"
	"io"

	"github.com/icza/bitio"
)

type alphabet struct {
	value byte
	count int
}

type tree struct {
	alphabet *alphabet
	left     *tree
	right    *tree
}

type ptree struct {
	index int
	tree
}

type priorityQueue []*ptree

func (pq priorityQueue) Len() int {
	return len(pq)
}

func (pq priorityQueue) Less(i, j int) bool {
	return pq[i].alphabet.count < pq[j].alphabet.count
}

func (pq priorityQueue) Swap(i, j int) {
	pq[i], pq[j] = pq[j], pq[i]
	pq[i].index = i
	pq[j].index = j
}

func (pq *priorityQueue) Push(x interface{}) {
	n := len(*pq)
	item := x.(*ptree)
	item.index = n
	*pq = append(*pq, item)
}

func (pq *priorityQueue) Pop() interface{} {
	old := *pq
	n := len(old)
	item := old[n-1]
	old[n-1] = nil  // avoid memory leak
	item.index = -1 // for safety
	*pq = old[0 : n-1]
	return item
}

// update modifies the priority and value of an Item in the queue.
func (pq *priorityQueue) update(ptree *ptree, value byte, count int) {
	ptree.alphabet.value = value
	ptree.alphabet.count = count
	heap.Fix(pq, ptree.index)
}

func newPriorityQueue(bytCntMap map[byte]int) priorityQueue {
	pq := make(priorityQueue, len(bytCntMap))
	i := 0
	for byt, cnt := range bytCntMap {
		pq[i] = &ptree{
			tree: tree{
				alphabet: &alphabet{
					value: byt,
					count: cnt,
				},
			},
			index: i,
		}
		i++
	}
	return pq
}

type code struct {
	bits []bool
}

func (c *code) size() int {
	return len(c.bits)
}

// EncodeInfo represents info of encoding. This will be used for Decode function.
type EncodeInfo struct {
	bytCodeMap map[byte]code
	Size       int
}

// Encode encodes bytes by huffman coding.
func Encode(out *bytes.Buffer, byts []byte) (*EncodeInfo, error) {
	t := createTree(byts)
	const codeSize = 32 // not optimize
	c := code{
		bits: make([]bool, 0, codeSize),
	}
	e := newEncoder()
	if err := e.encode(t, c); err != nil {
		return nil, err
	}
	sb, err := e.write(out, byts)
	ei := &EncodeInfo{
		bytCodeMap: e.bytCodeMap,
		Size:       sb,
	}
	return ei, err
}

func createTree(byts []byte) *tree {
	bytCntMap := count(byts)
	pq := newPriorityQueue(bytCntMap)
	heap.Init(&pq)
	for pq.Len() > 1 {
		ptl := heap.Pop(&pq).(*ptree)
		ptg := heap.Pop(&pq).(*ptree)
		cnt := ptl.alphabet.count + ptg.alphabet.count
		a := &alphabet{
			count: cnt,
		}
		pt := &ptree{
			tree: tree{
				alphabet: a,
				left:     &ptl.tree,
				right:    &ptg.tree,
			},
		}
		heap.Push(&pq, pt)
	}
	return &heap.Pop(&pq).(*ptree).tree
}

func count(byts []byte) map[byte]int {
	ret := make(map[byte]int, len(byts))
	for _, b := range byts {
		ret[b]++
	}
	return ret
}

type encoder struct {
	bytCodeMap map[byte]code
}

func newEncoder() *encoder {
	m := make(map[byte]code, 256)
	return &encoder{
		bytCodeMap: m,
	}
}

func (e *encoder) encode(t *tree, c code) error {
	if t.left == nil && t.right == nil {
		e.bytCodeMap[t.alphabet.value] = c
		return nil
	}

	c.bits = append(c.bits, false)
	if err := e.encode(t.left, c); err != nil {
		return err
	}

	newBits := make([]bool, len(c.bits), cap(c.bits))
	copy(newBits, c.bits)
	c.bits = newBits
	c.bits[len(c.bits)-1] = true
	if err := e.encode(t.right, c); err != nil {
		return err
	}

	return nil
}

func (e *encoder) write(out *bytes.Buffer, byts []byte) (int, error) {
	var sizeBit int
	w := bitio.NewWriter(out)
	for _, b := range byts {
		c, ok := e.bytCodeMap[b]
		if !ok {
			return sizeBit, fmt.Errorf("not exists %x in encoder.bytCodeMap", b)
		}
		for _, bit := range c.bits {
			if err := w.WriteBool(bit); err != nil {
				w.Close()
				return sizeBit, err
			}
			sizeBit++
		}
	}
	return sizeBit, w.Close()
}

// Decode decodes bytes encoded by huffman coding.
func Decode(out, encData *bytes.Buffer, ei *EncodeInfo) error {
	const datSize = 256 // not optimize
	dat := make([]bool, 0, datSize)
	r := bitio.NewReader(encData)
	for {
		b, err := r.ReadBool()
		if ei.Size == 0 {
			break
		}
		if err == io.EOF {
			break
		}
		if err != nil {
			return err
		}
		dat = append(dat, b)
		for b, c := range ei.bytCodeMap {
			if !cmp(dat, c.bits) {
				continue
			}
			if err := out.WriteByte(b); err != nil {
				return err
			}
			ei.Size -= len(dat)
			dat = make([]bool, 0, datSize)
			break
		}
	}
	return nil
}

func cmp(x, y []bool) bool {
	if len(x) != len(y) {
		return false
	}
	for i, v := range x {
		if v != y[i] {
			return false
		}
	}
	return true
}
