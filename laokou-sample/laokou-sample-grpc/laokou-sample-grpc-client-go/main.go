package main

import (
	"golang.org/x/net/context"
	"google.golang.org/grpc"
	"google.golang.org/grpc/credentials/insecure"
	"laokou-sample-grpc-client-go/grpc/protoc/pb"
	"log"
)

func main() {
	conn, err := grpc.NewClient("127.0.0.1:1114", grpc.WithTransportCredentials(insecure.NewCredentials()))
	if err != nil {
		log.Fatalf("grpc connect failed => %v\n", err)
	}
	defer conn.Close()
	client := pb.NewUserServiceClient(conn)
	qry := new(pb.UserGetQry)
	qry.Id = 1
	resp, err := client.GetUserById(context.Background(), qry)
	if err != nil {
		log.Fatalf("请求错误 => %v\n", err)
	}
	log.Printf("响应内容 => %v\n", resp)
}
