<template>
  <div class="login">
    <label class="backHome" @click="loginbtn">
      <i class="el-icon-s-home">首页</i>
    </label>
    <el-form
      ref="loginForm"
      :model="loginForm"
      :rules="loginRules"
      class="login-form"
    >
      <el-image class="formLogo" :src="require('@/assets/image/newStyle/loginLogo.png')"></el-image>
      <!-- <h3 class="title">南昌市共享云直播</h3> -->
      <el-form-item prop="userType">
        <el-radio-group v-model="userType" class="formRadio" >
          <el-radio :label="1" >学生/老师</el-radio>
          <el-radio :label="0">学校/管理员</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item prop="username">
        <el-input
          v-model="loginForm.username"
          type="text"
          auto-complete="off"
          placeholder="账号"
        >
          <svg-icon
            slot="prefix"
            icon-class="user"
            class="el-input__icon input-icon"
          />
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input
          v-model="loginForm.password"
          type="password"
          auto-complete="off"
          placeholder="密码"
          @keyup.enter.native="handleLogin"
        >
          <svg-icon
            slot="prefix"
            icon-class="password"
            class="el-input__icon input-icon"
          />
        </el-input>
      </el-form-item>
      <el-form-item prop="code">
        <el-input
          v-model="loginForm.code"
          auto-complete="off"
          placeholder="验证码"
          style="width: 63%"
          @keyup.enter.native="handleLogin"
        >
          <svg-icon
            slot="prefix"
            icon-class="validCode"
            class="el-input__icon input-icon"
          />
        </el-input>
        <div class="login-code">
          <img :src="codeUrl" @click="getCode" />
        </div>
      </el-form-item>
      <el-checkbox
        v-model="loginForm.rememberMe"
        style="margin: 0px 0px 15px 0px"
        >记住密码</el-checkbox
      >
      <el-form-item style="width: 100%">
        <el-button
          :loading="loading"
          size="medium"
          type="warning"
          style="width: 100%"
          @click.native.prevent="handleLogin"
        >
          <span v-if="!loading">登 录</span>
          <span v-else>登 录 中...</span>
        </el-button>
      </el-form-item>
    </el-form>
    <!--  底部  -->
    <div class="el-login-footer">
      <span
        >版权所有&nbsp;©&nbsp;2021&nbsp;&nbsp;南昌市共享云直播&nbsp;&nbsp;技术支持：南昌市现代教育技术中心</span
      >
    </div>
  </div>
</template>

<script>
import { getCodeImg } from "@/api/login";
import Cookies from "js-cookie";
import { encrypt, decrypt } from "@/utils/jsencrypt";
import {setStudentPath} from '@/utils/auth';
export default {
  name: "Login",
  data() {
    return {
      codeUrl: "",
      cookiePassword: "",
      loginForm: {
        username: "",
        password: "",
        rememberMe: false,
        code: "",
        uuid: "",
        userType: 1,
      },
      userType: 1,
      loginRules: {
        username: [
          { required: true, trigger: "blur", message: "用户名不能为空" },
        ],
        password: [
          { required: true, trigger: "blur", message: "密码不能为空" },
        ],
        code: [
          { required: true, trigger: "change", message: "验证码不能为空" },
        ],
      },
      loading: false,
      redirect: undefined,
    };
  },
  watch: {
    $route: {
      handler: function (route) {
        this.redirect = route.query && route.query.redirect;
      },
      immediate: true,
    },
  },
  created() {
    this.getCode();
    this.getCookie();
  },
  computed: {},
  methods: {
    getCode() {
      this.userType = 1;
      getCodeImg().then((res) => {
        this.codeUrl = "data:image/gif;base64," + res.img;
        this.loginForm.uuid = res.uuid;
      });
    },
    getCookie() {
      const username = Cookies.get("username");
      const password = Cookies.get("password");
      const rememberMe = Cookies.get("rememberMe");
      const userType = Cookies.get("userTypes");
      if (userType !== undefined) {
        this.userType = Number(userType);
        this.loginForm.userType = Number(userType);
      }
      this.loginForm = {
        username: username === undefined ? this.loginForm.username : username,
        password:
          password === undefined ? this.loginForm.password : decrypt(password),
        userType: this.loginForm.userType,
        rememberMe: rememberMe === undefined ? false : Boolean(rememberMe),
      };
    },
    handleLogin() {
      this.loginForm.userType = this.userType;
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          this.loading = true;
          if (this.loginForm.rememberMe) {
            Cookies.set("username", this.loginForm.username, { expires: 30 });
            Cookies.set("userTypes", this.loginForm.userType, { expires: 30 });
            Cookies.set("password", encrypt(this.loginForm.password), {
              expires: 30,
            });
            Cookies.set("rememberMe", this.loginForm.rememberMe, {
              expires: 30,
            });
          } else {
            Cookies.remove("username");
            Cookies.remove("password");
            Cookies.remove("rememberMe");
            Cookies.remove("userTypes");
          }
          this.$store
            .dispatch("Login", this.loginForm)
            .then((res) => {
              if (res.data.userType == 0) {
                this.$router.push({ path: "/index" });
              } else if (res.data.userType == 1) {
                this.$router.push({ path: "/student/index" });
              } else if (res.data.userType == 2) {
                this.$router.push(`/teacher/index`);
              }
            })
            .catch(() => {
              this.loading = false;
              this.getCode();
              this.userType =this.loginForm.userType
            });
        }
      });
    },
    loginbtn() {
      this.$router.push("/student/index");
      setStudentPath("/student/index")
    },
    handradio(e) {
      this.loginForm.userType = e;
    },
  },
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.login {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  background: url("../assets/image/newStyle/login-background.png") no-repeat center;
  background-size:100% 100%; 
}

.login-form {
  border-radius: 6px;
  width:362px;
  margin-left:25rem;
  .el-input {
    height: 38px;
    input {
      height: 38px;
    }
  }
  .input-icon {
    height: 39px;
    width: 14px;
    margin-left: 2px;
  }
}
.formLogo{
  width: 100%;
  height: 78px;
  margin-top: 10px;
}
.formRadio{
  display: flex;
  justify-content: space-between;
  margin-top: 44px;
}
.login-tip {
  font-size: 13px;
  text-align: center;
  color: #bfbfbf;
}
.login-code {
  width: 33%;
  height: 38px;
  float: right;
  img {
    cursor: pointer;
    vertical-align: middle;
  }
}
.el-login-footer {
  height: 40px;
  line-height: 40px;
  position: fixed;
  bottom: 0;
  width: 100%;
  text-align: center;
  color: #fff;
  font-family: Arial;
  font-size: 12px;
  letter-spacing: 1px;
}
.backHome {
  position: absolute;
  top: 9rem;
  left:12rem;
  cursor: pointer;
  color: #fff;

}
@media screen and (min-width:1860px) {
  .login-form{
    margin-left: 30rem;
  } 
  .backHome{
    top: 14rem;
    left: 16rem;
  }
}
</style>
