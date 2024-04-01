package main

import (
	"context"
	"fmt"
	"github.com/redis/go-redis/v9"
	"log"
)

type Redis struct {
	HOST     string
	PORT     int
	PASSWORD string
	DATABASE int
}

func InitRDB(r Redis) *redis.Client {
	rdb := redis.NewClient(&redis.Options{
		Addr:     fmt.Sprintf("%s:%d", r.HOST, r.PORT),
		Password: r.PASSWORD, // 默认密码为空
		DB:       r.DATABASE, // 默认DB为0
	})
	return rdb
}

func CloseRDB(rdb *redis.Client) {
	err := rdb.Close()
	if err != nil {
		return
	}
}

func main() {
	// https://redis.uptrace.dev/zh/guide/go-redis.html
	_redis := Redis{"127.0.0.1", 6379, "laokou123", 0}
	rdb := InitRDB(_redis)
	ctx := context.Background()
	defer CloseRDB(rdb)
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
