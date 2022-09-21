/**
 * Copyright 2020-2022 Kou Shenhai
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
package io.laokou.admin.infrastructure.config;
import lombok.Data;
/**
 * 云存储配置信息
 * @author  Kou Shenhai
 */
@Data
public class CloudStorageConfig {

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
