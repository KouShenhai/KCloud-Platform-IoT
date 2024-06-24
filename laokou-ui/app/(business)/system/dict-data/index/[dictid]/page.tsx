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
  ProFormDigit,
  ProFormRadio,
  ProFormSelect,
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
  faToggleOff,
  faToggleOn,
  faXmark,
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

import { useRef, useState } from "react";

//查询类型详情
const queryTypeAPI = "/api/system/dict/type";
//查询所有类型列表
const queryTypeListAPI = "/api/system/dict/type/optionselect";
//查询表格数据API
const queryAPI = "/api/system/dict/data/list";
//新建数据API
const newAPI = "/api/system/dict/data";
//修改数据API
const modifyAPI = "/api/system/dict/data";
//查询详情数据API
const queryDetailAPI = "/api/system/dict/data";
//删除API
const deleteAPI = "/api/system/dict/data";
//导出API
const exportAPI = "/api/system/dict/data/export";
//导出文件前缀名
const exportFilePrefix = "data";

export default function DictData({ params }: { params: { dictid: string } }) {
  const { push } = useRouter();

  const [defaultType, setDefaultType] = useState("");

  //获取对应的字典类型的值
  const getTypeData = async () => {
    const resp = await fetchApi(`${queryTypeAPI}/${params.dictid}`, push);
    if (resp != undefined) {
      if (searchTableFormRef.current) {
        searchTableFormRef.current.setFieldsValue({
          dictType: resp.data.dictType,
        });
      }

      setDefaultType(resp.data.dictType);
      return resp.data.dictType;
    }

    return "";
  };

  //查询字典类型列表
  const getTypeList = async () => {
    const dataArray: Array<any> = new Array<any>();
    const resp = await fetchApi(queryTypeListAPI, push);
    if (resp != undefined) {
      resp.data.forEach((item: any) => {
        const type = {
          label: item.dictName,
          value: item.dictType,
        };
        dataArray.push(type);
      });
    }

    return dataArray;
  };

  //表格列定义
  const columns: ProColumns[] = [
    {
      title: "字典名称",
      dataIndex: "dictType",
      valueType: "select",
      fieldProps: {
        allowClear: false,
      },
      request: getTypeList,
      hideInTable: true,
      order: 3,
    },
    {
      title: "数据编码",
      dataIndex: "dictCode",
      search: false,
    },
    {
      title: "数据标签",
      fieldProps: {
        placeholder: "请输入数据标签",
      },
      dataIndex: "dictLabel",
      order: 2,
      render: (_, record) => {
        const isTag = record.listClass === "";
        let tagColor = "default";
        if (record.listClass === "") {
          return _;
        } else {
          switch (record.listClass) {
            case "default":
              tagColor = "processing";
              break;
            case "primary":
              tagColor = "processing";
              break;
            case "success":
              tagColor = "success";
              break;
            case "info":
              tagColor = "default";
              break;
            case "warning":
              tagColor = "warning";
              break;
            case "danger":
              tagColor = "error";
              break;
            default:
              tagColor = "processing";
              break;
          }
          return (
            <Space>
              <Tag color={tagColor}>{_}</Tag>
            </Space>
          );
        }
      },
    },
    {
      title: "数据键值",
      dataIndex: "dictValue",
      search: false,
    },
    {
      title: "数据排序",
      dataIndex: "dictSort",
      sorter: true,
      search: false,
    },
    {
      title: "状态",
      fieldProps: {
        placeholder: "请选择数据状态",
      },
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
          text: "正常",
          status: "0",
        },
        1: {
          text: "停用",
          status: "1",
        },
      },
      order: 1,
    },
    {
      title: "备注",
      dataIndex: "remark",
      search: false,
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

    //如果没有带上默认的字典类型，查询绑定上
    if (!("dictType" in searchParams)) {
      const defaultType = await getTypeData();
      queryParams.append("dictType", defaultType);
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
    const dictCode =
      record !== undefined ? record.dictCode : selectedRow.dictCode;

    operatRowData["dictCode"] = dictCode;

    setOperateRowData(operatRowData);

    if (dictCode !== undefined) {
      const body = await fetchApi(`${queryDetailAPI}/${dictCode}`, push);

      if (body !== undefined) {
        if (body.code == 200) {
          modifyFormRef?.current?.setFieldsValue({
            //需要加载到修改表单中的数据
            dictType: body.data.dictType,
            dictLabel: body.data.dictLabel,
            dictValue: body.data.dictValue,
            dictSort: body.data.dictSort,
            status: body.data.status,
            listClass: body.data.listClass,
            cssClass: body.data.cssClass,
            remark: body.data.remark,
          });
        }
      }
    }
  };

  //确认修改数据
  const executeModifyData = async (values: any) => {
    values["dictCode"] = operatRowData["dictCode"];

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
    const dictCode =
      record != undefined ? record.dictCode : selectedRowKeys.join(",");
    Modal.confirm({
      title: "系统提示",
      icon: <ExclamationCircleFilled />,
      content: `确定删除字典编码为“${dictCode}”的数据项？`,
      onOk() {
        executeDeleteRow(dictCode);
      },
      onCancel() {},
    });
  };

  //确定删除选中的数据
  const executeDeleteRow = async (dictCode: any) => {
    const body = await fetchApi(`${deleteAPI}/${dictCode}`, push, {
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

  return (
    <PageContainer
      header={{
        title: "字典数据",
        onBack(e) {
          push("/system/dict");
        },
      }}
    >
      <ProTable
        formRef={searchTableFormRef}
        rowKey="dictCode"
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
              title="添加字典数据"
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
                  name="dictType"
                  label="字典类型"
                  initialValue={defaultType}
                  disabled
                />
              </ProForm.Group>
              <ProForm.Group>
                <ProFormText
                  width="md"
                  name="dictLabel"
                  label="数据标签"
                  rules={[{ required: true, message: "请输入数据标签" }]}
                />
                <ProFormText
                  width="md"
                  name="dictValue"
                  label="数据键值"
                  rules={[{ required: true, message: "请输入数据键值" }]}
                />
              </ProForm.Group>
              <ProForm.Group>
                <ProFormDigit
                  fieldProps={{ precision: 0 }}
                  width="md"
                  name="dictSort"
                  initialValue="0"
                  label="数据排序"
                  placeholder="请输入数据排序"
                  rules={[{ required: true, message: "请输入数据排序" }]}
                />
                <ProFormRadio.Group
                  width="md"
                  name="status"
                  label="状态"
                  initialValue="0"
                  options={[
                    {
                      label: "正常",
                      value: "0",
                    },
                    {
                      label: "停用",
                      value: "1",
                    },
                  ]}
                />
              </ProForm.Group>
              <ProForm.Group>
                <ProFormSelect
                  width="md"
                  name="listClass"
                  label="回显样式"
                  valueEnum={{
                    default: {
                      text: "默认（default）",
                      status: "default",
                    },
                    primary: {
                      text: "主要（primary）",
                      status: "primary",
                    },
                    success: {
                      text: "成功（成功）",
                      status: "success",
                    },
                    info: {
                      text: "信息（info）",
                      status: "info",
                    },
                    warning: {
                      text: "警告（warning）",
                      status: "warning",
                    },
                    danger: {
                      text: "危险（danger）",
                      status: "danger",
                    },
                  }}
                />
                <ProFormText width="md" name="cssClass" label="样式属性" />
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
              title="修改岗位"
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
                  name="dictType"
                  label="字典类型"
                  disabled
                />
              </ProForm.Group>
              <ProForm.Group>
                <ProFormText
                  width="md"
                  name="dictLabel"
                  label="字典标签"
                  rules={[{ required: true, message: "请输入字典标签" }]}
                />
                <ProFormText
                  width="md"
                  name="dictValue"
                  label="字典键值"
                  rules={[{ required: true, message: "请输入字典键值" }]}
                />
              </ProForm.Group>
              <ProForm.Group>
                <ProFormDigit
                  fieldProps={{ precision: 0 }}
                  width="md"
                  name="dictSort"
                  initialValue="0"
                  label="显示排序"
                  placeholder="请输入显示排序"
                  rules={[{ required: true, message: "请输入显示排序" }]}
                />
                <ProFormRadio.Group
                  width="md"
                  name="status"
                  label="状态"
                  initialValue="0"
                  options={[
                    {
                      label: "正常",
                      value: "0",
                    },
                    {
                      label: "停用",
                      value: "1",
                    },
                  ]}
                />
              </ProForm.Group>
              <ProForm.Group>
                <ProFormSelect
                  width="md"
                  name="listClass"
                  label="回显样式"
                  valueEnum={{
                    default: {
                      text: "默认（default）",
                      status: "default",
                    },
                    primary: {
                      text: "主要（primary）",
                      status: "primary",
                    },
                    success: {
                      text: "成功（成功）",
                      status: "success",
                    },
                    info: {
                      text: "信息（info）",
                      status: "info",
                    },
                    warning: {
                      text: "警告（warning）",
                      status: "warning",
                    },
                    danger: {
                      text: "危险（danger）",
                      status: "danger",
                    },
                  }}
                />
                <ProFormText width="md" name="cssClass" label="样式属性" />
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
