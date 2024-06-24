"use client";

import { fetchApi } from "@/app/_modules/func";
import {
  DeleteOutlined,
  ExclamationCircleFilled,
  ReloadOutlined,
} from "@ant-design/icons";
import type {
  ActionType,
  ProColumns,
  ProFormInstance,
} from "@ant-design/pro-components";
import { PageContainer, ProTable } from "@ant-design/pro-components";
import { Button, Modal, message } from "antd";
import { useRouter } from "next/navigation";

import { faToggleOff, faToggleOn } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

import { useRef, useState } from "react";

//查询表格数据API
const queryAPI = "/api/monitor/online/list";
//强退用户API
const logoutAPI = "/api/monitor/online";

export default function Online() {
  const { push } = useRouter();

  //表格列定义
  const columns: ProColumns[] = [
    {
      title: "序号",
      dataIndex: "index",
      valueType: "index",
    },
    {
      title: "会话编号",
      dataIndex: "tokenId",
      ellipsis: true,
      search: false,
    },
    {
      title: "用户名称",
      fieldProps: {
        placeholder: "请输入用户名称",
      },
      dataIndex: "userName",
      order: 1,
    },
    {
      title: "部门名称",
      dataIndex: "deptName",
      search: false,
    },
    {
      title: "IP地址",
      fieldProps: {
        placeholder: "请输入IP地址",
      },
      dataIndex: "ipaddr",
      order: 2,
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
      title: "登录时间",
      dataIndex: "loginTime",
      valueType: "dateTime",
      search: false,
    },
    {
      title: "操作",
      key: "option",
      search: false,
      render: (_, record) => [
        <Button
          key="deleteBtn"
          type="link"
          danger
          icon={<DeleteOutlined />}
          onClick={() => onClickLogoutRow(record)}
        >
          强退
        </Button>,
      ],
    },
  ];

  //0.查询表格数据
  const queryTableData = async (params: any, sorter: any, filter: any) => {
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

    return await fetchApi(`${queryAPI}?${queryParams}`, push);
  };

  //3.删除

  //点击删除按钮，展示删除确认框
  const onClickLogoutRow = (record?: any) => {
    const tokenId = record.tokenId;
    Modal.confirm({
      title: "系统提示",
      icon: <ExclamationCircleFilled />,
      content: `确定强退登录名称为“${record.userName}”的用户？`,
      onOk() {
        executeLogoutRow(tokenId);
      },
      onCancel() {},
    });
  };

  //确定强退选中的会话
  const executeLogoutRow = async (tokenId: any) => {
    const body = await fetchApi(`${logoutAPI}/${tokenId}`, push, {
      method: "DELETE",
    });
    if (body !== undefined) {
      if (body.code == 200) {
        message.success("强退成功");

        //刷新列表
        if (actionTableRef.current) {
          actionTableRef.current.reload();
        }
      } else {
        message.error(body.msg);
      }
    }
  };

  //搜索栏显示状态
  const [showSearch, setShowSearch] = useState(true);
  //action对象引用
  const actionTableRef = useRef<ActionType>();
  //搜索表单对象引用
  const searchTableFormRef = useRef<ProFormInstance>();
  //当前页数和每页条数
  const [page, setPage] = useState(1);
  const defaultPageSize = 10;
  const [pageSize, setPageSize] = useState(defaultPageSize);
  const pageChange = (page: number, pageSize: number) => {
    setPage(page);
    setPageSize(pageSize);
  };

  return (
    <PageContainer title={false}>
      <ProTable
        formRef={searchTableFormRef}
        rowKey="tokenId"
        columns={columns}
        request={async (params: any, sorter: any, filter: any) => {
          // 表单搜索项会从 params 传入，传递给后端接口。
          const data = await queryTableData(params, sorter, filter);
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
        actionRef={actionTableRef}
        toolbar={{
          actions: [],
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
                if (actionTableRef.current) {
                  actionTableRef.current.reload();
                }
              },
            },
          ],
        }}
      />
    </PageContainer>
  );
}
