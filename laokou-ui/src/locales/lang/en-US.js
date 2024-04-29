import antdEnUS from 'ant-design-vue/es/locale-provider/en_US'
import momentEU from 'moment/locale/eu'
import setting from './en-US/setting'
import user from './en-US/user'
import tenant from './en-US/tenant'

const components = {
  antLocale: antdEnUS,
  momentName: 'eu',
  momentLocale: momentEU
}

export default {
  'message': '-',
  ...components,
  ...setting,
  ...user,
  ...tenant
}
