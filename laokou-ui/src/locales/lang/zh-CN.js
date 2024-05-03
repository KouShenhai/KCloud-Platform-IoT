import antd from 'ant-design-vue/es/locale-provider/zh_CN'
import momentCN from 'moment/locale/zh-cn'
import setting from './zh-CN/setting'
import user from './zh-CN/user'
import tenant from './zh-CN/tenant'
import menu from './zh-CN/menu'

const components = {
  antLocale: antd,
  momentName: 'zh-cn',
  momentLocale: momentCN
}

export default {
  'message': '-',
  ...components,
  ...setting,
  ...user,
  ...tenant,
  ...menu
}
