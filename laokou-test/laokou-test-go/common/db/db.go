package db

import (
	"database/sql"
	"fmt"
	_ "github.com/mattn/go-adodb"
	"gorm.io/driver/mysql"
	"gorm.io/driver/sqlserver"
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

func ConnectSqlServerHigh(ds DataSource) (*gorm.DB, *sql.DB, error) {
	// github.com/denisenkom/go-mssqldb
	dsn := fmt.Sprintf("sqlserver://%s:%s@%s:%d?database=%s", ds.USERNAME, ds.PASSWORD, ds.HOST, ds.PORT, ds.DATABASE)
	db, err := gorm.Open(sqlserver.Open(dsn), &gorm.Config{})
	pool, _ := db.DB()
	return db, pool, err
}

func ConnectSqlServerLow(ds DataSource) (*sql.DB, error) {
	config := fmt.Sprintf("Provider=SQLOLEDB;Initial Catalog=%s;Data Source=%s\\MSSQLSERVER", ds.DATABASE, ds.HOST)
	config = fmt.Sprintf("%s,%d;user id=%s;password=%s", config, ds.PORT, ds.USERNAME, ds.PASSWORD)
	return sql.Open("adodb", config)
}

func QueryBySqlServerLow(db *sql.DB, qrySql string) *sql.Rows {
	rows, err := db.Query(qrySql)
	if err != nil {
		log.Printf("Query failed，error：%s", err.Error())
	}
	return rows
}

func ExecBySqlServerLow(db *sql.DB, execSql string) {
	_, err := db.Exec(execSql)
	if err != nil {
		log.Printf("Exec failed，error：%s", err.Error())
	}
}

func ConnectMysql(ds DataSource) (*gorm.DB, *sql.DB, error) {
	dsn := fmt.Sprintf("%s:%s@tcp(%s:%d)/%s?charset=%s&parseTime=True&loc=Local", ds.USERNAME, ds.PASSWORD, ds.HOST, ds.PORT, ds.DATABASE, ds.CHARSET)
	config := mysql.Config{
		DSN:                       dsn,   // DSN data source name
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
	pool := InitConnectPool(db, ds)
	return db, pool, err
}

func InitConnectPool(db *gorm.DB, ds DataSource) *sql.DB {
	// 连接池
	sqlDB, _ := db.DB()
	// SetMaxIdleConns sets the maximum number of connections in the idle connection pool.
	sqlDB.SetMaxIdleConns(ds.MaxIdleConns)
	// SetMaxOpenConns sets the maximum number of open connections to the database.
	sqlDB.SetMaxOpenConns(ds.MaxOpenConns)
	// SetConnMaxLifetime sets the maximum amount of time a connection may be reused.
	sqlDB.SetConnMaxLifetime(ds.ConnMaxLifetime)
	return sqlDB
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
