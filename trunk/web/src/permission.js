import router from './router'
import store from './store'
import { Message } from 'element-ui'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import { getToken, getUserType, setSchoolId, removeSchoolId, setStudentPath, getIsLogin, setIsLogin } from '@/utils/auth'

NProgress.configure({ showSpinner: false })

// const whiteList = ['/login', '/auth-redirect', '/bind', '/register']
const whiteList = ['/login', '/student/index', '/student/live_course', '/student/course_contant', '/student/inLive_teacher']

router.beforeEach((to, from, next) => {
  NProgress.start()
  if (getToken(to.path)) {

    /* has token*/
    if (getUserType() == 1) {
      next();
      NProgress.done()
    } else if (getUserType() == 2) {
      store.dispatch("GetUserInfo").then(res => {
        if (res.code == 200) {
          setSchoolId(res.data.schoolId);
          next();
        }
      })
      NProgress.done()
    } else {
      if (store.getters.roles.length === 0) {
        // 判断当前用户是否已拉取完user_info信息
        store.dispatch('GetInfo').then(res => {
          // 拉取user_info
          const roles = res.roles
          store.dispatch('GenerateRoutes', { roles }).then(accessRoutes => {
            // 测试 默认静态页面
            // store.dispatch('permission/generateRoutes', { roles }).then(accessRoutes => {
            // 根据roles权限生成可访问的路由表
            router.addRoutes(accessRoutes) // 动态添加可访问路由表
            next({ ...to, replace: true }) // hack方法 确保addRoutes已完成
          })
        })
          .catch(err => {
            store.dispatch('FedLogOut').then(() => {
              Message.error(err)
              // next({ path: '/' })
            })
          })
      } else {
        next()
        // 没有动态改变权限的需求可直接next() 删除下方权限判断 ↓
        // if (hasPermission(store.getters.roles, to.meta.roles)) {
        //   next()
        // } else {
        //   next({ path: '/401', replace: true, query: { noGoBack: true }})
        // }
        // 可删 ↑
      }
    }
  } else {
    // 没有token
    if (whiteList.indexOf(to.path) !== -1) {
      // 在免登录白名单，直接进入
      next()
    } else {
      if (getIsLogin()) {
        next(`/login`)
      } else {
        next(`/student/index`);
        setIsLogin('false');
      }
      setStudentPath("/student/index")
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  NProgress.done()
})
