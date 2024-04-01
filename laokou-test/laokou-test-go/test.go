package main

import (
	"bufio"
	"context"
	"fmt"
	_amqp "laokou-test-go/common/amqp"
	_db "laokou-test-go/common/db"
	_rdb "laokou-test-go/common/rdb"
	_tcp "laokou-test-go/common/tcp"
	_udp "laokou-test-go/common/udp"
	"log"
	"net"
	"time"
)

func main() {
	fmt.Println("测试...")
	// testAMQP()
	// testDB()
	// testRDB()
	// testTcpServer()
	// testConnectTcpServer()
	// testUdpServer()
	// testConnectUdpServer()
}

func testDB() {
	// https://gorm.io/zh_CN/docs/connecting_to_the_database.html
	ds := _db.DataSource{HOST: "127.0.0.1", PORT: 3306, DATABASE: "kcloud_platform_alibaba_iot", USERNAME: "root", PASSWORD: "laokou123", CHARSET: "utf8mb4", MaxIdleConns: 10, MaxOpenConns: 100, ConnMaxLifetime: time.Hour}
	// 连接mysql
	db, err := _db.InitMysql(ds)
	defer _db.CloseDB(db)
	if db == nil || err != nil {
		log.Printf("连接失败，错误信息：%s", err.Error())
	} else {
		log.Println("连接成功")
	}
}

func testRDB() {
	// https://redis.uptrace.dev/zh/guide/go-redis.html
	_redis := _rdb.Redis{HOST: "127.0.0.1", PORT: 6379, PASSWORD: "laokou123"}
	rdb := _rdb.InitRDB(_redis)
	ctx := context.Background()
	defer _rdb.CloseRDB(rdb)
	result, err := rdb.Get(ctx, "key").Result()
	if err != nil && err.Error() != "redis: nil" {
		log.Printf("连接失败，错误信息：%s\n", err.Error())
	} else {
		if result == "" {
			log.Println("连接成功，key不存在")
		} else {
			log.Println("连接成功，key存在")
		}
	}
}

func testConnectTcpServer() {
	conn := _tcp.ConnectTcpServer("tcp4", net.IPv4(127, 0, 0, 1), 7654)
	if conn != nil {
		defer func(conn net.Conn) {
			err := conn.Close()
			if err != nil {
				log.Printf("Close failed，error：%s", err)
			}
		}(conn)
		for {
			buf := make([]byte, 1024)
			reader := bufio.NewReader(conn)
			n, err := reader.Read(buf)
			if err != nil {
				return
			}
			str := string(buf[:n])
			log.Printf("Recive message：%s", str)
		}
	}
}

func testTcpServer() {
	_tcp.InitTcpServer("tcp4", net.IPv4(0, 0, 0, 0), 7654)
}

func testUdpServer() {
	_udp.InitUdpServer("udp4", net.IPv4(0, 0, 0, 0), 7655)
}

func testConnectUdpServer() {
	socket := _udp.ConnectUdpServer("udp4", net.IPv4(127, 0, 0, 1), 7655)
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

func testAMQP() {
	// https://github.com/rabbitmq/rabbitmq-tutorials/blob/main/go/emit_log_topic.go
	exchange := "laokou_iot_exchange"
	mq := _amqp.AMQP{HOST: "127.0.0.1", PORT: 5672, USERNAME: "root", PASSWORD: "laokou123"}
	payload := "hello world"
	key := "laokou.iot"
	routerKey := "*.iot"
	conn := _amqp.InitAMQP(mq)
	channel := _amqp.InitChannel(conn, exchange)
	queue := _amqp.DeclareQueue(channel)
	_amqp.BindQueue(channel, exchange, routerKey, queue)
	_amqp.ModifyQos(channel)
	defer _amqp.CloseAMQP(conn)
	defer _amqp.CloseChannel(channel)
	_amqp.Send(channel, exchange, key, payload)
	_amqp.Receive(channel, queue)
}
