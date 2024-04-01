package udp

import (
	"log"
	"net"
	"time"
)

func ConnectUdpServer(network string, ip net.IP, port int) *net.UDPConn {
	socket, err := net.DialUDP(network, nil, &net.UDPAddr{IP: ip, Port: port})
	if err != nil {
		log.Printf("Connect failed，error：%s", err)
		return nil
	}
	// 心跳
	_, err = socket.Write([]byte("heart-beat"))
	time.Sleep(5 * time.Second)
	if err != nil {
		return nil
	}
	return socket
}
