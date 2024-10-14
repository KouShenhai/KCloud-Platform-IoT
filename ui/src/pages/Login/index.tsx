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
// @ts-ignore
import {login} from '@/services/auth/auth';
// @ts-ignore
import {getCaptchaImageByUuidV3, sendCaptchaV3} from '@/services/auth/captcha';
// @ts-ignore
import {history} from 'umi';
// @ts-ignore
import {getSecretInfoV3} from '@/services/auth/secret';
import {JSEncrypt} from 'jsencrypt';
// @ts-ignore
import {v7 as uuidV7} from 'uuid';
// @ts-ignore
import {getTenantIdByDomainNameV3, listTenantOptionV3} from "@/services/auth/tenant"
import {ProFormInstance, ProFormSelect} from "@ant-design/pro-form/lib"
// @ts-ignore
import {clearToken, setToken} from "@/access"
// @ts-ignore
import {getTokenV3} from "@/services/auth/tokens"

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
	const [tenantOptions, setTenantOptions] = useState<API.TenantOptionParam[]>([])
	const formRef = useRef<ProFormInstance>();
	const [requestToken, setRequestToken] = useState<string>('')

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
				return {
					mail: form.mail,
					code: form.mail_captcha,
					tenant_id: form.tenant_id,
					grant_type: 'mail'
				};
			case 'mobile':
				return {
					mobile: form.mobile,
					code: form.mobile_captcha,
					tenant_id: form.tenant_id,
					grant_type: 'mobile'
				};
		}
	};

	const getCaptchaImage = async () => {
		// 清空验证码输入框
		setFormField({captcha: ''})
		// 调用验证码API
		const uuid = uuidV7();
		// @ts-ignore
        getCaptchaImageByUuidV3({uuid: uuid}).then((res: { code: string; data: React.SetStateAction<string>; }) => {
			if (res.code === 'OK') {
				setCaptchaImage(res.data);
			}
		});
		setUuid(uuid);
	};

	const getPublicKey = async () => {
		// @ts-ignore
        getSecretInfoV3().then((res: { code: string; data: { publicKey: React.SetStateAction<string>; }; }) => {
			if (res.code === 'OK') {
				setPublicKey(res.data.publicKey);
			}
		});
	};

	const getToken = async () => {
		// @ts-ignore
        getTokenV3().then((res: { data: React.SetStateAction<string>; }) => {
			setRequestToken(res.data)
		})
	}

	const listTenantOption = async () => {
		// @ts-ignore
        listTenantOptionV3().then((res: { code: string; data: API.TenantOptionParam[]; }) => {
			if (res.code === 'OK') {
				const options = []
				const defaultOption = {label: '老寇云集团', value: '0'}
				options.push(defaultOption)
				res.data.forEach((item: API.TenantOptionParam) => options.push(item))
				setTenantOptions(options)
			}
		})
	}

	const sendMailCaptcha = async () => {
		const param = {
			tenantId: formRef?.current?.getFieldValue("tenant_id"),
			tag: 'mailCaptcha',
			uuid: formRef?.current?.getFieldValue("mail")
		}
		sendCaptchaV3(param as API.SendCaptchaParam, requestToken).catch(console.log)
	}

	const sendMobileCaptcha = async () => {
		const param = {
			tenantId: formRef?.current?.getFieldValue("tenant_id"),
			tag: 'mobileCaptcha',
			uuid: formRef?.current?.getFieldValue("mobile")
		}
		sendCaptchaV3(param as API.SendCaptchaParam, requestToken).catch(console.log)
	}

	const getTenantIdByDomain = async () => {
		// @ts-ignore
        getTenantIdByDomainNameV3().then((res: { code: string; data: any; }) => {
			if (res.code === 'OK') {
				setFormField({tenant_id: res.data})
			}
		})
	}

	const clearMailCaptcha = () => {
		setFormField({mail_captcha: ''})
	}

	const clearMobileCaptcha = () => {
		setFormField({mobile_captcha: ''})
	}

	useEffect(() => {
		clearToken()
		listTenantOption().catch(console.log)
		getPublicKey().catch(console.log)
		getCaptchaImage().catch(console.log)
		getTenantIdByDomain().catch(console.log)
		getToken().catch(console.log)
	}, []);

	const onSubmit = async (form: API.LoginParam) => {
		const params = getParams(form);
        login({...params})
            // @ts-ignore
            .then((res: { code: string; data: { access_token: string; refresh_token: string; expires_in: number; }; }) => {
				if (res.code === 'OK') {
					// 登录成功【令牌过期前5分钟，自动刷新令牌】
					clearToken()
					// @ts-ignore
					setToken(res.data?.access_token, res.data?.refresh_token, new Date().getTime() + res.data?.expires_in)
					// 跳转路由
					const urlParams = new URL(window.location.href).searchParams;
					history.push(urlParams.get('redirect') || '/');
					// 延迟 1 秒显示欢迎信息
					setTimeout(() => {
						message.success(`${timeFix()}，欢迎回来`);
					}, 1000);
				}
			})
			.catch(() => {
				// 登录失败，刷新验证码
				getCaptchaImage();
				clearMailCaptcha();
				clearMobileCaptcha();
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
				backgroundImageUrl={"/FfdJeJRQWjEeGTpqgBKj.png"}
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
										{
											pattern: /^[A-Za-z0-9]{4}$/,
											message: '请输入4位验证码（数字和字母）'
										}
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
								}
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
								{
									pattern: /^\d{6}$/,
									message: '请输入6位数字的验证码'
								}
							]}
							onGetCaptcha={sendMobileCaptcha}
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
								{
									pattern: /^\d{6}$/,
									message: '请输入6位数字的验证码'
								}
							]}
							onGetCaptcha={sendMailCaptcha}
						/>
					</>
				)}
			</LoginFormPage>
		</div>
	);
};
