import request from '@/utils/noToken'
// console.log(mockUrl)
// 获取直播列表
export function getLivelist() {
    return request({
        url:'/student/getLiveList',
        method: 'post',
    })
}

// 获取热门课程列表
export function getHotSubjectList() {
    return request({
        url: '/student/getHotSubjectList',
        method: 'post'
    })
}

/**
 * 获取直播预告列表
 */
export function getPreLiveList(params) {
    return request({
        url: '/student/getPreLiveList',
        method: 'post',
        data: params
    })
}

// 获取课程列表
export function getSubjectList(params){
    return request({
        url: '/student/getSubjectList',
        method: 'post',
        data: params
    })
}



// 获取名师风采列表
export function getTeacherList(params) {
    return request({
        url:  '/student/getTeacherList',
        method: 'post',
        data: params
    })
}
