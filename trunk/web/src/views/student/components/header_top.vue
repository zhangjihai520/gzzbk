<template>
  <el-row type="flex" class="header">
    <el-col :span="10" :offset="4 ">
      <!-- <h3>南昌市共享云直播</h3> -->
      <el-image class="headLogo" :src="require('@/assets/image/newStyle/indexLogo.png')"></el-image>
    </el-col>
    <el-col :span="6"> </el-col>
    <el-col :span="2" v-if="loginShow">
      <label class="login_box" @click="loginbtn">
        <i class="el-icon-user">登录</i>
      </label>
    </el-col>
    <el-col :span="4" style="padding: 0" v-else>
      <el-dropdown  trigger="hover" @command="handleCommand">
        <div class="userMenu el-dropdown-link">
          <img :src="userInfo.userFace" alt="" class="userImg" />
          <p class="userName">{{userInfo.userTrueName}}</p>
        </div>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item command="a">
            退出登录
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </el-col>
  </el-row>
</template>

<script>
import { getUserType, getToken } from "@/utils/auth";
import { getUserInfo } from "@/api/common";
export default { 
  name: "header_top",
  data() {
    return {
      input: "",
      centerDialogVisible: false,
      loginShow: false,
      userInfo:{},
      formLabelWidth: "45px",
    };
  },
  created() {
    if (getUserType() == 1 && getToken()) {
      this.loginShow = false;
      this.getUserInfo()
    } else {
      this.loginShow = true;
    }
  },
  methods: {
    loginbtn() {
      this.$router.push("/login");
    },
   handleCommand(command) {
      if (command == "a") {
        this.$store.dispatch("removeLoad").then(() => {
          this.loginShow = true;
          this.$router.push("/");
        });
      }
    },
    getUserInfo() {
      getUserInfo().then(res =>{
        this.userInfo =res.data;
      })
    },
  },
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang='scss' scoped>
.header {
  width: 100%;
  height: 88px;
  font-size: 24px;
  line-height: 45px;
  color: #666;
  background: #fff;
}
.headLogo{
  width: 260px;
  height:56px;
  margin-top:15px ;
}
.search_box,
.login_box {
  line-height: 90px;
  cursor: pointer;
}
.search_input {
  width: 80%;
}
.search,
.login {
  padding-left: 5px;
  font-size: 18px;
}
.el-dialog__body {
  padding: 0 !important;
}
.el-icon-user {
  font-size: 18px;
}
.userMenu {
  display: flex;
  margin-top: 30px;
  cursor: pointer;
}
.userImg {
  width: 28px;
  height: 28px;
  border-radius: 50%;
}
.userName {
  font-size: 18px;
  margin: 0;
  margin-top: -5px;
  padding-left: 5px;
  color: #666;
}
.btn {
  position: relative;
  top: -20px;
  z-index: 999 !important;
}
</style>
