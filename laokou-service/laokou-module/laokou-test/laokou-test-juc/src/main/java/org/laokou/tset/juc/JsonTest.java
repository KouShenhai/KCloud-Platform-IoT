/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.laokou.tset.juc;

import org.laokou.common.core.utils.JacksonUtil;

/**
 * @author laokou
 */
public class JsonTest {
    public static void main(String[] args) {
        String[] strings = {"1"};
        String s = JacksonUtil.toJsonStr(strings);
        User user = new User(1,strings);
        System.out.println(s);
        System.out.println(JacksonUtil.toJsonStr(user));
    }
    static class User {
        public User(int age,String[] name) {
            this.age = age;
            this.name = name;
        }

        public String[] getName() {
            return name;
        }

        public void setName(String[] name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        private int age;
        private String[] name;
    }
}
