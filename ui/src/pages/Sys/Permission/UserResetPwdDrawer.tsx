import { DrawerForm, ProFormText } from '@ant-design/pro-components';
import { message } from 'antd';
import { resetPwdUser } from '@/services/admin/user';
import React, {useState} from "react";

interface UserResetPwdDrawerProps {
	visible: boolean;
	setVisible: (visible: boolean) => void;
	dataSource: any;
}

export const UserResetPwdDrawer: React.FC<UserResetPwdDrawerProps> = ({ visible, setVisible, dataSource}) => {

	const [loading, setLoading] = useState(false)

	return (
		<DrawerForm
			open={visible}
			title="重置密码"
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
					loading: loading
				}
			}}
			initialValues={dataSource}
			onOpenChange={setVisible}
			onFinish={async (value: any) => {
				setLoading(true);
				const { password, confirmPassword } = value;
				if (password !== confirmPassword) {
					message.error("两次密码不一致");
					return;
				}
				resetPwdUser({ id: value?.id, password }).then(res => {
					if (res.code === 'OK') {
						message.success("密码重置成功");
						setVisible(false);
						return true;
					}
				}).finally(() => {
					setLoading(false);
				});
			}}
		>
			<ProFormText
				disabled={loading}
				name="id"
				label="ID"
				hidden
			/>

			<ProFormText
				name="username"
				label="名称"
				tooltip={"密码登录【不允许重复，不允许修改】"}
				disabled={true}
				placeholder={'请输入用户名'}
				rules={[{ required: true, message: '请输入用户名' }]}
			/>

			<ProFormText.Password
				disabled={loading}
				name="password"
				label="密码"
				tooltip="默认密码：laokou123"
				placeholder="请输入密码"
				fieldProps={{ autoComplete: 'new-password' }}
				rules={[{ required: true, message: '请输入密码' }]}
			/>
			<ProFormText.Password
				disabled={loading}
				name="confirmPassword"
				label="确认密码"
				tooltip="默认密码：laokou123"
				placeholder="请输入确认密码"
				fieldProps={{ autoComplete: 'new-password' }}
				rules={[{ required: true, message: '请输入确认密码' }]}
			/>
		</DrawerForm>
	);
};
