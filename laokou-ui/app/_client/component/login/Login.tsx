'use client'
import React, { useState, useEffect } from 'react';
import { LockOutlined, UserOutlined, LoadingOutlined, SecurityScanOutlined, EyeInvisibleOutlined, EyeTwoTone } from '@ant-design/icons';
import { Button, Spin, Form, Input, Cascader } from 'antd';
import JSEncrypt from 'jsencrypt';
import { nanoid } from '@reduxjs/toolkit';
import { useRouter } from 'next/navigation'

import image from '@/app/_client/api/image';
import login from '@/app/_client/api/requestToken';
import setTooken, { setRefreshToken } from '@/app/_client/utils/token';

interface ChildProps {
    data: data;

}
interface data {
    pk: string;
    image: string;
    tenantsId: number;
    accounts?: any[];
    uuid: string;
}

const Longin: React.FC<ChildProps> = (props) => {
    //路由
    const router = useRouter()
    //基础数据
    const { data } = props;
    const imgBase64Data: string = data.image ? data.image : "";
    const accounts: any[] = data?.accounts ? data.accounts : [];

    //图形验证码
    const [imgBase64, setImgBase64] = useState(imgBase64Data);
    //图形验证码
    const [uuid, setUUID] = useState(data.uuid);


    //默认表单初始化值
    const [form] = Form.useForm();

    const refreshImage = () => {
        const uuid_image = nanoid();
        setImgBase64("");
        image(uuid_image).then((res: any) => {
            setImgBase64(res?.data);
            setUUID(uuid_image);
            form.setFieldsValue({ code: '' });
        })
    }

    //默认账号
    const formData = () => {
        if (0 === data.tenantsId) {
            return {
                company: accounts[0]?.label + " / " + accounts[0].children[0].label,
                username: accounts[0].children[0].label,
                password: accounts[0].children[0].value,
            };
        }
        if (1703312526740615171 === data.tenantsId) {
            return {
                company: accounts[1]?.label + " / " + accounts[1].children[0].label,
                username: accounts[1].children[0].label,
                password: accounts[1].children[0].value,
            }
        }
        return {}
    }

    //监听表单 company 的变化，根据变化更新账号密码
    const companyWatch = Form.useWatch('company', form);
    useEffect(() => {
        console.log('companyWatch', companyWatch)
        if (companyWatch) {
            accounts.map((value: any, index: number) => {
                if (companyWatch[0] === value.value) {
                    if (value.children) {
                        value.children.map(
                            (x: any, y: number) => {
                                if (companyWatch[1] === x.value) {
                                    form.setFieldsValue({ username: x.label, password: x.value });
                                }
                            }
                        )
                    } else {
                        form.setFieldsValue({ username: '', password: '' });
                    }
                }
            })
        }
    }, [companyWatch]);

    //登录
    const onFinish = (values: any) => {
        let code: string = values?.code;
        let username: string = values?.username;
        let password: string = values?.password;
        if (username === 'laok5') {
            password = password.replace('-', '')
        }
        const encrypt = new JSEncrypt();
        encrypt.setPublicKey(data.pk);
        const pwd = encrypt.encrypt(password);
        const accountName = encrypt.encrypt(username);
        form.setFieldsValue({ password: pwd });
        let tenantsId = data.tenantsId;

        login({
            username: accountName,
            password: pwd,
            captcha: code,
            uuid: uuid,
            grant_type: 'password',
            tenant_id: tenantsId,
            auth_type: 0
        }).then((res: any) => {
            const token = res?.access_token;
            const refreshToken = res?.refresh_token;
            const expired = res?.expires_in;
            setTooken(token, expired);
            setRefreshToken(refreshToken);
            if(token){
                router.push('/home')
            }

        })
    };

    return (


        <div style={{ margin: '15% 40%' }}  >

            <Form name="normal_login"
                className="login-form"
                initialValues={formData()}
                onFinish={onFinish}
                form={form}
            >
                <Form.Item
                    name="company"
                    rules={[{ required: true, message: '请选择所属公司' }]}>
                    <Cascader
                        options={accounts}
                        placeholder="选择公司"
                    />
                </Form.Item>

                <Form.Item
                    name="username"
                    rules={[{ required: true, message: 'Please input your Username!' }]}
                >
                    <Input
                        prefix={<UserOutlined className="site-form-item-icon" />}
                        placeholder="Username"
                        autoComplete="username"
                    />

                </Form.Item>

                <Form.Item
                    name="password"
                    rules={[{ required: true, message: 'Please input your Password!' }]}
                >
                    <Input.Password
                        name='password'
                        prefix={<LockOutlined className="site-form-item-icon" />}
                        type="password"
                        placeholder="Password"
                        autoComplete="current-password"
                        iconRender={(visible) => (visible ? <EyeTwoTone /> : <EyeInvisibleOutlined />)}
                    />
                </Form.Item>

                <Form.Item style={{ height: "32px" }}>

                    <Form.Item
                        name="code"
                        rules={[{ required: true, message: 'Please input your code!' },]}
                        style={{ width: '70%', height: "32px", float: "left", marginTop: "2px" }}>

                        <Input
                            prefix={<SecurityScanOutlined className="site-form-item-icon" />}
                            placeholder="验证码"
                            autoComplete="code"
                            name="code" />
                    </Form.Item>


                    <Form.Item style={{ width: '30%', height: "32px", float: "right" }}>
                        {
                            imgBase64 ? (<div style={{ marginLeft: "2px" }}>
                                <img onClick={refreshImage} style={{ width: '112px', height: "40px" }} src={imgBase64} alt="验证码" />
                            </div>) : (
                                <div onClick={refreshImage} style={{ marginLeft: "2px", textAlign: "center" }}>
                                    <Spin indicator={<LoadingOutlined style={{ fontSize: 24, }} spin />} />
                                </div>)
                        }
                    </Form.Item>
                </Form.Item>

                <Form.Item>

                    <Button
                        type="primary"
                        htmlType="submit"
                        className="login-form-button"
                        style={{ width: '100%' }}>
                        Log in
                    </Button>
                </Form.Item>
            </Form>
        </div>
    );
};

export default Longin;