import request from '@/utils/request'
/**
 * 获取用户信息
 */
export function getUserInfo() {
    return request({
        url:'/student/getUserInfo',
        method: 'POST',
    })
}

//年级下拉
export function gradeOptions(){
    return request({
        url:'/busscommon/gradeOptions',
        method:'POST'
    })
}

//学科下拉
export function courseOptions(){
    return request({
        url:'/busscommon/courseOptions',
        method:'POST'
    })
}

//获取教师下拉
export function teacherOptions(){
    return request({
        url:'/busscommon/teacherOptions',
        method:'POST'
    })
}
//获取学校下拉选项
export function schoolOptions(){
    return request({
        url:'/busscommon/schoolOptions',
        method:'POST'
    })
}
//首页导航
export function banners(){
    return request({
        url:'/busscommon/banners',
        method:'POST'
    })
}
