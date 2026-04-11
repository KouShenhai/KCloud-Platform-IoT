import { resetUserPwd } from '@/services/admin/user';
import { useIntl } from '@@/exports';
import { DrawerForm, ProFormText } from '@ant-design/pro-components';
import { message } from 'antd';
import React, { useState } from 'react';

interface UserResetPwdDrawerProps {
	visible: boolean;
	setVisible: (visible: boolean) => void;
	dataSource: any;
}

export const UserResetPwdDrawer: React.FC<UserResetPwdDrawerProps> = ({
	visible,
	setVisible,
	dataSource,
}) => {
	const intl = useIntl();
	const t = (id: string, values?: Record<string, any>) =>
		intl.formatMessage({ id }, values);
	const [loading, setLoading] = useState(false);

	return (
		<DrawerForm
			open={visible}
			title={t('sys.user.resetPwd.title')}
			drawerProps={{
				destroyOnClose: true,
				closable: true,
				maskClosable: true,
			}}
			submitter={{
				submitButtonProps: {
					style: {
						display: 'inline-block',
					},
					loading: loading,
				},
			}}
			initialValues={dataSource}
			onOpenChange={setVisible}
			onFinish={async (value: any) => {
				setLoading(true);
				const { password, confirmPassword } = value;
				if (password !== confirmPassword) {
					message.error(t('sys.user.resetPwd.passwordNotMatch'));
					return;
				}
				resetUserPwd({ id: value?.id, password })
					.then((res) => {
						if (res.code === 'OK') {
							message.success(t('sys.user.resetPwd.success'));
							setVisible(false);
							return true;
						}
					})
					.finally(() => {
						setLoading(false);
					});
			}}
		>
			<ProFormText disabled={loading} name="id" label="ID" hidden />

			<ProFormText
				name="username"
				label={t('sys.user.username')}
				tooltip={t('sys.user.tooltip.username')}
				disabled={true}
				placeholder={t('sys.user.placeholder.username')}
				rules={[
					{ required: true, message: t('sys.user.required.username') },
				]}
			/>

			<ProFormText.Password
				disabled={loading}
				name="password"
				label={t('sys.user.resetPwd.password')}
				tooltip={t('sys.user.tooltip.password')}
				placeholder={t('sys.user.resetPwd.placeholder.password')}
				fieldProps={{ autoComplete: 'new-password' }}
				rules={[
					{
						required: true,
						message: t('sys.user.resetPwd.required.password'),
					},
				]}
			/>
			<ProFormText.Password
				disabled={loading}
				name="confirmPassword"
				label={t('sys.user.resetPwd.confirmPassword')}
				tooltip={t('sys.user.tooltip.password')}
				placeholder={t('sys.user.resetPwd.placeholder.confirmPassword')}
				fieldProps={{ autoComplete: 'new-password' }}
				rules={[
					{
						required: true,
						message: t('sys.user.resetPwd.required.confirmPassword'),
					},
				]}
			/>
		</DrawerForm>
	);
};
