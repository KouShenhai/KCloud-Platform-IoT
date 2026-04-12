export default (initialState: any) => {
	// 在这里按照初始化数据定义项目中的权限，统一管理
	// 参考文档 https://umijs.org/docs/max/access
	const permissions = initialState?.permissions || [];
	const scopes = initialState?.scopes || [];
	return {
		canMenuGetDetail:
			permissions?.includes('sys:menu:detail') &&
			scopes?.includes('read'),

		canMenuModify:
			permissions?.includes('sys:menu:modify') &&
			scopes?.includes('write'),

		canMenuRemove:
			permissions?.includes('sys:menu:remove') &&
			scopes?.includes('write'),

		canMenuSave:
			permissions?.includes('sys:menu:save') && scopes?.includes('write'),

		canI18nMenuGetDetail:
			permissions?.includes('sys:i18n-menu:detail') &&
			scopes?.includes('read'),

		canI18nMenuModify:
			permissions?.includes('sys:i18n-menu:modify') &&
			scopes?.includes('write'),

		canI18nMenuRemove:
			permissions?.includes('sys:i18n-menu:remove') &&
			scopes?.includes('write'),

		canI18nMenuSave:
			permissions?.includes('sys:i18n-menu:save') && scopes?.includes('write'),

		canDeptGetDetail:
			permissions?.includes('sys:dept:detail') &&
			scopes?.includes('read'),

		canDeptModify:
			permissions?.includes('sys:dept:modify') &&
			scopes?.includes('write'),

		canDeptRemove:
			permissions?.includes('sys:dept:remove') &&
			scopes?.includes('write'),

		canDeptSave:
			permissions?.includes('sys:dept:save') && scopes?.includes('write'),

		canRoleGetDetail:
			permissions?.includes('sys:role:detail') &&
			scopes?.includes('read'),

		canRoleModify:
			permissions?.includes('sys:role:modify') &&
			scopes?.includes('write'),

		canRoleRemove:
			permissions?.includes('sys:role:remove') &&
			scopes?.includes('write'),

		canRoleSave:
			permissions?.includes('sys:role:save') && scopes?.includes('write'),

		canUserGetDetail:
			permissions?.includes('sys:user:detail') &&
			scopes?.includes('read'),

		canUserModify:
			permissions?.includes('sys:user:modify') &&
			scopes?.includes('write'),

		canUserRemove:
			permissions?.includes('sys:user:remove') &&
			scopes?.includes('write'),

		canUserSave:
			permissions?.includes('sys:user:save') && scopes?.includes('write'),

		canOssUpload:
			permissions?.includes('sys:oss:upload') &&
			scopes?.includes('write'),

		canOssGetDetail:
			permissions?.includes('sys:oss:detail') && scopes?.includes('read'),

		canOssModify:
			permissions?.includes('sys:oss:modify') &&
			scopes?.includes('write'),

		canOssRemove:
			permissions?.includes('sys:oss:remove') &&
			scopes?.includes('write'),

		canOssSave:
			permissions?.includes('sys:oss:save') && scopes?.includes('write'),

		canOssLogExport:
			permissions?.includes('sys:oss-log:export') &&
			scopes?.includes('write'),

		canDeviceGetDetail:
			permissions?.includes('iot:device:detail') &&
			scopes?.includes('read'),

		canDeviceModify:
			permissions?.includes('iot:device:modify') &&
			scopes?.includes('write'),

		canDeviceRemove:
			permissions?.includes('iot:device:remove') &&
			scopes?.includes('write'),

		canDeviceSave:
			permissions?.includes('iot:device:save') &&
			scopes?.includes('write'),

		canProductGetDetail:
			permissions?.includes('iot:product:detail') &&
			scopes?.includes('read'),

		canProductModify:
			permissions?.includes('iot:product:modify') &&
			scopes?.includes('write'),

		canProductRemove:
			permissions?.includes('iot:product:remove') &&
			scopes?.includes('write'),

		canProductSave:
			permissions?.includes('iot:product:save') &&
			scopes?.includes('write'),

		canThingModelGetDetail:
			permissions?.includes('iot:thing-model:detail') &&
			scopes?.includes('read'),

		canThingModelModify:
			permissions?.includes('iot:thing-model:modify') &&
			scopes?.includes('write'),

		canThingModelRemove:
			permissions?.includes('iot:thing-model:remove') &&
			scopes?.includes('write'),

		canThingModelSave:
			permissions?.includes('iot:thing-model:save') &&
			scopes?.includes('write'),

		canProductCategoryGetDetail:
			permissions?.includes('iot:product-category:detail') &&
			scopes?.includes('read'),

		canProductCategoryModify:
			permissions?.includes('iot:product-category:modify') &&
			scopes?.includes('write'),

		canProductCategoryRemove:
			permissions?.includes('iot:product-category:remove') &&
			scopes?.includes('write'),

		canProductCategorySave:
			permissions?.includes('iot:product-category:save') &&
			scopes?.includes('write'),

		canOperateLogGetDetail:
			permissions?.includes('sys:operate-log:detail') &&
			scopes?.includes('read'),

		canOperateLogExport:
			permissions?.includes('sys:operate-log:export') &&
			scopes?.includes('write'),

		canNoticeLogGetDetail:
			permissions?.includes('sys:notice-log:detail') &&
			scopes?.includes('read'),

		canNoticeLogExport:
			permissions?.includes('sys:notice-log:export') &&
			scopes?.includes('write'),

		canLoginLogExport:
			permissions?.includes('sys:login-log:export') &&
			scopes?.includes('write'),
	};
};

export function setToken(
	grant_type: string,
	access_token: string,
	refresh_token: string,
	expire_time: number,
): void {
	localStorage.setItem("grant_type", grant_type)
	localStorage.setItem('access_token', access_token);
	localStorage.setItem('refresh_token', refresh_token);
	localStorage.setItem('expire_time', `${expire_time}`);
}

export function getGrantType() {
	return localStorage.getItem('grant_type');
}

export function getAccessToken() {
	return localStorage.getItem('access_token');
}

export function getRefreshToken() {
	return localStorage.getItem('refresh_token');
}

export function getExpireTime() {
	return parseInt(String(localStorage.getItem('expire_time')));
}

export function clearToken() {
	localStorage.removeItem('grant_type');
	localStorage.removeItem('access_token');
	localStorage.removeItem('refresh_token');
	localStorage.removeItem('expire_time');
}
