/**
 * Copyright 2018
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.laokou.gateway.utils;
/**
 * @author laokou
 */
public class PasswordUtil {

    /**
     * 密码解密
     * @param str 加密字符串
     * @return
     * @throws Exception
     */
    public static String decode(String str) throws Exception {
        return RsaCoder.decryptByPrivateKey(str);
    }
}
