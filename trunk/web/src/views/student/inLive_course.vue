<template>
  <div class="container">
    <header-top />
    <nav-bar />
    <el-row>
      <el-col :span="16" :offset="4" class="topTitle">
        <el-page-header
          class="topHead"
          @back="goBack"
          :content="'课程：' + title"
        >
        </el-page-header>
        <div class="topInfo" v-show="teacher">教师：{{ teacher }}</div>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="16" :offset="4" class="tvBox">
        <div class="switchUrl" v-show="buttonShow" @click="handleSwitch()">
          <!-- <img alt="切换画面" src="@/assets/image/switch.png" /> -->
        </div>
        <div class="pay">
          <div class="wrapper">
            <section class="videoWrapper">
              <div id="mse" style="backgroundcolor: 'rgba(0,0,0,0.87)'"></div>
              <p class="warnTime" v-show="biginShow">
                <span>直播未开始</span>
                <br />
                <span style="display: inline-block; padding-top: 10px"
                  >开始时间：{{ biginTime }}</span
                >
              </p>
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
                <!--  -->
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
                    <!-- <el-avatar :size="20" :src="item.userFace"></el-avatar> -->
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
                        class="answer"
                        v-for="(v, i) in item.children"
                        :key="i"
                        v-show="index == textTab"
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
                  :disabled="
                    classState == -1 || classState == 0 || classState == 1
                  "
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
        <div v-show="isTable" style="margin-top: 10px">
          <p class="intro">{{ siginContent == "" ? "暂无" : siginContent }}</p>
        </div>
        <div v-show="!isTable" class="morecot">
          <p class="intro" v-show="moreList.length == 0">暂无</p>
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
      </el-col>
    </el-row>
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
  name: "inLive_course",
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
      player: null,
      config: null,
      wsUrl: "",
      ws: null,
      playerReady: false,
      loading: false,
      activeName: "first",
      topActive: "1",
      teacher: "",
      IsFinish: false,
      textarea: "",
      title: "",
      subjectId: "",
      chapterlessonId: "",
      messageContent: "",
      qustionList: [],
      qustionAllList: [],
      videoList: [],
      noMore: false,
      sendBtn: true,
      textTab: -1,
      //更多视频
      moreList: [],
      //简介tab显示
      isTable: true,
      //简介
      siginContent: "",
      //开始时间
      biginTime: "",
      //开始时间是否显示
      biginShow: false,
      //课程状态
      classState: "",
      //是否可以点击视频
      clickVideo: false,
      n: 1,
    };
  },
  created() {
    this.subjectId = this.$route.query.id;
  },
  mounted() {
    this.getLessonData();
  },
  destroy() {
    // this.ws.close();
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
    handleClick(item) {
      this.$router.push({
        path: "/student/inLive_detail",
        query: { id: item },
      });
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
    handleSwitch() {},

    //课程获取
    async getLessonData() {
      const current = await getSubjectDetail({
        subjectId: this.subjectId,
      });
      // 设置标题
      // document.title = current.data.name;
      this.title = current.data.name;
      this.biginTime = current.data.beginTime;
      this.siginContent = current.data.remark;
      this.teacher = current.data.chapterLessons[0].teacherName;
      this.classState = current.data.classState;
      let finish = current.data.classState == 3 ? true : false;
      this.IsFinish = finish;
      let result = [];
      if (current.data.chapterLessons) {
        current.data.chapterLessons.map((videoPath) => {
          result.push(videoPath);
        });
      }
      if (current.data.PPTVideoUrl) {
        current.data.PPTVideoUrl.map((item) => {
          result.push(item);
        });
      }
      this.videoList = result;
      if (result[0].videoPath == null) {
        this.biginShow = true;
      } else {
        this.biginShow = false;
      }
      // 设置视频播放
      this.videoConfigSetting(result[0].videoPath);
      (this.chapterlessonId = result[0].id), this.getMessage();
      let protocol = location.protocol === "https:" ? "wss:" : "ws:";
      let url =
        protocol +
        "//sskt.nceduc.cn/api/chat/" +
        // "//192.168.50.17:8080/api/chat/" +
        current.Channel +
        "/" +
        current.Uid +
        "_H5";
    },
    //播放器配置
    initPlayer() {
      var self = this;
      if (self.config.url && self.config.url !== "") {
        if (self.config.url.indexOf(".mp4") > -1) {
          this.clickVideo = true;
          self.player = new Player(self.config);
        } else if (self.config.url.indexOf(".m3u8") > -1) {
          self.player = new HlsJsPlayer(self.config);
        } else {
          self.player = new FlvJsPlayer(self.config);
        }
        self.player.once("play", function (pl) {
          self.waitTimeZone = 0;
        });
        self.player.once("ready", function (pl) {
          self.playerReady = true;
        });
        self.player.on("playing", function (pl) {
          if (self.playTimeZone == 0) {
            self.playTimeZone = new Date().getTime();
          }
          if (self.waitTimeZone > 0) {
            var dt = new Date().getTime() - self.waitTimeZone;
            self.devTimeZone += dt;
            self.waitTimeZone = 0;
          }
        });
        self.player.on("exitFullscreen", function () {
          self.playTimeFlag = 1;
          self.waitTimeZone = 0;
          self.devTimeZone = 0;
          self.player.reload();
        });
        self.player.on("waiting", function () {
          self.waitTimeZone = new Date().getTime();
        });
        if (self.config.autoplay) {
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
        autoplay: true,
        loop: false,
        videoInit: true,
        playsinline: this.clickVideo,
        closeVideoClick: this.clickVideo,
        closeVideoTouch: true,
        closeVideoDblclick: true,
        ignores: ["play", "time", "progress", "replay"],
      };

      this.config = configuration;
      this.initPlayer();
    },
    getMessage() {
      //获取留言列表
      getLeavemessage({
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
      }
      this.sendBtn = true;
      leavemessage({
        subjectId: this.subjectId,
        chapterlessonId: this.chapterlessonId,
        content: this.messageContent,
      }).then((res) => {
        if (res.code == 200 && res.status == 1) {
          this.getMessage();
          this.noMore = false;
          this.isScroll = true;
        } else {
          this.msgError(res.msg);
        }
        this.messageContent = "";
      });
    },
    btnShow(index) {
      //展开
      if (index != this.textTab) {
        this.textTab = index;
      } else {
        this.textTab = -1;
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
  position: relative;
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
    // height: 420px;
    border-radius: 5px;
    overflow: hidden;
  }
  #mse {
    width: 100%;
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
.warnTime {
  width: 70%;
  background: #666;
  font-size: 22px;
  color: #fff;
  position: absolute;
  bottom: 200px;
  left: 15%;
  padding: 20px;
  border-radius: 60px;
  text-align: center;
}
.intro {
  text-indent: 2em;
  font-size: 14px;
  word-wrap: break-word;
  word-break: normal;
  letter-spacing: 2px;
  color: #555;
  line-height: 20px;
}
</style>