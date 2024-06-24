import React, { useEffect } from 'react';
import {
  ProForm,
  ProFormDigit,
  ProFormText,
  ProFormTextArea,
  ProFormRadio,
  } from '@ant-design/pro-components';
import { Form, Modal} from 'antd';
import { useIntl, FormattedMessage } from '@umijs/max';
import { DictValueEnumObj } from '@/components/DictTag';

export type ConfigFormData = Record<string, unknown> & Partial<API.System.Config>;

export type ConfigFormProps = {
  onCancel: (flag?: boolean, formVals?: ConfigFormData) => void;
  onSubmit: (values: ConfigFormData) => Promise<void>;
  open: boolean;
  values: Partial<API.System.Config>;
  configTypeOptions: DictValueEnumObj;
};

const ConfigForm: React.FC<ConfigFormProps> = (props) => {
  const [form] = Form.useForm();
  
  const { configTypeOptions } = props;

  useEffect(() => {
    form.resetFields();
    form.setFieldsValue({
			configId: props.values.configId,
			configName: props.values.configName,
			configKey: props.values.configKey,
			configValue: props.values.configValue,
			configType: props.values.configType,
			createBy: props.values.createBy,
			createTime: props.values.createTime,
			updateBy: props.values.updateBy,
			updateTime: props.values.updateTime,
			remark: props.values.remark,
    });
  }, [form, props]);

  const intl = useIntl();
  const handleOk = () => {
    form.submit();
  };
  const handleCancel = () => {
    props.onCancel();
  };
  const handleFinish = async (values: Record<string, any>) => {
    props.onSubmit(values as ConfigFormData);
  };

  return (
    <Modal
      width={640}
      title={intl.formatMessage({
        id: 'system.config.title',
        defaultMessage: '编辑参数配置',
      })}
      open={props.open}
      forceRender
      destroyOnClose
      onOk={handleOk}
      onCancel={handleCancel}
    >
		  <ProForm 
        form={form}
        grid={true}
        submitter={false}
        layout="horizontal" 
        onFinish={handleFinish}>
        <ProFormDigit
          name="configId"
          label={intl.formatMessage({
            id: 'system.config.config_id',
            defaultMessage: '参数主键',
          })}
          colProps={{ md: 24 }}
          placeholder="请输入参数主键"
          disabled
          hidden={true}
          rules={[
            {
              required: false,
              message: <FormattedMessage id="请输入参数主键！" defaultMessage="请输入参数主键！" />,                  
            },
          ]}
        />
        <ProFormText
          name="configName"
          label={intl.formatMessage({
            id: 'system.config.config_name',
            defaultMessage: '参数名称',
          })}
          colProps={{ md: 24 }}
          placeholder="请输入参数名称"
          rules={[
            {
              required: false,
              message: <FormattedMessage id="请输入参数名称！" defaultMessage="请输入参数名称！" />,                  
            },
          ]}
        />
        <ProFormText
          name="configKey"
          label={intl.formatMessage({
            id: 'system.config.config_key',
            defaultMessage: '参数键名',
          })}
          colProps={{ md: 24 }}
          placeholder="请输入参数键名"
          rules={[
            {
              required: false,
              message: <FormattedMessage id="请输入参数键名！" defaultMessage="请输入参数键名！" />,                  
            },
          ]}
        />
        <ProFormTextArea
          name="configValue"
          label={intl.formatMessage({
            id: 'system.config.config_value',
            defaultMessage: '参数键值',
          })}
          colProps={{ md: 24 }}
          placeholder="请输入参数键值"
          rules={[
            {
              required: false,
              message: <FormattedMessage id="请输入参数键值！" defaultMessage="请输入参数键值！" />,                  
            },
          ]}
        />
        <ProFormRadio.Group
          valueEnum={configTypeOptions}
          name="configType"
          label={intl.formatMessage({
            id: 'system.config.config_type',
            defaultMessage: '系统内置',
          })}
          colProps={{ md: 24 }}
          placeholder="请输入系统内置"
          rules={[
            {
              required: false,
              message: <FormattedMessage id="请输入系统内置！" defaultMessage="请输入系统内置！" />,                  
            },
          ]}
        />
        <ProFormTextArea
          name="remark"
          label={intl.formatMessage({
            id: 'system.config.remark',
            defaultMessage: '备注',
          })}
          colProps={{ md: 24 }}
          placeholder="请输入备注"
          rules={[
            {
              required: false,
              message: <FormattedMessage id="请输入备注！" defaultMessage="请输入备注！" />,                  
            },
          ]}
        />
      </ProForm>
    </Modal>
  );
};

export default ConfigForm;
