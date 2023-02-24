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
package org.laokou.oss.server.controller;
import cn.hutool.core.util.IdUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.utils.FileUtil;
import org.laokou.common.log.entity.SysOssLogDO;
import org.laokou.common.log.service.SysOssLogService;
import org.laokou.common.i18n.core.CustomException;
import org.laokou.common.i18n.core.HttpResult;
import org.laokou.oss.client.vo.UploadVO;
import org.laokou.oss.server.support.StorageFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
/**
 * 对象存储控制器
 * @author laokou
 */
@RestController
@Tag(name = "Oss API",description = "对象存储API")
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor
public class OssApiController {

    private final StorageFactory storageFactory;
    private final SysOssLogService sysOssLogService;

    @PostMapping(value = "/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "对象存储>上传",description = "对象存储>上传")
    public HttpResult<UploadVO> upload(@RequestPart("file") MultipartFile file,@RequestParam("md5")String md5) throws Exception {
        if (file.isEmpty()) {
            throw new CustomException("上传的文件不能为空");
        }
        // 是否上传
        SysOssLogDO logDO = sysOssLogService.getLogByMd5(md5);
        if (null != logDO) {
            return new HttpResult<UploadVO>().ok(UploadVO.builder().url(logDO.getUrl()).build());
        }
        // 文件大小
        long fileSize = file.getSize();
        long file100M = 100 * 1024 * 1024;
        if (fileSize > file100M) {
            throw new CustomException("单个文件上传不能超过100M，请重新选择文件并上传");
        }
        // 文件名
        String fileName = file.getOriginalFilename();
        String newFileName = IdUtil.simpleUUID() + FileUtil.getFileSuffix(fileName);
        // 文件流
        InputStream inputStream = file.getInputStream();
        // 文件类型
        String contentType = file.getContentType();
        int limitRead = (int) (fileSize + 1);
        // 上传文件
        String url = storageFactory.build().upload(limitRead, fileSize, newFileName, inputStream, contentType);
        // 写入文件记录表
        sysOssLogService.insertLog(url,md5,fileName,fileSize);
        return new HttpResult<UploadVO>().ok(UploadVO.builder().url(url).build());
    }

}
