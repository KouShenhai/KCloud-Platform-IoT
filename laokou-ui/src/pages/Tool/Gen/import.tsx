import { Button, Card, message, Layout } from 'antd';
import React, { useState } from 'react';
import { history, FormattedMessage } from '@umijs/max';
import { importTables, queryTableList } from './service';
import type { GenCodeType } from './data.d';
import { ProColumns, ProTable } from '@ant-design/pro-components';
import { PlusOutlined, RollbackOutlined } from '@ant-design/icons';

const { Content } = Layout;

const handleImport = async (tables: string) => {
  const hide = message.loading('正在配置');
  try {
    await importTables(tables);
    hide();
    message.success('配置成功');
    return true;
  } catch (error) {
    hide();
    message.error('配置失败请重试！');
    return false;
  }
};

const ImportTableList: React.FC = () => {
  const [selectTables, setSelectTables] = useState<string[]>([]);

  const columns: ProColumns<GenCodeType>[] = [
    {
      title: '表名称',
      dataIndex: 'tableName',
    },
    {
      title: '表描述',
      dataIndex: 'tableComment',
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      valueType: 'textarea',
      hideInSearch: true,
    },
  ];

  return (
    <Content>
      <Card bordered={false}>
        <ProTable<GenCodeType>
          headerTitle="代码生成信息"
          rowKey="tableName"
          search={{
            labelWidth: 120,
          }}
          toolBarRender={() => [
            <Button
              type="primary"
              key="primary"
              onClick={async () => {
                if (selectTables.length < 1) {
                  message.error('请选择要导入的表！');
                  return;
                }
                const success = await handleImport(selectTables.join(','));
                if (success) {
                  history.back();
                }
              }}
            >
              <PlusOutlined /> <FormattedMessage id="gen.submit" defaultMessage="提交" />
            </Button>,
            <Button
              type="primary"
              key="goback"
              onClick={() => {
                history.back();
              }}
            >
              <RollbackOutlined /> <FormattedMessage id="gen.goback" defaultMessage="返回" />
            </Button>,
          ]}
          request={(params) =>
            queryTableList({ ...params }).then((res) => {
              return {
                data: res.rows,
                total: res.total,
                success: true,
              };
            })
          }
          columns={columns}
          rowSelection={{
            onChange: (_, selectedRows) => {
              if (selectedRows && selectedRows.length > 0) {
                const tables = selectedRows.map((row) => {
                  return row.tableName;
                });
                setSelectTables(tables);
              }
            },
          }}
        />
      </Card>
    </Content>
  );
};

export default ImportTableList;
