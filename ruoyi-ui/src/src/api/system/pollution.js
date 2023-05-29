import request from '@/utils/request'

// 查询【请填写功能名称】列表
export function listPollution(query) {
  return request({
    url: '/system/pollution/list',
    method: 'get',
    params: query
  })
}

export function cluster(query) {
  return request({
    url: '/system/pollution/cluster',
    method: 'get',
    params: query
  })
}

export function craw(query) {
  return request({
    url: '/system/pollution/craw',
    method: 'get',
    params: query
  })
}

export function SSC(query) {
  return request({
    url: '/system/pollution/SSC',
    method: 'get',
    params: query
  })
}

export function SP(query) {
  return request({
    url: '/system/pollution/SP',
    method: 'get',
    params: query
  })
}

export function Rcut(query) {
  return request({
    url: '/system/pollution/Rcut',
    method: 'get',
    params: query
  })
}

export function Ncut(query) {
  return request({
    url: '/system/pollution/Ncut',
    method: 'get',
    params: query
  })
}


export function DBSCAN(query) {
  return request({
    url: '/system/pollution/DBSCAN',
    method: 'get',
    params: query
  })
}

export function KMeans(query) {
  return request({
    url: '/system/pollution/KMeans',
    method: 'get',
    params: query
  })
}

export function DP(query) {
  return request({
    url: '/system/pollution/DP',
    method: 'get',
    params: query
  })
}

export function CLUB(query) {
  return request({
    url: '/system/pollution/CLUB',
    method: 'get',
    params: query
  })
}

export function EPC(query) {
  return request({
    url: '/system/pollution/EPC',
    method: 'get',
    params: query
  })
}




// 查询【请填写功能名称】详细
export function getPollution(id) {
  return request({
    url: '/system/pollution/' + id,
    method: 'get'
  })
}

// 新增【请填写功能名称】
export function addPollution(data) {
  return request({
    url: '/system/pollution',
    method: 'post',
    data: data
  })
}

// 修改【请填写功能名称】
export function updatePollution(data) {
  return request({
    url: '/system/pollution',
    method: 'put',
    data: data
  })
}

// 删除【请填写功能名称】
export function delPollution(id) {
  return request({
    url: '/system/pollution/' + id,
    method: 'delete'
  })
}
