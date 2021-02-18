<template>
  <el-menu
    :default-active="activeIndex"
    class="el-menu-vertical-demo"
    @open="handleOpen"
    @close="handleClose"
    :collapse="isCollapse"
    background-color="#545c64"
    text-color="#fff"
    active-text-color="#ffd04b"
    :router="isRouter"
  >
    <el-menu-item  @click="dbOpen"> 
      <span v-show="!isCollapse" style="padding-left:60px"></span>
       <i class="el-icon-s-unfold"></i>
    </el-menu-item>
    <el-menu-item  v-for="(item,i) in navList" :key="i" :index="item.name" @click.native="dbSchool">
      <i :class="item.icons"></i>
      <span>{{item.navItem}}</span>
    </el-menu-item>
  </el-menu>
</template>

<script>
import {getSchoolId} from "@/utils/auth";
export default {
  name: "slider",
  data() {
    return {
      isCollapse: false,
      isRouter:true,
      activeIndex:this.$route.path,
      navList:[
        {name:'/teacher/index',navItem:'课程管理',icons:'el-icon-s-operation'},
        {name:'/teacher/evaluateManager',navItem:'评价管理',icons:'el-icon-menu'},
        {name:'/teacher/voiceManager',navItem:'留言管理',icons:'el-icon-document'},
        {name:'/teacher/teacherManager',navItem:'个人中心',icons:'el-icon-setting'},
      ]
    };
  },
  created(){
    
  },
  mounted(){
    if( getSchoolId() == 'null' || getSchoolId() ==''){
      this.activeIndex ='/teacher/teacherManager';
      this.$router.push('/teacher/teacherManager');
      this.isRouter =false;
    } 
  },
   watch:{
    '$route'(to,from){
      
    }
  },
  methods: {
    handleOpen(key, keyPath) {
    },
    dbSchool(){
       if( getSchoolId() == 'null' || getSchoolId() ==''){
         this.activeIndex ='/teacher/teacherManager';
         this.msgError('请先确认您所在的学校')
       }
       
    },
    handleClose(key, keyPath) {
      console.log(key, keyPath);
    },
    dbOpen(){
      this.isCollapse =!this.isCollapse
    },
    changeRouter(){
      this.isRouter =true;
    }
  },
};
</script>
<style lang='scss' scoped>
.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 180px;
}
.el-menu-vertical-demo{
  min-height: 100vh;
}
</style>
