package io.laokou.admin.infrastructure.component.cloud;

import cn.hutool.core.util.IdUtil;
import com.aliyun.oss.OSSClient;
import io.laokou.admin.infrastructure.config.CloudStorageConfig;
import io.laokou.common.exception.CustomException;
import io.laokou.common.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;

/**
 * 阿里云存储
 *
 * @author Kou Shenhai
 */
@Slf4j
public class AliyunCloudStorageService extends AbstractCloudStorageService {

    public AliyunCloudStorageService (CloudStorageConfig config){
        super.cloudStorageConfig = config;
    }

    @Override
    public String upload(InputStream inputStream, String fileName, Long fileSize) throws Exception {
        final String filePath = cloudStorageConfig.getAliyunPrefix() + SEPARATOR + IdUtil.simpleUUID() + FileUtil.getFileSuffix(fileName);
        OSSClient client = new OSSClient(cloudStorageConfig.getAliyunEndPoint(), cloudStorageConfig.getAliyunAccessKeyId(),
                cloudStorageConfig.getAliyunAccessKeySecret());
        try {
            client.putObject(cloudStorageConfig.getAliyunBucketName(), filePath , inputStream);
            client.shutdown();
        } catch (Exception e){
            log.error("错误信息:{}", e.getMessage());
            throw new CustomException(e.getMessage());
        }
        return cloudStorageConfig.getAliyunDomain() + SEPARATOR + filePath;
    }
}
