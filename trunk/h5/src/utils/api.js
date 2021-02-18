import request from './request';


//权限验证、登录
/**
 * 验证码
 * 
 */
export function getCodeImg() {
  return request({
      url:'/captchaImage',
      method: 'get',
  })
}
// 登录
export function login(params) {
  return request({
      url:'/login',
      method: 'post',
      data: params
  })
}

//获取首页banners
export function banners() {
  return request({
      url:'/busscommon/banners',
      method: 'post',
  })
}

//获取直播列表
export function getLiveList() {
  return request({
      url:'/student/getLiveList',
      method: 'post',
  })
}
//热门课程列表
export function getHotSubjectList() {
  return request({
      url:'/student/getHotSubjectList',
      method: 'post',
  })
}
//直播预告列表
export function getPreLiveList(params) {
  return request({
      url:'/student/getPreLiveList',
      method: 'post',
      data: params
  })
}
//年级下拉
export function gradeOptions() {
  return request({
      url:'/student/gradeOptions',
      method: 'post',
  })
}
//学科下拉
export function courseOptions() {
  return request({
      url:'/student/courseOptions',
      method: 'post',
  })
}
//报名
export function registSubject(params) {
  return request({
      url:'/student/registSubject',
      method: 'post',
      data: params
  })
}
//课程列表
export function getSubjectList(params) {
  return request({
      url:'/student/getSubjectList',
      method: 'post',
      data: params
  })
}
 //课程详情
export function getSubjectDetail(params) {
  return request({
      url:'/student/getSubjectDetail',
      method: 'post',
      data: params
  })
}
//学生信息获取
export function getUserInfo() {
  return request({
      url:'/student/getUserInfo',
      method: 'post',
  })
}
//我的课程列表
export function mySubjectList(params) {
  return request({
      url:'/student/mySubjectList',
      method: 'post',
      data: params
  })
}
//获取留言列表
export function getLeavemessage(params) {
  return request({
      url:'/student/getLeavemessage',
      method: 'post',
      data: params
  })
}
 //课程留言
export function leavemessage(params) {
  return request({
      url:'/student/leavemessage',
      method: 'post',
      data: params
  })
}
//评价
export function evaluateSubject(params) {
  return request({
      url:'/student/evaluateSubject',
      method: 'post',
      data: params
  })
}
// export const getCodeImg = () => request('get', '/api/captchaImage', {});


// export const login = (params) => request('post', '/api/login', params);

// 
// export const banners = () => request('post', '/api/busscommon/banners', {});

// 
// export const getLiveList = () => request('post', '/api/student/getLiveList', {});

// //热门课程列表
// export const getHotSubjectList = () => request('post', '/api/student/getHotSubjectList', {});

// 
// export const getPreLiveList = (params) => request('post', '/api/student/getPreLiveList', params);

// 
// export const gradeOptions = () => request('post', '/api/busscommon/gradeOptions', {});

// 
// export const courseOptions = () => request('post', '/api/busscommon/courseOptions', {});

// 
// export const registSubject = (params) => request('post', '/api/student/registSubject', params);

// 
// export const getSubjectList = (params) => request('post', '/api/student/getSubjectList', params);

//
// export const getSubjectDetail = (params) => request('post', '/api/student/getSubjectDetail', params);

// 
// export const evaluateSubject = (params) => request('post', '/api/student/evaluateSubject', params);

// 
// export const getUserInfo = () => request('post', '/api/student/getUserInfo', {});

// 
// export const mySubjectList = (params) => request('post', '/api/student/mySubjectList', params);

// 
// export const getLeavemessage = (params) => request('post', '/api/student/getLeavemessage', params);

//
// export const leavemessage = (params) => request('post', '/api/student/leavemessage', params);