package udp

import (
	"log"
	"net"
)

func InitUdpServer(network string, ip net.IP, port int) {
	listen, err := net.ListenUDP(network, &net.UDPAddr{
		IP:   ip,
		Port: port,
	})
	if err != nil {
		log.Printf("Listen failed，error：%s", err)
		return
	}
	HandleUdpConnect(listen)
}

func HandleUdpConnect(listen *net.UDPConn) {
	defer func(listen *net.UDPConn) {
		err := listen.Close()
		if err != nil {
			log.Printf("UDP server close failed，error：%s", err)
		}
	}(listen)
	for {
		buf := make([]byte, 1024)
		n, addr, err := listen.ReadFromUDP(buf)
		if err != nil {
			log.Printf("Read from failed，error：%s", err)
		}
		str := string(buf[:n])
		log.Printf("Recive msg：%s，IP：%s", str, addr.IP.String())
		// 转发
		_, err = listen.WriteToUDP([]byte(str), addr)
		if err != nil {
			log.Printf("Send failed，error：%s", err)
		}
	}
}
