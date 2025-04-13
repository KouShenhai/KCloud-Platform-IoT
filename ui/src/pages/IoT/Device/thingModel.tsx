import {ProColumns} from '@ant-design/pro-components';
import {ProTable} from '@ant-design/pro-components';
import {pageV3, getByIdV3, removeV3} from "@/services/iot/thingModel";
import {Button, message, Modal, Space, Tag} from "antd";
import {DeleteOutlined, PlusOutlined} from "@ant-design/icons";
import {trim} from "@/utils/format";
import React, {useRef, useState} from "react";
import {ThingModelDrawer} from "@/pages/IoT/Device/ThingModelDrawer";
import {TableRowSelection} from "antd/es/table/interface";
import {useAccess} from "@@/exports";

export default () => {

	const access = useAccess()
	const actionRef = useRef();
	const [modalVisit, setModalVisit] = useState(false);
	const [dataSource, setDataSource] = useState<any>({})
	const [title, setTitle] = useState("")
	const [readOnly, setReadOnly] = useState(false)
	const [value, setValue] = useState("");
	const [ids, setIds] = useState<any>([])
	const [flag, setFlag] = useState(0)
	const [dataType, setDataType] = useState('integer')

	type TableColumns = {
		id: number;
		code: string | undefined;
		name: string | undefined;
		sort: number | undefined;
		dataType: string | undefined;
		category: number | undefined;
		type: string | undefined;
		expression: string | undefined;
		expressionFlag: number;
		specs: string | undefined;
		remark: string | undefined;
		createTime: string | undefined;
	};

	const getPageQueryParam = (params: any) => {
		return {
			pageSize: params?.pageSize,
			pageNum: params?.current,
			pageIndex: params?.pageSize * (params?.current - 1),
			code: trim(params?.code),
			name: trim(params?.name),
			dataType: params?.dataType,
			category: params?.category,
			type: params?.typeValue ? params?.typeValue.join(',') : '',
			params: {
				startTime: params?.startDate ? `${params.startDate} 00:00:00` : undefined,
				endTime: params?.endDate ? `${params.endDate} 23:59:59` : undefined
			}
		};
	}

	const getData = (data: any) => {
		const specs = JSON.parse(data?.specs)
		data.length = specs?.length
		data.integerLength = specs?.integerLength
		data.decimalLength = specs?.decimalLength
		data.unit = specs?.unit
		data.trueText = specs?.trueText
		data.falseText = specs?.falseText
		data.type = data?.type?.split(',')
		return data
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
			title: '物模型编码',
			dataIndex: 'code',
			valueType: 'text',
			ellipsis: true,
			fieldProps: {
				placeholder: '请输入物模型编码',
			}
		},
		{
			title: '物模型名称',
			dataIndex: 'name',
			valueType: 'text',
			ellipsis: true,
			fieldProps: {
				placeholder: '请输入物模型名称',
			}
		},
		{
			title: '物模型数据类型',
			key: "dataType",
			dataIndex: 'dataType',
			valueType: 'select',
			fieldProps: {
				valueType: 'select',
				mode: 'single',
				placeholder: '请选择物模型数据类型',
				options: [
					{
						value: 'integer',
						label: '整数型',
					},
					{
						value: 'decimal',
						label: '小数型',
					},
					{
						value: 'boolean',
						label: '布尔型'
					},
					{
						value: 'string',
						label: '字符串型',
					},
				]
			},
			ellipsis: true
		},
		{
			title: '物模型类别',
			key: "category",
			dataIndex: 'category',
			valueType: 'select',
			fieldProps: {
				valueType: 'select',
				mode: 'single',
				placeholder: '请选择物模型类别',
				options: [
					{
						value: 1,
						label: '属性',
					},
					{
						value: 2,
						label: '事件',
					},
				]
			},
			ellipsis: true
		},
		{
			title: '物模型类型',
			key: "typeValue",
			dataIndex: 'typeValue',
			valueType: 'select',
			ellipsis: true,
			hideInTable: true,
			fieldProps: {
				valueType: 'select',
				mode: 'multiple',
				placeholder: '请选择物模型类型',
				options: [
					{
						value: 'read',
						label: '读',
					},
					{
						value: 'write',
						label: '写',
					},
					{
						value: 'report',
						label: '上报',
					},
				]
			}
		},
		{
			title: '物模型类型',
			dataIndex: 'type',
			disable: true,
			hideInSearch: true,
			renderFormItem: (_, { defaultRender }) => {
				return defaultRender(_);
			},
			render: (_, record) => {
				const element: any = []
				record?.type?.split(',').forEach(item => {
					if (item === 'read') {
						element.push(<Tag color={'green-inverse'} key={'read'}>读</Tag>);
					} else if (item === 'write') {
						element.push(<Tag color={'#fd5251'} key={'write'}>写</Tag>);
					} else if (item === 'report') {
						element.push(<Tag color={'#f4a300'} key={'report'}>上报</Tag>);
					}
				})
				return <Space>{element}</Space>;
			}
		},
		{
			title: '物模型规则说明',
			dataIndex: 'specs',
			valueType: 'text',
			hideInSearch: true,
			renderFormItem: (_, { defaultRender }) => {
				return defaultRender(_);
			},
			render: (_, record) => {
				const data = JSON.parse(typeof record?.specs === "string" ? record?.specs : "{}")
				return <>
					{ (record?.dataType === 'integer' || record?.dataType === 'string') && <div>长度：<span style={{color: '#fd5251'}}>{data?.length}</span></div>}
					{ record?.dataType === 'decimal' && <div>整数位长度：<span style={{color: '#fd5251'}}>{data?.integerLength}</span></div>}
					{ record?.dataType === 'decimal' && <div>小数位长度：<span style={{color: '#fd5251'}}>{data?.decimalLength}</span></div>}
					{ (record?.dataType === 'decimal' || record?.dataType === 'integer') && <div>单位：<span style={{color: '#fd5251'}}>{data?.unit ? data?.unit : '无'}</span></div>}
					{ record?.dataType === 'boolean' && <div>0：<span style={{color: '#fd5251'}}>{data?.falseText}</span></div>}
					{ record?.dataType === 'boolean' && <div>1：<span style={{color: '#fd5251'}}>{data?.trueText}</span></div>}
				</>
			},
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
				( access.canThingModelGetDetail && <a key="getable"
				   onClick={() => {
					   getByIdV3({id: record?.id}).then(res => {
						   setTitle('查看物模型')
						   const data = getData(res?.data)
						   setDataSource(data)
						   setModalVisit(true)
						   setReadOnly(true)
						   setDataType(data?.dataType)
					   })
				   }}
				>
					查看
				</a>),
				( access.canThingModelModify && <a key="modify"
				 onClick={() => {
					 getByIdV3({id: record?.id}).then(res => {
						 setTitle('修改物模型')
						 const data = getData(res?.data)
						 setDataSource(data)
						 setModalVisit(true)
						 setReadOnly(false)
						 setDataType(data?.dataType)
					 })
				 }}
				>
					修改
				</a>),
				( access.canThingModelRemove && <a key="remove" onClick={() => {
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
				flag={flag}
				setFlag={setFlag}
				setDataType={setDataType}
				dataType={dataType}
			/>

			<ProTable<TableColumns>
				actionRef={actionRef}
				columns={columns}
				request={async (params) => {
					// 表单搜索项会从 params 传入，传递给后端接口。
					return pageV3(getPageQueryParam(params)).then(res => {
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
				rowSelection={{ ...rowSelection }}
				toolBarRender={
					() => [
						( access.canThingModelSave && <Button key="save" type="primary" icon={<PlusOutlined />} onClick={() => {
							setTitle('新增物模型')
							setReadOnly(false)
							setModalVisit(true)
							setFlag(0)
							setDataSource({
								id: undefined,
								sort: 1,
								expressionFlag: 0,
								dataType: 'integer',
								category: 1,
							})
						}}>
							新增
						</Button>),
						( access.canThingModelRemove && <Button key="remove" type="primary" danger icon={<DeleteOutlined />} onClick={() => {
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
					title: '物模型',
					tooltip: '物模型',
				}}
			/>
		</>
	);
};
