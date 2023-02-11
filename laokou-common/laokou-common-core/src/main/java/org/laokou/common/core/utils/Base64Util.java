/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *   http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laokou.common.core.utils;
import cn.hutool.core.codec.Base64Encoder;
import java.io.IOException;
import java.io.InputStream;
/**
 * @author laokou
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
