run:
	go run cmd/apiserver/apiserver.go

build:
	go build cmd/apiserver/apiserver.go

exec:
	./apiserver &

evans:
	evans pb/bolg.proto

build-win:
	GOOS=windows GOARCH=amd64 go build -o apiserver.exe cmd/apiserver/apiserver.go
	zip apiserver apiserver.exe
	rm -rf apiserver.exe