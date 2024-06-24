
import React, { useState, useRef, useEffect } from 'react';
import { useIntl, FormattedMessage, useAccess, history, useParams } from '@umijs/max';
import type { FormInstance } from 'antd';
import { Button, message, Modal } from 'antd';
import { ActionType, FooterToolbar, PageContainer, ProColumns, ProTable } from '@ant-design/pro-components';
import { PlusOutlined, DeleteOutlined, ExclamationCircleOutlined } from '@ant-design/icons';
import { getDictDataList, removeDictData, addDictData, updateDictData, exportDictData } from '@/services/system/dictdata';
import UpdateForm from './edit';
import { getDictValueEnum, getDictType, getDictTypeOptionSelect } from '@/services/system/dict';
import DictTag from '@/components/DictTag';

/**
 * 添加节点
 *
 * @param fields
 */
const handleAdd = async (fields: API.System.DictData) => {
  const hide = message.loading('正在添加');
  try {
    const resp = await addDictData({ ...fields });
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
const handleUpdate = async (fields: API.System.DictData) => {
  const hide = message.loading('正在更新');
  try {
    const resp = await updateDictData(fields);
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
const handleRemove = async (selectedRows: API.System.DictData[]) => {
  const hide = message.loading('正在删除');
  if (!selectedRows) return true;
  try {
    const resp = await removeDictData(selectedRows.map((row) => row.dictCode).join(','));
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

const handleRemoveOne = async (selectedRow: API.System.DictData) => {
  const hide = message.loading('正在删除');
  if (!selectedRow) return true;
  try {
    const params = [selectedRow.dictCode];
    const resp = await removeDictData(params.join(','));
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
 *
 */
const handleExport = async () => {
  const hide = message.loading('正在导出');
  try {
    await exportDictData();
    hide();
    message.success('导出成功');
    return true;
  } catch (error) {
    hide();
    message.error('导出失败，请重试');
    return false;
  }
};

export type DictTypeArgs = {
  id: string;
};


const DictDataTableList: React.FC = () => {

  const formTableRef = useRef<FormInstance>();

  const [dictId, setDictId] = useState<string>('');
  const [dictType, setDictType] = useState<string>('');

  const [modalVisible, setModalVisible] = useState<boolean>(false);

  const actionRef = useRef<ActionType>();
  const [currentRow, setCurrentRow] = useState<API.System.DictData>();
  const [selectedRows, setSelectedRows] = useState<API.System.DictData[]>([]);

  const [dictTypeOptions, setDictTypeOptions] = useState<any>([]);
  const [statusOptions, setStatusOptions] = useState<any>([]);

  const access = useAccess();

  /** 国际化配置 */
  const intl = useIntl();

  const params = useParams();
  if (params.id === undefined) {
    history.push('/system/dict');
  }
  const id = params.id || '0';

  useEffect(() => {
    if (dictId !== id) {
      setDictId(id);
      getDictTypeOptionSelect().then((res) => {
        if (res.code === 200) {
          const opts: any = {};
          res.data.forEach((item: any) => {
            opts[item.dictType] = item.dictName;
          });
          setDictTypeOptions(opts);
        }
      });
      getDictValueEnum('sys_normal_disable').then((data) => {
        setStatusOptions(data);
      });
      getDictType(id).then((res) => {
        if (res.code === 200) {
          setDictType(res.data.dictType);
          formTableRef.current?.setFieldsValue({
            dictType: res.data.dictType,
          });
          actionRef.current?.reloadAndRest?.();
        } else {
          message.error(res.msg);
        }
      });
    }
  }, [dictId, dictType, params]);

  const columns: ProColumns<API.System.DictData>[] = [
    {
      title: <FormattedMessage id="system.dict.data.dict_code" defaultMessage="字典编码" />,
      dataIndex: 'dictCode',
      valueType: 'text',
      hideInSearch: true,
    },
    {
      title: <FormattedMessage id="system.dict.data.dict_label" defaultMessage="字典标签" />,
      dataIndex: 'dictLabel',
      valueType: 'text',
    },
    {
      title: <FormattedMessage id="system.dict.data.dict_type" defaultMessage="字典类型" />,
      dataIndex: 'dictType',
      valueType: 'select',
      hideInTable: true,
      valueEnum: dictTypeOptions,
      search: {
        transform: (value) => {
          setDictType(value);
          return value;
        },
      },
    },
    {
      title: <FormattedMessage id="system.dict.data.dict_value" defaultMessage="字典键值" />,
      dataIndex: 'dictValue',
      valueType: 'text',
    },
    {
      title: <FormattedMessage id="system.dict.data.dict_sort" defaultMessage="字典排序" />,
      dataIndex: 'dictSort',
      valueType: 'text',
    },
    {
      title: <FormattedMessage id="system.dict.data.status" defaultMessage="状态" />,
      dataIndex: 'status',
      valueType: 'select',
      valueEnum: statusOptions,
      render: (_, record) => {
        return (<DictTag enums={statusOptions} value={record.status} />);
      },
    },
    {
      title: <FormattedMessage id="system.dict.data.remark" defaultMessage="备注" />,
      dataIndex: 'remark',
      valueType: 'textarea',
      hideInSearch: true,
    },
    {
      title: <FormattedMessage id="system.dict.data.create_time" defaultMessage="创建时间" />,
      dataIndex: 'createTime',
      valueType: 'dateRange',
      render: (_, record) => {
        return (<span>{record.createTime.toString()} </span>);
      },
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
      width: '120px',
      valueType: 'option',
      render: (_, record) => [
        <Button
          type="link"
          size="small"
          key="edit"
          hidden={!access.hasPerms('system:data:edit')}
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
          hidden={!access.hasPerms('system:data:remove')}
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
      ],
    },
  ];

  return (
    <PageContainer>
      <div style={{ width: '100%', float: 'right' }}>
        <ProTable<API.System.DictData>
          headerTitle={intl.formatMessage({
            id: 'pages.searchTable.title',
            defaultMessage: '信息',
          })}
          actionRef={actionRef}
          formRef={formTableRef}
          rowKey="dictCode"
          key="dataList"
          search={{
            labelWidth: 120,
          }}
          toolBarRender={() => [
            <Button
              type="primary"
              key="add"
              hidden={!access.hasPerms('system:data:add')}
              onClick={async () => {
                setCurrentRow({ dictType: dictType, isDefault: 'N', status: '0' } as API.System.DictData);
                setModalVisible(true);
              }}
            >
              <PlusOutlined /> <FormattedMessage id="pages.searchTable.new" defaultMessage="新建" />
            </Button>,
            <Button
              type="primary"
              key="remove"
              hidden={selectedRows?.length === 0 || !access.hasPerms('system:data:remove')}
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
              hidden={!access.hasPerms('system:data:export')}
              onClick={async () => {
                handleExport();
              }}
            >
              <PlusOutlined />
              <FormattedMessage id="pages.searchTable.export" defaultMessage="导出" />
            </Button>,
          ]}
          request={(params) =>
            getDictDataList({ ...params, dictType } as API.System.DictDataListParams).then((res) => {
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
            hidden={!access.hasPerms('system:data:del')}
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
          if (values.dictCode) {
            success = await handleUpdate({ ...values } as API.System.DictData);
          } else {
            success = await handleAdd({ ...values } as API.System.DictData);
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
        statusOptions={statusOptions}
      />
    </PageContainer>
  );
};

export default DictDataTableList;
