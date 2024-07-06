// 运行时配置

// 全局初始化数据配置，用于 Layout 用户信息和权限初始化
// 更多信息见文档：https://umijs.org/docs/api/runtime-config#getinitialstate
import {RequestConfig} from "@@/plugin-request/request";
import {message, theme} from "antd";
// @ts-ignore
import {RuntimeAntdConfig} from 'umi';

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
		layout: 'mix',
		splitMenus: true,
		token: {
			//菜单的样式配置
			colorBgAppListIconHover: 'rgba(0,0,0,0.06)',
			colorTextAppListIconHover: 'rgba(255,255,255,0.95)',
			colorTextAppListIcon: 'rgba(255,255,255,0.85)',
			sider: {
				//侧边菜单的配置 ，这里具体看文档
			},
		},
	};
};

export const request: RequestConfig = {
	timeout: 10000,
	// other axios options you want
	errorConfig: {
		errorHandler(error: any) {
			const {response, code} = error;
			let errorMessage;
			if (response && response.data && response.data.error_description !== undefined) {
				errorMessage = response.data.error_description
			}
			if (response && response.status === 500) {
				errorMessage = '服务器内部错误，无法完成请求'
			}
			if (code === 'ERR_BAD_RESPONSE') {
				errorMessage = '网络请求错误，请稍后再试'
			}
			message.error(errorMessage).then(() => {
			});
		},
		errorThrower() {
		},
	},
	// 请求拦截
	requestInterceptors: [
		(config: any) => {
			let token = localStorage.getItem('token') || '';
			if (token) {
				config.headers.Authorization = `Bearer ${token}`
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

export const antd: RuntimeAntdConfig = (memo: {
	theme: { algorithm?: any; }; appConfig: {
		message: {
			// 配置 message 最大显示数，超过限制时，最早的消息会被自动关闭
			maxCount: number;
		};
	};
}) => {
	memo.theme ??= {};
	memo.theme.algorithm = theme.darkAlgorithm; // 配置 antd5 的预设 dark 算法

	memo.appConfig = {
		message: {
			// 配置 message 最大显示数，超过限制时，最早的消息会被自动关闭
			maxCount: 3,
		}
	}

	return memo;
};
