<template>
  <div >
    <mt-tabbar v-model="selected" class="footer">
      <mt-tab-item id="/index" style="color: #979797">
        <img v-show="selected != '/index'" slot="icon" src="../../assets/image/index/index0.png" />
        <img v-show="selected == '/index'" slot="icon" src="../../assets/image/index/index1.png" />
        首页
      </mt-tab-item>
      <mt-tab-item id="/course" style="color: #979797">
        <img v-show="selected != '/course'" slot="icon" src="../../assets/image/course/course0.png" />
        <img v-show="selected == '/course'" slot="icon" src="../../assets/image/course/course1.png" />
        课程
      </mt-tab-item>
      <mt-tab-item id="/myself" style="color: #979797">
        <img v-show="selected != '/myself'" slot="icon" src="../../assets/image/myself/myself0.png" />
        <img v-show="selected == '/myself'" slot="icon" src="../../assets/image/myself/myself1.png" />
        我的
      </mt-tab-item>
      
    </mt-tabbar>
  </div>
</template>

<script>
export default {
  name: "HelloWorld",
  data() {
    return {
      selected: sessionStorage.getItem("selected")
        ? JSON.parse(sessionStorage.getItem("selected"))
        : "/index",
      home: "index",
      course: "course",
      user: "myself",
    };
  },
  watch: {
    $route(to, from) {
      console.log(to.fullPath);
      if (to.fullPath != "/") {
        sessionStorage.setItem("selected", JSON.stringify(to.fullPath));
        this.selected = JSON.parse(sessionStorage.getItem("selected"));
        return;
      }else{
        sessionStorage.setItem("selected", JSON.stringify("/index"));
        this.selected = JSON.parse(sessionStorage.getItem("selected"));
      }
    },
    selected(newv, oldv) {
      sessionStorage.setItem("selected", JSON.stringify(newv));
      this.selected = JSON.parse(sessionStorage.getItem("selected"));
      this.$router.push(this.selected);
    },
  },
  created() {
    // console.log(this.selected)
  },
  methods: {
    toast() {
      this.$toast("nimad");
    },
    clickBtn() {
      this.$router.push("/index");
    },
  },
};
</script>
<style scoped>
.footer{
  position: fixed;
  bottom: -1px;
  background: #fff; 
}

</style>
