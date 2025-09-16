/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package core

import (
	"database/sql"
	"errors"
	_ "github.com/mattn/go-sqlite3"
	"os"
)

/*

 * @author laokou
 * @date 2025/09/16 20:30

 */

type SQLite struct {
	db     *sql.DB
	dbPath string
}

func NewSQLite(dbPath string) *SQLite {
	return &SQLite{
		dbPath: dbPath,
	}
}

func (s *SQLite) CreateDatabase() error {
	// 针对网关高并发场景的优化：启用WAL模式并设置超时
	// _busy_timeout=5000 表示在遇到数据库锁定时，等待5000毫秒
	// _journal_mode=WAL 开启预写日志模式，提高并发读写性能
	dsn := s.dbPath + "?_busy_timeout=5000&_journal_mode=WAL"
	db, err := sql.Open("sqlite3", dsn)
	if err != nil {
		return errors.New("创建SQLite数据库失败，错误信息：" + err.Error())
	}
	// 验证数据库连接
	if err = db.Ping(); err != nil {
		return errors.New("SQLite数据库连接失败，错误信息：" + err.Error())
	}
	// 为网关应用设置连接池参数
	// 设置最大打开连接数为1，以避免并发写操作导致的 "database is locked" 错误
	db.SetMaxOpenConns(1)
	defer db.Close()
	s.db = db
	return nil
}

func (s *SQLite) DeleteDatabase() error {
	err := os.Remove(s.dbPath)
	if err != nil {
		return errors.New("删除SQLite数据库失败，错误信息：" + err.Error())
	}
	return nil
}

func (s *SQLite) ExecuteSql(sql string) error {
	_, err := s.db.Exec(sql)
	if err != nil {
		return errors.New("执行SQL失败，错误信息：" + err.Error())
	}
	return nil
}

func (s *SQLite) Save(sql string, args ...any) (int64, error) {
	stmt, err := s.db.Prepare(sql)
	if err != nil {
		return 0, errors.New("【保存】 -> 预编译SQL失败，错误信息：" + err.Error())
	}
	defer stmt.Close()
	res, err := stmt.Exec(args)
	if err != nil {
		return 0, errors.New("【保存】 -> 执行SQL失败，错误信息：" + err.Error())
	}
	id, err := res.LastInsertId()
	if err != nil {
		return 0, errors.New("【保存】 -> 获取主键ID失败，错误信息：" + err.Error())
	}
	return id, nil
}

func (s *SQLite) Get(sql string, args ...any) *sql.Row {
	return s.db.QueryRow(sql, args)
}

func (s *SQLite) Query(sql string) (*sql.Rows, error) {
	rows, err := s.db.Query(sql)
	if err != nil {
		return nil, errors.New("【查询】 -> 执行SQL失败，错误信息：" + err.Error())
	}
	defer rows.Close()
	return rows, nil
}

func (s *SQLite) Modify(sql string, args ...any) error {
	stmt, err := s.db.Prepare(sql)
	if err != nil {
		return errors.New("【修改】 -> 预编译SQL失败，错误信息：" + err.Error())
	}
	defer stmt.Close()
	res, err := stmt.Exec(args)
	if err != nil {
		return errors.New("【修改】 -> 执行SQL失败，错误信息：" + err.Error())
	}
	affected, err := res.RowsAffected()
	if err != nil {
		return errors.New("【修改】 -> 获取受影响行数失败，错误信息：" + err.Error())
	}
	if affected == 0 {
		return errors.New("【修改】 -> 修改失败，未找到这条数据")
	}
	return nil
}

func (s *SQLite) Remove(sql string, args ...any) error {
	stmt, err := s.db.Prepare(sql)
	if err != nil {
		return errors.New("【删除】 -> 预编译SQL失败，错误信息：" + err.Error())
	}
	defer stmt.Close()
	_, err = stmt.Exec(args)
	if err != nil {
		return errors.New("【删除】 -> 执行SQL失败，错误信息：" + err.Error())
	}
	return nil
}
