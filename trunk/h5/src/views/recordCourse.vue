<template>
  <div class="container">
    <header-top :title="title" :isShare="share"></header-top>
    <div class="pay">
      <div class="wrapper">
        <section class="videoWrapper">
          <div id="mse" style="backgroundcolor: 'rgba(0,0,0,0.87)'"></div>
        </section>
      </div>
    </div>
    <div>
      <div>
        <div style="border-bottom: 1px solid #ccc">
          <tab
            :line-width="3"
            :custom-bar-width="getBarWidth"
            active-color="#FF7A01"
            default-color="#A9A9A9"
            style="width: 70%"
            class="tab"
          >
            <tab-item selected @on-item-click="switchTabItem" class="tabItem"
              >课程简介</tab-item
            >
            <tab-item @on-item-click="switchTabItem" class="tabItem"
              >提问</tab-item
            >
            <tab-item @on-item-click="switchTabItem" class="tabItem"
              >章节</tab-item
            >
          </tab>
        </div>

        <div class="infoBox" v-show="isMsg == 0">
          <div class="infoTitle">
            <h4>课程：{{ courseName }}</h4>
            <h4>教师：{{ teacher }}</h4>
          </div>
          <p class="infoDetail"></p>
        </div>
        <div v-show="isMsg == 1">
          <ul class="ulBox" id="scroll" @scroll="loadMore()">
            <load-more
              style="margin-top: 0"
              v-show="load"
              :show-loading="true"
              :tip="'正在加载'"
            ></load-more>
            <li
              class="liItem"
              v-for="(item, index) in qustionAllList"
              :key="index"
            >
              <div class="userImg">
                <img :src="item.userFace" class="userImg" />
              </div>
              <div class="qustionItem">
                <p class="pItem">{{ item.userName }}</p>
                <p class="pItem">{{ item.content }}</p>
                <p class="pItem">
                  <span>{{ item.createTime }}</span>
                  <span
                    class="pMark"
                    v-show="item.children"
                    @click="lookQustion(index)"
                    >查看回复>></span
                  >
                </p>
                <div
                  v-show="index == textTab"
                  v-for="(v, i) in item.children"
                  :key="i"
                  style="display: flex; margin-top: 5px"
                >
                  <div class="userImg" style="margin-top: 5px">
                    <img :src="v.userFace" class="userImg" />
                  </div>
                  <div class="qustionItem">
                    <p class="pItem">{{ v.userName }}</p>
                    <p class="pItem">{{ v.content }}</p>
                    <p class="pItem">
                      <span>{{ v.createTime }}</span>
                    </p>
                  </div>
                </div>
              </div>
            </li>
          </ul>
          <x-input
            placeholder="请输入"
            class="serchInput"
            v-model="messageContent"
            :show-clear="false"
          >
            <x-button
              slot="right"
              type="text"
              class="right"
              :disabled="classState == -1 || classState == 0 || classState == 1"
              mini
              @click.native="send"
              >发送</x-button
            >
          </x-input>
        </div>
        <div class="infoBox" v-show="isMsg == 2">
          <p
            class="chapter"
            v-for="(item2, index) in videoList"
            :key="index"
            @click="changerCourse(item2.id)"
          >
            <span :class="showLink == item2.id ? 'linkActive' : ''">{{ item2.name }}</span>
          </p>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import headerTop from "@/components/header";
import { getSubjectDetail, getLeavemessage, leavemessage } from "@/utils/api";
import { Tab, TabItem, XInput, XButton, LoadMore } from "vux";
import Player from "xgplayer";
import FlvJsPlayer from "xgplayer-flv.js";
import HlsJsPlayer from "xgplayer-hls.js";
export default {
  name: "LiveCourse",
  components: {
    headerTop,
    Tab,
    TabItem,
    XInput,
    XButton,
    LoadMore,
  },
  data() {
    return {
      title: "直播课程",
      getBarWidth: function (index) {
        return 22 + "px";
      },
      allLoaded: false,
      share: true,
      //tab切换
      isMsg: 0,
      player: null,
      config: null,
      ws: null,
      playerReady: false,
      //限定轮次
      isScroll: true,
      teacher: "",
      IsFinish: false,
      textarea: "",
      courseName: "",
      subjectId: "",
      chapterlessonId: "",
      messageContent: "",
      qustionList: [],
      qustionAllList: [],
      //课程简介
      siginContent: "",
      //发送按钮状态
      sendBtn: true,
      //展开
      textTab: -1,
      //开始时间
      biginTime: "",
      //开始时间是否显示
      biginShow: false,
      //课程状态
      classState: "",
      //是否可以点击视频
      clickVideo: false,
      n: 1,
      //回复下拉
      textTab: -1,
      //加载动画
      load: false,
      //章节列表
      videoList: [],
      //章节选择
      showLink: "",
    };
  },
  created() {
    this.subjectId = this.$route.query.id;
  },
  mounted() {
    this.getLessonData();
  },
  destroyed() {
    Player.destroy();
    this.player = null;
  },
  methods: {
    //课程获取
    async getLessonData() {
      const current = await getSubjectDetail({
        subjectId: this.subjectId,
      });
      // 设置标题
      this.courseName = current.data.name;
      // this.biginTime = current.data.beginTime;
      this.siginContent = current.data.remark;
      this.teacher = current.data.chapterLessons[0].teacherName;
      // this.classState = current.data.classState;
      // let finish = current.data.classState == 3 ? true : false;
      // this.IsFinish = finish;
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
      // if (result[0].videoPath == null) {
      //   this.biginShow = true;
      // } else {
      //   this.biginShow = false;
      // }
      // 设置视频播放
      this.videoConfigSetting(result[0].videoPath);
      this.showLink = result[0].id;
      (this.chapterlessonId = result[0].id), this.getMessage();
    },
    //播放器配置
    initPlayer() {
      let self = this;
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
        autoplay: true,
        loop: false,
        videoInit: true,
        playsinline: true,
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
        this.toast("提问不能为空！");
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

    loadMore() {
      //加载
      let domScroll = document.getElementById("scroll");
      //let currentScroll
      if (
        domScroll.scrollTop <= 20 &&
        this.qustionAllList.length > 0 &&
        domScroll.scrollTop != 0
      ) {
        if (this.isScroll) {
          this.isScroll = false;
          this.load = true;
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
                  this.load = false;
                  for (let i = 0; i < listLength; i++) {
                    this.qustionAllList.unshift(this.qustionList[i]);
                  }
                } else {
                  domScroll.scrollTop = 0;
                  this.isScroll = false;
                  this.load = false;
                  this.noMore = true;
                  return false;
                }
              }, 1200);
            } else {
              this.toast(res.msg);
            }
          });
        }
      }
    },
    //发送留言
    send() {
      if (this.messageContent.trim() == "") {
        this.toast("提问不能为空！");
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
    //tab切换
    switchTabItem(index) {
      this.isMsg = index;
    },
    //展开
    lookQustion(index) {
      if (index != this.textTab) {
        this.textTab = index;
      } else {
        this.textTab = -1;
      }
    },
    //章节切换
    changerCourse(item) {
      this.showLink = item;
      for (let i = 0; i < this.videoList.length; i++) {
        if (this.videoList[i].id == item) {
          this.videoConfigSetting(this.videoList[i].videoPath);
          this.chapterlessonId = this.videoList[i].id;
          this.getMessage();
        }
      }
    },
  },
};
</script>
<style  scoped>
* {
  list-style: none;
  margin: 0;
  padding: 0;
}
.container {
  box-sizing: border-box;
  background: #f7f7f7;
  padding: 10px;
  height: 100vh;
}
.pay {
  flex-grow: 0;
  flex-shrink: 0;
  margin-top: 50px;
  box-sizing: border-box;
}
.wrapper {
  background: #fff;
  border-radius: 5px;
  margin-top: 5px;
}
.tool {
  position: absolute;
  right: 25px;
  display: inline-block;
  padding: 10px 0;
  font-size: 32px;
}
.title {
  display: inline-block;
  height: 80px;
  line-height: 80px;
  padding: 0 22px;
  font-size: 32px;
  color: #333;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}
.videoWrapper {
  height: 200px;
  border-radius: 5px;
  background: #333;
  overflow: hidden;
}
#mse {
  width: 100% !important;
  height: 100% !important;
}
.messageBox {
  margin-top: 10px;
  overflow: scroll;
}
.ulBox {
  height: calc(100vh - 370px);
  margin-top: 10px;
  overflow: auto;
  background: #fff;
  /* border: 1px solid red; */
}
.infoTitle {
  display: flex;
  margin-top: 10px;
  justify-content: center;
}
.infoTitle > h4 {
  font-size: 12px;
  color: #7c7a7b;
  margin-left: 15px;
}
.infoDetail {
  font-size: 10px;
  margin-top: 5px;
  text-indent: 2em;
  word-wrap: break-word;
  word-break: normal;
  text-align: left;
  color: #7c7a7b;
  letter-spacing: 2px;
}
.liItem {
  padding: 5px;
  margin: 10px 5px;
  display: flex;
  box-shadow: 0 2px 6px 0 rgba(0, 0, 0, 0.1);
}
.userImg {
  width: 32px;
  height: 32px;
  border-radius: 50%;
}
.qustionItem {
  margin-left: 5px;
  text-align: justify;
  text-justify: newspaper;
  word-break: break-all;
  text-align: left;
  font-size: 10px;
}
.pItem {
  margin-top: 2px;
  color: #7c7a7b;
}
.pMark {
  padding-left: 10px;
  color: #ff7a01;
}
.serchInput {
  background: #fff;
}
.right {
  background: #ff7a01;
}
.vux-tab .vux-tab-item {
  background: #f7f7f7 !important;
}
.textBox {
  border: 1px solid #ccc;
  height: 150px;
  margin-top: 10px;
  overflow: hidden;
}
.textConent {
  width: 150%;
  height: 100%;
  outline: none;
  border: none;
  resize: none;
}
.chapter {
  text-align: left;
  padding: 5px;
  font-size: 10px;
  color: #7c7a7b;
  margin-top: 5px;
}
.linkActive {
  color: #ff7a01;
}
@media screen and (min-width: 760px) {
  .videoWrapper {
    height: 400px !important;
  }
  .ulBox {
    height: 400px;
  }
  .textBox {
    height: 200px;
  }
}
.tabItem {
  background-color: #f7f7f7 !important;
}
</style>