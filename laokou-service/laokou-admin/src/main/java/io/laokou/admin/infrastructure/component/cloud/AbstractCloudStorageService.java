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
package io.laokou.admin.infrastructure.component.cloud;

import io.laokou.admin.infrastructure.config.CloudStorageConfig;

import java.io.InputStream;

/**
 * 阿里云/腾讯云
 * @author  Kou Shenhai
 */
public abstract class AbstractCloudStorageService {
    /**
     * 配置文件
     */
    CloudStorageConfig cloudStorageConfig;

    /**
     * 按10M分片
     */
    public static final Long chunkSize = 10L * 1024 * 1024;

    public static final String SEPARATOR = "/";

    /**
     * 上传文件
     * @param inputStream 文件流
     * @param fileName 文件名称
     * @param fileSize 文件大小
     * @return
     * @throws Exception
     */
    public abstract String upload(InputStream inputStream,String fileName,Long fileSize) throws Exception;

}
