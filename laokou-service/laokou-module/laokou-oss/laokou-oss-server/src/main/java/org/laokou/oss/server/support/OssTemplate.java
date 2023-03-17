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

import lombok.RequiredArgsConstructor;
import org.laokou.auth.client.utils.UserUtil;
import org.laokou.common.core.utils.SpringContextUtil;
import org.laokou.common.i18n.core.CustomException;
import org.laokou.common.log.entity.SysOssLogDO;
import org.laokou.common.log.event.OssLogEvent;
import org.laokou.common.log.service.SysOssLogService;
import org.laokou.oss.client.vo.UploadVO;
import org.springframework.stereotype.Component;

import java.io.InputStream;
/**
 * @author laokou
 */
@RequiredArgsConstructor
@Component
public class OssTemplate {

    private final SysOssLogService sysOssLogService;

    private final StorageFactory storageFactory;

    public UploadVO upload(long fileSize, String md5, String fileName, String contentType, InputStream inputStream) {
        // 是否上传
        SysOssLogDO logDO = sysOssLogService.getLogByMd5(md5);
        if (null != logDO) {
            return UploadVO.builder().url(logDO.getUrl()).build();
        }
        long file100M = 100 * 1024 * 1024;
        if (fileSize > file100M) {
            throw new CustomException("单个文件上传不能超过100M，请重新选择文件并上传");
        }
        // 一次读取字节数
        int limitRead = (int) (fileSize + 1);
        // 上传文件
        String url = storageFactory.build().upload(limitRead, fileSize, fileName, inputStream, contentType);
        // 构建事件对象
        OssLogEvent event = buildEvent(url, md5, fileName, fileSize);
        SpringContextUtil.publishEvent(event);
        return UploadVO.builder().url(url).build();
    }

    private OssLogEvent buildEvent(String url, String md5,String fileName,Long fileSize) {
        OssLogEvent ossLogEvent = new OssLogEvent(this);
        ossLogEvent.setUrl(url);
        ossLogEvent.setMd5(md5);
        ossLogEvent.setSourceName(UserUtil.getSourceName());
        ossLogEvent.setFileName(fileName);
        ossLogEvent.setFileSize(fileSize);
        return ossLogEvent;
    }

}
