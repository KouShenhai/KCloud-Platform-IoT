export default (initialState: API.CaptchaParams) => {
	// 在这里按照初始化数据定义项目中的权限，统一管理
	// 参考文档 https://umijs.org/docs/max/access
	const canSeeAdmin = initialState && initialState.uuid !== 'dontHaveAccess';
	return {
		canSeeAdmin,
	};
};

export function setToken(
	access_token: string,
	refresh_token: string,
	expires_time: number,
): void {
	localStorage.setItem('access_token', access_token);
	localStorage.setItem('refresh_token', refresh_token);
	localStorage.setItem('expires_time', `${expires_time}`);
}

export function getAccessToken() {
	return localStorage.getItem('access_token');
}

export function getRefreshToken() {
	return localStorage.getItem('refresh_token');
}

export function getExpiresTime() {
	return parseInt(String(localStorage.getItem('expires_time')));
}

export function clearToken() {
	localStorage.removeItem('access_token');
	localStorage.removeItem('refresh_token');
	localStorage.removeItem('expires_time');
}
