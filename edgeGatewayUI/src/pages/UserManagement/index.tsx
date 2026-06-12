import {
  ActionType,
  ModalForm,
  PageContainer,
  ProColumns,
  ProFormSelect,
  ProFormSwitch,
  ProFormText,
  ProTable,
} from '@ant-design/pro-components';
import { Badge, Button, message, Popconfirm, Space, Tag } from 'antd';
import React, { useRef, useState } from 'react';
import {
  createUser,
  deleteUser,
  listUsers,
  resetPassword,
  updateUser,
  User,
  UserRole,
  UserStatus,
} from '@/services/user';

const roleOptions = [
  { label: 'admin', value: 'admin' },
  { label: 'operator', value: 'operator' },
];

const passwordRules = [
  { required: true, message: 'Please enter a password' },
  { min: 8, message: 'Password must be at least 8 characters' },
];

const currentUserId = () => {
  const token = localStorage.getItem('edge_token');
  if (!token) {
    return '';
  }
  try {
    const body = token.split('.')[1] || '';
    const normalized = body.replace(/-/g, '+').replace(/_/g, '/');
    const padded = normalized.padEnd(
      normalized.length + ((4 - (normalized.length % 4)) % 4),
      '=',
    );
    const payload = JSON.parse(atob(padded));
    return payload.userId || '';
  } catch {
    return '';
  }
};

const UserManagement: React.FC = () => {
  const actionRef = useRef<ActionType>();
  const [createOpen, setCreateOpen] = useState(false);
  const [editUser, setEditUser] = useState<User>();
  const [passwordUser, setPasswordUser] = useState<User>();

  const columns: ProColumns<User>[] = [
    {
      title: 'Username',
      dataIndex: 'username',
      formItemProps: {
        rules: [{ required: true, message: 'Please enter a username' }],
      },
    },
    {
      title: 'Role',
      dataIndex: 'role',
      search: false,
      render: (_, record) => (
        <Tag color={record.role === 'admin' ? 'blue' : 'green'}>
          {record.role}
        </Tag>
      ),
    },
    {
      title: 'Status',
      dataIndex: 'status',
      search: false,
      render: (_, record) => (
        <Badge
          status={record.status === 'active' ? 'success' : 'default'}
          text={record.status}
        />
      ),
    },
    {
      title: 'Created',
      dataIndex: 'createdAt',
      valueType: 'dateTime',
      search: false,
    },
    {
      title: 'Actions',
      valueType: 'option',
      width: 220,
      render: (_, record) => (
        <Space>
          <a onClick={() => setEditUser(record)}>Edit</a>
          <a onClick={() => setPasswordUser(record)}>Reset Password</a>
          <Popconfirm
            title="Delete user?"
            onConfirm={async () => {
              await deleteUser(record.id);
              message.success('Deleted');
              actionRef.current?.reload();
            }}
          >
            <a>Delete</a>
          </Popconfirm>
        </Space>
      ),
    },
  ];

  return (
    <PageContainer header={{ title: 'User Management' }}>
      <ProTable<User>
        actionRef={actionRef}
        rowKey="id"
        columns={columns}
        pagination={{ defaultPageSize: 10 }}
        search={{ labelWidth: 90 }}
        toolBarRender={() => [
          <Button
            key="create"
            type="primary"
            onClick={() => setCreateOpen(true)}
          >
            New User
          </Button>,
        ]}
        request={async (params) => {
          const res = await listUsers({
            keyword: params.username as string,
            page: params.current,
            pageSize: params.pageSize,
          });
          return {
            data: res.data.list,
            total: res.data.total,
            success: res.code === 200,
          };
        }}
      />

      <ModalForm<{
        username: string;
        password: string;
        role: UserRole;
      }>
        title="New User"
        open={createOpen}
        modalProps={{
          destroyOnClose: true,
          onCancel: () => setCreateOpen(false),
        }}
        onFinish={async (values) => {
          await createUser(values);
          message.success('Created');
          setCreateOpen(false);
          actionRef.current?.reload();
          return true;
        }}
      >
        <ProFormText
          name="username"
          label="Username"
          rules={[
            { required: true, message: 'Please enter a username' },
            {
              pattern: /^[A-Za-z0-9_]{3,32}$/,
              message: 'Use 3-32 letters, numbers, or underscores',
            },
          ]}
        />
        <ProFormText.Password
          name="password"
          label="Password"
          rules={passwordRules}
        />
        <ProFormSelect
          name="role"
          label="Role"
          initialValue="operator"
          options={roleOptions}
          rules={[{ required: true, message: 'Please select a role' }]}
        />
      </ModalForm>

      <ModalForm<{
        username: string;
        role: UserRole;
        enabled: boolean;
      }>
        title="Edit User"
        open={!!editUser}
        initialValues={
          editUser
            ? {
                username: editUser.username,
                role: editUser.role,
                enabled: editUser.status === 'active',
              }
            : undefined
        }
        modalProps={{
          destroyOnClose: true,
          onCancel: () => setEditUser(undefined),
        }}
        onFinish={async (values) => {
          if (!editUser) {
            return false;
          }
          await updateUser(editUser.id, {
            username: values.username,
            role: values.role,
            status: (values.enabled ? 'active' : 'disabled') as UserStatus,
          });
          message.success('Updated');
          setEditUser(undefined);
          actionRef.current?.reload();
          return true;
        }}
      >
        <ProFormText
          name="username"
          label="Username"
          rules={[
            { required: true, message: 'Please enter a username' },
            {
              pattern: /^[A-Za-z0-9_]{3,32}$/,
              message: 'Use 3-32 letters, numbers, or underscores',
            },
          ]}
        />
        <ProFormSelect
          name="role"
          label="Role"
          options={roleOptions}
          rules={[{ required: true, message: 'Please select a role' }]}
        />
        <ProFormSwitch name="enabled" label="Enabled" />
      </ModalForm>

      <ModalForm<{
        oldPassword?: string;
        newPassword: string;
      }>
        title="Reset Password"
        open={!!passwordUser}
        modalProps={{
          destroyOnClose: true,
          onCancel: () => setPasswordUser(undefined),
        }}
        onFinish={async (values) => {
          if (!passwordUser) {
            return false;
          }
          await resetPassword(passwordUser.id, values);
          message.success('Password updated');
          setPasswordUser(undefined);
          return true;
        }}
      >
        {passwordUser?.id === currentUserId() && (
          <ProFormText.Password
            name="oldPassword"
            label="Old Password"
            rules={passwordRules}
          />
        )}
        <ProFormText.Password
          name="newPassword"
          label="New Password"
          rules={passwordRules}
        />
      </ModalForm>
    </PageContainer>
  );
};

export default UserManagement;
