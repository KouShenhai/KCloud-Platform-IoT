
import React, { useState, useRef, useEffect } from 'react';
import { useIntl, FormattedMessage, useAccess } from '@umijs/max';
import { Card, Col, Dropdown, FormInstance, Row, Space, Switch } from 'antd';
import { Button, message, Modal } from 'antd';
import { ActionType, FooterToolbar, PageContainer, ProColumns, ProTable } from '@ant-design/pro-components';
import { PlusOutlined, DeleteOutlined, ExclamationCircleOutlined, DownOutlined, EditOutlined } from '@ant-design/icons';
import { getUserList, removeUser, addUser, updateUser, exportUser, getUser, changeUserStatus, updateAuthRole, resetUserPwd } from '@/services/system/user';
import UpdateForm from './edit';
import { getDictValueEnum } from '@/services/system/dict';
import { DataNode } from 'antd/es/tree';
import { getDeptTree } from '@/services/system/user';
import DeptTree from './components/DeptTree';
import ResetPwd from './components/ResetPwd';
import { getPostList } from '@/services/system/post';
import { getRoleList } from '@/services/system/role';
import AuthRoleForm from './components/AuthRole';

const { confirm } = Modal;

/* *
 *
 * @author whiteshader@163.com
 * @datetime  2023/02/06
 * 
 * */

/**
 * 添加节点
 *
 * @param fields
 */
const handleAdd = async (fields: API.System.User) => {
  const hide = message.loading('正在添加');
  try {
    await addUser({ ...fields });
    hide();
    message.success('添加成功');
    return true;
  } catch (error) {
    hide();
    message.error('添加失败请重试！');
    return false;
  }
};

/**
 * 更新节点
 *
 * @param fields
 */
const handleUpdate = async (fields: API.System.User) => {
  const hide = message.loading('正在配置');
  try {
    await updateUser(fields);
    hide();
    message.success('配置成功');
    return true;
  } catch (error) {
    hide();
    message.error('配置失败请重试！');
    return false;
  }
};

/**
 * 删除节点
 *
 * @param selectedRows
 */
const handleRemove = async (selectedRows: API.System.User[]) => {
  const hide = message.loading('正在删除');
  if (!selectedRows) return true;
  try {
    await removeUser(selectedRows.map((row) => row.userId).join(','));
    hide();
    message.success('删除成功，即将刷新');
    return true;
  } catch (error) {
    hide();
    message.error('删除失败，请重试');
    return false;
  }
};

const handleRemoveOne = async (selectedRow: API.System.User) => {
  const hide = message.loading('正在删除');
  if (!selectedRow) return true;
  try {
    const params = [selectedRow.userId];
    await removeUser(params.join(','));
    hide();
    message.success('删除成功，即将刷新');
    return true;
  } catch (error) {
    hide();
    message.error('删除失败，请重试');
    return false;
  }
};

/**
 * 导出数据
 *
 * 
 */
const handleExport = async () => {
  const hide = message.loading('正在导出');
  try {
    await exportUser();
    hide();
    message.success('导出成功');
    return true;
  } catch (error) {
    hide();
    message.error('导出失败，请重试');
    return false;
  }
};

const UserTableList: React.FC = () => {
  const [messageApi, contextHolder] = message.useMessage();

  const formTableRef = useRef<FormInstance>();

  const [modalVisible, setModalVisible] = useState<boolean>(false);
  const [resetPwdModalVisible, setResetPwdModalVisible] = useState<boolean>(false);
  const [authRoleModalVisible, setAuthRoleModalVisible] = useState<boolean>(false);

  const actionRef = useRef<ActionType>();
  const [currentRow, setCurrentRow] = useState<API.System.User>();
  const [selectedRows, setSelectedRows] = useState<API.System.User[]>([]);

  const [selectDept, setSelectDept] = useState<any>({ id: 0 });
  const [sexOptions, setSexOptions] = useState<any>([]);
  const [statusOptions, setStatusOptions] = useState<any>([]);

  const [postIds, setPostIds] = useState<number[]>();
  const [postList, setPostList] = useState<any[]>();
  const [roleIds, setRoleIds] = useState<number[]>();
  const [roleList, setRoleList] = useState<any[]>();
  const [deptTree, setDeptTree] = useState<DataNode[]>();

  const access = useAccess();

  /** 国际化配置 */
  const intl = useIntl();

  useEffect(() => {
    getDictValueEnum('sys_user_sex').then((data) => {
      setSexOptions(data);
    });
    getDictValueEnum('sys_normal_disable').then((data) => {
      setStatusOptions(data);
    });
  }, []);

  const showChangeStatusConfirm = (record: API.System.User) => {
    let text = record.status === "1" ? "启用" : "停用";
    const newStatus = record.status === '0' ? '1' : '0';
    confirm({
      title: `确认要${text}${record.userName}用户吗？`,
      onOk() {
        changeUserStatus(record.userId, newStatus).then(resp => {
          if (resp.code === 200) {
            messageApi.open({
              type: 'success',
              content: '更新成功！',
            });
            actionRef.current?.reload();
          } else {
            messageApi.open({
              type: 'error',
              content: '更新失败！',
            });
          }
        });
      },
    });
  };
  
  const fetchUserInfo = async (userId: number) => {
    const res = await getUser(userId);
    setPostIds(res.postIds);
    setPostList(
      res.posts.map((item: any) => {
        return {
          value: item.postId,
          label: item.postName,
        };
      }),
    );
    setRoleIds(res.roleIds);
    setRoleList(
      res.roles.map((item: any) => {
        return {
          value: item.roleId,
          label: item.roleName,
        };
      }),
    );
  };

  const columns: ProColumns<API.System.User>[] = [
    {
      title: <FormattedMessage id="system.user.user_id" defaultMessage="用户编号" />,
      dataIndex: 'deptId',
      valueType: 'text',
    },
    {
      title: <FormattedMessage id="system.user.user_name" defaultMessage="用户账号" />,
      dataIndex: 'userName',
      valueType: 'text',
    },
    {
      title: <FormattedMessage id="system.user.nick_name" defaultMessage="用户昵称" />,
      dataIndex: 'nickName',
      valueType: 'text',
    },
    {
      title: <FormattedMessage id="system.user.dept_name" defaultMessage="部门" />,
      dataIndex: ['dept', 'deptName'],
      valueType: 'text',
      hideInSearch: true
    },
    {
      title: <FormattedMessage id="system.user.phonenumber" defaultMessage="手机号码" />,
      dataIndex: 'phonenumber',
      valueType: 'text',
    },
    {
      title: <FormattedMessage id="system.user.status" defaultMessage="帐号状态" />,
      dataIndex: 'status',
      valueType: 'select',
      valueEnum: statusOptions,
      render: (_, record) => {
        return (
          <Switch
            checked={record.status === '0'}
            checkedChildren="正常"
            unCheckedChildren="停用"
            defaultChecked
            onClick={() => showChangeStatusConfirm(record)}
          />)
      },
    },
    {
      title: <FormattedMessage id="pages.searchTable.titleOption" defaultMessage="操作" />,
      dataIndex: 'option',
      width: '220px',
      valueType: 'option',
      render: (_, record) => [
        <Button
          type="link"
          size="small"
          key="edit"
          icon=<EditOutlined />
          hidden={!access.hasPerms('system:user:edit')}
          onClick={async () => {
            fetchUserInfo(record.userId);
            const treeData = await getDeptTree({});
            setDeptTree(treeData);
            setModalVisible(true);
            setCurrentRow(record);
          }}
        >
          编辑
        </Button>,
        <Button
          type="link"
          size="small"
          danger
          icon=<DeleteOutlined />
          key="batchRemove"
          hidden={!access.hasPerms('system:user:remove')}
          onClick={async () => {
            Modal.confirm({
              title: '删除',
              content: '确定删除该项吗？',
              okText: '确认',
              cancelText: '取消',
              onOk: async () => {
                const success = await handleRemoveOne(record);
                if (success) {
                  if (actionRef.current) {
                    actionRef.current.reload();
                  }
                }
              },
            });
          }}
        >
          删除
        </Button>,
        <Dropdown
          key="more"
          menu={{
            items: [
              {
                label: <FormattedMessage id="system.user.reset.password" defaultMessage="密码重置" />,
                key: 'reset',
                disabled: !access.hasPerms('system:user:edit'),
              },
              {
                label: '分配角色',
                key: 'authRole',
                disabled: !access.hasPerms('system:user:edit'),
              },
            ],
            onClick: ({ key }) => {
              if (key === 'reset') {
                setResetPwdModalVisible(true);
                setCurrentRow(record);
              }
              else if (key === 'authRole') {
                fetchUserInfo(record.userId);
                setAuthRoleModalVisible(true);
                setCurrentRow(record);
              }
            }
          }}
        >
          <a onClick={(e) => e.preventDefault()}>
            <Space>
              <DownOutlined />
              更多
            </Space>
          </a>
        </Dropdown>,
      ],
    },
  ];

  return (
    <PageContainer>
      {contextHolder}
      <Row gutter={[16, 24]}>
        <Col lg={6} md={24}>
          <Card>
            <DeptTree
              onSelect={async (value: any) => {
                setSelectDept(value);
                if (actionRef.current) {
                  formTableRef?.current?.submit();
                }
              }}
            />
          </Card>
        </Col>
        <Col lg={18} md={24}>
          <ProTable<API.System.User>
            headerTitle={intl.formatMessage({
              id: 'pages.searchTable.title',
              defaultMessage: '信息',
            })}
            actionRef={actionRef}
            formRef={formTableRef}
            rowKey="userId"
            key="userList"
            search={{
              labelWidth: 120,
            }}
            toolBarRender={() => [
              <Button
                type="primary"
                key="add"
                hidden={!access.hasPerms('system:user:add')}
                onClick={async () => {
                  const treeData = await getDeptTree({});
                  setDeptTree(treeData);

                  const postResp = await getPostList()
                  if (postResp.code === 200) {
                    setPostList(
                      postResp.rows.map((item: any) => {
                        return {
                          value: item.postId,
                          label: item.postName,
                        };
                      }),
                    );
                  }

                  const roleResp = await getRoleList()
                  if (roleResp.code === 200) {
                    setRoleList(
                      roleResp.rows.map((item: any) => {
                        return {
                          value: item.roleId,
                          label: item.roleName,
                        };
                      }),
                    );
                  }
                  setCurrentRow(undefined);
                  setModalVisible(true);
                }}
              >
                <PlusOutlined /> <FormattedMessage id="pages.searchTable.new" defaultMessage="新建" />
              </Button>,
              <Button
                type="primary"
                key="remove"
                hidden={selectedRows?.length === 0 || !access.hasPerms('system:user:remove')}
                onClick={async () => {
                  Modal.confirm({
                    title: '是否确认删除所选数据项?',
                    icon: <ExclamationCircleOutlined />,
                    content: '请谨慎操作',
                    async onOk() {
                      const success = await handleRemove(selectedRows);
                      if (success) {
                        setSelectedRows([]);
                        actionRef.current?.reloadAndRest?.();
                      }
                    },
                    onCancel() { },
                  });
                }}
              >
                <DeleteOutlined />
                <FormattedMessage id="pages.searchTable.delete" defaultMessage="删除" />
              </Button>,
              <Button
                type="primary"
                key="export"
                hidden={!access.hasPerms('system:user:export')}
                onClick={async () => {
                  handleExport();
                }}
              >
                <PlusOutlined />
                <FormattedMessage id="pages.searchTable.export" defaultMessage="导出" />
              </Button>,
            ]}
            request={(params) =>
              getUserList({ ...params, deptId: selectDept.id } as API.System.UserListParams).then((res) => {
                const result = {
                  data: res.rows,
                  total: res.total,
                  success: true,
                };
                return result;
              })
            }
            columns={columns}
            rowSelection={{
              onChange: (_, selectedRows) => {
                setSelectedRows(selectedRows);
              },
            }}
          />
        </Col>
      </Row>
      {selectedRows?.length > 0 && (
        <FooterToolbar
          extra={
            <div>
              <FormattedMessage id="pages.searchTable.chosen" defaultMessage="已选择" />
              <a style={{ fontWeight: 600 }}>{selectedRows.length}</a>
              <FormattedMessage id="pages.searchTable.item" defaultMessage="项" />
            </div>
          }
        >
          <Button
            key="remove"
            hidden={!access.hasPerms('system:user:del')}
            onClick={async () => {
              Modal.confirm({
                title: '删除',
                content: '确定删除该项吗？',
                okText: '确认',
                cancelText: '取消',
                onOk: async () => {
                  const success = await handleRemove(selectedRows);
                  if (success) {
                    setSelectedRows([]);
                    actionRef.current?.reloadAndRest?.();
                  }
                },
              });
            }}
          >
            <FormattedMessage id="pages.searchTable.batchDeletion" defaultMessage="批量删除" />
          </Button>
        </FooterToolbar>
      )}
      <UpdateForm
        onSubmit={async (values) => {
          let success = false;
          if (values.userId) {
            success = await handleUpdate({ ...values } as API.System.User);
          } else {
            success = await handleAdd({ ...values } as API.System.User);
          }
          if (success) {
            setModalVisible(false);
            setCurrentRow(undefined);
            if (actionRef.current) {
              actionRef.current.reload();
            }
          }
        }}
        onCancel={() => {
          setModalVisible(false);
          setCurrentRow(undefined);
        }}
        open={modalVisible}
        values={currentRow || {}}
        sexOptions={sexOptions}
        statusOptions={statusOptions}
        posts={postList || []}
        postIds={postIds || []}
        roles={roleList || []}
        roleIds={roleIds || []}
        depts={deptTree || []}
      />
      <ResetPwd
        onSubmit={async (values: any) => {
          const success = await resetUserPwd(values.userId, values.password);
          if (success) {
            setResetPwdModalVisible(false);
            setSelectedRows([]);
            setCurrentRow(undefined);
            message.success('密码重置成功。');
          }
        }}
        onCancel={() => {
          setResetPwdModalVisible(false);
          setSelectedRows([]);
          setCurrentRow(undefined);
        }}
        open={resetPwdModalVisible}
        values={currentRow || {}}
      />
      <AuthRoleForm
        onSubmit={async (values: any) => {
          const success = await updateAuthRole(values);
          if (success) {
            setAuthRoleModalVisible(false);
            setSelectedRows([]);
            setCurrentRow(undefined);
            message.success('配置成功。');
          }
        }}
        onCancel={() => {
          setAuthRoleModalVisible(false);
          setSelectedRows([]);
          setCurrentRow(undefined);
        }}
        open={authRoleModalVisible}
        roles={roleList || []}
        roleIds={roleIds || []}
      />
    </PageContainer>
  );
};

export default UserTableList;
