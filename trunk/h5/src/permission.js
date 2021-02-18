import router from './router'
import {getToken} from '@/utils/cookie'
import {Toast} from 'mint-ui'

const whiteList = ['/login','/index','/course','/liveCourse']
router.beforeEach((to,from,next) =>{
    if(to.meta.title){
      document.title=to.meta.title
      next()
    }
    if (getToken(to.path)) {
      next()
    }else{
      if (whiteList.indexOf(to.path) !== -1){
        // 在免登录白名单，直接进入
        next()
      }else{
        next(`/login`)
      }
    }
})