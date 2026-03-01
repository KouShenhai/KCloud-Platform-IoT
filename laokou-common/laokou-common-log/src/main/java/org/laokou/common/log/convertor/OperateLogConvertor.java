/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.common.log.convertor;

import com.google.common.net.HttpHeaders;
import jakarta.servlet.http.HttpServletRequest;
import org.laokou.common.context.util.UserUtils;
import org.laokou.common.core.util.AddressUtils;
import org.laokou.common.core.util.IpUtils;
import org.laokou.common.core.util.RequestUtils;
import org.laokou.common.i18n.common.entity.OperateLogE;
import org.laokou.common.i18n.util.StringExtUtils;
import org.laokou.common.log.annotation.OperateLog;
import org.laokou.common.log.factory.DomainFactory;
import org.laokou.common.log.handler.event.OperateEvent;
import org.laokou.common.log.mapper.OperateLogDO;
import org.laokou.common.log.model.OperateLogA;
import org.lionsoul.ip2region.xdb.InetAddressException;

import java.io.IOException;
import java.util.function.Supplier;

public final class OperateLogConvertor {

	private OperateLogConvertor() {
	}

	public static OperateLogE toEntity(OperateLog operateLog, Supplier<String> serviceIdSupplier,
			Supplier<String> profileSupplier) throws InetAddressException, IOException, InterruptedException {
		HttpServletRequest request = RequestUtils.getHttpServletRequest();
		String ip = IpUtils.getIpAddr(request);
		return DomainFactory.createOperateLogE()
			.toBuilder()
			.uri(request.getRequestURI())
			.requestType(request.getMethod())
			.userAgent(request.getHeader(HttpHeaders.USER_AGENT))
			.ip(ip)
			.address(AddressUtils.getRealAddress(ip))
			.serviceAddress(System.getProperty("address"))
			.moduleName(operateLog.module())
			.name(operateLog.operation())
			.serviceId(serviceIdSupplier.get())
			.profile(profileSupplier.get())
			.build();
	}

	public static OperateEvent toDomainEvent(OperateLogA operateLogA) {
		OperateLogE operateLogE = operateLogA.getOperateLogE();
		return OperateEvent.builder()
			.id(operateLogA.getId())
			.name(operateLogE.getName())
			.moduleName(operateLogE.getModuleName())
			.uri(operateLogE.getUri())
			.methodName(operateLogE.getMethodName())
			.requestType(operateLogE.getRequestType())
			.requestParams(operateLogE.getRequestParams())
			.userAgent(operateLogE.getUserAgent())
			.ip(operateLogE.getIp())
			.address(operateLogE.getAddress())
			.status(operateLogE.getStatus())
			.errorMessage(operateLogE.getErrorMessage())
			.costTime(operateLogE.getCostTime())
			.serviceId(operateLogE.getServiceId())
			.serviceAddress(operateLogE.getServiceAddress())
			.profile(operateLogE.getProfile())
			.stackTrace(operateLogE.getStackTrace())
			.createTime(operateLogA.getCreateTime())
			.tenantId(UserUtils.getTenantId())
			.creator(UserUtils.getUserId())
			.deptId(UserUtils.getDeptId())
			.operator(UserUtils.getUserName())
			.build();
	}

	public static OperateLogDO toDataObject(OperateEvent operateEvent) {
		OperateLogDO operateLogDO = new OperateLogDO();
		operateLogDO.setName(operateEvent.getName());
		operateLogDO.setModuleName(operateEvent.getModuleName());
		operateLogDO.setUri(operateEvent.getUri());
		operateLogDO.setRequestType(operateEvent.getRequestType());
		operateLogDO.setUserAgent(operateEvent.getUserAgent());
		operateLogDO.setAddress(operateEvent.getAddress());
		operateLogDO.setOperator(operateEvent.getOperator());
		operateLogDO.setServiceId(operateEvent.getServiceId());
		operateLogDO.setServiceAddress(operateEvent.getServiceAddress());
		operateLogDO.setStackTrace(operateEvent.getStackTrace());
		operateLogDO.setIp(operateEvent.getIp());
		operateLogDO.setProfile(operateEvent.getProfile());
		operateLogDO.setMethodName(operateEvent.getMethodName());
		operateLogDO.setRequestParams(operateEvent.getRequestParams());
		operateLogDO.setStatus(operateEvent.getStatus());
		operateLogDO.setCostTime(operateEvent.getCostTime());
		operateLogDO.setErrorMessage(truncate(operateEvent.getErrorMessage()));
		operateLogDO.setId(operateEvent.getId());
		operateLogDO.setTenantId(operateEvent.getTenantId());
		operateLogDO.setCreator(operateEvent.getCreator());
		operateLogDO.setEditor(operateEvent.getCreator());
		operateLogDO.setCreateTime(operateEvent.getCreateTime());
		operateLogDO.setUpdateTime(operateEvent.getCreateTime());
		operateLogDO.setDeptId(operateEvent.getDeptId());
		return operateLogDO;
	}

	private static String truncate(String str) {
		if (!StringExtUtils.isNotEmpty(str)) {
			return null;
		}
		return str.length() > 2000 ? str.substring(0, 2000) : str;
	}

}
