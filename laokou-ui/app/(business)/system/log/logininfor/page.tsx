"use client";

import { fetchApi, fetchFile } from "@/app/_modules/func";
import {
  ClearOutlined,
  DeleteOutlined,
  EyeOutlined,
  ImportOutlined,
  ReloadOutlined,
  ExclamationCircleFilled,
  UnlockOutlined,
} from "@ant-design/icons";
import type {
  ProColumns,
  ProFormInstance,
  ActionType,
} from "@ant-design/pro-components";
import {
  PageContainer,
  ProDescriptions,
  ProTable,
} from "@ant-design/pro-components";
import { Button, Modal, Space, Tag, message } from "antd";
import { useRouter } from "next/navigation";
import {
  faCheck,
  faToggleOff,
  faToggleOn,
  faXmark,
  faDownload,
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

import { useRef, useState } from "react";

export default function OperLog() {
  const { push } = useRouter();

  //表格列定义
  const columns: ProColumns[] = [
    {
      title: "访问编号",
      dataIndex: "infoId",
      search: false,
    },
    {
      title: "用户名称",
      fieldProps: {
        placeholder: "请输入用户名称",
      },
      dataIndex: "userName",
      order: 3,
      sorter: true,
    },
    {
      title: "登录地址",
      fieldProps: {
        placeholder: "请输入登录地址",
      },
      dataIndex: "ipaddr",
      order: 4,
    },
    {
      title: "登录地点",
      dataIndex: "loginLocation",
      search: false,
    },

    {
      title: "浏览器",
      dataIndex: "browser",
      search: false,
    },
    {
      title: "操作系统",
      dataIndex: "os",
      search: false,
    },
    {
      title: "登录状态",
      fieldProps: {
        placeholder: "请选择登录状态",
      },
      dataIndex: "status",
      valueType: "select",
      order: 2,
      render: (_, record) => {
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
              {_}
            </Tag>
          </Space>
        );
      },
      valueEnum: {
        0: {
          text: "成功",
          status: "0",
        },
        1: {
          text: "失败",
          status: "1",
        },
      },
    },
    {
      title: "操作信息",
      dataIndex: "msg",
      search: false,
    },
    {
      title: "登录时间",
      dataIndex: "loginTime",
      valueType: "dateTime",
      search: false,
      sorter: true,
    },
    {
      title: "操作时间",
      fieldProps: {
        placeholder: ["开始日期", "结束日期"],
      },
      dataIndex: "loginTimeRange",
      valueType: "dateRange",
      hideInTable: true,
      order: 1,
      search: {
        transform: (value) => {
          return {
            "params[beginTime]": `${value[0]} 00:00:00`,
            "params[endTime]": `${value[1]} 23:59:59`,
          };
        },
      },
    },
  ];

  //查询日志数据
  const getLog = async (params: any, sorter: any, filter: any) => {
    const searchParams = {
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
      `/api/monitor/logininfor/list?${queryParams}`,
      push
    );
    console.log("data:", body);
    return body;
  };

  //删除按钮是否可用，选中行时才可用
  const [rowCanDelete, setRowCanDelete] = useState(false);

  //选中行操作
  const [selectedRowKeys, setSelectedRowKeys] = useState<React.Key[]>([]);
  const [selectedRow, setSelectedRow] = useState(undefined as any);

  //解锁按钮是否可用
  const [rowCanUnlock, setRowCanUnlock] = useState(false);

  const rowSelection = {
    onChange: (newSelectedRowKeys: React.Key[], selectedRows: any[]) => {
      setSelectedRowKeys(newSelectedRowKeys);
      setRowCanDelete(newSelectedRowKeys && newSelectedRowKeys.length > 0);

      if (newSelectedRowKeys && newSelectedRowKeys.length == 1) {
        console.log("row:", selectedRows[0]);
        setSelectedRow(selectedRows[0]);
        setRowCanUnlock(true);
      } else {
        setRowCanUnlock(false);
        setSelectedRow(undefined);
      }
    },
  };

  //点击删除按钮
  const onClickDeleteRow = () => {
    Modal.confirm({
      title: "系统提示",
      icon: <ExclamationCircleFilled />,
      content: `确定删除访问编号为“${selectedRowKeys.join(",")}”的数据项？`,
      onOk() {
        executeDeleteRow();
      },
      onCancel() {},
    });
  };

  //点击清空按钮
  const onClickClear = () => {
    Modal.confirm({
      title: "系统提示",
      icon: <ExclamationCircleFilled />,
      content: "是否确认清空所有操作日志数据项？",
      onOk() {
        executeClear();
      },
      onCancel() {},
    });
  };

  //点击解锁按钮
  const onClickUnlock = () => {
    console.log("row:", selectedRow);
    Modal.confirm({
      title: "系统提示",
      icon: <ExclamationCircleFilled />,
      content: `确定解锁用户"${selectedRow.userName}"数据项?`,
      onOk() {
        executeUnlock(selectedRow.userName);
      },
      onCancel() {},
    });
  };

  //确定删除选中的日志数据
  const executeDeleteRow = async () => {
    const body = await fetchApi(
      `/api/monitor/logininfor/${selectedRowKeys.join(",")}`,
      push,
      {
        method: "DELETE",
      }
    );
    if (body !== undefined) {
      if (body.code == 200) {
        message.success("删除成功");

        //删除按钮变回不可点击
        setRowCanDelete(false);
        //选中行数据重置为空
        setSelectedRowKeys([]);
        //刷新列表
        if (actionRef.current) {
          actionRef.current.reload();
        }
      } else {
        message.error(body.msg);
      }
    }
  };

  //确定清空日志数据
  const executeClear = async () => {
    const body = await fetchApi("/api/monitor/logininfor/clean", push, {
      method: "DELETE",
    });

    if (body !== undefined) {
      if (body.code == 200) {
        message.success("清空成功");
        //选中行数据重置为空
        setSelectedRowKeys([]);
        //刷新列表
        if (actionRef.current) {
          actionRef.current.reload();
        }
      } else {
        message.error(body.msg);
      }
    }
  };

  //确定解锁用户
  const executeUnlock = async (userName: string) => {
    const body = await fetchApi(
      `/api/monitor/logininfor/unlock/${userName}`,
      push
    );

    if (body !== undefined) {
      if (body.code == 200) {
        message.success("解锁成功");
        //刷新列表
        if (actionRef.current) {
          actionRef.current.reload();
        }
      } else {
        message.error(body.msg);
      }
    }
  };

  //搜索栏显示状态
  const [showSearch, setShowSearch] = useState(true);
  //action对象引用
  const actionRef = useRef<ActionType>();
  //表单对象引用
  const formRef = useRef<ProFormInstance>();

  //当前页数和每页条数
  const [page, setPage] = useState(1);
  const defaultPageSize = 10;
  const [pageSize, setPageSize] = useState(defaultPageSize);

  const pageChange = (page: number, pageSize: number) => {
    setPage(page);
    setPageSize(pageSize);
  };

  //导出日志文件
  const exportTable = async () => {
    if (formRef.current) {
      const formData = new FormData();

      const data = {
        pageNum: page,
        pageSize: pageSize,
        ...formRef.current.getFieldsValue(),
      };

      Object.keys(data).forEach((key) => {
        if (data[key] !== undefined) {
          formData.append(key, data[key]);
        }
      });

      await fetchFile(
        "/api/monitor/logininfor/export",
        push,
        {
          method: "POST",
          body: formData,
        },
        `logininfor_${new Date().getTime()}.xlsx`
      );
    }
  };

  return (
    <PageContainer title={false}>
      <ProTable
        formRef={formRef}
        rowKey="infoId"
        rowSelection={{
          selectedRowKeys,
          ...rowSelection,
        }}
        columns={columns}
        request={async (params: any, sorter: any, filter: any) => {
          // 表单搜索项会从 params 传入，传递给后端接口。
          const data = await getLog(params, sorter, filter);
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
          onChange: pageChange,
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
            <Button
              key="danger"
              danger
              icon={<DeleteOutlined />}
              disabled={!rowCanDelete}
              onClick={onClickDeleteRow}
            >
              删除
            </Button>,
            <Button
              key="clear"
              danger
              icon={<ClearOutlined />}
              onClick={onClickClear}
            >
              清空
            </Button>,
            <Button
              key="unlock"
              icon={<UnlockOutlined />}
              disabled={!rowCanUnlock}
              onClick={onClickUnlock}
            >
              解锁
            </Button>,
            <Button
              key="export"
              type="primary"
              icon={<FontAwesomeIcon icon={faDownload} />}
              onClick={exportTable}
            >
              导出
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
              onClick: (key?: string | undefined) => {
                setShowSearch(!showSearch);
              },
            },
            {
              key: "refresh",
              tooltip: "刷新",
              icon: <ReloadOutlined />,
              onClick: (key?: string | undefined) => {
                if (actionRef.current) {
                  actionRef.current.reload();
                }
              },
            },
          ],
        }}
      />
    </PageContainer>
  );
}
