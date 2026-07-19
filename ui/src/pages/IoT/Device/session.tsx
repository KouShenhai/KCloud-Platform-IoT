import { SessionDrawer } from '@/pages/IoT/Device/SessionDrawer';
import {
	getConnectionById,
	pageConnection,
	removeConnection,
} from '@/services/iot/connection';
import { trim } from '@/utils/format';
import { useAccess, useIntl } from '@@/exports';
import { DeleteOutlined, PlusOutlined } from '@ant-design/icons';
import { ProColumns, ProTable } from '@ant-design/pro-components';
import type { ActionType } from '@ant-design/pro-components';
import { Button, message, Modal } from 'antd';
import { TableRowSelection } from 'antd/es/table/interface';
import { useRef, useState } from 'react';
import { v7 as uuidV7 } from 'uuid';

type TableColumns = {
	id: number;
	name: string | undefined;
	type: number | undefined;
	host: string | undefined;
	port: number | undefined;
	enabled: number | undefined;
	config: string | undefined;
	remark: string | undefined;
	createTime: string | undefined;
};

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
	const [requestId, setRequestId] = useState('');

	const typeOptions = [
		{ value: 1, label: t('network.connection.type.mqttServer') },
		{ value: 2, label: t('network.connection.type.httpServer') },
		{ value: 3, label: t('network.connection.type.mqttClient') },
		{ value: 4, label: t('network.connection.type.kafka') },
		{ value: 5, label: t('network.connection.type.rabbitmq') },
	];

	const enabledOptions = [
		{ value: 0, label: t('network.connection.enabled.enabled') },
		{ value: 1, label: t('network.connection.enabled.disabled') },
	];

	const typeMap = typeOptions.reduce<Record<number, string>>((map, item) => {
		map[item.value] = item.label;
		return map;
	}, {});

	const enabledMap = enabledOptions.reduce<Record<number, string>>((map, item) => {
		map[item.value] = item.label;
		return map;
	}, {});

	const getPageQueryParam = (params: any) => {
		return {
			pageSize: params?.pageSize,
			pageNum: params?.current,
			pageIndex: params?.pageSize * (params?.current - 1),
			name: trim(params?.name),
			type: params?.type,
			enabled: params?.enabled,
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

	const rowSelection: TableRowSelection<TableColumns> = {
		onChange: (selectedRowKeys) => {
			const ids: number[] = [];
			selectedRowKeys.forEach((item) => {
				ids.push(item as number);
			});
			setIds(ids);
		},
	};

	const columns: ProColumns<TableColumns>[] = [
		{
			title: t('common.number'),
			dataIndex: 'index',
			valueType: 'indexBorder',
			width: 85,
		},
		{
			title: t('network.connection.name'),
			dataIndex: 'name',
			valueType: 'text',
			ellipsis: true,
			fieldProps: {
				placeholder: t('network.connection.placeholder.name'),
			},
		},
		{
			title: t('network.connection.type'),
			key: 'type',
			dataIndex: 'type',
			valueType: 'select',
			ellipsis: true,
			fieldProps: {
				placeholder: t('network.connection.placeholder.type'),
				options: typeOptions,
			},
			render: (_, record) => {
				return record?.type !== undefined ? typeMap[record.type] || record.type : '-';
			},
		},
		{
			title: t('network.connection.host'),
			dataIndex: 'host',
			valueType: 'text',
			ellipsis: true,
			hideInSearch: true,
		},
		{
			title: t('network.connection.port'),
			dataIndex: 'port',
			valueType: 'digit',
			hideInSearch: true,
			width: 90,
		},
		{
			title: t('network.connection.enabled'),
			key: 'enabled',
			dataIndex: 'enabled',
			valueType: 'select',
			width: 110,
			fieldProps: {
				placeholder: t('network.connection.placeholder.enabled'),
				options: enabledOptions,
			},
			render: (_, record) => {
				return record?.enabled !== undefined
					? enabledMap[record.enabled] || record.enabled
					: '-';
			},
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
			dataIndex: 'createTime',
			valueType: 'dateRange',
			hideInTable: true,
			fieldProps: {
				placeholder: [
					t('common.selectStartTime'),
					t('common.selectEndTime'),
				],
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
				access.canConnectionGetDetail && (
					<a
						key="get"
						onClick={() => {
							getConnectionById({ id: record?.id }).then((res) => {
								setTitle(t('network.connection.view'));
								setDataSource(res?.data);
								setModalVisit(true);
								setReadOnly(true);
							});
						}}
					>
						{t('common.view')}
					</a>
				),
				access.canConnectionModify && (
					<a
						key="modify"
						onClick={() => {
							getConnectionById({ id: record?.id }).then((res) => {
								setTitle(t('network.connection.modify'));
								setDataSource(res?.data);
								setModalVisit(true);
								setReadOnly(false);
							});
						}}
					>
						{t('common.modify')}
					</a>
				),
				access.canConnectionRemove && (
					<a
						key="remove"
						onClick={() => {
							Modal.confirm({
								title: t('confirm.deleteTitle'),
								content: t('confirm.deleteContent'),
								okText: t('common.ok'),
								cancelText: t('common.cancel'),
								onOk: () => {
									removeConnection([record?.id]).then((res) => {
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
					</a>
				),
			],
		},
	];

	return (
		<>
			<SessionDrawer
				modalVisit={modalVisit}
				setModalVisit={setModalVisit}
				title={title}
				readOnly={readOnly}
				dataSource={dataSource}
				onComponent={async () => {
					// @ts-ignore
					actionRef?.current?.reload();
				}}
				requestId={requestId}
				setRequestId={setRequestId}
			/>

			<ProTable<TableColumns>
				actionRef={actionRef}
				columns={columns}
				request={async (params) => {
					return pageConnection(getPageQueryParam(params)).then((res) => {
						return Promise.resolve({
							data: res?.data?.records,
							total: parseInt(res?.data?.total || 0),
							success: true,
						});
					});
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
					access.canConnectionSave && (
						<Button
							key="save"
							type="primary"
							icon={<PlusOutlined />}
							onClick={() => {
								setTitle(t('network.connection.insert'));
								setRequestId(uuidV7());
								setReadOnly(false);
								setModalVisit(true);
								setDataSource({
									id: undefined,
									name: '',
									type: 1,
									host: '',
									port: undefined,
									enabled: 0,
									config: '{}',
									remark: '',
								});
							}}
						>
							{t('common.insert')}
						</Button>
					),
					access.canConnectionRemove && (
						<Button
							key="remove"
							type="primary"
							danger
							icon={<DeleteOutlined />}
							onClick={() => {
								if (ids.length === 0) {
									message.warning(t('toast.selectAtLeastOne')).then();
									return;
								}
								Modal.confirm({
									title: t('confirm.deleteTitle'),
									content: t('confirm.deleteContent'),
									okText: t('common.ok'),
									cancelText: t('common.cancel'),
									onOk: async () => {
										removeConnection(ids).then((res) => {
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
					title: t('menu.iot.device.session'),
					tooltip: t('menu.iot.device.session'),
				}}
			/>
		</>
	);
};
