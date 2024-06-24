"use client";

import { fetchApi, fetchFile } from "@/app/_modules/func";
import {
  DeleteOutlined,
  ExclamationCircleFilled,
  PlusOutlined,
  ReloadOutlined,
} from "@ant-design/icons";
import type {
  ActionType,
  ProColumns,
  ProFormInstance,
} from "@ant-design/pro-components";
import {
  ModalForm,
  PageContainer,
  ProForm,
  ProFormRadio,
  ProFormText,
  ProFormTextArea,
  ProTable,
} from "@ant-design/pro-components";
import { Button, message, Modal, Space, Tag } from "antd";
import { useRouter } from "next/navigation";

import {
  faCheck,
  faDownload,
  faPenToSquare,
  faRotate,
  faToggleOff,
  faToggleOn,
  faXmark,
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

import { useRef, useState } from "react";

//查询表格数据API
const queryAPI = "/api/system/config/list";
//新建数据API
const newAPI = "/api/system/config";
//修改数据API
const modifyAPI = "/api/system/config";
//查询详情数据API
const queryDetailAPI = "/api/system/config";
//删除API
const deleteAPI = "/api/system/config";
//导出API
const exportAPI = "/api/system/config/export";
//导出文件前缀名
const exportFilePrefix = "config";
//刷新缓存
const refreshAPI = "/api/system/config/refreshCache";

export default function Config() {
  const { push } = useRouter();

  //表格列定义
  const columns: ProColumns[] = [
    {
      title: "参数编号",
      dataIndex: "configId",
      search: false,
    },
    {
      title: "参数名称",
      fieldProps: {
        placeholder: "请输入参数名称",
      },
      dataIndex: "configName",
      ellipsis: true,
      sorter: true,
      order: 4,
    },
    {
      title: "参数键名",
      fieldProps: {
        placeholder: "请输入参数键名",
      },
      dataIndex: "configKey",
      ellipsis: true,
      order: 3,
    },
    {
      title: "参数键值",
      dataIndex: "configValue",
      ellipsis: true,
      search: false,
    },
    {
      title: "系统内置",
      fieldProps: {
        placeholder: "请选择是否系统内置",
      },
      dataIndex: "configType",
      valueType: "select",
      render: (_, record) => {
        return (
          <Space>
            <Tag
              color={record.configType === "Y" ? "green" : "red"}
              icon={
                record.configType === "Y" ? (
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
        Y: {
          text: "是",
          status: "Y",
        },
        N: {
          text: "否",
          status: "N",
        },
      },
      order: 2,
    },
    {
      title: "备注",
      dataIndex: "remark",
      ellipsis: true,
      search: false,
    },
    {
      title: "创建时间",
      dataIndex: "createTime",
      valueType: "dateTime",
      search: false,
    },
    {
      title: "创建时间",
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
          key="modifyBtn"
          type="link"
          icon={<FontAwesomeIcon icon={faPenToSquare} />}
          onClick={() => onClickShowRowModifyModal(record)}
        >
          修改
        </Button>,
        <Button
          key="deleteBtn"
          type="link"
          danger
          icon={<DeleteOutlined />}
          onClick={() => onClickDeleteRow(record)}
        >
          删除
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

    const body = await fetchApi(`${queryAPI}?${queryParams}`, push);

    return body;
  };

  //1.新建

  //确定新建数据
  const executeAddData = async (values: any) => {
    const body = await fetchApi(newAPI, push, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(values),
    });

    if (body != undefined) {
      if (body.code == 200) {
        message.success(body.msg);
        if (actionTableRef.current) {
          actionTableRef.current.reload();
        }
        return true;
      }

      message.error(body.msg);
      return false;
    }
    return false;
  };

  //2.修改

  //是否展示修改对话框
  const [isShowModifyDataModal, setIsShowModifyDataModal] = useState(false);

  //展示修改对话框
  const onClickShowRowModifyModal = (record?: any) => {
    queryRowData(record);
    setIsShowModifyDataModal(true);
  };

  //修改数据表单引用
  const modifyFormRef = useRef<ProFormInstance>();

  //操作当前数据的附加数据
  const [operatRowData, setOperateRowData] = useState<{
    [key: string]: any;
  }>({});

  //查询并加载待修改数据的详细信息
  const queryRowData = async (record?: any) => {
    const configId =
      record !== undefined ? record.configId : selectedRow.configId;

    operatRowData["configId"] = configId;

    setOperateRowData(operatRowData);

    if (configId !== undefined) {
      const body = await fetchApi(`${queryDetailAPI}/${configId}`, push);

      if (body !== undefined) {
        if (body.code == 200) {
          modifyFormRef?.current?.setFieldsValue({
            //需要加载到修改表单中的数据
            configName: body.data.configName,
            configKey: body.data.configKey,
            configValue: body.data.configValue,
            configType: body.data.configType,
            remark: body.data.remark,
          });
        }
      }
    }
  };

  //确认修改数据
  const executeModifyData = async (values: any) => {
    values["configId"] = operatRowData["configId"];

    const body = await fetchApi(modifyAPI, push, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(values),
    });

    if (body !== undefined) {
      if (body.code == 200) {
        message.success(body.msg);
        //刷新列表
        if (actionTableRef.current) {
          actionTableRef.current.reload();
        }
        setIsShowModifyDataModal(false);
        return true;
      }
      message.error(body.msg);
      return false;
    }
  };

  //3.删除

  //点击删除按钮，展示删除确认框
  const onClickDeleteRow = (record?: any) => {
    const configId =
      record != undefined ? record.configId : selectedRowKeys.join(",");
    Modal.confirm({
      title: "系统提示",
      icon: <ExclamationCircleFilled />,
      content: `是否确认删除参数编号为“${configId}”的数据项？`,
      onOk() {
        executeDeleteRow(configId);
      },
      onCancel() {},
    });
  };

  //确定删除选中的数据
  const executeDeleteRow = async (dictId: any) => {
    const body = await fetchApi(`${deleteAPI}/${dictId}`, push, {
      method: "DELETE",
    });
    if (body !== undefined) {
      if (body.code == 200) {
        message.success("删除成功");

        //修改按钮变回不可点击
        setRowCanModify(false);
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

  //修改按钮是否可用，选中行时才可用
  const [rowCanModify, setRowCanModify] = useState(false);

  //删除按钮是否可用，选中行时才可用
  const [rowCanDelete, setRowCanDelete] = useState(false);

  //ProTable rowSelection
  const rowSelection = {
    onChange: (newSelectedRowKeys: React.Key[], selectedRows: any[]) => {
      setSelectedRowKeys(newSelectedRowKeys);
      setRowCanDelete(newSelectedRowKeys && newSelectedRowKeys.length > 0);

      if (newSelectedRowKeys && newSelectedRowKeys.length == 1) {
        setSelectedRow(selectedRows[0]);
        setRowCanModify(true);
      } else {
        setRowCanModify(false);
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

  //刷新缓存
  const refreshCache = async () => {
    const body = await fetchApi(refreshAPI, push, {
      method: "DELETE",
    });

    if (body !== undefined) {
      if (body.code == 200) {
        message.success("刷新成功");
        if (actionTableRef.current) {
          actionTableRef.current.reload();
        }
      } else {
        message.error(body.msg);
      }
    }
  };

  return (
    <PageContainer title={false}>
      <ProTable
        formRef={searchTableFormRef}
        rowKey="configId"
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
            <ModalForm
              key="addmodal"
              title="添加参数"
              trigger={
                <Button icon={<PlusOutlined />} type="primary">
                  新建
                </Button>
              }
              autoFocusFirstInput
              modalProps={{
                destroyOnClose: true,
              }}
              submitTimeout={2000}
              onFinish={executeAddData}
            >
              <ProForm.Group>
                <ProFormText
                  width="md"
                  name="configName"
                  label="参数名称"
                  placeholder="请输入参数名称"
                  rules={[{ required: true, message: "请输入参数名称" }]}
                />
              </ProForm.Group>
              <ProForm.Group>
                <ProFormText
                  width="md"
                  name="configKey"
                  label="参数键名"
                  placeholder="请输入参数键名"
                  rules={[{ required: true, message: "请输入参数键名" }]}
                />
                <ProFormText
                  width="md"
                  name="configValue"
                  label="参数键值"
                  placeholder="请输入参数键值"
                  rules={[{ required: true, message: "请输入参数键值" }]}
                />
              </ProForm.Group>
              <ProForm.Group>
                <ProFormRadio.Group
                  name="configType"
                  width="sm"
                  label="系统内置"
                  initialValue="Y"
                  options={[
                    {
                      label: "是",
                      value: "Y",
                    },
                    {
                      label: "否",
                      value: "N",
                    },
                  ]}
                />
              </ProForm.Group>
              <ProFormTextArea
                name="remark"
                width={688}
                label="备注"
                placeholder="请输入内容"
              />
            </ModalForm>,
            <ModalForm
              key="modifymodal"
              title="修改参数"
              formRef={modifyFormRef}
              trigger={
                <Button
                  icon={<FontAwesomeIcon icon={faPenToSquare} />}
                  disabled={!rowCanModify}
                  onClick={() => onClickShowRowModifyModal()}
                >
                  修改
                </Button>
              }
              open={isShowModifyDataModal}
              autoFocusFirstInput
              modalProps={{
                destroyOnClose: true,
                onCancel: () => {
                  setIsShowModifyDataModal(false);
                },
              }}
              submitTimeout={2000}
              onFinish={executeModifyData}
            >
              <ProForm.Group>
                <ProFormText
                  width="md"
                  name="configName"
                  label="参数名称"
                  placeholder="请输入参数名称"
                  rules={[{ required: true, message: "请输入参数名称" }]}
                />
              </ProForm.Group>
              <ProForm.Group>
                <ProFormText
                  width="md"
                  name="configKey"
                  label="参数键名"
                  placeholder="请输入参数键名"
                  rules={[{ required: true, message: "请输入参数键名" }]}
                />
                <ProFormText
                  width="md"
                  name="configValue"
                  label="参数键值"
                  placeholder="请输入参数键值"
                  rules={[{ required: true, message: "请输入参数键值" }]}
                />
              </ProForm.Group>
              <ProForm.Group>
                <ProFormRadio.Group
                  name="configType"
                  width="sm"
                  label="系统内置"
                  initialValue="Y"
                  options={[
                    {
                      label: "是",
                      value: "Y",
                    },
                    {
                      label: "否",
                      value: "N",
                    },
                  ]}
                />
              </ProForm.Group>
              <ProFormTextArea
                name="remark"
                width={688}
                label="备注"
                placeholder="请输入内容"
              />
            </ModalForm>,

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
              key="export"
              type="primary"
              icon={<FontAwesomeIcon icon={faDownload} />}
              onClick={exportTable}
            >
              导出
            </Button>,
            <Button
              key="refresh"
              type="primary"
              icon={<FontAwesomeIcon icon={faRotate} />}
              onClick={refreshCache}
            >
              刷新缓存
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
    </PageContainer>
  );
}
