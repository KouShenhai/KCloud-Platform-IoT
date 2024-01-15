/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

package org.laokou.admin.command.cluster.query;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.dto.cluster.ClusterInstanceListQry;
import org.laokou.admin.dto.cluster.clientobject.ClusterInstanceCO;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.nacos.utils.ServiceUtil;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 服务实例列表执行器.
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class ClusterInstanceListQryExe {

	private final ServiceUtil serviceUtil;

	/**
	 * 执行查询服务实例列表.
	 * @param qry 查询服务实例列表参数
	 * @return 服务实例列表
	 */
	public Result<Datas<ClusterInstanceCO>> execute(ClusterInstanceListQry qry) {
		return Result.of(getDatas(qry));
	}

	/**
	 * 服务实例列表分页.
	 * @param qry 查询服务实例列表参数
	 * @return 分页结果
	 */
	private Datas<ClusterInstanceCO> getDatas(ClusterInstanceListQry qry) {
		Integer pageNum = qry.getPageNum();
		Integer pageSize = qry.getPageSize();
		String serviceId = qry.getServiceId();
		List<ServiceInstance> instances = serviceUtil.getInstances(serviceId);
		return new Datas<>(instances.size(),
				instances.stream()
					.map(item -> build(item, serviceId.substring(7)))
					.skip((long) (pageNum - 1) * pageSize)
					.limit(pageSize)
					.toList());
	}

	/**
	 * 构建服务实例视图.
	 * @param instance 服务实例
	 * @param router 路由ID
	 * @return 服务实例视图
	 */
	private ClusterInstanceCO build(ServiceInstance instance, String router) {
		ClusterInstanceCO co = new ClusterInstanceCO();
		co.setHost(instance.getHost());
		co.setPort(instance.getPort());
		co.setRouter(router);
		return co;
	}

}
