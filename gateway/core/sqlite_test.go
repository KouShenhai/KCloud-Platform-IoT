package core

import (
	"fmt"
	"testing"
)

func Test_SQLite(t *testing.T) {
	sqLite := NewSQLite("./sqlite.db")

	// 创建库
	err := sqLite.CreateDatabase()
	if err != nil {
		t.Error(err.Error())
	}

	// 创建表
	sql := `
		CREATE TABLE IF NOT EXISTS sys_user (
		id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
		username TEXT NOT NULL
	);
	`
	err = sqLite.ExecuteSql(sql)
	if err != nil {
		t.Error(err.Error())
	}

	// 新增
	sql = `
		INSERT INTO sys_user (id, username) VALUES (?,?);
	`
	_, err = sqLite.Save(sql, 1, "laokou")
	if err != nil {
		t.Error(err.Error())
	}

	// 查询
	sql = `
		SELECT * FROM sys_user;
	`
	rows, err := sqLite.Query(sql)
	if err != nil {
		t.Error(err.Error())
	}
	for rows.Next() {
		var id int
		var username string
		err = rows.Scan(&id, &username)
		if err != nil {
			t.Error(err.Error())
		}
		fmt.Println("查询数据：", id, username)
	}

	// 修改
	sql = `
		UPDATE sys_user SET username = ? WHERE id = ?;
	`
	err = sqLite.Modify(sql, "laokou2", 1)
	if err != nil {
		t.Error(err.Error())
	}

	// 查看
	sql = `
		SELECT * FROM sys_user where id = ?;
	`
	row := sqLite.Get(sql, 1)
	var id int
	var username string
	err = row.Scan(&id, &username)
	if err != nil {
		t.Error(err.Error())
	}
	fmt.Println("查询数据：", id, username)

	// 删除
	sql = `
		DELETE FROM sys_user WHERE id = ?;
	`
	err = sqLite.Remove(sql, 1)
	if err != nil {
		t.Error(err.Error())
	}

	// 删除库
	err = sqLite.DeleteDatabase()
	if err != nil {
		t.Error(err.Error())
	}
}
