import React, { useEffect } from 'react';
import {
  ProForm,
  ProFormDigit,
  ProFormText,
  ProFormRadio,
  ProFormTimePicker,
  } from '@ant-design/pro-components';
import { Form, Modal} from 'antd';
import { useIntl, FormattedMessage } from '@umijs/max';
import { DictValueEnumObj } from '@/components/DictTag';

export type LogininforFormData = Record<string, unknown> & Partial<API.Monitor.Logininfor>;

export type LogininforFormProps = {
  onCancel: (flag?: boolean, formVals?: LogininforFormData) => void;
  onSubmit: (values: LogininforFormData) => Promise<void>;
  open: boolean;
  values: Partial<API.Monitor.Logininfor>;
  statusOptions: DictValueEnumObj;
};

const LogininforForm: React.FC<LogininforFormProps> = (props) => {
  const [form] = Form.useForm();
  
  const { statusOptions, } = props;

  useEffect(() => {
    form.resetFields();
    form.setFieldsValue({
			infoId: props.values.infoId,
			userName: props.values.userName,
			ipaddr: props.values.ipaddr,
			loginLocation: props.values.loginLocation,
			browser: props.values.browser,
			os: props.values.os,
			status: props.values.status,
			msg: props.values.msg,
			loginTime: props.values.loginTime,
    });
  }, [form, props]);

  const intl = useIntl();
  const handleOk = () => {
    form.submit();
  };
  const handleCancel = () => {
    props.onCancel();
    form.resetFields();
  };
  const handleFinish = async (values: Record<string, any>) => {
    props.onSubmit(values as LogininforFormData);
  };

  return (
    <Modal
      width={640}
      title={intl.formatMessage({
        id: 'system.logininfor.title',
        defaultMessage: '编辑系统访问记录',
      })}
      open={props.open}
      destroyOnClose
      forceRender
      onOk={handleOk}
      onCancel={handleCancel}
    >
		  <ProForm 
        form={form}
        grid={true}
        layout="horizontal" 
        onFinish={handleFinish}>
        <ProFormDigit
          name="infoId"
          label={intl.formatMessage({
            id: 'system.logininfor.info_id',
            defaultMessage: '访问编号',
          })}
          colProps={{ md: 12, xl: 24 }}
          placeholder="请输入访问编号"
          disabled
          hidden={true}
          rules={[
            {
              required: false,
              message: <FormattedMessage id="请输入访问编号！" defaultMessage="请输入访问编号！" />,                  
            },
          ]}
        />
        <ProFormText
          name="userName"
          label={intl.formatMessage({
            id: 'system.logininfor.user_name',
            defaultMessage: '用户账号',
          })}
          colProps={{ md: 12, xl: 24 }}
          placeholder="请输入用户账号"
          rules={[
            {
              required: false,
              message: <FormattedMessage id="请输入用户账号！" defaultMessage="请输入用户账号！" />,                  
            },
          ]}
        />
        <ProFormText
          name="ipaddr"
          label={intl.formatMessage({
            id: 'system.logininfor.ipaddr',
            defaultMessage: '登录IP地址',
          })}
          colProps={{ md: 12, xl: 24 }}
          placeholder="请输入登录IP地址"
          rules={[
            {
              required: false,
              message: <FormattedMessage id="请输入登录IP地址！" defaultMessage="请输入登录IP地址！" />,                  
            },
          ]}
        />
        <ProFormText
          name="loginLocation"
          label={intl.formatMessage({
            id: 'system.logininfor.login_location',
            defaultMessage: '登录地点',
          })}
          colProps={{ md: 12, xl: 24 }}
          placeholder="请输入登录地点"
          rules={[
            {
              required: false,
              message: <FormattedMessage id="请输入登录地点！" defaultMessage="请输入登录地点！" />,                  
            },
          ]}
        />
        <ProFormText
          name="browser"
          label={intl.formatMessage({
            id: 'system.logininfor.browser',
            defaultMessage: '浏览器类型',
          })}
          colProps={{ md: 12, xl: 24 }}
          placeholder="请输入浏览器类型"
          rules={[
            {
              required: false,
              message: <FormattedMessage id="请输入浏览器类型！" defaultMessage="请输入浏览器类型！" />,                  
            },
          ]}
        />
        <ProFormText
          name="os"
          label={intl.formatMessage({
            id: 'system.logininfor.os',
            defaultMessage: '操作系统',
          })}
          colProps={{ md: 12, xl: 24 }}
          placeholder="请输入操作系统"
          rules={[
            {
              required: false,
              message: <FormattedMessage id="请输入操作系统！" defaultMessage="请输入操作系统！" />,                  
            },
          ]}
        />
        <ProFormRadio.Group
          valueEnum={statusOptions}
          name="status"
          label={intl.formatMessage({
            id: 'system.logininfor.status',
            defaultMessage: '登录状态',
          })}
          colProps={{ md: 12, xl: 24 }}
          placeholder="请输入登录状态"
          rules={[
            {
              required: false,
              message: <FormattedMessage id="请输入登录状态！" defaultMessage="请输入登录状态！" />,                  
            },
          ]}
        />
        <ProFormText
          name="msg"
          label={intl.formatMessage({
            id: 'system.logininfor.msg',
            defaultMessage: '提示消息',
          })}
          colProps={{ md: 12, xl: 24 }}
          placeholder="请输入提示消息"
          rules={[
            {
              required: false,
              message: <FormattedMessage id="请输入提示消息！" defaultMessage="请输入提示消息！" />,                  
            },
          ]}
        />
        <ProFormTimePicker
          name="loginTime"
          label={intl.formatMessage({
            id: 'system.logininfor.login_time',
            defaultMessage: '访问时间',
          })}
          colProps={{ md: 12, xl: 24 }}
          placeholder="请输入访问时间"
          rules={[
            {
              required: false,
              message: <FormattedMessage id="请输入访问时间！" defaultMessage="请输入访问时间！" />,                  
            },
          ]}
        />
      </ProForm>
    </Modal>
  );
};

export default LogininforForm;
