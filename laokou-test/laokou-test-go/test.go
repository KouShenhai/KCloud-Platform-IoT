package main

import (
	"bufio"
	"context"
	"database/sql"
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
	// testMysql()
	// testRDB()
	// testTcpServer()
	// testConnectTcpServer()
	// testUdpServer()
	// testConnectUdpServer()
	testSqlServer()
}

func testSqlServer() {
	testSqlServer2000()
}

func testMysql() {
	// https://gorm.io/zh_CN/docs/connecting_to_the_database.html
	ds := _db.DataSource{HOST: "127.0.0.1", PORT: 3306, DATABASE: "kcloud_platform_alibaba_iot", USERNAME: "root", PASSWORD: "laokou123", CHARSET: "utf8mb4", MaxIdleConns: 10, MaxOpenConns: 100, ConnMaxLifetime: time.Hour}
	// 连接mysql
	db, pool, err := _db.ConnectMysql(ds)
	defer _db.CloseDB(pool)
	if db == nil || err != nil {
		log.Printf("连接失败，错误信息：%s", err.Error())
	} else {
		log.Println("连接成功")
	}
}

func testSqlServer2000() {
	ds := _db.DataSource{HOST: "192.168.111.128", PORT: 1433, DATABASE: "kcloud_platform_alibaba_xot", USERNAME: "sa", PASSWORD: "123456"}
	db, err := _db.ConnectSqlServer2000(ds)
	defer _db.CloseDB(db)
	if db == nil || err != nil {
		log.Printf("连接失败，错误信息：%s", err.Error())
	} else {
		log.Println("连接成功")
	}
	// https://www.sjkjc.com/posts/golang-basic-sqlserver

	// Query
	testSqlServer2000Query(db)

	// Find
	testSqlServer2000Find(db)

	// Create
	testSqlServer2000Create(db)
	testSqlServer2000Query(db)

	// Modify
	testSqlServer2000Modify(db)
	testSqlServer2000Query(db)

	// Remove
	testSqlServer2000Remove(db)
	testSqlServer2000Query(db)
}

func testSqlServer2000Remove(db *sql.DB) {
	// 删除
	_db.ExecBySqlServer2000(db, "delete from t_user where username = 'lk'")
	log.Printf("Exec Remove")
}

func testSqlServer2000Modify(db *sql.DB) {
	// 修改
	_db.ExecBySqlServer2000(db, "update t_user set username = 'lk' where username = 'laokou123'")
	log.Printf("Exec Modify")
}

func testSqlServer2000Find(db *sql.DB) {
	// 查看
	rows := _db.QueryBySqlServer2000(db, "select username from t_user where username = 'laokou'")
	for rows.Next() {
		var username string
		err := rows.Scan(&username)
		if err != nil {
			return
		}
		log.Printf("Find：%s", username)
	}
}

func testSqlServer2000Create(db *sql.DB) {
	// 新增
	_db.ExecBySqlServer2000(db, "insert into t_user(username) values('laokou123')")
	log.Printf("Exec Create")
}

func testSqlServer2000Query(db *sql.DB) {
	// 查询
	rows := _db.QueryBySqlServer2000(db, "select username from t_user")
	for rows.Next() {
		var username string
		err := rows.Scan(&username)
		if err != nil {
			return
		}
		log.Printf("Query：%s", username)
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
				log.Printf("Close failed，error：%s", err.Error())
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
			log.Printf("Close failed，error：%s", err.Error())
		}
	}(socket)
	if socket != nil {
		buf := make([]byte, 1024)
		n, addr, err := socket.ReadFromUDP(buf)
		if err != nil {
			log.Printf("Recive failed，error：%s", err.Error())
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
