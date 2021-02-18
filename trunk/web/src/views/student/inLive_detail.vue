<template>
  <div class="container">
    <header-top />
    <nav-bar />
    <el-row>
      <el-col :span="16" :offset="4" class="topTitle">
        <el-page-header
          class="topHead"
          @back="goBack"
          :content="'课程：' + titles"
        >
        </el-page-header>
        <div class="topInfo" v-show="teacher">教师：{{ teacher }}</div>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="16" :offset="4" class="tvBox">
        <div
          class="switchUrl"
          v-show="buttonShow"
          @click="handleSwitch()"
        ></div>
        <div class="pay">
          <div class="wrapper">
            <section class="videoWrapper">
              <div id="mse" style="backgroundcolor: 'rgba(0,0,0,0.87)'"></div>
            </section>
          </div>
        </div>

        <div class="leave_msg">
          <el-tabs v-model="topActive" type="card">
            <el-tab-pane label="提问" name="1">
              <div
                class="infinite-list-wrapper"
                id="scroll"
                style="overflow: auto"
                @scroll="load()"
              >
                <p v-if="loading" class="load">
                  <i class="el-icon-loading"></i>
                </p>
                <p v-if="noMore" class="noMore">没有更多了</p>
                <ul
                  class="list"
                  v-infinite-scroll="load"
                  infinite-scroll-disabled="disabled"
                >
                  <li
                    v-for="(item, index) in qustionAllList"
                    :key="index"
                    class="list-item"
                  >
                    <div style="width: 20px; height: 20px">
                      <el-avatar :size="20" :src="item.userFace"></el-avatar>
                    </div>
                    <div class="qustionItem">
                      <p style="color: #409eff">{{ item.userName }}</p>
                      <p>{{ item.content }}</p>
                      <p>
                        <span style="color: #999">{{ item.createTime }}</span>
                        <span
                          class="replay"
                          v-show="item.children"
                          @click="btnShow(index)"
                          >查看回复</span
                        >
                      </p>
                      <div
                        v-show="index == textTab"
                        class="answer"
                        v-for="(v, i) in item.children"
                        :key="i"
                      >
                        <div style="width: 20px; height: 20px">
                          <el-avatar
                            :size="20"
                            :src="v.userFace"
                          ></el-avatar>
                        </div>
                        <!-- <el-avatar :size="20" :src="v.userFace"></el-avatar> -->
                        <div class="qustionItem">
                          <p style="color: #409eff">{{ v.userName }}</p>
                          <p>{{ v.content }}</p>
                          <p style="color: #999">{{ v.createTime }}</p>
                        </div>
                      </div>
                    </div>
                  </li>
                </ul>
              </div>

              <div>
                <el-input
                  type="textarea"
                  placeholder="请输入内容"
                  v-model="messageContent"
                  @keypress.native.enter="send"
                  maxlength="200"
                  show-word-limit
                  class="textarea"
                  resize="none"
                  rows="3"
                >
                </el-input>
              </div>
              <div class="send_box">
                <span style="float: right">
                  <el-button
                    type="warning"
                    class="cars_btn"
                    :disabled="messageContent.length == 0 ? sendBtn : !sendBtn"
                    @click="send"
                    >发送</el-button
                  >
                </span>
              </div>
            </el-tab-pane>
            <el-tab-pane label="章节" name="2">
              <p
                class="section"
                v-for="(item2, index) in videoList"
                :key="index"
                @click="changerCourse(item2.id)"
              >
                <el-link
                  :class="showLink == item2.id ? 'linkActive' : ''"
                  icon="el-icon-video-camera"
                  :underline="false"
                  >{{ item2.name }}</el-link
                >
              </p>
            </el-tab-pane>
          </el-tabs>
        </div>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="16" :offset="4" class="tabBox">
        <div class="tabTop">
          <p
            class="tabItem"
            id="1"
            :class="isTable ? 'tabActive' : ''"
            @click="tabClick"
          >
            课程简介
          </p>
          <p
            class="tabItem"
            id="2"
            :class="!isTable ? 'tabActive' : ''"
            @click="tabClick"
          >
            更多视频
          </p>
        </div>
        <div v-show="isTable">
          <p class="intro">{{ siginContent == "" ? "暂无" : siginContent }}</p>
        </div>
        <div v-show="!isTable" class="morecot">
          <div
            class="moreBox"
            v-for="(mItem, mIndex) in moreList"
            :key="mIndex"
          >
            <el-image :src="mItem.imagePath" class="mImgs"></el-image>
            <div class="mInfoBox">
              <p class="mInfo">课程名称：{{ mItem.name }}</p>
              <p class="mInfo">播放次数：{{ mItem.playCount }}</p>
              <p class="mInfo">课时：{{ mItem.classHour }}</p>
              <p class="btnBox">
                <el-button type="warning" @click="handleClick(mItem.id)"
                  >观看</el-button
                >
              </p>
            </div>
          </div>
        </div>
      </el-col></el-row
    >
    <footer-bottom />
  </div>
</template>

<script>
import HeaderTop from "./components/header_top";
import NavBar from "./components/nav_bar";
import FooterBottom from "@/components/footer";
import {
  getSubjectDetail,
  getLeavemessage,
  leavemessage,
  moreSubjectList,
} from "@/api/student/hasToken";
import Player from "xgplayer";
import FlvJsPlayer from "xgplayer-flv.js";
import HlsJsPlayer from "xgplayer-hls.js";
export default {
  name: "inLive_detail",
  components: {
    HeaderTop,
    NavBar,
    FooterBottom,
  },
  data() {
    return {
      buttonShow: true,
      isScroll: true,
      showId: "",
      showLink: "",
      player: null,
      config: null,
      playerReady: false,
      loading: false,
      activeName: "first",
      topActive: "1",
      textarea: "",
      titles: "",
      subjectId: "",
      chapterlessonId: "",
      messageContent: "",
      teacher: "",
      qustionList: [],
      videoList: [],
      //所有留言
      qustionAllList: [],
      noMore: false,
      sendBtn: true,
      //回复下拉
      textTab: -1,
      //更多视频
      moreList: [],
      //简介tab显示
      isTable: true,
      //简介
      siginContent: "",
      n: 1,
    };
  },
  created() {
    this.subjectId = this.$route.query.id;
  },
  mounted() {
    this.getLessonData();
  },
  destroyed: function () {
    this.player.destroy();
    this.player = null;
  },
  computed: {
    disabled() {
      return this.loading || this.noMore;
    },
  },
  methods: {
    tabClick(e) {
      if (e.currentTarget.id == 1) {
        this.isTable = true;
      } else {
        this.isTable = false;
        moreSubjectList({ subjectId: this.subjectId }).then((res) => {
          if (res.code == 200) {
            this.moreList = res.data;
          }
        });
      }
    },
    load() {
      //加载
      let domScroll = document.getElementById("scroll");
      if (
        domScroll.scrollTop <= 20 &&
        this.qustionAllList.length > 0 &&
        domScroll.scrollTop != 0
      ) {
        if (this.isScroll) {
          this.isScroll = false;
          this.loading = true;
          let id = this.qustionAllList[0].id;
          getLeavemessage({
            subjectId: this.subjectId,
            chapterlessonId: this.chapterlessonId,
            boundaryId: id,
          }).then((res) => {
            if (res.code == 200) {
              this.n += 1;
              setTimeout(() => {
                this.qustionList = [];
                this.qustionList = res.data.dataList.reverse();
                let listLength = this.qustionList.length;
                if (listLength == 25) {
                  domScroll.scrollTop = domScroll.scrollHeight / (this.n - 1);
                  domScroll.scrollTop += 100;
                } else {
                  let len = listLength / 25;
                  domScroll.scrollTop = domScroll.scrollHeight / (this.n + 1);
                }
                if (listLength > 0) {
                  this.isScroll = true;
                  this.loading = false;
                  for (let i = 0; i < listLength; i++) {
                    this.qustionAllList.unshift(this.qustionList[i]);
                  }
                } else {
                  domScroll.scrollTop = 0;
                  this.isScroll = false;
                  this.loading = false;
                  this.noMore = true;
                  return false;
                }
              }, 1200);
            }
          });
        }
      }
    },
    goBack() {
      this.$router.go(-1);
    },
    handleClick(item) {
      this.$router.push({
        path: "/student/inLive_detail",
        query: { id: item },
      });
    },

    //课程获取
    async getLessonData() {
      const current = await getSubjectDetail({
        subjectId: this.subjectId,
      });
      // 设置标题
      this.titles = current.data.name;
      this.teacher = current.data.chapterLessons[0].teacherName;
      this.siginContent = current.data.remark;
      let result = [];
      if (current.data.chapterLessons) {
        current.data.chapterLessons.map((videoPath) => {
          result.push(videoPath);
        });
      }
      this.videoList = result;
      if (this.videoList.length > 1) {
        this.buttonShow = true;
      }
      // 设置视频播放
      this.videoConfigSetting(result[0].videoPath);
      this.showLink = result[0].id;
      (this.chapterlessonId = result[0].id), this.getMessage();
    },
    //播放器配置
    initPlayer() {
      var self = this;
      if (self.config.url && self.config.url !== "") {
        if (self.config.url.indexOf(".mp4") > -1) {
          self.player = new Player(self.config);
        } else if (self.config.url.indexOf(".m3u8") > -1) {
          self.player = new HlsJsPlayer(self.config);
        } else {
          self.player = new FlvJsPlayer(self.config);
        }
        if (self.config.autoplay == false) {
          self.player.reload();
        }
      }
    },
    /**
     * 设置播放器的配置
     * @param {Object} current 当前视频的信息
     */
    videoConfigSetting(url) {
      if (this.player) {
        this.player.destroy();
        this.player = null;
      }
      const configuration = {
        id: "mse",
        url: url,
        lang: "zh-cn",
        fluid: true,
        fitVideoSize: "auto",
        volume: 1,
        playbackRate: [0.5, 0.75, 1, 1.5, 2],
        defaultPlaybackRate: 1,
        autoplay: true,
        loop: false,
        videoInit: true,
        playsinline: true,
      };
      this.config = configuration;
      this.initPlayer();
    },
    async getMessage() {
      //获取留言列表
      await getLeavemessage({
        subjectId: this.subjectId,
        chapterlessonId: this.chapterlessonId,
        boundaryId: 0,
      }).then((res) => {
        if (res.code == 200) {
          this.qustionAllList = [];
          this.qustionList = res.data.dataList;
          this.qustionAllList = this.qustionList;

          let scroll = document.getElementById("scroll");
          setTimeout(() => {
            scroll.scrollTop = scroll.scrollHeight;
          }, 100);
        }
      });
    },
    //发送留言
    send() {
      if (this.messageContent.trim() == "") {
        this.msgError("提问不能为空！");
        return false;
      } else {
        this.sendBtn = true;
        leavemessage({
          subjectId: this.subjectId,
          chapterlessonId: this.chapterlessonId,
          content: this.messageContent,
        }).then((res) => {
          if (res.code == 200 && res.status == 1) {
            this.getMessage();
          } else {
            this.msgError(res.msg);
          }
          this.messageContent = "";
        });
      }
    },

    btnShow(index) {
      //展开
      if (index != this.textTab) {
        this.textTab = index;
      } else {
        this.textTab = -1;
      }
    },
    async changerCourse(item) {
      this.showLink = item;
      for (let i = 0; i < this.videoList.length; i++) {
        if (this.videoList[i].id == item) {
          this.videoConfigSetting(this.videoList[i].videoPath);
          this.chapterlessonId = this.videoList[i].id;
          await this.getMessage();
        }
      }
    },
  },
};
</script>
<style lang='scss' scoped>
@import "@/assets/styles/public.scss";
.topTitle {
  height: 44px;
  display: flex;
  background: #fff;
  line-height: 44px;
  margin-top: 20px;
  box-shadow: 0 2px 9px 3px rgba(0, 0, 0, 0.1);
}
.topHead {
  line-height: 44px;
  padding-left: 18px;
  border-radius: 5px;
  font-size: 15px !important;
  color: #555 !important;
}

.topInfo {
  line-height: 44px;
  font-size: 18px;
  padding-left: 20px;
  font-size: 15px;
  color: #555;
}
.tvBox {
  display: flex;
  background: #e1e3e8;
  padding: 18px 20px;
}
.pay {
  width: 70%;
  flex-grow: 0;
  flex-shrink: 0;
}
.wrapper {
  width: 100%;
  background: #333;
  border-radius: 5px;
  .tool {
    position: absolute;
    right: 25px;
    display: inline-block;
    padding: 10px 0;
    font-size: 32px;
  }
  .videoWrapper {
    border-radius: 5px;
  }
  #mse {
    width: 100%;
    // height: 100%;
    height: 485px !important;
  }
}
.switchUrl {
  position: absolute;
  top: 25px;
  padding-left: 10px;
}

.leave_msg {
  width: 38%;
  background: #fff;
  margin-left: 10px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  border-radius: 5px;
}
.leave_title {
  font-size: 20px;
  padding: 5px 15px;
  color: #009eef;
  border-bottom: 1px solid #ccc;
}

.infinite-list-wrapper {
  height: 287px;
  .list {
    list-style: none;
    margin: 0;
    padding: 0;
  }
}
.list-item {
  padding: 5px 0;
  margin: 10px 5px;
  display: flex;
  box-shadow: 0 2px 6px 0 rgba(0, 0, 0, 0.1);
}
.answer {
  margin-top: 5px;
  display: flex;
}
.send_box {
  border-top: 1px solid #ccc;
  height: 55px;
  padding: 10px 10px 10px;
  line-height: 33px;
  background-color: #fff;
}
.qustionItem {
  margin-left: 5px;
  text-align: justify;
  text-justify: newspaper;
  word-break: break-all;
  & > p {
    font-size: 12px;
    margin-top: 3px;
  }
}
.replay {
  color: #409eff;
  cursor: pointer;
  margin-left: 5px;
}
.section {
  padding-left: 10px;
  margin-top: 10px;
}
.linkActive {
  color: #409eff;
}
.load {
  text-align: center;
}

.noMore {
  text-align: center;
  color: #666;
  font-size: 12px;
}

.tabBox {
  background: #e1e3e8;
  padding: 18px 20px;
}
.tabTop {
  width: 100%;
  display: flex;
}
.tabItem {
  width: 114px;
  height: 42px;
  line-height: 42px;
  // border-radius: 5px;
  border-top-left-radius: 5px;
  border-top-right-radius: 5px;
  font-size: 18px;
  text-align: center;
  color: #444;
  font-weight: bolder;
  cursor: pointer;
}
.tabItem:hover {
  color: #fff;
}
.tabActive {
  background: #fe7a00;
  color: #fff;
}
.morecot {
  background: #f5f5f5;
  overflow: hidden;
  box-sizing: border-box;
  padding: 14px 16px;
}
.moreBox {
  margin-top: 10px;
  background: #fff;
  text-align: justify;
  text-justify: newspaper;
  word-break: break-all;
  width: 48%;
  box-shadow: 0 2px 6px 0 rgba(0, 0, 0, 0.1);
  float: left;
  display: flex;
  border: 1px solid #cfcfcf;
  border-radius: 5px;
  height: 180px;
  padding-right: 5px;
  margin-left: 15px;
  box-sizing: border-box;
  padding: 12px;
}
.mImgs {
  width: 208px;
  border-radius: 5px;
}
.mInfoBox {
  width: calc(100% -208);
}
.mInfo {
  padding-left: 10px;
  margin: 10px 0;
  font-size: 16px;
  color: #666;
}
.btnBox {
  padding-left: 10px;
  margin-top: 10px;
}
.intro {
  text-indent: 2em;
  font-size: 14px;
  word-wrap: break-word;
  word-break: normal;
  letter-spacing: 2px;
  color: #555;
  line-height: 20px;
  padding: 10px;
}
</style>