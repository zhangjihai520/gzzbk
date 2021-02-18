import request from '@/utils/request'
const mockUrl = process.env.VUE_APP_MOCK_API;

/**
 * 查询学生列表-查看核验结果（中招科、教体局）
 * 使用页面：查看核验结果
 */
export function getStudentInfoList(params) {
    return request({
        url: mockUrl + '/recruit/getStudentInfoList',
        method: 'post',
        data: params
    })
}

/**
 * 删除学生信息（教体局）
 * 使用页面：查看核验结果
 */
export function deleteStudentInfo(params) {
    return request({
        url: mockUrl + '/recruit/deleteStudentInfo',
        method: 'post',
        data: params
    })
}

/**
 * 查询所有学校招生计划
 * 使用页面：招生计划（中招科）
 */
export function getPlanList(params) {
    return request({
        url: mockUrl + '/recruit/getPlanList',
        method: 'post',
        data: params
    })
}

/**
 * 获取所有学校报名情况（中招科）
 * 使用页面：报名情况（中招科）
 */
export function getFillSummaryList(params) {
    return request({
        url: mockUrl + '/recruit/getFillSummaryList',
        method: 'post',
        data: params
    })
}

/**
 * 根据学校获取学生填报信息（中招科）
 * 使用页面：报名情况（中招科）
 */
export function getFillListBySchoolId(params) {
    return request({
        url: mockUrl + '/recruit/getFillListBySchoolId',
        method: 'post',
        data: params
    })
}

/**
 * 获取录取结果列表（中招科）
 * 使用页面：录取结果（中招科）
 */
export function getAdmissionResultList(params) {
    return request({
        url: mockUrl + '/recruit/getAdmissionResultList',
        method: 'post',
        data: params
    })
}


