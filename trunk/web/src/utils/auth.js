import Cookies from 'js-cookie'

const TokenKey = 'Token'
const userType ='userType'
const schoolId ='schoolId'
const studentPath ='studentPath'
const isLogin ="isLogin"
export function getToken() {
  return Cookies.get(TokenKey)
}

export function setToken(token) {
  return Cookies.set(TokenKey, token)
}

export function removeToken() {
  return Cookies.remove(TokenKey)
}

export function getHeaders() {
  return {
    Authorization: 'Bearer ' + getToken()
  }
}

export function setUserType(token){
  return Cookies.set(userType, token)
}

export function getUserType() {
  return Cookies.get(userType)
}

export function removeUserType() {
  return Cookies.remove(userType)
}
export function setSchoolId(token){
  return Cookies.set(schoolId, token)
}

export function getSchoolId() {
  return Cookies.get(schoolId)
}

export function removeSchoolId() {
  return Cookies.remove(schoolId)
}

export function setStudentPath(path){
  return Cookies.set(studentPath, path)
}

export function getStudentPath() {
  return Cookies.get(studentPath)
}

export function removeStudentPath() {
  return Cookies.remove(studentPath)
}

export function setIsLogin(path){
  return  sessionStorage.setItem(isLogin,path)
  // return Cookies.set(isLogin, path)
}

export function getIsLogin() {
  return sessionStorage.getItem(isLogin)
  // return Cookies.get(isLogin)
}

export function removeIsLogin() {
  return sessionStorage.removeItem(isLogin)
  // return Cookies.remove(isLogin)
}
