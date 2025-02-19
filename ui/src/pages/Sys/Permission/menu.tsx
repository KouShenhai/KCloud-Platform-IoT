import {
	DrawerForm,
	ProColumns,
	ProFormDigit,
	ProFormSelect,
	ProFormText,
	ProFormTreeSelect
} from '@ant-design/pro-components';
import {ProTable} from '@ant-design/pro-components';
import {treeListV3, removeV3, saveV3, getByIdV3, modifyV3} from "@/services/admin/menu";
import {useEffect, useRef, useState} from "react";
import {TableRowSelection} from "antd/es/table/interface";
import {Button, message, Modal, Space, Switch, Tag} from 'antd';
import { DeleteOutlined, PlusOutlined } from '@ant-design/icons';
import {v7 as uuidV7} from 'uuid';

export default () => {

	type TableColumns = {
		id: number;
		pid: number;
		name: string | undefined;
		path: string | undefined;
		status: number | undefined;
		type: number | undefined;
		permission: string | undefined;
		createTime: string | undefined;
		sort: number | undefined;
	};

	const [readOnly, setReadOnly] = useState(false)
	const [typeValue, setTypeValue] = useState(0);
	const [modalVisit, setModalVisit] = useState(false);
	const actionRef = useRef();
	const [dataSource, setDataSource] = useState({})
	const [ids, setIds] = useState<number[]>([])
	const [title, setTitle] = useState("")
	const [treeList, setTreeList] = useState<any[]>([])

	const getPageQuery = (params: any) => {
		return {
			code: 1,
			type: params?.type,
			status: params?.status,
			params: {
				startTime: params?.startDate ? `${params.startDate} 00:00:00` : undefined,
				endTime: params?.endDate ? `${params.endDate} 23:59:59` : undefined
			}
		}
	}

	const getTreeList = async () => {
		treeListV3({code: 1, type: 0, status: 0}).then(res => {
			setTreeList([{
				id: '0',
				name: '根目录',
				children: res?.data
			}])
		})
	}

	const rowSelection: TableRowSelection<TableColumns> = {
		onChange: (selectedRowKeys) => {
			const ids: number[] = []
			selectedRowKeys.forEach(item => {
				ids.push(item as number)
			})
			setIds(ids)
		}
	};

	useEffect(() => {
		getTreeList().catch(console.log)
	}, []);

	const columns: ProColumns<TableColumns>[] = [
		{
			title: '名称',
			dataIndex: 'name',
			hideInSearch: true,
			width: 200,
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
			hideInSearch: true,
			width: 180
		},
		{
			title: '权限标识',
			dataIndex: 'permission',
			ellipsis: true,
			hideInSearch: true,
			width: 150
		},
		{
			title: '类型',
			dataIndex: 'type',
			hideInTable: true,
			valueEnum: {
				0: {text: '菜单', status: 'Processing'},
				1: {text: '按钮', status: 'Default'},
			},
			ellipsis: true
		},
		{
			disable: true,
			title: '类型',
			dataIndex: 'type',
			search: false,
			renderFormItem: (_, { defaultRender }) => {
				return defaultRender(_);
			},
			render: (_, record) => (
				<Space>
					{record?.type === 0 && (
						<Tag color={'rgb(51 114 253)'} key={'menu'}>
							菜单
						</Tag>
					)}
					{record?.type === 1 && (
						<Tag color={'#fd5251'} key={'button'}>
							按钮
						</Tag>
					)}
				</Space>
			),
		},
		{
			title: '状态',
			dataIndex: 'status',
			hideInTable: true,
			valueEnum: {
				0: {text: '启用', status: 'Success'},
				1: {text: '禁用', status: 'Error'},
			},
			ellipsis: true
		},
		{
			title: '状态',
			dataIndex: 'status',
			search: false,
			render: (_, record) => (
				<Switch checkedChildren="启用" unCheckedChildren="禁用" disabled={true} checked={record?.status === 0} />
			),
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
		},
		{
			title: '操作',
			valueType: 'option',
			key: 'option',
			render: (_, record) => [
				<a key="get"
				   onClick={() => {
					   getByIdV3({id: record?.id}).then(res => {
						   setTitle('查看菜单')
						   setModalVisit(true)
						   setReadOnly(true)
						   const data = res?.data;
						   setTypeValue(data.type)
						   setDataSource(data)
					   })
				   }}
				>
					查看
				</a>,
				<a key="modify"
				   onClick={() => {
					   getByIdV3({id: record?.id}).then(res => {
						   setTitle('修改菜单')
						   setModalVisit(true)
						   setReadOnly(false)
						   const data = res?.data;
						   setTypeValue(data.type)
						   setDataSource(data)
					   })
				   }}
				>
					修改
				</a>,
				<a key="remove" onClick={() => {
					Modal.confirm({
						title: '确认删除?',
						content: '您确定要删除吗?',
						okText: '确认',
						cancelText: '取消',
						onOk: () => {
							removeV3([record?.id]).then(res => {
								if (res.code === 'OK') {
									message.success("删除成功").then()
									// @ts-ignore
									actionRef?.current?.reload();
								}
							})
						}
					})
				}}>
					删除
				</a>
			],
		},
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
				submitter={{
					submitButtonProps: {
						style: {
							display: readOnly ? 'none' : 'inline-block',
						},
					}
				}}
				onFinish={ async (value) => {
					if (value.id === undefined) {
						saveV3({co: value}, uuidV7()).then(res => {
							if (res.code === 'OK') {
								message.success("新增成功").then()
								setModalVisit(false)
								// @ts-ignore
								actionRef?.current?.reload();
								getTreeList().catch(console.log);
							}
						})
					} else {
						modifyV3({co: value}).then(res => {
							if (res.code === 'OK') {
								message.success("修改成功").then()
								setModalVisit(false)
								// @ts-ignore
								actionRef?.current?.reload();
								getTreeList().catch(console.log);
							}
						})
					}
				}}>

				<ProFormText
					name="id"
					label="ID"
					hidden={true}
				/>

				<ProFormTreeSelect
					name="pid"
					label="父级"
					readonly={readOnly}
					allowClear={true}
					placeholder={'请选择父级'}
					rules={[{ required: true, message: '请选择父级' }]}
					fieldProps={{
						fieldNames: {
							label: 'name',
							value: 'id',
							children: 'children'
						},
					}}
					request={async () => {
						return treeList
					}}
				/>

				<ProFormText
					name="name"
					label="名称"
					readonly={readOnly}
					placeholder={'请输入名称'}
					rules={[{ required: true, message: '请输入名称' }]}
				/>

				<ProFormSelect
					name="type"
					label="类型"
					readonly={readOnly}
					placeholder={'请选择类型'}
					rules={[{ required: true, message: '请选择类型' }]}
					onChange={(value: number) => {
						setTypeValue(value)
					}}
					options={[
						{value: 0, label: '菜单'},
						{value: 1, label: '按钮'}
					]}
				/>

				{typeValue === 0 && (
					<ProFormText
						name="path"
						label="路径"
						tooltip={'只对菜单有效【不允许重复】'}
						readonly={readOnly}
						placeholder={'请输入路径'}
						rules={[{ required: true, message: '请输入路径' }]}
					/>
				)}

				{typeValue === 1 && (
					<ProFormText
						name="permission"
						label="权限标识"
						tooltip={'只对按钮有效【不允许重复】'}
						readonly={readOnly}
						placeholder={'请输入权限标识'}
						rules={[{ required: true, message: '请输入权限标识' }]}
					/>
				)}

				{typeValue === 0 && (
					<ProFormText
						name="icon"
						label="图标"
						tooltip={'只支持目录菜单显示图标'}
						readonly={readOnly}
						placeholder={'请输入图标'}
					/>
				)}

				{typeValue === 0 && (
					<ProFormSelect
						name="status"
						label="状态"
						readonly={readOnly}
						placeholder={'请选择状态'}
						rules={[{ required: true, message: '请选择状态' }]}
						options={[
							{value: 0, label: '启用'},
							{value: 1, label: '禁用'}
						]}
					/>
				)}

				<ProFormDigit
					name="sort"
					label="排序"
					readonly={readOnly}
					placeholder={'请输入排序'}
					min={1}
					max={99999}
					rules={[{ required: true, message: '请输入排序' }]}
				/>

			</DrawerForm>
			<ProTable<TableColumns>
				actionRef={actionRef}
				columns={columns}
				request={ async (params) => {
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
							setTitle('新增菜单')
							setTypeValue(0)
							setReadOnly(false)
							setModalVisit(true)
							setDataSource({
								id: undefined,
								name: '',
								path: '',
								permission: '',
								sort: 1,
								icon: '',
								status: 0,
								type: 0,
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
									if (ids.length === 0) {
										message.warning("请至少选择一条数据").then()
										return;
									}
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
