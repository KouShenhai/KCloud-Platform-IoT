import { TenantDrawer } from '@/pages/Sys/Tenant/TenantDrawer';
import {
	exportTenant,
	getTenantById,
	importTenant,
	pageTenant,
	removeTenant,
} from '@/services/admin/tenant';
import { trim } from '@/utils/format';
import { useAccess, useIntl } from '@@/exports';
import {
	DeleteOutlined,
	DownloadOutlined,
	PlusOutlined,
	UploadOutlined,
} from '@ant-design/icons';
import type { ActionType, ProColumns } from '@ant-design/pro-components';
import { ProTable } from '@ant-design/pro-components';
import type { UploadProps } from 'antd';
import { Button, message, Modal, Upload } from 'antd';
import type { TableRowSelection } from 'antd/es/table/interface';
import { useRef, useState } from 'react';
import { v7 as uuidV7 } from 'uuid';

export default () => {
	const access = useAccess();
	const intl = useIntl();
	const t = (id: string, values?: Record<string, any>) =>
		intl.formatMessage({ id }, values);

	const actionRef = useRef<ActionType | null>(null);
	const [readOnly, setReadOnly] = useState(false);
	const [modalVisit, setModalVisit] = useState(false);
	const [dataSource, setDataSource] = useState<API.TenantCO>({});
	const [title, setTitle] = useState('');
	const [requestId, setRequestId] = useState('');
	const [ids, setIds] = useState<number[]>([]);
	const [pageParam, setPageParam] = useState<any>({});
	const [exportLoading, setExportLoading] = useState(false);
	const [importLoading, setImportLoading] = useState(false);

	const reload = () => {
		actionRef?.current?.reload();
	};

	const getPageQueryParam = (params: any) => {
		const param = {
			pageSize: params?.pageSize,
			pageNum: params?.current,
			pageIndex: params?.pageSize * (params?.current - 1),
			name: trim(params?.name),
			code: trim(params?.code),
			params: {
				startTime: params?.startDate
					? `${params.startDate} 00:00:00`
					: undefined,
				endTime: params?.endDate
					? `${params.endDate} 23:59:59`
					: undefined,
			},
		};
		setPageParam(param);
		return param;
	};

	const rowSelection: TableRowSelection<API.TenantCO> = {
		onChange: (selectedRowKeys) => {
			const selectedIds: number[] = [];
			selectedRowKeys.forEach((item) => {
				selectedIds.push(item as number);
			});
			setIds(selectedIds);
		},
	};

	const showRemoveConfirm = (removeIds: number[]) => {
		Modal.confirm({
			title: t('confirm.deleteTitle'),
			content: t('confirm.deleteContent'),
			okText: t('common.ok'),
			cancelText: t('common.cancel'),
			onOk: async () => {
				if (removeIds.length === 0) {
					message.warning(t('toast.selectAtLeastOne')).then();
					return;
				}
				removeTenant(removeIds).then((res) => {
					if (res.code === 'OK') {
						message.success(t('toast.deleteSuccess')).then();
						setIds([]);
						reload();
					}
				});
			},
		});
	};

	const uploadProps: UploadProps = {
		showUploadList: false,
		accept: '.xls,.xlsx',
		beforeUpload: (file) => {
			setImportLoading(true);
			importTenant({}, [file])
				.then((res) => {
					if (res.code === 'OK') {
						message.success(t('toast.importSuccess')).then();
						setIds([]);
						reload();
					}
				})
				.finally(() => {
					setImportLoading(false);
				});
			return false;
		},
	};

	const columns: ProColumns<API.TenantCO>[] = [
		{
			title: t('common.number'),
			dataIndex: 'index',
			valueType: 'indexBorder',
			width: 85,
		},
		{
			title: t('sys.tenant.name'),
			dataIndex: 'name',
			ellipsis: true,
			fieldProps: {
				placeholder: t('sys.tenant.placeholder.name'),
			},
		},
		{
			title: t('sys.tenant.code'),
			dataIndex: 'code',
			ellipsis: true,
			fieldProps: {
				placeholder: t('sys.tenant.placeholder.code'),
			},
		},
		{
			title: t('sys.tenant.sourceId'),
			dataIndex: 'sourceId',
			hideInSearch: true,
			width: 120,
			ellipsis: true,
		},
		{
			title: t('sys.tenant.packageId'),
			dataIndex: 'packageId',
			hideInSearch: true,
			width: 120,
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
			width: 180,
			render: (_, record) => [
				access.canTenantGetDetail && (
					<a
						key="get"
						onClick={() => {
							getTenantById({ id: record?.id as number }).then(
								(res) => {
									if (res.code === 'OK') {
										setTitle(t('sys.tenant.view'));
										setModalVisit(true);
										setReadOnly(true);
										setDataSource(res?.data);
									}
								},
							);
						}}
					>
						{t('common.view')}
					</a>
				),
				access.canTenantModify && (
					<a
						key="modify"
						onClick={() => {
							getTenantById({ id: record?.id as number }).then(
								(res) => {
									if (res.code === 'OK') {
										setTitle(t('sys.tenant.modify'));
										setModalVisit(true);
										setReadOnly(false);
										setDataSource(res?.data);
									}
								},
							);
						}}
					>
						{t('common.modify')}
					</a>
				),
				access.canTenantRemove && (
					<a
						key="remove"
						onClick={() => {
							showRemoveConfirm([record?.id as number]);
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
			<TenantDrawer
				modalVisit={modalVisit}
				setModalVisit={setModalVisit}
				title={title}
				readOnly={readOnly}
				dataSource={dataSource}
				requestId={requestId}
				setRequestId={setRequestId}
				onComponent={() => {
					setIds([]);
					reload();
				}}
			/>
			<ProTable<API.TenantCO>
				actionRef={actionRef}
				columns={columns}
				request={async (params) => {
					if (!access.canTenantPage) {
						return Promise.resolve({
							data: [],
							total: 0,
							success: true,
						});
					}
					return pageTenant(getPageQueryParam(params)).then((res) => {
						return Promise.resolve({
							data: res?.data?.records,
							total: parseInt(res?.data?.total || 0),
							success: true,
						});
					});
				}}
				rowSelection={{ ...rowSelection }}
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
				toolBarRender={() => [
					access.canTenantSave && (
						<Button
							key="save"
							type="primary"
							icon={<PlusOutlined />}
							onClick={() => {
								setTitle(t('sys.tenant.insert'));
								setReadOnly(false);
								setModalVisit(true);
								setRequestId(uuidV7());
								setDataSource({
									id: undefined,
									name: '',
									code: '',
									sourceId: 0,
									packageId: 0,
								});
							}}
						>
							{t('common.insert')}
						</Button>
					),
					access.canTenantRemove && (
						<Button
							key="remove"
							type="primary"
							danger
							icon={<DeleteOutlined />}
							onClick={() => {
								showRemoveConfirm(ids);
							}}
						>
							{t('common.delete')}
						</Button>
					),
					access.canTenantImport && (
						<Upload key="import" {...uploadProps}>
							<Button
								loading={importLoading}
								icon={<UploadOutlined />}
							>
								{t('common.import')}
							</Button>
						</Upload>
					),
					access.canTenantExport && (
						<Button
							key="export"
							type="primary"
							ghost
							loading={exportLoading}
							icon={<DownloadOutlined />}
							onClick={() => {
								setExportLoading(true);
								exportTenant(pageParam).finally(() => {
									setExportLoading(false);
								});
							}}
						>
							{t('sys.commonLog.exportAll')}
						</Button>
					),
				]}
				dateFormatter="string"
				toolbar={{
					title: t('menu.sys.tenant.tenant'),
					tooltip: t('menu.sys.tenant.tenant'),
				}}
			/>
		</>
	);
};
