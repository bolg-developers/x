package main

import (
	"bytes"
	"encoding/hex"
	"fmt"
	"os"

	"github.com/bolg-developers/x/projects/huffman"
)

func main() {
	/* Wikpedia(https://ja.wikipedia.org/wiki/%E3%83%8F%E3%83%95%E3%83%9E%E3%83%B3%E7%AC%A6%E5%8F%B7):
	data: DAEBCBACBBBC
	result:
		1110 110 1111 0 10 0 110 10 0 0 0 10
		==> 1110 1101 1110 1001 1010 0001 0000 0000
		==> ed e9 a1 00
	*/
	fmt.Println("******************** Encode Start ********************")
	out := new(bytes.Buffer)
	byts := []byte{
		'D', 'A', 'E', 'B', 'C',
		'B', 'A', 'C', 'B', 'B',
		'B', 'C',
	}
	i, err := huffman.Encode(out, byts)
	if err != nil {
		fmt.Println("failed to encode:", err)
		os.Exit(1)
	}
	fmt.Printf("%s\n", hex.Dump(out.Bytes()))
	fmt.Printf("encoded data size: %d bit\n", i.Size)

	fmt.Println("******************** Decode Start ********************")
	outd := new(bytes.Buffer)
	if err := huffman.Decode(outd, out, i); err != nil {
		fmt.Println("failed to decode:", err)
		os.Exit(1)
	}
	fmt.Printf("%s\n", hex.Dump(outd.Bytes()))

	/* 道すがら講堂(https://michisugara.jp/archives/2013/huffman.html):
	data: ADBCBABCBBCE
	result: 110 1110 0 10 0 110 0 10 0 0 10 1111
		==> 1101 1100 1001 1001 0001 0111 1000 0000
		==> dc 99 17 80
	*/
	fmt.Println("******************** Encode Start ********************")
	out2 := new(bytes.Buffer)
	byts2 := []byte{
		'A', 'D', 'B', 'C', 'B',
		'A', 'B', 'C', 'B', 'B',
		'C', 'E',
	}
	i2, err := huffman.Encode(out2, byts2)
	if err != nil {
		fmt.Println("failed to encode:", err)
		os.Exit(1)
	}
	fmt.Printf("%s\n", hex.Dump(out2.Bytes()))
	fmt.Printf("encoded data size: %d bit\n", i2.Size)

	fmt.Println("******************** Decode Start ********************")
	outd2 := new(bytes.Buffer)
	if err := huffman.Decode(outd2, out2, i2); err != nil {
		fmt.Println("failed to decode:", err)
		os.Exit(1)
	}
	fmt.Printf("%s\n", hex.Dump(outd2.Bytes()))
}
