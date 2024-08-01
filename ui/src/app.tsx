// 运行时配置

// 全局初始化数据配置，用于 Layout 用户信息和权限初始化
// 更多信息见文档：https://umijs.org/docs/api/runtime-config#getinitialstate
import {Dropdown, message, theme} from "antd";
// @ts-ignore
import {history} from 'umi';
import {LogoutOutlined} from "@ant-design/icons";
import {ReactElement, ReactNode, ReactPortal} from "react";
import {logoutV3} from "@/services/auth/logoutsV3Controller";

export async function getInitialState(): Promise<{
	name: string;
	avatar?: string;
}> {
	return {
		name: 'admin',
		avatar: '/1.jpg',
	};
}

export const layout = () => {
	return {
		logo: '/logo.png',
		menu: {
			locale: false,
		},
		layout: 'mix',
		splitMenus: false,
		fixSiderbar: true,
		navTheme: "light",
		contentWidth: "Fluid",
		colorPrimary: "#1677FF",
		fixedHeader: true,
		siderMenuType: "sub",
		avatarProps: {
			src: '/1.jpg',
			size: 'small',
			title: 'admin',
			render: (_props: any, dom: string | number | boolean | ReactElement | Iterable<ReactNode> | ReactPortal | null | undefined) => {
				return (
					<Dropdown
						menu={{
							items: [
								{
									key: 'logout',
									icon: <LogoutOutlined/>,
									label: '注销',
									onClick: async () => {
										const token = localStorage.getItem('access_token')
										// @ts-ignore
										logoutV3({token: token}).then(() => {
											localStorage.removeItem('access_token')
											history.push('/login')
										})
									},
								},
							],
						}}
					>
						{dom}
					</Dropdown>
				);
			}
		},
		token: {
			//菜单的样式配置
			sider: {
				//侧边菜单的配置 ，这里具体看文档
				colorMenuBackground: 'rgb(16 18 26)',
				colorTextMenuTitle: '#ffffff',
				colorMenuItemDivider: 'transparent',
				colorTextMenu: '#dee1f0',
				colorTextMenuSelected: '#ffffff',
				colorTextMenuItemHover: '#ffffff',
				colorTextMenuActive: '#ffffff',
				colorBgMenuItemSelected: '#00c1de',
			},
			header: {
				colorBgHeader: 'rgb(16 18 26)',
				colorHeaderTitle: '#ffffff',
				colorTextMenu: '#dee1f0',
				colorTextRightActionsItem: '#ffffff',
				colorBgRightActionsItemHover: '#00c1de',
				heightLayoutHeader: 60
			}
		},
	};
};

export const request: {
	responseInterceptors: ((response: any) => any)[];
	requestInterceptors: (((config: any) => any) | ((error: any) => any))[];
	timeout: number;
	errorConfig: { errorThrower(): void; errorHandler(error: any): void }
} = {
	timeout: 60000,
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
	// 响应拦截
	responseInterceptors: [
		(response: any) => {
			const {status, data} = response;
			if (status === 200 && data.code === undefined) {
				response.data = {code: 'OK', msg: '请求成功', data: data};
			} else if (status === 200 && data.code !== 'OK') {
				message.error(data.msg).then(() => {
				});
			}
			return response;
		},
	],
};

export const antd: (memo: { theme: { algorithm?: any }; appConfig: { message: { maxCount: number } } }) => {
	theme: { algorithm?: any };
	appConfig: { message: { maxCount: number } }
} = (memo: {
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
