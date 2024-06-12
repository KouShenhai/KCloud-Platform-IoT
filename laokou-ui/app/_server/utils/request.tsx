import axios from 'axios'
import serverToken from '@/app/_server/utils/getToken'


const https = require("https");
// 在 axios 请求时，选择性忽略 SSL
const agent = new https.Agent({
  rejectUnauthorized: false,
  keepAlive: true 
});

const request = axios.create({
  baseURL: process.env.APP_BASE_API, // url = base url + request url
  withCredentials: true, // send cookies when cross-domain requests
  timeout: 100000,
  httpsAgent: agent, 
  headers: {'Authorization': 'Basic OTVUeFNzVFBGQTN0RjEyVEJTTW1VVkswZGE6RnBId0lmdzR3WTkyZE8='}
})

// Add a request interceptor
request.interceptors.request.use(
  config => {
    
      
    config.headers['X-Token'] = serverToken()
     
    return config
  },
  error => {
    // do something with request error
    return Promise.reject(error)
  }
)

request.interceptors.response.use(

  response => {
    // do something with response data
    const res = response.data

    // if the custom code is not 20000, it is judged as an error.
    if (res.code !== 200) {
      // TODO: Message prompt
      console.error(res.message || 'Error')

      // 50008: Illegal token; 50012: Other clients logged in; 50014: Token expired;
      if (res.code === 500) {
        // TODO: to re-login
      }
      // reject
      return Promise.reject( res.message || 'Error')
    } else {
      return res
    }
  },
  error => {
    // do something with response error
    return Promise.reject(error)
  }
)

export default request
