import {
	ProColumns
} from '@ant-design/pro-components';
import {ProTable} from '@ant-design/pro-components';
import {pageRole, removeRole, getRoleById} from "@/services/admin/role";
import {useEffect, useRef, useState} from "react";
import {TableRowSelection} from "antd/es/table/interface";
import {Button, message, Modal} from 'antd';
import { DeleteOutlined, PlusOutlined } from '@ant-design/icons';
import {trim} from "@/utils/format";
import {listTreeMenu} from "@/services/admin/menu";
import {listTreeDept} from "@/services/admin/dept";
import {RoleDrawer} from "@/pages/Sys/Permission/RoleDrawer";
import {RoleModifyAuthorityDrawer} from "@/pages/Sys/Permission/RoleModifyAuthorityDrawer";
import {useAccess} from "@@/exports";
import {v7 as uuidV7} from "uuid";

export default () => {

	type TableColumns = {
		id: number;
		name: string | undefined;
		createTime: string | undefined;
		sort: number | undefined;
		dataScope: string | undefined;
	};

	const access = useAccess()
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
	const [requestId, setRequestId] = useState('')

	const getPageQueryParam = (params: any) => {
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
		listTreeMenu({code: 1, status: 0}).then(res => {
			setMenuTreeList(res?.data)
		})
	}

	const getDeptTreeList = async () => {
		listTreeDept({}).then(res => {
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
			title: '序号',
			dataIndex: 'index',
			valueType: 'indexBorder',
			width: 60,
		},
		{
			title: '角色名称',
			dataIndex: 'name',
			valueType: 'text',
			fieldProps: {
				placeholder: '请输入角色名称',
			}
		},
		{
			title: '数据范围',
			dataIndex: 'dataScope',
			valueType: 'select',
			fieldProps: {
				valueType: 'select',
				mode: 'single',
				placeholder: '请选择数据范围',
				options: [
					{
						value: 'all',
						label: '全部',
					},
					{
						value: 'custom',
						label: '自定义',
					},
					{
						value: 'self_dept',
						label: '仅本部门',
					},
					{
						value: 'below_dept',
						label: '部门及以下',
					},
					{
						value: 'self',
						label:'仅本人',
					}
				]
			},
		},
		{
			title: '角色排序',
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
			dataIndex: 'createTimeValue',
			valueType: 'dateRange',
			hideInTable: true,
			fieldProps: {
				placeholder: ['请选择开始时间', '请选择结束时间'],
			},
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
				( access.canRoleGetDetail && <a key="get"
				   onClick={() => {
					   getRoleById({id: record?.id}).then(res => {
						   if (res.code === 'OK') {
							   setTitle('查看角色')
							   setModalVisit(true)
							   setReadOnly(true)
							   setDataSource(res?.data)
						   }
					   })
				   }}
				>
					查看
				</a>),
				( access.canRoleModify && <a key="modify"
				   onClick={() => {
					   getRoleById({id: record?.id}).then(res => {
						   if (res.code === 'OK') {
							   setTitle('修改角色')
							   setModalVisit(true)
							   setReadOnly(false)
							   setDataSource(res?.data)
						   }
					   })
				   }}
				>
					修改
				</a>),
				( access.canRoleModify && <a key={'modifyAuthority'} onClick={() => {
					getRoleById({id: record?.id}).then(res => {
						setTitle('分配权限')
						setModalModifyAuthorityVisit(true)
						setDataSource(res?.data)
						setTypeValue(res?.data?.dataScope)
					})
				}}>
					分配权限
				</a>),
				( access.canRoleRemove && <a key="remove" onClick={() => {
					Modal.confirm({
						title: '确认删除?',
						content: '您确定要删除吗?',
						okText: '确认',
						cancelText: '取消',
						onOk: () => {
							removeRole([record?.id]).then(res => {
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
				requestId={requestId}
				setRequestId={setRequestId}
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
					return pageRole(getPageQueryParam(params)).then(res => {
						return Promise.resolve({
							data: res?.data?.records,
							total: parseInt(res?.data?.total || 0),
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
						( access.canRoleSave && <Button key="save" type="primary" icon={<PlusOutlined />} onClick={() => {
							setTitle('新增角色')
							setRequestId(uuidV7())
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
						</Button>),
						( access.canRoleRemove && <Button key="remove" type="primary" danger icon={<DeleteOutlined />} onClick={() => {
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
									removeRole(ids).then(res => {
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
					title: '角色',
					tooltip: '角色',
				}}
			/>
		</>
	);
};
