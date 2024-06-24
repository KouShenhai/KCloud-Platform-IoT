
import React, { useState, useRef, useEffect } from 'react';
import { useIntl, FormattedMessage, useAccess, history, useParams } from '@umijs/max';
import { Button, Modal, message } from 'antd';
import { ActionType, PageContainer, ProColumns, ProTable } from '@ant-design/pro-components';
import { PlusOutlined, DeleteOutlined, ExclamationCircleOutlined, RollbackOutlined } from '@ant-design/icons';
import { authUserSelectAll, authUserCancel, authUserCancelAll, allocatedUserList, unallocatedUserList } from '@/services/system/role';
import { getDictValueEnum } from '@/services/system/dict';
import DictTag from '@/components/DictTag';
import UserSelectorModal from './components/UserSelectorModal';
import { HttpResult } from '@/enums/httpEnum';

/**
 * 删除节点
 *
 * @param selectedRows
 */
const cancelAuthUserAll = async (roleId: string, selectedRows: API.System.User[]) => {
	const hide = message.loading('正在取消授权');
	if (!selectedRows) return true;
	try {
		const userIds = selectedRows.map((row) => row.userId).join(',');
		const resp = await authUserCancelAll({roleId, userIds});
		hide();
		if (resp.code === 200) {
			message.success('取消授权成功，即将刷新');
		} else {
			message.error(resp.msg);
		}
		return true;
	} catch (error) {
		hide();
		message.error('取消授权失败，请重试');
		return false;
	}
};

const cancelAuthUser = async (roleId: string, userId: number) => {
	const hide = message.loading('正在取消授权');
	try {
		const resp = await authUserCancel({ userId, roleId });
		hide();
		if (resp.code === 200) {
			message.success('取消授权成功，即将刷新');
		} else {
			message.error(resp.msg);
		}
		return true;
	} catch (error) {
		hide();
		message.error('取消授权失败，请重试');
		return false;
	}
};


const AuthUserTableList: React.FC = () => {

	const [modalVisible, setModalVisible] = useState<boolean>(false);

	const actionRef = useRef<ActionType>();
	const [selectedRows, setSelectedRows] = useState<API.System.User[]>([]);
	const [statusOptions, setStatusOptions] = useState<any>([]);

	const access = useAccess();

	/** 国际化配置 */
	const intl = useIntl();

	const params = useParams();
	if (params.id === undefined) {
		history.back();
	}
	const roleId = params.id || '0';

	useEffect(() => {
		getDictValueEnum('sys_normal_disable').then((data) => {
			setStatusOptions(data);
		});
	}, []);

	const columns: ProColumns<API.System.User>[] = [
		{
			title: <FormattedMessage id="system.user.user_id" defaultMessage="用户编号" />,
			dataIndex: 'deptId',
			valueType: 'text',
		},
		{
			title: <FormattedMessage id="system.user.user_name" defaultMessage="用户账号" />,
			dataIndex: 'userName',
			valueType: 'text',
		},
		{
			title: <FormattedMessage id="system.user.nick_name" defaultMessage="用户昵称" />,
			dataIndex: 'nickName',
			valueType: 'text',
		},
		{
			title: <FormattedMessage id="system.user.phonenumber" defaultMessage="手机号码" />,
			dataIndex: 'phonenumber',
			valueType: 'text',
		},
		{
			title: <FormattedMessage id="system.role.create_time" defaultMessage="创建时间" />,
			dataIndex: 'createTime',
			valueType: 'dateRange',
			render: (_, record) => {
				return (<span>{record.createTime.toString()} </span>);
			},
			hideInSearch: true,
		},
		{
			title: <FormattedMessage id="system.user.status" defaultMessage="帐号状态" />,
			dataIndex: 'status',
			valueType: 'select',
			valueEnum: statusOptions,
			render: (_, record) => {
				return (<DictTag enums={statusOptions} value={record.status} />);
			},
		},
		{
			title: <FormattedMessage id="pages.searchTable.titleOption" defaultMessage="操作" />,
			dataIndex: 'option',
			width: '60px',
			valueType: 'option',
			render: (_, record) => [
				<Button
					type="link"
					size="small"
					danger
					icon={<DeleteOutlined />}
					key="remove"
					hidden={!access.hasPerms('system:role:remove')}
					onClick={async () => {
						Modal.confirm({
							title: '删除',
							content: '确认要取消该用户' + record.userName + '"角色授权吗？',
							okText: '确认',
							cancelText: '取消',
							onOk: async () => {
								const success = await cancelAuthUser(roleId, record.userId);
								if (success) {
									if (actionRef.current) {
										actionRef.current.reload();
									}
								}
							},
						});
					}}
				>
					取消授权
				</Button>,
			],
		},
	];

	return (
		<PageContainer>
			<div style={{ width: '100%', float: 'right' }}>
				<ProTable<API.System.User>
					headerTitle={intl.formatMessage({
						id: 'pages.searchTable.title',
						defaultMessage: '信息',
					})}
					actionRef={actionRef}
					rowKey="userId"
					key="userList"
					search={{
						labelWidth: 120,
					}}
					toolBarRender={() => [
						<Button
							type="primary"
							key="add"
							hidden={!access.hasPerms('system:role:add')}
							onClick={async () => {
								setModalVisible(true);
							}}
						>
							<PlusOutlined /> <FormattedMessage id="system.role.auth.addUser" defaultMessage="添加用户" />
						</Button>,
						<Button
							type="primary"
							key="remove"
							hidden={selectedRows?.length === 0 || !access.hasPerms('system:role:remove')}
							onClick={async () => {
								Modal.confirm({
									title: '是否确认删除所选数据项?',
									icon: <ExclamationCircleOutlined />,
									content: '请谨慎操作',
									async onOk() {
										const success = await cancelAuthUserAll(roleId, selectedRows);
										if (success) {
											setSelectedRows([]);
											actionRef.current?.reloadAndRest?.();
										}
									},
									onCancel() { },
								});
							}}
						>
							<DeleteOutlined />
							<FormattedMessage id="system.role.auth.cancelAll" defaultMessage="批量取消授权" />
						</Button>,
						<Button
							type="primary"
							key="back"
							onClick={async () => {
								history.back();
							}}
						>
							<RollbackOutlined />
							<FormattedMessage id="pages.goback" defaultMessage="返回" />
						</Button>,
					]}
					request={(params) =>
						allocatedUserList({ ...params, roleId } as API.System.RoleListParams).then((res) => {
							const result = {
								data: res.rows,
								total: res.total,
								success: true,
							};
							return result;
						})
					}
					columns={columns}
					rowSelection={{
						onChange: (_, selectedRows) => {
							setSelectedRows(selectedRows);
						},
					}}
				/>
			</div>
			<UserSelectorModal
				open={modalVisible}
				onSubmit={(values: React.Key[]) => {
					const userIds = values.join(",");
					if (userIds === "") {
						message.warning("请选择要分配的用户");
						return;
					}
					authUserSelectAll({ roleId: roleId, userIds: userIds }).then(resp => {
						if (resp.code === HttpResult.SUCCESS) {
							message.success('更新成功！');
							if (actionRef.current) {
								actionRef.current.reload();
							}
						} else {
							message.warning(resp.msg);
						}
					})
					setModalVisible(false);
				}}
				onCancel={() => {
					setModalVisible(false);
				}}
				params={{roleId}}
				request={(params) =>
					unallocatedUserList({ ...params } as API.System.RoleListParams).then((res) => {
						const result = {
							data: res.rows,
							total: res.rows.length,
							success: true,
						};
						return result;
					})
				}
			/>
		</PageContainer>
	);
};

export default AuthUserTableList;
