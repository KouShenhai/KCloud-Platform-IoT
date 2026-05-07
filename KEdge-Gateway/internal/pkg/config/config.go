/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package config

import (
	"errors"
	"flag"
	"log"
	"os"
	"path/filepath"
	"time"

	"gopkg.in/yaml.v3"
)

type Config struct {
	Server ServerConfig `yaml:"server"`
	DB     DBConfig     `yaml:"db"`
	Log    LogConfig    `yaml:"log"`
}

type ServerConfig struct {
	Port int `yaml:"port"`
}

type DBConfig struct {
	Sqlite SqliteConfig `yaml:"sqlite"`
}

type SqliteConfig struct {
	Url string `yaml:"url"`
}

type LogConfig struct {
	// 日志级别
	Level string `yaml:"level"`
	// 环境
	Profile string `yaml:"profile"`
	// 日志格式
	Pattern string `yaml:"pattern"`
	// 日志文件路径
	FilePath string `yaml:"file-path"`
	// 日志最大容量【单位M】
	MaxSize int `yaml:"max-size"`
	// 日志最大数量
	MaxBackups int `yaml:"max-backups"`
	// 日志保留时间【单位天】
	MaxAge int `yaml:"max-age"`
	// 压缩
	Compression string `yaml:"compression"`
	// 本地时间，默认值：false（使用UTC）
	LocalTime bool `yaml:"local-time"`
	// json格式
	JsonFormat bool `yaml:"json-format"`
	// 转换频率
	RotationInterval time.Duration `yaml:"rotation-interval"`
	// 转换时间【分钟】
	RotateAtMinutes []int `yaml:"rotate-at-minutes"`
	// 日志时间格式
	BackupTimeFormat string `yaml:"backup-time-format"`
}

func Load() *Config {
	configPath, err := getConfigPath()
	if err != nil {
		log.Fatalf("failed to get config path: %v", err)
		return nil
	}
	config, err := load(configPath)
	if err != nil {
		log.Fatalf("failed to load config: %v", err)
		return nil
	}
	err = config.check()
	if err != nil {
		log.Fatalf("failed to check config: %v", err)
	}
	return config
}

func (config *Config) check() error {
	return nil
}

func load(path string) (*Config, error) {
	data, err := os.ReadFile(path)
	if err != nil {
		return nil, errors.New("failed to read the file")
	}
	config := &Config{}
	err = yaml.Unmarshal(data, &config)
	if err != nil {
		return nil, errors.New("deserialization failed")
	}
	return config, nil
}

func getConfigPath() (string, error) {
	configDir := flag.String("c", "", "配置文件目录")
	flag.Parse()
	if *configDir == "" {
		return "", errors.New("config directory is empty")
	}
	dir, err := filepath.Abs(*configDir)
	if err != nil {
		return "", errors.New("failed to get absolute path")
	}
	info, err := os.Stat(dir)
	if err != nil || !info.IsDir() {
		return "", errors.New("config directory is not exist")
	}
	return filepath.Join(dir, "config.yml"), nil
}
