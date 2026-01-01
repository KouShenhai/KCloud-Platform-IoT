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

package core

import (
	"errors"
	"github.com/DeRuina/timberjack"
	"go.uber.org/zap"
	"go.uber.org/zap/zapcore"
	"log"
	"time"
)

const (
	PROD = "prod"
)

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
	// 是否压缩
	Compress bool `yaml:"compress"`
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

func (c *LogConfig) InitLogger() (*zap.Logger, error) {
	timberjackLogger := &timberjack.Logger{
		Filename:         c.FilePath,
		MaxSize:          c.MaxSize,
		MaxBackups:       c.MaxBackups,
		MaxAge:           c.MaxAge,
		Compress:         c.Compress,
		LocalTime:        c.LocalTime,
		RotationInterval: c.RotationInterval,
		RotateAtMinutes:  c.RotateAtMinutes,
		BackupTimeFormat: c.BackupTimeFormat,
	}
	log.SetOutput(timberjackLogger)
	defer timberjackLogger.Close()
	writeSyncer := zapcore.AddSync(timberjackLogger)
	// 配置日志级别
	levelConfig := zap.NewAtomicLevel()
	level, err := zapcore.ParseLevel(c.Level)
	if err != nil {
		return nil, errors.New("日志级别不存在，请重新配置，错误信息：" + err.Error())
	}
	levelConfig.SetLevel(level)
	// 配置环境
	var encoderConfig zapcore.EncoderConfig
	switch c.Profile {
	case PROD:
		encoderConfig = zap.NewProductionEncoderConfig()
	default:
		encoderConfig = zap.NewDevelopmentEncoderConfig()
	}
	// 设置时间格式
	encoderConfig.EncodeTime = c.customTimeEncoder
	var encoder zapcore.Encoder
	// Json格式
	if c.JsonFormat {
		encoder = zapcore.NewJSONEncoder(encoderConfig)
	} else {
		encoder = zapcore.NewConsoleEncoder(encoderConfig)
	}
	core := zapcore.NewCore(encoder, writeSyncer, levelConfig)
	return zap.New(core, zap.AddCaller()), nil
}

func (c *LogConfig) customTimeEncoder(t time.Time, enc zapcore.PrimitiveArrayEncoder) {
	enc.AppendString(t.Format(c.Pattern))
}
