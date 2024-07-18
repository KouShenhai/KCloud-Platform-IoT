package org.laokou.common.i18n.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class ApiLog extends DTO {

	private String code;
	private String name;
	private String param;
	private Integer status;
	private String errorMessage;

	public ApiLog(String param, Integer status, String errorMessage) {
		this.code = getApiCode();
		this.name = getApiName();
		this.param = param;
		this.status = status;
		this.errorMessage = errorMessage;
	}

	abstract String getApiCode();

	abstract String getApiName();

}
