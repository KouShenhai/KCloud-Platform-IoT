import { DictDrawer } from '@/pages/Sys/Base/DictDrawer';
import { DictItemDrawer } from '@/pages/Sys/Base/DictItemDrawer';
import {
	exportDict,
	getDictById,
	importDict,
	pageDict,
	removeDict,
} from '@/services/admin/dict';
import {
	exportDictItem,
	getDictItemById,
	importDictItem,
	pageDictItem,
	removeDictItem,
} from '@/services/admin/dictItem';
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
import {
	Button,
	Col,
	Empty,
	message,
	Modal,
	Row,
	Switch,
	Typography,
	Upload,
} from 'antd';
import type { TableRowSelection } from 'antd/es/table/interface';
import { useRef, useState } from 'react';
import { v7 as uuidV7 } from 'uuid';

export default () => {
	const access = useAccess();
	const intl = useIntl();
	const t = (id: string, values?: Record<string, any>) =>
		intl.formatMessage({ id }, values);

	const dictActionRef = useRef<ActionType | null>(null);
	const dictItemActionRef = useRef<ActionType | null>(null);

	const [dictReadOnly, setDictReadOnly] = useState(false);
	const [dictModalVisit, setDictModalVisit] = useState(false);
	const [dictDataSource, setDictDataSource] = useState<API.DictCO>({});
	const [dictTitle, setDictTitle] = useState('');
	const [dictRequestId, setDictRequestId] = useState('');
	const [dictIds, setDictIds] = useState<number[]>([]);
	const [dictParam, setDictParam] = useState<any>({});
	const [selectedDict, setSelectedDict] = useState<API.DictCO | undefined>();
	const [dictExportLoading, setDictExportLoading] = useState(false);
	const [dictImportLoading, setDictImportLoading] = useState(false);

	const [dictItemReadOnly, setDictItemReadOnly] = useState(false);
	const [dictItemModalVisit, setDictItemModalVisit] = useState(false);
	const [dictItemDataSource, setDictItemDataSource] =
		useState<API.DictItemCO>({});
	const [dictItemTitle, setDictItemTitle] = useState('');
	const [dictItemRequestId, setDictItemRequestId] = useState('');
	const [dictItemIds, setDictItemIds] = useState<number[]>([]);
	const [dictItemParam, setDictItemParam] = useState<any>({});
	const [dictItemExportLoading, setDictItemExportLoading] = useState(false);
	const [dictItemImportLoading, setDictItemImportLoading] = useState(false);
	const selectedDictId = selectedDict?.id;
	const hasSelectedDict = !!selectedDictId;

	const getDictPageQueryParam = (params: any) => {
		const param = {
			pageSize: params?.pageSize,
			pageNum: params?.current,
			pageIndex: params?.pageSize * (params?.current - 1),
			name: trim(params?.name),
			type: trim(params?.type),
			status: params?.statusValue,
			params: {
				startTime: params?.startDate
					? `${params.startDate} 00:00:00`
					: undefined,
				endTime: params?.endDate
					? `${params.endDate} 23:59:59`
					: undefined,
			},
		};
		setDictParam(param);
		return param;
	};

	const getDictItemPageQueryParam = (params: any) => {
		const param = {
			pageSize: params?.pageSize,
			pageNum: params?.current,
			pageIndex: params?.pageSize * (params?.current - 1),
			label: trim(params?.label),
			value: trim(params?.value),
			status: params?.statusValue,
			typeId: selectedDictId,
			params: {
				startTime: params?.startDate
					? `${params.startDate} 00:00:00`
					: undefined,
				endTime: params?.endDate
					? `${params.endDate} 23:59:59`
					: undefined,
			},
		};
		setDictItemParam(param);
		return param;
	};

	const reloadDict = () => {
		dictActionRef?.current?.reload();
	};

	const reloadDictItem = () => {
		dictItemActionRef?.current?.reload();
	};

	const reloadDictItemWithLatestSelection = () => {
		setTimeout(() => {
			reloadDictItem();
		});
	};

	const clearDictSelection = () => {
		setSelectedDict(undefined);
		setDictIds([]);
		setDictItemIds([]);
		setDictItemParam({});
	};

	const selectDict = (record?: API.DictCO) => {
		setSelectedDict(record);
		setDictIds(record?.id ? [record.id] : []);
		setDictItemIds([]);
		reloadDictItemWithLatestSelection();
	};

	const refreshDictItemAfterDictChange = () => {
		clearDictSelection();
		reloadDict();
		reloadDictItemWithLatestSelection();
	};

	const dictRowSelection: TableRowSelection<API.DictCO> = {
		selectedRowKeys: selectedDictId ? [selectedDictId] : [],
		type: 'radio',
		onChange: (_, selectedRows) => {
			selectDict(selectedRows?.[0]);
		},
	};

	const dictItemRowSelection: TableRowSelection<API.DictItemCO> = {
		selectedRowKeys: dictItemIds,
		onChange: (selectedRowKeys) => {
			const ids: number[] = [];
			selectedRowKeys.forEach((item) => {
				ids.push(item as number);
			});
			setDictItemIds(ids);
		},
	};

	const showDictRemoveConfirm = (ids: number[]) => {
		Modal.confirm({
			title: t('confirm.deleteTitle'),
			content: t('confirm.deleteContent'),
			okText: t('common.ok'),
			cancelText: t('common.cancel'),
			onOk: async () => {
				if (ids.length === 0) {
					message.warning(t('toast.selectAtLeastOne')).then();
					return;
				}
				removeDict(ids).then((res) => {
					if (res.code === 'OK') {
						message.success(t('toast.deleteSuccess')).then();
						refreshDictItemAfterDictChange();
					}
				});
			},
		});
	};

	const showDictItemRemoveConfirm = (ids: number[]) => {
		Modal.confirm({
			title: t('confirm.deleteTitle'),
			content: t('confirm.deleteContent'),
			okText: t('common.ok'),
			cancelText: t('common.cancel'),
			onOk: async () => {
				if (ids.length === 0) {
					message.warning(t('toast.selectAtLeastOne')).then();
					return;
				}
				removeDictItem(ids).then((res) => {
					if (res.code === 'OK') {
						message.success(t('toast.deleteSuccess')).then();
						setDictItemIds([]);
						reloadDictItem();
					}
				});
			},
		});
	};

	const uploadDictProps: UploadProps = {
		showUploadList: false,
		accept: '.xls,.xlsx',
		beforeUpload: (file) => {
			setDictImportLoading(true);
			importDict({}, [file])
				.then((res) => {
					if (res.code === 'OK') {
						message.success(t('toast.importSuccess')).then();
						refreshDictItemAfterDictChange();
					}
				})
				.finally(() => {
					setDictImportLoading(false);
				});
			return false;
		},
	};

	const uploadDictItemProps: UploadProps = {
		showUploadList: false,
		accept: '.xls,.xlsx',
		beforeUpload: (file) => {
			if (!selectedDictId) {
				message.warning(t('sys.dictItem.selectDictFirst')).then();
				return false;
			}
			setDictItemImportLoading(true);
			importDictItem({ typeId: selectedDictId }, [file])
				.then((res) => {
					if (res.code === 'OK') {
						message.success(t('toast.importSuccess')).then();
						reloadDictItem();
					}
				})
				.finally(() => {
					setDictItemImportLoading(false);
				});
			return false;
		},
	};

	const statusSearchColumn = (placeholder: string) => ({
		valueType: 'select' as const,
		fieldProps: {
			valueType: 'select',
			mode: 'single',
			placeholder,
			options: [
				{
					value: 0,
					label: t('common.enable'),
				},
				{
					value: 1,
					label: t('common.disable'),
				},
			],
		},
		ellipsis: true,
	});

	const renderSelectedDictContext = () => {
		if (!selectedDictId) {
			return (
				<Typography.Text key="selected" type="secondary">
					{t('sys.dictItem.selectDictFirst')}
				</Typography.Text>
			);
		}
	};

	const dictColumns: ProColumns<API.DictCO>[] = [
		{
			title: t('common.number'),
			dataIndex: 'index',
			valueType: 'indexBorder',
			width: 70,
		},
		{
			title: t('sys.dict.name'),
			dataIndex: 'name',
			ellipsis: true,
			width: 140,
			fieldProps: {
				placeholder: t('sys.dict.placeholder.name'),
			},
		},
		{
			title: t('sys.dict.type'),
			dataIndex: 'type',
			ellipsis: true,
			width: 150,
			fieldProps: {
				placeholder: t('sys.dict.placeholder.type'),
			},
		},
		{
			title: t('sys.dict.status'),
			key: 'statusValue',
			dataIndex: 'statusValue',
			hideInTable: true,
			...statusSearchColumn(t('sys.dict.placeholder.status')),
		},
		{
			title: t('sys.dict.status'),
			dataIndex: 'status',
			hideInSearch: true,
			width: 110,
			render: (_, record) => (
				<Switch
					checkedChildren={t('common.enable')}
					unCheckedChildren={t('common.disable')}
					disabled={true}
					checked={record?.status === 0}
				/>
			),
		},
		{
			title: t('sys.dict.remark'),
			dataIndex: 'remark',
			ellipsis: true,
			hideInSearch: true,
			width: 150,
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
				access.canDictGetDetail && (
					<a
						key="get"
						onClick={(event) => {
							event.stopPropagation();
							getDictById({ id: record?.id as number }).then(
								(res) => {
									if (res.code === 'OK') {
										setDictTitle(t('sys.dict.view'));
										setDictModalVisit(true);
										setDictReadOnly(true);
										setDictDataSource(res?.data);
									}
								},
							);
						}}
					>
						{t('common.view')}
					</a>
				),
				access.canDictModify && (
					<a
						key="modify"
						onClick={(event) => {
							event.stopPropagation();
							getDictById({ id: record?.id as number }).then(
								(res) => {
									if (res.code === 'OK') {
										setDictTitle(t('sys.dict.modify'));
										setDictModalVisit(true);
										setDictReadOnly(false);
										setDictDataSource(res?.data);
									}
								},
							);
						}}
					>
						{t('common.modify')}
					</a>
				),
				access.canDictRemove && (
					<a
						key="remove"
						onClick={(event) => {
							event.stopPropagation();
							showDictRemoveConfirm([record?.id as number]);
						}}
					>
						{t('common.delete')}
					</a>
				),
			],
		},
	];

	const dictItemColumns: ProColumns<API.DictItemCO>[] = [
		{
			title: t('common.number'),
			dataIndex: 'index',
			valueType: 'indexBorder',
			width: 70,
		},
		{
			title: t('sys.dictItem.label'),
			dataIndex: 'label',
			ellipsis: true,
			width: 150,
			fieldProps: {
				placeholder: t('sys.dictItem.placeholder.label'),
			},
		},
		{
			title: t('sys.dictItem.value'),
			dataIndex: 'value',
			ellipsis: true,
			width: 150,
			fieldProps: {
				placeholder: t('sys.dictItem.placeholder.value'),
			},
		},
		{
			title: t('sys.dictItem.status'),
			key: 'statusValue',
			dataIndex: 'statusValue',
			hideInTable: true,
			...statusSearchColumn(t('sys.dictItem.placeholder.status')),
		},
		{
			title: t('sys.dictItem.status'),
			dataIndex: 'status',
			hideInSearch: true,
			width: 110,
			render: (_, record) => (
				<Switch
					checkedChildren={t('common.enable')}
					unCheckedChildren={t('common.disable')}
					disabled={true}
					checked={record?.status === 0}
				/>
			),
		},
		{
			title: t('sys.dictItem.sort'),
			dataIndex: 'sort',
			hideInSearch: true,
			width: 90,
			ellipsis: true,
		},
		{
			title: t('sys.dictItem.remark'),
			dataIndex: 'remark',
			hideInSearch: true,
			width: 160,
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
				access.canDictItemGetDetail && (
					<a
						key="get"
						onClick={() => {
							getDictItemById({ id: record?.id as number }).then(
								(res) => {
									if (res.code === 'OK') {
										setDictItemTitle(
											t('sys.dictItem.view'),
										);
										setDictItemModalVisit(true);
										setDictItemReadOnly(true);
										setDictItemDataSource(res?.data);
									}
								},
							);
						}}
					>
						{t('common.view')}
					</a>
				),
				access.canDictItemModify && (
					<a
						key="modify"
						onClick={() => {
							getDictItemById({ id: record?.id as number }).then(
								(res) => {
									if (res.code === 'OK') {
										setDictItemTitle(
											t('sys.dictItem.modify'),
										);
										setDictItemModalVisit(true);
										setDictItemReadOnly(false);
										setDictItemDataSource(res?.data);
									}
								},
							);
						}}
					>
						{t('common.modify')}
					</a>
				),
				access.canDictItemRemove && (
					<a
						key="remove"
						onClick={() => {
							showDictItemRemoveConfirm([record?.id as number]);
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
			<DictDrawer
				modalVisit={dictModalVisit}
				setModalVisit={setDictModalVisit}
				title={dictTitle}
				readOnly={dictReadOnly}
				dataSource={dictDataSource}
				requestId={dictRequestId}
				setRequestId={setDictRequestId}
				onComponent={refreshDictItemAfterDictChange}
			/>
			<DictItemDrawer
				modalVisit={dictItemModalVisit}
				setModalVisit={setDictItemModalVisit}
				title={dictItemTitle}
				readOnly={dictItemReadOnly}
				dataSource={dictItemDataSource}
				requestId={dictItemRequestId}
				setRequestId={setDictItemRequestId}
				onComponent={() => {
					setDictItemIds([]);
					reloadDictItem();
				}}
			/>
			<Row gutter={[16, 16]} align="top">
				<Col xs={24} xl={12} style={{ minWidth: 0 }}>
					<ProTable<API.DictCO>
						actionRef={dictActionRef}
						columns={dictColumns}
						request={async (params) => {
							if (!access.canDictPage) {
								return Promise.resolve({
									data: [],
									total: 0,
									success: true,
								});
							}
							return pageDict(getDictPageQueryParam(params)).then(
								(res) => {
									return Promise.resolve({
										data: res?.data?.records,
										total: parseInt(res?.data?.total || 0),
										success: true,
									});
								},
							);
						}}
						rowSelection={{ ...dictRowSelection }}
						onRow={(record) => ({
							onClick: () => {
								selectDict(record);
							},
							style: { cursor: 'pointer' },
						})}
						rowKey="id"
						scroll={{ x: 860 }}
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
							access.canDictSave && (
								<Button
									key="save"
									type="primary"
									icon={<PlusOutlined />}
									onClick={() => {
										setDictTitle(t('sys.dict.insert'));
										setDictReadOnly(false);
										setDictModalVisit(true);
										setDictRequestId(uuidV7());
										setDictDataSource({
											id: undefined,
											name: '',
											type: '',
											remark: '',
											status: 0,
										});
									}}
								>
									{t('common.insert')}
								</Button>
							),
							access.canDictRemove && (
								<Button
									key="remove"
									type="primary"
									danger
									icon={<DeleteOutlined />}
									onClick={() => {
										showDictRemoveConfirm(dictIds);
									}}
								>
									{t('common.delete')}
								</Button>
							),
							access.canDictImport && (
								<Upload key="import" {...uploadDictProps}>
									<Button
										loading={dictImportLoading}
										icon={<UploadOutlined />}
									>
										{t('common.import')}
									</Button>
								</Upload>
							),
							access.canDictExport && (
								<Button
									key="export"
									type="primary"
									ghost
									loading={dictExportLoading}
									icon={<DownloadOutlined />}
									onClick={() => {
										setDictExportLoading(true);
										exportDict(dictParam).finally(() => {
											setDictExportLoading(false);
										});
									}}
								>
									{t('sys.commonLog.exportAll')}
								</Button>
							),
						]}
						dateFormatter="string"
						toolbar={{
							title: t('sys.dict.title'),
							tooltip: t('sys.dict.title'),
						}}
					/>
				</Col>
				<Col xs={24} xl={12} style={{ minWidth: 0 }}>
					<ProTable<API.DictItemCO>
						actionRef={dictItemActionRef}
						columns={dictItemColumns}
						request={async (params) => {
							if (!selectedDictId || !access.canDictItemPage) {
								setDictItemParam({});
								return Promise.resolve({
									data: [],
									total: 0,
									success: true,
								});
							}
							return pageDictItem(
								getDictItemPageQueryParam(params),
							).then((res) => {
								return Promise.resolve({
									data: res?.data?.records,
									total: parseInt(res?.data?.total || 0),
									success: true,
								});
							});
						}}
						rowSelection={{ ...dictItemRowSelection }}
						rowKey="id"
						scroll={{ x: 1000 }}
						pagination={{
							showQuickJumper: true,
							showSizeChanger: false,
							pageSize: 10,
						}}
						search={{
							layout: 'vertical',
							defaultCollapsed: true,
						}}
						locale={{
							emptyText: selectedDict?.id ? (
								<Empty image={Empty.PRESENTED_IMAGE_SIMPLE} />
							) : (
								<Empty
									image={Empty.PRESENTED_IMAGE_SIMPLE}
									description={t(
										'sys.dictItem.selectDictFirst',
									)}
								/>
							),
						}}
						toolBarRender={() => [
							renderSelectedDictContext(),
							access.canDictItemSave && (
								<Button
									key="save"
									type="primary"
									icon={<PlusOutlined />}
									disabled={!hasSelectedDict}
									onClick={() => {
										if (!selectedDictId) {
											message
												.warning(
													t(
														'sys.dictItem.selectDictFirst',
													),
												)
												.then();
											return;
										}
										setDictItemTitle(
											t('sys.dictItem.insert'),
										);
										setDictItemReadOnly(false);
										setDictItemModalVisit(true);
										setDictItemRequestId(uuidV7());
										setDictItemDataSource({
											id: undefined,
											label: '',
											value: '',
											sort: 1,
											remark: '',
											status: 0,
											typeId: selectedDictId,
										});
									}}
								>
									{t('common.insert')}
								</Button>
							),
							access.canDictItemRemove && (
								<Button
									key="remove"
									type="primary"
									danger
									icon={<DeleteOutlined />}
									disabled={!hasSelectedDict}
									onClick={() => {
										showDictItemRemoveConfirm(dictItemIds);
									}}
								>
									{t('common.delete')}
								</Button>
							),
							access.canDictItemImport && (
								<Upload key="import" {...uploadDictItemProps}>
									<Button
										loading={dictItemImportLoading}
										disabled={!hasSelectedDict}
										icon={<UploadOutlined />}
									>
										{t('common.import')}
									</Button>
								</Upload>
							),
							access.canDictItemExport && (
								<Button
									key="export"
									type="primary"
									ghost
									disabled={!hasSelectedDict}
									loading={dictItemExportLoading}
									icon={<DownloadOutlined />}
									onClick={() => {
										if (!selectedDictId) {
											message
												.warning(
													t(
														'sys.dictItem.selectDictFirst',
													),
												)
												.then();
											return;
										}
										setDictItemExportLoading(true);
										exportDictItem({
											...dictItemParam,
											typeId: selectedDictId,
										}).finally(() => {
											setDictItemExportLoading(false);
										});
									}}
								>
									{t('sys.commonLog.exportAll')}
								</Button>
							),
						]}
						dateFormatter="string"
						toolbar={{
							title: t('sys.dictItem.title'),
							tooltip: selectedDict?.type || t('sys.dictItem.selectDictFirst'),
						}}
					/>
				</Col>
			</Row>
		</>
	);
};
