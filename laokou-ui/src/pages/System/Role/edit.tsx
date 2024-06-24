import React, { useEffect, useState } from 'react';
import {
  ProForm,
  ProFormDigit,
  ProFormText,
  ProFormRadio,
  ProFormTextArea,
} from '@ant-design/pro-components';
import { Form, Modal } from 'antd';
import { useIntl, FormattedMessage } from '@umijs/max';
import Tree, { DataNode } from 'antd/es/tree';
import { DictValueEnumObj } from '@/components/DictTag';

export type RoleFormData = Record<string, unknown> & Partial<API.System.Role>;

export type RoleFormProps = {
  onCancel: (flag?: boolean, formVals?: RoleFormData) => void;
  onSubmit: (values: RoleFormData) => Promise<void>;
  open: boolean;
  values: Partial<API.System.Role>;
  menuTree: DataNode[];
  menuCheckedKeys: string[];
  statusOptions: DictValueEnumObj;
};

const RoleForm: React.FC<RoleFormProps> = (props) => {
  const [form] = Form.useForm();
  const { menuTree, menuCheckedKeys } = props;
  const [menuIds, setMenuIds] = useState<string[]>([]);
  const { statusOptions } = props;

  useEffect(() => {
    form.resetFields();
    form.setFieldsValue({
      roleId: props.values.roleId,
      roleName: props.values.roleName,
      roleKey: props.values.roleKey,
      roleSort: props.values.roleSort,
      dataScope: props.values.dataScope,
      menuCheckStrictly: props.values.menuCheckStrictly,
      deptCheckStrictly: props.values.deptCheckStrictly,
      status: props.values.status,
      delFlag: props.values.delFlag,
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
    props.onSubmit({ ...values, menuIds } as RoleFormData);
  };

  return (
    <Modal
      width={640}
      title={intl.formatMessage({
        id: 'system.role.title',
        defaultMessage: '编辑角色信息',
      })}
      forceRender
      open={props.open}
      destroyOnClose
      onOk={handleOk}
      onCancel={handleCancel}
    >
      <ProForm
        form={form}
        grid={true}
        layout="horizontal"
        submitter={false}
        onFinish={handleFinish}>
        <ProFormDigit
          name="roleId"
          label={intl.formatMessage({
            id: 'system.role.role_id',
            defaultMessage: '角色编号',
          })}
          placeholder="请输入角色编号"
          disabled
          hidden={true}
          rules={[
            {
              required: false,
              message: <FormattedMessage id="请输入角色编号！" defaultMessage="请输入角色编号！" />,
            },
          ]}
        />
        <ProFormText
          name="roleName"
          label={intl.formatMessage({
            id: 'system.role.role_name',
            defaultMessage: '角色名称',
          })}
          placeholder="请输入角色名称"
          rules={[
            {
              required: true,
              message: <FormattedMessage id="请输入角色名称！" defaultMessage="请输入角色名称！" />,
            },
          ]}
        />
        <ProFormText
          name="roleKey"
          label={intl.formatMessage({
            id: 'system.role.role_key',
            defaultMessage: '权限字符串',
          })}
          placeholder="请输入角色权限字符串"
          rules={[
            {
              required: true,
              message: <FormattedMessage id="请输入角色权限字符串！" defaultMessage="请输入角色权限字符串！" />,
            },
          ]}
        />
        <ProFormDigit
          name="roleSort"
          label={intl.formatMessage({
            id: 'system.role.role_sort',
            defaultMessage: '显示顺序',
          })}
          placeholder="请输入显示顺序"
          rules={[
            {
              required: true,
              message: <FormattedMessage id="请输入显示顺序！" defaultMessage="请输入显示顺序！" />,
            },
          ]}
          fieldProps = {{
            defaultValue: 1
          }}
        />
        <ProFormRadio.Group
          valueEnum={statusOptions}
          name="status"
          label={intl.formatMessage({
            id: 'system.role.status',
            defaultMessage: '角色状态',
          })}
          placeholder="请输入角色状态"
          rules={[
            {
              required: true,
              message: <FormattedMessage id="请输入角色状态！" defaultMessage="请输入角色状态！" />,
            },
          ]}
          fieldProps = {{
            defaultValue: "0"
          }}
        />
        <ProForm.Item
          name="menuIds"
          label={intl.formatMessage({
            id: 'system.role.auth',
            defaultMessage: '菜单权限',
          })}
        >
          <Tree
            checkable={true}
            multiple={true}
            checkStrictly={true}
            defaultExpandAll={false}
            treeData={menuTree}
            defaultCheckedKeys={menuCheckedKeys}
            onCheck={(checkedKeys: any) => {             
              return setMenuIds(checkedKeys.checked);
            }}
          />
        </ProForm.Item>
        <ProFormTextArea
          name="remark"
          label={intl.formatMessage({
            id: 'system.role.remark',
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

export default RoleForm;
