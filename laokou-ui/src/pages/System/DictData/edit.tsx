import React, { useEffect } from 'react';
import {
  ProForm,
  ProFormDigit,
  ProFormText,
  ProFormSelect,
  ProFormRadio,
  ProFormTextArea,
} from '@ant-design/pro-components';
import { Form, Modal} from 'antd';
import { useIntl, FormattedMessage } from '@umijs/max';
import { DictValueEnumObj } from '@/components/DictTag';

export type DataFormData = Record<string, unknown> & Partial<API.System.DictData>;

export type DataFormProps = {
  onCancel: (flag?: boolean, formVals?: DataFormData) => void;
  onSubmit: (values: DataFormData) => Promise<void>;
  open: boolean;
  values: Partial<API.System.DictData>;
  statusOptions: DictValueEnumObj;
};

const DictDataForm: React.FC<DataFormProps> = (props) => {
  const [form] = Form.useForm();

  const { statusOptions } = props;

  useEffect(() => {
    form.resetFields();
    form.setFieldsValue({
      dictCode: props.values.dictCode,
      dictSort: props.values.dictSort,
      dictLabel: props.values.dictLabel,
      dictValue: props.values.dictValue,
      dictType: props.values.dictType,
      cssClass: props.values.cssClass,
      listClass: props.values.listClass,
      isDefault: props.values.isDefault,
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
    props.onSubmit(values as DataFormData);
  };

  return (
    <Modal
      width={640}
      title={intl.formatMessage({
        id: 'system.dict.data.title',
        defaultMessage: '编辑字典数据',
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
          name="dictCode"
          label={intl.formatMessage({
            id: 'system.dict.data.dict_code',
            defaultMessage: '字典编码',
          })}
          colProps={{ md: 24, xl: 24 }}
          placeholder="请输入字典编码"
          disabled
          hidden={true}
          rules={[
            {
              required: false,
              message: <FormattedMessage id="请输入字典编码！" defaultMessage="请输入字典编码！" />,
            },
          ]}
        />
        <ProFormText
          name="dictType"
          label={intl.formatMessage({
            id: 'system.dict.data.dict_type',
            defaultMessage: '字典类型',
          })}
          colProps={{ md: 12, xl: 24 }}
          placeholder="请输入字典类型"
          disabled
          rules={[
            {
              required: false,
              message: <FormattedMessage id="请输入字典类型！" defaultMessage="请输入字典类型！" />,
            },
          ]}
        />
        <ProFormText
          name="dictLabel"
          label={intl.formatMessage({
            id: 'system.dict.data.dict_label',
            defaultMessage: '字典标签',
          })}
          colProps={{ md: 12, xl: 24 }}
          placeholder="请输入字典标签"
          rules={[
            {
              required: false,
              message: <FormattedMessage id="请输入字典标签！" defaultMessage="请输入字典标签！" />,
            },
          ]}
        />
        <ProFormText
          name="dictValue"
          label={intl.formatMessage({
            id: 'system.dict.data.dict_value',
            defaultMessage: '字典键值',
          })}
          colProps={{ md: 12, xl: 24 }}
          placeholder="请输入字典键值"
          rules={[
            {
              required: false,
              message: <FormattedMessage id="请输入字典键值！" defaultMessage="请输入字典键值！" />,
            },
          ]}
        />
        <ProFormText
          name="cssClass"
          label={intl.formatMessage({
            id: 'system.dict.data.css_class',
            defaultMessage: '样式属性',
          })}
          colProps={{ md: 12, xl: 24 }}
          placeholder="请输入样式属性"
          rules={[
            {
              required: false,
              message: <FormattedMessage id="请输入样式属性！" defaultMessage="请输入样式属性！" />,
            },
          ]}
        />
        <ProFormSelect
          name="listClass"
          label={intl.formatMessage({
            id: 'system.dict.data.list_class',
            defaultMessage: '回显样式',
          })}
          colProps={{ md: 12, xl: 24 }}
          placeholder="请输入回显样式"
          valueEnum={{
            'default': '默认',
            'primary': '主要',
            'success': '成功',
            'info': '信息',
            'warning': '警告',
            'danger': '危险',
          }}
          rules={[
            {
              required: false,
              message: <FormattedMessage id="请输入回显样式！" defaultMessage="请输入回显样式！" />,
            },
          ]}
        />
        <ProFormDigit
          name="dictSort"
          label={intl.formatMessage({
            id: 'system.dict.data.dict_sort',
            defaultMessage: '字典排序',
          })}
          colProps={{ md: 12, xl: 12 }}
          placeholder="请输入字典排序"
          rules={[
            {
              required: false,
              message: <FormattedMessage id="请输入字典排序！" defaultMessage="请输入字典排序！" />,
            },
          ]}
        />
        <ProFormRadio.Group
          name="isDefault"
          label={intl.formatMessage({
            id: 'system.dict.data.is_default',
            defaultMessage: '是否默认',
          })}
          valueEnum={{
            'Y': '是',
            'N': '否',
          }}
          initialValue={'N'}
          colProps={{ md: 12, xl: 24 }}
          placeholder="请输入是否默认"
          rules={[
            {
              required: false,
              message: <FormattedMessage id="请输入是否默认！" defaultMessage="请输入是否默认！" />,
            },
          ]}
        />
        <ProFormRadio.Group
          valueEnum={statusOptions}
          name="status"
          label={intl.formatMessage({
            id: 'system.dict.data.status',
            defaultMessage: '状态',
          })}
          initialValue={'0'}
          colProps={{ md: 12, xl: 24 }}
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
            id: 'system.dict.data.remark',
            defaultMessage: '备注',
          })}
          colProps={{ md: 24, xl: 24 }}
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

export default DictDataForm;
