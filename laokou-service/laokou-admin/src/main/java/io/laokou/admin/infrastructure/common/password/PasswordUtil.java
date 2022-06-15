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
package io.laokou.admin.infrastructure.common.password;
/**
 * @author Kou Shenhai
 */
public class PasswordUtil {
    private static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 加密
     * @param str  字符串
     * @return     返回加密字符串
     */
    public static String encode(String str){
        return passwordEncoder.encode(str);
    }


    /**
     * 比较密码是否相等
     * @param str  明文密码
     * @param  password  加密后密码
     * @return     true：成功    false：失败
     */
    public static boolean matches(String str, String password){
        return passwordEncoder.matches(str, password);
    }

}
