import {ProColumns} from '@ant-design/pro-components';
import {ProTable} from '@ant-design/pro-components';
import {pageV3, getByIdV3, removeV3} from "@/services/iot/thingModel";
import {Button, message, Modal} from "antd";
import {DeleteOutlined, PlusOutlined} from "@ant-design/icons";
import {trim} from "@/utils/format";
import React, {useRef, useState} from "react";
import {getStatus, STATUS} from "@/services/constant";
import {ThingModelDrawer} from "@/pages/IoT/Device/ThingModelDrawer";

export default () => {

	const actionRef = useRef();
	const [modalVisit, setModalVisit] = useState(false);
	const [dataSource, setDataSource] = useState<any>({})
	const [title, setTitle] = useState("")
	const [readOnly, setReadOnly] = useState(false)
	const [param, setParam] = useState<any>({});
	const [value, setValue] = useState("");

	type TableColumns = {
		id: number;
		code: string | undefined;
		name: string | undefined;
		sort: number | undefined;
		dataType: string | undefined;
		category: number | undefined;
		type: string | undefined;
		expression: string | undefined;
		specs: string | undefined;
		remark: string | undefined;
		createTime: string | undefined;
	};

	const getPageQuery = (params: any) => {
		const param = {
			pageSize: params?.pageSize,
			pageNum: params?.current,
			pageIndex: params?.pageSize * (params?.current - 1),
			code: trim(params?.code),
			name: trim(params?.name),
			status: params?.status,
			errorMessage: trim(params?.errorMessage),
			params: {
				startTime: params?.startDate ? `${params.startDate} 00:00:00` : undefined,
				endTime: params?.endDate ? `${params.endDate} 23:59:59` : undefined
			}
		};
		setParam(param)
		return param
	}

	const columns: ProColumns<TableColumns>[] = [
		{
			title: '序号',
			dataIndex: 'index',
			valueType: 'indexBorder',
			width: 60,
		},
		{
			title: '编码',
			dataIndex: 'code',
			ellipsis: true
		},
		{
			title: '名称',
			dataIndex: 'name',
			ellipsis: true
		},
		{
			title: '状态',
			dataIndex: 'status',
			valueEnum: {
				[STATUS.OK]: getStatus(STATUS.OK),
				[STATUS.FAIL]: getStatus(STATUS.FAIL)
			},
			ellipsis: true
		},
		{
			title: '错误信息',
			dataIndex: 'errorMessage',
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

			<ThingModelDrawer
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
					return pageV3(getPageQuery(params)).then(res => {
						return Promise.resolve({
							data: res?.data?.records,
							total: parseInt(res.data.total),
							success: true,
						});
					})
				}}
				rowKey="id"
				pagination={{
					showQuickJumper: true,
					showSizeChanger: false,
					pageSize: 10
				}}
				search={{
					layout: 'vertical',
					defaultCollapsed: true,
				}}
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
									// if (ids.length === 0) {
									// 	message.warning("请至少选择一条数据").then()
									// 	return;
									// }
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
					title: '物模型',
					tooltip: '物模型',
				}}
			/>
		</>
	);
};
