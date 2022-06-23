package io.laokou.admin.infrastructure.config;

import lombok.Data;

import java.io.Serializable;

/**
 * 云存储配置信息
 * @author  Kou Shenhai
 */
@Data
public class CloudStorageConfig implements Serializable {

    /**
     * l：阿里云
     * 2：本地上传
     * 3：FastDFS
     */
    private int type;

    private String aliyunDomain;

    private String aliyunPrefix;

    private String aliyunEndPoint;

    private String aliyunAccessKeyId;

    private String aliyunAccessKeySecret;

    private String aliyunBucketName;

    private String localDomain;

    private String localPath;

    private String localPrefix;

    private String fastdfsDomain;

    private String fastdfsGroup;

}
