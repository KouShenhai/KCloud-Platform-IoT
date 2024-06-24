
import React, { useState, useRef, useEffect } from 'react';
import { useIntl, FormattedMessage, useAccess, history } from '@umijs/max';
import { Dropdown, FormInstance, Space } from 'antd';
import { Button, message, Modal } from 'antd';
import { ActionType, FooterToolbar, PageContainer, ProColumns, ProTable } from '@ant-design/pro-components';
import { PlusOutlined, DeleteOutlined, ExclamationCircleOutlined, DownOutlined, EditOutlined } from '@ant-design/icons';
import { getJobList, removeJob, addJob, updateJob, exportJob, runJob } from '@/services/monitor/job';
import { getDictSelectOption, getDictValueEnum } from '@/services/system/dict';
import UpdateForm from './edit';
import DetailForm from './detail';
import DictTag from '@/components/DictTag';

/**
 * 定时任务调度 List Page
 * 
 * @author whiteshader
 * @date 2023-02-07
 */

/**
 * 添加节点
 *
 * @param fields
 */
const handleAdd = async (fields: API.Monitor.Job) => {
  const hide = message.loading('正在添加');
  try {
    const resp = await addJob({ ...fields });
    hide();
    if (resp.code === 200) {
      message.success('添加成功');
    } else {
      message.error(resp.msg);
    }
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
const handleUpdate = async (fields: API.Monitor.Job) => {
  const hide = message.loading('正在更新');
  try {
    const resp = await updateJob(fields);
    hide();
    if (resp.code === 200) {
      message.success('更新成功');
    } else {
      message.error(resp.msg);
    }
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
const handleRemove = async (selectedRows: API.Monitor.Job[]) => {
  const hide = message.loading('正在删除');
  if (!selectedRows) return true;
  try {
    const resp = await removeJob(selectedRows.map((row) => row.jobId).join(','));
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

const handleRemoveOne = async (selectedRow: API.Monitor.Job) => {
  const hide = message.loading('正在删除');
  if (!selectedRow) return true;
  try {
    const params = [selectedRow.jobId];
    const resp = await removeJob(params.join(','));
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

/**
 * 导出数据
 *
 */
const handleExport = async () => {
  const hide = message.loading('正在导出');
  try {
    await exportJob();
    hide();
    message.success('导出成功');
    return true;
  } catch (error) {
    hide();
    message.error('导出失败，请重试');
    return false;
  }
};


const JobTableList: React.FC = () => {
  const formTableRef = useRef<FormInstance>();

  const [modalVisible, setModalVisible] = useState<boolean>(false);
  const [detailModalVisible, setDetailModalVisible] = useState<boolean>(false);

  const actionRef = useRef<ActionType>();
  const [currentRow, setCurrentRow] = useState<API.Monitor.Job>();
  const [selectedRows, setSelectedRows] = useState<API.Monitor.Job[]>([]);

  const [jobGroupOptions, setJobGroupOptions] = useState<any>([]);
  const [statusOptions, setStatusOptions] = useState<any>([]);

  const access = useAccess();

  /** 国际化配置 */
  const intl = useIntl();

  useEffect(() => {
    getDictSelectOption('sys_job_group').then((data) => {
      setJobGroupOptions(data);
    });
    getDictValueEnum('sys_normal_disable').then((data) => {
      setStatusOptions(data);
    });
  }, []);

  const columns: ProColumns<API.Monitor.Job>[] = [
    {
      title: <FormattedMessage id="monitor.job.job_id" defaultMessage="任务编号" />,
      dataIndex: 'jobId',
      valueType: 'text',
      hideInSearch: true,
    },
    {
      title: <FormattedMessage id="monitor.job.job_name" defaultMessage="任务名称" />,
      dataIndex: 'jobName',
      valueType: 'text',
      render: (dom, record) => {
        return (
          <a
            onClick={() => {
              setDetailModalVisible(true);
              setCurrentRow(record);
            }}
          >
            {dom}
          </a>
        );
      },
    },
    {
      title: <FormattedMessage id="monitor.job.job_group" defaultMessage="任务组名" />,
      dataIndex: 'jobGroup',
      valueType: 'text',
      valueEnum: jobGroupOptions,
      render: (_, record) => {
        return (<DictTag options={jobGroupOptions} value={record.jobGroup} />);
      },
    },
    {
      title: <FormattedMessage id="monitor.job.invoke_target" defaultMessage="调用目标字符串" />,
      dataIndex: 'invokeTarget',
      valueType: 'textarea',
    },
    {
      title: <FormattedMessage id="monitor.job.cron_expression" defaultMessage="cron执行表达式" />,
      dataIndex: 'cronExpression',
      valueType: 'text',
    },
    {
      title: <FormattedMessage id="monitor.job.status" defaultMessage="状态" />,
      dataIndex: 'status',
      valueType: 'select',
      valueEnum: statusOptions,
      render: (_, record) => {
        return (<DictTag enums={statusOptions} value={record.status} />);
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
          icon = <EditOutlined />
          hidden={!access.hasPerms('monitor:job:edit')}
          onClick={() => {
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
          key="batchRemove"
          icon = <DeleteOutlined />
          hidden={!access.hasPerms('monitor:job:remove')}
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
                label: '执行一次',
                key: 'runOnce',
              },
              {
                label: '详细',
                key: 'detail',
              },
              {
                label: '历史',
                key: 'log',
              },
            ],
            onClick: ({ key }) => {
              if (key === 'runOnce') {
                Modal.confirm({
                  title: '警告',
                  content: '确认要立即执行一次？',
                  okText: '确认',
                  cancelText: '取消',
                  onOk: async () => {
                    const success = await runJob(record.jobId, record.jobGroup);
                    if (success) {
                      message.success('执行成功');
                    }
                  },
                });
              }
              else if (key === 'detail') {
                setDetailModalVisible(true);
                setCurrentRow(record);
              }
              else if( key === 'log') {
                history.push(`/monitor/job-log/index/${record.jobId}`);
              }
            }
          }}
        >
          <a className="ant-dropdown-link" onClick={(e) => e.preventDefault()}>
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
      <div style={{ width: '100%', float: 'right' }}>
        <ProTable<API.Monitor.Job>
          headerTitle={intl.formatMessage({
            id: 'pages.searchTable.title',
            defaultMessage: '信息',
          })}
          actionRef={actionRef}
          formRef={formTableRef}
          rowKey="jobId"
          key="jobList"
          search={{
            labelWidth: 120,
          }}
          toolBarRender={() => [
            <Button
              type="primary"
              key="add"
              hidden={!access.hasPerms('monitor:job:add')}
              onClick={async () => {
                setCurrentRow(undefined);
                setModalVisible(true);
              }}
            >
              <PlusOutlined /> <FormattedMessage id="pages.searchTable.new" defaultMessage="新建" />
            </Button>,
            <Button
              type="primary"
              key="remove"
              hidden={selectedRows?.length === 0 || !access.hasPerms('monitor:job:remove')}
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
              hidden={!access.hasPerms('monitor:job:export')}
              onClick={async () => {
                handleExport();
              }}
            >
              <PlusOutlined />
              <FormattedMessage id="pages.searchTable.export" defaultMessage="导出" />
            </Button>,
          ]}
          request={(params) =>
            getJobList({ ...params } as API.Monitor.JobListParams).then((res) => {
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
            hidden={!access.hasPerms('monitor:job:del')}
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
          if (values.jobId) {
            success = await handleUpdate({ ...values } as API.Monitor.Job);
          } else {
            success = await handleAdd({ ...values } as API.Monitor.Job);
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
        jobGroupOptions={jobGroupOptions||{}}
        statusOptions={statusOptions}
      />
      <DetailForm
        onCancel={() => {
          setDetailModalVisible(false);
          setCurrentRow(undefined);
        }}
        open={detailModalVisible}
        values={currentRow || {}}
        statusOptions={statusOptions}
      />
    </PageContainer>
  );
};

export default JobTableList;
