import type {ProColumns} from '@ant-design/pro-components';
import {ProTable} from '@ant-design/pro-components';
import {pageV3} from "@/services/admin/loginLog";
import {Button} from "antd";
import {DeleteOutlined, ExportOutlined} from "@ant-design/icons";

export default () => {

	const statusEnum = {
		0: '0',
		1: '1'
	};

	const typeEnum = {
		'password': 'password',
		'mobile': 'mobile',
		'mail': 'mail',
		'authorization_code': 'authorization_code'
	};

	type TableColumns = {
		id: number | undefined;
		username: string | undefined;
		ip: string | undefined;
		address: string | undefined;
		browser: string | undefined;
		os: string | undefined;
		status: string | undefined;
		errorMessage: string | undefined;
		type: string | undefined;
		createTime: string | undefined;
	};

	const getPageQuery = (params: any) => {
		return {
			pageSize: params?.pageSize,
			pageNum: params?.current,
			username: params?.username,
			ip: params?.ip,
			address: params?.address,
			browser: params?.browser,
			status: params?.status,
			os: params?.os,
			type: params?.type,
			errorMessage: params?.errorMessage,
			params: {
				startDate: params?.startDate,
				endDate: params?.endDate
			}
		};
	}

	const listLoginLog = async (params: any) => {
		return pageV3(getPageQuery(params)).then(res => {
			const list: TableColumns[] = []
			res?.data?.records?.forEach((item: TableColumns) => {
				item.status = statusEnum[item.status as '0'];
				item.type = typeEnum[item.type as 'password'];
				list.push(item);
			});
			return Promise.resolve({
				data: list,
				total: res.data.total,
				success: true,
			});
		})
	}

	const columns: ProColumns<TableColumns>[] = [
		{
			title: '序号',
			dataIndex: 'index',
			valueType: 'indexBorder',
			width: 60,
		},
		{
			title: '用户名',
			dataIndex: 'username'
		},
		{
			title: 'IP地址',
			dataIndex: 'ip'
		},
		{
			title: '归属地',
			dataIndex: 'address'
		},
		{
			title: '浏览器',
			dataIndex: 'browser'
		},
		{
			title: '操作系统',
			dataIndex: 'os'
		},
		{
			title: '登录状态',
			dataIndex: 'status',
			valueEnum: {
				0: {text: '登录成功', status: 'Success'},
				1: {text: '登录失败', status: 'Error'},
			},
		},
		{
			title: '错误信息',
			dataIndex: 'errorMessage'
		},
		{
			title: '登录类型',
			dataIndex: 'type',
			valueEnum: {
				password: {text: '用户密码登录', status: 'Processing'},
				mobile: {text: '手机号登录', status: 'Default'},
				mail: {text: '邮箱登录', status: 'Success'},
				authorization_code: {text: '授权码登录', status: 'Error'}
			},
		},
		{
			title: '登录日期',
			key: 'createTime',
			dataIndex: 'createTime',
			valueType: 'dateTime',
			hideInSearch: true,
		},
		{
			title: '登录日期',
			dataIndex: 'createTime',
			valueType: 'dateRange',
			hideInTable: true,
			search: {
				transform: (value) => {
					return {
						startDate: value[0],
						endDate: value[1],
					};
				},
			}
		}
	];

	return (
		<ProTable<TableColumns>
			columns={columns}
			request={(params) => {
				// 表单搜索项会从 params 传入，传递给后端接口。
				return listLoginLog(params)
			}}
			rowKey="id"
			pagination={{
				showQuickJumper: true,
				showSizeChanger: false,
				pageSize: 10
			}}
			search={{
				layout: 'vertical',
				defaultCollapsed: true,
			}}
			toolBarRender={
				() => [
					<Button key="delete" danger ghost icon={<DeleteOutlined/>}>
						删除
					</Button>,
					<Button key="truncate" type="primary" danger icon={<DeleteOutlined/>}>
						清空
					</Button>,
					<Button key="export" type="primary" ghost icon={<ExportOutlined/>}>
						导出
					</Button>,
					<Button key="exportAll" type="primary" icon={<ExportOutlined/>}>
						导出全部
					</Button>
				]
			}
			dateFormatter="string"
			toolbar={{
				title: '登录日志',
				tooltip: '登录日志',
			}}
		/>
	);
};
