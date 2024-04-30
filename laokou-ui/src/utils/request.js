import axios from 'axios'
import store from '@/store'
import storage from 'store'
import notification from 'ant-design-vue/es/notification'
import message from 'ant-design-vue/es/message'
import { VueAxios } from './axios'
import { ACCESS_TOKEN, LANG, TENANT_ID, USER_ID, USER_NAME } from '@/store/mutation-types'
import errorCode from '@/utils/errorCode'
import qs from 'qs'
import { blobValidate } from '@/utils/ruoyi'
import { saveAs } from 'file-saver'
import moment from 'moment'

// 是否显示重新登录
let isReloadLoginShow

// 创建 axios 实例
const request = axios.create({
  // API 请求的默认前缀
  baseURL: process.env.VUE_APP_BASE_API,
  // baseURL: 'https://ruoyi.setworld.net',
  timeout: 6000 // 请求超时时间
})

// 异常拦截处理器
const errorHandler = (error) => {
  console.log('err' + error)
    let { message } = error
  if (message === 'Network Error') {
    message = '后端接口连接异常'
  } else if (message.includes('timeout')) {
    message = '系统接口请求超时'
  } else if (message.includes('Request failed with status code')) {
    message = '网络请求错误，请稍后再试'
  } else if (message.includes('Request aborted')) {
    message = '请求已中断，请刷新页面'
  }
    notification.error({
      message: '错误',
      description: message
    }, 5000)
    return Promise.reject(error)
}

// request interceptor
request.interceptors.request.use(config => {
  const token = storage.get(ACCESS_TOKEN) || ''
  const userId = storage.get(USER_ID) || ''
  const userName = storage.get(USER_NAME) || ''
  const tenantId = storage.get(TENANT_ID) || ''
  const lang = storage.get(LANG) || ''
  // 如果 token 存在
  // 让每个请求携带自定义 token 请根据实际情况自行修改
  if (token) {
    config.headers['Authorization'] = 'Bearer ' + token // 让每个请求携带自定义token 请根据实际情况自行修改
    config.headers['User-Id'] = userId
    config.headers['User-Name'] = userName
    config.headers['Tenant-Id'] = tenantId
  }
  config.headers['Service-Gray'] = 'true'
  config.headers['Lang'] = lang
  config.headers['Trace-Id'] = userId + moment().valueOf()
  // 处理params参数
  if (config.params) {
    const url = config.url + '?' + qs.stringify(config.params, { indices: false })
    config.params = {}
    config.url = url
  }
  return config
}, errorHandler)

// response interceptor
request.interceptors.response.use((res) => {
  // 未设置状态码则默认成功状态
  const code = res.data.code || 'OK'
  // 获取错误信息
  const msg = errorCode[code] || res.data.msg || errorCode['default']
  // 二进制数据则直接返回
  if (code === 'OK' || res.request.responseType === 'blob' || res.request.responseType === 'arraybuffer') {
    return res.data
  }
  if (code === 'Unauthorized') {
    if (!isReloadLoginShow) {
      isReloadLoginShow = true
      notification.open({
        message: '系统提示',
        description: msg,
        btn: h => {
          return h(
            'a-button',
            {
              props: {
                type: 'primary',
                size: 'small'
              },
              on: {
                click: () => {
                  store.dispatch('Logout').then(() => {
                    isReloadLoginShow = false
                    location.href = '/'
                  })
                }
              }
            },
            '确认'
          )
        },
        duration: 0,
        onClose: () => {
          isReloadLoginShow = false
        }
      })
    }
  } else {
    notification.error({
      message: '错误',
      description: msg
    }, 5000)
  }
  return Promise.reject(msg)
}, errorHandler)

const installer = {
  vm: {},
  install (Vue) {
    Vue.use(VueAxios, request)
  }
}

// 通用下载方法
export function download (url, params, filename) {
  const notificationKey = 'download'
  notification.open({
    key: notificationKey,
    message: '正在下载数据，请稍候',
    duration: null,
    icon: h => {
      return h(
        'a-icon',
        {
          props: {
            type: 'loading'
          }
        }
      )
      }
  })
  return request.post(url, params, {
    transformRequest: [(params) => { return qs.stringify(params, { indices: false }) }],
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    responseType: 'blob'
  }).then(async (data) => {
    const isLogin = await blobValidate(data)
    if (isLogin) {
      const blob = new Blob([data])
      saveAs(blob, filename)
      message.success('下载成功')
    } else {
      const resText = await data.text()
      const rspObj = JSON.parse(resText)
      const errMsg = errorCode[rspObj.code] || rspObj.msg || errorCode['default']
      message.error(errMsg)
    }
    notification.close(notificationKey)
  }).catch(() => {
    message.error('下载文件出现错误，请联系管理员！')
    notification.close(notificationKey)
  })
}

export default request

export {
  installer as VueAxios,
  request as axios
}
