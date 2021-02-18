import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

const routerPush = Router.prototype.push
Router.prototype.push = function push(location) {
  return routerPush.call(this, location).catch(error=> error)
}
 
export default new Router({
  routes: [
    {
      path: '/',
      name: 'Index',
      component: resolve => require(['@/views//index'], resolve),
      meta:{
        title:'首页',
        navShow:true
      }
    },
    {
      path: '/index',
      name: 'Index',
      component: resolve => require(['@/views/index'], resolve),
      meta:{
        title:'首页',
        navShow:true
      }
    },
    {
      path: '/course',
      name: 'Course',
      component: resolve => require(['@/views/course'], resolve),
      meta:{
        title:'课程',
        navShow:true
      }
    },
    {
      path: '/myself',
      name: 'Myself',
      component: resolve => require(['@/views/myself'], resolve),
      meta:{
        title:'我的',
        navShow:true
      }
    },
    {
      path: '/courseList',
      name: 'CourseList',
      component: resolve => require(['@/views/courseList'], resolve),
      meta:{
        title:'我的课程',
        navShow:false
      }
    },
    {
      path: '/liveCourse',
      name: 'LiveCourse',
      component: resolve => require(['@/views/liveCourse'], resolve),
      meta:{
        title:'直播课程',
        navShow:false
      }
    },
    {
      path: '/recordCourse',
      name: 'RecordCourse',
      component: resolve => require(['@/views/recordCourse'], resolve),
      meta:{
        title:'课程详情',
        navShow:false
      }
    },
    {
      path: '/login',
      name: 'Login',
      component: resolve => require(['@/views/login'], resolve),
      meta:{
        title:'登录',
        navShow:false
      }
    },
  ],
  mode: 'history', // 去掉url中的#
})
