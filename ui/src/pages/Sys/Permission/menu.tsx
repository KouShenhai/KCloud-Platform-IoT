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

	const [readOnly, setReadOnly] = useState(false)
	const [modalVisit, setModalVisit] = useState(false);
	const actionRef = useRef();
	const [dataSource, setDataSource] = useState<any>({})
	const [ids, setIds] = useState<number[]>([])
	const [title, setTitle] = useState("")
	const [typeValue, setTypeValue] = useState(0);
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
			title: '名称',
			dataIndex: 'name',
			hideInSearch: true,
			ellipsis: true,
			width: 200,
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
			width: 180
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
				</a>,
				<a key="save" onClick={() => {
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
				</a>,
				<a key="modify"
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
			width: 200
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
					return listTreeV3(getPageQuery(params)).then(res => {
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
