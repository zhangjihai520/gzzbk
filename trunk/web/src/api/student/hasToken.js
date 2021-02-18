import request from '@/utils/request'
/**
 * 获取课程详细信息
 */
export function getSubjectDetail(params) {
  return request({
      url:'/student/getSubjectDetail',
      method: 'post',
      data: params
  })
}
// 获取教师详细信息
export function getTeacherDetail(params) {
  return request({
      url:'/student/getTeacherDetail',
      method: 'post',
      data: params
  })
}
// 获取我的课程列表
export function getMySubjectList(params) {
  return request({
      url:'/student/mySubjectList',
      method: 'post',
      data: params
  })
}

// 评价课程信息
export function evaluateSubject(params) {
  return request({
      url:'/student/evaluateSubject',
      method: 'post',
      data: params
  })
}

// 学生课程报名
export function registSubject(params) {
  return request({
      url:'/student/registSubject',
      method: 'post',
      data: params
  })
}
// 获取留言
export function getLeavemessage(params) {
  return request({
      url:'/student/getLeavemessage',
      method: 'post',
      data: params
  })
}
// 课程留言
export function leavemessage(params) {
  return request({
      url:'/student/leavemessage',
      method: 'post',
      data: params
  })
}

// 课程留言
export function moreSubjectList(params) {
  return request({
      url:'/student/moreSubjectList',
      method: 'post',
      data: params
  })
}