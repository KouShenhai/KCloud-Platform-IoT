declare namespace API {

	type CaptchaParams = {
		uuid: string;
	};

	type LogoutParam = {
		/** 令牌 */
		token?: string;
	};

	type TenantOptionParam = {
		label?: string;
		value?: string;
	}

	type SendCaptchaParam = {
		uuid: string;
		tenantCode: string;
	};

	type SendCaptchaCO = {
		co: SendCaptchaParam;
	}

	type OAuth2Param = {
		username?: string;
		password?: string;
		uuid?: string;
		captcha?: string;
		grant_type?: string;
		tenant_code?: string;
		code?: string;
		mail?: string;
		mobile?: string;
	};

	type RefreshTokenParam = {
		refresh_token?: string;
		grant_type?: string;
	};

	type LoginParam = {
		username?: string;
		password?: string;
		captcha?: string;
		mail?: string;
		mobile?: string;
		mail_captcha?: string;
		mobile_captcha?: string;
		tenant_code?: string
	};

    type Result = {
		/** 状态编码 */
		code?: string;
		/** 响应描述 */
		msg?: string;
		/** 响应结果 */
		data?: any;
		/** 链路ID */
		traceId?: string;
		/** 标签ID */
		spanId?: string;
	};
}
