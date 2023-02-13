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
package org.laokou.admin.server.infrastructure.feign.oss;
import org.laokou.admin.server.infrastructure.feign.oss.factory.OssApiFeignClientFallbackFactory;
import org.laokou.common.core.constant.ServiceConstant;
import org.laokou.common.core.utils.HttpResult;
import org.laokou.oss.client.vo.UploadVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
/**
 * @author laokou
 */
@FeignClient(value = ServiceConstant.LAOKOU_OSS,path = "/api", fallbackFactory = OssApiFeignClientFallbackFactory.class)
@Service
public interface OssApiFeignClient {

    /**
     * 上传文件
     * @param file
     * @param md5
     * @return
     */
    @PostMapping(value = "/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    HttpResult<UploadVO> upload(@RequestPart("file") MultipartFile file, @RequestParam("md5")String md5);

}
