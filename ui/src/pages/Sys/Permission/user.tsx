import { UserDrawer } from '@/pages/Sys/Permission/UserDrawer';
import { UserModifyAuthorityDrawer } from '@/pages/Sys/Permission/UserModifyAuthorityDrawer';
import { UserResetPwdDrawer } from '@/pages/Sys/Permission/UserResetPwdDrawer';
import { listSelectTreeDept } from '@/services/admin/dept';
import { pageRole } from '@/services/admin/role';
import { getUserById, pageUser, removeUser } from '@/services/admin/user';
import { trim } from '@/utils/format';
import { useAccess, useIntl } from '@@/exports';
import { DeleteOutlined, PlusOutlined } from '@ant-design/icons';
import { ActionType, ProColumns, ProTable } from '@ant-design/pro-components';
import { Button, message, Modal, Space, Switch, Tag, UploadFile } from 'antd';
import { TableRowSelection } from 'antd/es/table/interface';
import { useEffect, useRef, useState } from 'react';
import { v7 as uuidV7 } from 'uuid';

export default () => {
	const intl = useIntl();
	const t = (id: string, values?: Record<string, any>) =>
		intl.formatMessage({ id }, values);

	type TableColumns = {
		id: number;
		username: string | undefined;
		status: number | undefined;
		mail: string | undefined;
		mobile: string | undefined;
		createTime: string | undefined;
		superAdmin: number | undefined;
	};

	const access = useAccess();
	const [readOnly, setReadOnly] = useState(false);
	const [modalVisit, setModalVisit] = useState(false);
	const [modalModifyAuthorityVisit, setModalModifyAuthorityVisit] =
		useState(false);
	const [modalRestPwdVisit, setModalRestPwdVisit] = useState(false);
	// @ts-ignore
	const actionRef = useRef<ActionType>();
	const [dataSource, setDataSource] = useState<any>({});
	const [ids, setIds] = useState<number[]>([]);
	const [title, setTitle] = useState('');
	const [edit, setEdit] = useState(false);
	const [deptTreeList, setDeptTreeList] = useState<any[]>([]);
	const [roleList, setRoleList] = useState<any[]>([]);
	const [fileList, setFileList] = useState<UploadFile[]>([]);
	const [requestId, setRequestId] = useState('');
	const [logId, setLogId] = useState<number>(1);

	const getDeptTreeList = async () => {
		listSelectTreeDept({}).then((res) => {
			setDeptTreeList(res?.data);
		});
	};

	const getRoleList = async () => {
		pageRole({ pageSize: 10000, pageNum: 1, pageIndex: 0 }).then((res) => {
			setRoleList(res?.data?.records);
		});
	};

	const getPageQueryParam = (params: any) => {
		return {
			pageSize: params?.pageSize,
			pageNum: params?.current,
			pageIndex: params?.pageSize * (params?.current - 1),
			username: trim(params?.username),
			mail: trim(params?.mail),
			mobile: trim(params?.mobile),
			superAdmin: params?.superAdmin,
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

	useEffect(() => {
		getDeptTreeList().catch(console.log);
		getRoleList().catch(console.log);
	}, []);

	const columns: ProColumns<TableColumns>[] = [
		{
			title: t('common.number'),
			dataIndex: 'index',
			valueType: 'indexBorder',
			width: 85,
		},
		{
			title: t('user.username'),
			dataIndex: 'username',
			tooltip: t('user.tooltipFuzzyQueryFourChars'),
			valueType: 'text',
			ellipsis: true,
			width: 120,
			fieldProps: {
				placeholder: t('user.placeholder.username'),
			},
		},
		{
			title: t('user.mail'),
			dataIndex: 'mail',
			tooltip: t('user.tooltipFuzzyQueryFourChars'),
			valueType: 'text',
			width: 120,
			fieldProps: {
				placeholder: t('user.placeholder.mail'),
			},
			ellipsis: true,
		},
		{
			title: t('user.mobile'),
			dataIndex: 'mobile',
			tooltip: t('user.tooltipFuzzyQueryThreeOrFourChars'),
			valueType: 'text',
			width: 120,
			fieldProps: {
				placeholder: t('user.placeholder.mobile'),
			},
			ellipsis: true,
		},
		{
			title: t('user.superAdmin'),
			dataIndex: 'superAdmin',
			valueType: 'select',
			hideInTable: true,
			fieldProps: {
				valueType: 'select',
				mode: 'single',
				placeholder: t('user.placeholder.superAdmin'),
				options: [
					{
						label: t('common.no'),
						value: 0,
					},
					{
						label: t('common.yes'),
						value: 1,
					},
				],
			},
			ellipsis: true,
		},
		{
			disable: true,
			title: t('user.superAdmin'),
			dataIndex: 'superAdmin',
			hideInSearch: true,
			width: 120,
			renderFormItem: (_, { defaultRender }) => {
				return defaultRender(_);
			},
			render: (_, record) => (
				<Space>
					{record?.superAdmin === 0 && (
						<Tag color={'rgb(51 114 253)'} key={'menu'}>
							{t('common.no')}
						</Tag>
					)}
					{record?.superAdmin === 1 && (
						<Tag color={'#fd5251'} key={'button'}>
							{t('common.yes')}
						</Tag>
					)}
				</Space>
			),
		},
		{
			title: t('user.status'),
			dataIndex: 'status',
			hideInTable: true,
			valueType: 'select',
			fieldProps: {
				valueType: 'select',
				mode: 'single',
				placeholder: t('user.placeholder.status'),
				options: [
					{
						label: t('common.enable'),
						value: 0,
					},
					{
						label: t('common.disable'),
						value: 1,
					},
				],
			},
			ellipsis: true,
			width: 100,
		},
		{
			title: t('user.status'),
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
			width: 400,
			render: (_, record) => [
				access.canUserGetDetail && (
					<a
						key="get"
						title={t('common.view')}
						onClick={() => {
							getUserById({ id: record?.id }).then((res) => {
								if (res.code === 'OK') {
									setTitle(t('user.view'));
									setModalVisit(true);
									setReadOnly(true);
									setDataSource(res?.data);
									setLogId(res?.data?.logId);
									const avatarUrl = res?.data?.avatarUrl;
									if (avatarUrl) {
										setFileList([
											{
												uid: '-1',
												name: t('user.avatarFileName'),
												status: 'done',
												url: avatarUrl,
											},
										]);
									} else {
										setFileList([]);
									}
								}
							});
						}}
					>
						{t('common.view')}
					</a>
				),
				access.canUserModify && (
					<a
						key="modify"
						onClick={() => {
							getUserById({ id: record?.id }).then((res) => {
								if (res.code === 'OK') {
									setTitle(t('user.modify'));
									setModalVisit(true);
									setReadOnly(false);
									setEdit(true);
									setDataSource(res?.data);
									const avatarUrl = res?.data?.avatarUrl;
									if (avatarUrl) {
										setFileList([
											{
												uid: '-1',
												name: t('user.avatarFileName'),
												status: 'done',
												url: avatarUrl,
											},
										]);
									} else {
										setFileList([]);
									}
								}
							});
						}}
					>
						{t('common.modify')}
					</a>
				),
				access.canUserModify && (
					<a
						key={'modifyAuthority'}
						onClick={() => {
							getUserById({ id: record?.id }).then((res) => {
								setTitle(t('user.assignAuthority'));
								setModalModifyAuthorityVisit(true);
								setDataSource(res?.data);
							});
						}}
					>
						{t('common.assignPermission')}
					</a>
				),
				access.canUserModify && (
					<a
						key={'resetPwd'}
						onClick={() => {
							getUserById({ id: record?.id }).then((res) => {
								setModalRestPwdVisit(true);
								setDataSource(res?.data);
							});
						}}
					>
						{t('common.resetPwd')}
					</a>
				),
				access.canUserRemove && (
					<a
						key="remove"
						onClick={() => {
							Modal.confirm({
								title: t('confirm.deleteTitle'),
								content: t('confirm.deleteContent'),
								okText: t('common.ok'),
								cancelText: t('common.cancel'),
								onOk: () => {
									removeUser([record?.id]).then((res) => {
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
			<UserResetPwdDrawer
				visible={modalRestPwdVisit}
				setVisible={setModalRestPwdVisit}
				dataSource={dataSource}
			/>

			<UserDrawer
				onComponent={async () => {
					// @ts-ignore
					actionRef?.current?.reload();
				}}
				deptTreeList={deptTreeList}
				roleList={roleList}
				edit={edit}
				modalVisit={modalVisit}
				setModalVisit={setModalVisit}
				title={title}
				readOnly={readOnly}
				dataSource={dataSource}
				fileList={fileList}
				setFileList={setFileList}
				requestId={requestId}
				setRequestId={setRequestId}
				setLogId={setLogId}
				logId={logId}
			/>

			<UserModifyAuthorityDrawer
				modalModifyAuthorityVisit={modalModifyAuthorityVisit}
				setModalModifyAuthorityVisit={setModalModifyAuthorityVisit}
				title={title}
				dataSource={dataSource}
				onComponent={() => {
					// @ts-ignore
					actionRef?.current?.reload();
				}}
				roleList={roleList}
			/>

			<ProTable<TableColumns>
				actionRef={actionRef}
				columns={columns}
				request={async (params) => {
					// 表单搜索项会从 params 传入，传递给后端接口。
					return pageUser(getPageQueryParam(params)).then((res) => {
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
					access.canUserSave && (
						<Button
							key="save"
							type="primary"
							icon={<PlusOutlined />}
							onClick={() => {
								setTitle(t('user.insert'));
								setRequestId(uuidV7());
								setReadOnly(false);
								setModalVisit(true);
								setEdit(false);
								setDataSource({
									id: undefined,
									username: '',
									superAdmin: 0,
									status: 0,
								});
								setFileList([]);
							}}
						>
							{t('common.insert')}
						</Button>
					),
					access.canUserRemove && (
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
										removeUser(ids).then((res) => {
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
					title: t('menu.sys.permission.user'),
					tooltip: t('user.tooltipDefaultPwd'),
				}}
			/>
		</>
	);
};
