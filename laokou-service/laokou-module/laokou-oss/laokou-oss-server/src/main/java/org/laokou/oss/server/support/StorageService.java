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
package org.laokou.oss.server.support;
import java.io.InputStream;

/**
 * @author laokou
 */
public interface StorageService<O> {

    /**
     * 创建bucket
     * @param obj
     */
    void createBucket(O obj);

    /**
     * 上传文件
     * @param obj
     * @param readLimit
     * @param size
     * @param fileName
     * @param inputStream
     * @param contentType
     */
    void putObject(O obj, int readLimit, long size, String fileName, InputStream inputStream,String contentType);

    /**
     * 获取地址
     * @param obj
     * @param fileName
     * @return
     */
    String getUrl(O obj,String fileName);

    /**
     * 上传文件
     * @param fileName
     * @param inputStream
     * @param contentType
     * @param size
     * @param readLimit
     * @return
     */
    String upload(int readLimit, long size, String fileName, InputStream inputStream,String contentType);
}
