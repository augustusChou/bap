import request from '@/utils/http_request'

export const getEventHandlerJsScriptTemplate = () => {
  return request({
    url: `/eventOperation`,
    method: 'get'
  }).then(res => res.data)
}

export const verifyScript = data => {
  return request({
    url: `/eventOperation/verifyScript`,
    method: 'post',
    data: data,
    headers: {
      'Content-type': 'application/json;charset=UTF-8'
    }
  }).then(res => res.data)
}

export const addEventOperation = data => {
  return request({
    url: `/eventOperation`,
    method: 'post',
    data: data,
    headers: {
      'Content-type': 'application/json;charset=UTF-8'
    }
  }).then(res => res.data)
}

export const updateEventOperation = data => {
  return request({
    url: `/eventOperation`,
    method: 'put',
    data: data,
    headers: {
      'Content-type': 'application/json;charset=UTF-8'
    }
  }).then(res => res.data)
}

export const delEventOperation = data => {
  return request({
    url: `/eventOperation`,
    method: 'delete',
    data: data,
    headers: {
      'Content-type': 'application/json;charset=UTF-8'
    }
  }).then(res => res.data)
}
