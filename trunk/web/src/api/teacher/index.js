import request from '@/utils/request'


/**
 * 老师创建直播课程
 */
export function saveSubject(params) {
    return request({
        url:'/teacher/saveSubject',
        method: 'post',
        data:params
    })
}

/**
 *获取课程信息列表
 */
export function getSubjectList(params) {
    return request({
        url: '/teacher/getSubjectList',
        method: 'post',
        data:params
    })
}

/**
 *获取课程详细信息
 */
export function getSubjectDetail(params) {
    return request({
        url: '/teacher/getSubjectDetail',
        method: 'post',
        data: params
    })
}

/**
 *更新课程状态
 */
export function changeStatus(params) {
    return request({
        url:'/teacher/changeStatus',
        method: 'post',
        data: params
    })
}

/**
 * 课程评价信息列表
 */
export function getEvaluateDetail(params) {
    return request({
        url:'/teacher/getEvaluateDetail',
        method: 'post',
        data: params
    })
}

/**
 *获取个人信息
 */
export function getUserInfo() {
    return request({
        url:'/teacher/getUserInfo',
        method: 'post',
    })
}

/**
 * 修改个人信息
 */
export function updateUserInfo(params) {
    return request({
        url:'/teacher/updateUserInfo',
        method: 'post',
        data: params
    })
}
/**
 * 获取留言列表
 */
export function getLeavemessage(params) {
    return request({
        url:'/teacher/getLeavemessage',
        method: 'post',
        data: params
    })
}
/**
 * 回复留言信息
 */
export function saveMessage(params) {
    return request({
        url:'/teacher/saveMessage',
        method: 'post',
        data: params
    })
}

/**
 * 根据课程获取章节
 */
export function getChaptersBySubjectId(params) {
    return request({
        url:'/teacher/getChaptersBySubjectId',
        method: 'post',
        data: params
    })
}

/**
 * 更新留言状态
 */
export function changeLeaveMessageStatus(params) {
    return request({
        url:'/teacher/changeLeaveMessageStatus',
        method: 'post',
        data: params
    })
}
/**
 *更新评价状态
 */
export function changeEvaluateStatus(params) {
    return request({
        url:'/teacher/changeEvaluateStatus',
        method: 'post',
        data: params
    })
}

/**
 *获取授权号码
 */
export function getAuthCode(params) {
    return request({
        url:'/teacher/getAuthCode',
        method: 'post',
        data: params
    })
}
