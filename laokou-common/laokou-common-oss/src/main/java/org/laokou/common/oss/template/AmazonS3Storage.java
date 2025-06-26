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
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.oss.model.BaseOss;
import org.laokou.common.oss.model.FileInfo;

/**
 * @author laokou
 */
public final class AmazonS3Storage extends AbstractStorage<AmazonS3> {

	private volatile org.laokou.common.oss.model.AmazonS3 amazonS3;

	private final Object lock = new Object();

	public AmazonS3Storage(FileInfo fileInfo, BaseOss baseOss) {
		super(fileInfo, baseOss);
	}

	@Override
	protected AmazonS3 getObj() {
		org.laokou.common.oss.model.AmazonS3 s3 = getAmazonS3();
		String accessKey = s3.getAccessKey();
		String secretKey = s3.getSecretKey();
		String region = s3.getRegion();
		String endpoint = s3.getEndpoint();
		Boolean pathStyleAccessEnabled = s3.getPathStyleAccessEnabled() == 1;
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
	protected void createBucket(AmazonS3 amazonS3) {
		org.laokou.common.oss.model.AmazonS3 s3 = getAmazonS3();
		String bucketName = s3.getBucketName();
		// bucketName不存在则新建
		if (!amazonS3.doesBucketExistV2(bucketName)) {
			amazonS3.createBucket(bucketName);
		}
	}

	@Override
	protected void upload(AmazonS3 amazonS3) {
		org.laokou.common.oss.model.AmazonS3 s3 = getAmazonS3();
		String bucketName = s3.getBucketName();
		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentLength(fileInfo.size());
		objectMetadata.setContentType(fileInfo.contentType());
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileInfo.name(), fileInfo.inputStream(),
				objectMetadata);
		putObjectRequest.getRequestClientOptions().setReadLimit((int) (fileInfo.size() + 1));
		amazonS3.putObject(putObjectRequest);
	}

	@Override
	protected String getUrl(AmazonS3 amazonS3) {
		org.laokou.common.oss.model.AmazonS3 s3 = getAmazonS3();
		String bucketName = s3.getBucketName();
		return amazonS3.getUrl(bucketName, fileInfo.name()).toString();
	}

	private org.laokou.common.oss.model.AmazonS3 getAmazonS3() {
		if (ObjectUtils.isNull(amazonS3)) {
			synchronized (lock) {
				if (ObjectUtils.isNull(amazonS3)) {
					if (baseOss instanceof org.laokou.common.oss.model.AmazonS3 s3) {
						return amazonS3 = s3;
					}
					throw new IllegalArgumentException("BaseOss must be an instance of AmazonS3");
				}
			}
		}
		return amazonS3;
	}

}
