import request from '@/utils/http_request'

export const getEventRule = data => {
  return request({
    url: `/eventRule?pageNo=${data.pageNo}&pageSize=${data.pageSize}&schemasName=${data.schemasName}&eventType=${data.eventType}&tName=${data.tName}`,
    method: 'get'
  }).then(res => res.data)
}

export const addEventRule = data => {
  return request({
    url: `/eventRule`,
    method: 'post',
    data: data,
    headers: {
      'Content-type': 'application/json;charset=UTF-8'
    }
  }).then(res => res.data)
}

export const updateEventRule = data => {
  return request({
    url: `/eventRule`,
    method: 'put',
    data: data,
    headers: {
      'Content-type': 'application/json;charset=UTF-8'
    }
  }).then(res => res.data)
}

export const delEventRule = data => {
  return request({
    url: `/eventRule`,
    method: 'delete',
    data: data,
    headers: {
      'Content-type': 'application/json;charset=UTF-8'
    }
  }).then(res => res.data)
}
