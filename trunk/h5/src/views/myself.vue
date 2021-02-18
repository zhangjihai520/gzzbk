<template>
  <div class="contanier">
    <div class="top">
      <div class="topImg">
        <img src="@/assets/logo.png" alt="" class="userImg" />
      </div>
    </div>
    <p class="userName" v-text="userInfo.userName"></p>
    <grid :show-lr-borders="false" :show-vertical-dividers="false" :rows="1">
      <grid-item :label="userInfo.schoolName" >
        <img slot="icon" src="@/assets/image/myself/school.png" />
      </grid-item>
      <grid-item :label="userInfo.class">
        <img slot="icon" src="@/assets/image/myself/grad.png" />
      </grid-item>
      <grid-item link="/component/cell" :label="userInfo.email">
        <img slot="icon" src="@/assets/image/myself/email.png" />
      </grid-item>
    </grid>
    <div class="CourseLink" @click="goList">
      <p class="courseLef">
        <img src="@/assets/image/myself/myCourse.png" width="18" height="18" class="icons"/>
        <span>我的课程</span>
      </p>
      <img src="@/assets/image/myself/right.png" width="18" height="18" class="icons"/>
    </div>
    <mt-button  class="outBtn" @click="loginOut">退出登录</mt-button>
  </div>
</template>
<script>
import { getUserInfo } from "@/utils/api";
import { Grid, GridItem } from "vux";
export default {
  name: "Myself",
  components: {
    Grid,
    GridItem,
  },
  data() {
    return {
      userInfo: {},
    };
  },
  created() {
    this.loading.open()
    this.getInfo();
  },
  methods: {
    goList() {
      this.$router.push("/courseList");
    },
    loginOut() {
      this.$router.push("/login");
    },
    //获取用户信息
    getInfo() {
      getUserInfo().then((res) => {
         this.loading.close();
        if (res.code == 200) {
          res.userInfo = res.data;
        } else {
          this.toast(res.msg);
        }
      });
    },
  },
};
</script>
<style  scoped>
@import "../assets/style/public.css";
.contanier {
  width: 100%;
}
.top {
  width: 100%;
  height: 50vw;
  background-image: url("../assets/image/myself/topBg.png");
  background-size: cover;
  overflow: hidden;
}
.topImg {
  width: 31vw;
  height: 31vw;
  border-radius: 50%;
  background-image: url("../assets/image/myself/userBg.png");
  background-size: cover;
  margin: 20vw auto;
  line-height: 31vw;
}
.userImg {
  width: 90%;
  height: 90%;
  border-radius: 50%;
  margin-top: expression((31vw - 27.9vw)/2);
  vertical-align: middle;
}
.userName {
  padding: 5px;
  color: #1F1F1F;
  font-size: 16px;
}
.CourseLink{
  display: flex;
  justify-content: space-between;
  padding: 10px;
  font-size: 10px;
  line-height: 18px;
}
.outBtn{
  position: absolute;
  bottom: 70px;
  width: 50vw;
  left:25vw;
  color: #939393;
}
</style>