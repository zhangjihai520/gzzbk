import { login, logout, getInfo } from '@/api/login'
import { getToken, 
         setToken, 
         removeToken,
         setUserType, 
        //  getUserType,
         removeUserType,
         setStudentPath, 
         getStudentPath,
         removeStudentPath,
         setIsLogin,
         removeIsLogin } from '@/utils/auth'

const user = {
  state: {
    token: getToken(),
    name: '',
    avatar: '',
    roles: [],
    permissions: [],
    studentPath:getStudentPath()
  },

  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_NAME: (state, name) => {
      state.name = name
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
    },
    SET_PERMISSIONS: (state, permissions) => {
      state.permissions = permissions
    },

  },

  actions: {
    // 登录
    Login({ commit }, userInfo) {
      const username = userInfo.username.trim()
      const password = userInfo.password
      const code = userInfo.code
      const uuid = userInfo.uuid
      const loginType = userInfo.userType
      return new Promise((resolve, reject) => {
        login(username, password, code, uuid, loginType).then(res => {
          setToken(res.data.token);
          setUserType(res.data.userType)
          commit('SET_TOKEN', res.data.token);
          setStudentPath("/student/index");
          setIsLogin('false');
          resolve(res)
        }).catch(error => {
          reject(error)
        })
      })
    },
    // 获取用户信息
    GetInfo({ commit, state }) {
      return new Promise((resolve, reject) => {
        getInfo(state.token).then(res => {
          const user = res.user
          const avatar = user.avatar == "" ? require("@/assets/image/profile.jpg") : process.env.VUE_APP_BASE_API + user.avatar;
          if (res.roles && res.roles.length > 0) { // 验证返回的roles是否是一个非空数组
            commit('SET_ROLES', res.roles)
            commit('SET_PERMISSIONS', res.permissions)
          } else {
            commit('SET_ROLES', ['ROLE_DEFAULT'])
          }
          commit('SET_NAME', user.userName)
          commit('SET_AVATAR', avatar)
          resolve(res)
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 退出系统
    LogOut({ commit, state }) {
      return new Promise((resolve, reject) => {
        logout(state.token).then(() => {
          commit('SET_TOKEN', '');
          commit('SET_ROLES', []);
          commit('SET_PERMISSIONS', []);
          removeToken();
          removeUserType();
          removeStudentPath();
          setStudentPath('/student/index');
          removeIsLogin();
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 前端 登出
    FedLogOut({ commit }) {
      return new Promise(resolve => {
        commit('SET_TOKEN', '')
        removeToken()
        resolve()
      })
    },
    //401退出
    removeLoad({ commit, state }){
      return new Promise(resolve =>{
        commit('SET_TOKEN', '');
        commit('SET_ROLES', []);
        commit('SET_PERMISSIONS', []);
        removeToken();
        removeUserType();
        removeStudentPath();
        setStudentPath('/student/index');
        removeIsLogin();
        resolve()
      })
    }
  }
}

export default user
