import { DeviceDrawer } from '@/pages/IoT/Device/DeviceDrawer';
import {
	exportDevice,
	getDeviceById,
	importDevice,
	pageDevice,
	removeDevice,
} from '@/services/iot/device';
import { pageProduct } from '@/services/iot/product';
import { trim } from '@/utils/format';
import { useAccess, useIntl } from '@@/exports';
import {
	DeleteOutlined,
	DownloadOutlined,
	PlusOutlined,
	UploadOutlined,
} from '@ant-design/icons';
import { ProColumns, ProTable } from '@ant-design/pro-components';
import type { ActionType } from '@ant-design/pro-components';
import { Button, message, Modal, Upload } from 'antd';
import type { UploadProps } from 'antd';
import { TableRowSelection } from 'antd/es/table/interface';
import { useRef, useState } from 'react';
import { v7 as uuidV7 } from 'uuid';

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
	const [productMap, setProductMap] = useState<Record<number, string>>({});
	const [requestId, setRequestId] = useState('');
	const [importLoading, setImportLoading] = useState(false);
	const [exportLoading, setExportLoading] = useState(false);
	const [exportParam, setExportParam] = useState<any>({});

	type TableColumns = {
		id: number;
		sn: string | undefined;
		name: string | undefined;
		status: number | undefined;
		productId: number | undefined;
		longitude: number | undefined;
		latitude: number | undefined;
		imgUrl: string | undefined;
		address: string | undefined;
		remark: string | undefined;
		createTime: string | undefined;
	};

	const getPageQueryParam = (params: any) => {
		const param = {
			pageSize: params?.pageSize,
			pageNum: params?.current,
			pageIndex: params?.pageSize * (params?.current - 1),
			sn: trim(params?.sn),
			name: trim(params?.name),
			status: params?.status,
			productId: params?.productId,
			params: {
				startTime: params?.startDate
					? `${params.startDate} 00:00:00`
					: undefined,
				endTime: params?.endDate
					? `${params.endDate} 23:59:59`
					: undefined,
			},
		};
		setExportParam(param);
		return param;
	};

	const uploadProps: UploadProps = {
		showUploadList: false,
		accept: '.xls,.xlsx',
		beforeUpload: (file) => {
			setImportLoading(true);
			importDevice({}, [file])
				.then((res) => {
					if (res.code === 'OK') {
						message.success(t('toast.importSuccess')).then();
						// @ts-ignore
						actionRef?.current?.reload();
					}
				})
				.finally(() => {
					setImportLoading(false);
				});
			return false;
		},
	};

	const loadProductOptions = async () => {
		return pageProduct({
			pageNum: 1,
			pageSize: 1000,
			pageIndex: 0,
		}).then((res) => {
			const records = res?.data?.records || [];
			const map: Record<number, string> = {};
			const options = records.map((item: any) => {
				map[item?.id] = item?.name;
				return { label: item?.name, value: item?.id };
			});
			setProductMap(map);
			return options;
		});
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
			title: t('iot.device.sn'),
			dataIndex: 'sn',
			valueType: 'text',
			ellipsis: true,
			fieldProps: {
				placeholder: t('iot.device.placeholder.sn'),
			},
		},
		{
			title: t('iot.device.name'),
			dataIndex: 'name',
			valueType: 'text',
			ellipsis: true,
			fieldProps: {
				placeholder: t('iot.device.placeholder.name'),
			},
		},
		{
			title: t('iot.device.status'),
			key: 'status',
			dataIndex: 'status',
			valueType: 'select',
			ellipsis: true,
			fieldProps: {
				placeholder: t('iot.device.placeholder.status'),
				options: [
					{
						value: 0,
						label: t('iot.device.status.online'),
					},
					{
						value: 1,
						label: t('iot.device.status.offline'),
					},
				],
			},
		},
		{
			title: t('iot.device.productId'),
			key: 'productId',
			dataIndex: 'productId',
			valueType: 'select',
			ellipsis: true,
			fieldProps: {
				placeholder: t('iot.device.placeholder.productId'),
			},
			request: async () => {
				return loadProductOptions();
			},
			render: (_, record) => {
				return record?.productId !== undefined
					? productMap[record.productId] || record.productId
					: '-';
			},
		},
		{
			title: t('iot.device.address'),
			dataIndex: 'address',
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
				access.canDeviceGetDetail && (
					<a
						key="get"
						onClick={() => {
							getDeviceById({ id: record?.id }).then((res) => {
								setTitle(t('iot.device.view'));
								setDataSource(res?.data);
								setModalVisit(true);
								setReadOnly(true);
							});
						}}
					>
						{t('common.view')}
					</a>
				),
				access.canDeviceModify && (
					<a
						key="modify"
						onClick={() => {
							getDeviceById({ id: record?.id }).then((res) => {
								setTitle(t('iot.device.modify'));
								setDataSource(res?.data);
								setModalVisit(true);
								setReadOnly(false);
							});
						}}
					>
						{t('common.modify')}
					</a>
				),
				access.canDeviceRemove && (
					<a
						key="remove"
						onClick={() => {
							Modal.confirm({
								title: t('confirm.deleteTitle'),
								content: t('confirm.deleteContent'),
								okText: t('common.ok'),
								cancelText: t('common.cancel'),
								onOk: () => {
									removeDevice([record?.id]).then((res) => {
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
			<DeviceDrawer
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
					// 表单搜索项会从 params 传入，传递给后端接口。
					return pageDevice(getPageQueryParam(params)).then((res) => {
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
					access.canDeviceSave && (
						<Button
							key="save"
							type="primary"
							icon={<PlusOutlined />}
							onClick={() => {
								setTitle(t('iot.device.insert'));
								setRequestId(uuidV7());
								setReadOnly(false);
								setModalVisit(true);
								setDataSource({
									id: undefined,
									sn: '',
									name: '',
									status: undefined,
									productId: undefined,
									longitude: undefined,
									latitude: undefined,
									imgUrl: '',
									address: '',
									remark: '',
								});
							}}
						>
							{t('common.insert')}
						</Button>
					),
					access.canDeviceRemove && (
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
										removeDevice(ids).then((res) => {
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
					access.canDeviceImport && (
						<Upload key="import" {...uploadProps}>
							<Button
								loading={importLoading}
								icon={<UploadOutlined />}
							>
								{t('common.import')}
							</Button>
						</Upload>
					),
					access.canDeviceExport && (
						<Button
							key="export"
							type="primary"
							ghost
							loading={exportLoading}
							icon={<DownloadOutlined />}
							onClick={() => {
								setExportLoading(true);
								exportDevice(exportParam).finally(() => {
									setExportLoading(false);
								});
							}}
						>
							{t('iot.device.export')}
						</Button>
					),
				]}
				dateFormatter="string"
				toolbar={{
					title: t('menu.iot.device.device'),
					tooltip: t('menu.iot.device.device'),
				}}
			/>
		</>
	);
};
