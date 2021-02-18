<template>
  <div class="contanier">
    <div class="loginBox">
      <div class="loginCont">
        <x-img
          :src="require('@/assets/image/login/logo.png')"
          class="logoImg"
        ></x-img>

        <x-input
          placeholder="请输入账号"
          v-model="loginForm.username"
          class="inputItem"
          :required="isRequire"
        >
          <img
            slot="label"
            style="padding-right: 10px; display: block"
            src="@/assets/image/login/icon_user.png"
            width="24"
            height="24"
          />
        </x-input>
        <x-input
          type="password"
          placeholder="请输入密码"
          v-model="loginForm.password"
          class="inputItem"
          :required="isRequire"
        >
          <img
            slot="label"
            style="padding-right: 10px; display: block"
            src="@/assets/image/login/icon_psd.png"
            width="24"
            height="24"
          />
        </x-input>

        <x-input
          type="password"
          placeholder="请输入验证码"
          v-model="loginForm.code"
          class="codeItem"
        >
          <img
            slot="right-full-height"
            style="padding-right: 10px; display: block"
            :src="codeUrl"
            height="24"
            @click="getcode"
          />
        </x-input>
        <div class="submitBox">
          <x-button
            @click.native="submit"
            class="submit"
            >登 录</x-button
          >
        </div>
        
      </div>
      <div class="bg"></div>
    </div>
  </div>
</template>
<script>
import { XInput, Group, XButton, XImg } from "vux";
import { getCodeImg, login } from "@/utils/api";
import { setToken } from "@/utils/cookie";
export default {
  name: "Login",
  data() {
    return {
      //是否校验输入为空
      isRequire: true,
      loginForm: {
        username: "",
        password: "",
        code: "",
        uuid: "",
        loginType: 1,
      },
      codeUrl: "",
    };
  },
  components: {
    XInput,
    Group,
    XButton,
    XImg,
  },
  created() {
    this.getcode();
  },
  methods: {
    submit() {
      // this.$router.push("/index");
      this.loginWait();
    },
    async getcode() {
      let res = await getCodeImg();
      if (res.code == 200) {
        this.codeUrl = "data:image/gif;base64," + res.img;
        this.loginForm.uuid = res.uuid;
      } else {
        this.toast(res.msg);
      }
    },
    loginWait() {
      let params = this.loginForm;
      if (
        this.loginForm.username == "" ||
        this.loginForm.password == "" ||
        this.loginForm.code == ""
      ) {
        this.toast("请将信息填写完整");
        return false;
      }
      login(params).then((res) => {
        if (res.code == 200) {
          setToken(res.data.token);
          this.$router.push("/index");
          // console.log(res)
        } else {
          this.getcode();
          this.toast(res.msg);
        }
      });

      // let res = await login(params);
    },
  },
};
</script>
<style  scoped>
.contanier {
  width: 100%;
  height: 100vh;
  background: #fd8719;
}
.loginBox {
  position: absolute;
  bottom: 0;
  top: 0;
  left: 0;
  right: 0;
  margin: auto;
  width: calc(100vw - 30px);
  height: calc(100vh - 180px);
  background: #fff;
  border-radius: 20px;
}
.logoImg {
  margin-top: 20px;
  width: 30vw;
  height: 30vw;
}
.inputItem,
.codeItem {
  background: #f8f8f8;
  border-radius: 30px;
  width: calc(100% -30px);
  height: 30px;
  margin: 15px 10px 0 10px;
}
.weui-cell:before {
  border: none !important;
}
.codeItem {
  height: 45px !important;
  border-radius: 45px;
}
.vux-x-input-right-full > img {
  padding: 0 !important;
}
.submitBox{
 box-sizing: border-box;
 margin-left: 10px;
 margin-right: 10px;
 margin-top:4vh;
 z-index: 1 !important;
}
.submit{
  background: linear-gradient(#FFAD69,#FFAA66,#FF9454);
  background-size: 100%;
  border-radius: 44px;
  color: #fff;
}
.bg{
  width: 80%;
  height: 30px;
  background: #fff;
  position: absolute;
  bottom:-20px;
  left: 10%;
  opacity: 0.3;
  border-radius: 10px;
}
</style>