import { DrawerForm, ProFormText } from '@ant-design/pro-components';
import { message } from 'antd';
import { resetPwdV3 } from '@/services/admin/user';

interface ResetPwdDrawerProps {
	visible: boolean;
	setVisible: (visible: boolean) => void;
	primaryKey: number | undefined;
}

export const ResetPwdDrawer: React.FC<ResetPwdDrawerProps> = ({ visible, setVisible, primaryKey }) => {
	const handleFinish = async (values: any) => {
		const { password, confirmPassword } = values;
		if (password !== confirmPassword) {
			message.error("两次密码不一致");
			return false;
		}
		resetPwdV3({ id: primaryKey, password }).then(res => {
			if (res.code === 'OK') {
				message.success("密码重置成功");
				setVisible(false);
				return true;
			}
		});
		return false;
	};

	return (
		<DrawerForm
			open={visible}
			title="重置密码"
			drawerProps={{
				destroyOnClose: true,
				closable: true,
				maskClosable: true,
			}}
			onOpenChange={setVisible}
			onFinish={handleFinish}
		>
			<ProFormText name="id" label="ID" hidden />
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
