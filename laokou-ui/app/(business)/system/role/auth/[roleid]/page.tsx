"use client";

import { fetchApi, fetchFile } from "@/app/_modules/func";
import {
  CaretDownOutlined,
  CheckOutlined,
  CloseOutlined,
  DeleteOutlined,
  ExclamationCircleFilled,
  EyeOutlined,
  PlusOutlined,
  ReloadOutlined,
  SearchOutlined,
  KeyOutlined,
  LoadingOutlined,
  CloudUploadOutlined,
  FileAddOutlined,
} from "@ant-design/icons";
import type {
  ProColumns,
  ProFormInstance,
  ActionType,
} from "@ant-design/pro-components";
import {
  ModalForm,
  PageContainer,
  ProCard,
  ProForm,
  ProFormRadio,
  ProFormSelect,
  ProFormText,
  ProFormTextArea,
  ProFormTreeSelect,
  ProTable,
} from "@ant-design/pro-components";

import type { TreeDataNode, MenuProps, UploadProps, GetProp } from "antd";
import {
  Button,
  Col,
  Flex,
  Input,
  message,
  Modal,
  Row,
  Space,
  Spin,
  Switch,
  Tree,
  Dropdown,
  Form,
  Upload,
  Typography,
  Checkbox,
  Tag,
} from "antd";
import { useRouter } from "next/navigation";

import {
  faDownload,
  faPenToSquare,
  faToggleOff,
  faToggleOn,
  faUpload,
  faUsers,
  faCheck,
  faXmark,
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

import { useEffect, useMemo, useRef, useState } from "react";

type FileType = Parameters<GetProp<UploadProps, "beforeUpload">>[0];

const { Dragger } = Upload;

export type OptionType = {
  label: string;
  value: string | number;
};

export default function RoleAuth({ params }: { params: { roleid: string } }) {
  const { push } = useRouter();

  const roleId = params.roleid;

  //表格列定义
  const columns: ProColumns[] = [
    {
      title: "用户名称",
      dataIndex: "userName",
      order: 2,
    },
    {
      title: "用户昵称",
      dataIndex: "nickName",
      search: false,
    },

    {
      title: "邮箱",
      dataIndex: "email",
      search: false,
    },
    {
      title: "手机号",
      dataIndex: "phonenumber",
      order: 1,
    },
    {
      title: "状态",
      dataIndex: "status",
      search: false,
      valueEnum: {
        0: {
          text: "正常",
          status: "0",
        },
        1: {
          text: "停用",
          status: "1",
        },
      },
      render: (text, record) => {
        return (
          <Space>
            <Tag
              color={record.status == 0 ? "green" : "red"}
              icon={
                record.status == 0 ? (
                  <FontAwesomeIcon icon={faCheck} />
                ) : (
                  <FontAwesomeIcon icon={faXmark} />
                )
              }
            >
              {text}
            </Tag>
          </Space>
        );
      },
    },
    {
      title: "创建时间",
      dataIndex: "createTime",
      valueType: "dateTime",
      search: false,
    },
    {
      title: "操作",
      key: "option",
      search: false,
      render: (_, record) => {
        if (record.userId != 1)
          return [
            <Button
              key="deleteBtn"
              type="link"
              danger
              icon={<DeleteOutlined />}
              onClick={() => onClickRemoveAuth(record)}
            >
              取消授权
            </Button>,
          ];
      },
    },
  ];

  //未分配授权用户列定义
  const unAllocateColumns: ProColumns[] = [
    {
      title: "用户名称",
      dataIndex: "userName",
      order: 2,
    },
    {
      title: "用户昵称",
      dataIndex: "nickName",
      search: false,
    },

    {
      title: "邮箱",
      dataIndex: "email",
      search: false,
    },
    {
      title: "手机号",
      dataIndex: "phonenumber",
      order: 1,
    },
    {
      title: "状态",
      dataIndex: "status",
      search: false,
      valueEnum: {
        0: {
          text: "正常",
          status: "0",
        },
        1: {
          text: "停用",
          status: "1",
        },
      },
      render: (text, record) => {
        return (
          <Space>
            <Tag
              color={record.status == 0 ? "green" : "red"}
              icon={
                record.status == 0 ? (
                  <FontAwesomeIcon icon={faCheck} />
                ) : (
                  <FontAwesomeIcon icon={faXmark} />
                )
              }
            >
              {text}
            </Tag>
          </Space>
        );
      },
    },
    {
      title: "创建时间",
      dataIndex: "createTime",
      valueType: "dateTime",
      search: false,
    },
  ];

  //查询角色授权数据
  const getRoleAllocate = async (params: any, sorter: any, filter: any) => {
    const searchParams = {
      roleId: roleId,
      pageNum: params.current,
      ...params,
    };

    delete searchParams.current;

    const queryParams = new URLSearchParams(searchParams);

    Object.keys(sorter).forEach((key) => {
      queryParams.append("orderByColumn", key);
      if (sorter[key] === "ascend") {
        queryParams.append("isAsc", "ascending");
      } else {
        queryParams.append("isAsc", "descending");
      }
    });

    const body = await fetchApi(
      `/api/system/role/authUser/allocatedList?${queryParams}`,
      push
    );

    if (body !== undefined) {
      return body;
    }
  };

  //查询角色未授权数据
  const getRoleUnallocate = async (params: any, sorter: any, filter: any) => {
    const searchParams = {
      roleId: roleId,
      pageNum: params.current,
      ...params,
    };

    delete searchParams.current;

    const queryParams = new URLSearchParams(searchParams);

    Object.keys(sorter).forEach((key) => {
      queryParams.append("orderByColumn", key);
      if (sorter[key] === "ascend") {
        queryParams.append("isAsc", "ascending");
      } else {
        queryParams.append("isAsc", "descending");
      }
    });

    const body = await fetchApi(
      `/api/system/role/authUser/unallocatedList?${queryParams}`,
      push
    );

    if (body !== undefined) {
      return body;
    }
  };

  //取消授权按钮是否可用，选中行时才可用
  const [rowCanRemoveAuth, setCanRemoveAuth] = useState(false);

  //点击批量取消授权按钮
  const onClickBatchRemoveAuth = () => {
    Modal.confirm({
      title: "系统提示",
      icon: <ExclamationCircleFilled />,
      content: `确定要取消选中用户的角色授权吗？`,
      onOk() {
        executeBatchRemoveRoleAuth();
      },
      onCancel() {},
    });
  };

  //点击取消授权按钮
  const onClickRemoveAuth = (record: any) => {
    const userId = record.userId;

    Modal.confirm({
      title: "系统提示",
      icon: <ExclamationCircleFilled />,
      content: `确定要取消用户“${record.userName}”的角色授权吗？`,
      onOk() {
        executeRemoveRoleAuth(userId);
      },
      onCancel() {},
    });
  };

  //执行批量取消用户角色授权
  const executeBatchRemoveRoleAuth = async () => {
    const data = {
      roleId: roleId,
      userIds: selectedRowKeys.join(","),
    };

    const body = await fetchApi(
      `/api/system/role/authUser/cancelAll?${new URLSearchParams(data)}`,
      push,
      {
        method: "PUT",
      }
    );

    if (body !== undefined) {
      if (body.code == 200) {
        message.success("批量取消授权成功");
      } else {
        message.error(body.msg);
      }

      setSelectedRowKeys([]);
      //刷新表格
      if (actionRef.current) {
        actionRef.current.reload();
      }
    }
  };

  //执行取消用户角色授权
  const executeRemoveRoleAuth = async (userId: any) => {
    const data = {
      roleId: roleId,
      userId: userId,
    };

    const body = await fetchApi("/api/system/role/authUser/cancel", push, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(data),
    });

    if (body !== undefined) {
      if (body.code == 200) {
        message.success("取消授权成功");
      } else {
        message.error(body.msg);
      }

      //刷新表格
      if (actionRef.current) {
        actionRef.current.reload();
      }
    }
  };

  //选中行操作
  const [selectedRowKeys, setSelectedRowKeys] = useState<React.Key[]>([]);

  const rowSelection = {
    onChange: (newSelectedRowKeys: React.Key[]) => {
      setSelectedRowKeys(newSelectedRowKeys);
      setCanRemoveAuth(newSelectedRowKeys && newSelectedRowKeys.length > 0);
    },
  };

  //未授权用户选中行操作
  const [selectedRowKeysUnallocate, setSelectedRowKeysUnallocate] = useState<
    React.Key[]
  >([]);

  const rowSelectionUnallocate = {
    onChange: (newSelectedRowKeys: React.Key[]) => {
      setSelectedRowKeysUnallocate(newSelectedRowKeys);
    },
  };

  //是否展示分配用户对话框
  const [showUnallocateModal, setShowUnallocateModal] = useState(false);

  //展示分配用户对话框
  const onClickShowModal = () => {
    if (unallocateActionRef.current) {
      unallocateActionRef.current.reload();
    }

    setShowUnallocateModal(true);
  };

  //确认分配新的用户
  const confirmAddUnallocate = async () => {
    const data = {
      roleId: roleId,
      userIds: selectedRowKeysUnallocate.join(","),
    };

    const body = await fetchApi(
      `/api/system/role/authUser/selectAll?${new URLSearchParams(data)}`,
      push,
      {
        method: "PUT",
      }
    );

    if (body !== undefined) {
      if (body.code == 200) {
        message.success(body.msg);
      } else {
        message.error(body.msg);
      }
    }

    setSelectedRowKeysUnallocate([]);

    if (unallocateActionRef.current) {
      unallocateActionRef.current.reload();
    }

    console.log(selectedRowKeysUnallocate);

    if (actionRef.current) {
      actionRef.current.reload();
    }

    setShowUnallocateModal(false);
  };

  //取消分配用户
  const cancelAddUnallocate = () => {
    setShowUnallocateModal(false);
  };

  //搜索栏显示状态
  const [showSearch, setShowSearch] = useState(true);
  //action对象引用
  const actionRef = useRef<ActionType>();
  //表单对象引用
  const formRef = useRef<ProFormInstance>();

  //未分配用户列表action对象引用
  const unallocateActionRef = useRef<ActionType>();

  //当前默认条数
  const defaultPageSize = 10;

  return (
    <PageContainer
      header={{
        title: "分配用户",
        onBack(e) {
          push("/system/role");
        },
      }}
    >
      <ProTable
        formRef={formRef}
        rowKey="userId"
        rowSelection={{
          selectedRowKeys,
          ...rowSelection,
        }}
        columns={columns}
        request={async (params: any, sorter: any, filter: any) => {
          // 表单搜索项会从 params 传入，传递给后端接口。
          const data = await getRoleAllocate(params, sorter, filter);
          if (data !== undefined) {
            return Promise.resolve({
              data: data.rows,
              success: true,
              total: data.total,
            });
          }
          return Promise.resolve({
            data: [],
            success: true,
          });
        }}
        pagination={{
          defaultPageSize: defaultPageSize,
          showQuickJumper: true,
          showSizeChanger: true,
        }}
        search={
          showSearch
            ? {
                defaultCollapsed: false,
                searchText: "搜索",
              }
            : false
        }
        dateFormatter="string"
        actionRef={actionRef}
        toolbar={{
          actions: [
            <Button icon={<PlusOutlined />} key="allocate" type="primary" onClick={onClickShowModal}>
              添加用户
            </Button>,

            <Button
              key="unallocate"
              danger
              icon={<DeleteOutlined />}
              disabled={!rowCanRemoveAuth}
              onClick={() => onClickBatchRemoveAuth()}
            >
              批量取消授权
            </Button>,
          ],
          settings: [
            {
              key: "switch",
              icon: showSearch ? (
                <FontAwesomeIcon icon={faToggleOn} />
              ) : (
                <FontAwesomeIcon icon={faToggleOff} />
              ),
              tooltip: showSearch ? "隐藏搜索栏" : "显示搜索栏",
              onClick: (key: string | undefined) => {
                setShowSearch(!showSearch);
              },
            },
            {
              key: "refresh",
              tooltip: "刷新",
              icon: <ReloadOutlined />,
              onClick: (key: string | undefined) => {
                if (actionRef.current) {
                  actionRef.current.reload();
                }
              },
            },
          ],
        }}
      />
      <Modal
        title={`选择用户`}
        width={1000}
        open={showUnallocateModal}
        onOk={confirmAddUnallocate}
        onCancel={cancelAddUnallocate}
      >
        <ProTable
          rowKey="userId"
          rowSelection={{
            selectedRowKeys: selectedRowKeysUnallocate,
            ...rowSelectionUnallocate,
          }}
          columns={unAllocateColumns}
          request={async (params: any, sorter: any, filter: any) => {
            // 表单搜索项会从 params 传入，传递给后端接口。
            const data = await getRoleUnallocate(params, sorter, filter);
            if (data !== undefined) {
              return Promise.resolve({
                data: data.rows,
                success: true,
                total: data.total,
              });
            }
            return Promise.resolve({
              data: [],
              success: true,
            });
          }}
          pagination={{
            defaultPageSize: defaultPageSize,
            showQuickJumper: true,
            showSizeChanger: true,
          }}
          search={
            showSearch
              ? {
                  defaultCollapsed: false,
                  searchText: "搜索",
                }
              : false
          }
          dateFormatter="string"
          actionRef={unallocateActionRef}
          toolbar={{
            actions: [],
            settings: [],
          }}
        />
      </Modal>
    </PageContainer>
  );
}
