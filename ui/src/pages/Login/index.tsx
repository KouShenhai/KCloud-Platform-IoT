import {
	AlipayOutlined,
	GithubOutlined,
	LockOutlined,
	MailOutlined,
	MobileOutlined,
	QqOutlined,
	SafetyCertificateOutlined,
	UserOutlined,
	WechatOutlined,
} from '@ant-design/icons';
import {LoginFormPage, ProFormCaptcha, ProFormText,} from '@ant-design/pro-components';
import {Col, Divider, Image, message, Row, Space, Tabs} from 'antd';
import {CSSProperties, useEffect, useRef, useState} from 'react';
import {login} from '@/services/auth/authsController';
import {getCaptchaImageByUuidV3} from '@/services/auth/captchasV3Controller';
// @ts-ignore
import {history} from 'umi';
import {getSecretInfoV3} from '@/services/auth/secretsV3Controller';
import {JSEncrypt} from 'jsencrypt';
// @ts-ignore
import {v7 as uuidV7} from 'uuid';
import {getTenantIdByDomainNameV3, listTenantOptionV3} from "@/services/auth/tenantsV3Controller";
import {ProFormInstance, ProFormSelect} from "@ant-design/pro-form/lib";

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
		{label: '邮箱登录', key: 'mail'},
	];
	const [loginType, setLoginType] = useState<LoginType>('usernamePassword');
	const [captchaImage, setCaptchaImage] = useState<string>('');
	const [uuid, setUuid] = useState<string>('');
	const [publicKey, setPublicKey] = useState<string>('');
	const [tenantOptions, setTenantOptions] = useState<API.TenantOption[]>([])
	const formRef = useRef<ProFormInstance>();

	const setFormField = (form: API.LoginParam) => {
		formRef?.current?.setFieldsValue(form);
	}

	const timeFix = () => {
		const time = new Date();
		const hour = time.getHours();
		return hour < 9
			? '早上好'
			: hour <= 11
				? '上午好'
				: hour <= 13
					? '中午好'
					: hour < 20
						? '下午好'
						: '晚上好';
	};

	const encrypt = new JSEncrypt();
	const getParams = (form: API.LoginParam) => {
		switch (loginType) {
			case 'usernamePassword': {
				encrypt.setPublicKey(publicKey);
				return {
					uuid: uuid,
					captcha: form.captcha,
					username: encodeURIComponent(
						encrypt.encrypt(form.username as string),
					),
					password: encodeURIComponent(
						encrypt.encrypt(form.password as string),
					),
					tenant_id: form.tenant_id,
					grant_type: 'password',
				};
			}
			case 'mail':
				return {};
			case 'mobile':
				return {};
		}
	};

	const getCaptchaImage = async () => {
		// 清空验证码输入框
		setFormField({captcha: ''})
		// 调用验证码API
		const uuid = uuidV7();
		getCaptchaImageByUuidV3({uuid: uuid}).then((res) => {
			setCaptchaImage(res.data);
		});
		setUuid(uuid);
	};

	const getPublicKey = async () => {
		getSecretInfoV3().then((res) => {
			setPublicKey(res.data.publicKey);
		});
	};

	const listTenantOption = async () => {
		listTenantOptionV3().then(res => {
			const options = []
			const defaultOption = {label: '老寇云集团', value: '0'}
			options.push(defaultOption)
			res.data.forEach((item: API.TenantOption) => options.push(item))
			setTenantOptions(options)
		})
	}

	const getTenantIdByDomain = async () => {
		getTenantIdByDomainNameV3().then(res => {
			setFormField({tenant_id: res.data})
		})
	}

	useEffect(() => {
		getTenantIdByDomain().catch(console.error)
		listTenantOption().catch(console.error)
		getPublicKey().catch(console.error);
		getCaptchaImage().catch(console.error);
	}, []);

	const onSubmit = async (form: API.LoginParam) => {
		const params = getParams(form);
		login({...params})
			.then((res) => {
				// 登录成功
				console.log("登录成功", res)
				// 跳转路由
				const urlParams = new URL(window.location.href).searchParams;
				history.push(urlParams.get('redirect') || '/');
				// 延迟 1 秒显示欢迎信息
				setTimeout(() => {
					message.success(`${timeFix()}，欢迎回来`);
				}, 1000);
			})
			.catch(() => {
				// 登录失败，刷新验证码
				getCaptchaImage();
			});
	};
	return (
		<div
			style={{
				backgroundColor: 'white',
				height: '100vh',
				width: '100vw',
			}}
		>
			<LoginFormPage
				formRef={formRef}
				onFinish={onSubmit}
				backgroundImageUrl="https://gw.alipayobjects.com/zos/rmsportal/FfdJeJRQWjEeGTpqgBKj.png"
				logo={<img alt="logo" src="/logo.png"/>}
				title="老寇IoT云平台"
				subTitle="企业级微服务架构云服务多租户IoT平台"
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

				<ProFormSelect
					name="tenant_id"
					showSearch
					options={tenantOptions}
					debounceTime={300}
					placeholder="请选择租户"
					rules={[
						{
							required: true,
							message: '请选择租户',
						},
					]}
				/>

				{loginType === 'usernamePassword' && (
					<>
						<ProFormText
							name="username"
							fieldProps={{
								size: 'large',
								prefix: <UserOutlined className={'prefixIcon'}/>,
								autoComplete: 'new-password',
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
								autoComplete: 'new-password',
							}}
							placeholder={'请输入密码'}
							rules={[
								{
									required: true,
									message: '请输入密码',
								},
							]}
						/>
						<Row>
							<Col flex={3}>
								<ProFormText
									style={{
										float: 'right',
									}}
									fieldProps={{
										prefix: (
											<SafetyCertificateOutlined className={'prefixIcon'}/>
										),
										autoComplete: 'new-password',
									}}
									name="captcha"
									placeholder={'请输入验证码'}
									rules={[
										{
											required: true,
											message: '请输入验证码',
										},
									]}
								/>
							</Col>
							<Col flex={2}>
								<Image
									src={captchaImage}
									alt="验证码"
									style={{
										display: 'inline-block',
										verticalAlign: 'top',
										cursor: 'pointer',
										paddingLeft: '10px',
										width: '100px',
									}}
									preview={false}
									onClick={() => getCaptchaImage()}
								/>
							</Col>
						</Row>
					</>
				)}
				{loginType === 'mobile' && (
					<>
						<ProFormText
							fieldProps={{
								size: 'large',
								prefix: <MobileOutlined className={'prefixIcon'}/>,
								autoComplete: 'new-password',
							}}
							name="mobile"
							placeholder={'请输入手机号'}
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
								prefix: <SafetyCertificateOutlined className={'prefixIcon'}/>,
								autoComplete: 'new-password',
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
							name="mobile_captcha"
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
				{loginType === 'mail' && (
					<>
						<ProFormText
							fieldProps={{
								size: 'large',
								prefix: <MailOutlined className={'prefixIcon'}/>,
								autoComplete: 'new-password',
							}}
							name="mail"
							placeholder={'请输入邮箱'}
							rules={[
								{
									required: true,
									message: '请输入邮箱',
								},
								{
									pattern: /^\w+(-+.\w+)*@\w+(-.\w+)*.\w+(-.\w+)*$/,
									message: '邮箱格式错误',
								},
							]}
						/>
						<ProFormCaptcha
							fieldProps={{
								size: 'large',
								prefix: <SafetyCertificateOutlined className={'prefixIcon'}/>,
								autoComplete: 'new-password',
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
							name="mail_captcha"
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
