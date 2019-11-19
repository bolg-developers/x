// TODO: jwtに置き換える
package bolg

import (
	"errors"
	"fmt"
	"strconv"
	"strings"
)

var (
	ErrInvalidToken = errors.New("invalid token")
)

const mdKeyToken = "bolg-room-token"

func createToken(roomID, playerID int64) string {
	return fmt.Sprintf("%d:%d", roomID, playerID)
}

func parseFromToken(token string) (int64, int64, error) {
	strs := strings.Split(token, ":")
	if len(strs) != 2 {
		return 0, 0, ErrInvalidToken
	}
	rid, err := strconv.ParseInt(strs[0], 10, 64)
	if err != nil {
		return 0, 0, ErrInvalidToken
	}
	pid, err := strconv.ParseInt(strs[1], 10, 64)
	if err != nil {
		return 0, 0, ErrInvalidToken
	}
	return rid, pid, nil
}
