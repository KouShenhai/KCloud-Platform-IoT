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

package main

import (
	"KEdge-Gateway/internal/pkg/config"
	"KEdge-Gateway/internal/pkg/engine"
	"fmt"
	"log"
)

func main() {
	cfg := config.Load()
	if cfg == nil {
		return
	}
	cleanup, err := cfg.Log.InitLogger()
	if err != nil {
		log.Fatalf("init logger failed: %v", err)
	}
	defer cleanup()
	config.Logger.Debug("init logger success")
	luaEngine := engine.NewLuaEngine()

	script := `
	package.cpath = "/usr/lib/x86_64-linux-gnu/lua/5.1/?.so;" .. package.cpath
	local cjson = require("cjson")
	function parse(data)
		local obj = cjson.decode(data)
		return "temp:" .. obj.temp
	end
	`

	luaEngine.Load(script)

	script2 := `
	package.cpath = "/usr/lib/x86_64-linux-gnu/lua/5.1/?.so;" .. package.cpath
	local cjson = require("cjson")
	function parse(data)
		local obj = cjson.decode(data)
		return "temp2222:" .. obj.temp
	end
	`

	luaEngine.Load(script2)

	result, _ := luaEngine.Execute(
		"parse",
		`{"temp":26}`,
	)

	result2, _ := luaEngine.Execute(
		"parse",
		`{"temp":333}`,
	)

	fmt.Println("result =", result)
	fmt.Println("result =", result2)
}
