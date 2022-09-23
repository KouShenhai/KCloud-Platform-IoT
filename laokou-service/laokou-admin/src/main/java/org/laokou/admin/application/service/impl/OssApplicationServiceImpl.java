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
package org.laokou.admin.application.service.impl;

import org.laokou.admin.application.service.OssApplicationService;
import org.laokou.admin.infrastructure.component.cloud.CloudFactory;
import org.laokou.admin.interfaces.vo.UploadVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.InputStream;

@Service
@Slf4j
public class OssApplicationServiceImpl implements OssApplicationService {

    @Autowired
    private CloudFactory cloudFactory;

    @Override
    public UploadVO upload(InputStream inputStream, String fileName, Long fileSize) throws Exception {
        UploadVO vo = new UploadVO();
        String url = cloudFactory.build().upload(inputStream, fileName, fileSize);
        log.info("上传文件地址：{}",url);
        vo.setUrl(url);
        return vo;
    }
}
