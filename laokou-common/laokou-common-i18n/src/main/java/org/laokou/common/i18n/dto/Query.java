package org.laokou.common.i18n.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;

/**
 * Query request from Client.
 *
 * @author Frank Zhang 2020.11.13
 *
 */
@Schema(name = "Query", description = "客户端查询参数")
public abstract class Query extends Command {

	@Serial
	private static final long serialVersionUID = 1L;

}
