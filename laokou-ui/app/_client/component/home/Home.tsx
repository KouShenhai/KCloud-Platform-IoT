"use client"
import React, { useState, useEffect, useCallback } from 'react'
import { useRouter } from 'next/navigation'
import type { MenuProps } from 'antd';
import { Breadcrumb, Layout, Menu, theme } from 'antd';


import { NavigationEvents } from '@/app/_client/auth/pageAuth';
import menusList from '@/app/_server/lib/menus';



const { Header, Content, Footer, Sider } = Layout;


interface MenuItem {
  label: React.ReactNode,
  key: React.Key,
  icon?: React.ReactNode,
  children?: MenuItem[],
  type?: React.ReactNode,
  path?: React.ReactNode,
  url?:React.ReactNode,
  parent?:React.ReactNode,
}



const Home = ({ children }: React.PropsWithChildren) => {
  const {
    token: { colorBgContainer, borderRadiusLG },
  } = theme.useToken();

  const [isCollapsed, setIsCollapsed] = useState(false)

  const navigate = useRouter()

  const handleCollapse = useCallback(() => {
    setIsCollapsed(!isCollapsed)
  }, [isCollapsed])


  useEffect(() => {
    setIsCollapsed(window.innerWidth < 1024 ? true : false)
  }, []);

  // 监听外部窗口宽度的变化，如果小于 1024px，自动收起侧边栏
  useEffect(() => {
    const handleResize = () => {
      if (window.innerWidth < 1024) {
        setIsCollapsed(true)
      } else {
        setIsCollapsed(false)
      }
    }

    window.addEventListener('resize', handleResize)

    return () => {
      window.removeEventListener('resize', handleResize)
    }
  }, [])

  // 菜单的点击事件
  const onMenuClick: MenuProps['onClick'] = (e) => {
    const menuItem = currentMenu(e.key, menusList);
    if (menuItem && menuItem.path&&menuItem.parent) {
      navigate.push(menuItem.parent+menuItem.path.toString())
    }
  }
  //菜单递归查找
  const currentMenu = (evenKey: string, childItem: MenuItem[]): MenuItem | undefined => {
    let result: MenuItem | undefined;
    childItem.find((x: MenuItem) => {
      const { key,children } = x;
      if (key.toString() === evenKey) {
        result = x;
      } else if (undefined != children) {
        const child = currentMenu(evenKey, children);
        if(child){
          result = child;
        }
      }
    })
    return result;
  }

  return (

    <Layout style={{ minHeight: '100vh' }}>

      <Sider
        collapsible
        collapsed={isCollapsed}
        onCollapse={handleCollapse}>
        <div style={{width:'100%',height:'10px',backgroundImage:'next.svg'}}>

        </div>
        <Menu
          theme="dark"
          mode="inline"
          items={menusList}
          onClick={onMenuClick}
          className="mb-4 dark:bg-slate-800"

        />

      </Sider>
      <Layout>
        <Header style={{ padding: 0, background: colorBgContainer }} />

        <Content style={{ margin: '0 16px' }}>

          <Breadcrumb style={{ margin: '16px 0' }} items={[{ title: "User" }, { title: "Bill" }]} />
          <div
            style={{
              padding: 24,
              minHeight: '100%',
              background: colorBgContainer,
              borderRadius: borderRadiusLG,
            }}
          >
            <NavigationEvents children={children} />
          </div>
        </Content>
        <Footer style={{ textAlign: 'center' }}>
          Ant Design ©{new Date().getFullYear()} Created by Ant UED
        </Footer>
      </Layout>
    </Layout>
  );
};
export default Home;

