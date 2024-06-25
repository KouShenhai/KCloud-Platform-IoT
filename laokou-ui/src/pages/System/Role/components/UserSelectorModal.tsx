import React, { useEffect, useRef, useState } from 'react';
import { Modal } from 'antd';
import { FormattedMessage, useIntl } from '@umijs/max';
import { ActionType, ParamsType, ProColumns, ProTable, RequestData } from '@ant-design/pro-components';
import { getDictValueEnum } from '@/services/system/dict';
import DictTag from '@/components/DictTag';

export type DataScopeFormProps = {
  onCancel: () => void;
  onSubmit: (values: React.Key[]) => void;
  open: boolean;
  params: ParamsType;
  request?: (params: Record<string, any>) => Promise<Partial<RequestData<API.System.User>>>;
};

const UserSelectorModal: React.FC<DataScopeFormProps> = (props) => {

  const actionRef = useRef<ActionType>();
  const [selectedRowKeys, setSelectedRowKeys] = useState<React.Key[]>([]);
  const [statusOptions, setStatusOptions] = useState<any>([]);

  useEffect(() => {
    getDictValueEnum('sys_normal_disable').then((data) => {
      setStatusOptions(data);
    });
  }, [props]);

  const intl = useIntl();
  const handleOk = () => {
    props.onSubmit(selectedRowKeys);
  };
  const handleCancel = () => {
    props.onCancel();
  };

  const columns: ProColumns<API.System.User>[] = [
    {
      title: <FormattedMessage id="system.user.user_id" defaultMessage="用户编号" />,
      dataIndex: 'userId',
      valueType: 'text',
      hideInSearch: true,
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
      hideInSearch: true,
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
      hideInSearch: true,
      valueEnum: statusOptions,
      render: (_, record) => {
        return (<DictTag enums={statusOptions} value={record.status} />);
      },
    },
    {
      title: <FormattedMessage id="system.user.create_time" defaultMessage="创建时间" />,
      dataIndex: 'createTime',
      valueType: 'dateRange',
      hideInSearch: true,
      render: (_, record) => {
        return (<span>{record.createTime.toString()} </span>);
      },
    }
  ];

  return (
    <Modal
      width={800}
      title={intl.formatMessage({
        id: 'system.role.auth.user',
        defaultMessage: '选择用户',
      })}
      open={props.open}
      destroyOnClose
      onOk={handleOk}
      onCancel={handleCancel}
    >
      <ProTable<API.System.User>
        headerTitle={intl.formatMessage({
          id: 'pages.searchTable.title',
          defaultMessage: '信息',
        })}
        actionRef={actionRef}
        rowKey="userId"
        key="userList"
        search={{
          labelWidth: 120,
        }}
        toolbar={{}}
        params={props.params}
        request={props.request}
        columns={columns}
        rowSelection={{
          onChange: (selectedRowKeys: React.Key[]) => {
            setSelectedRowKeys(selectedRowKeys);
          },
        }}
      />
    </Modal>
  );
};

export default UserSelectorModal;
