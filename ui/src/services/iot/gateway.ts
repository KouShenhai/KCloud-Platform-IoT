/* eslint-disable */
import { request } from '@umijs/max';

/** 修改网关 修改网关 PUT /api/v1/gateways */
export async function modifyGateway(
	body: API.GatewayModifyCmd,
	options?: { [key: string]: any },
) {
	return request<any>('/api-proxy/iot/api/v1/gateways', {
		method: 'PUT',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 保存网关 保存网关 POST /api/v1/gateways */
export async function saveGateway(
	body: API.GatewaySaveCmd,
	requestId: string,
	options?: { [key: string]: any },
) {
	return request<any>('/api-proxy/iot/api/v1/gateways', {
		method: 'POST',
		headers: {
			'request-id': requestId,
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 删除网关 删除网关 DELETE /api/v1/gateways */
export async function removeGateway(
	body: number[],
	options?: { [key: string]: any },
) {
	return request<any>('/api-proxy/iot/api/v1/gateways', {
		method: 'DELETE',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 查看网关详情 查看网关详情 GET /api/v1/gateways/${param0} */
export async function getGatewayById(
	// 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
	params: API.getByIdParams,
	options?: { [key: string]: any },
) {
	const { id: param0, ...queryParams } = params;
	return request<API.Result>(`/api-proxy/iot/api/v1/gateways/${param0}`, {
		method: 'GET',
		params: { ...queryParams },
		...(options || {}),
	});
}

/** 导出网关 导出网关 POST /api/v1/gateways/export */
export async function exportGateway(
	body: API.GatewayExportCmd,
	options?: { [key: string]: any },
) {
	return request<any>('/api-proxy/iot/api/v1/gateways/export', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 导入网关 导入网关 POST /api/v1/gateways/import */
export async function importGateway(
	body: {},
	file?: File[],
	options?: { [key: string]: any },
) {
	const formData = new FormData();

	if (file) {
		file.forEach((f) => formData.append('file', f || ''));
	}

	Object.keys(body).forEach((ele) => {
		const item = (body as any)[ele];

		if (item !== undefined && item !== null) {
			if (typeof item === 'object' && !(item instanceof File)) {
				if (item instanceof Array) {
					item.forEach((f) => formData.append(ele, f || ''));
				} else {
					formData.append(ele, JSON.stringify(item));
				}
			} else {
				formData.append(ele, item);
			}
		}
	});

	return request<any>('/api-proxy/iot/api/v1/gateways/import', {
		method: 'POST',
		data: formData,
		requestType: 'form',
		...(options || {}),
	});
}

/** 分页查询网关列表 分页查询网关列表 POST /api/v1/gateways/page */
export async function pageGateway(
	body: API.GatewayPageQry,
	options?: { [key: string]: any },
) {
	return request<API.Result>('/api-proxy/iot/api/v1/gateways/page', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 重启网关 重启网关 POST /api/v1/gateways/command/reboot */
export async function rebootGateway(
	body: API.GatewayRebootCmd,
	requestId: string,
	options?: { [key: string]: any },
) {
	return request<API.Result>('/api-proxy/iot/api/v1/gateways/command/reboot', {
		method: 'POST',
		headers: {
			'request-id': requestId,
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 读取设备属性 读取设备属性 POST /api/v1/gateways/command/read-property */
export async function readGatewayProperty(
	body: API.GatewayReadPropertyCmd,
	requestId: string,
	options?: { [key: string]: any },
) {
	return request<API.Result>(
		'/api-proxy/iot/api/v1/gateways/command/read-property',
		{
			method: 'POST',
			headers: {
				'request-id': requestId,
				'Content-Type': 'application/json',
			},
			data: body,
			...(options || {}),
		},
	);
}

/** 写入设备属性 写入设备属性 POST /api/v1/gateways/command/write-property */
export async function writeGatewayProperty(
	body: API.GatewayWritePropertyCmd,
	requestId: string,
	options?: { [key: string]: any },
) {
	return request<API.Result>(
		'/api-proxy/iot/api/v1/gateways/command/write-property',
		{
			method: 'POST',
			headers: {
				'request-id': requestId,
				'Content-Type': 'application/json',
			},
			data: body,
			...(options || {}),
		},
	);
}

/** 分页查询网关指令日志 分页查询网关指令日志 POST /api/v1/gateways/command/log/page */
export async function pageGatewayCommandLog(
	body: API.GatewayCommandLogPageQry,
	options?: { [key: string]: any },
) {
	return request<API.Result>('/api-proxy/iot/api/v1/gateways/command/log/page', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}
