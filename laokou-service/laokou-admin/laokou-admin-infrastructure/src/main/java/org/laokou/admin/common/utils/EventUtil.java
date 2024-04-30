/*
 * Copyright (c) 2022-2024 KCloud-Platform-IOT Author or Authors. All Rights Reserved.
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

package org.laokou.admin.common.utils;

import org.springframework.stereotype.Component;

/**
 * 消息事件工具类.
 *
 * @author laokou
 */
@Component
public class EventUtil {

	/*
	 * public MessageEvent toAuditMessageEvent(String assignee, Long id, String name,
	 * String instanceId) { String title = "资源待审批任务提醒"; String content =
	 * String.format("编号为%s，名称为%s的资源需要审批，请及时查看并审批", id, name); return build(title,
	 * content, instanceId, assignee); }
	 *
	 * public MessageEvent toHandleMessageEvent(String assignee, Long id, String name,
	 * String instanceId) { String title = "资源待处理任务提醒"; String content =
	 * String.format("编号为%s，名称为%s的资源需要处理，请及时查看并处理", id, name); return build(title,
	 * content, instanceId, assignee); }
	 *
	 * private MessageEvent build(String title, String content, String instanceId, String
	 * assignee) { MessageEvent event = null; event.setContent(content);
	 * event.setTitle(title); event.setInstanceId(instanceId);
	 * event.setType(MessageTypeEnums.REMIND.ordinal());
	 * event.setReceiver(StringUtil.isEmpty(assignee) ? Collections.emptySet() :
	 * Collections.singleton(assignee)); return event; }
	 */

}
