import request from '@/utils/http_request'

/**
 * 获取功能配置列表
 * @param pageNo 页码
 * @param pageSize 页面大小
 * @returns {PromiseLike<T | never> | Promise<T | never> | *}
 */
export const getFuncConfig = (pageNo, pageSize) => {
  return request({
    url: `/funcConfig?pageNo=${pageNo}&pageSize=${pageSize}`,
    method: 'get'
  }).then(res => res.data)
}

export const addFuncConfig = data => {
  return request({
    url: `/funcConfig`,
    method: 'post',
    data: data,
    headers: {
      'Content-type': 'application/json;charset=UTF-8'
    }
  }).then(res => res.data)
}

export const updateFuncConfig = data => {
  return request({
    url: `/funcConfig`,
    method: 'put',
    data: data,
    headers: {
      'Content-type': 'application/json;charset=UTF-8'
    }
  }).then(res => res.data)
}

export const delFuncConfig = data => {
  return request({
    url: `/funcConfig`,
    method: 'delete',
    data: data,
    headers: {
      'Content-type': 'application/json;charset=UTF-8'
    }
  }).then(res => res.data)
}
