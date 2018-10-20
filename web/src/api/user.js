import request from '@/utils/http_request'

export const tryLogin = data => {
  return request({
    url: `/user/login`,
    method: 'post',
    data: data,
    headers: {
      'Content-type': 'application/json;charset=UTF-8'
    }
  }).then(res => res.data)
}

export const logout = sid => {
  return request({
    url: `/user/logout?sid=${sid}`,
    method: 'post'
  }).then(res => res.data)
}
