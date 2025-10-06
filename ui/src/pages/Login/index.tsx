import {
	AlipayOutlined,
	GithubOutlined,
	LockOutlined,
	MailOutlined,
	MobileOutlined,
	QqOutlined,
	SafetyCertificateOutlined,
	TeamOutlined,
	UserOutlined,
	WechatOutlined,
} from '@ant-design/icons';
import {CaptFieldRef, LoginFormPage, ProFormCaptcha, ProFormInstance, ProFormText,} from '@ant-design/pro-components';
import {Col, Divider, Image, message, Row, Space, Tabs} from 'antd';
import {CSSProperties, useEffect, useRef, useState} from 'react';
import {login} from '@/services/auth/auth';
import {getCaptchaByUuid, sendCaptcha } from '@/services/auth/captcha';
import {getSecretInfo} from '@/services/auth/secret';
import {JSEncrypt} from 'jsencrypt';
import {v7 as uuidV7} from 'uuid';
import {clearToken, setToken} from "@/access"
import {history} from "@umijs/max";

const USERNAME_PASSWORD = {key: 'username_password', label: '用户名密码登录'};
const MOBILE = {key: 'mobile', label: '手机号登录'};
const MAIL = {key: 'mail', label: '邮箱登录'};
type LoginType = 'username_password' | 'mobile' | 'mail';

const iconStyles: CSSProperties = {
	color: 'rgba(0, 0, 0, 0.2)',
	fontSize: '18px',
	verticalAlign: 'middle',
	cursor: 'pointer',
};

export default () => {
	const items = [
		USERNAME_PASSWORD,
		MOBILE,
		MAIL
	];
	const [loading, setLoading] = useState<boolean>(false);
	const [loginType, setLoginType] = useState<LoginType>('username_password');
	const [captchaImage, setCaptchaImage] = useState<string>('');
	const [uuid, setUuid] = useState<string>('');
	const [publicKey, setPublicKey] = useState<string>('');
	const formRef = useRef<ProFormInstance>();
	const mailCaptchaRef = useRef<CaptFieldRef | null | undefined>();
	const mobileCaptchaRef = useRef<CaptFieldRef | null | undefined>();

	const setFormField = (form: API.LoginParam) => {
		formRef?.current?.setFieldsValue(form);
	}
	const encrypt = new JSEncrypt();

	const getParams = (form: API.LoginParam) => {
		switch (loginType) {
			case USERNAME_PASSWORD.key: {
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
					tenant_code: form.tenant_code,
					grant_type: USERNAME_PASSWORD.key,
				};
			}
			case MAIL.key:
				return {
					mail: form.mail,
					code: form.mail_captcha,
					tenant_code: form.tenant_code,
					grant_type: MAIL.key
				};
			case MOBILE.key:
				return {
					mobile: form.mobile,
					code: form.mobile_captcha,
					tenant_code: form.tenant_code,
					grant_type: MOBILE.key
				};
		}
	};

	const getCaptchaImage = async () => {
		// 清空验证码输入框
		setFormField({captcha: ''})
		// 调用验证码API
		const uuid = uuidV7();
		// @ts-ignore
		getCaptchaByUuid({uuid: uuid}).then((res: { code: string; data: React.SetStateAction<string>; }) => {
			if (res.code === 'OK') {
				setCaptchaImage(res.data);
			}
		});
		setUuid(uuid);
	};

	const getPublicKey = async () => {
		// @ts-ignore
		getSecretInfo().then((res: { code: string; data: { publicKey: React.SetStateAction<string>; }; }) => {
			if (res.code === 'OK') {
				setPublicKey(res.data.publicKey);
			}
		});
	};

	const sendMailCaptcha = async () => {
		const param = {
			tenantCode: formRef?.current?.getFieldValue("tenant_code"),
			uuid: formRef?.current?.getFieldValue(MAIL.key)
		}
		const co = { co : param }
		sendCaptcha('mail', co as API.SendCaptchaCO, uuidV7()).then(res => {
			if (res.code !== "OK") {
				mailCaptchaRef.current?.endTiming()
			}
		}).catch(console.log)
	}

	const sendMobileCaptcha = async () => {
		const param = {
			tenantCode: formRef?.current?.getFieldValue("tenant_code"),
			uuid: formRef?.current?.getFieldValue(MOBILE.key)
		}
		const co = { co : param }
		sendCaptcha('mobile', co as API.SendCaptchaCO, uuidV7()).then(res => {
			if (res.code !== "OK") {
				mobileCaptchaRef.current?.endTiming()
			}
		}).catch(console.log)
	}

	const clearMailCaptcha = () => {
		setFormField({mail_captcha: ''})
	}

	const clearMobileCaptcha = () => {
		setFormField({mobile_captcha: ''})
	}

	const timeFix 	= () => {
		const time = new Date();
		const hour = time.getHours();
		return hour < 9 ? '早上好' : hour <= 11 ? '上午好' : hour <= 13 ? '中午好' : hour < 20 ? '下午好' : '晚上好';
	};

	useEffect(() => {
		clearToken()
		getPublicKey().catch(console.log)
		getCaptchaImage().catch(console.log)
	}, []);

	const onSubmit = async (form: API.LoginParam) => {
		setLoading(true)
		const params = getParams(form);
		login({...params})
			// @ts-ignore
			.then((res: {
				code: string;
				data: { access_token: string; refresh_token: string; expires_in: number; };
			}) => {
				if (res.code === 'OK') {
					// 提示框【登录成功】
					message.success(`${timeFix()}，欢迎回来`).then();
					// 登录成功，存储令牌
					setToken(res.data?.access_token, res.data?.refresh_token, res.data?.expires_in * 1000 + new Date().getTime())
					// 获取跳转地址
					const urlParams = new URL(window.location.href).searchParams;
					// 跳转路由
					history.push(urlParams.get('redirect') || '/');
					// 1.5秒后刷新页面
					setTimeout(() => {
						window.location.reload();
					}, 1500)
				}
			})
			.catch(() => {
				// 登录失败，刷新验证码
				getCaptchaImage();
				clearMailCaptcha();
				clearMobileCaptcha();
			}).finally(() => setLoading(false));
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
				loading={loading}
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

				<ProFormText
					disabled={loading}
					name="tenant_code"
					fieldProps={{
						size: 'large',
						prefix: <TeamOutlined className={'prefixIcon'}/>,
						autoComplete: 'new-password',
					}}
					placeholder={'请输入租户编码'}
					rules={[
						{
							required: true,
							message: '请输入租户编码',
						},
					]}
				/>

				{loginType === USERNAME_PASSWORD.key && (
					<>
						<ProFormText
							disabled={loading}
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
							disabled={loading}
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
						<Row gutter={24}>
							<Col span={16}>
								<ProFormText
									disabled={loading}
									width={"sm"}
									fieldProps={{
										size: 'large',
										prefix: <SafetyCertificateOutlined className={'prefixIcon'}/>,
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
							<Col span={8}>
								<Image
									src={captchaImage}
									alt="验证码"
									style={{
										maxWidth: "100%",
										minHeight: "32px"
									}}
									preview={false}
									onClick={() => getCaptchaImage()}
								/>
							</Col>
						</Row>
					</>
				)}
				{loginType === MOBILE.key && (
					<>
						<ProFormText
							disabled={loading}
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
							disabled={loading}
							fieldProps={{
								size: 'large',
								prefix: <SafetyCertificateOutlined className={'prefixIcon'}/>,
								autoComplete: 'new-password',
							}}
							fieldRef={mobileCaptchaRef}
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
				{loginType === MAIL.key && (
					<>
						<ProFormText
							disabled={loading}
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
							disabled={loading}
							fieldProps={{
								size: 'large',
								prefix: <SafetyCertificateOutlined className={'prefixIcon'}/>,
								autoComplete: 'new-password',
							}}
							fieldRef={mailCaptchaRef}
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
