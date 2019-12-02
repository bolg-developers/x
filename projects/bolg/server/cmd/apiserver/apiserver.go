package main

import (
	"log"
	"net"
	"os"

	"github.com/bolg-developers/x/projects/bolg/server/pb"
	"github.com/bolg-developers/x/projects/bolg/server/pkg/bolg"
	"google.golang.org/grpc"
)

func main() {
	logfile, err := os.OpenFile("./bolg.log", os.O_APPEND|os.O_CREATE|os.O_WRONLY, 0666)
	log.SetOutput(logfile)

	port, err := net.Listen("tcp", ":50051")
	if err != nil {
		panic(err)
	}
	defer port.Close()
	srv := grpc.NewServer()
	svc := bolg.NewService()
	pb.RegisterBolgServiceServer(srv, svc)
	log.Println("Server is runnnig")
	if err := srv.Serve(port); err != nil {
		panic(err)
	}
}
