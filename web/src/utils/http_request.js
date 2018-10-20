import axios from 'axios'
import {Message} from 'element-ui'
import Cookies from 'js-cookie'

/**
 * [创建axios 实例]
 * @type {[type]}
 */
const service = axios.create({
  baseURL: process.env.BASE_API,
  timeout: 10000
})

/**
 * [统一拦截请求]
 * @param  {[type]} config [description]
 * @return {[type]}        [description]
 */
service.interceptors.request.use(config => {
  config.headers['accessToken'] = Cookies.get('accessToken') || null
  return config
}, error => {
  console.log(error) // 打印测试
  Promise.reject(error)
})
/**
 * [统一拦截响应]
 * @param  {[type]} response [description]
 * @return {[type]}          [description]
 */
service.interceptors.response.use(
  response => response,
  error => {
    console.log('error' + error) // 打印测试
    Message({
      message: error.message,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)

export default service
