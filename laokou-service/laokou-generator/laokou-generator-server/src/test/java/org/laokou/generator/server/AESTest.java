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

package org.laokou.generator.server;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.laokou.common.jasypt.utils.AESUtil;
import org.springframework.boot.test.context.SpringBootTest;
/**
 * @author laokou
 */
@Slf4j
@SpringBootTest(classes = GeneratorApplication.class)
public class AESTest {

    @Test
    public void test()  {
        String str = "test";
        String encrypt = AESUtil.encrypt(str);
        String decrypt = AESUtil.decrypt(encrypt);
        System.out.println(encrypt);
        System.out.println(decrypt);
    }

    public static void main(String[] args) {
        String str = "1897443257";
        String encrypt = AESUtil.encrypt(str);
        String decrypt = AESUtil.decrypt(encrypt);
        System.out.println(encrypt);
        System.out.println(decrypt);
    }

}
