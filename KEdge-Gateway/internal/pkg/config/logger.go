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
	"fmt"
	"os"
	"path/filepath"
	"time"

	"github.com/DeRuina/timberjack"
	"go.uber.org/zap"
	"go.uber.org/zap/zapcore"
)

func (c *LogConfig) InitLogger() (*zap.Logger, func() error, error) {
	if c.FilePath == "" {
		return nil, nil, errors.New("log file path is empty")
	}

	dir := filepath.Dir(c.FilePath)
	if err := os.MkdirAll(dir, 0755); err != nil {
		return nil, nil, fmt.Errorf("create log dir failed: %w", err)
	}

	timberjackLogger := &timberjack.Logger{
		Filename:         c.FilePath,
		MaxSize:          c.MaxSize,
		MaxBackups:       c.MaxBackups,
		MaxAge:           c.MaxAge,
		Compression:      c.Compression,
		LocalTime:        c.LocalTime,
		RotationInterval: c.RotationInterval,
		RotateAtMinutes:  c.RotateAtMinutes,
		BackupTimeFormat: c.BackupTimeFormat,
	}

	levelConfig := zap.NewAtomicLevel()
	level, err := zapcore.ParseLevel(c.Level)
	if err != nil {
		return nil, nil, fmt.Errorf("invalid log level: %w", err)
	}
	levelConfig.SetLevel(level)

	var encoderConfig zapcore.EncoderConfig
	if c.Profile == "prod" {
		encoderConfig = zap.NewProductionEncoderConfig()
	} else {
		encoderConfig = zap.NewDevelopmentEncoderConfig()
	}
	encoderConfig.EncodeTime = c.customTimeEncoder

	var encoder zapcore.Encoder
	if c.JsonFormat {
		encoder = zapcore.NewJSONEncoder(encoderConfig)
	} else {
		encoder = zapcore.NewConsoleEncoder(encoderConfig)
	}

	fileWS := zapcore.AddSync(timberjackLogger)
	consoleWS := zapcore.AddSync(os.Stdout)

	core := zapcore.NewTee(
		zapcore.NewCore(encoder, fileWS, levelConfig),
		zapcore.NewCore(encoder, consoleWS, levelConfig),
	)

	logger := zap.New(core, zap.AddCaller())

	cleanup := func() error {
		_ = logger.Sync()
		return timberjackLogger.Close()
	}

	return logger, cleanup, nil
}

func (c *LogConfig) customTimeEncoder(t time.Time, enc zapcore.PrimitiveArrayEncoder) {
	enc.AppendString(t.Format(c.Pattern))
}
