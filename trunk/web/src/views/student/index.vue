<template>
  <el-container class="container" v-loading="loading">
    <main class="container">
      <header-top />
      <nav-bar />
      <el-row>
        <el-col>
          <el-carousel
            :interval="5000"
            type="outside"
            class="bannerBox"
            height="400px"
          >
            <el-carousel-item v-for="(item, index) in imageList" :key="index">
              <div class="medium">
                <a @click="openLink(item.linkUrl)">
                  <el-image :src="item.picUrl" class="lg_img"></el-image>
                </a>
              </div>
            </el-carousel-item>
          </el-carousel>
        </el-col>
      </el-row>
      <el-row class="liveTitle">
        <div class="titles">
          <h3 class="titleName">直播课程</h3>
          <p class="titleEngish">LIVE COURSE</p>
        </div>
      </el-row>
      <el-row>
        <el-col :span="16" :offset="4" class="more">
          <el-button type="text" @click="lookLive_more">查看更多 <i class="el-icon-more"></i></el-button>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="16" :offset="4" class="liveBox">
          <div
            class="carBody"
            v-for="(item, index) in Livelist"
            :key="index"
            :body-style="{ padding: '0px' }"
          >
            <div
              class="clearfix"
              style="background: #fff; padding-bottom: 10px"
            >
              <img
                v-show="item.registerStatus == 1"
                src="@/assets/image/already_sign.png"
                class="mark"
              />
              <el-image :src="item.imagePath" class="carImg"></el-image>
              <div style="padding: 0 10px">
                <h3 class="courseName">
                  {{ item.name }}
                </h3>
                <p class="cars_item">
                  主讲教师：<span>{{ item.teacherName }}</span>
                </p>
                <p class="cars_item">
                  上课时间：<span>{{ item.beginTime }}</span>
                </p>
                <p class="cars_item">
                  当前进度：
                  <span v-if="item.classStatus == -1">【待报名】</span>
                  <span v-if="item.classStatus == 0">【报名中】</span>
                  <span v-if="item.classStatus == 1">【待上课】</span>
                  <span v-if="item.classStatus == 2">【上课中】</span>
                  <span v-if="item.classStatus == 3">【已下课】</span>
                </p>
                <div class="bottom clearfix" style="float: right">
                  <el-button
                    type="warning"
                    class="cars_btn"
                    @click="lookLive(item.id)"
                    :disabled="
                      item.registerStatus == 0 && item.classStatus == 2
                    "
                    v-show="item.classStatus != 3"
                    >进入直播</el-button
                  >
                  <el-button
                    type="warning"
                    class="cars_btn"
                    @click="lookLive(item.id)"
                    v-show="item.classStatus == 3"
                    >回看</el-button
                  >
                  <el-button
                    type="warning"
                    class="cars_btn"
                    @click="regist(item.id)"
                    v-show="item.registerStatus == 0 && item.classStatus == 0"
                    >报名</el-button
                  >
                </div>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
      <el-row class="liveTitle">
        <div class="titles" style="background: #595757">
          <h3 class="titleName">热门课程</h3>
          <p class="titleEngish">HOT COURSE</p>
        </div>
      </el-row>
      <el-row>
        <el-col :span="16" :offset="4" class="more">
          <el-button type="text" @click="lookHot_more">查看更多 <i class="el-icon-more"></i></el-button>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="16" :offset="4" class="liveBox">
          <div
            class="carBody"
            v-for="(item, index) in courseList"
            :key="index"
            :body-style="{ padding: '0px' }"
          >
            <div
              class="clearfix"
              style="background: #fff; padding-bottom: 10px"
            >
              <el-image :src="item.imagePath" class="carImg"></el-image>
              <div style="padding: 0 10px">
                <h3 class="courseName">{{ item.name }}</h3>
                <p class="cars_item">
                  播放次数：<span>{{ item.playCount }}</span
                  >次
                </p>
                <p class="cars_item">
                  课时：【<span>{{ item.classHour }}</span
                  >】
                </p>
                <div class="bottom">
                  <el-button
                    type="warning"
                    class="cars_btn"
                    @click="lookHot(item.id)"
                    >观看</el-button
                  >
                </div>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
      <footer-bottom />
    </main>
  </el-container>
</template>

<script>
import HeaderTop from "./components/header_top";
import NavBar from "./components/nav_bar";
import FooterBottom from "@/components/footer";
import { getLivelist, getHotSubjectList } from "@/api/student";
import { banners } from "@/api/common";
import { getToken, setIsLogin ,setStudentPath} from "@/utils/auth";
export default {
  name: "stuIndex",
  components: {
    HeaderTop,
    NavBar,
    FooterBottom,
  },
  data() {
    return {
      liveShow: false,
      courseShow: false,
      imageList: [],
      Livelist: [],
      courseList: [],
      loading: true,
    };
  },
  watch: {
    $route: {
      immediate: true,
      handler(to, form) {
        console.log(to.path);
      },
    },
  },
  created() {
    setIsLogin("true");
  },
  mounted() {
    this.getLiveCourse();
    this.getCourseList();
    this.getBanners();
  },
  computed: {},
  methods: {
    lookLive(item) {
      this.$router.push({
        path: "/student/inLive_course",
        query: { id: item },
      });
    },
    lookHot(item) {
      this.$router.push({
        path: "/student/inLive_detail",
        query: { id: item },
      });
    },
    lookLive_more(){
      setStudentPath('/student/live_course');
      this.$router.push('/student/live_course')
    },
    lookHot_more(){
      setStudentPath('/student/course_contant');
      this.$router.push('/student/course_contant')
    },
    getLiveCourse() {
      getLivelist().then((res) => {
        if (res.code == 200) {
          this.Livelist = res.data.slice(0, 5);
        }
      });
      setTimeout(() => {
        this.loading = false;
      }, 1000);
    },
    getCourseList() {
      getHotSubjectList().then((res) => {
        if (res.code == 200) {
          this.courseList = res.data.slice(0, 5);
        }
      });
    },
    getBanners() {
      banners().then((res) => {
        if (res.code == 200) {
          this.imageList = res.data;
        }
      });
    },
    openLink(url) {
      //外部链接跳转
      window.open(url, "_blank");
    },
    regist(item) {
      //报名
      if (getToken()) {
        this.$store
          .dispatch("RegistSubject", { subjectId: item })
          .then((res) => {
            if (res.code == 200 && res.status ==1) {
              this.msgSuccess("报名成功");
              this.getLiveCourse();
            } else {
              this.msgError(res.msg);
            }
          });
      } else {
        this.$router.push("/login");
      }
    },
  },
};
</script>
<style lang='scss' scoped>
@import "@/assets/styles/public.scss";
.container {
  width: 100%;
}
.clearfix {
  width: 100%;
  clear: both;
  box-sizing: border-box;
}
.contant {
  position: relative;
  width: 100vw;
  background: #f4f6f5;
}
.medium {
  width: 100%;
  height: 100%;
  cursor: pointer;
}
.lg_img {
  width: 100%;
  height: 100%;
  object-fit: fill;
}
.liveTitle {
  clear: both;
  display: flex;
  justify-content: center;
  border-radius: 5px;
}
.titles {
  width: 230px;
  height: 62px;
  background: #fe7a00;
  margin-top: 48px;
  .titleName {
    font-size: 24px;
    color: #fff;
    text-align: center;
    margin-top: 8px;
    letter-spacing: 5pt;
  }
  .titleEngish {
    font-size: 13px;
    text-align: center;
    color: #fff;
    margin-top: 8px;
  }
}
.more{
  margin-top: 48px;
  text-align: right;
}
.liveBox {
  display: flex;
  justify-content: center;
  // 
}
.carBody {
  width: 24%;
  margin-left: 1.33%;
  border-radius: 5px;
  cursor: pointer;
}
.carImg {
  width: 100%;
  height: 138px;
  border-top-right-radius: 5px;
  border-top-left-radius: 5px;
}
.carBody:hover {
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.3);
  transform: scale(1.03);
}

.bottom {
  text-align: right;
}
.courseName {
  margin-top: 16px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.mark {
  z-index: 99;
  width: 54px;
  height: 54px;

  position: absolute;
}
.cars_item {
  margin-top: 10px;
  font-size: 12px;
  color: #999;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.cars_btn {
  margin-top: 10px;
  z-index: 99;
}
@media screen and (min-width: 1900px) {
  .carImg {
    height: 151px;
  }
}
</style>
