import * as studentService from '@/api/student/hasToken'
const student = {
    state: {
        
    },

    mutations: {
       
    },

    actions: {
        /**
         *获取课程详细信息
         */
        GetSubjectDetail({ commit }, params) {
            return new Promise((resolve, reject) => {
                studentService.getSubjectDetail(params).then(res => {
                    resolve(res)
                }).catch(error => {
                    reject(error)
                })
            })
        },

        /**
         * 获取教师详细信息
         */
        GetTeacherDetail({ commit }, params) {
            return new Promise((resolve, reject) => {
                studentService.getTeacherDetail(params).then(res => {
                    resolve(res)
                }).catch(error => {
                    reject(error)
                })
            })
        },
        /**
        * 获取我的课程列表
        */
        GetMySubjectList({ commit }, params) {
            return new Promise((resolve, reject) => {
                studentService.getMySubjectList(params).then(res => {
                    resolve(res)
                }).catch(error => {
                    reject(error)
                })
            })
        },
        /**
       * 评价课程信息
       */
        EvaluateSubject({ commit }, params) {
            return new Promise((resolve, reject) => {
                studentService.evaluateSubject(params).then(res => {
                    resolve(res)
                }).catch(error => {
                    reject(error)
                })
            })
        },
        /**
       * 学生课程报名
       */
        RegistSubject({ commit }, params) {
            return new Promise((resolve, reject) => {
                studentService.registSubject(params).then(res => {
                    resolve(res)
                }).catch(error => {
                    reject(error)
                })
            })
        },

        /**
       * 课程留言
       */
        Leavemessage({ commit }, params) {
            return new Promise((resolve, reject) => {
                studentService.leavemessage(params).then(res => {
                    resolve(res)
                }).catch(error => {
                    reject(error)
                })
            })
        },
    }
}

export default student
