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
package org.laokou.oss.server.support;
import com.amazonaws.services.s3.AmazonS3;
import org.laokou.oss.client.vo.SysOssVO;
import java.io.InputStream;

/**
 * @author laokou
 */
public abstract class AbstractStorageService implements StorageService{

    protected SysOssVO vo;
    public String upload(int limitRead, long size, String fileName, InputStream inputStream, String contentType) {
        return uploadS3(limitRead,size,fileName,inputStream,contentType);
    }

    public String uploadS3(int limitRead, long size, String fileName, InputStream inputStream, String contentType) {
        // 获取AmazonS3
        AmazonS3 amazonS3 = getAmazonS3();
        // 创建bucket
        createBucket(amazonS3);
        // 上传文件
        putObject(amazonS3,limitRead,size,fileName,inputStream,contentType);
        // 获取地址
        return getUrl(amazonS3, fileName);
    }

    /**
     * 获取s3连接
     * @return
     */
    protected abstract AmazonS3 getAmazonS3();

}
