import {
	ProColumns
} from '@ant-design/pro-components';
import {ProTable} from '@ant-design/pro-components';
import {listTreeV3, removeV3, getByIdV3} from "@/services/admin/menu";
import {useEffect, useRef, useState} from "react";
import {TableRowSelection} from "antd/es/table/interface";
import {Button, message, Modal, Space, Switch, Tag} from 'antd';
import { DeleteOutlined, PlusOutlined } from '@ant-design/icons';
import {MenuDrawer} from "@/pages/Sys/Permission/MenuDrawer";
import {useAccess} from "@@/exports";

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

	const access = useAccess()
	const [readOnly, setReadOnly] = useState(false)
	const [modalVisit, setModalVisit] = useState(false);
	const actionRef = useRef();
	const [dataSource, setDataSource] = useState<any>({})
	const [ids, setIds] = useState<number[]>([])
	const [title, setTitle] = useState("")
	const [typeValue, setTypeValue] = useState(0);
	const [treeList, setTreeList] = useState<any[]>([])

	const getListTreeQueryParam = (params: any) => {
		return {
			code: 1,
			type: params?.typeValue,
			status: params?.statusValue,
			params: {
				startTime: params?.startDateValue ? `${params.startDateValue} 00:00:00` : undefined,
				endTime: params?.endDateValue ? `${params.endDateValue} 23:59:59` : undefined
			}
		}
	}

	const getTreeList = async () => {
		listTreeV3({code: 1, type: 0, status: 0}).then(res => {
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
			title: '菜单名称',
			dataIndex: 'name',
			hideInSearch: true,
			ellipsis: true,
			width: 210,
		},
		{
			title: '菜单路径',
			dataIndex: 'path',
			ellipsis: true,
			hideInSearch: true,
			width: 180
		},
		{
			title: '菜单权限标识',
			dataIndex: 'permission',
			ellipsis: true,
			hideInSearch: true,
			width: 180
		},
		{
			title: '菜单类型',
			key: 'typeValue',
			dataIndex: 'typeValue',
			hideInTable: true,
			valueType: 'select',
			fieldProps: {
				valueType: 'select',
				mode: 'single',
				placeholder: '请选择菜单类型',
				options: [
					{
						value: 0,
						label: '菜单',
					},
					{
						value: 1,
						label: '按钮',
					},
				],
			},
			ellipsis: true
		},
		{
			disable: true,
			title: '菜单类型',
			dataIndex: 'type',
			hideInSearch: true,
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
			title: '菜单状态',
			key: 'statusValue',
			dataIndex: 'statusValue',
			hideInTable: true,
			valueType: 'select',
			fieldProps: {
				valueType: 'select',
				mode: 'single',
				placeholder: '请选择菜单状态',
				options: [
					{
						value: 0,
						label: '启用',
					},
					{
						value: 1,
						label: '禁用',
					},
				]
			},
			ellipsis: true
		},
		{
			title: '菜单状态',
			dataIndex: 'status',
			hideInSearch: true,
			render: (_, record) => (
				<Switch checkedChildren="启用" unCheckedChildren="禁用" disabled={true} checked={record?.status === 0} />
			),
		},
		{
			title: '菜单排序',
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
			key: 'createTimeValue',
			dataIndex: 'createTimeValue',
			valueType: 'dateRange',
			hideInTable: true,
			fieldProps: {
				placeholder: ['请选择开始时间', '请选择结束时间'],
			},
			search: {
				transform: (value) => {
					return {
						startDateValue: value[0],
						endDateValue: value[1],
					};
				},
			}
		},
		{
			title: '操作',
			valueType: 'option',
			key: 'option',
			render: (_, record) => [
				( access.canMenuGetDetail && <a key="get"
				   onClick={() => {
					   getByIdV3({id: record?.id}).then(res => {
						   if (res.code === 'OK') {
							   setTitle('查看菜单')
							   setModalVisit(true)
							   setReadOnly(true)
							   const data = res?.data;
							   setTypeValue(data.type)
							   setDataSource(data)
						   }
					   })
				   }}
				>
					查看
				</a>),
				( access.canMenuSave && <a key="save" onClick={() => {
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
						pid: record?.id
					})
				}}>
					新增
				</a>),
				( access.canMenuModify && <a key="modify"
				   onClick={() => {
					   getByIdV3({id: record?.id}).then(res => {
						   if (res.code === 'OK') {
							   setTitle('修改菜单')
							   setModalVisit(true)
							   setReadOnly(false)
							   const data = res?.data;
							   setTypeValue(data.type)
							   setDataSource(data)
						   }
					   })
				   }}
				>
					修改
				</a>),
				( access.canMenuRemove && <a key="remove" onClick={() => {
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
				</a>)
			],
			width: 190
		},
	];

	return (
		<>

			<MenuDrawer
				modalVisit={modalVisit}
				setModalVisit={setModalVisit}
				title={title}
				readOnly={readOnly}
				dataSource={dataSource}
				treeList={treeList}
				typeValue={typeValue}
				setTypeValue={setTypeValue}
				onComponent={async () => {
					// @ts-ignore
					actionRef?.current?.reload();
					getTreeList().catch(console.log);
				}}
			/>

			<ProTable<TableColumns>
				actionRef={actionRef}
				columns={columns}
				request={ async (params) => {
					// 表单搜索项会从 params 传入，传递给后端接口。
					return listTreeV3(getListTreeQueryParam(params)).then(res => {
						return Promise.resolve({
							data: res?.data,
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
						( access.canMenuSave && <Button key="save" type="primary" icon={<PlusOutlined />} onClick={() => {
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
						</Button>),
						( access.canMenuRemove && <Button key="remove" type="primary" danger icon={<DeleteOutlined />} onClick={() => {
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
						</Button>)
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
