package main

import (
	"database/sql"
	"fmt"
	"gorm.io/driver/mysql"
	"gorm.io/gorm"
	"log"
	"time"
)

type DataSource struct {
	HOST            string
	PORT            int
	DATABASE        string
	USERNAME        string
	PASSWORD        string
	CHARSET         string
	MaxIdleConns    int
	MaxOpenConns    int
	ConnMaxLifetime time.Duration
}

func InitMysql(ds DataSource) (*sql.DB, error) {
	dns := fmt.Sprintf("%s:%s@tcp(%s:%d)/%s?charset=%s&parseTime=True&loc=Local", ds.USERNAME, ds.PASSWORD, ds.HOST, ds.PORT, ds.DATABASE, ds.CHARSET)
	config := mysql.Config{
		DSN:                       dns,   // DSN data source name
		DefaultStringSize:         256,   // string 类型字段的默认长度
		DisableDatetimePrecision:  true,  // 禁用 datetime 精度，MySQL 5.6 之前的数据库不支持
		DontSupportRenameIndex:    true,  // 重命名索引时采用删除并新建的方式，MySQL 5.7 之前的数据库和 MariaDB 不支持重命名索引
		DontSupportRenameColumn:   true,  // 用 `change` 重命名列，MySQL 8 之前的数据库和 MariaDB 不支持重命名列
		SkipInitializeWithVersion: false, // 根据当前 MySQL 版本自动配置
	}
	db, err := gorm.Open(mysql.New(config), &gorm.Config{
		// 迁移时禁用外键约束
		DisableForeignKeyConstraintWhenMigrating: true,
	})
	if err != nil {
		return nil, err
	} else {
		// 连接池
		sqlDB, er := db.DB()
		// SetMaxIdleConns sets the maximum number of connections in the idle connection pool.
		sqlDB.SetMaxIdleConns(ds.MaxIdleConns)
		// SetMaxOpenConns sets the maximum number of open connections to the database.
		sqlDB.SetMaxOpenConns(ds.MaxOpenConns)
		// SetConnMaxLifetime sets the maximum amount of time a connection may be reused.
		sqlDB.SetConnMaxLifetime(ds.ConnMaxLifetime)
		return sqlDB, er
	}
}

func CloseDB(db *sql.DB) {
	// 延迟调用
	if db != nil {
		err := db.Close()
		if err != nil {
			return
		}
	}
}

func main() {
	// https://gorm.io/zh_CN/docs/connecting_to_the_database.html
	ds := DataSource{"127.0.0.1", 3306, "kcloud_platform_alibaba_iot", "root", "laokou123", "utf8mb4", 10, 100, time.Hour}
	// 连接mysql
	db, err := InitMysql(ds)
	if db == nil || err != nil {
		log.Printf("连接失败，错误信息：%s", err.Error())
	} else {
		log.Println("连接成功")
	}
	defer CloseDB(db)
}
