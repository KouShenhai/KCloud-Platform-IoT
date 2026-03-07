import { ProductCategoryDrawer } from '@/pages/IoT/Device/ProductCategoryDrawer';
import {
	getProductCategoryById,
	listTreeProductCategory,
	removeProductCategory,
} from '@/services/iot/productCategory';
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
	const [treeList, setTreeList] = useState<any[]>([]);
	const [requestId, setRequestId] = useState('');

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
			title: t('iot.productCategory.name'),
			dataIndex: 'name',
			ellipsis: true,
			valueType: 'text',
			hideInSearch: true,
		},
		{
			title: t('iot.productCategory.sort'),
			dataIndex: 'sort',
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
				access.canProductCategoryGetDetail && (
					<a
						key="get"
						onClick={() => {
							getProductCategoryById({ id: record?.id }).then(
								(res) => {
									setTitle(t('iot.productCategory.view'));
									setDataSource(res?.data);
									setModalVisit(true);
									setReadOnly(true);
								},
							);
						}}
					>
						{t('common.view')}
					</a>
				),
				access.canProductCategorySave && (
					<a
						key="save"
						onClick={() => {
							setTitle(t('iot.productCategory.insert'));
							setRequestId(uuidV7());
							setReadOnly(false);
							setModalVisit(true);
							setDataSource({
								sort: 1,
								pid: record?.id,
								id: undefined,
								name: '',
								remark: '',
							});
						}}
					>
						{t('common.insert')}
					</a>
				),
				access.canProductCategoryModify && (
					<a
						key="modify"
						onClick={() => {
							getProductCategoryById({ id: record?.id }).then(
								(res) => {
									setTitle(t('iot.productCategory.modify'));
									setDataSource(res?.data);
									setModalVisit(true);
									setReadOnly(false);
								},
							);
						}}
					>
						{t('common.modify')}
					</a>
				),
				access.canProductCategoryRemove && (
					<a
						key="remove"
						onClick={() => {
							Modal.confirm({
								title: t('confirm.deleteTitle'),
								content: t('confirm.deleteContent'),
								okText: t('common.ok'),
								cancelText: t('common.cancel'),
								onOk: () => {
									removeProductCategory([record?.id]).then(
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
				treeList={treeList}
				requestId={requestId}
				setRequestId={setRequestId}
			/>

			<ProTable<TableColumns>
				actionRef={actionRef}
				columns={columns}
				request={async (params) => {
					// 表单搜索项会从 params 传入，传递给后端接口。
					return listTreeProductCategory(
						getListTreeQueryParam(params),
					).then((res) => {
						setTreeList([
							{
								id: '0',
								name: t('common.root'),
								children: res?.data,
							},
						]);
						return Promise.resolve({
							data: res?.data,
							success: true,
						});
					});
				}}
				rowKey="id"
				search={{
					layout: 'vertical',
					defaultCollapsed: true,
				}}
				rowSelection={{ ...rowSelection }}
				toolBarRender={() => [
					access.canProductCategorySave && (
						<Button
							key="save"
							type="primary"
							icon={<PlusOutlined />}
							onClick={() => {
								setTitle(t('iot.productCategory.insert'));
								setRequestId(uuidV7());
								setReadOnly(false);
								setModalVisit(true);
								setDataSource({
									sort: 1,
									pid: undefined,
									id: undefined,
									name: '',
									remark: '',
								});
							}}
						>
							{t('common.insert')}
						</Button>
					),
					access.canProductCategoryRemove && (
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
										removeProductCategory(ids).then(
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
						</Button>
					),
				]}
				dateFormatter="string"
				toolbar={{
					title: t('menu.iot.device.productCategory'),
					tooltip: t('menu.iot.device.productCategory'),
				}}
			/>
		</>
	);
};
