"use client";

import {
  PageContainer,
  ProDescriptions,
  ProTable,
  ProCard,
  ProForm,
  ProFormText,
  ProSkeleton,
} from "@ant-design/pro-components";

import type { ProColumns } from "@ant-design/pro-components";

import { Divider, message, Flex, Button } from "antd";

import { fetchApi } from "@/app/_modules/func";

import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";

export default function UserAuth({ params }: { params: { userid: string } }) {
  const { push } = useRouter();

  //用户信息
  const [user, setUser] = useState<any>({});
  //角色信息
  const [roles, setRoles] = useState([]);

  const getUserData = async () => {
    const body = await fetchApi(
      `/api/system/user/authRole/${params.userid}`,
      push
    );
    if (body !== undefined) {
      if (body.code == 200) {
        setUser(body.user);
        setRoles(body.roles);

        setSelectedRowKeys(body.user.roles.map((item: any) => item.roleId));
      }
    }
  };

  useEffect(() => {
    getUserData();
  }, []);

  //表格列定义
  const columns: ProColumns[] = [
    {
      title: "序号",
      dataIndex: "index",
      valueType: "indexBorder",
      width: 48,
    },
    {
      title: "角色编号",
      dataIndex: "roleId",
      search: false,
    },
    {
      title: "角色名称",
      dataIndex: "roleName",
      search: false,
    },
    {
      title: "权限字符",
      dataIndex: "roleKey",
      search: false,
    },
    {
      title: "创建时间",
      dataIndex: "createTime",
    },
  ];

  //选中行操作
  const [selectedRowKeys, setSelectedRowKeys] = useState<React.Key[]>([]);

  const rowSelection = {
    onChange: (newSelectedRowKeys: React.Key[]) => {
      setSelectedRowKeys(newSelectedRowKeys);
    },
  };

  //当前每页条数
  const defaultPageSize = 10;

  //提交按钮加载状态
  const [confirmLoding, setConfirmLoading] = useState(false);

  //更新用户角色
  const updateAuth = async () => {
    setConfirmLoading(true);
    const queryParam = {
      userId: user.userId,
      roleIds: selectedRowKeys.join(","),
    };

    const body = await fetchApi(
      `/api/system/user/authRole?${new URLSearchParams(queryParam)}`,
      push,
      {
        method: "PUT",
      }
    );

    if (body !== undefined) {
      if (body.code == 200) {
        message.success("授权成功");
      } else {
        message.error(body.msg);
      }
    }

    setConfirmLoading(false);
  };

  return (
    <PageContainer
      header={{
        title: "分配角色",
        onBack(e) {
          push("/system/user");
        },
      }}
    >
      {Object.keys(user).length === 0 ? (
        <ProSkeleton type="list" />
      ) : (
        <>
          <ProCard title="基本信息">
            <ProDescriptions column={24}>
              <ProDescriptions.Item span={12} label="用户昵称">
                {user.nickName}
              </ProDescriptions.Item>
              <ProDescriptions.Item span={12} label="用户名称">
                {user.userName}
              </ProDescriptions.Item>
            </ProDescriptions>
          </ProCard>
          <Divider />
          <ProCard title="角色信息">
            <ProTable
              rowKey="roleId"
              rowSelection={{
                selectedRowKeys,
                ...rowSelection,
              }}
              tableAlertRender={false}
              columns={columns}
              dataSource={roles}
              pagination={{
                defaultPageSize: defaultPageSize,
                showQuickJumper: true,
                showSizeChanger: true,
                // onChange: pageChange,
              }}
              search={false}
              dateFormatter="string"
              toolbar={{
                actions: [],
                settings: [],
              }}
            />
          </ProCard>
          <ProCard>
            <Flex justify="center" gap="middle">
              <Button href="/system/user">返回</Button>
              <Button
                type="primary"
                onClick={updateAuth}
                loading={confirmLoding}
              >
                提交
              </Button>
            </Flex>
          </ProCard>
        </>
      )}
    </PageContainer>
  );
}
