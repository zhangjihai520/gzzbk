import axios from 'axios';

import { getToken, getHeaders } from './cookie'
// import store from '@/store';

axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8'
let $axios = axios.create();
// 创建axios实例
const service = axios.create({
  // axios中请求配置有baseURL选项，表示请求URL公共部分
  baseURL: "http://192.168.50.17:8080/api",
  // 超时
  timeout: 0
})
// request拦截器
service.interceptors.request.use(config => {
  // 是否需要设置 token
  const isToken = (config.headers || {}).isToken === false
  if (getToken() && !isToken) {
    //config.headers['Authorization'] = 'Bearer ' + getToken() // 让每个请求携带自定义token 请根据实际情况自行修改
    config.headers = { ...config.headers, ...getHeaders() }
  }
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

    // Message({
    //   message: "用户信息已过期，请重新登录",
    //   type: 'error'
    // })
    // setTimeout(() => {
    //   store.dispatch('removeLoad').then(() => {
    //     location.reload()
    //   })
    // }, 1500)

  } else if (code === 500) {
    // Message({
    //   message: message,
    //   type: 'error'
    // })
    return Promise.reject(new Error(message))
  } else if (code !== 200) {
    // Notification.error({
    //   title: message
    // })
    return Promise.reject('error')
  } else {
    return res.data
  }
},
  error => {
    console.log(error)
    // console.log(error.response.status)
    // Message({
    //   message: error.message,
    //   type: 'error',
    //   duration: 5 * 1000
    // })
    if (error.response.status == 401) {
      // setTimeout(() => {
      //   store.dispatch('removeLoad').then(() => {
      //     location.reload()
      //   })
      // }, 1500)
    }
    return Promise.reject(error)
  }
)
export default service



// export default function request(method, url, data) {

//   return new Promise((reslove, reject) => {
//     $axios({
//       method,
//       url,
//       data: data,

//     }).then(res => {
//       let body = res.data
//       if (res.status == 200) {

//         reslove(body);
//       } else {

//         console.log('请求异常！');
//         reject(body);
//       }
//     }).catch(err => {
//       console.log(err)
//       console.log('请求异常！111111');
//       reject(err);
//     });
//   })
// }