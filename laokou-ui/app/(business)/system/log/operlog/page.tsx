"use client";

import { fetchApi, fetchFile } from "@/app/_modules/func";
import {
  ClearOutlined,
  DeleteOutlined,
  ExclamationCircleFilled,
  EyeOutlined,
  ReloadOutlined,
} from "@ant-design/icons";
import type {
  ActionType,
  ProColumns,
  ProFormInstance,
} from "@ant-design/pro-components";
import {
  PageContainer,
  ProDescriptions,
  ProTable,
} from "@ant-design/pro-components";
import { Button, message, Modal, Space, Tag } from "antd";
import { useRouter } from "next/navigation";

import {
  faCheck,
  faDownload,
  faToggleOff,
  faToggleOn,
  faXmark,
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

import { useRef, useState } from "react";

export default function OperLog() {
  const { push } = useRouter();

  //表格列定义
  const columns: ProColumns[] = [
    {
      title: "日志编号",
      dataIndex: "operId",
      search: false,
    },
    {
      title: "系统模块",
      fieldProps: {
        placeholder: "请输入系统模块",
      },
      dataIndex: "title",
      order: 9,
    },
    {
      title: "操作类型",
      fieldProps: {
        placeholder: "请选择操作类型",
      },
      dataIndex: "businessType",
      order: 7,
      valueEnum: {
        1: {
          text: "新增",
          status: "1",
        },
        2: {
          text: "修改",
          status: "2",
        },
        3: {
          text: "删除",
          status: "3",
        },
        4: {
          text: "授权",
          status: "4",
        },
        5: {
          text: "导出",
          status: "5",
        },
        6: {
          text: "导入",
          status: "6",
        },
        7: {
          text: "强退",
          status: "7",
        },
        8: {
          text: "生成代码",
          status: "8",
        },
        9: {
          text: "清空数据",
          status: "9",
        },
        0: {
          text: "其他",
          status: "0",
        },
      },
    },
    {
      title: "操作人员",
      fieldProps: {
        placeholder: "请输入操作人员",
      },
      dataIndex: "operName",
      sorter: true,
      order: 8,
    },
    {
      title: "操作地址",
      fieldProps: {
        placeholder: "请输入操作地址",
      },
      dataIndex: "operIp",
      order: 10,
    },
    {
      title: "操作地点",
      dataIndex: "operLocation",
      search: false,
    },
    {
      title: "操作状态",
      fieldProps: {
        placeholder: "请选择操作状态",
      },
      dataIndex: "status",
      valueType: "select",
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
      order: 6,
    },
    {
      title: "操作时间",
      dataIndex: "operTime",
      valueType: "dateTime",
      search: false,
      sorter: true,
    },
    {
      title: "操作时间",
      fieldProps: {
        placeholder: ["开始日期", "结束日期"],
      },
      dataIndex: "operTimeRange",
      valueType: "dateRange",
      hideInTable: true,
      order: 5,
      search: {
        transform: (value) => {
          return {
            "params[beginTime]": `${value[0]} 00:00:00`,
            "params[endTime]": `${value[1]} 23:59:59`,
          };
        },
      },
    },
    {
      title: "消耗时间",
      dataIndex: "costTime",
      sorter: true,
      search: false,
      render: (_, record) => {
        return <span>{_}毫秒</span>;
      },
    },
    {
      title: "操作",
      key: "option",
      search: false,
      render: (_, record) => [
        <Button
          key={record.operId}
          type="link"
          icon={<EyeOutlined />}
          onClick={() => showRowModal(record)}
        >
          详情
        </Button>,
      ],
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
      `/api/monitor/operlog/list?${queryParams}`,
      push
    );
    console.log("data:", body);
    return body;
  };

  //删除按钮是否可用，选中行时才可用
  const [rowCanDelete, setRowCanDelete] = useState(false);

  //选中行操作
  const [selectedRowKeys, setSelectedRowKeys] = useState<React.Key[]>([]);
  const rowSelection = {
    onChange: (newSelectedRowKeys: React.Key[]) => {
      setSelectedRowKeys(newSelectedRowKeys);
      setRowCanDelete(newSelectedRowKeys && newSelectedRowKeys.length > 0);
    },
  };

  //点击删除按钮
  const onClickDeleteRow = () => {
    Modal.confirm({
      title: "系统提示",
      icon: <ExclamationCircleFilled />,
      content: `确定删除日志编号为“${selectedRowKeys.join(",")}”的数据项？`,
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

  //确定删除选中的日志数据
  const executeDeleteRow = async () => {
    const body = await fetchApi(
      `/api/monitor/operlog/${selectedRowKeys.join(",")}`,
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
        //选中的数据恢复为空
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
    const body = await fetchApi("/api/monitor/operlog/clean", push, {
      method: "DELETE",
    });

    if (body !== undefined) {
      if (body.code == 200) {
        message.success("清空成功");
        //选中的数据恢复为空
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

  //控制是否展示行详情模态框
  const [isModalOpen, setIsModalOpen] = useState(false);

  //关闭行详情展示
  function closeRowModal() {
    setIsModalOpen(false);
  }

  const [selectedRow, setSelectedRow] = useState(undefined as any);

  //展示行详情
  function showRowModal(record: any) {
    setIsModalOpen(true);
    setSelectedRow(record);
  }

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
        "/api/monitor/operlog/export",
        push,
        {
          method: "POST",
          body: formData,
        },
        `operlog_${new Date().getTime()}.xlsx`
      );
    }
  };

  return (
    <PageContainer title={false}>
      <ProTable
        formRef={formRef}
        rowKey="operId"
        rowSelection={{
          selectedRowKeys,
          ...rowSelection,
        }}
        columns={columns}
        request={async (params: any, sorter: any, filter: any) => {
          // 表单搜索项会从 params 传入，传递给后端接口。
          console.log(params, sorter, filter);
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

      {selectedRow !== undefined && (
        <Modal
          title="操作日志详情"
          footer={<Button onClick={closeRowModal}>关闭</Button>}
          open={isModalOpen}
          onCancel={closeRowModal}
        >
          <ProDescriptions column={10}>
            <ProDescriptions.Item span={3} label="操作模块">
              {selectedRow.title} /
            </ProDescriptions.Item>
            <ProDescriptions.Item
              span={2}
              valueEnum={{
                1: {
                  text: "新增",
                  status: "1",
                },
                2: {
                  text: "修改",
                  status: "2",
                },
                3: {
                  text: "删除",
                  status: "3",
                },
                4: {
                  text: "授权",
                  status: "4",
                },
                5: {
                  text: "导出",
                  status: "5",
                },
                6: {
                  text: "导入",
                  status: "6",
                },
                7: {
                  text: "强退",
                  status: "7",
                },
                8: {
                  text: "生成代码",
                  status: "8",
                },
                9: {
                  text: "清空数据",
                  status: "9",
                },
                0: {
                  text: "其他",
                  status: "0",
                },
              }}
            >
              {selectedRow.businessType}
            </ProDescriptions.Item>
            <ProDescriptions.Item span={5} label="请求地址">
              {selectedRow.operUrl}
            </ProDescriptions.Item>
          </ProDescriptions>
          <ProDescriptions column={6}>
            <ProDescriptions.Item span={3} label="登录信息">
              {selectedRow.operName}/{selectedRow.operIp}/
              {selectedRow.operLocation}
            </ProDescriptions.Item>
            <ProDescriptions.Item span={3} label="请求方式">
              {selectedRow.requestMethod}
            </ProDescriptions.Item>

            <ProDescriptions.Item span={6} label="操作方法">
              {selectedRow.method}
            </ProDescriptions.Item>
            <ProDescriptions.Item span={6} label="请求参数">
              {selectedRow.operParam}
            </ProDescriptions.Item>
            <ProDescriptions.Item span={6} label="返回参数">
              {selectedRow.jsonResult}
            </ProDescriptions.Item>
          </ProDescriptions>

          <ProDescriptions column={8}>
            <ProDescriptions.Item
              span={2}
              label="操作状态"
              valueEnum={{
                0: {
                  text: "成功",
                  status: "0",
                },
                1: {
                  text: "失败",
                  status: "1",
                },
              }}
            >
              {selectedRow.status}
            </ProDescriptions.Item>
            <ProDescriptions.Item span={2} label="消耗时间">
              {selectedRow.costTime}
            </ProDescriptions.Item>
            <ProDescriptions.Item span={4} label="操作时间">
              {selectedRow.operTime}
            </ProDescriptions.Item>
          </ProDescriptions>
          {selectedRow.errorMsg && (
            <ProDescriptions>
              <ProDescriptions.Item label="异常信息">
                {selectedRow.errorMsg}
              </ProDescriptions.Item>
            </ProDescriptions>
          )}
        </Modal>
      )}
    </PageContainer>
  );
}
