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

package org.laokou.admin.domain.resource;

/**
 * @author laokou
 */
public interface Status {

	/**
	 * 待审批
	 */
	int PENDING_APPROVAL = 0;

	/**
	 * 审批中
	 */
	int IN_APPROVAL = 1;

	/**
	 * 驳回审批
	 */
	int REJECT_APPROVAL = -1;

	/**
	 * 通过审批
	 */
	int APPROVED = 2;

	/**
	 * 通过
	 */
	int PASS = 1;

	/**
	 * 拒绝
	 */
	int REFUSE = -1;

}
