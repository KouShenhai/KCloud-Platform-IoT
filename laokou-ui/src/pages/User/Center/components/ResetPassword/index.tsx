import React from 'react';
import { Form, message } from 'antd';
import { FormattedMessage, useIntl } from '@umijs/max';
import { updateUserPwd } from '@/services/system/user';
import { ProForm, ProFormText } from '@ant-design/pro-components';

const ResetPassword: React.FC = () => {
  const [form] = Form.useForm();
  const intl = useIntl();

  const handleFinish = async (values: Record<string, any>) => {
    const resp = await updateUserPwd(values.oldPassword, values.newPassword);
    if (resp.code === 200) {
      message.success('密码重置成功。');
    } else {
      message.warning(resp.msg);
    }
  };

  const checkPassword = (rule: any, value: string) => {
    const login_password = form.getFieldValue('newPassword');
    if (value === login_password) {
      return Promise.resolve();
    }
    return Promise.reject(new Error('两次密码输入不一致'));
  };

  return (
    <>
      <ProForm form={form} onFinish={handleFinish}>       
          <ProFormText.Password
            name="oldPassword"
            label={intl.formatMessage({
              id: 'system.user.old_password',
              defaultMessage: '旧密码',
            })}
            width="xl"
            placeholder="请输入旧密码"
            rules={[
              {
                required: true,
                message: <FormattedMessage id="请输入旧密码！" defaultMessage="请输入旧密码！" />,
              },
            ]}
          />
          <ProFormText.Password
            name="newPassword"
            label={intl.formatMessage({
              id: 'system.user.new_password',
              defaultMessage: '新密码',
            })}
            width="xl"
            placeholder="请输入新密码"
            rules={[
              {
                required: true,
                message: <FormattedMessage id="请输入新密码！" defaultMessage="请输入新密码！" />,
              },
            ]}
          />
          <ProFormText.Password
            name="confirmPassword"
            label={intl.formatMessage({
              id: 'system.user.confirm_password',
              defaultMessage: '确认密码',
            })}
            width="xl"
            placeholder="请输入确认密码"
            rules={[
              {
                required: true,
                message: (
                  <FormattedMessage id="请输入确认密码！" defaultMessage="请输入确认密码！" />
                ),
              },
              { validator: checkPassword },
            ]}
          />
      </ProForm>
    </>
  );
};

export default ResetPassword;
