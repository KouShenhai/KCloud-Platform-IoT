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
import org.laokou.common.i18n.common.exception.BizException;
import org.laokou.common.oss.model.BaseOss;
import org.laokou.common.oss.model.FileInfo;
import org.laokou.common.oss.model.MinIO;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

/**
 * @author laokou
 */
public final class MinIOStorage extends AbstractStorage<MinioClient> {

	private final MinIO minIO;

	public MinIOStorage(FileInfo fileInfo, BaseOss baseOss) {
		super(fileInfo);
		minIO = (MinIO) baseOss;
	}

	@Override
	protected MinioClient getObj() {
		return MinioClient.builder()
			.endpoint(this.minIO.getEndpoint())
			.credentials(this.minIO.getAccessKey(), this.minIO.getSecretKey())
			.region(this.minIO.getRegion())
			.build();
	}

	@Override
	protected void checkBucket(MinioClient minioClient) throws ServerException, InsufficientDataException,
			ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException,
			InvalidResponseException, XmlParserException, InternalException {
		String bucketName = this.minIO.getBucketName();
		boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
		if (!isExist) {
			throw new BizException("B_Oss_MinIOBucketNotExist",
					String.format("%s 存储桶 %s 不存在【MinIO】", minIO.getName(), bucketName));
		}
	}

	@Override
	protected void upload(MinioClient minioClient) throws ServerException, InsufficientDataException,
			ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException,
			InvalidResponseException, XmlParserException, InternalException {
		PutObjectArgs objectArgs = PutObjectArgs.builder()
			.bucket(this.minIO.getBucketName())
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
		GetPresignedObjectUrlArgs objectUrlArgs = GetPresignedObjectUrlArgs.builder()
			.bucket(this.minIO.getBucketName())
			.object(fileInfo.name())
			.method(Method.GET)
			.expiry(5, TimeUnit.DAYS)
			.build();
		return minioClient.getPresignedObjectUrl(objectUrlArgs);
	}

}
