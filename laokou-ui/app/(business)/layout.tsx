"use client";
import {
  ChromeFilled,
  ExclamationCircleFilled,
  GithubOutlined,
  HomeOutlined,
  LogoutOutlined,
  MenuOutlined,
  QuestionCircleFilled,
  SearchOutlined,
  UserOutlined,
  FullscreenOutlined,
  FullscreenExitOutlined,
} from "@ant-design/icons";
import { ProConfigProvider, ProLayout } from "@ant-design/pro-components";
import { Dropdown, Select, MenuProps, Modal, Tooltip, Input } from "antd";
import type { SelectProps } from "antd";
import { deleteCookie, getCookie } from "cookies-next";
import Link from "next/link";
import { usePathname, useRouter } from "next/navigation";
import { useEffect, useState, useRef } from "react";
import { IconMap, RouteInfo, UserInfo } from "../_modules/definies";
import "./styles.css";

import {
  displayModeIsDark,
  fetchApi,
  watchDarkModeChange,
} from "../_modules/func";

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  const { push } = useRouter();

  const redirectToLogin = () => {
    push("/login");
  };

  //深色模式
  const [isDark, setIsDark] = useState(false);

  //检查登录状态，失效跳转到登录页
  useEffect(() => {
    const token = getCookie("token");

    if (token === "") {
      redirectToLogin();
      return;
    }
    getProfile();

    setIsDark(displayModeIsDark());
    const unsubscribe = watchDarkModeChange((matches: boolean) => {
      setIsDark(matches);
    });

    //点击非搜索区域隐藏搜索框
    document.addEventListener("click", hideSearchInput);

    return () => {
      unsubscribe();
      document.removeEventListener("click", hideSearchInput);
    };
  }, []);

  //搜索按钮区域引用
  const searchRef = useRef<HTMLDivElement>(null);

  //点击非搜索按钮区域，用于隐藏搜索框
  const hideSearchInput = (e: any) => {
    if (searchRef.current && !searchRef.current.contains(e.target)) {
      //关闭搜索框
      setShowSearch(false);
      //清空搜索列表数据
      setSearchListData([]);
    }
  };

  //是否展示搜索框
  const [showSearch, setShowSearch] = useState(false);

  //是否展示退出对话框
  const [isLogoutShow, setIsLogoutShow] = useState(false);
  //是否加载中
  const [confirmLoading, setConfirmLoading] = useState(false);

  //用户下拉菜单点击操作
  const onActionClick: MenuProps["onClick"] = ({ key }) => {
    if (key === "logout") {
      setIsLogoutShow(true);
    } else if (key === "profile") {
      push("/user/profile");
    }
  };

  //用户昵称
  const [userInfo, setUserInfo] = useState({
    nickName: "Monrtnon",
    avatar: "/avatar1.jpeg",
  } as UserInfo);

  //获取用户信息
  const getProfile = async () => {
    const data = await fetchApi("/api/getInfo", push);

    if (data !== undefined) {
      const userInfo: UserInfo = {
        nickName: data.user.nickName,
        avatar:
          data.user.avatar === ""
            ? data.user.sex === "1"
              ? "/avatar1.jpeg"
              : "/avatar0.jpeg"
            : "/api" + data.user.avatar,
      };

      setUserInfo(userInfo);
    }
  };

  //缓存的菜单数据
  const [menuData, setMenuData] = useState<any[]>([]);

  //获取菜单
  const getRoutes = async () => {
    const body = await fetchApi("/api/getRouters", push);
    const rootChildren: Array<RouteInfo> = new Array<RouteInfo>();
    //搜索用的菜单列表
    const searchMenuList: any[] = [];

    const indexRoute: RouteInfo = {
      path: "/home",
      name: "首页",
      icon: <HomeOutlined />,
    };

    searchMenuList.push({
      value: indexRoute.path,
      text: indexRoute.name,
    });

    rootChildren.push(indexRoute);

    if (body.data && body.data.length > 0) {
      body.data.forEach((menu: any) => {
        const route: RouteInfo = {
          path: menu.path,
          name: menu.meta.title,
          icon:
            menu.meta.icon !== null ? (
              IconMap[menu.meta.icon.replace(/-/g, "") as "system"]
            ) : (
              <MenuOutlined />
            ),
        };

        if (menu.children && menu.children.length > 0) {
          getSubMenu(
            route,
            menu.children,
            route.name,
            route.path,
            searchMenuList
          );
        }
        rootChildren.push(route);
      });
    }

    const bookHub: RouteInfo = {
      path: "https://docs.bookhub.tech",
      name: "BookHub 网站",
      icon: <ChromeFilled />,
    };

    rootChildren.push(bookHub);

    setMenuData(searchMenuList);
    return rootChildren;
  };

  const getSubMenu = (
    parent: RouteInfo,
    menuChildren: any,
    parentName: any,
    parentPath: any,
    searchMenuList: any[]
  ) => {
    const routeChildren: Array<RouteInfo> = new Array<RouteInfo>();
    menuChildren.forEach((menu: any) => {
      const route: RouteInfo = {
        path: menu.path,
        name: menu.meta.title,
        icon:
          menu.meta.icon !== null ? (
            IconMap[menu.meta.icon.replace(/-/g, "") as "system"]
          ) : (
            <MenuOutlined />
          ),
      };
      routeChildren.push(route);

      if (menu.children && menu.children.length > 0) {
        getSubMenu(
          route,
          menu.children,
          parentName + " > " + route.name,
          parentPath + "/" + route.path,
          searchMenuList
        );
      } else {
        searchMenuList.push({
          text: parentName + " > " + menu.meta.title,
          value: parentPath + "/" + route.path,
        });
      }
    });

    parent.routes = routeChildren;
  };

  //退出登录
  const logout = async () => {
    setConfirmLoading(true);
    const data = await fetchApi("/api/logout", push, {
      method: "POST",
    });

    if (data.code == 200) {
      deleteCookie("token");
      redirectToLogin();
      setIsLogoutShow(false);
      setConfirmLoading(false);
    }
  };

  //默认当前展示首页
  const pathName = usePathname();
  const [pathname, setPathname] = useState(pathName);

  //搜索数据
  const [searchListData, setSearchListData] = useState<SelectProps["options"]>(
    []
  );

  //处理搜索
  const handleSearch = (newValue: string) => {
    if (newValue === "") {
      return;
    }

    setSearchListData(menuData.filter((item) => item.text.includes(newValue)));
  };

  //点击选中搜索结果
  const handleSearchChange = (path: string) => {
    setPathname(path || "/index");
    push(path);
  };

  //页面是否全屏
  const [isFullscreen, setIsFullscreen] = useState(false);

  //打开全屏
  const openFullscreen = () => {
    const element = document.documentElement;
    if (element.requestFullscreen) {
      element.requestFullscreen();
    } else if (element.mozRequestFullScreen) {
      element.mozRequestFullScreen();
    } else if (element.webkitRequestFullScreen) {
      element.webkitRequestFullScreen();
    }

    setIsFullscreen(true);
  };

  //关闭全屏
  const closeFullscreen = () => {
    if (document.exitFullscreen) {
      document.exitFullscreen();
    } else if (document.mozCancelFullScreen) {
      document.mozCancelFullScreen();
    } else if (document.webkitExitFullScreen) {
      document.webkitExitFullScreen();
    }

    setIsFullscreen(false);
  };

  return (
    <ProConfigProvider dark={isDark}>
      <ProLayout
        title="MorTnon 若依"
        logo="https://static.dongfangzan.cn/img/mortnon.svg"
        menu={{
          request: getRoutes,
        }}
        layout="mix"
        splitMenus={false}
        defaultCollapsed={false}
        breakpoint={false}
        onMenuHeaderClick={(e) => console.log(e)}
        menuItemRender={(item, dom) => {
          let shouldRenderIcon =
            item.pro_layout_parentKeys && item.pro_layout_parentKeys.length > 0;
          return (
            <div
              onClick={() => {
                setPathname(item.path || "/index");
              }}
            >
              <Link href={item.path !== undefined ? item.path : ""}>
                {shouldRenderIcon ? (
                  <span style={{ display: "flex", alignItems: "center" }}>
                    {item.icon}
                    <span style={{ marginLeft: "8px" }}>{dom}</span>
                  </span>
                ) : (
                  dom
                )}
              </Link>
            </div>
          );
        }}
        subMenuItemRender={(item, dom) => {
          let shouldRenderIcon =
            item.pro_layout_parentKeys && item.pro_layout_parentKeys.length > 0;
          return (
            <>
              {shouldRenderIcon ? (
                <span style={{ display: "flex", alignItems: "center" }}>
                  {item.icon}
                  <span style={{ marginLeft: "8px" }}>{dom}</span>
                </span>
              ) : (
                dom
              )}
            </>
          );
        }}
        location={{
          pathname,
        }}
        avatarProps={{
          src: `${userInfo.avatar}`,
          size: "small",
          title: `${userInfo.nickName}`,
          render: (props, dom) => {
            return (
              <Dropdown
                menu={{
                  items: [
                    {
                      key: "profile",
                      icon: <UserOutlined />,
                      label: "个人中心",
                    },
                    {
                      type: "divider",
                    },
                    {
                      key: "logout",
                      icon: <LogoutOutlined />,
                      label: "退出登录",
                    },
                  ],
                  onClick: onActionClick,
                }}
              >
                {dom}
              </Dropdown>
            );
          },
        }}
        actionsRender={(props) => {
          if (props.isMobile) return [];
          return [
            props.layout !== "side" ? (
              <div
                key="SearchOutlined"
                aria-hidden
                style={{
                  height: "100%",
                  display: "flex",
                  alignItems: "center",
                }}
                ref={searchRef}
              >
                <SearchOutlined
                  style={{
                    color: "var(--ant-primary-color)",
                    marginRight: showSearch && 8,
                    height: "100%",
                  }}
                  onClick={() => setShowSearch(!showSearch)}
                />

                {showSearch && (
                  <Select
                    showSearch
                    autoFocus
                    style={{
                      borderRadius: 4,
                      marginInlineEnd: 12,
                      backgroundColor: "rgba(0,0,0,0.03)",
                      width: 300,
                    }}
                    suffixIcon={null}
                    placeholder="搜索菜单"
                    variant="borderless"
                    filterOption={false}
                    notFoundContent={null}
                    onSearch={handleSearch}
                    onChange={handleSearchChange}
                    options={(searchListData || []).map((d) => ({
                      value: d.value,
                      label: d.text,
                    }))}
                  />
                )}
              </div>
            ) : undefined,
            <Link
              style={{ padding: "0 8px" }}
              key="github"
              href="https://github.com/mortise-and-tenon/RuoYi-React-Pro"
              target="_blank"
            >
              <Tooltip title="Github 源码仓库">
                <GithubOutlined style={{ color: "gray" }} />
              </Tooltip>
            </Link>,
            <Link
              style={{ padding: "0 8px" }}
              key="question"
              href="https://doc.ruoyi.vip/ruoyi-vue/"
              target="_blank"
            >
              <Tooltip title="RuoYi 文档">
                <QuestionCircleFilled style={{ color: "gray" }} />
              </Tooltip>
            </Link>,
            isFullscreen ? (
              <FullscreenExitOutlined
                style={{
                  color: "var(--ant-primary-color)",
                  marginRight: showSearch && 8,
                  height: "100%",
                }}
                onClick={closeFullscreen}
              />
            ) : (
              <FullscreenOutlined
                style={{
                  color: "var(--ant-primary-color)",
                  marginRight: showSearch && 8,
                  height: "100%",
                }}
                onClick={openFullscreen}
              />
            ),
          ];
        }}
        menuFooterRender={(props) => {
          if (props?.collapsed) return undefined;
          return (
            <div
              style={{
                textAlign: "center",
                paddingBlockStart: 12,
              }}
            >
              <div>©{new Date().getFullYear()} Mortnon.</div>
            </div>
          );
        }}
      >
        <Modal
          title={
            <>
              <ExclamationCircleFilled style={{ color: "#faad14" }} /> 提示
            </>
          }
          open={isLogoutShow}
          onOk={logout}
          onCancel={() => setIsLogoutShow(false)}
          confirmLoading={confirmLoading}
        >
          确定注销并退出系统吗？
        </Modal>
        {children}
      </ProLayout>
    </ProConfigProvider>
  );
}
