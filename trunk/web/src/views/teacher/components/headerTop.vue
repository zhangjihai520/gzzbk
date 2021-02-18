<template>
  <el-row>
    <el-breadcrumb separator="/" style="padding-top: 20px">
      <el-breadcrumb-item :to="{ path: '/teacher/index' }"
        >首页</el-breadcrumb-item
      >
      <el-breadcrumb-item>{{ title }}</el-breadcrumb-item>
    </el-breadcrumb>
    <el-dropdown class="userBox" trigger="hover" @command="handleCommand">
      <div class="userMenu el-dropdown-link">
        <el-avatar :size="30" :src="userInfo.userFace" @error="errorHandler">
          <img
            src="https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png"
          />
        </el-avatar>
        <!-- <img :src="userInfo.userFace" alt="" class="userImg" /> -->
        <p class="userName">{{ userInfo.userTrueName }}</p>
      </div>
      <el-dropdown-menu slot="dropdown">
        
        <el-dropdown-item command="b" style="border-bottom: 1px solid #ccc"> 个人中心 </el-dropdown-item>
        <el-dropdown-item command="a">
          退出登录
        </el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
  </el-row>
</template>

<script>
import { removeSchoolId } from "@/utils/auth";
import { getUserInfo } from "@/api/teacher";
export default {
  name: "headerTop",
  data() {
    return {
      userInfo: {},
    };
  },
  props: {
    title: {
      type: String,
      default: "",
    },
    topShow: {
      type: Boolean,
      default: true,
    },
  },
  created() {
    this.getUserInfo();
  },
  methods: {
    goBack() {
      this.$router.go(-1);
    },
    getUserInfo() {
      //获取用户信息
      getUserInfo().then((res) => {
        if (res.code == 200) {
          this.userInfo = res.data;
        }
      });
    },
    handleCommand(command) {
      if (command == "a") {
        this.$store.dispatch("removeLoad").then(() => {
          removeSchoolId();
          this.$router.push("/");
        });
      } else {
        this.$router.push("/teacher/teacherManager");
      }
    },
    errorHandler() {
        return true
      }
  },
};
</script>
<style lang='scss' scoped>
.elHeader {
  padding-top: 20px;
}
.userBox {
  display: flex;
  cursor: pointer;
  position: absolute;
  right: 10px;
  top: 20px;
  z-index: 999;
}
.userMenu {
  display: flex;
}
.userImg {
  width: 28px;
  height: 28px;
  border-radius: 50%;
}
.userName {
  font-size: 18px;
  margin: 0;
  padding-left: 5px;
  padding-top: 5px;
}
.btn {
  display: block;
  position: absolute;
  right: 10px;
  z-index: 999;
}
</style>
