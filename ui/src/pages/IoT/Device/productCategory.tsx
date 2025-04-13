import {ProColumns} from '@ant-design/pro-components';
import {ProTable} from '@ant-design/pro-components';
import {getByIdV3, removeV3, listTreeV3} from "@/services/iot/productCategory";
import {Button, message, Modal} from "antd";
import {DeleteOutlined, PlusOutlined} from "@ant-design/icons";
import {trim} from "@/utils/format";
import React, {useRef, useState} from "react";
import {TableRowSelection} from "antd/es/table/interface";
import {ProductCategoryDrawer} from "@/pages/IoT/Device/ProductCategoryDrawer";

export default () => {

	const actionRef = useRef();
	const [modalVisit, setModalVisit] = useState(false);
	const [dataSource, setDataSource] = useState<any>({})
	const [title, setTitle] = useState("")
	const [readOnly, setReadOnly] = useState(false)
	const [value, setValue] = useState("");
	const [ids, setIds] = useState<any>([])

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
			code: trim(params?.code),
			name: trim(params?.name),
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
			title: '产品类别编码',
			dataIndex: 'code',
			ellipsis: true,
			valueType: 'text',
			fieldProps: {
				placeholder: '请输入产品类别编码',
			}
		},
		{
			title: '产品类别名称',
			dataIndex: 'name',
			ellipsis: true,
			valueType: 'text',
			fieldProps: {
				placeholder: '请输入产品类别名称',
			}
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
				<a key="getable"
				   onClick={() => {
					   getByIdV3({id: record?.id}).then(res => {
						   setDataSource(res?.data)
						   setModalVisit(true)
					   })
				   }}
				>
					查看
				</a>
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
				value={value}
				setValue={setValue}
			/>

			<ProTable<TableColumns>
				actionRef={actionRef}
				columns={columns}
				request={async (params) => {
					// 表单搜索项会从 params 传入，传递给后端接口。
					return listTreeV3(getListTreeQueryParam(params)).then(res => {
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
						<Button key="save" type="primary" icon={<PlusOutlined />} onClick={() => {
							setTitle('新增物模型')
							setReadOnly(false)
							setModalVisit(true)
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
					title: '产品类别',
					tooltip: '产品类别',
				}}
			/>
		</>
	);
};
