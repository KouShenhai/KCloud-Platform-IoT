import { RoleDrawer } from '@/pages/Sys/Permission/RoleDrawer';
import { RoleModifyAuthorityDrawer } from '@/pages/Sys/Permission/RoleModifyAuthorityDrawer';
import { listTreeDept } from '@/services/admin/dept';
import { listSelectTreeMenu } from '@/services/admin/menu';
import { getRoleById, pageRole, removeRole } from '@/services/admin/role';
import { trim } from '@/utils/format';
import { useAccess, useIntl } from '@@/exports';
import { DeleteOutlined, PlusOutlined } from '@ant-design/icons';
import {
	ActionType,
	ProColumns,
	ProTable,
} from '@ant-design/pro-components';
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
		createTime: string | undefined;
		sort: number | undefined;
		dataScope: string | undefined;
	};

	const access = useAccess();
	const [readOnly, setReadOnly] = useState(false);
	const [modalVisit, setModalVisit] = useState(false);
	// @ts-ignore
	const actionRef = useRef<ActionType>();
	const [dataSource, setDataSource] = useState<any>({});
	const [ids, setIds] = useState<number[]>([]);
	const [title, setTitle] = useState('');
	const [menuTreeList, setMenuTreeList] = useState<any[]>([]);
	const [modalModifyAuthorityVisit, setModalModifyAuthorityVisit] =
		useState(false);
	const [deptTreeList, setDeptTreeList] = useState<any[]>([]);
	const [typeValue, setTypeValue] = useState('all');
	const [requestId, setRequestId] = useState('');

	const getPageQueryParam = (params: any) => {
		return {
			pageSize: params?.pageSize,
			pageNum: params?.current,
			pageIndex: params?.pageSize * (params?.current - 1),
			name: trim(params?.name),
			dataScope: params?.dataScope,
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

	const getMenuTreeList = async () => {
		listSelectTreeMenu({ code: 1, status: 0 }).then((res) => {
			setMenuTreeList(res?.data);
		});
	};

	const getDeptTreeList = async () => {
		listTreeDept({}).then((res) => {
			setDeptTreeList(res?.data);
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
		getMenuTreeList().catch(console.log);
		getDeptTreeList().catch(console.log);
	}, []);

	const columns: ProColumns<TableColumns>[] = [
		{
			title: t('common.number'),
			dataIndex: 'index',
			valueType: 'indexBorder',
			width: 85,
		},
		{
			title: t('sys.role.name'),
			dataIndex: 'name',
			valueType: 'text',
			fieldProps: {
				placeholder: t('sys.role.placeholder.name'),
			},
			width: 220,
		},
		{
			title: t('sys.role.dataScope'),
			dataIndex: 'dataScope',
			valueType: 'select',
			width: 200,
			fieldProps: {
				valueType: 'select',
				mode: 'single',
				placeholder: t('sys.role.placeholder.dataScope'),
				options: [
					{
						value: 'all',
						label: t('sys.role.dataScope.all'),
					},
					{
						value: 'custom',
						label: t('sys.role.dataScope.custom'),
					},
					{
						value: 'self_dept',
						label: t('sys.role.dataScope.selfDept'),
					},
					{
						value: 'below_dept',
						label: t('sys.role.dataScope.belowDept'),
					},
					{
						value: 'self',
						label: t('sys.role.dataScope.self'),
					},
				],
			},
		},
		{
			title: t('sys.role.sort'),
			dataIndex: 'sort',
			hideInSearch: true,
			ellipsis: true,
			width: 200,
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
				access.canRoleGetDetail && (
					<a
						key="get"
						onClick={() => {
							getRoleById({ id: record?.id }).then((res) => {
								if (res.code === 'OK') {
									setTitle(t('sys.role.view'));
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
				access.canRoleModify && (
					<a
						key="modify"
						onClick={() => {
							getRoleById({ id: record?.id }).then((res) => {
								if (res.code === 'OK') {
									setTitle(t('sys.role.modify'));
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
				access.canRoleModify && (
					<a
						key={'modifyAuthority'}
						onClick={() => {
							getRoleById({ id: record?.id }).then((res) => {
								setTitle(t('sys.role.assignAuthority'));
								setModalModifyAuthorityVisit(true);
								setDataSource(res?.data);
								setTypeValue(res?.data?.dataScope);
							});
						}}
					>
						{t('common.assignPermission')}
					</a>
				),
				access.canRoleRemove && (
					<a
						key="remove"
						onClick={() => {
							Modal.confirm({
								title: t('confirm.deleteTitle'),
								content: t('confirm.deleteContent'),
								okText: t('common.ok'),
								cancelText: t('common.cancel'),
								onOk: () => {
									removeRole([record?.id]).then((res) => {
										if (res.code === 'OK') {
											message.success(t('toast.deleteSuccess')).then();
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
			<RoleDrawer
				modalVisit={modalVisit}
				setModalVisit={setModalVisit}
				title={title}
				readOnly={readOnly}
				dataSource={dataSource}
				onComponent={async () => {
					// @ts-ignore
					actionRef?.current?.reload();
				}}
				deptTreeList={deptTreeList}
				menuTreeList={menuTreeList}
				requestId={requestId}
				setRequestId={setRequestId}
			/>

			<RoleModifyAuthorityDrawer
				modalModifyAuthorityVisit={modalModifyAuthorityVisit}
				setModalModifyAuthorityVisit={setModalModifyAuthorityVisit}
				title={title}
				dataSource={dataSource}
				onComponent={() => {
					// @ts-ignore
					actionRef?.current?.reload();
				}}
				menuTreeList={menuTreeList}
				deptTreeList={deptTreeList}
				typeValue={typeValue}
				setTypeValue={setTypeValue}
			/>

			<ProTable<TableColumns>
				actionRef={actionRef}
				columns={columns}
				request={async (params) => {
					// 表单搜索项会从 params 传入，传递给后端接口。
					return pageRole(getPageQueryParam(params)).then((res) => {
						return Promise.resolve({
							data: res?.data?.records,
							total: parseInt(res?.data?.total || 0),
							success: true,
						});
					});
				}}
				rowSelection={{ ...rowSelection }}
				rowKey="id"
				search={{
					layout: 'vertical',
					defaultCollapsed: true,
				}}
				toolBarRender={() => [
					access.canRoleSave && (
						<Button
							key="save"
							type="primary"
							icon={<PlusOutlined />}
							onClick={() => {
								setTitle(t('sys.role.insert'));
								setRequestId(uuidV7());
								setReadOnly(false);
								setModalVisit(true);
								setDataSource({
									id: undefined,
									name: '',
									dataScope: 'all',
									menuIds: [],
									sort: 1,
								});
							}}
						>
							{t('common.insert')}
						</Button>
					),
					access.canRoleRemove && (
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
											message.warning(t('toast.selectAtLeastOne')).then();
											return;
										}
										removeRole(ids).then((res) => {
											if (res.code === 'OK') {
												message.success(t('toast.deleteSuccess')).then();
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
					title: t('menu.sys.permission.role'),
					tooltip: t('menu.sys.permission.role'),
				}}
			/>
		</>
	);
};
