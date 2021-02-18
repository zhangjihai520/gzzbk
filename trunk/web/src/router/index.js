import Vue from 'vue'
import Router from 'vue-router'
import { getUserType } from '@/utils/auth'
Vue.use(Router)

/* Layout */
import Layout from '@/layout'
// import ParentView from '@/components/ParentView';

import live_course from '@/views/student/live_course'
import course_contant from '@/views/student/course_contant'
import inLive_teacher from '@/views/student/inLive_teacher'
import student_info from '@/views/student/student_info'
/**
 * Note: 路由配置项
 *
 * hidden: true                   // 当设置 true 的时候该路由不会再侧边栏出现 如401，login等页面，或者如一些编辑页面/edit/1
 * alwaysShow: true               // 当你一个路由下面的 children 声明的路由大于1个时，自动会变成嵌套的模式--如组件页面
 *                                // 只有一个时，会将那个子路由当做根路由显示在侧边栏--如引导页面
 *                                // 若你想不管路由下面的 children 声明的个数都显示你的根路由
 *                                // 你可以设置 alwaysShow: true，这样它就会忽略之前定义的规则，一直显示根路由
 * redirect: noRedirect           // 当设置 noRedirect 的时候该路由在面包屑导航中不可被点击
 * name:'router-name'             // 设定路由的名字，一定要填写不然使用<keep-alive>时会出现各种问题
 * meta : {
    noCache: true                // 如果设置为true，则不会被 <keep-alive> 缓存(默认 false)
    title: 'title'               // 设置该路由在侧边栏和面包屑中展示的名字
    icon: 'svg-name'             // 设置该路由的图标，对应路径src/assets/icons/svg
    breadcrumb: false            // 如果设置为false，则不会在breadcrumb面包屑中显示
  }
 */

// 公共路由
export const adminRoutes = [
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path(.*)',
        component: (resolve) => require(['@/views/redirect'], resolve)
      }
    ]
  },
  {
    path: '/login',
    component: (resolve) => require(['@/views/login'], resolve),
    hidden: true
  },
  {
    path: '/404',
    component: (resolve) => require(['@/views/error/404'], resolve),
    hidden: true
  },
  {
    path: '/401',
    component: (resolve) => require(['@/views/error/401'], resolve),
    hidden: true
  },
  {
    path: '',
    component: Layout,
    redirect: 'index',
    children: [
      {
        path: 'index',
        component: (resolve) => require(['@/views/index'], resolve),
        name: '首页',
        meta: { title: '首页', icon: 'dashboard', noCache: true, affix: true }
      }
    ]
  },
  {
    path: '/user',
    component: Layout,
    hidden: true,
    redirect: 'noredirect',
    children: [
      {
        path: 'profile',
        component: (resolve) => require(['@/views/system/user/profile/index'], resolve),
        name: 'Profile',
        meta: { title: '个人中心', icon: 'user' }
      }
    ]
  },
  {
    path: '/dict',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'type/data/:dictId(\\d+)',
        component: (resolve) => require(['@/views/system/dict/index'], resolve),
        name: 'Data',
        meta: { title: '字典数据', icon: '' }
      }
    ]
  },
  {
    path: '/job',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'log',
        component: (resolve) => require(['@/views/monitor/job/log'], resolve),
        name: 'JobLog',
        meta: { title: '调度日志' }
      }
    ]
  },
  {
    path: '/notice',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'notice',
        component: (resolve) => require(['@/views/system/notice/index'], resolve),
        name: 'Notice',
        meta: { title: '调度日志' }
      }
    ]
  },
  {
    path: '/gen',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'edit/:tableId(\\d+)',
        component: (resolve) => require(['@/views/tool/gen/editTable'], resolve),
        name: 'GenEdit',
        meta: { title: '修改生成配置' }
      }
    ]
  }
]

export const studentRoutes = [
  // {
  //   path: '/',
  //   redirect: '/student/index',
  //   component: resolve => require(['@/views/student/index'], resolve)
  // },
  {
    path: '/student/index',
    name: 'stuIndex',
    component: resolve => require(['@/views/student/index'], resolve)

  },
  {
    path: '/student/live_course',
    name: 'live_course',
    component: live_course
  },
  {
    path: '/student/course_contant',
    name: 'course_contant',
    component: course_contant
  },
  {
    path: '/student/inLive_teacher',
    name: 'inLive_teacher',
    component: inLive_teacher,
  },
  {
    path: '/student/student_info',
    name: 'student_info',
    component: student_info
  },
  {
    path: '/student/teacher_info',
    component: (reslove) => require(['@/views/student/teacher_info'], reslove)
  },
  {
    path: '/student/inLive_detail',
    component: resolve => require(['@/views/student/inLive_detail'], resolve)
  },
  {
    path: '/student/inLive_course',
    component: resolve => require(['@/views/student/inLive_course'], resolve)
  },
]
export const teacherRoutes = [
  {
    path: '/',
    redirect: '/teacher/index',
    component: resolve => require(['@/views/teacher/index'], resolve)
  },
  {
    path: '/teacher/index',
    name: 'teaIndex',
    component: (resolve) => require(['@/views/teacher/index'], resolve),
  },
  {
    path: '/teacher/evaluateManager',
    name: 'evaluateManager',
    component: (resolve) => require(['@/views/teacher/evaluateManager'], resolve),
  },
  {
    path: '/teacher/voiceManager',
    name: 'voiceManager',
    component: (resolve) => require(['@/views/teacher/voiceManager'], resolve),
  },
  {
    path: '/teacher/teacherManager',
    name: 'teacherManager',
    component: (resolve) => require(['@/views/teacher/teacherManager'], resolve),
  },

]



let routes = [];
routes = [...adminRoutes, ...studentRoutes,...teacherRoutes]
export const constantRoutes = routes;

export default new Router({
  mode: 'history', // 去掉url中的#
  scrollBehavior: () => ({ y: 0 }),
  routes: routes
})
