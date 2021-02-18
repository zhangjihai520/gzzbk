<template>
  <el-row style="background: #595757">
    <el-col :span="12" :offset="4">
      <!-- this.$route.path-->
      <el-menu
        :default-active="activePath"
        class="el-menu-demo"
        mode="horizontal"
        @select="handleSelect"
        background-color="#595757"
        text-color="#fff"
        active-text-color="#fff"
        router
      >
        <el-menu-item
          v-for="(item, i) in navList"
          :key="i"
          :index="item.name"
          >{{ item.navItem }}</el-menu-item
        >
      </el-menu>
    </el-col>
  </el-row>
</template>

<script>
import { setStudentPath, getStudentPath } from "@/utils/auth";
import { getToken } from "@/utils/auth";
export default {
  name: "nav_bar",
  data() {
    return {
      navList: [
        { name: "/student/index", navItem: "首页" },
        { name: "/student/live_course", navItem: "直播中心" },
        { name: "/student/course_contant", navItem: "课程中心" },
        { name: "/student/inLive_teacher", navItem: "科教风云" },
        { name: "/student/student_info", navItem: "个人中心" },
      ],
    };
  },

  created() {},
  computed: {
    activePath() {
      let path = "";
      return (path = getStudentPath());
    },
  },
  methods: {
    
    handleSelect(key, keyPath) {
      setStudentPath(key);
      if (getToken() == undefined) {
        this.$router.push("/login");
      }
    },
  },
};
</script>
<style lang='scss' scoped>
.el-menu.el-menu--horizontal {
  border: none !important;
}
.el-menu-demo {
  height: 62px;
}
.el-menu-item.is-active {
  background-color: #fe7a00 !important;
  height: 62px;
  // color: #fff;
  span {
    color: #fe7a00 !important;
  }
}
</style>
