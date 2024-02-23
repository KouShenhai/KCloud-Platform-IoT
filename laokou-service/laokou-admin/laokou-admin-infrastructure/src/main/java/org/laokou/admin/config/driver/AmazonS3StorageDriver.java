/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

package org.laokou.admin.config.driver;

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
import org.jetbrains.annotations.NotNull;
import org.laokou.admin.domain.oss.File;
import org.laokou.admin.domain.oss.Oss;
import org.laokou.common.i18n.utils.DateUtil;

import java.io.ByteArrayInputStream;
import java.net.URL;

/**
 * OSS Amazon S3协议.
 *
 * @author laokou
 */
public class AmazonS3StorageDriver extends AbstractStorageDriver<AmazonS3> {

	public AmazonS3StorageDriver(Oss oss) {
		this.oss = oss;
	}

	/**
	 * @param amazonS3 连接对象
	 */
	@Override
	public void createBucket(AmazonS3 amazonS3) {
		String bucketName = oss.getBucketName();
		// bucketName不存在则新建
		if (!amazonS3.doesBucketExistV2(bucketName)) {
			amazonS3.createBucket(bucketName);
		}
	}

	/**
	 * 上传文件.
	 * @param file 文件对象
	 * @param amazonS3 s3对象
	 */
	@Override
	public void putObject(AmazonS3 amazonS3, File file) {
		// 上传文件
		String bucketName = oss.getBucketName();
		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentLength(file.getSize());
		objectMetadata.setContentType(file.getContentType());
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, getFileName(file.getName()),
				new ByteArrayInputStream(file.getBosCache().toByteArray()), objectMetadata);
		putObjectRequest.getRequestClientOptions().setReadLimit(file.getLimitRead());
		amazonS3.putObject(putObjectRequest);
	}

	/**
	 * 查看URL.
	 * @param amazonS3 连接对象
	 * @param file 文件对象
	 * @return URL
	 */
	@Override
	public String getUrl(AmazonS3 amazonS3, File file) {
		String bucketName = oss.getBucketName();
		URL url = amazonS3.getUrl(bucketName, getFileName(file.getName()));
		return url.toString();
	}

	/**
	 * 查看文件名。
	 * @param fileName 文件名
	 * @return 文件名
	 */
	@Override
	public String getFileName(String fileName) {
		return DateUtil.format(DateUtil.now(), DateUtil.YYYYMMDDHHMMSS) + getFileExt(fileName);
	}

	/**
	 * 获取AmazonS3连接.
	 * @return AmazonS3对象
	 */
	@NotNull
	@Override
	protected AmazonS3 getObj() {
		String accessKey = oss.getAccessKey();
		String secretKey = oss.getSecretKey();
		String region = oss.getRegion();
		String endpoint = oss.getEndpoint();
		Boolean pathStyleAccessEnabled = oss.getPathStyleAccessEnabled() == 1;
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

}
