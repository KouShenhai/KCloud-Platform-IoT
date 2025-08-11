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
	"errors"
	"gopkg.in/yaml.v3"
	"os"
)

type SystemConfig struct {
	Log LogConfig `yaml:"log"`
}

func GetSystemConfig(path string) (*SystemConfig, error) {
	data, err := os.ReadFile(path)
	if err != nil {
		return nil, errors.New("读取配置文件失败，错误信息：" + err.Error())
	}
	sysConfig := &SystemConfig{}
	err = yaml.Unmarshal(data, sysConfig)
	if err != nil {
		return nil, errors.New("配置文件反序列化失败，错误信息：" + err.Error())
	}
	return sysConfig, nil
}
