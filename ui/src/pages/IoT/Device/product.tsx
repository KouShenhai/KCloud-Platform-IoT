import { ProductDrawer } from '@/pages/IoT/Device/ProductDrawer';
import {
	getProductById,
	pageProduct,
	removeProduct,
} from '@/services/iot/product';
import { pageProductCategory } from '@/services/iot/productCategory';
import { trim } from '@/utils/format';
import { useAccess, useIntl } from '@@/exports';
import { DeleteOutlined, PlusOutlined } from '@ant-design/icons';
import { ProColumns, ProTable } from '@ant-design/pro-components';
import type { ActionType } from '@ant-design/pro-components';
import { Button, message, Modal } from 'antd';
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
	const [categoryMap, setCategoryMap] = useState<Record<number, string>>({});
	const [requestId, setRequestId] = useState('');

	type TableColumns = {
		id: number;
		name: string | undefined;
		categoryId: number | undefined;
		deviceType: number | undefined;
		imgUrl: string | undefined;
		cpId: number | undefined;
		tpId: number | undefined;
		remark: string | undefined;
		createTime: string | undefined;
	};

	const getPageQueryParam = (params: any) => {
		return {
			pageSize: params?.pageSize,
			pageNum: params?.current,
			pageIndex: params?.pageSize * (params?.current - 1),
			name: trim(params?.name),
			categoryId: params?.categoryId,
			deviceType: params?.deviceType,
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

	const loadCategoryOptions = async () => {
		return pageProductCategory({
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
			setCategoryMap(map);
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
			title: t('iot.product.name'),
			dataIndex: 'name',
			valueType: 'text',
			ellipsis: true,
			fieldProps: {
				placeholder: t('iot.product.placeholder.name'),
			},
		},
		{
			title: t('iot.product.categoryId'),
			key: 'categoryId',
			dataIndex: 'categoryId',
			valueType: 'select',
			ellipsis: true,
			fieldProps: {
				placeholder: t('iot.product.placeholder.categoryId'),
			},
			request: async () => {
				return loadCategoryOptions();
			},
			render: (_, record) => {
				return record?.categoryId !== undefined
					? categoryMap[record.categoryId] || record.categoryId
					: '-';
			},
		},
		{
			title: t('iot.product.deviceType'),
			key: 'deviceType',
			dataIndex: 'deviceType',
			valueType: 'select',
			ellipsis: true,
			fieldProps: {
				placeholder: t('iot.product.placeholder.deviceType'),
				options: [
					{
						value: 1,
						label: t('iot.product.deviceType.direct'),
					},
					{
						value: 2,
						label: t('iot.product.deviceType.gateway'),
					},
					{
						value: 3,
						label: t('iot.product.deviceType.monitor'),
					},
				],
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
				access.canProductGetDetail && (
					<a
						key="get"
						onClick={() => {
							getProductById({ id: record?.id }).then((res) => {
								setTitle(t('iot.product.view'));
								setDataSource(res?.data);
								setModalVisit(true);
								setReadOnly(true);
							});
						}}
					>
						{t('common.view')}
					</a>
				),
				access.canProductModify && (
					<a
						key="modify"
						onClick={() => {
							getProductById({ id: record?.id }).then((res) => {
								setTitle(t('iot.product.modify'));
								setDataSource(res?.data);
								setModalVisit(true);
								setReadOnly(false);
							});
						}}
					>
						{t('common.modify')}
					</a>
				),
				access.canProductRemove && (
					<a
						key="remove"
						onClick={() => {
							Modal.confirm({
								title: t('confirm.deleteTitle'),
								content: t('confirm.deleteContent'),
								okText: t('common.ok'),
								cancelText: t('common.cancel'),
								onOk: () => {
									removeProduct([record?.id]).then((res) => {
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
			<ProductDrawer
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
					return pageProduct(getPageQueryParam(params)).then((res) => {
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
					access.canProductSave && (
						<Button
							key="save"
							type="primary"
							icon={<PlusOutlined />}
							onClick={() => {
								setTitle(t('iot.product.insert'));
								setRequestId(uuidV7());
								setReadOnly(false);
								setModalVisit(true);
								setDataSource({
									id: undefined,
									name: '',
									categoryId: undefined,
									deviceType: undefined,
									imgUrl: '',
									cpId: undefined,
									tpId: undefined,
									remark: '',
								});
							}}
						>
							{t('common.insert')}
						</Button>
					),
					access.canProductRemove && (
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
										removeProduct(ids).then((res) => {
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
					title: t('menu.iot.device.product'),
					tooltip: t('menu.iot.device.product'),
				}}
			/>
		</>
	);
};
