import {ProColumns} from '@ant-design/pro-components';
import {ProTable} from '@ant-design/pro-components';
import {treeListV3} from "@/services/admin/menu";
import {trim} from "@/utils/format";
import {useRef} from "react";
import {TableRowSelection} from "antd/es/table/interface";

export default () => {

	type TableColumns = {
		id: number;
		name: string | undefined;
		path: string | undefined;
		status: number | undefined;
		type: number | undefined;
		permission: string | undefined;
		createTime: string | undefined;
		sort: number | undefined;
	};

	const actionRef = useRef();

	let param: any

	const getPageQuery = (params: any) => {
		param = {
			pageSize: params?.pageSize,
			pageNum: params?.current,
			pageIndex: params?.pageSize * (params?.current - 1),
			code: 1,
			type: trim(params?.type),
			status: params?.status,
			params: {
				startTime: params?.startDate ? `${params.startDate} 00:00:00` : undefined,
				endTime: params?.endDate ? `${params.endDate} 23:59:59` : undefined
			}
		};
		return param;
	}

	const _list = async (params: any) => {
		return treeListV3(getPageQuery(params)).then(res => {
			return Promise.resolve({
				data: res.data,
				success: true,
			});
		})
	}

	const rowSelection: TableRowSelection<TableColumns> = {
		onChange: (selectedRowKeys, selectedRows) => {
			console.log(`selectedRowKeys: ${selectedRowKeys}`, 'selectedRows: ', selectedRows);
		},
		onSelect: (record, selected, selectedRows) => {
			console.log(record, selected, selectedRows);
		},
	};

	const columns: ProColumns<TableColumns>[] = [
		{
			title: '名称',
			dataIndex: 'name',
			ellipsis: true,
			hideInSearch: true,
		},
		{
			title: '图标',
			dataIndex: 'icon',
			ellipsis: true,
			hideInSearch: true
		},
		{
			title: '路径',
			dataIndex: 'path',
			ellipsis: true,
			hideInSearch: true
		},
		{
			title: '权限标识',
			dataIndex: 'permission',
			ellipsis: true,
			hideInSearch: true,
		},
		{
			title: '类型',
			dataIndex: 'type',
			valueEnum: {
				0: {text: '菜单', status: 'Processing'},
				1: {text: '按钮', status: 'Default'},
			},
			ellipsis: true
		},
		{
			title: '状态',
			dataIndex: 'status',
			valueEnum: {
				0: {text: '启用', status: 'Success'},
				1: {text: '禁用', status: 'Error'},
			},
			ellipsis: true
		},
		{
			title: '排序',
			dataIndex: 'sort',
			hideInSearch: true,
			ellipsis: true
		},
		{
			title: '创建时间',
			key: 'createTime',
			dataIndex: 'createTime',
			valueType: 'dateTime',
			hideInSearch: true,
			width: 160,
			ellipsis: true
		},
		{
			title: '创建时间',
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
		<>
			<ProTable<TableColumns>
				actionRef={actionRef}
				columns={columns}
				request={(params) => {
					// 表单搜索项会从 params 传入，传递给后端接口。
					return _list(params)
				}}
				rowSelection={{ ...rowSelection }}
				rowKey="id"
				search={{
					layout: 'vertical',
					defaultCollapsed: true,
				}}
				dateFormatter="string"
				toolbar={{
					title: '菜单',
					tooltip: '菜单',
				}}
			/>
		</>
	);
};
