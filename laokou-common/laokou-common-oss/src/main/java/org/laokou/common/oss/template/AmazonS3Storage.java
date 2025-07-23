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

import org.laokou.common.i18n.common.exception.BizException;
import org.laokou.common.oss.model.BaseOss;
import org.laokou.common.oss.model.FileInfo;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;
import java.time.Duration;

/**
 * @author laokou
 */
public final class AmazonS3Storage extends AbstractStorage<S3Client> {

	private final org.laokou.common.oss.model.AmazonS3 amazonS3;

	public AmazonS3Storage(FileInfo fileInfo, BaseOss baseOss) {
		super(fileInfo);
		this.amazonS3 = (org.laokou.common.oss.model.AmazonS3) baseOss;
	}

	@Override
	protected S3Client getObj() {
		String accessKey = this.amazonS3.getAccessKey();
		String secretKey = this.amazonS3.getSecretKey();
		String region = this.amazonS3.getRegion();
		String endpoint = this.amazonS3.getEndpoint();
		Boolean pathStyleAccessEnabled = this.amazonS3.getPathStyleAccessEnabled() == 1;
		return S3Client.builder()
			.region(Region.of(region))
			.endpointOverride(URI.create(endpoint))
			.credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
			.serviceConfiguration(S3Configuration.builder()
				.pathStyleAccessEnabled(pathStyleAccessEnabled)
				.chunkedEncodingEnabled(false)
				.build())
			.build();
	}

	@Override
	protected void checkBucket(S3Client s3Client) {
		String bucketName = this.amazonS3.getBucketName();
		if (s3Client.listBuckets().buckets().stream().noneMatch(b -> b.name().equals(bucketName))) {
			throw new BizException("B_Oss_AmazonS3BucketNotExist",
					String.format("【AmazonS3】 %s 存储桶 %s 不存在", amazonS3.getName(), bucketName));
		}
	}

	@Override
	protected void upload(S3Client s3Client) {
		String bucketName = this.amazonS3.getBucketName();
		PutObjectRequest putObjectRequest = PutObjectRequest.builder()
			.bucket(bucketName)
			.key(fileInfo.name())
			.contentType(fileInfo.contentType())
			.contentLength(fileInfo.size())
			.build();
		s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(fileInfo.inputStream(), fileInfo.size()));
	}

	@Override
	protected String getUrl(S3Client s3Client) {
		String bucketName = this.amazonS3.getBucketName();
		String accessKey = this.amazonS3.getAccessKey();
		String secretKey = this.amazonS3.getSecretKey();
		String region = this.amazonS3.getRegion();
		Boolean pathStyleAccessEnabled = this.amazonS3.getPathStyleAccessEnabled() == 1;
		try (S3Presigner s3Presigner = S3Presigner.builder()
			.region(Region.of(region))
			.serviceConfiguration(S3Configuration.builder()
				.pathStyleAccessEnabled(pathStyleAccessEnabled)
				.chunkedEncodingEnabled(false)
				.build())
			.endpointOverride(URI.create(this.amazonS3.getEndpoint()))
			.credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
			.build()) {
			return s3Presigner
				.presignGetObject(builder -> builder.signatureDuration(Duration.ofDays(5))
					.getObjectRequest(gor -> gor.bucket(bucketName).key(fileInfo.name())))
				.url()
				.toString();
		}
	}

}
