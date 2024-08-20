import request from '@/utils/request'

export function login(data) {
  return request({
    url: 'http://localhost:8090/dev/user/login',
    method: 'post',
    data
  })
}

export function getInfo(token) {
  return request({
    url: 'http://localhost:8090/dev/user/info',
    method: 'get',
    params: { token }
  })
}

export function logout() {
  return request({
    url: '/vue-element-admin/user/logout',
    method: 'post'
  })
}

// 获取用户列表
export function getUsers(data){
  let {name,minCreateTime,maxCreateTime}=data;
  return request({
    url: 'http://localhost:8090/dev/user/users/list',
    method: 'get',
    params: { name,minCreateTime,maxCreateTime }
  })
}

// 删除用户
export function deleteUser(id){
  return request({
    url:`http://localhost:8090/dev/user/${id}`,
    method:'delete',
    params: { id }
  })
}

// 增加用户
export function addUser(data){
  return request({
    url: 'http://localhost:8090/dev/user/',
    method: 'post',
    data
  })
}

// 更改用户信息
export function updateUser(data){
  let id=data.id;
  return request({
    url: `http://localhost:8090/dev/user/update/${id}`,
    method: 'post',
    data
  })
}
