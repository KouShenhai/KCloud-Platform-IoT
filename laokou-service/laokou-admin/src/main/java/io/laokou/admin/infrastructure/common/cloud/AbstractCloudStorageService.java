package io.laokou.admin.infrastructure.common.cloud;

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
     * 按1M分片
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
