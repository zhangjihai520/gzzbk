import request from '@/utils/request'
/**
 *获取课程信息列表
 */
export function getSubjectList(params) {
    return request({
        url: '/manager/getSubjectList',
        method: 'post',
        data:params
    })
}
/**
 *获取待办课程
 */
export function getToDoSubjectList(params) {
    return request({
        url: '/manager/getToDoSubjectList',
        method: 'post',
        data:params
    })
}

/**
 *管理员创建/更新录播课程
 */
export function saveSubject(params) {
    return request({
        url: '/manager/saveSubject',
        method: 'post',
        data:params
    })
}

/**
 获取课程详细信息
 */
export function getSubjectDetail(params) {
    return request({
        url: '/manager/getSubjectDetail',
        method: 'post',
        data:params
    })
}

/**
 *更新课程状态（上架、下架）
 */
export function changeStatus(params) {
    return request({
        url: '/manager/changeStatus',
        method: 'post',
        data:params
    })
}

/**
 *获取banner列表
 */
export function getBannerList(params) {
    return request({
        url: '/manager/getBannerList',
        method: 'post',
        data:params
    })
}

/**
 *创建、修改banner
 */
export function saveBanner(params) {
    return request({
        url: '/manager/saveBanner',
        method: 'post',
        data:params
    })
}

/**
 *更新banner状态（上架、下架、删除）
 */
export function changeBannerStatus(params) {
    return request({
        url: '/manager/changeBannerStatus',
        method: 'post',
        data:params
    })
}

/**
 *banner详情
 */
export function getBannerDetail(params) {
    return request({
        url: '/manager/getBannerDetail',
        method: 'post',
        data:params
    })
}