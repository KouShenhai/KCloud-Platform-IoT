package core

import (
	"fmt"
	"gorm.io/driver/sqlite"
	"gorm.io/gorm"
	"testing"
)

type User struct {
	ID       uint   `gorm:"primaryKey"`
	Username string `gorm:"column:username;size:50;"`
}

func Test_SQLite(t *testing.T) {
	// 针对网关高并发场景的优化：启用WAL模式并设置超时
	// _busy_timeout=5000 表示在遇到数据库锁定时，等待5000毫秒
	// _journal_mode=WAL 开启预写日志模式，提高并发读写性能
	dbPath := "d:/sqlite3/data/sqlite.db"
	dsn := dbPath + "?_busy_timeout=5000&_journal_mode=WAL"
	db, err := gorm.Open(sqlite.Open(dsn), &gorm.Config{})
	if err != nil {
		fmt.Println("failed to connect database", err.Error())
	}
	// 自动迁移
	err = db.AutoMigrate(&User{})
	if err != nil {
		fmt.Println("自动迁移失败", err.Error())
	}

	// 新增
	db.Create(&User{ID: 1, Username: "laokou"})

	// 查询
	var user User
	db.First(&user, 1)
	fmt.Println("查询结果：", user)

	// 修改
	db.Model(&User{}).Where("id = ?", 1).Update("username", "laokou2").First(&user, 1)
	fmt.Println("修改结果：", user)

	var user2 User
	// 删除
	db.Delete(&User{}, 1).Find(&user2, 1)
	fmt.Println("删除结果：", user2)

}
