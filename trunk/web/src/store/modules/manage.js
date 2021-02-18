import * as manageService from '@/api/manage/index'


const manage = {
    state: {
    },

    mutations: {
    },

    actions: {

         /**
         * 获取待办课程
         */
        adGetToDoSubjectList({ commit }, params) {
            return new Promise((resolve, reject) => {
                manageService.getToDoSubjectList(params).then(res => {
                    resolve(res)
                }).catch(error => {
                    reject(error)
                })
            })
        },
          /**
         * 管理员创建/更新录播课程
         */
        adSaveSubject({ commit }, params) {
            return new Promise((resolve, reject) => {
                manageService.saveSubject(params).then(res => {
                    resolve(res)
                }).catch(error => {
                    reject(error)
                })
            })
        },
         /**
         * 管理员创建/更新录播课程
         */
        adGetSubjectDetail({ commit }, params) {
            return new Promise((resolve, reject) => {
                manageService.getSubjectDetail(params).then(res => {
                    resolve(res)
                }).catch(error => {
                    reject(error)
                })
            })
        },
    }
}

export default manage
