import React, { useEffect } from 'react';
import {
  ProForm,
  ProFormDigit,
  ProFormText,
  ProFormRadio,
  ProFormTreeSelect,
} from '@ant-design/pro-components';
import { Form, Modal} from 'antd';
import { useIntl, FormattedMessage } from '@umijs/max';
import { DataNode } from 'antd/es/tree';
import { DictValueEnumObj } from '@/components/DictTag';

export type DeptFormData = Record<string, unknown> & Partial<API.System.Dept>;

export type DeptFormProps = {
  onCancel: (flag?: boolean, formVals?: DeptFormData) => void;
  onSubmit: (values: DeptFormData) => Promise<void>;
  open: boolean;
  values: Partial<API.System.Dept>;
  deptTree: DataNode[];
  statusOptions: DictValueEnumObj;
};

const DeptForm: React.FC<DeptFormProps> = (props) => {
  const [form] = Form.useForm();

  const { statusOptions, deptTree } = props;

  useEffect(() => {
    form.resetFields();
    form.setFieldsValue({
      deptId: props.values.deptId,
      parentId: props.values.parentId,
      ancestors: props.values.ancestors,
      deptName: props.values.deptName,
      orderNum: props.values.orderNum,
      leader: props.values.leader,
      phone: props.values.phone,
      email: props.values.email,
      status: props.values.status,
      delFlag: props.values.delFlag,
      createBy: props.values.createBy,
      createTime: props.values.createTime,
      updateBy: props.values.updateBy,
      updateTime: props.values.updateTime,
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
    props.onSubmit(values as DeptFormData);
  };

  return (
    <Modal
      width={640}
      title={intl.formatMessage({
        id: 'system.dept.title',
        defaultMessage: '编辑部门',
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
          name="deptId"
          label={intl.formatMessage({
            id: 'system.dept.dept_id',
            defaultMessage: '部门id',
          })}
          colProps={{ md: 12, xl: 12 }}
          placeholder="请输入部门id"
          disabled
          hidden={true}
          rules={[
            {
              required: false,
              message: <FormattedMessage id="请输入部门id！" defaultMessage="请输入部门id！" />,
            },
          ]}
        />
        <ProFormTreeSelect
          name="parentId"
          label={intl.formatMessage({
            id: 'system.dept.parent_dept',
            defaultMessage: '上级部门:',
          })}
          request={async () => {
            return deptTree;
          }}
          placeholder="请选择上级部门"
          rules={[
            {
              required: true,
              message: (
                <FormattedMessage id="请输入用户昵称！" defaultMessage="请选择上级部门!" />
              ),
            },
          ]}
        />
        <ProFormText
          name="deptName"
          label={intl.formatMessage({
            id: 'system.dept.dept_name',
            defaultMessage: '部门名称',
          })}
          colProps={{ md: 12, xl: 12 }}
          placeholder="请输入部门名称"
          rules={[
            {
              required: false,
              message: <FormattedMessage id="请输入部门名称！" defaultMessage="请输入部门名称！" />,
            },
          ]}
        />
        <ProFormDigit
          name="orderNum"
          label={intl.formatMessage({
            id: 'system.dept.order_num',
            defaultMessage: '显示顺序',
          })}
          colProps={{ md: 12, xl: 12 }}
          placeholder="请输入显示顺序"
          rules={[
            {
              required: false,
              message: <FormattedMessage id="请输入显示顺序！" defaultMessage="请输入显示顺序！" />,
            },
          ]}
        />
        <ProFormText
          name="leader"
          label={intl.formatMessage({
            id: 'system.dept.leader',
            defaultMessage: '负责人',
          })}
          colProps={{ md: 12, xl: 12 }}
          placeholder="请输入负责人"
          rules={[
            {
              required: false,
              message: <FormattedMessage id="请输入负责人！" defaultMessage="请输入负责人！" />,
            },
          ]}
        />
        <ProFormText
          name="phone"
          label={intl.formatMessage({
            id: 'system.dept.phone',
            defaultMessage: '联系电话',
          })}
          colProps={{ md: 12, xl: 12 }}
          placeholder="请输入联系电话"
          rules={[
            {
              required: false,
              message: <FormattedMessage id="请输入联系电话！" defaultMessage="请输入联系电话！" />,
            },
          ]}
        />
        <ProFormText
          name="email"
          label={intl.formatMessage({
            id: 'system.dept.email',
            defaultMessage: '邮箱',
          })}
          colProps={{ md: 12, xl: 12 }}
          placeholder="请输入邮箱"
          rules={[
            {
              required: false,
              message: <FormattedMessage id="请输入邮箱！" defaultMessage="请输入邮箱！" />,
            },
          ]}
        />
        <ProFormRadio.Group
          valueEnum={statusOptions}
          name="status"
          label={intl.formatMessage({
            id: 'system.dept.status',
            defaultMessage: '部门状态',
          })}
          colProps={{ md: 12, xl: 12 }}
          placeholder="请输入部门状态"
          rules={[
            {
              required: false,
              message: <FormattedMessage id="请输入部门状态！" defaultMessage="请输入部门状态！" />,
            },
          ]}
        />
      </ProForm>
    </Modal>
  );
};

export default DeptForm;
