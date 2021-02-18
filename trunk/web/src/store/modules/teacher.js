import * as teacherService from '@/api/teacher/index'


const teacher = {
    state: {
        
    },

    mutations: {

    },

    actions: {
        /**
         *老师创建直播课程
         */
        SaveSubject({ commit },params) {
            return new Promise((resolve, reject) => {
                teacherService.saveSubject(params).then(res => {
                    resolve(res)
                }).catch(error => {
                    reject(error)
                })
            })
        },
        /**
         * 获取课程信息列表
         */
        GetSubjectList({ commit },params) {
            return new Promise((resolve, reject) => {
                teacherService.getSubjectList(params).then(res => {

                    resolve(res)
                }).catch(error => {
                    reject(error)
                })
            })
        },
        /**
         * 获取课程详细信息
         */
        GetSubjectDetail({ commit }, params) {
            return new Promise((resolve, reject) => {
                teacherService.getSubjectDetail(params).then(res => {
                    
                    resolve(res)
                }).catch(error => {
                    reject(error)
                })
            })
        },
        /**
         * 更新课程状态
         */
        ChangeStatus({ commit }, params) {
            return new Promise((resolve, reject) => {
                teacherService.changeStatus(params).then(res => {
                    resolve(res)
                }).catch(error => {
                    reject(error)
                })
            })
        },
        /**
         * 课程评价信息列表
         */
        GetEvaluateDetail({ commit }, params) {
            return new Promise((resolve, reject) => {
                teacherService.getEvaluateDetail(params).then(res => {
                    resolve(res)
                }).catch(error => {
                    reject(error)
                })
            })
        },
        /**
         * 获取个人信息
         */
        GetUserInfo({ commit }) { 
            return new Promise((resolve, reject) => {
                teacherService.getUserInfo().then(res => {
                    resolve(res)
                }).catch(error => {
                    reject(error)
                })
            })
        },
        /**
         *获取留言列表
         */
        GetLeavemessage({ commit }, params) {
            return new Promise((resolve, reject) => {
                teacherService.getLeavemessage(params).then(res => {
                    resolve(res)
                }).catch(error => {
                    reject(error)
                })
            })
        },
         /**
         *获取留言列表
         */
        SaveMessage({ commit }, params) {
            return new Promise((resolve, reject) => {
                teacherService.saveMessage(params).then(res => {
                    resolve(res)
                }).catch(error => {
                    reject(error)
                })
            })
        },
    }
}

export default teacher
