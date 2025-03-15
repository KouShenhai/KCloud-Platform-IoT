/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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
 *
 */

package org.laokou.common.oss.template;

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
import org.laokou.common.oss.entity.FileInfo;
import org.laokou.common.oss.entity.OssInfo;
import java.net.URL;

/**
 * @author laokou
 */
public class AmazonS3Storage extends AbstractStorage<AmazonS3> {

	public AmazonS3Storage(FileInfo fileInfo, OssInfo ossInfo) {
		super(fileInfo, ossInfo);
	}

	@Override
	protected AmazonS3 getObj() {
		String accessKey = ossInfo.getAccessKey();
		String secretKey = ossInfo.getSecretKey();
		String region = ossInfo.getRegion();
		String endpoint = ossInfo.getEndpoint();
		Boolean pathStyleAccessEnabled = ossInfo.getPathStyleAccessEnabled() == 1;
		ClientConfiguration clientConfiguration = new ClientConfiguration();
		AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(
				endpoint, region);
		AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
		AWSCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(awsCredentials);
		return AmazonS3Client.builder()
			.withEndpointConfiguration(endpointConfiguration)
			.withClientConfiguration(clientConfiguration)
			.withCredentials(awsCredentialsProvider)
			.withPathStyleAccessEnabled(pathStyleAccessEnabled)
			.build();
	}

	@Override
	protected void createBucket(AmazonS3 obj) {
		String bucketName = ossInfo.getBucketName();
		// bucketName不存在则新建
		if (!obj.doesBucketExistV2(bucketName)) {
			obj.createBucket(bucketName);
		}
	}

	@Override
	protected void upload(AmazonS3 obj) {
		String bucketName = ossInfo.getBucketName();
		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentLength(fileInfo.getSize());
		objectMetadata.setContentType(fileInfo.getContentType());
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileInfo.getName(),
				fileInfo.getInputStream(), objectMetadata);
		putObjectRequest.getRequestClientOptions().setReadLimit((int) (fileInfo.getSize() + 1));
		obj.putObject(putObjectRequest);
	}

	@Override
	protected String getUrl(AmazonS3 obj) {
		String bucketName = ossInfo.getBucketName();
		URL url = obj.getUrl(bucketName, fileInfo.getName());
		return url.toString();
	}

}
