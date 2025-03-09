import {
	ProColumns
} from '@ant-design/pro-components';
import {ProTable} from '@ant-design/pro-components';
import {pageV3, removeV3, getByIdV3} from "@/services/admin/role";
import {useEffect, useRef, useState} from "react";
import {TableRowSelection} from "antd/es/table/interface";
import {Button, message, Modal} from 'antd';
import { DeleteOutlined, PlusOutlined } from '@ant-design/icons';
import {trim} from "@/utils/format";
import {treeListV3 as menuTreeListV3} from "@/services/admin/menu";
import {treeListV3 as deptTreeListV3} from "@/services/admin/dept";
import {RoleDrawer} from "@/pages/Sys/Permission/RoleDrawer";
import {RoleModifyAuthorityDrawer} from "@/pages/Sys/Permission/RoleModifyAuthorityDrawer";

export default () => {

	type TableColumns = {
		id: number;
		name: string | undefined;
		createTime: string | undefined;
		sort: number | undefined;
		dataScope: string | undefined;
	};

	const [readOnly, setReadOnly] = useState(false)
	const [modalVisit, setModalVisit] = useState(false);
	const actionRef = useRef();
	const [dataSource, setDataSource] = useState<any>({})
	const [ids, setIds] = useState<number[]>([])
	const [title, setTitle] = useState("")
	const [menuTreeList, setMenuTreeList] = useState<any[]>([])
	const [modalModifyAuthorityVisit, setModalModifyAuthorityVisit] = useState(false);
	const [deptTreeList, setDeptTreeList] = useState<any[]>([])
	const [typeValue, setTypeValue] = useState('all');

	const getPageQuery = (params: any) => {
		return {
			pageSize: params?.pageSize,
			pageNum: params?.current,
			pageIndex: params?.pageSize * (params?.current - 1),
			name: trim(params?.name),
			dataScope: params?.dataScope,
			params: {
				startTime: params?.startDate ? `${params.startDate} 00:00:00` : undefined,
				endTime: params?.endDate ? `${params.endDate} 23:59:59` : undefined
			}
		}
	}

	const getMenuTreeList = async () => {
		menuTreeListV3({code: 1, status: 0}).then(res => {
			setMenuTreeList(res?.data)
		})
	}

	const getDeptTreeList = async () => {
		deptTreeListV3({}).then(res => {
			setDeptTreeList(res?.data)
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
		getMenuTreeList().catch(console.log)
		getDeptTreeList().catch(console.log)
	}, []);

	const columns: ProColumns<TableColumns>[] = [
		{
			title: '名称',
			dataIndex: 'name'
		},
		{
			title: '数据范围',
			dataIndex: 'dataScope',
			valueEnum: {
				all: '全部',
				custom: '自定义',
				dept_self: '仅本部门',
				dept: '部门及以下',
				self: '仅本人',
			},
		},
		{
			title: '排序',
			dataIndex: 'sort',
			hideInSearch: true,
			ellipsis: true,
			width:80,
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
						   setTitle('查看角色')
						   setModalVisit(true)
						   setReadOnly(true)
						   setDataSource(res?.data)
					   })
				   }}
				>
					查看
				</a>,
				<a key="modify"
				   onClick={() => {
					   getByIdV3({id: record?.id}).then(res => {
						   setTitle('修改角色')
						   setModalVisit(true)
						   setReadOnly(false)
						   setDataSource(res?.data)
					   })
				   }}
				>
					修改
				</a>,
				<a key={'modifyAuthority'} onClick={() => {
					getByIdV3({id: record?.id}).then(res => {
						setTitle('分配权限')
						setModalModifyAuthorityVisit(true)
						setDataSource(res?.data)
						setTypeValue(res?.data?.dataScope)
					})
				}}>
					分配权限
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

			<RoleDrawer
				modalVisit={modalVisit}
				setModalVisit={setModalVisit}
				title={title}
				readOnly={readOnly}
				dataSource={dataSource}
				onComponent={async () => {
					// @ts-ignore
					actionRef?.current?.reload();
				}}
				menuTreeList={menuTreeList}
			/>

			<RoleModifyAuthorityDrawer
				modalModifyAuthorityVisit={modalModifyAuthorityVisit}
				setModalModifyAuthorityVisit={setModalModifyAuthorityVisit}
				title={title}
				dataSource={dataSource}
				onComponent={() => {
					// @ts-ignore
					actionRef?.current?.reload();
				}}
				menuTreeList={menuTreeList}
				deptTreeList={deptTreeList}
				typeValue={typeValue}
				setTypeValue={setTypeValue}
			/>

			<ProTable<TableColumns>
				actionRef={actionRef}
				columns={columns}
				request={ async (params) => {
					// 表单搜索项会从 params 传入，传递给后端接口。
					return pageV3(getPageQuery(params)).then(res => {
						return Promise.resolve({
							data: res?.data?.records,
							total: parseInt(res.data.total),
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
							setTitle('新增角色')
							setReadOnly(false)
							setModalVisit(true)
							setDataSource({
								id: undefined,
								name: '',
								dataScope: 'all',
								menuIds: [],
								sort: 1,
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
					title: '角色',
					tooltip: '角色',
				}}
			/>
		</>
	);
};
