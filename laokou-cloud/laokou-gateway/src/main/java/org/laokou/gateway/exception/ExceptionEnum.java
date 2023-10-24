package org.laokou.gateway.exception;

import lombok.Getter;
import org.laokou.common.i18n.common.ErrorCode;
import org.laokou.common.i18n.utils.MessageUtil;

/**
 * @author laokou
 */
public enum ExceptionEnum {

	/**
	 * 无效客户端
	 */
	INVALID_CLIENT(ErrorCode.INVALID_CLIENT, MessageUtil.getMessage(ErrorCode.INVALID_CLIENT));

	@Getter
	private final int code;

	@Getter
	private final String msg;

	ExceptionEnum(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public static ExceptionEnum getInstance(String code) {
		return ExceptionEnum.valueOf(code);
	}

}