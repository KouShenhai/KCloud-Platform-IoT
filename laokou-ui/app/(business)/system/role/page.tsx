"use client";

import { fetchApi, fetchFile } from "@/app/_modules/func";
import {
  CaretDownOutlined,
  CheckOutlined,
  CloseOutlined,
  DeleteOutlined,
  ExclamationCircleFilled,
  KeyOutlined,
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
  ProFormTextArea,
  ProFormTreeSelect,
  ProTable,
} from "@ant-design/pro-components";
import type { GetProp, UploadProps } from "antd";
import {
  Button,
  Dropdown,
  Form,
  Input,
  message,
  Modal,
  Select,
  Space,
  Switch,
  Upload,
} from "antd";
import { useRouter } from "next/navigation";

import {
  faDownload,
  faPenToSquare,
  faToggleOff,
  faToggleOn,
  faUsers,
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

import { TreeSelect } from "@/node_modules/antd/es/index";
import { useRef, useState } from "react";

type FileType = Parameters<GetProp<UploadProps, "beforeUpload">>[0];

const { Dragger } = Upload;

export type OptionType = {
  label: string;
  value: string | number;
};

export default function Role() {
  const { push } = useRouter();

  //控制行的状态值的恢复
  const [rowStatusMap, setRowStatusMap] = useState<{ [key: number]: boolean }>(
    {}
  );

  //表格列定义
  //表格列定义
  const columns: ProColumns[] = [
    {
      title: "角色编号",
      dataIndex: "roleId",
      search: false,
    },
    {
      title: "角色名称",
      fieldProps: {
        placeholder: "请输入角色名称",
      },
      dataIndex: "roleName",
      ellipsis: true,
      sorter: true,
      order: 4,
    },
    {
      title: "权限字符",
      fieldProps: {
        placeholder: "请输入权限字符",
      },
      dataIndex: "roleKey",
      ellipsis: true,
      sorter: true,
      order: 3,
    },
    {
      title: "角色排序",
      dataIndex: "roleSort",
      search: false,
      sorter: true,
    },
    {
      title: "状态",
      fieldProps: {
        placeholder: "请选择角色状态",
      },
      dataIndex: "status",
      valueType: "select",
      order: 2,
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
            <Switch
              checkedChildren={<CheckOutlined />}
              unCheckedChildren={<CloseOutlined />}
              defaultChecked={record.status === "0"}
              checked={rowStatusMap[record.roleId]}
              disabled={record.roleId == 1}
              onChange={(checked, event) => {
                showSwitchRoleStatusModal(checked, record);
              }}
            />
          </Space>
        );
      },
    },
    {
      title: "创建时间",
      dataIndex: "createTime",
      valueType: "dateTime",
      search: false,
      sorter: true,
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
      render: (_, record) => {
        if (record.roleId != 1)
          return [
            <Button
              key="modifyBtn"
              type="link"
              icon={<FontAwesomeIcon icon={faPenToSquare} />}
              onClick={() => showRowModifyModal(record)}
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
            <Dropdown
              key="moreDrop"
              menu={{
                items: [
                  {
                    key: "1",
                    label: (
                      <a
                        onClick={() => {
                          modifyRolePermission(record);
                        }}
                      >
                        数据权限
                      </a>
                    ),
                    icon: <KeyOutlined />,
                  },
                  {
                    key: "2",
                    label: (
                      <a
                        onClick={() =>
                          push(`/system/role/auth/${record.roleId}`)
                        }
                      >
                        分配用户
                      </a>
                    ),
                    icon: <FontAwesomeIcon icon={faUsers} />,
                  },
                ],
              }}
            >
              <a onClick={(e) => e.preventDefault()}>
                <Space>
                  更多
                  <CaretDownOutlined />
                </Space>
              </a>
            </Dropdown>,
          ];
      },
    },
  ];

  //是否展示修改角色对话框
  const [showModifyRoleModal, setShowModifyRoleModal] = useState(false);

  //展示修改用户对话框
  const showRowModifyModal = (record?: any) => {
    queryRoleInfo(record);
    setShowModifyRoleModal(true);
    queryRolePermissionData(record);
  };

  //是否展示修改角色权限
  const [showModifyRolePermissionModal, setShowModifyRolePermissionModal] =
    useState(false);

  //重置密码表单引用
  const [scopeFormRef] = Form.useForm();

  //打开修改角色权限范围对话框
  const modifyRolePermission = (record: any) => {
    attachRowdata["roleId"] = record.roleId;
    attachRowdata["roleName"] = record.roleName;
    setAttachRowdata(attachRowdata);
    setShowModifyRolePermissionModal(true);

    queryRoleScope(record.roleId);
    queryRoleDeptTree(record.roleId);
  };

  //查询用户相关权限范围
  const queryRoleScope = async (roleId: number) => {
    const body = await fetchApi(`/api/system/role/${roleId}`, push);
    if (body !== undefined) {
      if (body.code == 200) {
        scopeFormRef.setFieldsValue({
          roleName: body.data.roleName,
          roleKey: body.data.roleKey,
          dataScope: body.data.dataScope,
        });

        if (body.data.dataScope === "2") {
          setShowDept(true);
        }
      }
    }
  };

  //角色权限范围的部门树
  const [roleDeptTree, setRoleDeptTree] = useState([]);

  //查询角色权限范围的部门树
  const queryRoleDeptTree = async (roleId: number) => {
    const body = await fetchApi(`/api/system/role/deptTree/${roleId}`, push);
    if (body !== undefined) {
      if (body.code == 200) {
        setRoleDeptTree(body.depts);
        scopeFormRef.setFieldsValue({
          deptIds: body.checkedKeys,
        });
      }
    }
  };

  //是否展示部门列表
  const [showDept, setShowDept] = useState(false);

  //选择权限范围
  const onSelectScope = (value: number) => {
    setShowDept(value == 2);
  };

  //确认修改角色权限范围
  const confirmModifyRolePermission = () => {
    scopeFormRef.submit();
    setShowDept(false);
  };

  //取消修改角色权限范围
  const cancelModifyRolePermission = () => {
    setShowModifyRolePermissionModal(false);
    setShowDept(false);
  };

  //执行修改分配权限范围
  const executeModifyRolePermissionScope = async (values: any) => {
    setShowModifyRolePermissionModal(false);
    values["roleId"] = attachRowdata["roleId"];
    if (!values.hasOwnProperty("deptIds")) {
      values["deptIds"] = [];
    }
    console.log("depts:", values);
    const body = await fetchApi("/api/system/role/dataScope", push, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(values),
    });
    if (body != undefined) {
      if (body.code == 200) {
        message.success(`修改${attachRowdata["roleName"]}权限范围成功`);
      } else {
        message.error(body.msg);
      }
    }
    scopeFormRef.resetFields();
  };

  //查询用户数据
  const getList = async (params: any, sorter: any, filter: any) => {
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

    const body = await fetchApi(`/api/system/role/list?${queryParams}`, push);

    if (body !== undefined) {
      body.rows.forEach((row: any) => {
        setRowStatusMap({ ...rowStatusMap, [row.roleId]: row.status === "0" });
      });
    }

    return body;
  };

  //展示切换角色状态对话框
  const showSwitchRoleStatusModal = (checked: boolean, record: any) => {
    setRowStatusMap({ ...rowStatusMap, [record.roleId]: checked });

    Modal.confirm({
      title: "系统提示",
      icon: <ExclamationCircleFilled />,
      content: `确认要${checked ? "启用" : "停用"}"${record.roleName}"角色吗？`,
      onOk() {
        executeSwitchStatus(checked, record.roleId, () => {
          setRowStatusMap({ ...rowStatusMap, [record.roleId]: !checked });
        });
      },
      onCancel() {
        setRowStatusMap({ ...rowStatusMap, [record.roleId]: !checked });
      },
    });
  };

  //确认变更角色状态
  const executeSwitchStatus = async (
    checked: boolean,
    roleId: string,
    erroCallback: () => void
  ) => {
    const modifyData = {
      roleId: roleId,
      status: checked ? "0" : "1",
    };
    const body = await fetchApi(`/api/system/role/changeStatus`, push, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(modifyData),
    });

    if (body !== undefined) {
      if (body.code == 200) {
        message.success(body.msg);
      } else {
        message.error(body.msg);
        erroCallback();
      }
    }
  };

  //删除按钮是否可用，选中行时才可用
  const [rowCanDelete, setRowCanDelete] = useState(false);

  //选中行操作
  const [selectedRowKeys, setSelectedRowKeys] = useState<React.Key[]>([]);
  const [selectedRow, setSelectedRow] = useState(undefined as any);

  //修改按钮是否可用
  const [rowCanModify, setRowCanModify] = useState(false);

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
    getCheckboxProps: (record: any) => ({
      disabled: record.roleId == 1,
    }),
  };

  //确定新建角色
  const executeAddRole = async (values: any) => {
    const body = await fetchApi("/api/system/role", push, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(values),
    });

    if (body != undefined) {
      if (body.code == 200) {
        message.success(body.msg);
        if (actionRef.current) {
          actionRef.current.reload();
        }
        return true;
      }

      message.error(body.msg);
      return false;
    }
    return false;
  };

  //修改角色表单引用
  const modifyFormRef = useRef<ProFormInstance>();

  //操作角色的附加数据
  const [attachRowdata, setAttachRowdata] = useState<{ [key: string]: any }>(
    {}
  );

  //查询用户信息
  const queryRoleInfo = async (record?: any) => {
    const roleId = record !== undefined ? record.roleId : selectedRow.roleId;
    const roleName =
      record !== undefined ? record.roleName : selectedRow.roleName;

    attachRowdata["roleId"] = roleId;
    attachRowdata["roleName"] = roleName;

    setAttachRowdata(attachRowdata);

    if (roleId !== undefined) {
      const body = await fetchApi(`/api/system/role/${roleId}`, push);

      if (body !== undefined) {
        if (body.code == 200) {
          modifyFormRef?.current?.setFieldsValue({
            roleName: body.data.roleName,
            roleKey: body.data.roleKey,
            roleSort: body.data.roleSort,
            status: body.data.status,
            menuIds: body.menuIds,
            remark: body.data.remark,
          });
        }
      }
    }
  };

  //待修改角色选中的权限数据
  const [roleSelectedPermission, setRoleSelectedPermission] = useState([]);

  //修改角色框中权限树数据
  const [rolePermissionTree, setRolePermissionTree] = useState([]);

  //查询修改角色时权限树，并获取角色选中权限数据
  const queryRolePermissionData = async (record?: any) => {
    const roleId = record !== undefined ? record.roleId : selectedRow.roleId;

    const body = await fetchApi(
      `/api/system/menu/roleMenuTreeselect/${roleId}`,
      push
    );

    if (body !== undefined) {
      if (body.code == 200) {
        setRolePermissionTree(body.menus);

        //绑定角色已选择的权限
        modifyFormRef?.current?.setFieldsValue({
          menuIds: body.checkedKeys,
        });
      }
    }
  };

  //确认修改角色
  const executeModifyRole = async (values: any) => {
    values["roleId"] = attachRowdata["roleId"];

    const body = await fetchApi("/api/system/role", push, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(values),
    });

    if (body !== undefined) {
      setShowModifyRoleModal(false);
      if (body.code == 200) {
        message.success(body.msg);
        //刷新列表
        if (actionRef.current) {
          actionRef.current.reload();
        }
        return true;
      }
      message.error(body.msg);
      return false;
    }
  };

  //点击删除按钮
  const onClickDeleteRow = (record?: any) => {
    const roleId =
      record != undefined ? record.roleId : selectedRowKeys.join(",");
    Modal.confirm({
      title: "系统提示",
      icon: <ExclamationCircleFilled />,
      content: `确定删除角色编号为“${roleId}”的数据项？`,
      onOk() {
        executeDeleteRow(roleId);
      },
      onCancel() {},
    });
  };

  //确定删除选中的角色
  const executeDeleteRow = async (roleId: any) => {
    const body = await fetchApi(`/api/system/role/${roleId}`, push, {
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

  //导出用户
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
        "/api/system/role/export",
        push,
        {
          method: "POST",
          body: formData,
        },
        `role_${new Date().getTime()}.xlsx`
      );
    }
  };

  //查询所有权限树
  const getPermissionTree = async () => {
    const body = await fetchApi("/api/system/menu/treeselect", push);
    if (body !== undefined) {
      if (body.code == 200) {
        return body.data;
      }
    }

    return [];
  };

  return (
    <PageContainer title={false}>
      <ProTable
        formRef={formRef}
        rowKey="roleId"
        rowSelection={{
          selectedRowKeys,
          ...rowSelection,
        }}
        columns={columns}
        request={async (params: any, sorter: any, filter: any) => {
          // 表单搜索项会从 params 传入，传递给后端接口。
          const data = await getList(params, sorter, filter);
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
            <ModalForm
              key="addmodal"
              title="添加角色"
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
              onFinish={executeAddRole}
            >
              <ProForm.Group>
                <ProFormText
                  width="md"
                  name="roleName"
                  label="角色名称"
                  placeholder="请输入角色名称"
                  rules={[{ required: true, message: "请输入角色名称" }]}
                />
                <ProFormText
                  width="md"
                  name="roleKey"
                  label="权限字符"
                  placeholder="请输入权限字符"
                  rules={[{ required: true, message: "请输入权限字符" }]}
                />
              </ProForm.Group>
              <ProForm.Group>
                <ProFormDigit
                  fieldProps={{ precision: 0 }}
                  width="md"
                  name="roleSort"
                  initialValue="0"
                  label="角色排序"
                  placeholder="请输入角色排序"
                  rules={[{ required: true, message: "请输入角色排序" }]}
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
              <ProFormTreeSelect
                width="md"
                name="menuIds"
                label="菜单权限"
                request={async () => {
                  return getPermissionTree();
                }}
                fieldProps={{
                  placement: "topRight",
                  filterTreeNode: true,
                  showSearch: true,
                  multiple: true,
                  treeCheckable: true,
                  treeNodeFilterProp: "label",
                  fieldNames: {
                    label: "label",
                    value: "id",
                  },
                }}
              />
              <ProFormTextArea
                name="remark"
                width={688}
                label="备注"
                placeholder="请输入内容"
              />
            </ModalForm>,
            <ModalForm
              key="modifymodal"
              title="修改角色"
              formRef={modifyFormRef}
              trigger={
                <Button
                  icon={<FontAwesomeIcon icon={faPenToSquare} />}
                  disabled={!rowCanModify}
                  onClick={() => showRowModifyModal()}
                >
                  修改
                </Button>
              }
              open={showModifyRoleModal}
              autoFocusFirstInput
              modalProps={{
                destroyOnClose: true,
                onCancel: () => {
                  setShowModifyRoleModal(false);
                },
              }}
              submitTimeout={2000}
              onFinish={executeModifyRole}
            >
              <ProForm.Group>
                <ProFormText
                  width="md"
                  name="roleName"
                  label="角色名称"
                  placeholder="请输入角色名称"
                  rules={[{ required: true, message: "请输入角色名称" }]}
                />
                <ProFormText
                  width="md"
                  name="roleKey"
                  label="权限字符"
                  placeholder="请输入权限字符"
                  rules={[{ required: true, message: "请输入权限字符" }]}
                />
              </ProForm.Group>
              <ProForm.Group>
                <ProFormDigit
                  fieldProps={{ precision: 0 }}
                  width="md"
                  name="roleSort"
                  initialValue="0"
                  label="角色排序"
                  placeholder="请输入角色排序"
                  rules={[{ required: true, message: "请输入角色排序" }]}
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
              <ProFormTreeSelect
                width="md"
                name="menuIds"
                label="菜单权限"
                initialValue={roleSelectedPermission}
                request={async () => {
                  return rolePermissionTree;
                }}
                fieldProps={{
                  placement: "topRight",
                  filterTreeNode: true,
                  showSearch: true,
                  multiple: true,
                  treeCheckable: true,
                  treeNodeFilterProp: "label",
                  fieldNames: {
                    label: "label",
                    value: "id",
                  },
                }}
              />
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
                if (actionRef.current) {
                  actionRef.current.reload();
                }
              },
            },
          ],
        }}
      />

      <Modal
        title={`分配数据权限`}
        open={showModifyRolePermissionModal}
        onOk={confirmModifyRolePermission}
        onCancel={cancelModifyRolePermission}
      >
        <Form
          layout="horizontal"
          form={scopeFormRef}
          onFinish={executeModifyRolePermissionScope}
        >
          <Form.Item name="roleName" label="角色名称">
            <Input disabled />
          </Form.Item>
          <Form.Item name="roleKey" label="权限字符">
            <Input disabled />
          </Form.Item>

          <Form.Item name="dataScope" label="权限范围" initialValue="1">
            <Select
              placeholder="选择权限范围"
              onChange={onSelectScope}
              options={[
                {
                  label: "全部数据权限",
                  value: "1",
                },
                {
                  label: "自定义数据权限",
                  value: "2",
                },
                {
                  label: "本部门数据权限",
                  value: "3",
                },
                {
                  label: "本部门及以下权限",
                  value: "4",
                },
                {
                  label: "仅本人数据权限",
                  value: "5",
                },
              ]}
            ></Select>
          </Form.Item>
          {showDept && (
            <Form.Item name="deptIds" label="数据权限">
              <TreeSelect
                treeData={roleDeptTree}
                allowClear
                multiple={true}
                showCheckedStrategy={TreeSelect.SHOW_ALL}
                treeCheckable={true}
                fieldNames={{
                  label: "label",
                  value: "id",
                }}
              />
            </Form.Item>
          )}
        </Form>
      </Modal>
    </PageContainer>
  );
}
