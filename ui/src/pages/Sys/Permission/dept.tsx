import {
	ProColumns,
} from '@ant-design/pro-components';
import {ProTable} from '@ant-design/pro-components';
import {listTreeDept, removeDept, getDeptById} from "@/services/admin/dept";
import {useEffect, useRef, useState} from "react";
import {TableRowSelection} from "antd/es/table/interface";
import {Button, message, Modal} from 'antd';
import { DeleteOutlined, PlusOutlined } from '@ant-design/icons';
import {trim} from "@/utils/format";
import {DeptDrawer} from "@/pages/Sys/Permission/DeptDrawer";
import {useAccess} from "@@/exports";
import {v7 as uuidV7} from "uuid";

export default () => {

	type TableColumns = {
		id: number;
		name: string | undefined;
		path: string | undefined;
		createTime: string | undefined;
		sort: number | undefined;
	};

	const access = useAccess();
	const [readOnly, setReadOnly] = useState(false)
	const [modalVisit, setModalVisit] = useState(false);
	const actionRef = useRef();
	const [dataSource, setDataSource] = useState<any>({})
	const [ids, setIds] = useState<number[]>([])
	const [title, setTitle] = useState("")
	const [treeList, setTreeList] = useState<any[]>([])
	const [requestId, setRequestId] = useState('')

	const getListTreeQueryParam = (params: any) => {
		return {
			name: trim(params?.name),
			params: {
				startTime: params?.startDate ? `${params.startDate} 00:00:00` : undefined,
				endTime: params?.endDate ? `${params.endDate} 23:59:59` : undefined
			}
		}
	}

	const getTreeList = async () => {
		listTreeDept({}).then(res => {
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
			title: '序号',
			dataIndex: 'index',
			valueType: 'indexBorder',
			width: 60,
		},
		{
			title: '部门名称',
			dataIndex: 'name',
			valueType: 'text',
			fieldProps: {
				placeholder: '请输入部门名称',
			}
		},
		{
			title: '部门路径',
			dataIndex: 'path',
			ellipsis: true,
			hideInSearch: true,
		},
		{
			title: '部门排序',
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
				( access.canDeptGetDetail && <a key="get"
				   onClick={() => {
					   getDeptById({id: record?.id}).then(res => {
						   if (res.code === 'OK') {
							   setTitle('查看部门')
							   setModalVisit(true)
							   setReadOnly(true)
							   setDataSource(res?.data)
						   }
					   })
				   }}
				>
					查看
				</a>),
				( access.canDeptSave && <a key="save" onClick={() => {
					setTitle('新增部门')
					setRequestId(uuidV7())
					setReadOnly(false)
					setModalVisit(true)
					setDataSource({
						id: undefined,
						name: '',
						path: '',
						pid: record?.id,
						sort: 1,
					})
				}}>
					新增
				</a>),
				( access.canDeptModify && <a key="modify"
				   onClick={() => {
					   getDeptById({id: record?.id}).then(res => {
						   if (res.code === 'OK') {
							   setTitle('修改部门')
							   setModalVisit(true)
							   setReadOnly(false)
							   setDataSource(res?.data)
						   }
					   })
				   }}
				>
					修改
				</a>),
				( access.canDeptRemove && <a key="remove" onClick={() => {
					Modal.confirm({
						title: '确认删除?',
						content: '您确定要删除吗?',
						okText: '确认',
						cancelText: '取消',
						onOk: () => {
							removeDept([record?.id]).then(res => {
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

			<DeptDrawer
				modalVisit={modalVisit}
				setModalVisit={setModalVisit}
				title={title}
				readOnly={readOnly}
				dataSource={dataSource}
				onComponent={async () => {
					// @ts-ignore
					actionRef?.current?.reload();
					getTreeList().catch(console.log);
				}}
				treeList={treeList}
				setRequestId={setRequestId}
				requestId={requestId}
			/>

			<ProTable<TableColumns>
				actionRef={actionRef}
				columns={columns}
				request={ async (params) => {
					// 表单搜索项会从 params 传入，传递给后端接口。
					return listTreeDept(getListTreeQueryParam(params)).then(res => {
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
						( access.canDeptSave && <Button key="save" type="primary" icon={<PlusOutlined />} onClick={() => {
							setTitle('新增部门')
							setRequestId(uuidV7())
							setReadOnly(false)
							setModalVisit(true)
							setDataSource({
								id: undefined,
								name: '',
								path: '',
								sort: 1,
							})
						}}>
							新增
						</Button>),
						( access.canDeptRemove && <Button key="remove" type="primary" danger icon={<DeleteOutlined />} onClick={() => {
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
									removeDept(ids).then(res => {
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
					title: '部门',
					tooltip: '部门',
				}}
			/>
		</>
	);
};
