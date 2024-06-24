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
  ClockCircleOutlined,
  ScheduleOutlined,
  PlayCircleOutlined,
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
  ProDescriptions,
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

import "./styles.scss";

import { ReQuartzCron, ReUnixCron, CronLocalization } from "@sbzen/re-cron";

import { useEffect, useMemo, useRef, useState } from "react";
import { Divider } from "@/node_modules/antd/es/index";
import { MortnonCronLocalization } from "@/app/_modules/definies";

//查询表格数据API
const queryAPI = "/api/monitor/job/list";
//新建数据API
const newAPI = "/api/monitor/job";
//修改数据API
const modifyAPI = "/api/monitor/job";
//查询详情数据API
const queryDetailAPI = "/api/monitor/job";
//删除API
const deleteAPI = "/api/monitor/job";
//导出API
const exportAPI = "/api/monitor/job/export";
//导出文件前缀名
const exportFilePrefix = "job";
//变更任务状态API
const changeJobStatusAPI = "/api/monitor/job/changeStatus";
//执行任务API
const runAPI = "/api/monitor/job/run";

export default function Job() {
  const { push } = useRouter();

  //表格列定义
  const columns: ProColumns[] = [
    {
      title: "任务编号",
      dataIndex: "jobId",
      search: false,
    },
    {
      title: "任务名称",
      fieldProps: {
        placeholder: "请输入任务名称",
      },
      dataIndex: "jobName",
      ellipsis: true,
      order: 3,
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
      sorter: true,
      order: 2,
    },
    {
      title: "调用目标字符串",
      dataIndex: "invokeTarget",
      search: false,
    },
    {
      title: "Cron执行表达式",
      dataIndex: "cronExpression",
      search: false,
    },
    {
      title: "状态",
      fieldProps: {
        placeholder: "请选择任务状态",
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
              checked={rowStatusMap[record.jobId]}
              onChange={(checked, event) => {
                showSwitchJobStatusModal(checked, record);
              }}
            />
          </Space>
        );
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
        <Dropdown
          key="moreDrop"
          menu={{
            items: [
              {
                key: "1",
                label: (
                  <a
                    onClick={() => {
                      showRunOnceModal(record);
                    }}
                  >
                    执行一次
                  </a>
                ),
                icon: <PlayCircleOutlined />,
              },
              {
                key: "2",
                label: <a onClick={() => showRowModal(record)}>任务详情</a>,
                icon: <EyeOutlined />,
              },
              {
                key: "3",
                label: (
                  <a
                    onClick={() =>
                      push(`/monitor/job-log/index/${record.jobId}`)
                    }
                  >
                    调度日志
                  </a>
                ),
                icon: <ScheduleOutlined />,
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

    if (body !== undefined) {
      body.rows.forEach((row: any) => {
        setRowStatusMap({ ...rowStatusMap, [row.userId]: row.status === "0" });
      });
    }

    return body;
  };

  //1.新建

  //新建对话框表单引用
  const addFormRef = useRef<ProFormInstance>();

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

  //控制行的状态值的恢复
  const [rowStatusMap, setRowStatusMap] = useState<{ [key: number]: boolean }>(
    {}
  );

  //展示切换任务状态对话框
  const showSwitchJobStatusModal = (checked: boolean, record: any) => {
    setRowStatusMap({ ...rowStatusMap, [record.jobId]: checked });

    Modal.confirm({
      title: "系统提示",
      icon: <ExclamationCircleFilled />,
      content: `确认要${checked ? "启用" : "停用"}"${record.jobName}"任务吗？`,
      onOk() {
        executeSwitchStatus(checked, record.jobId, () => {
          setRowStatusMap({ ...rowStatusMap, [record.jobId]: !checked });
        });
      },
      onCancel() {
        setRowStatusMap({ ...rowStatusMap, [record.jobId]: !checked });
      },
    });
  };

  //确认变更任务状态
  const executeSwitchStatus = async (
    checked: boolean,
    jobId: string,
    erroCallback: () => void
  ) => {
    const modifyData = {
      jobId: jobId,
      status: checked ? "0" : "1",
    };
    const body = await fetchApi(changeJobStatusAPI, push, {
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
    const jobId = record !== undefined ? record.jobId : selectedRow.jobId;

    operatRowData["jobId"] = jobId;

    setOperateRowData(operatRowData);

    if (jobId !== undefined) {
      const body = await fetchApi(`${queryDetailAPI}/${jobId}`, push);

      if (body !== undefined) {
        if (body.code == 200) {
          modifyFormRef?.current?.setFieldsValue({
            //需要加载到修改表单中的数据
            jobName: body.data.jobName,
            jobGroup: body.data.jobGroup,
            invokeTarget: body.data.invokeTarget,
            cronExpression: body.data.cronExpression,
            status: body.data.status,
            misfirePolicy: body.data.misfirePolicy,
            concurrent: body.data.concurrent,
          });

          setCronValue(body.data.cronExpression);
        }
      }
    }
  };

  //确认修改数据
  const executeModifyData = async (values: any) => {
    values["jobId"] = operatRowData["jobId"];

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
    const jobId =
      record != undefined ? record.jobId : selectedRowKeys.join(",");
    Modal.confirm({
      title: "系统提示",
      icon: <ExclamationCircleFilled />,
      content: `确定删除任务编号为“${jobId}”的数据项？`,
      onOk() {
        executeDeleteRow(jobId);
      },
      onCancel() {},
    });
  };

  //确定删除选中的数据
  const executeDeleteRow = async (jobId: any) => {
    const body = await fetchApi(`${deleteAPI}/${jobId}`, push, {
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

  //是否展示Cron表达式生成框
  const [isCronShow, setIsCronShow] = useState(false);

  //Cron表达式值
  const [cronValue, setCronValue] = useState("");

  //当前是新建还是修改触发的Cron生成框
  const [isNew, setIsNew] = useState(true);

  //用于重置Cron生成框的key值
  const [modalKey, setModalKey] = useState(0);

  //展示Cron对话框，区分新建用还是修改用
  const showCronModal = (isNew: boolean) => {
    setIsNew(isNew);
    setIsCronShow(true);
  };

  //回写Cron数据
  const getCronData = () => {
    setIsCronShow(false);
    if (isNew) {
      addFormRef?.current?.setFieldsValue({
        cronExpression: cronValue,
      });
    } else {
      modifyFormRef?.current?.setFieldsValue({
        cronExpression: cronValue,
      });
    }
    //重置Cron数据
    setCronValue("");
    setModalKey((preKey) => preKey + 1);
  };

  //执行任务一次
  const showRunOnceModal = (record: any) => {
    Modal.confirm({
      title: "系统提示",
      icon: <ExclamationCircleFilled />,
      content: `确定要立即执行一次任务“${record.jobName}”吗？`,
      onOk() {
        executeJob(record);
      },
      onCancel() {},
    });
  };

  //执行任务
  const executeJob = async (record: any) => {
    const runData = {
      jobId: record.jobId,
      jobGroup: record.jobGroup,
    };

    const body = await fetchApi(runAPI, push, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(runData),
    });

    if (body !== undefined) {
      if (body.code == 200) {
        message.success("执行成功");
      } else {
        message.error(body.msg);
      }
    }
  };

  //是否展示任务详情框
  const [isShowDetail, setIsShowDetail] = useState(false);

  //展示行详情框
  const showRowModal = async (record: any) => {
    const jobId = record.jobId;
    if (jobId !== undefined) {
      const body = await fetchApi(`${queryDetailAPI}/${jobId}`, push);
      setSelectedRow(body.data);
    }

    setIsShowDetail(true);
  };

  return (
    <PageContainer title={false}>
      <ProTable
        formRef={searchTableFormRef}
        rowKey="jobId"
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
              formRef={addFormRef}
              key="addmodal"
              layout="horizontal"
              title="添加任务"
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
                  name="jobName"
                  label="任务名称"
                  placeholder="请输入任务名称"
                  rules={[{ required: true, message: "请输入任务名称" }]}
                />
                <ProFormSelect
                  width="md"
                  name="jobGroup"
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
                />
              </ProForm.Group>
              <ProForm.Group>
                <ProFormText
                  width="lg"
                  name="invokeTarget"
                  label="调用方法"
                  placeholder="请输入调用方法的字符串"
                  tooltip="Bean调用示例：ryTask.ryParams('ry')
                  Class调用示例：com.ruoyi.quartz.task.RyTask.ryParams('ry')
                  参数说明：支持字符串，布尔类型，长整型，浮点型，整型"
                  rules={[
                    { required: true, message: "请输入调用方法的字符串" },
                  ]}
                />
              </ProForm.Group>
              <ProForm.Group>
                <Space.Compact>
                  <ProFormText
                    width="lg"
                    name="cronExpression"
                    label="Cron表达式"
                    placeholder="请输入Cron表达式"
                    rules={[{ required: true, message: "请输入Cron表达式" }]}
                  />
                  <Button
                    icon={<ClockCircleOutlined />}
                    onClick={() => showCronModal(true)}
                  >
                    生成表达式
                  </Button>
                </Space.Compact>
              </ProForm.Group>
              <ProForm.Group>
                <ProFormRadio.Group
                  width="md"
                  name="misfirePolicy"
                  label="执行策略"
                  initialValue="1"
                  options={[
                    {
                      label: "立即执行",
                      value: "1",
                    },
                    {
                      label: "执行一次",
                      value: "2",
                    },
                    {
                      label: "放弃执行",
                      value: "3",
                    },
                  ]}
                />
                <ProFormRadio.Group
                  name="concurrent"
                  width="md"
                  label="是否并发"
                  initialValue="0"
                  options={[
                    {
                      label: "允许",
                      value: "0",
                    },
                    {
                      label: "禁止",
                      value: "1",
                    },
                  ]}
                />
              </ProForm.Group>
            </ModalForm>,
            <ModalForm
              key="modifymodal"
              title="修改岗位"
              layout="horizontal"
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
                  name="jobName"
                  label="任务名称"
                  placeholder="请输入任务名称"
                  rules={[{ required: true, message: "请输入任务名称" }]}
                />
                <ProFormSelect
                  width="md"
                  name="jobGroup"
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
                />
              </ProForm.Group>
              <ProForm.Group>
                <ProFormText
                  width="lg"
                  name="invokeTarget"
                  label="调用方法"
                  placeholder="请输入调用方法的字符串"
                  tooltip="Bean调用示例：ryTask.ryParams('ry')
                  Class调用示例：com.ruoyi.quartz.task.RyTask.ryParams('ry')
                  参数说明：支持字符串，布尔类型，长整型，浮点型，整型"
                  rules={[
                    { required: true, message: "请输入调用方法的字符串" },
                  ]}
                />
              </ProForm.Group>
              <ProForm.Group>
                <Space.Compact>
                  <ProFormText
                    width="lg"
                    name="cronExpression"
                    label="Cron表达式"
                    placeholder="请输入Cron表达式"
                    rules={[{ required: true, message: "请输入Cron表达式" }]}
                  />
                  <Button
                    icon={<ClockCircleOutlined />}
                    onClick={() => showCronModal(false)}
                  >
                    生成表达式
                  </Button>
                </Space.Compact>
              </ProForm.Group>
              <ProForm.Group>
                <ProFormRadio.Group
                  name="status"
                  width="md"
                  label="状态"
                  initialValue="0"
                  options={[
                    {
                      label: "正常",
                      value: "0",
                    },
                    {
                      label: "暂停",
                      value: "1",
                    },
                  ]}
                />
              </ProForm.Group>
              <ProForm.Group>
                <ProFormRadio.Group
                  width="md"
                  name="misfirePolicy"
                  label="执行策略"
                  initialValue="1"
                  options={[
                    {
                      label: "立即执行",
                      value: "1",
                    },
                    {
                      label: "执行一次",
                      value: "2",
                    },
                    {
                      label: "放弃执行",
                      value: "3",
                    },
                  ]}
                />
                <ProFormRadio.Group
                  name="concurrent"
                  width="md"
                  label="是否并发"
                  initialValue="0"
                  options={[
                    {
                      label: "允许",
                      value: "0",
                    },
                    {
                      label: "禁止",
                      value: "1",
                    },
                  ]}
                />
              </ProForm.Group>
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
      <Modal
        title="Cron表达式生成器"
        key={modalKey}
        zIndex={10000}
        open={isCronShow}
        onOk={getCronData}
        onCancel={() => setIsCronShow(false)}
        // confirmLoading={confirmLoading}
      >
        <Input prefix={<ClockCircleOutlined />} value={cronValue} />
        <div style={{ maxHeight: 450, overflow: "auto" }}>
          <ReQuartzCron
            cssClassPrefix="cron-"
            localization={MortnonCronLocalization}
            value={cronValue}
            onChange={setCronValue}
            renderYearsFrom={new Date().getFullYear()}
          />
        </div>
      </Modal>
      {selectedRow !== undefined && (
        <Modal
          title="任务详情"
          footer={<Button onClick={() => setIsShowDetail(false)}>关闭</Button>}
          open={isShowDetail}
          onCancel={() => setIsShowDetail(false)}
        >
          <ProDescriptions column={2}>
            <ProDescriptions.Item label="任务编号">
              {selectedRow.jobId}
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
            <ProDescriptions.Item label="创建时间">
              {selectedRow.createTime}
            </ProDescriptions.Item>
            <ProDescriptions.Item label="Cron表达式">
              {selectedRow.cronExpression}
            </ProDescriptions.Item>
            <ProDescriptions.Item label="下次执行时间">
              {selectedRow.nextValidTime}
            </ProDescriptions.Item>
          </ProDescriptions>
          <ProDescriptions column={1}>
            <ProDescriptions.Item label="调用目标方法">
              {selectedRow.invokeTarget}
            </ProDescriptions.Item>
          </ProDescriptions>
          <ProDescriptions column={2}>
            <ProDescriptions.Item
              label="任务状态"
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
            <ProDescriptions.Item
              label="是否并发"
              valueEnum={{
                0: {
                  text: "允许",
                  status: "0",
                },
                1: {
                  text: "禁止",
                  status: "1",
                },
              }}
            >
              {selectedRow.concurrent}
            </ProDescriptions.Item>
            <ProDescriptions.Item
              label="执行策略"
              valueEnum={{
                1: {
                  text: "立即执行",
                  status: "1",
                },
                2: {
                  text: "执行一次",
                  status: "2",
                },
                3: {
                  text: "放弃执行",
                  status: "3",
                },
              }}
            >
              {selectedRow.misfirePolicy}
            </ProDescriptions.Item>
          </ProDescriptions>
        </Modal>
      )}
    </PageContainer>
  );
}
