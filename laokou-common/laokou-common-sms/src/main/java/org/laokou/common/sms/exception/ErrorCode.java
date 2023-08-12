package org.laokou.common.sms.exception;

public interface ErrorCode {

	/**
	 * 签名未报备，请检查签名
	 */
	int SMS_SIGNATURE_NOT_REPORTED = 6001;

	/**
	 * 签名不可用，请检查签名
	 */
	int SMS_SIGNATURE_NOT_AVAILABLE = 6002;

	/**
	 * 短信内容包含敏感词，请重新输入
	 */
	int SMS_CONTENT_CONTAINS_SENSITIVE = 6003;

	/**
	 * 短信内容过长，请重新输入
	 */
	int SMS_CONTENT_TOO_LONG = 6004;

	/**
	 * 模板编号不存在，请重新选择模板
	 */
	int SMS_TEMPLATE_ID_NOT_EXIST = 6005;

	/**
	 * 验证未通过，请核对相关信息
	 */
	int SMS_VERIFICATION_FAILED = 6006;

	/**
	 * 手机号错误，请重新输入
	 */
	int MOBILE_ERROR = 2011;

}
