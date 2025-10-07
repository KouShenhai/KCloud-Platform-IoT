export default (initialState: any) => {
	// 在这里按照初始化数据定义项目中的权限，统一管理
	// 参考文档 https://umijs.org/docs/max/access
	const permissions = initialState?.permissions || []
	return {

		canMenuGetDetail: 					permissions?.includes('sys:menu:detail'),
		canMenuModify: 						permissions?.includes('sys:menu:modify'),
		canMenuRemove: 						permissions?.includes('sys:menu:remove'),
		canMenuSave: 						permissions?.includes('sys:menu:save'),

		canDeptGetDetail: 					permissions?.includes('sys:dept:detail'),
		canDeptModify: 						permissions?.includes('sys:dept:modify'),
		canDeptRemove: 						permissions?.includes('sys:dept:remove'),
		canDeptSave: 						permissions?.includes('sys:dept:save'),

		canRoleGetDetail: 					permissions?.includes('sys:role:detail'),
		canRoleModify: 						permissions?.includes('sys:role:modify'),
		canRoleRemove: 						permissions?.includes('sys:role:remove'),
		canRoleSave: 						permissions?.includes('sys:role:save'),

		canUserGetDetail: 					permissions?.includes('sys:user:detail'),
		canUserModify: 						permissions?.includes('sys:user:modify'),
		canUserRemove: 						permissions?.includes('sys:user:remove'),
		canUserSave: 						permissions?.includes('sys:user:save'),

		canOssUpload: 						permissions?.includes('sys:oss:upload'),
		canOssGetDetail: 					permissions?.includes('sys:oss:detail'),
		canOssModify: 						permissions?.includes('sys:oss:modify'),
		canOssRemove: 						permissions?.includes('sys:oss:remove'),
		canOssSave: 						permissions?.includes('sys:oss:save'),

		canOssLogExport: 					permissions?.includes('sys:oss-log:export'),

		canDeviceGetDetail: 				permissions?.includes('iot:device:detail'),
		canDeviceModify: 					permissions?.includes('iot:device:modify'),
		canDeviceRemove: 					permissions?.includes('iot:device:remove'),
		canDeviceSave: 						permissions?.includes('iot:device:save'),

		canProductGetDetail: 				permissions?.includes('iot:product:detail'),
		canProductModify: 					permissions?.includes('iot:product:modify'),
		canProductRemove: 					permissions?.includes('iot:product:remove'),
		canProductSave: 					permissions?.includes('iot:product:save'),

		canThingModelGetDetail: 			permissions?.includes('iot:thing-model:detail'),
		canThingModelModify: 				permissions?.includes('iot:thing-model:modify'),
		canThingModelRemove: 				permissions?.includes('iot:thing-model:remove'),
		canThingModelSave: 					permissions?.includes('iot:thing-model:save'),

		canProductCategoryGetDetail: 		permissions?.includes('iot:product-category:detail'),
		canProductCategoryModify: 			permissions?.includes('iot:product-category:modify'),
		canProductCategoryRemove: 			permissions?.includes('iot:product-category:remove'),
		canProductCategorySave: 			permissions?.includes('iot:product-category:save'),

		canOperateLogGetDetail: 			permissions?.includes('sys:operate-log:detail'),
		canOperateLogExport: 				permissions?.includes('sys:operate-log:export'),

		canNoticeLogGetDetail: 				permissions?.includes('sys:notice-log:detail'),
		canNoticeLogExport: 				permissions?.includes('sys:notice-log:export'),

		canLoginLogExport: 					permissions?.includes('sys:login-log:export'),

	};
};

export function setToken(
	access_token: string,
	refresh_token: string,
	expire_time: number
): void {
	localStorage.setItem('access_token', access_token);
	localStorage.setItem('refresh_token', refresh_token);
	localStorage.setItem('expire_time', `${expire_time}`);
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
	localStorage.removeItem('access_token');
	localStorage.removeItem('refresh_token');
	localStorage.removeItem('expire_time');
}
