"use client";

import { fetchApi } from "@/app/_modules/func";
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
  ProFormText,
  ProFormTreeSelect,
  ProTable,
} from "@ant-design/pro-components";
import { Button, message, Modal, Space, Tag } from "antd";
import { useRouter } from "next/navigation";

import {
  faArrowsUpDown,
  faCheck,
  faPenToSquare,
  faToggleOff,
  faToggleOn,
  faXmark,
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

import { useRef, useState } from "react";

//查询表格数据API
const queryAPI = "/api/system/dept/list";
//新建数据API
const newAPI = "/api/system/dept";
//修改数据API
const modifyAPI = "/api/system/dept";
//查询详情数据API
const queryDetailAPI = "/api/system/dept";
//删除API
const deleteAPI = "/api/system/dept";

export default function Dept() {
  const { push } = useRouter();

  //表格列定义
  const columns: ProColumns[] = [
    {
      title: "部门名称",
      fieldProps: {
        placeholder: "请输入部门名称",
      },
      dataIndex: "deptName",
      order: 2,
    },
    {
      title: "排序",
      dataIndex: "orderNum",
      search: false,
    },
    {
      title: "状态",
      fieldProps: {
        placeholder: "请选择部门状态",
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
        if (record.deptId == 100) {
          return [
            <Button
              key="modifyBtn"
              type="link"
              icon={<FontAwesomeIcon icon={faPenToSquare} />}
              onClick={() => onClickShowRowModifyModal(record)}
            >
              修改
            </Button>,
            <Button
              key="newBtn"
              type="link"
              icon={<PlusOutlined />}
              onClick={() => onClickAdd(record)}
            >
              新建
            </Button>,
          ];
        } else {
          return [
            <Button
              key="modifyBtn"
              type="link"
              icon={<FontAwesomeIcon icon={faPenToSquare} />}
              onClick={() => onClickShowRowModifyModal(record)}
            >
              修改
            </Button>,
            <Button
              key="newBtn"
              type="link"
              icon={<PlusOutlined />}
              onClick={() => onClickAdd(record)}
            >
              新建
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
          ];
        }
      },
    },
  ];

  //0.查询表格数据

  //原始的可展开的所有行的 id
  const [defaultExpandKeys, setDefaultExpandKeys] = useState<any[]>([]);

  //控制行展开的数据
  const [expandKeys, setExpandKeys] = useState<any[]>([]);

  const queryTableData = async (params: any, sorter: any, filter: any) => {
    const searchParams = {
      ...params,
    };

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

    const root = getRoot(body.data);
    if (!root) {
      return body.data;
    }
    getChildren(body.data, root);
    const dataArray = [root];

    const newExpandedKeys: any[] = [];
    const render = (treeDatas: any[]) => {
      // 获取到所有可展开的父节点
      treeDatas.map((item) => {
        if (item.children) {
          newExpandedKeys.push(item.deptId);
          render(item.children);
        }
      });
      return newExpandedKeys;
    };

    const keys = render(dataArray);
    setDefaultExpandKeys(keys);
    setExpandKeys(keys);
    return dataArray;
  };

  const getRoot = (data: any[]) => {
    for (let index = 0; index < data.length; index++) {
      const item = data[index];
      if (item.parentId === 0) {
        return item;
      }
    }
  };

  const getChildren = (data: any[], parentNode: any) => {
    for (let index = 0; index < data.length; index++) {
      const item = data[index];
      if (item.parentId === parentNode.deptId) {
        parentNode.children.push(item);
        getChildren(data, item);
      }
    }

    if (parentNode.children.length == 0) {
      delete parentNode.children;
    }
  };

  //1.新建

  const [showAddModal, setShowAddModal] = useState(false);

  //新建表单是否带有父节点id
  const [rowParentId, setRowParentId] = useState(100);

  //点击新建，如果从行点击新建，给定父组织
  const onClickAdd = (record?: any) => {
    setRowParentId(record.deptId);
    setShowAddModal(true);
  };

  const cancelAddModal = () => {
    setShowAddModal(false);
    setRowParentId(100);
  };

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
        setShowAddModal(false);
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
  const onClickShowRowModifyModal = (record: any) => {
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
  const queryRowData = async (record: any) => {
    const deptId = record.deptId;

    operatRowData["deptId"] = deptId;
    operatRowData["ancestors"] = record.ancestors;

    setOperateRowData(operatRowData);

    if (deptId !== undefined) {
      const body = await fetchApi(`${queryDetailAPI}/${deptId}`, push);

      if (body !== undefined) {
        if (body.code == 200) {
          console.log("modi:", modifyFormRef);
          modifyFormRef?.current?.setFieldsValue({
            //需要加载到修改表单中的数据
            parentId: body.data.parentId,
            deptName: body.data.deptName,
            orderNum: body.data.orderNum,
            leader: body.data.leader,
            phone: body.data.phone,
            email: body.data.email,
            status: body.data.status,
          });
        }
      }
    }
  };

  //确认修改数据
  const executeModifyData = async (values: any) => {
    values["deptId"] = operatRowData["deptId"];
    values["ancestors"] = operatRowData["ancestors"];

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

  //3.展开/折叠

  //点击展开/折叠按钮
  const onClickExpandRow = () => {
    if (expandKeys.length > 0) {
      setExpandKeys([]);
    } else {
      setExpandKeys(defaultExpandKeys);
    }
  };

  //处理行的展开/折叠逻辑
  const handleExpand = (expanded: boolean, record: any) => {
    let keys = [...expandKeys];

    if (expanded) {
      keys.push(record.deptId);
    } else {
      keys = keys.filter((key: number) => key !== record.deptId);
    }

    setExpandKeys(keys);
  };

  //4.导出

  //5.选择行

  //搜索栏显示状态
  const [showSearch, setShowSearch] = useState(true);
  //action对象引用
  const actionTableRef = useRef<ActionType>();
  //搜索表单对象引用
  const searchTableFormRef = useRef<ProFormInstance>();

  const getDeptList = async () => {
    const body = await fetchApi(queryAPI, push);
    if (body !== undefined) {
      const root = getRoot(body.data);
      if (!root) {
        return body.data;
      }
      getChildren(body.data, root);
      return [root];
    }
  };

  //点击删除按钮
  const onClickDeleteRow = (record: any) => {
    Modal.confirm({
      title: "系统提示",
      icon: <ExclamationCircleFilled />,
      content: `确定删除部门名称为“${record.deptName}”的数据项？`,
      onOk() {
        executeDeleteRow(record.deptId);
      },
      onCancel() {},
    });
  };

  //确定删除选中的部门
  const executeDeleteRow = async (roleId: any) => {
    const body = await fetchApi(`${deleteAPI}/${roleId}`, push, {
      method: "DELETE",
    });
    if (body !== undefined) {
      if (body.code == 200) {
        message.success("删除成功");
        //刷新列表
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
        rowKey="deptId"
        columns={columns}
        expandable={{
          expandedRowKeys: expandKeys,
          onExpand: handleExpand,
        }}
        request={async (params: any, sorter: any, filter: any) => {
          // 表单搜索项会从 params 传入，传递给后端接口。
          const data = await queryTableData(params, sorter, filter);
          if (data !== undefined) {
            return Promise.resolve({
              data: data,
              success: true,
              total: data.length,
            });
          }
          return Promise.resolve({
            data: [],
            success: true,
          });
        }}
        pagination={false}
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
              title="添加部门"
              open={showAddModal}
              trigger={
                <Button icon={<PlusOutlined />} type="primary">
                  新建
                </Button>
              }
              autoFocusFirstInput
              modalProps={{
                destroyOnClose: true,
                onCancel: () => {
                  cancelAddModal();
                },
              }}
              submitTimeout={2000}
              onFinish={executeAddData}
            >
              <ProForm.Group>
                <ProFormTreeSelect
                  width="md"
                  name="parentId"
                  initialValue={rowParentId}
                  label="上级部门"
                  placeholder="请选择上级部门"
                  rules={[{ required: true, message: "请选择上级部门" }]}
                  request={getDeptList}
                  fieldProps={{
                    filterTreeNode: true,
                    showSearch: true,
                    treeNodeFilterProp: "label",
                    fieldNames: {
                      label: "deptName",
                      value: "deptId",
                    },
                  }}
                />
                <ProFormText
                  width="md"
                  name="deptName"
                  label="部门名称"
                  placeholder="请输入部门名称"
                  rules={[{ required: true, message: "请输入部门名称" }]}
                />
              </ProForm.Group>
              <ProForm.Group>
                <ProFormDigit
                  fieldProps={{ precision: 0 }}
                  width="md"
                  name="orderNum"
                  initialValue="0"
                  label="排序"
                  placeholder="请输入排序"
                  rules={[{ required: true, message: "请输入排序" }]}
                />
                <ProFormRadio.Group
                  name="status"
                  width="sm"
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
                <ProFormText
                  width="md"
                  name="leader"
                  label="负责人"
                  placeholder="请输入负责人"
                />
                <ProFormText
                  width="md"
                  name="phone"
                  label="联系电话"
                  placeholder="请输入联系电话"
                  rules={[
                    {
                      pattern: /^1\d{10}$/,
                      message: "请输入正确的手机号码",
                    },
                  ]}
                />
              </ProForm.Group>
              <ProForm.Group>
                <ProFormText
                  width="md"
                  name="email"
                  label="联系邮箱"
                  placeholder="请输入联系邮箱"
                  rules={[{ type: "email", message: "请输入正确的邮箱地址" }]}
                />
              </ProForm.Group>
            </ModalForm>,
            <Button
              key="expand"
              icon={<FontAwesomeIcon icon={faArrowsUpDown} />}
              onClick={() => onClickExpandRow()}
            >
              折叠/展开
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
      <ModalForm
        key="modifymodal"
        title="修改部门"
        formRef={modifyFormRef}
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
          <ProFormTreeSelect
            width="md"
            name="parentId"
            label="上级部门"
            placeholder="请选择上级部门"
            rules={[{ required: true, message: "请选择上级部门" }]}
            request={getDeptList}
            fieldProps={{
              filterTreeNode: true,
              showSearch: true,
              treeNodeFilterProp: "label",
              fieldNames: {
                label: "deptName",
                value: "deptId",
              },
            }}
          />
          <ProFormText
            width="md"
            name="deptName"
            label="部门名称"
            placeholder="请输入部门名称"
            rules={[{ required: true, message: "请输入部门名称" }]}
          />
        </ProForm.Group>
        <ProForm.Group>
          <ProFormDigit
            fieldProps={{ precision: 0 }}
            width="md"
            name="orderNum"
            initialValue="0"
            label="排序"
            placeholder="请输入排序"
            rules={[{ required: true, message: "请输入排序" }]}
          />
          <ProFormRadio.Group
            name="status"
            width="sm"
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
          <ProFormText
            width="md"
            name="leader"
            label="负责人"
            placeholder="请输入负责人"
          />
          <ProFormText
            width="md"
            name="phone"
            label="联系电话"
            placeholder="请输入联系电话"
            rules={[
              {
                pattern: /^1\d{10}$/,
                message: "请输入正确的手机号码",
              },
            ]}
          />
        </ProForm.Group>
        <ProForm.Group>
          <ProFormText
            width="md"
            name="email"
            label="联系邮箱"
            placeholder="请输入联系邮箱"
            rules={[{ type: "email", message: "请输入正确的邮箱地址" }]}
          />
        </ProForm.Group>
      </ModalForm>
      ,
    </PageContainer>
  );
}
