
import React, { useState, useRef, useEffect } from 'react';
import { useIntl, FormattedMessage, useAccess } from '@umijs/max';
import type { FormInstance } from 'antd';
import { Button, message, Modal } from 'antd';
import { ActionType, FooterToolbar, PageContainer, ProColumns, ProTable } from '@ant-design/pro-components';
import { PlusOutlined, DeleteOutlined, ExclamationCircleOutlined, UnlockOutlined } from '@ant-design/icons';
import { getLogininforList, removeLogininfor, exportLogininfor, unlockLogininfor, cleanLogininfor } from '@/services/monitor/logininfor';
import DictTag from '@/components/DictTag';
import { getDictValueEnum } from '@/services/system/dict';

/**
 * 删除节点
 *
 * @param selectedRows
 */
const handleRemove = async (selectedRows: API.Monitor.Logininfor[]) => {
  const hide = message.loading('正在删除');
  if (!selectedRows) return true;
  try {
    const resp = await removeLogininfor(selectedRows.map((row) => row.infoId).join(','));
    hide();
    if (resp.code === 200) {
      message.success('删除成功，即将刷新');
    } else {
      message.error(resp.msg);
    }
    return true;
  } catch (error) {
    hide();
    message.error('删除失败，请重试');
    return false;
  }
};

const handleClean = async () => {
  const hide = message.loading('请稍候');
  try {
    const resp = await cleanLogininfor();
    hide();
    if (resp.code === 200) {
      message.success('清空成功，即将刷新');
    } else {
      message.error(resp.msg);
    }
    return true;
  } catch (error) {
    hide();
    message.error('请求失败，请重试');
    return false;
  }
};

const handleUnlock = async (userName: string) => {
  const hide = message.loading('正在解锁');
  try {
    const resp = await unlockLogininfor(userName);
    hide();
    if (resp.code === 200) {
      message.success('解锁成功，即将刷新');
    } else {
      message.error(resp.msg);
    }
    return true;
  } catch (error) {
    hide();
    message.error('解锁失败，请重试');
    return false;
  }
};

/**
 * 导出数据
 *
 * @param id
 */
const handleExport = async () => {
  const hide = message.loading('正在导出');
  try {
    await exportLogininfor();
    hide();
    message.success('导出成功');
    return true;
  } catch (error) {
    hide();
    message.error('导出失败，请重试');
    return false;
  }
};


const LogininforTableList: React.FC = () => {
  const formTableRef = useRef<FormInstance>();

  const actionRef = useRef<ActionType>();
  const [selectedRows, setSelectedRows] = useState<API.Monitor.Logininfor[]>([]);
  const [statusOptions, setStatusOptions] = useState<any>([]);

  const access = useAccess();

  /** 国际化配置 */
  const intl = useIntl();

  useEffect(() => {
    getDictValueEnum('sys_common_status', true).then((data) => {
      setStatusOptions(data);
    });
  }, []);

  const columns: ProColumns<API.Monitor.Logininfor>[] = [
    {
      title: <FormattedMessage id="monitor.logininfor.info_id" defaultMessage="访问编号" />,
      dataIndex: 'infoId',
      valueType: 'text',
      hideInSearch: true,
    },
    {
      title: <FormattedMessage id="monitor.logininfor.user_name" defaultMessage="用户账号" />,
      dataIndex: 'userName',
      valueType: 'text',
    },
    {
      title: <FormattedMessage id="monitor.logininfor.ipaddr" defaultMessage="登录IP地址" />,
      dataIndex: 'ipaddr',
      valueType: 'text',
    },
    {
      title: <FormattedMessage id="monitor.logininfor.login_location" defaultMessage="登录地点" />,
      dataIndex: 'loginLocation',
      valueType: 'text',
      hideInSearch: true,
    },
    {
      title: <FormattedMessage id="monitor.logininfor.browser" defaultMessage="浏览器类型" />,
      dataIndex: 'browser',
      valueType: 'text',
      hideInSearch: true,
    },
    {
      title: <FormattedMessage id="monitor.logininfor.os" defaultMessage="操作系统" />,
      dataIndex: 'os',
      valueType: 'text',
      hideInSearch: true,
    },
    {
      title: <FormattedMessage id="monitor.logininfor.status" defaultMessage="登录状态" />,
      dataIndex: 'status',
      valueType: 'select',
      valueEnum: statusOptions,
      render: (_, record) => {
        return (<DictTag enums={statusOptions} value={record.status} />);
      },
    },
    {
      title: <FormattedMessage id="monitor.logininfor.msg" defaultMessage="提示消息" />,
      dataIndex: 'msg',
      valueType: 'text',
      hideInSearch: true,
    },
    {
      title: <FormattedMessage id="monitor.logininfor.login_time" defaultMessage="访问时间" />,
      dataIndex: 'loginTime',
      valueType: 'dateTime',
    },
  ];

  return (
    <PageContainer>
      <div style={{ width: '100%', float: 'right' }}>
        <ProTable<API.Monitor.Logininfor>
          headerTitle={intl.formatMessage({
            id: 'pages.searchTable.title',
            defaultMessage: '信息',
          })}
          actionRef={actionRef}
          formRef={formTableRef}
          rowKey="infoId"
          key="logininforList"
          search={{
            labelWidth: 120,
          }}
          toolBarRender={() => [
            <Button
              key="remove"
              danger
              hidden={selectedRows?.length === 0 || !access.hasPerms('monitor:logininfor:remove')}
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
              key="clean"
              danger
              hidden={selectedRows?.length === 0 || !access.hasPerms('monitor:logininfor:remove')}
              onClick={async () => {
                Modal.confirm({
                  title: '是否确认清空所有数据项?',
                  icon: <ExclamationCircleOutlined />,
                  content: '请谨慎操作',
                  async onOk() {
                    const success = await handleClean();
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
              <FormattedMessage id="pages.searchTable.cleanAll" defaultMessage="清空" />
            </Button>,
            <Button
              type="primary"
              key="unlock"
              hidden={selectedRows?.length === 0 || !access.hasPerms('monitor:logininfor:unlock')}
              onClick={async () => {
                Modal.confirm({
                  title: '是否确认解锁该用户的数据项?',
                  icon: <ExclamationCircleOutlined />,
                  content: '请谨慎操作',
                  async onOk() {
                    const success = await handleUnlock(selectedRows[0].userName);
                    if (success) {
                      setSelectedRows([]);
                      actionRef.current?.reloadAndRest?.();
                    }
                  },
                  onCancel() { },
                });
              }}
            >
              <UnlockOutlined />
              <FormattedMessage id="monitor.logininfor.unlock" defaultMessage="解锁" />
            </Button>,
            <Button
              type="primary"
              key="export"
              hidden={!access.hasPerms('monitor:logininfor:export')}
              onClick={async () => {
                handleExport();
              }}
            >
              <PlusOutlined />
              <FormattedMessage id="pages.searchTable.export" defaultMessage="导出" />
            </Button>,
          ]}
          request={(params) =>
            getLogininforList({ ...params } as API.Monitor.LogininforListParams).then((res) => {
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
      </div>
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
            hidden={!access.hasPerms('monitor:logininfor:remove')}
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
    </PageContainer>
  );
};

export default LogininforTableList;
