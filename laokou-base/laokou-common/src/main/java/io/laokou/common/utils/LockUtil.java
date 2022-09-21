/**
 * Copyright (c) 2022 KCloud-Platform Authors. All Rights Reserved.
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
 */
package io.laokou.common.utils;

import io.laokou.common.exception.CustomException;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * 分布式锁工具类
 * @author Kou Shenhai
 * @version 1.0
 * @date 2021/7/1 0001 上午 8:18
 */
public class LockUtil {

    /**
     * 获取本机网卡地址
     * @return
     */
    public static String getLocalMAC() {
        try {
            InetAddress ia = InetAddress.getLocalHost();
            //获取网络接口对象(即网卡),并得到mac地址，MAC地址存在一个byte数组中
            byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                if (i != 0) {
                    sb.append("-");
                }
                // mac[i] & 0xFF 是为了把byte转化为正整数
                String s = Integer.toHexString(mac[i] & 0xFF);
                sb.append(s.length() == 1 ? 0 + s : s);
            }
            //把字符串所有小写都改成大写成为正规的mac地址并返回
            return sb.toString().toUpperCase().replaceAll("-","");
        } catch (UnknownHostException | SocketException e) {
            throw new CustomException("getLocalMAC error");
        }
    }

    public static String getJvmPid() {
        String pid = ManagementFactory.getRuntimeMXBean().getName();
        int indexOf = pid.indexOf("@");
        if (indexOf != -1) {
            pid = pid.substring(0,indexOf);
            return pid;
        }
        throw new CustomException("ManagementFactory error");
    }

}
