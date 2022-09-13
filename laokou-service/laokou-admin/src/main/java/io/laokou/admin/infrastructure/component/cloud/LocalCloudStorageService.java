package io.laokou.admin.infrastructure.component.cloud;
import cn.hutool.core.util.IdUtil;
import io.laokou.admin.infrastructure.config.CloudStorageConfig;
import io.laokou.common.utils.FileUtil;
import io.laokou.common.utils.HashUtil;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
/**
 * OSS本地上传
 * @author : Kou Shenhai
 * @date : 2020-06-21 23:42
 */
public class LocalCloudStorageService extends AbstractCloudStorageService{

    private static final String[] NODES = {"node1","node2","node3","node4","node5"};

    public LocalCloudStorageService(CloudStorageConfig config){
        this.cloudStorageConfig = config;
    }

    @Override
    public String upload(InputStream inputStream,String fileName,Long fileSize) throws IOException {
       fileName = IdUtil.simpleUUID() + FileUtil.getFileSuffix(fileName);
       String directoryPath = SEPARATOR + cloudStorageConfig.getLocalPrefix() + SEPARATOR + NODES[HashUtil.getHash(fileName) & (NODES.length - 1)];
       //上传文件
       if (inputStream instanceof ByteArrayInputStream) {
           FileUtil.fileUpload(cloudStorageConfig.getLocalPath(), directoryPath, fileName, inputStream);
       } else {
           FileUtil.nioRandomFileChannelUpload(cloudStorageConfig.getLocalPath(), directoryPath, fileName, inputStream, fileSize, chunkSize);
       }
       return cloudStorageConfig.getLocalDomain() + directoryPath + SEPARATOR + fileName ;
    }

}
