package org.laokou.common.i18n.common.exception;

enum ExceptionEnum {

		/**
		 * 无效客户端
		 */
		INVALID_CLIENT(1);

		private final int code;

	ExceptionEnum(int code) {
		this.code = code;
	}

	public static ExceptionEnum getInstance(String code) {
			return ExceptionEnum.valueOf(code);
		}

	}