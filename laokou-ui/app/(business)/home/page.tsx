"use client";

import { GithubOutlined } from "@ant-design/icons";
import { PageContainer, ProCard } from "@ant-design/pro-components";
import { Avatar, Button, Col, List, Row, Space, Typography } from "antd";
import Image from "next/image";
import RcResizeObserver from "rc-resize-observer";
import { useState } from "react";
import {
  faDownload
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
const { Text, Link, Title } = Typography;

export default function Index() {
  const [responsive, setResponsive] = useState(false);

  const data = [
    {
      avatar: "/nextjs.ico",
      link: "https://nextjs.org/",
      title: "NextJS",
      desc: "通过扩展最新的 React 功能，并集成强大的基于 Rust 的 JavaScript 工具以实现最快的构建，Next.js 可帮助您创建全栈 Web 应用程序。",
    },
    {
      avatar: "/antd.svg",
      link: "https://ant-design.antgroup.com/index-cn",
      title: "Ant Design",
      desc: "一款企业级用户界面设计语言和 React UI 库。",
    },
    {
      avatar: "/antdpro.svg",
      link: "https://procomponents.ant.design/",
      title: "Ant Design ProComponents",
      desc: "对于中后台类应用，我们希望提供更高程度的抽象，提供更上层的设计规范，并且提供相应的组件使得开发者可以快速搭建出高质量的页面。",
    },
    {
      avatar: "/fontawesome.ico",
      link: "https://fontawesome.com/",
      title: "Font Awesome",
      desc: "互联网上的图标库和工具包，被数百万设计师、开发人员和内容创建者所使用。",
    },
  ];

  return (
    <PageContainer>
      <RcResizeObserver
        key="resize-observer"
        onResize={(offset) => {
          setResponsive(offset.width < 596);
        }}
      >
        <ProCard
          title="MorTnon RuoYi"
          split={responsive ? "horizontal" : "vertical"}
          bordered
          headerBordered
        >
          <ProCard title={<Title level={5}>介绍</Title>} colSpan="50%">
            <Space direction="vertical">
              <Text>
                RuoYi
                是一个广泛使用的的后台管理框架，适合原型验证或快速开发项目。
              </Text>
              <Text>
                RuoYi 的前后端分离版本中，前端项目使用了 Vue 和
                Element。鉴于其美观度及技术方向，
                <Link
                  href="https://gitee.com/mortise-and-tenon"
                  target="_blank"
                >
                  MorTnon
                </Link>{" "}
                开发了本项目的 RuoYi 前端。
              </Text>
              <Text>
                本项目主要采用 NextJs、AntD 和 AntD ProComponents
                开发，可以完美替换 RuoYi
                前后端分离版本的前端，且具有更好的美观度和交互性。本项目可以直接使用，也适合
                React 技术栈的开发人员二次开发使用。
              </Text>
              <Title level={5}>项目</Title>
              <Row gutter={8}>
                <Col>
                  <Image
                    src="https://img.shields.io/github/stars/mortise-and-tenon/RuoYi-React-Pro?color=%2321BAB5&label=Stars&logo=github"
                    width={76}
                    height={20}
                    alt="github_stars"
                  />
                </Col>
                <Col>
                  <Image
                    src="https://img.shields.io/github/forks/mortise-and-tenon/RuoYi-React-Pro?color=%2321BAB5&label=Forks&logo=github"
                    width={76}
                    height={20}
                    alt="github_forks"
                  />
                </Col>
              </Row>
              <Text>
                目前项目采用协议 Apache-2.0，可自由使用本项目。项目在 GitHub 和
                Gitee 都有仓库。
              </Text>
              <Row gutter={16}>
                <Col>
                  <Button
                    type="dashed"
                    icon={<GithubOutlined />}
                    href="https://github.com/mortise-and-tenon/RuoYi-React-Pro"
                  >
                    访问 GitHub
                  </Button>
                </Col>
                <Col>
                  <Button
                    type="dashed"
                    icon={<FontAwesomeIcon icon={faDownload} />}
                    href="https://gitee.com/mortise-and-tenon/ruoyi-react-pro"
                  >
                    访问 Gitee
                  </Button>
                </Col>
              </Row>
            </Space>
          </ProCard>
          <ProCard title={<Title level={5}>技术栈</Title>}>
            <List
              itemLayout="horizontal"
              dataSource={data}
              renderItem={(item, index) => (
                <List.Item>
                  <List.Item.Meta
                    avatar={<Avatar src={item.avatar} />}
                    title={<Link href={item.link}>{item.title}</Link>}
                    description={item.desc}
                  />
                </List.Item>
              )}
            />
          </ProCard>
        </ProCard>
      </RcResizeObserver>
    </PageContainer>
  );
}
