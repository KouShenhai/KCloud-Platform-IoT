/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.auth.domain.model.auth;

/**
 * 日志值对象.
 *
 * @param ip 登录的IP地址
 * @param address 登录的归属地
 * @param browser 登录的浏览器
 * @param os 登录的操作系统
 * @param status 登录状态 0登录成功 1登录失败
 * @param message 登录信息
 */
public record LogV(String os, String ip, String address, String browser, Integer status, String message) {

}
