import { GatewayCommandModal } from '@/pages/IoT/Gateway/GatewayCommandModal';
import { GatewayDrawer } from '@/pages/IoT/Gateway/GatewayDrawer';
import {
	getGatewayById,
	pageGateway,
	removeGateway,
} from '@/services/iot/gateway';
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
	gatewayKey: string | undefined;
	name: string | undefined;
	status: number | undefined;
	productId: number | undefined;
	address: string | undefined;
	longitude: number | undefined;
	latitude: number | undefined;
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
	const [commandVisit, setCommandVisit] = useState(false);
	const [commandGateway, setCommandGateway] = useState<any>({});

	const statusOptions = [
		{ value: 0, label: t('iot.gateway.status.online') },
		{ value: 1, label: t('iot.gateway.status.offline') },
	];

	const statusMap = statusOptions.reduce<Record<number, string>>((map, item) => {
		map[item.value] = item.label;
		return map;
	}, {});

	const getPageQueryParam = (params: any) => {
		return {
			pageSize: params?.pageSize,
			pageNum: params?.current,
			pageIndex: params?.pageSize * (params?.current - 1),
			gatewayKey: trim(params?.gatewayKey),
			name: trim(params?.name),
			status: params?.status,
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
			title: t('iot.gateway.gatewayKey'),
			dataIndex: 'gatewayKey',
			valueType: 'text',
			ellipsis: true,
			fieldProps: {
				placeholder: t('iot.gateway.placeholder.gatewayKey'),
			},
		},
		{
			title: t('iot.gateway.name'),
			dataIndex: 'name',
			valueType: 'text',
			ellipsis: true,
			fieldProps: {
				placeholder: t('iot.gateway.placeholder.name'),
			},
		},
		{
			title: t('iot.gateway.status'),
			key: 'status',
			dataIndex: 'status',
			valueType: 'select',
			width: 110,
			fieldProps: {
				placeholder: t('iot.gateway.placeholder.status'),
				options: statusOptions,
			},
			render: (_, record) => {
				return record?.status !== undefined
					? statusMap[record.status] || record.status
					: '-';
			},
		},
		{
			title: t('iot.gateway.address'),
			dataIndex: 'address',
			valueType: 'text',
			ellipsis: true,
			hideInSearch: true,
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
				access.canGatewayGetDetail && (
					<a
						key="get"
						onClick={() => {
							getGatewayById({ id: record?.id }).then((res) => {
								setTitle(t('iot.gateway.view'));
								setDataSource(res?.data);
								setModalVisit(true);
								setReadOnly(true);
							});
						}}
					>
						{t('common.view')}
					</a>
				),
				access.canGatewayModify && (
					<a
						key="modify"
						onClick={() => {
							getGatewayById({ id: record?.id }).then((res) => {
								setTitle(t('iot.gateway.modify'));
								setDataSource(res?.data);
								setModalVisit(true);
								setReadOnly(false);
							});
						}}
					>
						{t('common.modify')}
					</a>
				),
				access.canGatewayCommand && (
					<a
						key="command"
						onClick={() => {
							setCommandGateway(record);
							setRequestId(uuidV7());
							setCommandVisit(true);
						}}
					>
						{t('iot.gateway.command')}
					</a>
				),
				access.canGatewayRemove && (
					<a
						key="remove"
						onClick={() => {
							Modal.confirm({
								title: t('confirm.deleteTitle'),
								content: t('confirm.deleteContent'),
								okText: t('common.ok'),
								cancelText: t('common.cancel'),
								onOk: () => {
									removeGateway([record?.id]).then((res) => {
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
			<GatewayDrawer
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

			<GatewayCommandModal
				modalVisit={commandVisit}
				setModalVisit={setCommandVisit}
				gateway={commandGateway}
				requestId={requestId}
				setRequestId={setRequestId}
			/>

			<ProTable<TableColumns>
				actionRef={actionRef}
				columns={columns}
				request={async (params) => {
					return pageGateway(getPageQueryParam(params)).then((res) => {
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
					access.canGatewaySave && (
						<Button
							key="save"
							type="primary"
							icon={<PlusOutlined />}
							onClick={() => {
								setTitle(t('iot.gateway.insert'));
								setRequestId(uuidV7());
								setReadOnly(false);
								setModalVisit(true);
								setDataSource({
									id: undefined,
									gatewayKey: '',
									name: '',
									status: 0,
									productId: undefined,
									address: '',
									longitude: undefined,
									latitude: undefined,
									remark: '',
								});
							}}
						>
							{t('common.insert')}
						</Button>
					),
					access.canGatewayRemove && (
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
										removeGateway(ids).then((res) => {
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
					title: t('menu.iot.gateway.gateway'),
					tooltip: t('menu.iot.gateway.gateway'),
				}}
			/>
		</>
	);
};
