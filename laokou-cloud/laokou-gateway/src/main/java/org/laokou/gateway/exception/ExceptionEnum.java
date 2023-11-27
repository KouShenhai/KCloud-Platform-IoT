package org.laokou.gateway.exception;

import lombok.Getter;
import org.laokou.common.i18n.common.ErrorCode;
import org.laokou.common.i18n.utils.MessageUtil;

/**
 * @author laokou
 */
@Getter
public enum ExceptionEnum {

	/**
	 * 无效客户端
	 */
	INVALID_CLIENT(ErrorCode.INVALID_CLIENT, MessageUtil.getMessage(ErrorCode.INVALID_CLIENT)),

	/**
	 * 无效请求
	 */
	INVALID_REQUEST(ErrorCode.INVALID_REQUEST, MessageUtil.getMessage(ErrorCode.INVALID_REQUEST));


	private final int code;

	private final String msg;

	ExceptionEnum(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public static ExceptionEnum getInstance(String code) {
		return ExceptionEnum.valueOf(code);
	}

}