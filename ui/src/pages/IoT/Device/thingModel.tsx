import { ThingModelDrawer } from '@/pages/IoT/Device/ThingModelDrawer';
import {
	getThingModelById,
	pageThingModel,
	removeThingModel,
} from '@/services/iot/thingModel';
import { trim } from '@/utils/format';
import { useAccess, useIntl } from '@@/exports';
import { DeleteOutlined, PlusOutlined } from '@ant-design/icons';
import { ProColumns, ProTable } from '@ant-design/pro-components';
import type { ActionType } from '@ant-design/pro-components';
import { Button, message, Modal } from 'antd';
import { TableRowSelection } from 'antd/es/table/interface';
import {useEffect, useRef, useState} from 'react';
import { v7 as uuidV7 } from 'uuid';
import {listDictItem} from "@/services/admin/dictItem";

export default () => {
	const access = useAccess();
	const intl = useIntl();
	const t = (id: string, values?: Record<string, any>) =>
		intl.formatMessage({ id }, values);
	const actionRef = useRef<ActionType | null>(null);
	const [modalVisit, setModalVisit] = useState(false);
	const [dataSource, setDataSource] = useState<any>({});
	const [title, setTitle] = useState('');
	const [readOnly, setReadOnly] = useState(false);
	const [ids, setIds] = useState<any>([]);
	const [dataType, setDataType] = useState('int');
	const [dataUnitOptions, setDataUnitOptions] = useState<any>([]);
	const [dataTypeOptions, setDataTypeOptions] = useState<any>([]);
	const [requestId, setRequestId] = useState('');

	type TableColumns = {
		id: number;
		code: string | undefined;
		name: string | undefined;
		sort: number | undefined;
		dataType: string | undefined;
		spec: string | undefined;
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
			params: {
				startTime: params?.startDate
					? `${params.startDate} 00:00:00`
					: undefined,
				endTime: params?.endDate
					? `${params.endDate} 23:59:59`
					: undefined,
			},
		};
	};

	const getDataType = async () => {
		const  res = await listDictItem({dictCode: 'thing_model_data_type'});
		const options: any[] = [];
		res?.data?.forEach((item: any) => {
			options.push({label: item.name, value: item.code})
		})
		setDataTypeOptions(options)
	}

	const getDataUnit = async () => {
		const  res = await listDictItem({dictCode: 'thing_model_data_unit'});
		const options: any[] = [];
		res?.data?.forEach((item: any) => {
			options.push({label: item.name, value: item.code})
		})
		setDataUnitOptions(options)
	}

	const getData = (data: any) => {
		const spec = JSON.parse(data?.spec);
		data.length = spec?.length;
		data.trueText = spec?.trueText;
		data.falseText = spec?.falseText;
		data.min = spec?.min;
		data.max = spec?.max;
		data.unit = spec?.unit;
		return data;
	};

	const rowSelection: TableRowSelection<TableColumns> = {
		onChange: (selectedRowKeys) => {
			const ids: number[] = [];
			selectedRowKeys.forEach((item) => {
				ids.push(item as number);
			});
			setIds(ids);
		},
	};

	useEffect(() => {
		getDataType().catch(console.log)
		getDataUnit().catch(console.log)
	}, []);

	const columns: ProColumns<TableColumns>[] = [
		{
			title: t('common.number'),
			dataIndex: 'index',
			valueType: 'indexBorder',
			width: 85,
		},
		{
			title: t('iot.thingModel.code'),
			dataIndex: 'code',
			valueType: 'text',
			ellipsis: true,
			fieldProps: {
				placeholder: t('iot.thingModel.placeholder.code'),
			},
		},
		{
			title: t('iot.thingModel.name'),
			dataIndex: 'name',
			valueType: 'text',
			ellipsis: true,
			fieldProps: {
				placeholder: t('iot.thingModel.placeholder.name'),
			},
		},
		{
			title: t('iot.thingModel.dataType'),
			key: 'dataType',
			dataIndex: 'dataType',
			valueType: 'select',
			fieldProps: {
				valueType: 'select',
				mode: 'single',
				placeholder: t('iot.thingModel.placeholder.dataType'),
				options: dataTypeOptions,
			},
			ellipsis: true,
		},
		{
			title: t('iot.thingModel.spec'),
			dataIndex: 'spec',
			valueType: 'text',
			hideInSearch: true,
			ellipsis: true,
			renderFormItem: (_, { defaultRender }) => {
				return defaultRender(_);
			},
			render: (_, record) => {
				const data = JSON.parse(
					typeof record?.spec === 'string' ? record?.spec : '{}',
				);
				return (
					<>
						{(record?.dataType === 'int' || record?.dataType === 'long' || record?.dataType === 'float' || record?.dataType === 'double') && (
							<div>
								最小值：
								<span style={{ color: '#fd5251' }}>
									{data?.min}
								</span>
							</div>
						)}
						{(record?.dataType === 'int' || record?.dataType === 'long' || record?.dataType === 'float' || record?.dataType === 'double') && (
							<div>
								最大值：
								<span style={{ color: '#fd5251' }}>
									{data?.max}
								</span>
							</div>
						)}
						{(record?.dataType === 'int' || record?.dataType === 'long' || record?.dataType === 'float' || record?.dataType === 'double') && (
							<div>
								单位：
								<span style={{ color: '#fd5251' }}>
									{data?.unit}
								</span>
							</div>
						)}
						{record?.dataType === 'boolean' && (
							<div>
								{t('iot.thingModel.falseText')}：
								<span style={{ color: '#fd5251' }}>
									{data?.falseText}
								</span>
							</div>
						)}
						{record?.dataType === 'boolean' && (
							<div>
								{t('iot.thingModel.trueText')}：
								<span style={{ color: '#fd5251' }}>
									{data?.trueText}
								</span>
							</div>
						)}
						{record?.dataType === 'text' && (
							<div>
								{t('iot.thingModel.length')}：
								<span style={{ color: '#fd5251' }}>
									{data?.length}
								</span>
							</div>
						)}
					</>
				);
			},
		},
		{
			title: t('iot.thingModel.sort'),
			dataIndex: 'sort',
			valueType: 'text',
			hideInSearch: true,
			ellipsis: true,
		},
		{
			title: t('iot.thingModel.remark'),
			dataIndex: 'remark',
			valueType: 'text',
			hideInSearch: true,
			ellipsis: true,
		},
		{
			title: t('common.createTime'),
			key: 'createTime',
			dataIndex: 'createTime',
			valueType: 'dateTime',
			hideInSearch: true,
			width: 160,
			ellipsis: true,
		},
		{
			title: t('common.createTime'),
			dataIndex: 'createTimeValue',
			valueType: 'dateRange',
			hideInTable: true,
			fieldProps: {
				placeholder: [t('common.selectStartTime'), t('common.selectEndTime')],
			},
			search: {
				transform: (value) => {
					return {
						startDate: value[0],
						endDate: value[1],
					};
				},
			},
		},
		{
			title: t('common.operation'),
			valueType: 'option',
			key: 'option',
			render: (_, record) => [
				access.canThingModelGetDetail && (
					<a
						key="get"
						onClick={() => {
							getThingModelById({ id: record?.id }).then(
								(res) => {
									setTitle(t('iot.thingModel.view'));
									const data = getData(res?.data);
									setDataSource(data);
									setModalVisit(true);
									setReadOnly(true);
									setDataType(data?.dataType);
								},
							);
						}}
					>
						{t('common.view')}
					</a>
				),
				access.canThingModelModify && (
					<a
						key="modify"
						onClick={() => {
							getThingModelById({ id: record?.id }).then(
								(res) => {
									setTitle(t('iot.thingModel.modify'));
									const data = getData(res?.data);
									setDataSource(data);
									setModalVisit(true);
									setReadOnly(false);
									setDataType(data?.dataType);
								},
							);
						}}
					>
						{t('common.modify')}
					</a>
				),
				access.canThingModelRemove && (
					<a
						key="remove"
						onClick={() => {
							Modal.confirm({
								title: t('confirm.deleteTitle'),
								content: t('confirm.deleteContent'),
								okText: t('common.ok'),
								cancelText: t('common.cancel'),
								onOk: () => {
									removeThingModel([record?.id]).then(
										(res) => {
											if (res.code === 'OK') {
												message
													.success(t('toast.deleteSuccess'))
													.then();
												// @ts-ignore
												actionRef?.current?.reload();
											}
										},
									);
								},
							});
						}}
					>
						{t('common.delete')}
					</a>
				),
			],
		},
	];

	return (
		<>
			<ThingModelDrawer
				dataTypeOptions={dataTypeOptions}
				modalVisit={modalVisit}
				setModalVisit={setModalVisit}
				title={title}
				readOnly={readOnly}
				dataSource={dataSource}
				dataUnitOptions={dataUnitOptions}
				onComponent={async () => {
					// @ts-ignore
					actionRef?.current?.reload();
				}}
				setDataType={setDataType}
				dataType={dataType}
				requestId={requestId}
				setRequestId={setRequestId}
			/>

			<ProTable<TableColumns>
				actionRef={actionRef}
				columns={columns}
				request={async (params) => {
					// 表单搜索项会从 params 传入，传递给后端接口。
					return pageThingModel(getPageQueryParam(params)).then(
						(res) => {
							return Promise.resolve({
								data: res?.data?.records,
								total: parseInt(res?.data?.total || 0),
								success: true,
							});
						},
					);
				}}
				rowKey="id"
				pagination={{
					showQuickJumper: true,
					showSizeChanger: false,
					pageSize: 10,
				}}
				search={{
					layout: 'vertical',
					defaultCollapsed: true,
				}}
				rowSelection={{ ...rowSelection }}
				toolBarRender={() => [
					access.canThingModelSave && (
						<Button
							key="save"
							type="primary"
							icon={<PlusOutlined />}
							onClick={() => {
								setTitle(t('iot.thingModel.insert'));
								setRequestId(uuidV7());
								setReadOnly(false);
								setModalVisit(true);
								setDataType('int')
								setDataSource({
									id: undefined,
									sort: 1,
									dataType: 'int'
								});
							}}
						>
							{t('common.insert')}
						</Button>
					),
					access.canThingModelRemove && (
						<Button
							key="remove"
							type="primary"
							danger
							icon={<DeleteOutlined />}
							onClick={() => {
								Modal.confirm({
									title: t('confirm.deleteTitle'),
									content: t('confirm.deleteContent'),
									okText: t('common.ok'),
									cancelText: t('common.cancel'),
									onOk: async () => {
										if (ids.length === 0) {
											message
												.warning(t('toast.selectAtLeastOne'))
												.then();
											return;
										}
										removeThingModel(ids).then((res) => {
											if (res.code === 'OK') {
												message
													.success(t('toast.deleteSuccess'))
													.then();
												// @ts-ignore
												actionRef?.current?.reload();
											}
										});
									},
								});
							}}
						>
							{t('common.delete')}
						</Button>
					),
				]}
				dateFormatter="string"
				toolbar={{
					title: t('menu.iot.device.thingModel'),
					tooltip: t('menu.iot.device.thingModel'),
				}}
			/>
		</>
	);
};
