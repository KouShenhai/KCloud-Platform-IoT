import type {ProColumns} from '@ant-design/pro-components';
import {ProTable} from '@ant-design/pro-components';

export default () => {
	// const valueEnum = {
	// 	0: 'ok',
	// 	1: 'fail'
	// };

	type TableListItem = {
		id: number;
		name: string;
		containers: number;
		creator: string;
		status: string;
		createdAt: number;
		progress: number;
		money: number;
		memo: string;
	};

	const columns: ProColumns<TableListItem>[] = [
		{
			title: '序号',
			dataIndex: 'index',
			valueType: 'indexBorder',
			width: 48,
		},
		{
			title: '应用名称',
			dataIndex: 'name'
		},
		{
			title: '创建者',
			dataIndex: 'creator'
		},
		{
			title: '状态',
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
			title: '备注',
			dataIndex: 'memo',
			ellipsis: true,
			copyable: true,
		},
		{
			title: '操作',
			width: 180,
			key: 'option',
			valueType: 'option',
			render: () => [
				<a key="link">查看</a>
			],
		},
	];

	return (
		<ProTable<TableListItem>
			columns={columns}
			request={(params, sorter, filter) => {
				// 表单搜索项会从 params 传入，传递给后端接口。
				console.log(params, sorter, filter);
				return Promise.resolve({
					data: [],
					success: true,
				});
			}}
			rowKey="id"
			pagination={{
				showQuickJumper: true,
				showSizeChanger: true,
				pageSize: 10,
				onChange: (pageNo, pageSize) => console.log(pageNo, pageSize),
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
