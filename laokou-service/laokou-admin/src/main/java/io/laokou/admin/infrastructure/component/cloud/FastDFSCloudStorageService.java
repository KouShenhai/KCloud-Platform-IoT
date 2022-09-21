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

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.DefaultGenerateStorageClient;
import io.laokou.admin.infrastructure.config.CloudStorageConfig;
import io.laokou.common.exception.CustomException;
import io.laokou.common.utils.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;

import java.io.InputStream;

/**
 * FastDFS
 * @author  Kou Shenhai
 */
@Slf4j
public class FastDFSCloudStorageService extends AbstractCloudStorageService {

    private static DefaultGenerateStorageClient defaultGenerateStorageClient;

    static {
        defaultGenerateStorageClient = (DefaultGenerateStorageClient) SpringContextUtil.getBean("defaultGenerateStorageClient");
    }

    public FastDFSCloudStorageService (CloudStorageConfig config){
        super.cloudStorageConfig = config;
    }

    @Override
    public String upload(InputStream inputStream, String fileName,Long size) throws Exception {
        StorePath storePath;
        try {
            storePath = defaultGenerateStorageClient.uploadFile(cloudStorageConfig.getFastdfsGroup(), inputStream, inputStream.available(), FilenameUtils.getExtension(fileName));
        } catch (Exception ex) {
            log.error("错误信息:{}", ex.getMessage());
            throw new CustomException(ex.getMessage());
        }
        return cloudStorageConfig.getFastdfsDomain() + SEPARATOR + cloudStorageConfig.getFastdfsGroup() + SEPARATOR + storePath.getPath();
    }

}
