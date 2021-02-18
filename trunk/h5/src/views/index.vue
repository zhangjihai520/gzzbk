<template>
  <div class="container">
    <mt-swipe :auto="4000" style="height: 20vh">
      <mt-swipe-item v-for="(item, index) in banners" :key="index">
        <img :src="item.picUrl" alt="" class="banners" />
      </mt-swipe-item>
    </mt-swipe>
    <div>
      <div class="listTitle">
        <p class="icons"></p>
        <h4 class="titleName">直播课程</h4>
      </div>
      <ul class="listBox">
        <li class="listItem" v-for="(item, index) in liveList" :key="index">
          <p class="mark">
            <span v-if="item.classStatus == -1">待报名</span>
            <span v-if="item.classStatus == 0">报名中</span>
            <span v-if="item.classStatus == 1">待上课</span>
            <span v-if="item.classStatus == 2">上课中</span>
            <span v-if="item.classStatus == 3">已下课</span>
          </p>
          <img
            :src="require('@/assets/image/index/play.png')"
            alt=""
            class="playeIcon"
          />
          <img
            :src="require('@/assets/image/index/play.png')"
            alt=""
            class="playeIcon"
          />
          <img :src="item.imagePath" class="listImg" alt="" />

          <div class="infoBox">
            <p class="listInfo">{{ item.name }}</p>
            <p class="listInfo">主讲教师：{{ item.teacherName }}</p>
            <p class="listInfo">上课时间：{{ item.beginTime }}</p>

            <p class="btnBox">
              <mt-button
                class="btn"
                :disabled="item.registerStatus == 0 && item.classStatus == 2"
                v-if="item.classStatus != 3"
                @click="lookLive(item.id)"
                >进入直播</mt-button
              >
              <mt-button
                class="btn"
                v-if="item.classStatus == 3"
                @click="lookLive(item.id)"
                >回放</mt-button
              >
              <mt-button
                class="btn"
                v-if="item.registerStatus == 0 && item.classStatus == 0"
                >报名</mt-button
              >
            </p>
          </div>
        </li>
      </ul>
      <div class="listTitle">
        <p class="icons"></p>
        <h4 class="titleName">热门课程</h4>
      </div>
      <ul class="listBox">
        <li
          class="listItem"
          v-for="(item, index) in hotSubjectList"
          :key="index"
        >
          <img
            :src="require('@/assets/image/index/play.png')"
            alt=""
            class="playeIcon"
          />
          <img :src="item.imagePath" class="listImg" alt="" />
          <div class="infoBox">
            <p class="listInfo">{{ item.name }}</p>
            <p class="listInfo">播放次数：{{ item.playCount }}</p>
            <p class="listInfo">
              课时：【<span>{{ item.classHour }}</span
              >】
            </p>
            <!-- <p class="listInfo">当前进度</p> -->
            <p class="btnBox">
              <mt-button class="btn" @click="lookHot(item.id)">观看</mt-button>
            </p>
          </div>
        </li>
      </ul>
    </div>
  </div>
</template>
<script>
import { banners, getLiveList, getHotSubjectList } from "@/utils/api";
export default {
  name: "Index",
  components: {},
  data() {
    return {
      //轮播列表
      banners: [],
      //直播列表
      liveList: [],
      //热门课程
      hotSubjectList: [],
    };
  },
  created() {
    this.loading.open();
    this.getBanners();
    this.getLiveList();
    this.getHotList();
  },
  methods: {
    //获取banner
    getBanners() {
      banners().then((res) => {
        if (res.code == 200) {
          this.banners = res.data;
        } else {
          this.toast(res.msg);
        }
      });
    },
    //获取直播课程列表
    getLiveList() {
      getLiveList().then((res) => {
        this.loading.close()
        if (res.code == 200) {
          this.liveList = res.data;
        } else {
          this.toast(res.msg);
        }
      });
    },
    //获取热门课程列表
    getHotList() {
      getHotSubjectList().then((res) => {
        if (res.code == 200) {
          this.hotSubjectList = res.data;
        } else {
          this.toast(res.msg);
        }
      });
      let res = getHotSubjectList();
    },
    clickBack() {
      this.$router.go(-1);
    },
    //直播
    lookLive(item) {
      this.$router.push({
        path: "/liveCourse",
        query: { id: item },
      });
    },
    //热门
    lookHot(item) {
      this.$router.push({
        path: "/recordCourse",
        query: { id: item },
      });
    },
  },
};
</script>
<style scoped>
@import "../assets/style/public.css";
.container {
  height: calc(100vh - 55px);
  box-sizing: border-box;
  padding: 10px;
  overflow-y: scroll;
  clear: both !important;
}
.banners {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.icons {
  border-left: 5px solid #ff7a01;
}
</style>