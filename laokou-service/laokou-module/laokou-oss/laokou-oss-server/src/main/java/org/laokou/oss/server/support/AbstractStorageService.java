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
import org.laokou.oss.client.vo.SysOssVO;
import java.io.InputStream;

/**
 * @author laokou
 */
public abstract class AbstractStorageService<O> implements StorageService<O>{
    protected SysOssVO vo;

    public String upload(int limitRead, long size, String fileName, InputStream inputStream, String contentType) {
        // 获取连接对象
        O obj = getObj();
        // 创建bucket
        createBucket(obj);
        // 上传文件
        putObject(obj,limitRead,size,fileName,inputStream,contentType);
        // 获取地址
        return getUrl(obj, fileName);
    }

    /**
     * 获取连接对象
     * @return
     */
    protected abstract O getObj();

}
