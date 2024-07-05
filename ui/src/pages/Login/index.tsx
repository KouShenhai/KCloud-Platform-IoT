import {
	AlipayOutlined,
	GithubOutlined,
	LockOutlined,
	MailOutlined,
	MobileOutlined,
	QqOutlined,
	UserOutlined,
	WechatOutlined,
} from '@ant-design/icons';
import {LoginFormPage, ProFormCaptcha, ProFormText,} from '@ant-design/pro-components';
import {Divider, message, Space, Tabs} from 'antd';
import type {CSSProperties} from 'react';
import {useState} from 'react';
// @ts-ignore
import {history} from 'umi';

type LoginType = 'usernamePassword' | 'mobile' | 'mail';

const iconStyles: CSSProperties = {
	color: 'rgba(0, 0, 0, 0.2)',
	fontSize: '18px',
	verticalAlign: 'middle',
	cursor: 'pointer',
};

export default () => {
	const items = [
		{label: '用户名密码登录', key: 'usernamePassword'},
		{label: '手机号登录', key: 'mobile'},
		{label: '邮箱登录', key: 'mail'}
	];
	const [loginType, setLoginType] = useState<LoginType>('usernamePassword');

	const onSubmit = async (formData: any) => {
		console.log(formData);
		history.push('/');
	};
	// @ts-ignore
	return (
		<div
			style={{
				backgroundColor: 'white',
				height: '100vh',
				width: '100vw',
			}}
		>
			<LoginFormPage
				onFinish={onSubmit}
				backgroundImageUrl="https://gw.alipayobjects.com/zos/rmsportal/FfdJeJRQWjEeGTpqgBKj.png"
				logo={<img alt="logo" src="/logo.png"/>}
				title="老寇IoT云平台"
				subTitle="一个企业级微服务架构的云服务多租户IoT平台"
				actions={
					<div
						style={{
							display: 'flex',
							justifyContent: 'center',
							alignItems: 'center',
							flexDirection: 'column',
						}}
					>
						<Divider plain>
              <span
				  style={{color: '#CCC', fontWeight: 'normal', fontSize: 14}}
			  >
                其他登录方式
              </span>
						</Divider>
						<Space align="center" size={24}>
							<div
								style={{
									display: 'flex',
									justifyContent: 'center',
									alignItems: 'center',
									flexDirection: 'column',
									height: 40,
									width: 40,
									border: '1px solid #D4D8DD',
									borderRadius: '50%',
								}}
							>
								<GithubOutlined style={{...iconStyles, color: '#1f2328'}}/>
							</div>
							<div
								style={{
									display: 'flex',
									justifyContent: 'center',
									alignItems: 'center',
									flexDirection: 'column',
									height: 40,
									width: 40,
									border: '1px solid #D4D8DD',
									borderRadius: '50%',
								}}
							>
								<WechatOutlined style={{...iconStyles, color: '#0fcd2a'}}/>
							</div>
							<div
								style={{
									display: 'flex',
									justifyContent: 'center',
									alignItems: 'center',
									flexDirection: 'column',
									height: 40,
									width: 40,
									border: '1px solid #D4D8DD',
									borderRadius: '50%',
								}}
							>
								<QqOutlined style={{...iconStyles, color: '#1191ff'}}/>
							</div>
							<div
								style={{
									display: 'flex',
									justifyContent: 'center',
									alignItems: 'center',
									flexDirection: 'column',
									height: 40,
									width: 40,
									border: '1px solid #D4D8DD',
									borderRadius: '50%',
								}}
							>
								<AlipayOutlined style={{...iconStyles, color: '#1677FF'}}/>
							</div>
						</Space>
					</div>
				}
			>
				<Tabs
					centered
					items={items}
					activeKey={loginType}
					onChange={(activeKey) => setLoginType(activeKey as LoginType)}
				></Tabs>

				{loginType === 'usernamePassword' && (
					<>
						<ProFormText
							name="username"
							fieldProps={{
								size: 'large',
								prefix: <UserOutlined className={'prefixIcon'}/>,
								autoComplete: 'new-password'
							}}
							placeholder={'请输入用户名'}
							rules={[
								{
									required: true,
									message: '请输入用户名',
								},
							]}
						/>
						<ProFormText.Password
							name="password"
							fieldProps={{
								size: 'large',
								prefix: <LockOutlined className={'prefixIcon'}/>,
								autoComplete: 'new-password'
							}}
							placeholder={'请输入密码'}
							rules={[
								{
									required: true,
									message: '请输入密码',
								},
							]}
						/>
					</>
				)}
				{loginType === 'mobile' && (
					<>
						<ProFormText
							fieldProps={{
								size: 'large',
								prefix: <MobileOutlined className={'prefixIcon'}/>,
								autoComplete: 'new-password'
							}}
							name="mobile"
							placeholder={'手机号'}
							rules={[
								{
									required: true,
									message: '请输入手机号',
								},
								{
									pattern: /^1\d{10}$/,
									message: '手机号格式错误',
								},
							]}
						/>
						<ProFormCaptcha
							fieldProps={{
								size: 'large',
								prefix: <LockOutlined className={'prefixIcon'}/>,
								autoComplete: 'new-password'
							}}
							captchaProps={{
								size: 'large',
							}}
							placeholder={'请输入验证码'}
							captchaTextRender={(timing, count) => {
								if (timing) {
									return `${count} 获取验证码`;
								}
								return '获取验证码';
							}}
							name="captcha"
							rules={[
								{
									required: true,
									message: '请输入验证码',
								}
							]}
							onGetCaptcha={async () => {
								message.success('获取验证码成功！验证码为：1234');
							}}
						/>
					</>
				)}
				{loginType === 'mail' && (
					<>
						<ProFormText
							fieldProps={{
								size: 'large',
								prefix: <MailOutlined className={'prefixIcon'}/>,
								autoComplete: 'new-password'
							}}
							name="mail"
							placeholder={'邮箱'}
							rules={[
								{
									required: true,
									message: '请输入邮箱',
								},
								{
									pattern: /^\w+(-+.\w+)*@\w+(-.\w+)*.\w+(-.\w+)*$/,
									message: '邮箱格式错误'
								}
							]}
						/>
						<ProFormCaptcha
							fieldProps={{
								size: 'large',
								prefix: <LockOutlined className={'prefixIcon'}/>,
								autoComplete: 'new-password'
							}}
							captchaProps={{
								size: 'large',
							}}
							placeholder={'请输入验证码'}
							captchaTextRender={(timing, count) => {
								if (timing) {
									return `${count} 获取验证码`;
								}
								return '获取验证码';
							}}
							name="captcha"
							rules={[
								{
									required: true,
									message: '请输入验证码',
								},
							]}
							onGetCaptcha={async () => {
								message.success('获取验证码成功！验证码为：1234');
							}}
						/>
					</>
				)}
			</LoginFormPage>
		</div>
	);
};
