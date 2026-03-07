import { modifyUserAuthority } from '@/services/admin/user';
import { useIntl } from '@@/exports';
import {
	DrawerForm,
	ProFormSelect,
	ProFormText,
} from '@ant-design/pro-components';
import { message } from 'antd';
import React, { useState } from 'react';

interface UserAuthorityProps {
	modalModifyAuthorityVisit: boolean;
	setModalModifyAuthorityVisit: (visible: boolean) => void;
	title: string;
	dataSource: TableColumns;
	onComponent: () => void;
	roleList: any[];
}

type TableColumns = {
	id: number;
	username: string | undefined;
	deptId: number;
	roleIds: string[];
};

export const UserModifyAuthorityDrawer: React.FC<UserAuthorityProps> = ({
	modalModifyAuthorityVisit,
	setModalModifyAuthorityVisit,
	title,
	dataSource,
	onComponent,
	roleList,
}) => {
	const intl = useIntl();
	const t = (id: string, values?: Record<string, any>) =>
		intl.formatMessage({ id }, values);
	const [loading, setLoading] = useState(false);

	return (
		<DrawerForm<TableColumns>
			open={modalModifyAuthorityVisit}
			title={title}
			drawerProps={{
				destroyOnClose: true,
				closable: true,
				maskClosable: true,
			}}
			initialValues={dataSource}
			onOpenChange={setModalModifyAuthorityVisit}
			autoFocusFirstInput
			submitter={{
				submitButtonProps: {
					disabled: loading,
					style: {
						display: 'inline-block',
					},
				},
			}}
			onFinish={async (value) => {
				setLoading(true);
				const co = {
					id: value?.id,
					deptId: value?.deptId,
					roleIds: value?.roleIds,
				};
				modifyUserAuthority({ co: co })
					.then((res) => {
						if (res.code === 'OK') {
							message.success(t('toast.assignSuccess')).then();
							setModalModifyAuthorityVisit(false);
							onComponent();
						}
					})
					.finally(() => {
						setLoading(false);
					});
			}}
		>
			<ProFormText
				disabled={loading}
				name="id"
				label="ID"
				hidden={true}
			/>

			<ProFormText
				name="username"
				label={t('user.username')}
				tooltip={t('user.tooltip.username')}
				disabled={true}
				placeholder={t('user.placeholder.username')}
				rules={[
					{ required: true, message: t('user.required.username') },
				]}
			/>

			<ProFormSelect
				disabled={loading}
				name="roleIds"
				allowClear={true}
				label={t('user.roles')}
				mode={'multiple'}
				placeholder={t('user.placeholder.roles')}
				rules={[
					{ required: true, message: t('user.required.roles') },
				]}
				options={roleList}
				fieldProps={{
					fieldNames: {
						label: 'name',
						value: 'id',
					},
				}}
			/>
		</DrawerForm>
	);
};
