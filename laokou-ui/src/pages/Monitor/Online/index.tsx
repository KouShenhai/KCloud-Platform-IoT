import type { FormInstance } from 'antd';
import { Button, message, Modal } from 'antd';
import React, { useRef, useEffect } from 'react';
import { useIntl, FormattedMessage, useAccess } from '@umijs/max';
import { getOnlineUserList, forceLogout } from '@/services/monitor/online';
import { ActionType, ProColumns, ProTable } from '@ant-design/pro-components';
import { DeleteOutlined } from '@ant-design/icons';
 

/* *
 *
 * @author whiteshader@163.com
 * @datetime  2023/02/07
 * 
 * */


const handleForceLogout = async (selectedRow: API.Monitor.OnlineUserType) => {
  const hide = message.loading('正在强制下线');
  try {
    await forceLogout(selectedRow.tokenId);
    hide();
    message.success('强制下线成功，即将刷新');
    return true;
  } catch (error) {
    hide();
    message.error('强制下线失败，请重试');
    return false;
  }
};

const OnlineUserTableList: React.FC = () => {
  const formTableRef = useRef<FormInstance>();
  const actionRef = useRef<ActionType>();
  const access = useAccess();
  const intl = useIntl();

  useEffect(() => {}, []);

  const columns: ProColumns<API.Monitor.OnlineUserType>[] = [
    {
      title: <FormattedMessage id="monitor.online.user.token_id" defaultMessage="会话编号" />,
      dataIndex: 'tokenId',
      valueType: 'text',
      hideInSearch: true,
    },
    {
      title: <FormattedMessage id="monitor.online.user.user_name" defaultMessage="用户账号" />,
      dataIndex: 'userName',
      valueType: 'text',
    },
    {
      title: <FormattedMessage id="monitor.online.user.dept_name" defaultMessage="部门名称" />,
      dataIndex: 'deptName',
      valueType: 'text',
      hideInSearch: true,
    },
    {
      title: <FormattedMessage id="monitor.online.user.ipaddr" defaultMessage="登录IP地址" />,
      dataIndex: 'ipaddr',
      valueType: 'text',
    },
    {
      title: <FormattedMessage id="monitor.online.user.login_location" defaultMessage="登录地点" />,
      dataIndex: 'loginLocation',
      valueType: 'text',
      hideInSearch: true,
    },
    {
      title: <FormattedMessage id="monitor.online.user.browser" defaultMessage="浏览器类型" />,
      dataIndex: 'browser',
      valueType: 'text',
      hideInSearch: true,
    },
    {
      title: <FormattedMessage id="monitor.online.user.os" defaultMessage="操作系统" />,
      dataIndex: 'os',
      valueType: 'text',
      hideInSearch: true,
    },
    {
      title: <FormattedMessage id="monitor.online.user.login_time" defaultMessage="登录时间" />,
      dataIndex: 'loginTime',
      valueType: 'dateRange',
      render: (_, record) => <span>{record.loginTime}</span>,
      hideInSearch: true,
      search: {
        transform: (value) => {
          return {
            'params[beginTime]': value[0],
            'params[endTime]': value[1],
          };
        },
      },
    },
    {
      title: <FormattedMessage id="pages.searchTable.titleOption" defaultMessage="操作" />,
      dataIndex: 'option',
      width: '60px',
      valueType: 'option',
      render: (_, record) => [
        <Button
          type="link"
          size="small"
          danger
          key="batchRemove"
          icon=<DeleteOutlined />
          hidden={!access.hasPerms('monitor:online:forceLogout')}
          onClick={async () => {
            Modal.confirm({
              title: '强踢',
              content: '确定强制将该用户踢下线吗？',
              okText: '确认',
              cancelText: '取消',
              onOk: async () => {
                const success = await handleForceLogout(record);
                if (success) {
                  if (actionRef.current) {
                    actionRef.current.reload();
                  }
                }
              },
            });
          }}
        >
          强退
        </Button>,
      ],
    },
  ];

  return (
      <div style={{ width: '100%', float: 'right' }}>
        <ProTable<API.Monitor.OnlineUserType>
          headerTitle={intl.formatMessage({
            id: 'pages.searchTable.title',
            defaultMessage: '信息',
          })}
          actionRef={actionRef}
          formRef={formTableRef}
          rowKey="tokenId"
          key="logininforList"
          search={{
            labelWidth: 120,
          }}
          request={(params) =>
            getOnlineUserList({ ...params } as API.Monitor.OnlineUserListParams).then((res) => {
              const result = {
                data: res.rows,
                total: res.total,
                success: true,
              };
              return result;
            })
          }
          columns={columns}
        />
      </div>
  );
};

export default OnlineUserTableList;
