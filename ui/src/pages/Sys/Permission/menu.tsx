import {
	DrawerForm,
	ProColumns,
	ProFormDigit,
	ProFormSelect,
	ProFormText,
	ProFormTreeSelect
} from '@ant-design/pro-components';
import {ProTable} from '@ant-design/pro-components';
import {treeListV3, removeV3, saveV3, dictTreeListV3} from "@/services/admin/menu";
import {trim} from "@/utils/format";
import {useRef, useState} from "react";
import {TableRowSelection} from "antd/es/table/interface";
import {Button, message, Modal} from 'antd';
import { DeleteOutlined, PlusOutlined } from '@ant-design/icons';
import {v7 as uuidV7} from 'uuid';

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

	const [typeValue, setTypeValue] = useState(0);
	const [modalVisit, setModalVisit] = useState(false);
	const actionRef = useRef();
	const [dataSource, setDataSource] = useState({})

	let param: any

	let ids: number[];

	let title: string = "";

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

	const rowSelection: TableRowSelection<TableColumns> = {
		onChange: (selectedRowKeys) => {
			ids = []
			selectedRowKeys.forEach(item => {
				ids.push(item as number)
			})
		}
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
			<DrawerForm<TableColumns>
				open={modalVisit}
				title={title}
				drawerProps={{
					destroyOnClose: true,
					closable: true,
					maskClosable: true
				}}
				initialValues={dataSource}
				onOpenChange={setModalVisit}
				autoFocusFirstInput
				submitTimeout={2000}
				onFinish={ async (value) => {
					saveV3({co: value}, uuidV7()).then(() => {
						message.success("新增成功").then()
						setModalVisit(false)
					})
				}}>

				<ProFormTreeSelect
					name="pid"
					label="父级"
					debounceTime={3000}
					allowClear={true}
					placeholder={'请选择父级'}
					rules={[{ required: true, message: '请选择父级' }]}
					fieldProps={{
						fieldNames: {
							label: 'name',
							value: 'id'
						},
					}}
					request={async () => {
						const result = await dictTreeListV3({code: 2}).catch(console.log);
						return [{
							id: '0',
							name: '根目录',
							children: result?.data
						}]
					}}
				/>

				<ProFormText
					name="name"
					label="名称"
					placeholder={'请输入名称'}
					rules={[{ required: true, message: '请输入名称' }]}
				/>

				<ProFormText
					name="path"
					label="路径"
					placeholder={'请输入路径'}
					rules={[{ required: true, message: '请输入路径' }]}
				/>

				<ProFormSelect
					name="type"
					label="类型"
					placeholder={'请选择类型'}
					rules={[{ required: true, message: '请选择类型' }]}
					onChange={(value: number) => {
						setTypeValue(value)
					}}
					initialValue={typeValue}
					options={[
						{value: 0, label: '菜单'},
						{value: 1, label: '按钮'}
					]}
				/>

				{typeValue === 1 && (
					<ProFormText
						name="permission"
						label="权限标识"
						placeholder={'请输入权限标识'}
						rules={[{ required: true, message: '请输入权限标识' }]}
					/>
				)}

				<ProFormText
					name="icon"
					label="图标"
					placeholder={'请输入图标'}
				/>

				<ProFormSelect
					name="status"
					label="状态"
					placeholder={'请选择状态'}
					rules={[{ required: true, message: '请选择状态' }]}
					valueEnum={{
						0: '启用',
						1: '禁用'
					}}
				/>

				<ProFormDigit
					name="sort"
					label="排序"
					placeholder={'请输入排序'}
					min={1}
					max={99999}
					rules={[{ required: true, message: '请输入排序' }]}
				/>

			</DrawerForm>
			<ProTable<TableColumns>
				actionRef={actionRef}
				columns={columns}
				request={(params) => {
					// 表单搜索项会从 params 传入，传递给后端接口。
					return treeListV3(getPageQuery(params)).then(res => {
						return Promise.resolve({
							data: res.data,
							success: true,
						});
					})
				}}
				rowSelection={{ ...rowSelection }}
				rowKey="id"
				search={{
					layout: 'vertical',
					defaultCollapsed: true,
				}}
				toolBarRender={
					() => [
						<Button key="save" type="primary" icon={<PlusOutlined />} onClick={() => {
							title = '新增菜单'
							setModalVisit(true)
							setDataSource({
								name: '',
								path: '',
								permission: '',
								sort: 1,
								icon: '',
							})
						}}>
							新增
						</Button>,
						<Button key="remove" type="primary" danger icon={<DeleteOutlined />} onClick={() => {
							Modal.confirm({
								title: '确认删除?',
								content: '您确定要删除吗?',
								okText: '确认',
								cancelText: '取消',
								onOk: async () => {
									// @ts-ignore
									removeV3(ids).then(res => {
										if (res.code === 'OK') {
											message.success("删除成功").then()
											// @ts-ignore
											actionRef?.current?.reload();
										}
									})
								},
							});
						}}>
							删除
						</Button>
					]
				}
				dateFormatter="string"
				toolbar={{
					title: '菜单',
					tooltip: '菜单',
				}}
			/>
		</>
	);
};
