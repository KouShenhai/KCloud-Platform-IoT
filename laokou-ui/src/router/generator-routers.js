// eslint-disable-next-line
import { getRouters } from '@/api/sys/menu'
import { indexRouterMap } from '@/config/router.config'
import allIcon from '@/core/icons'
import { UserLayout, BlankLayout, PageView } from '@/layouts'
import { validURL } from '@/utils/validate'
// 前端路由表
const constantRouterComponents = {
  // 基础页面 layout 必须引入
  BasicLayout: () => import('@/layouts/BasicLayout'),
  BlankLayout: BlankLayout,
  RouteView: () => import('@/layouts/RouteView'),
  PageView: PageView,
  UserLayout: UserLayout, // 登陆注册页面的通用布局

  // 你需要动态引入的页面组件
  'Index': () => import('@/views/index')
}

// 前端未找到页面路由（固定不用改）
const notFoundRouter = {
  path: '*', redirect: '/404', hidden: true
}

// 根级菜单
const rootMenu = {
  key: '',
  name: 'index',
  path: '',
  component: 'BasicLayout',
  redirect: '/index',
  meta: {
    title: '首页'
  },
  children: []
}

// 根级路由
const rootRouter = {
  key: '',
  name: 'index',
  path: '',
  component: constantRouterComponents.BasicLayout,
  redirect: '/index',
  meta: {
    title: '首页'
  },
  children: []
}

/**
 * 动态生成菜单
 * @returns {Promise<Router>}
 */
export const generatorDynamicRouter = () => {
  return new Promise((resolve, reject) => {
    // 向后端请求路由数据
    getRouters().then(res => {
      // 路由菜单分离，路由全部为二级，解决多级菜单缓存问题
      const routers = []
      const menuNav = []
      rootMenu.children = indexRouterMap.concat(res.data)
      menuNav.push(rootMenu)
      const menus = generator(menuNav, null, routers)
      menus.push(notFoundRouter)
      rootRouter.children = routers
      resolve({ menus, routers: rootRouter })
    }).catch(err => {
      reject(err)
    })
  })
}

/**
 * 格式化树形结构数据 生成 vue-router 层级路由表
 *
 * @param routerMap
 * @param parent
 * @param routers
 * @returns {*}
 */
export const generator = (routerMap, parent, routers) => {
  const names = parent ? parent.meta.names : []
  return routerMap.map(item => {
    // 适配若依一级菜单
    if (item.path === '/' && item.children && item.children.length === 1) {
      item = item.children[0]
      item.children = undefined
    }
    const { title, hideChildren, hiddenHeaderContent, hidden, icon, keepAlive } = item.meta || {}
    const name = item.name || item.key || ''
    const isRouter = item.component && item.component !== 'Layout' && item.component !== 'ParentView'
    const currentRouter = {
      // 如果路由设置了 path，则作为默认 path，否则 路由地址 动态拼接生成如 /dashboard/workplace
      path: item.path || `${parent && parent.path || ''}/${item.path}`,
      // 路由名称，建议唯一
      name: name,
      // 该路由对应页面的 组件(动态加载)
      component: constantRouterComponents[item.component || item.key] || (() => import(`@/views/${item.component}`)),
      hidden: item.hidden,
      // meta: 页面标题, 菜单图标, 页面权限(供指令权限用，可去掉)
      meta: {
        title: title,
        icon: allIcon[icon + 'Icon'] || icon,
        hiddenHeaderContent: hiddenHeaderContent,
        // 目前只能通过判断path的http链接来判断是否外链，适配若依
        target: validURL(item.path) ? '_blank' : '',
        keepAlive: keepAlive,
        hidden: hidden,
        // 因菜单路由分离，通过此names确定菜单树的展开
        names: names.concat([name])
      },
      redirect: item.redirect
    }
    // 适配若依，若依为缩写路径，而antdv-pro的pro-layout要求每个路径需为全路径
    if (!constantRouterComponents[item.component || item.key]) {
      // currentRouter.path = `${parent && parent.path !== '/' && parent.path || ''}/${item.path}`
      currentRouter.path = `${parent && parent.path !== '/' && parent.path + '/' || ''}${item.path}`
    }
    // 是否设置了隐藏子菜单
    if (hideChildren) {
      currentRouter.hideChildrenInMenu = true
    }
    // const names = parent.names
    const router = {
      path: currentRouter.path,
      // 路由名称，建议唯一
      name: currentRouter.name,
      // 该路由对应页面的 组件(动态加载)
      component: currentRouter.component,
      hidden: currentRouter.hidden,
      // meta: 页面标题, 菜单图标, 页面权限(供指令权限用，可去掉)
      meta: currentRouter.meta,
      redirect: currentRouter.redirect
    }
    if (router.component && isRouter) {
      routers.push(router)
    }
    // 是否有子菜单，并递归处理，并将父path传入
    if (item.children && item.children.length > 0) {
      // Recursion
      currentRouter.children = generator(item.children, currentRouter, routers)
    }
    return currentRouter
  })
}
