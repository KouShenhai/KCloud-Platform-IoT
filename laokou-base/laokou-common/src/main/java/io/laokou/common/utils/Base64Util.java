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
import cn.hutool.core.codec.Base64Encoder;
import java.io.IOException;
import java.io.InputStream;
/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2021/10/13 0013 下午 10:23
 */
public class Base64Util {

    public static String convertBase64(InputStream in) {
        try {
            byte[] buf = new byte[in.available()];
            in.read(buf);
            return Base64Encoder.encode(buf);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
