import type {ProColumns} from '@ant-design/pro-components';
import {ProTable} from '@ant-design/pro-components';
import {pageV3} from "@/services/admin/loginLog";
import {useState} from "react";

export default () => {

	const valueEnum = {
		0: 'ok',
		1: 'fail'
	};

	type TableColumns = {
		id: number | undefined;
		username: string | undefined;
		ip: string | undefined;
		address: string | undefined;
		browser: string | undefined;
		os: number | undefined;
		status: string | undefined;
		errorMessage: number | undefined;
		type: string | undefined;
		createTime: string | undefined;
	};

	type Param = {
		pageNum: number | undefined;
		pageSize: number | undefined;
		username: string | undefined;
	}

	const loginLogList: TableColumns[] = [];

	const [pageQuery, setPageQuery] = useState<Param>({pageSize: 10, pageNum: 1, username: ""});

	let listLoginLog = async () => {
		return pageV3(pageQuery).then(res => {
			res?.data?.records?.forEach((item: TableColumns) => {
				item.status = valueEnum[item.status as '0'];
				loginLogList.push(item);
			});
			return Promise.resolve({
				data: loginLogList,
				total: res.data.total,
				success: res.code === 'OK',
			});
		})
	}

	const columns: ProColumns<TableColumns>[] = [
		{
			title: '序号',
			dataIndex: 'index',
			valueType: 'indexBorder',
			width: 48,
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
			initialValue: 'ok',
			filters: true,
			onFilter: true,
			valueEnum: {
				ok: {text: '登录成功', status: 'Success'},
				fail: {text: '登录失败', status: 'Error'},
			},
		},
		{
			title: '错误信息',
			dataIndex: 'errorMessage'
		},
		{
			title: '登录类型',
			dataIndex: 'type'
		},
		{
			title: '登录日期',
			dataIndex: 'createTime'
		},
	];

	return (
		<ProTable<TableColumns>
			columns={columns}
			request={(params) => {
				// 表单搜索项会从 params 传入，传递给后端接口。
				setPageQuery({pageSize: params?.pageSize, pageNum: params?.pageSize, username: params?.username})
				return listLoginLog()
			}}
			rowKey="id"
			pagination={{
				showQuickJumper: true,
				showSizeChanger: true,
				pageSize: 10,
				onChange: (pageNum, pageSize) => {
					setPageQuery({pageSize: pageSize, pageNum: pageNum, username: pageQuery.username})
				},
			}}
			search={{
				layout: 'vertical',
				defaultCollapsed: true,
			}}
			dateFormatter="string"
			toolbar={{
				title: '登录日志',
				tooltip: '登录日志',
			}}
		/>
	);
};
