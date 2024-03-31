package main

import (
	"log"
	"net"
	"time"
)

func connectUdpServer(network string, ip net.IP, port int) *net.UDPConn {
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

func main() {
	socket := connectUdpServer("udp4", net.IPv4(127, 0, 0, 1), 7655)
	defer func(socket *net.UDPConn) {
		err := socket.Close()
		if err != nil {
			log.Printf("Close failed，error：%s", err)
		}
	}(socket)
	if socket != nil {
		buf := make([]byte, 1024)
		n, addr, err := socket.ReadFromUDP(buf)
		if err != nil {
			log.Printf("Recive failed，error：%s", err)
		}
		log.Printf("Recive message：%s，IP：%s", string(buf[:n]), addr)
	}
}
