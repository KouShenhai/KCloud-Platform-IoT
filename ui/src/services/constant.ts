export const LOGIN_TYPE = {
	USERNAME_PASSWORD: 'username_password',
	MOBILE: "mobile",
	MAIL: 'mail',
	AUTHORIZATION_CODE: "authorization_code"
} as const;

export const getLoginType = (code: string) => ({
	[LOGIN_TYPE.USERNAME_PASSWORD]: {
		code: 'username_password',
		text: '用户名密码登录',
		status: 'Processing'
	},
	[LOGIN_TYPE.MOBILE]: {
		code: 'mobile',
		text: '手机号登录',
		status: 'Default'
	},
	[LOGIN_TYPE.MAIL]: {
		code: 'mail',
		text: '邮箱登录',
		status: 'Success'
	},
	[LOGIN_TYPE.AUTHORIZATION_CODE]:{
		code: 'authorization_code',
		text: '授权码登录',
		status: 'Error'
	}
}[code])

export const STATUS = {
	OK: '0',
	FAIL: '1',
} as const;

export const LOGIN_STATUS = {
	OK: '0',
	FAIL: '1',
}

export const getStatus = (code: string) => ({
	[STATUS.OK]: {
		code: 'ok',
		text: '成功',
		status: 'Success'
	},
	[STATUS.FAIL]: {
		code: 'ok',
		text: '失败',
		status: 'Error'
	},
}[code]);

export const getLoginStatus = (code: string) => ({
	[LOGIN_STATUS.OK]: {
		code: '0',
		text: '登录成功',
		status: 'Success'
	},
	[LOGIN_STATUS.FAIL]: {
		code: '1',
		text: '登录失败',
		status: 'Error'
	}
}[code]);
