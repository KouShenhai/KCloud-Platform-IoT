package rdb

import (
	"fmt"
	"github.com/redis/go-redis/v9"
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
