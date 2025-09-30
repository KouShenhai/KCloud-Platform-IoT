/* eslint-disable */
import {request} from '@umijs/max';

/** 修改部门 修改部门 PUT /api/v1/depts */
export async function modifyDept(body: API.DeptModifyCmd, options?: { [key: string]: any }) {
	return request<any>('/apis/admin/v1/depts', {
		method: 'PUT',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 保存部门 保存部门 POST /api/v1/depts */
export async function saveDept(body: API.DeptSaveCmd, requestId: string, options?: { [key: string]: any }) {
	return request<any>('/apis/admin/v1/depts', {
		method: 'POST',
		headers: {
			'request-id': requestId,
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 删除部门 删除部门 DELETE /api/v1/depts */
export async function removeDept(body: number[], options?: { [key: string]: any }) {
	return request<any>('/apis/admin/v1/depts', {
		method: 'DELETE',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 查看部门详情 查看部门详情 GET /api/v1/depts/${param0} */
export async function getDeptById(
	// 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
	params: API.getByIdParams,
	options?: { [key: string]: any },
) {
	const {id: param0, ...queryParams} = params;
	return request<API.Result>(`/apis/admin/v1/depts/${param0}`, {
		method: 'GET',
		params: {...queryParams},
		...(options || {}),
	});
}

/** 导出部门 导出部门 POST /api/v1/depts/export */
export async function exportDept(body: API.DeptExportCmd, options?: { [key: string]: any }) {
	return request<any>('/apis/admin/v1/depts/export', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 导入部门 导入部门 POST /api/v1/depts/import */
export async function importDept(body: {}, file?: File[], options?: { [key: string]: any }) {
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

	return request<any>('/apis/admin/v1/depts/import', {
		method: 'POST',
		data: formData,
		requestType: 'form',
		...(options || {}),
	});
}

/** 分页查询部门列表 分页查询部门列表 POST /api/v1/depts/page */
export async function pageDept(body: API.DeptPageQry, options?: { [key: string]: any }) {
	return request<API.Result>('/apis/admin/v1/depts/page', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

export async function listTreeDept(body: any,options?: { [key: string]: any }) {
	return request<API.Result>('/apis/admin/v1/depts/list-tree', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}
