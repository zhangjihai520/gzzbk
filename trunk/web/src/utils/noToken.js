import axios from 'axios'
import { Notification, MessageBox, Message } from 'element-ui'
import store from '@/store'
import { getToken, getHeaders } from '@/utils/auth'
import errorCode from '@/utils/errorCode'

axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8'
// 创建axios实例
const service = axios.create({
  // axios中请求配置有baseURL选项，表示请求URL公共部分
  baseURL: process.env.VUE_APP_BASE_API,
  // 超时
  timeout: 0
})
// request拦截器
service.interceptors.request.use(config => {
  // 是否需要设置 token
 return config
}, error => {
  Promise.reject(error)
})

// 响应拦截器
service.interceptors.response.use(res => {
  // 未设置状态码则默认成功状态
  const code = res.data.code || 200;
  // 获取错误信息
  const message = errorCode[code] || res.data.msg || errorCode['default']
  if (code === 401) {
    Message({
      message: "用户信息已过期，请重新登录",
      type: 'error'
    })
    setTimeout(() => {
      store.dispatch('removeLoad').then(() => {
        location.reload()
      })
    }, 1500)
  } else if (code === 500) {
    Message({
      message: message,
      type: 'error'
    })
    return Promise.reject(new Error(message))
  } else if (code !== 200) {
    Notification.error({
      title: message
    })
    return Promise.reject('error')
  } else {
    return res.data
  }
},
  error => {
    Message({
      message: error.message,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)

export default service
