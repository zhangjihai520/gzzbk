import * as recruitService from '@/api/recruit/index'


const recruit = {
    state: {
    },

    mutations: {
    },

    actions: {
        /**
         * 查询学生列表-查看核验结果（中招科、教体局）
         * 使用页面：查询学生列表-查看核验结果
         */
        GetStudentInfoList({ commit }, params) {
            return new Promise((resolve, reject) => {
                recruitService.getStudentInfoList(params).then(res => {
                    resolve(res)
                }).catch(error => {
                    reject(error)
                })
            })
        },
        /**
         * 删除学生信息（教体局）
         * 使用页面：查看核验结果
         */
        DeleteStudentInfo({ commit }, params) {
            return new Promise((resolve, reject) => {
                recruitService.deleteStudentInfo(params).then(res => {
                    resolve(res)
                }).catch(error => {
                    reject(error)
                })
            })
        },
        /**
         * 查询所有学校招生计划
         * 使用页面：招生计划（中招科）
         */
        GetPlanList({ commit }, params) {
            return new Promise((resolve, reject) => {
                recruitService.getPlanList(params).then(res => {
                    resolve(res)
                }).catch(error => {
                    reject(error)
                })
            })
        },
        /**
         * 获取所有学校报名情况（中招科）
         * 使用页面：报名情况（中招科）
         */
        GetFillSummaryList({ commit }, params) {
            return new Promise((resolve, reject) => {
                recruitService.getFillSummaryList(params).then(res => {
                    resolve(res)
                }).catch(error => {
                    reject(error)
                })
            })
        },
        /**
         * 根据学校获取学生填报信息（中招科）
         * 使用页面：报名情况（中招科）
         */
        GetFillListBySchoolId({ commit }, params) {
            return new Promise((resolve, reject) => {
                recruitService.getFillListBySchoolId(params).then(res => {
                    resolve(res)
                }).catch(error => {
                    reject(error)
                })
            })
        },
        /**
         * 获取录取结果列表（中招科）
         * 使用页面：录取结果（中招科）
         */
        GetRecruitAdmissionResultList({ commit }, params) {
            return new Promise((resolve, reject) => {
                recruitService.getAdmissionResultList(params).then(res => {
                    resolve(res)
                }).catch(error => {
                    reject(error)
                })
            })
        }
    }
}

export default recruit
