import antd from 'ant-design-vue/es/locale-provider/zh_CN'
import momentCN from 'moment/locale/zh-cn'
import setting from './zh-CN/setting'
import user from './zh-CN/user'
import home from './zh-CN/home'

const components = {
  antLocale: antd,
  momentName: 'zh',
  momentLocale: momentCN
}

export default {
  'message': '-',
  ...components,
  ...setting,
  ...user,
  ...home
}
