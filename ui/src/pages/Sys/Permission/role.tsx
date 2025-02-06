import {
	DrawerForm,
	ProColumns,
	ProFormDigit, ProFormSelect,
	ProFormText, ProFormTreeSelect,
} from '@ant-design/pro-components';
import {ProTable} from '@ant-design/pro-components';
import {pageV3, removeV3, saveV3, getByIdV3, modifyV3} from "@/services/admin/role";
import {useEffect, useRef, useState} from "react";
import {TableRowSelection} from "antd/es/table/interface";
import {Button, message, Modal} from 'antd';
import { DeleteOutlined, PlusOutlined } from '@ant-design/icons';
import {v7 as uuidV7} from 'uuid';
import {trim} from "@/utils/format";
import {treeListV3} from "@/services/admin/menu";

export default () => {

	type TableColumns = {
		id: number;
		name: string | undefined;
		createTime: string | undefined;
		sort: number | undefined;
		menuIds: string[] | undefined;
		dataScope: string | undefined;
	};

	const [readOnly, setReadOnly] = useState(false)
	const [modalVisit, setModalVisit] = useState(false);
	const actionRef = useRef();
	const [dataSource, setDataSource] = useState({})
	const [ids, setIds] = useState<number[]>([])
	const [title, setTitle] = useState("")
	const [treeList, setTreeList] = useState<any[]>([])

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

	const getTreeList = async () => {
		treeListV3({code: 1, status: 0}).then(res => {
			setTreeList(res?.data)
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
					// @ts-ignore
					const menuIds = value?.menuIds.map(item => item?.value)
					const co = {
						name: value.name,
						menuIds: menuIds,
						dataScope: value.dataScope,
						sort: value.sort,
						id: value.id
					}
					if (value.id === undefined) {
						saveV3({co: co}, uuidV7()).then(res => {
							if (res.code === 'OK') {
								message.success("新增成功").then()
								setModalVisit(false)
								// @ts-ignore
								actionRef?.current?.reload();
							}
						})
					} else {
						modifyV3({co: co}).then(res => {
							if (res.code === 'OK') {
								message.success("修改成功").then()
								setModalVisit(false)
								// @ts-ignore
								actionRef?.current?.reload();
							}
						})
					}
				}}>

				<ProFormText
					name="id"
					label="ID"
					hidden={true}
				/>

				<ProFormText
					name="name"
					label="名称"
					readonly={readOnly}
					placeholder={'请输入名称'}
					rules={[{ required: true, message: '请输入名称' }]}
				/>

				<ProFormSelect
					name="dataScope"
					label="数据范围"
					readonly={readOnly}
					placeholder={'请选择数据范围'}
					rules={[{ required: true, message: '请选择数据范围' }]}
					options={[
						{value: 'all', label: '全部'},
						{value: 'custom', label: '自定义'},
						{value: 'dept_self', label: '仅本部门'},
						{value: 'dept', label: '部门及以下'},
						{value: 'self', label: '仅本人'},
					]}
				/>

				<ProFormTreeSelect
					name="menuIds"
					label="菜单权限"
					readonly={readOnly}
					allowClear={true}
					placeholder={'请选择菜单权限'}
					rules={[{ required: true, message: '请选择菜单权限' }]}
					fieldProps={{
						fieldNames: {
							label: 'name',
							value: 'id',
							children: 'children'
						},
						// 最多显示多少个 tag，响应式模式会对性能产生损耗
						maxTagCount: 6,
						// 多选
						multiple: true,
						// 显示复选框
						treeCheckable: true,
						// 展示策略
						showCheckedStrategy: 'SHOW_ALL',
						// 取消父子节点联动
						treeCheckStrictly: true,
						// 默认展示所有节点
						treeDefaultExpandAll: true,
						// 高度
						dropdownStyle: { maxHeight: 500 },
						// 不显示搜索
						showSearch: false,
					}}
					request={async () => {
						return treeList
					}}
				/>

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
