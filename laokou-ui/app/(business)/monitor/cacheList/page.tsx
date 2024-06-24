"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { ReloadOutlined, ClearOutlined } from "@ant-design/icons";
import {
  Row,
  Col,
  Tooltip,
  Table,
  Button,
  Form,
  Input,
  Flex,
  message,
} from "antd";
import { DeleteOutlined } from "@ant-design/icons";
import { PageContainer, ProCard } from "@ant-design/pro-components";
import {
  faFloppyDisk,
  faKey,
  faFileLines,
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { fetchApi } from "@/app/_modules/func";

const { TextArea } = Input;

//查询缓存API
const queryCacheAPI = "/api/monitor/cache/getNames";
//删除指定的缓存API
const deleteCacheAPI = "/api/monitor/cache/clearCacheName";
//查询缓存Key API
const queryCacheKeyAPI = "/api/monitor/cache/getKeys";
//查询缓存值API
const queryValueAPI = "/api/monitor/cache/getValue";
//删除指定的Key API
const deleteKeyAPI = "/api/monitor/cache/clearCacheKey";
//清空所有缓存API
const clearAPI = "/api/monitor/cache/clearCacheAll";

export default function CacheList() {
  const { push } = useRouter();

  //缓存列表加载状态
  const [isCacheLoading, setIsCacheLoading] = useState(false);

  //缓存数据
  const [cacheData, setCacheData] = useState([]);

  //查询缓存
  const queryCache = async () => {
    setIsCacheLoading(true);
    const body = await fetchApi(queryCacheAPI, push);
    if (body !== undefined) {
      if (body.code == 200) {
        setCacheData(body.data);
      }
    }
    setIsCacheLoading(false);
  };

  useEffect(() => {
    queryCache();
  }, []);

  //缓存列定义
  const cacheColumns = [
    {
      title: "序号",
      key: "index",
      width: "60px",
      render: (text: any, record: any, index: number) => `${index + 1}`,
    },
    {
      title: "缓存名称",
      dataIndex: "cacheName",
      ellipsis: true,
    },
    {
      title: "备注",
      dataIndex: "remark",
    },
    {
      title: "操作",
      key: "action",
      render: (text: any, record: any) => [
        <Button
          key="deleteBtn"
          type="link"
          danger
          icon={<DeleteOutlined />}
          onClick={() => deleteCache(record.cacheName)}
        ></Button>,
      ],
    },
  ];

  //缓存列表行点击
  const cacheRowClick = (record: any, index: any) => {
    return {
      onMouseDown: (event: any) => {
        setSelectedCache(record.cacheName);
        queryKeys(record.cacheName);
      },
    };
  };

  //删除指定的缓存
  const deleteCache = async (cacheName: string) => {
    const body = await fetchApi(`${deleteCacheAPI}/${cacheName}}`, push, {
      method: "DELETE",
    });
    if (body != undefined) {
      if (body.code == 200) {
        message.success(`清理缓存[${cacheName}]成功`);
        queryCache();
      } else {
        message.error(body.msg);
      }
    }
  };

  //选中的缓存
  const [selectedCache, setSelectedCache] = useState("");

  //key列表加载状态
  const [isKeyLoading, setIsKeyLoading] = useState(false);

  //key 数据
  const [keyData, setKeyData] = useState([]);

  //查询缓存对应的key
  const queryKeys = async (cacheName?: string) => {
    if (cacheName === undefined) {
      if (selectedCache === "") {
        return;
      }
      cacheName = selectedCache;
    }

    setIsKeyLoading(true);
    const body = await fetchApi(`${queryCacheKeyAPI}/${cacheName}`, push);
    if (body !== undefined) {
      if (body.code == 200) {
        setKeyData(body.data);
      }
    }

    setIsKeyLoading(false);
  };

  //key列定义
  const keyColumns = [
    {
      title: "序号",
      key: "index",
      width: "60px",
      render: (text: any, record: any, index: number) => `${index + 1}`,
    },
    {
      title: "缓存键名",
      key: "key",
      ellipsis: true,
      render: (text: any, record: string) => record.split(":")[1],
    },
    {
      title: "操作",
      key: "action",
      render: (text: any, record: any) => [
        <Button
          key="deleteBtn"
          type="link"
          danger
          icon={<DeleteOutlined />}
          onClick={() => deleteKey(record)}
        ></Button>,
      ],
    },
  ];

  //key列表行点击
  const keyRowClick = (record: any, index: any) => {
    return {
      onClick: (event: any) => {
        queryValue(record);
      },
    };
  };

  //删除指定的key
  const deleteKey = async (key: string) => {
    const body = await fetchApi(`${deleteKeyAPI}/${key}`, push, {
      method: "DELETE",
    });
    if (body != undefined) {
      if (body.code == 200) {
        message.success(`清理缓存键名[${key}]成功`);
        queryKeys();
      } else {
        message.error(body.msg);
      }
    }
  };

  //缓存值展示表单
  const [valueForm] = Form.useForm();

  //查询值
  const queryValue = async (key: any) => {
    const body = await fetchApi(
      `${queryValueAPI}/${selectedCache}/${key}`,
      push
    );

    if (body !== undefined) {
      if (body.code == 200) {
        valueForm?.setFieldsValue({
          cacheName: body.data.cacheName,
          cacheKey: body.data.cacheKey,
          cacheValue: body.data.cacheValue,
        });
      }
    }
  };

  //清空全部缓存
  const clearCache = async () => {
    const body = await fetchApi(clearAPI, push, {
      method: "DELETE",
    });

    if (body !== undefined) {
      if (body.code == 200) {
        message.success("清空全部缓存成功");
      } else {
        message.error(body.msg);
      }
    } else {
      message.error("清空全部缓存异常");
    }
  };

  return (
    <PageContainer title={false}>
      <Flex justify="flex-end" style={{ marginBottom: 16 }}>
        <Tooltip title="清空全部缓存">
          <Button
            icon={<ClearOutlined />}
            type="primary"
            onClick={clearCache}
          />
        </Tooltip>
      </Flex>
      <Row gutter={8}>
        <Col span={8}>
          <ProCard
            title={
              <>
                <FontAwesomeIcon icon={faFloppyDisk} />
                <span style={{ marginLeft: 6 }}>缓存列表</span>
              </>
            }
            extra={
              <Tooltip title="刷新">
                <a onClick={queryCache}>
                  <ReloadOutlined />
                </a>
              </Tooltip>
            }
            style={{ height: "100%" }}
            headerBordered
            bordered
            hoverable
          >
            <div style={{ overflowX: "auto" }}>
              <Table
                dataSource={cacheData}
                columns={cacheColumns}
                loading={isCacheLoading}
                onRow={cacheRowClick}
              />
            </div>
          </ProCard>
        </Col>
        <Col span={8}>
          <ProCard
            title={
              <>
                <FontAwesomeIcon icon={faKey} />
                <span style={{ marginLeft: 6 }}>键名列表</span>
              </>
            }
            extra={
              <Tooltip title="刷新">
                <a onClick={() => queryKeys()}>
                  <ReloadOutlined />
                </a>
              </Tooltip>
            }
            style={{ height: "100%" }}
            headerBordered
            bordered
            hoverable
          >
            <div style={{ overflowX: "auto" }}>
              <Table
                dataSource={keyData}
                columns={keyColumns}
                loading={isKeyLoading}
                onRow={keyRowClick}
              />
            </div>
          </ProCard>
        </Col>
        <Col span={8}>
          <ProCard
            title={
              <>
                <FontAwesomeIcon icon={faFileLines} />
                <span style={{ marginLeft: 6 }}>缓存内容</span>
              </>
            }
            style={{ height: "100%" }}
            headerBordered
            bordered
            hoverable
          >
            <Form layout="vertical" form={valueForm}>
              <Form.Item label="缓存名称" name="cacheName">
                <Input readOnly />
              </Form.Item>
              <Form.Item label="缓存键名" name="cacheKey">
                <Input readOnly />
              </Form.Item>
              <Form.Item label="缓存内容" name="cacheValue">
                <TextArea readOnly rows={8} />
              </Form.Item>
            </Form>
          </ProCard>
        </Col>
      </Row>
    </PageContainer>
  );
}
