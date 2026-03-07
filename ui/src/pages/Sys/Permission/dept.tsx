import { DeptDrawer } from '@/pages/Sys/Permission/DeptDrawer';
import { getDeptById, listTreeDept, removeDept } from '@/services/admin/dept';
import { trim } from '@/utils/format';
import { useAccess, useIntl } from '@@/exports';
import { DeleteOutlined, PlusOutlined } from '@ant-design/icons';
import { ActionType, ProColumns, ProTable } from '@ant-design/pro-components';
import { Button, message, Modal } from 'antd';
import { TableRowSelection } from 'antd/es/table/interface';
import { useEffect, useRef, useState } from 'react';
import { v7 as uuidV7 } from 'uuid';

export default () => {
	const intl = useIntl();
	const t = (id: string, values?: Record<string, any>) =>
		intl.formatMessage({ id }, values);

	type TableColumns = {
		id: number;
		name: string | undefined;
		path: string | undefined;
		createTime: string | undefined;
		sort: number | undefined;
	};

	const access = useAccess();
	const [readOnly, setReadOnly] = useState(false);
	const [modalVisit, setModalVisit] = useState(false);
	const actionRef = useRef<ActionType | undefined>(undefined);
	const [dataSource, setDataSource] = useState<any>({});
	const [ids, setIds] = useState<number[]>([]);
	const [title, setTitle] = useState('');
	const [treeList, setTreeList] = useState<any[]>([]);
	const [requestId, setRequestId] = useState('');

	const getListTreeQueryParam = (params: any) => {
		return {
			name: trim(params?.name),
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

	const getTreeList = async () => {
		listTreeDept({}).then((res) => {
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
			title: t('sys.dept.name'),
			dataIndex: 'name',
			valueType: 'text',
			fieldProps: {
				placeholder: t('sys.dept.placeholder.name'),
			},
		},
		{
			title: t('sys.dept.sort'),
			dataIndex: 'sort',
			hideInSearch: true,
			ellipsis: true,
			width: 80,
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
				access.canDeptGetDetail && (
					<a
						key="get"
						onClick={() => {
							getDeptById({ id: record?.id }).then((res) => {
								if (res.code === 'OK') {
									setTitle(t('sys.dept.view'));
									setModalVisit(true);
									setReadOnly(true);
									setDataSource(res?.data);
								}
							});
						}}
					>
						{t('common.view')}
					</a>
				),
				access.canDeptSave && (
					<a
						key="save"
						onClick={() => {
							setTitle(t('sys.dept.insert'));
							setRequestId(uuidV7());
							setReadOnly(false);
							setModalVisit(true);
							setDataSource({
								id: undefined,
								name: '',
								path: '',
								pid: record?.id,
								sort: 1,
							});
						}}
					>
						{t('common.insert')}
					</a>
				),
				access.canDeptModify && (
					<a
						key="modify"
						onClick={() => {
							getDeptById({ id: record?.id }).then((res) => {
								if (res.code === 'OK') {
									setTitle(t('sys.dept.modify'));
									setModalVisit(true);
									setReadOnly(false);
									setDataSource(res?.data);
								}
							});
						}}
					>
						{t('common.modify')}
					</a>
				),
				access.canDeptRemove && (
					<a
						key="remove"
						onClick={() => {
							Modal.confirm({
								title: t('confirm.deleteTitle'),
								content: t('confirm.deleteContent'),
								okText: t('common.ok'),
								cancelText: t('common.cancel'),
								onOk: () => {
									removeDept([record?.id]).then((res) => {
										if (res.code === 'OK') {
											message
												.success(
													t('toast.deleteSuccess'),
												)
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
			<DeptDrawer
				modalVisit={modalVisit}
				setModalVisit={setModalVisit}
				title={title}
				readOnly={readOnly}
				dataSource={dataSource}
				onComponent={async () => {
					// @ts-ignore
					actionRef?.current?.reload();
					getTreeList().catch(console.log);
				}}
				treeList={treeList}
				setRequestId={setRequestId}
				requestId={requestId}
			/>

			<ProTable<TableColumns>
				actionRef={actionRef}
				columns={columns}
				request={async (params) => {
					// 表单搜索项会从 params 传入，传递给后端接口。
					return listTreeDept(getListTreeQueryParam(params)).then(
						(res) => {
							return Promise.resolve({
								data: res.data,
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
					access.canDeptSave && (
						<Button
							key="save"
							type="primary"
							icon={<PlusOutlined />}
							onClick={() => {
								setTitle(t('sys.dept.insert'));
								setRequestId(uuidV7());
								setReadOnly(false);
								setModalVisit(true);
								setDataSource({
									id: undefined,
									name: '',
									path: '',
									sort: 1,
								});
							}}
						>
							{t('common.insert')}
						</Button>
					),
					access.canDeptRemove && (
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
												.warning(
													t('toast.selectAtLeastOne'),
												)
												.then();
											return;
										}
										removeDept(ids).then((res) => {
											if (res.code === 'OK') {
												message
													.success(
														t(
															'toast.deleteSuccess',
														),
													)
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
					title: t('menu.sys.permission.dept'),
					tooltip: t('menu.sys.permission.dept'),
				}}
			/>
		</>
	);
};
