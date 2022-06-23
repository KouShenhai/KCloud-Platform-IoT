package io.laokou.admin.application.service;

import io.laokou.admin.interfaces.vo.UploadVO;

import java.io.InputStream;

public interface OssApplicationService {

    UploadVO upload(InputStream inputStream,String fileName,Long fileSize) throws Exception;
}
