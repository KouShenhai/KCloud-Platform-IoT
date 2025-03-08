import { DrawerForm, ProFormText } from '@ant-design/pro-components';
import { message } from 'antd';
import { resetPwdV3 } from '@/services/admin/user';

interface ResetPwdDrawerProps {
	visible: boolean;
	setVisible: (visible: boolean) => void;
	dataSource: any;
}

export const ResetPwdDrawer: React.FC<ResetPwdDrawerProps> = ({ visible, setVisible, dataSource}) => {

	return (
		<DrawerForm
			open={visible}
			title="重置密码"
			drawerProps={{
				destroyOnClose: true,
				closable: true,
				maskClosable: true,
			}}
			initialValues={dataSource}
			onOpenChange={setVisible}
			onFinish={async (value: any) => {
				const { password, confirmPassword } = value;
				if (password !== confirmPassword) {
					message.error("两次密码不一致");
					return;
				}
				resetPwdV3({ id: value?.id, password }).then(res => {
					if (res.code === 'OK') {
						message.success("密码重置成功");
						setVisible(false);
						return true;
					}
				});
			}}
		>
			<ProFormText
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
				name="password"
				label="密码"
				tooltip="默认密码：laokou123"
				placeholder="请输入密码"
				fieldProps={{ autoComplete: 'new-password' }}
				rules={[{ required: true, message: '请输入密码' }]}
			/>
			<ProFormText.Password
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
