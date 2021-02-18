<template>
  <div class="container">
    <tab
      :line-width="3"
      :custom-bar-width="getBarWidth"
      active-color="#FF7A01"
      default-color="#A9A9A9"
    >
      <tab-item selected @on-item-click="switchTabItem">直播中心</tab-item>
      <tab-item @on-item-click="switchTabItem">课程中心</tab-item>
    </tab>
    <div v-show="isLive">
      <div class="searchBox">
        <x-input
          placeholder="输入课程名称"
          class="serchInput"
          :show-clear="false"
          v-model="courseName"
        >
          <img
            slot="label"
            style="padding-right: 10px; display: block"
            src="@/assets/image/course/searchIcon.png"
            width="15"
            height="15"
          />
          <x-button
            slot="right"
            type="text"
            class="right"
            mini
            @click.native="nameSearch"
            >搜索</x-button
          >
        </x-input>

        <div class="inputBox">
          <input
            type="text"
            @focus="pickTimeOpen"
            id="start"
            class="inputs"
            placeholder="开始时间"
            v-model="startTime"
          />
          <span>至</span>
          <input
            type="text"
            @focus="pickTimeOpen"
            class="inputs"
            placeholder="结束时间"
            id="end"
            v-model="endTime"
          />
        </div>
        <x-button class="searchBtn" type="text" mini @click.native="search"
          >搜索</x-button
        >
      </div>
      <ul class="listBox">
        <li v-for="(item, index) in preLiveList" :key="index">
          <p class="courseTime">{{ item.liveDate }}</p>
          <!--  -->
          <div class="listItem" v-for="(v, i) in item.liveList" :key="i">
            <p class="mark">
              <span v-if="v.classState == -1">待报名</span>
              <span v-if="v.classState == 0">报名中</span>
              <span v-if="v.classState == 1">待上课</span>
              <span v-if="v.classState == 2" style="color: red">上课中</span>
              <span v-if="v.classState == 3">已下课</span>
            </p>
            <img
              :src="require('@/assets/image/index/play.png')"
              alt=""
              class="playeIcon"
            />
            <img :src="v.imagePath" class="listImg" />
            <div class="infoBox">
              <p class="listInfo">{{ v.name }}</p>
              <p class="listInfo">主讲教师：{{ v.teacherName }}</p>
              <p class="listInfo">上课时间：{{ v.liveTimeRange }}</p>
              <p class="listInfo">报名人数：{{ v.signUpCounts }}</p>
              <p class="btnBox">
                <mt-button
                  class="btn"
                  :disabled="v.registerStatus == 0 && v.classStatus == 2"
                  v-if="v.classStatus != 3"
                  @click="liveCourse(v.subjectId)"
                  >进入直播</mt-button
                >
                <mt-button
                  class="btn"
                  v-if="v.classStatus == 3"
                  @click="liveCourse(v.subjectId)"
                  >回放</mt-button
                >
                <mt-button
                  class="btn"
                  v-if="v.registerStatus == 0 && v.classStatus == 0"
                  >报名</mt-button
                >
              </p>
            </div>
          </div>
        </li>
      </ul>
    </div>

    <div class="courseBox" v-show="!isLive">
      <div class="labelBox">
        <x-input
          placeholder="输入课程名称"
          class="serchInput"
          :show-clear="false"
          v-model="queryParams.name"
        >
          <img
            slot="label"
            style="padding-right: 10px; display: block"
            src="@/assets/image/course/searchIcon.png"
            width="15"
            height="15"
          />
          <x-button
            slot="right"
            type="text"
            class="right"
            mini
            @click.native="subjName"
            >搜索</x-button
          >
        </x-input>

        <div class="courseCheck">
          <span class="checkTile">年级</span>
          <div class="chcked" id="grad" @click="showShare">
            <p v-text="grad">全部</p>
            <img
              src="@/assets/image/course/downIcon.png"
              style="margin-top: 8px"
              height="15"
              width="15"
            />
          </div>
        </div>
        <div class="courseCheck">
          <span class="checkTile">学科</span>
          <div class="chcked" id="course" @click="showShare">
            <p v-text="course">全部</p>
            <img
              src="@/assets/image/course/downIcon.png"
              style="margin-top: 8px"
              height="15"
              width="15"
            />
          </div>
        </div>
      </div>
      <ul class="listBox" @scroll="loadMore" id="scroll">
        <li class="listItem" v-for="(item, index) in subjectList" :key="index">
          <img :src="item.imagePath" class="listImg" />
          <div class="">
            <p class="listInfo">{{ item.name }}</p>
            <p class="listInfo">播放量：{{ item.playCount }}</p>
            <p class="listInfo">课时：{{ item.classHour }}</p>
            <p class="listInfo" style="display: flex">
              <span>评价</span>
              <rater
                v-model="item.commentScore"
                :font-size="12"
                disabled
              ></rater>
            </p>
            <p class="btnBox">
              <mt-button class="btn" @click="recordCourse(item.id)"
                >观看</mt-button
              >
              <mt-button
                class="btn"
                v-show="item.evaluateStatus == 1"
                @click="openEvaluate(item.id)"
                >评价</mt-button
              >
            </p>
          </div>
        </li>
        <load-more
          style="margin-top: 10px"
          v-show="load"
          :show-loading="true"
          :tip="'正在加载'"
        ></load-more>
        <p v-if="noMore" class="noMore">没有更多了</p>
      </ul>
    </div>
    <!-- 底部弹出 -->
    <mt-popup class="sharePopup" v-model="popupVisible" position="bottom">
      <ul class="shareUl">
        <li
          v-for="(item, index) in pullDownList"
          @click="chooseDom($event)"
          :key="index"
          :data-name="item.name"
          :id="item.id"
        >
          {{ item.name }}
        </li>
      </ul>
      <div class="shareFooter">
        <mt-button
          type="primary"
          style="width: 100vw; margin-top: 10px"
          size="small"
          @click="hiddenShare()"
          >确认</mt-button
        >
      </div>
    </mt-popup>
    <mt-datetime-picker
      ref="picker"
      v-model="pickTime"
      type="date"
      year-format="{value}"
      month-format="{value}"
      date-format="{value}"
      @confirm="handleTime"
    >
    </mt-datetime-picker>
    <mt-popup v-model="rateShow" popup-transition="popup-fade">
      <div class="rateBox">
        <p class="rateTitle">评价</p>
        <rater v-model="evaluateInfo.evaluateLevel"></rater>
        <group>
          <x-textarea
            v-model="evaluateInfo.content"
            style="color: #666"
            :max="200"
          ></x-textarea>
        </group>
        <mt-button
          style="margin-top: 10px"
          type="primary"
          size="small"
          @click.native="sendEvaluateInfo"
          >确定</mt-button
        >
      </div>
    </mt-popup>
  </div>
</template>
<script>
import {
  getPreLiveList,
  getSubjectList,
  gradeOptions,
  courseOptions,
  evaluateSubject,
} from "@/utils/api";
import {
  Rater,
  XTextarea,
  Group,
  Tab,
  TabItem,
  XInput,
  XButton,
  LoadMore,
} from "vux";
export default {
  name: "Course",
  components: {
    Rater,
    XTextarea,
    Group,
    Tab,
    TabItem,
    XInput,
    XButton,
    LoadMore,
  },
  data() {
    return {
      isLive: true,
      getBarWidth: function (index) {
        return (index + 1) * 22 + "px";
      },
      isTrue: false,
      startTime: "",
      endTime: "",
      courseName: "",
      pickObj: "",
      pickTime: new Date(),
      //直播列表
      preLiveList: [],
      //课程中心参数
      queryParams: {
        pageIndex: 1,
        pageSize: 12,
        name: "",
        courseId: "",
        gradeId: "",
      },
      //课程中心对象
      subObj: "",
      //课程中心列表
      subjectList: [],
      //年级下拉
      gradeOptions: [],
      //学科下拉
      courseOptions: [],
      //下拉数组
      pullDownList: [],
      grad: "全部",
      course: "全部",
      //选中的名字
      checkName: "",
      //向右横拉
      rateShow: false,
      popupVisible: false,
      //评价
      evaluateInfo: {
        subjectId: "",
        evaluateLevel: 1,
        content: "",
      },
      //限定轮次
      isScroll: true,
      //显示加载动画
      load: false,
      //显示没有更多
      noMore: false,
    };
  },
  created() {
    this.search();
    this.getCourseOptions();
    this.getGradeOptions();
    this.loading.close();
  },
  methods: {
    switchTabItem(index) {
      if (index == 0) {
        this.isLive = true;
      } else {
        this.isLive = false;
        this.getList();
      }
    },
    //开始时间弹窗
    pickTimeOpen(event) {
      document.activeElement.blur();
      this.$refs.picker.open();
      this.pickObj = event.target.id;
      this.pickTime = new Date();
    },
    //时间选择
    handleTime(event) {
      let date = this.formatDate(event);
      if (this.pickObj == "start") {
        this.startTime = date;
      } else {
        let start = new Date(this.startTime);
        if (this.startTime == "" || event.getTime() < start.getTime()) {
          this.toast("时间选择错误");
          return false;
        }
        this.endTime = date;
      }
    },
    //获取直播中心list
    getPreList(parmas) {
      getPreLiveList(parmas).then((res) => {
        this.loading.close();
        if (res.code == 200) {
          this.preLiveList = res.data;
        } else {
          this.toast(res.msg);
        }
      });
    },

    //搜素
    search() {
      this.loading.open();
      let parmas = {
        beginTime: this.startTime,
        endTime: this.endTime,
        name: "",
      };
      this.getPreList(parmas);
    },
    //名称搜素
    nameSearch() {
      this.loading.open();
      this.startTime = "";
      this.endTime = "";
      let parmas = {
        beginTime: "",
        endTime: "",
        name: this.courseName,
      };
      this.getPreList(parmas);
    },
    //获取学科
    getCourseOptions() {
      courseOptions().then((res) => {
        if (res.code == 200) {
          this.courseOptions = res.data;
          this.courseOptions.unshift({ id: "", name: "全部" });
        } else {
          toast(res.msg);
        }
      });
    },
    //获取年级
    getGradeOptions() {
      gradeOptions().then((res) => {
        if (res.code == 200) {
          this.gradeOptions = res.data;
          this.gradeOptions.unshift({ id: "", name: "全部" });
        } else {
          toast(res.msg);
        }
      });
    },
    //选择弹窗
    showShare(e) {
      this.subObj = e.currentTarget.id;
      if (this.subObj == "grad") {
        this.pullDownList = this.gradeOptions;
      } else if (this.subObj == "course") {
        this.pullDownList = this.courseOptions;
      }
      this.popupVisible = true;
    },
    //选择选项
    chooseDom(e) {
      let name = e.target.dataset.name;
      let id = e.target.id;
      this.checkName = name;
      if (this.subObj == "grad") {
        this.queryParams.gradeId = id;
      } else {
        this.queryParams.courseId = id;
      }
    },
    //取消弹窗
    hiddenShare() {
      this.loading.open();
      this.pullDownList = "";
      if (this.subObj == "grad") {
        this.grad = this.checkName;
      } else if (this.subObj == "course") {
        this.course = this.checkName;
      }
      this.getList();
      this.popupVisible = false;
    },
    //获取课程中心列表
    getList() {
      this.noMore = false;
      getSubjectList(this.queryParams).then((res) => {
        this.loading.close();
        if (res.data) {
          this.subjectList =[];
          this.subjectList = res.data.dataList;
        } else {
          this.toast(res.msg);
        }
      });
    },
    //课程中心名称搜素
    subjName() {
      this.loading.open();
      this.grad = "全部";
      this.course = "全部";
      (this.queryParams.courseId = ""),
        (this.queryParams.gradeId = ""),
        this.getList();
    },
    //点击评价
    openEvaluate(id) {
      this.rateShow = true;
      this.evaluateInfo.content = "";
      this.evaluateInfo.evaluateLevel = 1;
      this.evaluateInfo.subjectId = id;
    },
    //发布评价
    sendEvaluateInfo() {
      //发布评价
      this.rateShow = false;
      if (this.evaluateInfo.content == "") {
        this.toast("评价内容不能为空");
      } else {
        evaluateSubject(this.evaluateInfo).then((res) => {
          if (res.code == 200 && res.status == 1) {
            this.toast("评价成功");
            this.getList();
          } else {
            this.toast(res.msg);
          }
        });
      }
    },
    //直播
    liveCourse(item) {
      this.$router.push({
        path: "/liveCourse",
        query: { id: item },
      });
    },
    //录播
    recordCourse(item) {
      this.$router.push({
        path: "/recordCourse",
        query: { id: item },
      });
    },
    //加载更多
    loadMore() {
      let scrollDom = document.getElementById("scroll");
      //判断是否滚到底部
      if (
        scrollDom.scrollHeight - scrollDom.scrollTop ==
        scrollDom.clientHeight
      ) {
        if (this.isScroll) {
          this.isScroll = false;
          this.load = true;
          this.noMore = false;
          this.queryParams.pageIndex += 1;
          getSubjectList(this.queryParams).then((res) => {
            if (res.code == 200) {
              let newArr = [];
              newArr = res.data.dataList;
              if (newArr.length > 0) {
                this.isScroll = true;
                this.load = false;
                newArr.forEach((item, index) => {
                  this.subjectList.push(item);
                });
              } else {
                this.isScroll = false;
                this.load = false;
                this.noMore = true;
                 return false
              }
            } else {
              this.toast(res.msg);
            }
          });
        }
      }
    },

    //时间转换
    formatDate(date) {
      // 格式化日期为 y-m-d
      let y = date.getFullYear();
      let m = date.getMonth() + 1;
      m = m < 10 ? "0" + m : m;
      let d = date.getDate();
      d = d < 10 ? "0" + d : d;
      return y + "-" + m + "-" + d;
    },
  },
};
</script>
<style scoped>
@import "../assets/style/public.css";

.searchBox {
  display: flex;
  margin-top: 15px;
  justify-content: space-between;
  padding: 0 5px;
}
.inputBox {
  width: 45%;
  font-size: 10px;
  color: #303030;
  display: flex;
  line-height: 30px;
  border: 1px solid #ccc;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  height: 25px;
  margin-left: 5px;
  border-radius: 4px;
}
.inputs {
  width: 45%;
  border: none;
  outline: none;
  height: 25px;
  text-align: center;
  font-size: 10px;
}
.serchInput {
  width: 38%;
  height: 25px;
  font-size: 10px !important;
  padding: 0 !important;
  border: 1px solid #ff7a01;
  border-radius: 30px;
}
.weui-input::-webkit-input-placeholder {
  font-size: 10px !important;
}
.searchBtn {
  font-size: 10px;
  padding: 0 5px;
  background: #ff7a01;
  color: #fff;
  height: 25px;
}
.right {
  height: 25px;
  font-size: 10px;
  border-radius: 30px;
  background: #ff7a01;
  color: #fff;
  padding: 0 5px;
}
.listBox {
  box-sizing: border-box !important;
  padding: 0 10px;
  height: calc(100vh - 150px);
  padding-bottom: 10px;
  overflow-y: scroll !important;
}
.listItem {
  height: 32vw;
}
.labelBox {
  display: flex;
  justify-content: space-around;
  margin: 15px 0;
}
.checkTile {
  font-weight: bolder;
  color: #757575;
}
.courseCheck {
  display: flex !important;
  font-size: 10px !important;
  line-height: 30px;
  color: #757575;
}
.chcked {
  display: flex;
  padding: 0 5px;
  background: #fff;
  border-radius: 4px;
  margin-left: 3px;
}
.shareUl {
  width: 100%;
  max-height: 200px;
  border-radius: 5px;
  overflow: scroll;
  background: #fff;
}
.shareUl > li {
  width: 100vw;
  height: 30px;
  line-height: 30px;
  background: #fff;
}
.shareUl > li:hover {
  background: #26a2ff;
  color: #fff;
}
.rateBox {
  width: 80vw;
  padding: 10px 0;
  border-radius: 5px;
}
.rateTitle {
  margin-bottom: 10px;
  color: #666;
}
.courseTime {
  text-align: left;
  margin-top: 20px !important;
  padding-left: 10px;
  color: #ff7a01;
  font-weight: bolder;
}
</style>