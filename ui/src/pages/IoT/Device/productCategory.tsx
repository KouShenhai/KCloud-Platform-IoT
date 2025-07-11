import {ProColumns} from '@ant-design/pro-components';
import {ProTable} from '@ant-design/pro-components';
import {getProductCategoryById, removeProductCategory, listTreeProductCategory} from "@/services/iot/productCategory";
import {Button, message, Modal} from "antd";
import {DeleteOutlined, PlusOutlined} from "@ant-design/icons";
import React, {useRef, useState} from "react";
import {TableRowSelection} from "antd/es/table/interface";
import {ProductCategoryDrawer} from "@/pages/IoT/Device/ProductCategoryDrawer";
import {useAccess} from "@@/exports";
import {v7 as uuidV7} from "uuid";

export default () => {

	const access = useAccess()
	const actionRef = useRef();
	const [modalVisit, setModalVisit] = useState(false);
	const [dataSource, setDataSource] = useState<any>({})
	const [title, setTitle] = useState("")
	const [readOnly, setReadOnly] = useState(false)
	const [ids, setIds] = useState<any>([])
	const [treeList, setTreeList] = useState<any[]>([])
	const [requestId, setRequestId] = useState('')

	type TableColumns = {
		id: number;
		code: string | undefined;
		name: string | undefined;
		sort: number | undefined;
		pid: number | undefined;
		remark: string | undefined;
		createTime: string | undefined;
	};

	const getListTreeQueryParam = (params: any) => {
		return {
			params: {
				startTime: params?.startDate ? `${params.startDate} 00:00:00` : undefined,
				endTime: params?.endDate ? `${params.endDate} 23:59:59` : undefined
			}
		};
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

	const columns: ProColumns<TableColumns>[] = [
		{
			title: '序号',
			dataIndex: 'index',
			valueType: 'indexBorder',
			width: 60,
		},
		{
			title: '产品类别名称',
			dataIndex: 'name',
			ellipsis: true,
			valueType: 'text',
			hideInSearch: true,
		},
		{
			title: '产品类别排序',
			dataIndex: 'sort',
			hideInSearch: true,
			ellipsis: true,
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
				( access.canProductCategoryGetDetail && <a key="get"
				   onClick={() => {
					   getProductCategoryById({id: record?.id}).then(res => {
						   setTitle('查看产品类别')
						   setDataSource(res?.data)
						   setModalVisit(true)
						   setReadOnly(true)
					   })
				   }}
				>
					查看
				</a>),
				( access.canProductCategorySave && <a key="save"
				   onClick={() => {
					   setTitle('新增产品类别')
					   setRequestId(uuidV7())
					   setReadOnly(false)
					   setModalVisit(true)
					   setDataSource({sort: 1, pid: record?.id, id: undefined, name: '', remark: ''})
				   }}
				>
					新增
				</a>),
				( access.canProductCategoryModify && <a key="modify"
				   onClick={() => {
					   getProductCategoryById({id: record?.id}).then(res => {
						   setTitle('修改产品类别')
						   setDataSource(res?.data)
						   setModalVisit(true)
						   setReadOnly(false)
					   })
				   }}
				>
					修改
				</a>),
				( access.canProductCategoryRemove && <a key="remove" onClick={() => {
					Modal.confirm({
						title: '确认删除?',
						content: '您确定要删除吗?',
						okText: '确认',
						cancelText: '取消',
						onOk: () => {
							removeProductCategory([record?.id]).then(res => {
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

			<ProductCategoryDrawer
				modalVisit={modalVisit}
				setModalVisit={setModalVisit}
				title={title}
				readOnly={readOnly}
				dataSource={dataSource}
				onComponent={async () => {
					// @ts-ignore
					actionRef?.current?.reload();
				}}
				treeList={treeList}
				requestId={requestId}
				setRequestId={setRequestId}
			/>

			<ProTable<TableColumns>
				actionRef={actionRef}
				columns={columns}
				request={async (params) => {
					// 表单搜索项会从 params 传入，传递给后端接口。
					return listTreeProductCategory(getListTreeQueryParam(params)).then(res => {
						setTreeList([{
							id: '0',
							name: '根目录',
							children: res?.data
						}])
						return Promise.resolve({
							data: res?.data,
							success: true,
						});
					})
				}}
				rowKey="id"
				search={{
					layout: 'vertical',
					defaultCollapsed: true,
				}}
				rowSelection={{ ...rowSelection }}
				toolBarRender={
					() => [
						( access.canProductCategorySave && <Button key="save" type="primary" icon={<PlusOutlined />} onClick={() => {
							setTitle('新增产品类别')
							setRequestId(uuidV7())
							setReadOnly(false)
							setModalVisit(true)
							setDataSource({sort: 1, pid: undefined, id: undefined, name: '', remark: ''})
						}}>
							新增
						</Button>),
						( access.canProductCategoryRemove && <Button key="remove" type="primary" danger icon={<DeleteOutlined />} onClick={() => {
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
									removeProductCategory(ids).then(res => {
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
					title: '产品类别',
					tooltip: '产品类别',
				}}
			/>
		</>
	);
};
