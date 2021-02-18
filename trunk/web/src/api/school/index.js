import request from '@/utils/request'
/**
 *获取课程信息列表
 */
export function getSubjectList(params) {
    return request({
        url: '/school/getSubjectList',
        method: 'post',
        data:params
    })
}
/**
 *获取待办课程
 */
export function getToDoSubjectList(params) {
    return request({
        url: '/school/getToDoSubjectList',
        method: 'post',
        data:params
    })
}

/**
 *管理员创建/更新录播课程
 */
export function saveSubject(params) {
    return request({
        url: 'school/saveSubject',
        method: 'post',
        data:params
    })
}

/**
 获取课程详细信息
 */
export function getSubjectDetail(params) {
    return request({
        url: '/school/getSubjectDetail',
        method: 'post',
        data:params
    })
}

/**
 *更新课程状态（上架、下架）
 */
export function changeStatus(params) {
    return request({
        url: '/school/changeStatus',
        method: 'post',
        data:params
    })
}

/**
 *获取学校教师下拉选项
 */
export function teacherOptions() {
    return request({
        url: '/school/teacherOptions',
        method: 'post',
    })
}