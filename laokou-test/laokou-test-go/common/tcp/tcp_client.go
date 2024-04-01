package tcp

import (
	"log"
	"net"
	"time"
)

func ConnectTcpServer(network string, ip net.IP, port int) net.Conn {
	conn, err := net.DialTCP(network, nil, &net.TCPAddr{
		IP:   ip,
		Port: port,
	})
	if err != nil {
		log.Printf("Client connect failed，error：%s", err)
		return nil
	}
	go func() {
		for {
			// 发送心跳
			_, err = conn.Write([]byte("heart-beat"))
			time.Sleep(5 * time.Second)
			if err != nil {
				return
			}
		}
	}()
	return conn
}
