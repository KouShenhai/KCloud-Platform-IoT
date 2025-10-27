/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.laokou.distributed.identifier.api;

import org.apache.dubbo.common.stream.StreamObserver;
import org.apache.dubbo.remoting.http12.HttpMethods;
import org.apache.dubbo.remoting.http12.rest.Mapping;
import org.apache.dubbo.rpc.stub.annotations.GRequest;
import com.google.protobuf.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.concurrent.CompletableFuture;

public interface DistributedIdentifierServiceI extends org.apache.dubbo.rpc.model.DubboStub {

	String JAVA_SERVICE_NAME = "org.laokou.distributed.identifier.api.DistributedIdentifierServiceI";

	String SERVICE_NAME = "org.laokou.distributed.identifier.api.DistributedIdentifierServiceI";

	org.laokou.distributed.identifier.api.DistributedIdentifierResult generateSnowflake(
			org.laokou.distributed.identifier.api.DistributedIdentifierCmd request);

	CompletableFuture<org.laokou.distributed.identifier.api.DistributedIdentifierResult> generateSnowflakeAsync(
			org.laokou.distributed.identifier.api.DistributedIdentifierCmd request);

	org.laokou.distributed.identifier.api.DistributedIdentifierBatchResult generateSnowflakeBatch(
			org.laokou.distributed.identifier.api.DistributedIdentifierBatchCmd request);

	CompletableFuture<org.laokou.distributed.identifier.api.DistributedIdentifierBatchResult> generateSnowflakeBatchAsync(
			org.laokou.distributed.identifier.api.DistributedIdentifierBatchCmd request);

}
