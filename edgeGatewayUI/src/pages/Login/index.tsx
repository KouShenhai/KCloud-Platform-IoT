import { request } from '@umijs/max';
import { Button, Form, Input, message } from 'antd';
import React from 'react';

const Login: React.FC = () => {
  const onFinish = async (values: any) => {
    try {
      const res = await request('/api/v1/login', {
        method: 'POST',
        data: values,
      });
      if (res.code === 200) {
        message.success('Login successful');
        localStorage.setItem('edge_token', res.data.token);
        window.location.href = '/home';
      } else {
        message.error(res.message || 'Login failed');
      }
    } catch (e) {
      message.error('Login error');
    }
  };

  return (
    <div style={{ padding: 50, maxWidth: 400, margin: '0 auto' }}>
      <h1>Edge Gateway Login</h1>
      <Form onFinish={onFinish}>
        <Form.Item name="username" rules={[{ required: true }]}>
          <Input placeholder="Username" />
        </Form.Item>
        <Form.Item name="password" rules={[{ required: true }]}>
          <Input.Password placeholder="Password" />
        </Form.Item>
        <Form.Item>
          <Button type="primary" htmlType="submit" block>
            Login
          </Button>
        </Form.Item>
      </Form>
    </div>
  );
};

export default Login;
