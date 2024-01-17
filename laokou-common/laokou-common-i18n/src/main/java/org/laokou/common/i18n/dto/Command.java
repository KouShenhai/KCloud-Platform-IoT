package org.laokou.common.i18n.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;

/**
 * Command request from Client.
 *
 * @author Frank Zhang 2020.11.13
 *
 */
@Schema(name = "Command", description = "客户端命令请求")
public abstract class Command extends DTO {

	@Serial
	private static final long serialVersionUID = 1L;

}
