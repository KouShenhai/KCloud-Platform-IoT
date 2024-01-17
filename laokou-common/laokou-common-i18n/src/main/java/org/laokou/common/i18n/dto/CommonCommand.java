package org.laokou.common.i18n.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;

/**
 * @author laokou
 */
@Schema(name = "CommonCommand", description = "客户端通用命令请求")
public class CommonCommand extends Command {

	@Serial
	private static final long serialVersionUID = 2478790090537077799L;

}
