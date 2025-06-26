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

import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.oss.model.BaseOss;
import org.laokou.common.oss.model.FileInfo;
import org.laokou.common.oss.model.MinIO;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @author laokou
 */
public final class MinIOStorage extends AbstractStorage<MinioClient> {

	private volatile MinIO minIO;

	private final Object lock = new Object();

	public MinIOStorage(FileInfo fileInfo, BaseOss baseOss) {
		super(fileInfo, baseOss);
	}

	@Override
	protected MinioClient getObj() {
		MinIO minIO = getMinIO();
		return MinioClient.builder()
			.endpoint(minIO.getEndpoint())
			.credentials(minIO.getAccessKey(), minIO.getSecretKey())
			.region(minIO.getRegion())
			.build();
	}

	@Override
	protected void createBucket(MinioClient minioClient) throws ServerException, InsufficientDataException,
			ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException,
			InvalidResponseException, XmlParserException, InternalException {
		MinIO minIO = getMinIO();
		String bucketName = minIO.getBucketName();
		boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
		if (!isExist) {
			minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
		}
	}

	@Override
	protected void upload(MinioClient minioClient) throws ServerException, InsufficientDataException,
			ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException,
			InvalidResponseException, XmlParserException, InternalException {
		MinIO minIO = getMinIO();
		PutObjectArgs objectArgs = PutObjectArgs.builder()
			.bucket(minIO.getBucketName())
			.object(fileInfo.name())
			.stream(fileInfo.inputStream(), fileInfo.size(), -1)
			.contentType(fileInfo.contentType())
			.build();
		minioClient.putObject(objectArgs);
	}

	@Override
	protected String getUrl(MinioClient minioClient) throws ServerException, InsufficientDataException,
			ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException,
			InvalidResponseException, XmlParserException, InternalException {
		MinIO minIO = getMinIO();
		GetPresignedObjectUrlArgs objectUrlArgs = GetPresignedObjectUrlArgs.builder()
			.bucket(minIO.getBucketName())
			.object(fileInfo.name())
			.method(Method.GET)
			.build();
		return minioClient.getPresignedObjectUrl(objectUrlArgs);
	}

	private MinIO getMinIO() {
		if (ObjectUtils.isNull(minIO)) {
			synchronized (lock) {
				if (ObjectUtils.isNull(minIO)) {
					if (baseOss instanceof MinIO minio) {
						return this.minIO = minio;
					}
					throw new IllegalArgumentException("BaseOss must be an instance of MinIO");
				}
			}
		}
		return minIO;
	}

}
