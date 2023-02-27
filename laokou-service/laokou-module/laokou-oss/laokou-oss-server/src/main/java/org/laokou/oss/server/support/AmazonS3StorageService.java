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
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.laokou.oss.client.vo.SysOssVO;
import java.io.InputStream;
import java.net.URL;
/**
 * @author laokou
 */
public class AmazonS3StorageService extends AbstractStorageService<AmazonS3> {

    public AmazonS3StorageService(SysOssVO vo) {
        this.vo = vo;
    }

    @Override
    public void createBucket(AmazonS3 amazonS3) {
        String bucketName = vo.getBucketName();
        // bucketName不存在则新建
        if (!amazonS3.doesBucketExistV2(bucketName)) {
            amazonS3.createBucket(bucketName);
        }
    }

    @Override
    public void putObject(AmazonS3 amazonS3, int readLimit, long size, String fileName, InputStream inputStream,String contentType) {
        // 上传文件
        String bucketName = vo.getBucketName();
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(size);
        objectMetadata.setContentType(contentType);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName,fileName , inputStream, objectMetadata);
        putObjectRequest.getRequestClientOptions().setReadLimit(readLimit);
        amazonS3.putObject(putObjectRequest);
    }

    @Override
    public String getUrl(AmazonS3 amazonS3,String fileName) {
        String bucketName = vo.getBucketName();
        URL url = amazonS3.getUrl(bucketName, fileName);
        return url.toString();
    }

    @Override
    protected AmazonS3 getObj() {
        String accessKey = vo.getAccessKey();
        String secretKey = vo.getSecretKey();
        String region = vo.getRegion();
        String endpoint = vo.getEndpoint();
        Boolean pathStyleAccessEnabled = vo.getPathStyleAccessEnabled() == 1 ? true : false;
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(endpoint, region);
        AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        AWSCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(awsCredentials);
        AmazonS3 amazonS3 = AmazonS3Client.builder()
                .withEndpointConfiguration(endpointConfiguration)
                .withClientConfiguration(clientConfiguration)
                .withCredentials(awsCredentialsProvider)
                .withPathStyleAccessEnabled(pathStyleAccessEnabled)
                .build();
        return amazonS3;
    }
}
