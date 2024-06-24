"use client";

import { fetchApi, fetchFile } from "@/app/_modules/func";
import {
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

//查询Job详情
const queryJobAPI = "/api/monitor/job";
//查询表格数据API
const queryAPI = "/api/monitor/jobLog/list";
//删除API
const deleteAPI = "/api/monitor/jobLog";
//导出API
const exportAPI = "/api/monitor/jobLog/export";
//导出文件前缀名
const exportFilePrefix = "joblog";

//清空调度日志API
const clearAllAPI = "/api/monitor/jobLog/clean";

export default function JobLog({ params }: { params: { jobid: string } }) {
  const { push } = useRouter();

  //获取对应的任务的JobName的值
  const getJobName = async () => {
    const resp = await fetchApi(`${queryJobAPI}/${params.jobid}`, push);
    if (resp != undefined) {
      if (searchTableFormRef.current) {
        searchTableFormRef.current.setFieldsValue({
          jobName: resp.data.jobName,
          jobGroup: resp.data.jobGroup,
        });
      }
      return [resp.data.jobName, resp.data.jobGroup];
    }

    return "";
  };

  //表格列定义
  const columns: ProColumns[] = [
    {
      title: "日志编号",
      dataIndex: "jobLogId",
      search: false,
    },
    {
      title: "任务名称",
      fieldProps: {
        placeholder: "请输入任务名称",
      },
      dataIndex: "jobName",
      ellipsis: true,
      order: 4,
    },
    {
      title: "任务组名",
      dataIndex: "jobGroup",
      valueType: "select",
      valueEnum: {
        DEFAULT: {
          text: "默认",
          status: "DEFAULT",
        },
        SYSTEM: {
          text: "系统",
          status: "SYSTEM",
        },
      },
      order: 3,
    },
    {
      title: "调用目标字符串",
      dataIndex: "invokeTarget",
      ellipsis: true,
      search: false,
    },
    {
      title: "日志信息",
      dataIndex: "jobMessage",
      ellipsis: true,
      search: false,
    },
    {
      title: "执行状态",
      dataIndex: "status",
      valueType: "select",
      render: (_, record) => {
        return (
          <Space>
            <Tag
              color={record.status === "0" ? "green" : "red"}
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
      order: 2,
    },
    {
      title: "执行时间",
      dataIndex: "createTime",
      valueType: "dateTime",
      search: false,
    },
    {
      title: "执行时间",
      fieldProps: {
        placeholder: ["开始日期", "结束日期"],
      },
      dataIndex: "createTimeRange",
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
    {
      title: "操作",
      key: "option",
      search: false,
      render: (_, record) => [
        <Button
          key="detailBtn"
          type="link"
          icon={<EyeOutlined />}
          onClick={() => onClickShowRowDetailModal(record)}
        >
          详情
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

    //如果没有带上默认的字典类型，查询绑定上
    if (!("jobName" in searchParams)) {
      const result = await getJobName();
      queryParams.append("jobName", result[0]);
      queryParams.append("jobGroup", result[1]);
    }

    Object.keys(sorter).forEach((key) => {
      queryParams.append("orderByColumn", key);
      if (sorter[key] === "ascend") {
        queryParams.append("isAsc", "ascending");
      } else {
        queryParams.append("isAsc", "descending");
      }
    });

    const body = await fetchApi(`${queryAPI}?${queryParams}`, push);

    return body;
  };

  //操作当前数据的附加数据
  const [operatRowData, setOperateRowData] = useState<{
    [key: string]: any;
  }>({});

  //3.删除

  //点击删除按钮，展示删除确认框
  const onClickDeleteRow = (record?: any) => {
    const jobLogId =
      record != undefined ? record.jobLogId : selectedRowKeys.join(",");
    Modal.confirm({
      title: "系统提示",
      icon: <ExclamationCircleFilled />,
      content: `确定删除调度日志编号为“${jobLogId}”的数据项？`,
      onOk() {
        executeDeleteRow(jobLogId);
      },
      onCancel() {},
    });
  };

  //确定删除选中的数据
  const executeDeleteRow = async (jobLogId: any) => {
    const body = await fetchApi(`${deleteAPI}/${jobLogId}`, push, {
      method: "DELETE",
    });
    if (body !== undefined) {
      if (body.code == 200) {
        message.success("删除成功");

        //删除按钮变回不可点击
        setRowCanDelete(false);
        //选中行数据重置为空
        setSelectedRowKeys([]);
        //刷新列表
        if (actionTableRef.current) {
          actionTableRef.current.reload();
        }
      } else {
        message.error(body.msg);
      }
    }
  };

  //弹出清空确认框
  const onClickClearAll = () => {
    Modal.confirm({
      title: "系统提示",
      icon: <ExclamationCircleFilled />,
      content: `确定清空所有调度日志数据项？`,
      onOk() {
        executeClearAll();
      },
      onCancel() {},
    });
  };

  //执行清空调度日志
  const executeClearAll = async () => {
    const body = await fetchApi(clearAllAPI, push, {
      method: "DELETE",
    });

    if (body !== undefined) {
      if (body.code == 200) {
        message.success("清空成功");
        if (actionTableRef.current) {
          actionTableRef.current.reload();
        }
      } else {
        message.error(body.msg);
      }
    } else {
      message.error("清空发生异常");
    }
  };

  //4.导出

  //导出表格数据
  const exportTable = async () => {
    if (searchTableFormRef.current) {
      const formData = new FormData();

      const data = {
        pageNum: page,
        pageSize: pageSize,
        ...searchTableFormRef.current.getFieldsValue(),
      };

      Object.keys(data).forEach((key) => {
        if (data[key] !== undefined) {
          formData.append(key, data[key]);
        }
      });

      await fetchFile(
        exportAPI,
        push,
        {
          method: "POST",
          body: formData,
        },
        `${exportFilePrefix}_${new Date().getTime()}.xlsx`
      );
    }
  };

  //5.选择行

  //选中行操作
  const [selectedRowKeys, setSelectedRowKeys] = useState<React.Key[]>([]);
  const [selectedRow, setSelectedRow] = useState(undefined as any);

  //删除按钮是否可用，选中行时才可用
  const [rowCanDelete, setRowCanDelete] = useState(false);

  //ProTable rowSelection
  const rowSelection = {
    onChange: (newSelectedRowKeys: React.Key[], selectedRows: any[]) => {
      setSelectedRowKeys(newSelectedRowKeys);
      setRowCanDelete(newSelectedRowKeys && newSelectedRowKeys.length > 0);

      if (newSelectedRowKeys && newSelectedRowKeys.length == 1) {
        setSelectedRow(selectedRows[0]);
      } else {
        setSelectedRow(undefined);
      }
    },

    //复选框的额外禁用判断
    // getCheckboxProps: (record) => ({
    //   disabled: record.userId == 1,
    // }),
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

  const [isShowDetail, setIsShowDetail] = useState(false);

  //展示详情框
  const onClickShowRowDetailModal = (record: any) => {
    setIsShowDetail(true);
    setSelectedRow(record);
  };

  return (
    <PageContainer
      header={{
        title: "调度日志",
        onBack(e) {
          push("/monitor/job");
        },
      }}
    >
      <ProTable
        formRef={searchTableFormRef}
        rowKey="jobLogId"
        rowSelection={{
          selectedRowKeys,
          ...rowSelection,
        }}
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
          actions: [
            <Button
              key="danger"
              danger
              icon={<DeleteOutlined />}
              disabled={!rowCanDelete}
              onClick={() => onClickDeleteRow()}
            >
              删除
            </Button>,
            <Button
              key="danger"
              danger
              icon={<DeleteOutlined />}
              onClick={() => onClickClearAll()}
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
                if (actionTableRef.current) {
                  actionTableRef.current.reload();
                }
              },
            },
          ],
        }}
      />
      {selectedRow !== undefined && (
        <Modal
          title="调度日志详情"
          footer={<Button onClick={() => setIsShowDetail(false)}>关闭</Button>}
          open={isShowDetail}
          onCancel={() => setIsShowDetail(false)}
        >
          <ProDescriptions column={2}>
            <ProDescriptions.Item label="日志序号">
              {selectedRow.jobLogId}
            </ProDescriptions.Item>
            <ProDescriptions.Item
              label="任务分组"
              valueEnum={{
                DEFAULT: {
                  text: "默认",
                  status: "DEFAULT",
                },
                SYSTEM: {
                  text: "系统",
                  status: "SYSTEM",
                },
              }}
            >
              {selectedRow.jobGroup}
            </ProDescriptions.Item>
            <ProDescriptions.Item label="任务名称">
              {selectedRow.jobName}
            </ProDescriptions.Item>
            <ProDescriptions.Item label="执行时间">
              {selectedRow.createTime}
            </ProDescriptions.Item>
          </ProDescriptions>
          <ProDescriptions column={1}>
            <ProDescriptions.Item label="调用目标方法">
              {selectedRow.invokeTarget}
            </ProDescriptions.Item>
            <ProDescriptions column={1}>
              <ProDescriptions.Item label="日志信息">
                {selectedRow.jobMessage}
              </ProDescriptions.Item>
              <ProDescriptions.Item
                label="执行状态"
                valueEnum={{
                  0: {
                    text: "正常",
                    status: "0",
                  },
                  1: {
                    text: "暂停",
                    status: "1",
                  },
                }}
              >
                {selectedRow.status}
              </ProDescriptions.Item>
            </ProDescriptions>
          </ProDescriptions>
        </Modal>
      )}
    </PageContainer>
  );
}
