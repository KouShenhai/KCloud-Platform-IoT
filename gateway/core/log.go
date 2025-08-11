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
	"go.uber.org/zap"
	"time"
)

type LogConfig struct {
	// 文件路径
	FilePath string `yaml:"filePath"`

	MaxSize          int       `yaml:"maxSize"`
	MaxBackups       int       `yaml:"maxBackups"`
	MaxAge           int       `yaml:"maxAge"`
	Compress         bool      `yaml:"compress"`
	LocalTime        bool      `yaml:"localTime"`
	RotationInterval time.Time `yaml:"rotationInterval"`
	RotateAtMinutes  []int     `yaml:"rotateAtMinutes"`
	BackupTimeFormat string    `yaml:"backupTimeFormat"`
}

func (c *LogConfig) InitLogger() *zap.Logger {
	return nil
}
