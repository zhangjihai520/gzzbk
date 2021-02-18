<template>
  <div class="container">
    <header-top />
    <nav-bar />
    <el-row>
      <el-col :span="16" :offset="4" style="margin-top: 20px">
        <el-page-header @back="goBack" content="查看详情" style="color:#555"> </el-page-header>
      </el-col>
    </el-row>
    <el-row>
      <el-col  :span="16" :offset="4">
        <div class="grid-content">
          <div class="info_box">
            <div class="info_left">
              <el-image  class="info_img" :src="teacherInfo.userFace"></el-image>
              <div>
                 <p class="uer_name">{{ teacherInfo.userTrueName }}</p>
                 <p class="schoolName">{{ teacherInfo.schoolName }}</p>
              </div>
            </div>
            <div class="frevue">
              <p class="comment"><span class="font-size:18px;color:#333 !important">简介：</span>{{teacherInfo.comment}}</p>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
    <el-row>
      <el-col  :span="16" :offset="4" class="work_box">
        <div class="work_list">
          <div class="work_title">
            <p>作品</p>
            <p>操作</p>
          </div>
        </div>
        <div
          class="work_item"
          :key="index"
          v-for="(item, index) in teacherInfo.opusList"
          :class="index % 4 == 0 ? '' : ''"
        >
          <div class="work_left">
            <el-image  class="work_img" :src="item.imagePath"></el-image>
            <div class="work_info">
              <p>{{ item.name }}</p>
              <p>老师：{{ teacherInfo.userTrueName }}</p>
              <p>课程开始时间：{{ item.beginTime }}</p>
              <p>课程结束时间：{{item.endTime}}</p>
              <p style="display: flex">
                <span>评分：</span>
                <el-rate disabled v-model="item.commentScore" show-score text-color="#ff9900"></el-rate>
              </p>
            </div>
          </div>
          <div>
            
            <el-button
              v-show="item.classState == 3 && item.category == 1"
              type="warning"
              class="work_btn"
              @click="inLive(item.id)"
              >回放</el-button
            >
            <el-button
              v-show="
                item.classState == 2 &&
                item.registerStatus == 1 &&
                item.category == 1
              "
              type="warning"
              class="work_btn"
               @click="inLive(item.id)"
              >进入直播</el-button
            >
            <el-button
              v-show="
                item.category == 2
              "
              type="warning"
              class="work_btn"
               @click="lookCourse(item.id)"
              >观看</el-button
            >
            <el-button
              v-show="
                item.classState == 0 &&
                item.category == 1 &&
                item.registerStatus == 0
              "
              type="warning"
              class="work_btn"
              @click="regist(item.id)"
              >报名</el-button
            >
            <el-button type="btn" disabled class="work_btn">{{
              statusShow(item.classState, item.registerStatus)
            }}</el-button>
          </div>
        </div>
      </el-col>
      <footer-bottom />
    </el-row>
  </div>
</template>

<script>
import HeaderTop from "./components/header_top";
import NavBar from "./components/nav_bar";
import FooterBottom from "@/components/footer";

export default {
  name: "teacher_info",
  components: {
    HeaderTop,
    NavBar,
    FooterBottom,
  },
  data() {
    return {
      param: {
        teacherId: this.$route.query.id,
      },
      teacherInfo: {},
      recently: [],
      show: false,
    };
  },
  mounted() {
    console.log(this.$route.query.id);
    this.getDetail();
  },
  methods: {
    goBack() {
      this.$router.go(-1);
    },
    getDetail() {
      //教师详情
      this.$store.dispatch("GetTeacherDetail", this.param).then((res) => {
        this.teacherInfo = res.data;
        this.recently = [];
        if (res.code == 200) {
          console.log(res)
          if (res.data.opusList.length > 0 || res.data.opusList) {
            this.recently.push(res.data.opusList[0]);
          }
          this.show = !this.show;
        }
      });
    },
    statusShow(status, regis) {
      //状态显示
      let showStatus = "";
      if (status == -1) {
        showStatus = "待报名";
      } else if (status == 0 && regis == 1) {
        showStatus = "已报名";
      } else if (status == 0 && regis == 0) {
        showStatus = "报名中";
      } else if (status == 1) {
        showStatus = "待开课";
      } else if (status == 2) {
        showStatus = "课中";
      } else if (status == 3) {
        showStatus = "已结束";
      }
      return showStatus;
    },
    regist(item){
      this.$store.dispatch('RegistSubject',{subjectId:item}).then(res =>{
        if(res.code==200 && res.status ==1){
          this.msgSuccess("报名成功");
          this.getDetail();
        }else{
          this.msgError(res.msg)
        }
      })
    },
    inLive(item){//直播跳转
       this.$router.push({
        path: "/student/inLive_course",
        query: { id: item },
      });
    },
    lookCourse(item){//录播跳转
      this.$router.push({
        path: "/student/inLive_detail",
        query: { id: item },
      });
    }
  },
};
</script>
<style lang='scss' scoped>
@import "@/assets/styles/public.scss";
.info_box {
  height: 120px;
  width: 100%;
  background: #fff;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  border-radius: 10px;
  margin-top: 10px;
  display: flex;
  padding-right: 10px;
  justify-content: space-between;
  .info_left {
    display: flex;
  }
  .info_img {
    width: 60px;
    height: 60px;
    margin-top: 30px;
    border-radius: 5px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
    margin-left: 10px;
  }
  .uer_name {
    margin-top: 30px;
    font-size: 20px;
    margin-left: 10px;
    font-weight: bolder;
  }
  .schoolName{
    font-size: 16px;
    margin-left: 10px;
    color: #666;
    margin-top:15px;
  }
  .frevue {
    width: 70%;
    font-size: 14px;
    word-break:break-all;   
    word-wrap:break-word; 
  }
}
.comment{
  font-size: 14px;
  margin-top: 15px;
  line-height: 18px;
  letter-spacing: 1px;
  color: #555;
}
.work_box {
  min-height:calc(100vh - 400px);
  background: #fff;
  margin-top: 20px;
  border-radius: 5px;
}
.work_list {
  width: 100%;
}
.work_title {
  display: flex;
  justify-content: space-between;
  padding: 15px;
  width: 100%;
  border-bottom: 1px solid #cfcfcf;
  & > p {
    font-size: 16px;
    color: #555;
  }
}
.work_item {
  background: #fff;
  display: flex;
  padding: 10px;
  border-bottom: 1px solid #cfcfcf;
  justify-content: space-between;
  .work_left {
    display: flex;
    .work_img {
      width: 180px;
      height: 110px;
    }
    .work_info {
      margin-left: 10px;
      & > p {
        margin-top: 8px;
        font-size: 14px;
        color: #555;
      }
    }
  }
}
.work_btn {
  margin-top: 40px;
  position: relative;
  z-index: 99;
}
</style>
