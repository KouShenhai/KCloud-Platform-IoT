import React, { useEffect } from 'react';
import {
  ProForm,
  ProFormDigit,
  ProFormText,
  ProFormRadio,
  ProFormTextArea,
  } from '@ant-design/pro-components';
import { Form, Modal} from 'antd';
import { useIntl, FormattedMessage } from '@umijs/max';
import { DictValueEnumObj } from '@/components/DictTag';

export type DictTypeFormData = Record<string, unknown> & Partial<API.System.DictType>;

export type DictTypeFormProps = {
  onCancel: (flag?: boolean, formVals?: DictTypeFormData) => void;
  onSubmit: (values: DictTypeFormData) => Promise<void>;
  open: boolean;
  values: Partial<API.System.DictType>;
  statusOptions: DictValueEnumObj;
};

const DictTypeForm: React.FC<DictTypeFormProps> = (props) => {
  const [form] = Form.useForm();

  const { statusOptions } = props;

  useEffect(() => {
    form.resetFields();
    form.setFieldsValue({
			dictId: props.values.dictId,
			dictName: props.values.dictName,
			dictType: props.values.dictType,
			status: props.values.status,
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
    props.onSubmit(values as DictTypeFormData);
  };

  return (
    <Modal
      width={640}
      title={intl.formatMessage({
        id: 'system.dict.title',
        defaultMessage: '编辑字典类型',
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
          name="dictId"
          label={intl.formatMessage({
            id: 'system.dict.dict_id',
            defaultMessage: '字典主键',
          })}
          placeholder="请输入字典主键"
          disabled
          hidden={true}
          rules={[
            {
              required: false,
              message: <FormattedMessage id="请输入字典主键！" defaultMessage="请输入字典主键！" />,
            },
          ]}
        />
        <ProFormText
          name="dictName"
          label={intl.formatMessage({
            id: 'system.dict.dict_name',
            defaultMessage: '字典名称',
          })}
          placeholder="请输入字典名称"
          rules={[
            {
              required: false,
              message: <FormattedMessage id="请输入字典名称！" defaultMessage="请输入字典名称！" />,
            },
          ]}
        />
        <ProFormText
          name="dictType"
          label={intl.formatMessage({
            id: 'system.dict.dict_type',
            defaultMessage: '字典类型',
          })}
          placeholder="请输入字典类型"
          rules={[
            {
              required: false,
              message: <FormattedMessage id="请输入字典类型！" defaultMessage="请输入字典类型！" />,
            },
          ]}
        />
        <ProFormRadio.Group
          valueEnum={statusOptions}
          name="status"
          label={intl.formatMessage({
            id: 'system.dict.status',
            defaultMessage: '状态',
          })}
          initialValue={'0'}
          placeholder="请输入状态"
          rules={[
            {
              required: false,
              message: <FormattedMessage id="请输入状态！" defaultMessage="请输入状态！" />,
            },
          ]}
        />
        <ProFormTextArea
          name="remark"
          label={intl.formatMessage({
            id: 'system.dict.remark',
            defaultMessage: '备注',
          })}
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

export default DictTypeForm;
