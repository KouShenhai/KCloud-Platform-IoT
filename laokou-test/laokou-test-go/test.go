package main

import (
	"bufio"
	"context"
	"database/sql"
	"fmt"
	_amqp "laokou-test-go/common/amqp"
	_db "laokou-test-go/common/db"
	_rdb "laokou-test-go/common/rdb"
	_smb "laokou-test-go/common/smb"
	_sys "laokou-test-go/common/sys"
	_tcp "laokou-test-go/common/tcp"
	_udp "laokou-test-go/common/udp"
	"log"
	"net"
	"sync"
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
	// testSqlServer()
	// testSmb()
	testSysInfo()
}

func testSqlServer() {
	// testSqlServer2000()
	// testSqlServer2005()
	// testSqlServer2008()
}

func testSysInfo() {
	info := _sys.GetSysInfo()
	log.Printf("系统信息：%s", info)
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

func testSqlServer2008() {
	ds := _db.DataSource{HOST: "127.0.0.1", PORT: 1433, DATABASE: "kcloud_platform_alibaba_iot", USERNAME: "sa", PASSWORD: "123456"}
	db, err := _db.ConnectSqlServerLow(ds)
	defer _db.CloseDB(db)
	if db == nil || err != nil {
		log.Printf("连接失败，错误信息：%s", err.Error())
	} else {
		log.Println("连接成功")
	}
	// https://www.sjkjc.com/posts/golang-basic-sqlserver

	// Create
	testSqlServerLowCreate(db)
	testSqlServerLowQuery(db)

	// Find
	testSqlServerLowFind(db)

	// Modify
	testSqlServerLowModify(db)
	testSqlServerLowQuery(db)

	// Remove
	testSqlServerLowRemove(db)
	testSqlServerLowQuery(db)
}

func testSqlServer2005() {
	ds := _db.DataSource{HOST: "192.168.111.129", PORT: 1433, DATABASE: "kcloud_platform_alibaba_iot", USERNAME: "sa", PASSWORD: "123456"}
	db, err := _db.ConnectSqlServerLow(ds)
	defer _db.CloseDB(db)
	if db == nil || err != nil {
		log.Printf("连接失败，错误信息：%s", err.Error())
	} else {
		log.Println("连接成功")
	}
	// https://www.sjkjc.com/posts/golang-basic-sqlserver

	// Create
	testSqlServerLowCreate(db)
	testSqlServerLowQuery(db)

	// Find
	testSqlServerLowFind(db)

	// Modify
	testSqlServerLowModify(db)
	testSqlServerLowQuery(db)

	// Remove
	testSqlServerLowRemove(db)
	testSqlServerLowQuery(db)
}

func testSqlServer2000() {
	ds := _db.DataSource{HOST: "192.168.111.128", PORT: 1433, DATABASE: "kcloud_platform_alibaba_iot", USERNAME: "sa", PASSWORD: "123456"}
	db, err := _db.ConnectSqlServerLow(ds)
	defer _db.CloseDB(db)
	if db == nil || err != nil {
		log.Printf("连接失败，错误信息：%s", err.Error())
	} else {
		log.Println("连接成功")
	}
	// https://www.sjkjc.com/posts/golang-basic-sqlserver

	// Create
	testSqlServerLowCreate(db)
	testSqlServerLowQuery(db)

	// Find
	testSqlServerLowFind(db)

	// Modify
	testSqlServerLowModify(db)
	testSqlServerLowQuery(db)

	// Remove
	testSqlServerLowRemove(db)
	testSqlServerLowQuery(db)
}

func testSqlServerLowRemove(db *sql.DB) {
	// 删除
	_db.ExecBySqlServerLow(db, "delete from t_user where username = 'lk'")
	log.Printf("Exec Remove")
}

func testSqlServerLowModify(db *sql.DB) {
	// 修改
	_db.ExecBySqlServerLow(db, "update t_user set username = 'lk' where username = 'laokou123'")
	log.Printf("Exec Modify")
}

func testSqlServerLowFind(db *sql.DB) {
	// 查看
	rows := _db.QueryBySqlServerLow(db, "select username from t_user where username = 'laokou123'")
	for rows.Next() {
		var username string
		err := rows.Scan(&username)
		if err != nil {
			return
		}
		log.Printf("Find：%s", username)
	}
}

func testSqlServerLowCreate(db *sql.DB) {
	// 新增
	_db.ExecBySqlServerLow(db, "insert into t_user(username) values('laokou123')")
	log.Printf("Exec Create")
}

func testSqlServerLowQuery(db *sql.DB) {
	// 查询
	rows := _db.QueryBySqlServerLow(db, "select username from t_user")
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
	typ := "topic"
	conn := _amqp.InitAMQP(mq)
	channel := _amqp.InitChannel(conn, exchange, typ)
	queue := _amqp.DeclareQueue(channel)
	_amqp.BindQueue(channel, exchange, routerKey, queue)
	_amqp.ModifyQos(channel)
	defer _amqp.CloseAMQP(conn)
	defer _amqp.CloseChannel(channel)
	_amqp.Send(channel, exchange, key, payload)
	_amqp.Receive(channel, queue)
}

func testSmb() {
	smb := _smb.Smb{Network: "tcp4", Host: net.IPv4(127, 0, 0, 1), Port: 445, Username: "laokou", Password: "123456", ShareName: "共享文件夹"}
	client := _smb.ConnectSmb(smb)
	defer _smb.CloseSmb(client)
	results := _smb.Search(client, "1", ".*")
	log.Println(results)
	var wg sync.WaitGroup
	ch := make(chan struct{}, 5)
	for i := range results {
		ch <- struct{}{}
		wg.Add(1)
		go func(i int) {
			defer wg.Done()
			_smb.ReadLine(client, results[i])
			<-ch
		}(i)
	}
	wg.Wait()
}
