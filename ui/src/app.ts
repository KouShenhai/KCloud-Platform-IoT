// 运行时配置

// 全局初始化数据配置，用于 Layout 用户信息和权限初始化
// 更多信息见文档：https://umijs.org/docs/api/runtime-config#getinitialstate

import {RequestConfig} from "@@/plugin-request/request";
import {message} from "antd";

export async function getInitialState(): Promise<{
	name: string;
	avatar?: string;
}> {
	return {
		name: 'admin',
		avatar: './1.jpg',
	};
}

export const layout = () => {
	return {
		logo: './logo.png',
		menu: {
			locale: false,
		},
	};
};

export const request: RequestConfig = {
	timeout: 1000,
	// other axios options you want
	errorConfig: {
		errorHandler(error: any) {
			const {response} = error;
			if (response && response.status === 500) {
				message.error('服务器内部错误，无法完成请求').then(() => {
				});
			}
		},
		errorThrower() {
		},
	},
	// 请求拦截
	requestInterceptors: [
		(config: any) => {
			let token = localStorage.getItem('token') || '';
			if (token) {
				config.headers.Authorization = 'Bearer ' + token;
			}
			return config;
		},
		(error: any) => {
			return error;
		},
	],
	// 相应拦截
	responseInterceptors: [
		(response: any) => {
			const {status, data} = response;
			if (status === 200 && data.code === undefined) {
				response.data = {code: 'OK', msg: '请求成功', data: data}
			}
			return response;
		},
	],
};
