import { DownloadOutlined, PlusOutlined } from '@ant-design/icons';
import { Button, message, Drawer, Modal, Card, Layout } from 'antd';
import type { FormInstance } from 'antd';
import React, { useState, useRef } from 'react';
import { history, FormattedMessage, useAccess } from '@umijs/max';
import PreviewForm from './components/PreviewCode';
import type { GenCodeTableListParams, GenCodeType } from './data.d';
import {
  batchGenCode,
  genCode,
  previewCode,
  getGenCodeList,
  removeData,
  syncDbInfo,
} from './service';
import {
  ActionType,
  FooterToolbar,
  ProColumns,
  ProDescriptions,
  ProDescriptionsItemProps,
  ProTable,
} from '@ant-design/pro-components';

const { Content } = Layout;

/**
 * 删除节点
 *
 * @param selectedRows
 */
const handleRemove = async (selectedRows: GenCodeType[]) => {
  const hide = message.loading('正在删除');
  if (!selectedRows) return true;
  try {
    await removeData({
      ids: selectedRows.map((row) => row.tableId),
    });
    hide();
    message.success('删除成功，即将刷新');
    return true;
  } catch (error) {
    hide();
    message.error('删除失败，请重试');
    return false;
  }
};

const handleRemoveOne = async (selectedRow: GenCodeType) => {
  const hide = message.loading('正在删除');
  if (!selectedRow) return true;
  try {
    const params = [selectedRow.tableId];
    await removeData({
      ids: params,
    });
    hide();
    message.success('删除成功，即将刷新');
    return true;
  } catch (error) {
    hide();
    message.error('删除失败，请重试');
    return false;
  }
};

const GenCodeView: React.FC = () => {
  const formTableRef = useRef<FormInstance>();

  const [showDetail, setShowDetail] = useState<boolean>(false);
  const [showPreview, setShowPreview] = useState<boolean>(false);
  const [preivewData, setPreivewData] = useState<boolean>(false);

  const actionRef = useRef<ActionType>();
  const [currentRow, setCurrentRow] = useState<GenCodeType>();
  const [selectedRows, setSelectedRows] = useState<GenCodeType[]>([]);

  const access = useAccess();

  const columns: ProColumns<GenCodeType>[] = [
    {
      title: '编号',
      dataIndex: 'tableId',
      tip: '编号',
      render: (dom, entity) => {
        return (
          <a
            onClick={() => {
              setCurrentRow(entity);
              setShowDetail(true);
            }}
          >
            {dom}
          </a>
        );
      },
    },
    {
      title: '表名',
      dataIndex: 'tableName',
      valueType: 'textarea',
    },
    {
      title: '表描述',
      dataIndex: 'tableComment',
      hideInForm: true,
      hideInSearch: true,
    },
    {
      title: '实体',
      dataIndex: 'className',
      valueType: 'textarea',
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      valueType: 'textarea',
      hideInSearch: true,
    },
    {
      title: '更新时间',
      dataIndex: 'updateTime',
      valueType: 'textarea',
      hideInSearch: true,
    },
    {
      title: '操作',
      dataIndex: 'option',
      width: '220px',
      valueType: 'option',
      render: (_, record) => [
        <Button
          type="link"
          size="small"
          key="preview"
          hidden={!access.hasPerms('tool:gen:edit')}
          onClick={() => {
            previewCode(record.tableId).then((res) => {
              if (res.code === 200) {
                setPreivewData(res.data);
                setShowPreview(true);
              } else {
                message.error('获取数据失败');
              }
            });
          }}
        >
          预览
        </Button>,
        <Button
          type="link"
          size="small"
          key="config"
          hidden={!access.hasPerms('tool:gen:edit')}
          onClick={() => {
            history.push(`/tool/gen/edit?id=${record.tableId}`);
          }}
        >
          编辑
        </Button>,
        <Button
          type="link"
          size="small"
          danger
          key="delete"
          hidden={!access.hasPerms('tool:gen:del')}
          onClick={async () => {
            Modal.confirm({
              title: '删除任务',
              content: '确定删除该任务吗？',
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
        <Button
          type="link"
          size="small"
          key="sync"
          hidden={!access.hasPerms('tool:gen:edit')}
          onClick={() => {
            syncDbInfo(record.tableName).then((res) => {
              if (res.code === 200) {
                message.success('同步成功');
              } else {
                message.error('同步失败');
              }
            });
          }}
        >
          同步
        </Button>,
        <Button
          type="link"
          size="small"
          key="gencode"
          hidden={!access.hasPerms('tool:gen:edit')}
          onClick={() => {
            if (record.genType === '1') {
              genCode(record.tableName).then((res) => {
                if (res.code === 200) {
                  message.success(`成功生成到自定义路径：${record.genPath}`);
                } else {
                  message.error(res.msg);
                }
              });
            } else {
              batchGenCode(record.tableName);
            }
          }}
        >
          生成代码
        </Button>,
      ],
    },
  ];

  return (
    <Content>
      <Card bordered={false}>
        <ProTable<GenCodeType>
          headerTitle="代码生成信息"
          actionRef={actionRef}
          formRef={formTableRef}
          rowKey="tableId"
          search={{
            labelWidth: 120,
          }}
          toolBarRender={() => [
            <Button
              type="primary"
              key="gen"
              hidden={!access.hasPerms('tool:gen:edit')}
              onClick={() => {
                if (selectedRows.length === 0) {
                  message.error('请选择要生成的数据');
                  return;
                }
                const tableNames = selectedRows.map((row) => row.tableName);
                if (selectedRows[0].genType === '1') {
                  genCode(tableNames.join(',')).then((res) => {
                    if (res.code === 200) {
                      message.success(`成功生成到自定义路径：${selectedRows[0].genPath}`);
                    } else {
                      message.error(res.msg);
                    }
                  });
                } else {
                  batchGenCode(tableNames.join(','));
                }
              }}
            >
              <DownloadOutlined /> <FormattedMessage id="gen.gencode" defaultMessage="生成" />
            </Button>,
            <Button
              type="primary"
              key="import"
              hidden={!access.hasPerms('tool:gen:add')}
              onClick={() => {
                history.push('/tool/gen/import');
              }}
            >
              <PlusOutlined /> <FormattedMessage id="gen.import" defaultMessage="导入" />
            </Button>,
          ]}
          request={(params) =>
            getGenCodeList({ ...params } as GenCodeTableListParams).then((res) => {
              return {
                data: res.rows,
                total: res.rows.length,
                success: true,
              };
            })
          }
          columns={columns}
          rowSelection={{
            onChange: (_, selectedRows) => {
              setSelectedRows(selectedRows);
            },
          }}
        />
        {selectedRows?.length > 0 && (
          <FooterToolbar
            extra={
              <div>
                <FormattedMessage id="pages.searchTable.chosen" defaultMessage="已选择" />{' '}
                <a style={{ fontWeight: 600 }}>{selectedRows.length}</a>{' '}
                <FormattedMessage id="pages.searchTable.item" defaultMessage="项" />
              </div>
            }
          >
            <Button
              key="delete"
              hidden={!access.hasPerms('tool:gen:remove')}
              onClick={async () => {
                Modal.confirm({
                  title: '删除任务',
                  content: '确定删除该任务吗？',
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
        <PreviewForm
          open={showPreview}
          data={preivewData}
          onHide={() => {
            setShowPreview(false);
          }}
        />
        <Drawer
          width={600}
          open={showDetail}
          onClose={() => {
            setCurrentRow(undefined);
            setShowDetail(false);
          }}
          closable={false}
        >
          {currentRow?.tableName && (
            <ProDescriptions<GenCodeType>
              column={2}
              title={currentRow?.tableName}
              request={async () => ({
                data: currentRow || {},
              })}
              params={{
                id: currentRow?.tableName,
              }}
              columns={columns as ProDescriptionsItemProps<GenCodeType>[]}
            />
          )}
        </Drawer>
      </Card>
    </Content>
  );
};

export default GenCodeView;
