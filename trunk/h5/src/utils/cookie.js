import Cookies from "js-cookie"

//设置自定义头部
export function getHeaders() {
  return {
    Authorization: 'Bearer ' + getToken()
  }
}

/**
 * 存储Token
 * @param {*} token
 */
export function setToken(token) {
	setCookie('Authorization', token);
}
/**
 * 获取Token
 */
export function getToken() {
	return getCookie('Authorization');
}

/**
 * 设置cookie
 */
export function setCookie(cookName, value) {
	delCookie(cookName);
	Cookies.set(cookName, value, { expires: 30, path: '/' });
}
/**
 * 获取cookie
 */
export function getCookie(cookName) {
	return Cookies.get(cookName);
}

/**
 * 删除指定name cookie
 * @param {*} cookName
 */
export function delCookie(cookName = 'Authorization') {
	Cookies.remove(cookName);
}

/**
 * 清空所有cookie
 */
function clearCookie() {
	const keys = document.cookie.match(/[^ =;]+(?=\=)/g);
	if (keys) {
		for (let i = keys.length; i--; ) Cookies.remove(keys[i]);
	}
}