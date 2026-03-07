import { MenuDrawer } from '@/pages/Sys/Permission/MenuDrawer';
import { getMenuById, listTreeMenu, removeMenu } from '@/services/admin/menu';
import { useAccess, useIntl } from '@@/exports';
import { DeleteOutlined, PlusOutlined } from '@ant-design/icons';
import { ProColumns, ProTable } from '@ant-design/pro-components';
import type { ActionType } from '@ant-design/pro-components';
import { Button, message, Modal, Space, Switch, Tag } from 'antd';
import { TableRowSelection } from 'antd/es/table/interface';
import { useEffect, useRef, useState } from 'react';
import { v7 as uuidV7 } from 'uuid';

export default () => {
	type TableColumns = {
		id: number;
		name: string | undefined;
		path: string | undefined;
		status: number | undefined;
		type: number | undefined;
		permission: string | undefined;
		createTime: string | undefined;
		sort: number | undefined;
	};

	const access = useAccess();
	const intl = useIntl();
	const t = (id: string, values?: Record<string, any>) =>
		intl.formatMessage({ id }, values);
	const [readOnly, setReadOnly] = useState(false);
	const [modalVisit, setModalVisit] = useState(false);
	const actionRef = useRef<ActionType | null>(null);
	const [dataSource, setDataSource] = useState<any>({});
	const [ids, setIds] = useState<number[]>([]);
	const [title, setTitle] = useState('');
	const [typeValue, setTypeValue] = useState(0);
	const [treeList, setTreeList] = useState<any[]>([]);
	const [requestId, setRequestId] = useState('');

	const getListTreeQueryParam = (params: any) => {
		return {
			code: 1,
			type: params?.typeValue,
			status: params?.statusValue,
			params: {
				startTime: params?.startDateValue
					? `${params.startDateValue} 00:00:00`
					: undefined,
				endTime: params?.endDateValue
					? `${params.endDateValue} 23:59:59`
					: undefined,
			},
		};
	};

	const getTreeList = async () => {
		return listTreeMenu({ code: 1, type: 0, status: 0 }).then((res) => {
			setTreeList([
				{
					id: '0',
					name: t('common.root'),
					children: res?.data,
				},
			]);
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

	useEffect(() => {
		getTreeList().catch(console.log);
	}, []);

	const columns: ProColumns<TableColumns>[] = [
		{
			title: t('common.number'),
			dataIndex: 'index',
			valueType: 'indexBorder',
			width: 110,
		},
		{
			title: t('sys.menu.name'),
			dataIndex: 'name',
			hideInSearch: true,
			ellipsis: true,
			width: 220,
		},
		{
			title: t('sys.menu.path'),
			dataIndex: 'path',
			ellipsis: true,
			hideInSearch: true,
			width: 180,
		},
		{
			title: t('sys.menu.permission'),
			dataIndex: 'permission',
			ellipsis: true,
			hideInSearch: true,
			width: 180,
		},
		{
			title: t('sys.menu.type'),
			key: 'typeValue',
			dataIndex: 'typeValue',
			hideInTable: true,
			valueType: 'select',
			fieldProps: {
				valueType: 'select',
				mode: 'single',
				placeholder: t('sys.menu.placeholder.type'),
				options: [
					{
						value: 0,
						label: t('sys.menu.type.menu'),
					},
					{
						value: 1,
						label: t('sys.menu.type.button'),
					},
				],
			},
			ellipsis: true,
		},
		{
			disable: true,
			title: t('sys.menu.type'),
			dataIndex: 'type',
			hideInSearch: true,
			renderFormItem: (_, { defaultRender }) => {
				return defaultRender(_);
			},
			render: (_, record) => (
				<Space>
					{record?.type === 0 && (
						<Tag color={'rgb(51 114 253)'} key={'menu'}>
							{t('sys.menu.type.menu')}
						</Tag>
					)}
					{record?.type === 1 && (
						<Tag color={'#fd5251'} key={'button'}>
							{t('sys.menu.type.button')}
						</Tag>
					)}
				</Space>
			),
		},
		{
			title: t('sys.menu.status'),
			key: 'statusValue',
			dataIndex: 'statusValue',
			hideInTable: true,
			valueType: 'select',
			fieldProps: {
				valueType: 'select',
				mode: 'single',
				placeholder: t('sys.menu.placeholder.status'),
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
		},
		{
			title: t('sys.menu.status'),
			dataIndex: 'status',
			hideInSearch: true,
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
			title: t('sys.menu.sort'),
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
			key: 'createTimeValue',
			dataIndex: 'createTimeValue',
			valueType: 'dateRange',
			hideInTable: true,
			fieldProps: {
				placeholder: [t('common.selectStartTime'), t('common.selectEndTime')],
			},
			search: {
				transform: (value) => {
					return {
						startDateValue: value[0],
						endDateValue: value[1],
					};
				},
			},
		},
		{
			title: t('common.operation'),
			valueType: 'option',
			key: 'option',
			render: (_, record) => [
				access.canMenuGetDetail && (
					<a
						key="get"
						onClick={() => {
							getMenuById({ id: record?.id }).then((res) => {
								if (res.code === 'OK') {
									setTitle(t('sys.menu.view'));
									setModalVisit(true);
									setReadOnly(true);
									const data = res?.data;
									setTypeValue(data.type);
									setDataSource(data);
								}
							});
						}}
					>
						{t('common.view')}
					</a>
				),
				access.canMenuSave && (
					<a
						key="save"
						onClick={() => {
							setTitle(t('sys.menu.insert'));
							setRequestId(uuidV7());
							setTypeValue(0);
							setReadOnly(false);
							setModalVisit(true);
							setDataSource({
								id: undefined,
								name: '',
								path: '',
								permission: '',
								sort: 1,
								icon: '',
								status: 0,
								type: 0,
								pid: record?.id,
							});
						}}
					>
						{t('common.insert')}
					</a>
				),
				access.canMenuModify && (
					<a
						key="modify"
						onClick={() => {
							getMenuById({ id: record?.id }).then((res) => {
								if (res.code === 'OK') {
									setTitle(t('sys.menu.modify'));
									setModalVisit(true);
									setReadOnly(false);
									const data = res?.data;
									setTypeValue(data.type);
									setDataSource(data);
								}
							});
						}}
					>
						{t('common.modify')}
					</a>
				),
				access.canMenuRemove && (
					<a
						key="remove"
						onClick={() => {
							Modal.confirm({
								title: t('confirm.deleteTitle'),
								content: t('confirm.deleteContent'),
								okText: t('common.ok'),
								cancelText: t('common.cancel'),
								onOk: () => {
									removeMenu([record?.id]).then((res) => {
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
			width: 190,
		},
	];

	return (
		<>
			<MenuDrawer
				modalVisit={modalVisit}
				setModalVisit={setModalVisit}
				title={title}
				readOnly={readOnly}
				dataSource={dataSource}
				treeList={treeList}
				typeValue={typeValue}
				setTypeValue={setTypeValue}
				onComponent={async () => {
					// @ts-ignore
					actionRef?.current?.reload();
					getTreeList().catch(console.log);
				}}
				requestId={requestId}
				setRequestId={setRequestId}
			/>

			<ProTable<TableColumns>
				actionRef={actionRef}
				columns={columns}
				request={async (params) => {
					// 表单搜索项会从 params 传入，传递给后端接口。
					return listTreeMenu(getListTreeQueryParam(params)).then(
						(res) => {
							return Promise.resolve({
								data: res?.data,
								success: true,
							});
						},
					);
				}}
				rowSelection={{ ...rowSelection }}
				rowKey="id"
				search={{
					layout: 'vertical',
					defaultCollapsed: true,
				}}
				toolBarRender={() => [
					access.canMenuSave && (
						<Button
							key="save"
							type="primary"
							icon={<PlusOutlined />}
							onClick={() => {
								setTitle(t('sys.menu.insert'));
								setRequestId(uuidV7());
								setTypeValue(0);
								setReadOnly(false);
								setModalVisit(true);
								setDataSource({
									id: undefined,
									name: '',
									path: '',
									permission: '',
									sort: 1,
									icon: '',
									status: 0,
									type: 0,
								});
							}}
						>
							{t('common.insert')}
						</Button>
					),
					access.canMenuRemove && (
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
										removeMenu(ids).then((res) => {
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
					title: t('menu.sys.permission.menu'),
					tooltip: t('menu.sys.permission.menu'),
				}}
			/>
		</>
	);
};
