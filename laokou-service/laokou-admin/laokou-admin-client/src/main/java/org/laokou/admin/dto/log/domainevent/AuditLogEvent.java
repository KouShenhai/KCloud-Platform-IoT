/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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
package org.laokou.admin.dto.log.domainevent;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.io.Serial;
import java.io.Serializable;
import java.time.Clock;
import java.time.LocalDateTime;

/**
 * @author laokou
 */
@Setter
@Getter
public class AuditLogEvent extends ApplicationEvent implements Serializable {

	@Serial
	private static final long serialVersionUID = 1532877866226749304L;

	private Long businessId;

	private String auditName;

	private LocalDateTime auditDate;

	private Integer auditStatus;

	private String comment;

	private Long creator;

	private Integer type;

	public AuditLogEvent(Object source) {
		super(source);
	}

	public AuditLogEvent(Object source, Clock clock) {
		super(source, clock);
	}

}
