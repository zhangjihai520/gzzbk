<template>
  <div class="container">
    <header-top :title="title"></header-top>
    <div class="searchBox">
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
          @click.native="getList"
          >搜索</x-button
        >
      </x-input>
    </div>

    <ul class="listBox" @scroll="loadMore" id="scroll">
      <li class="listItem" v-for="(item, index) in courseList" :key="index">
        <p class="mark" v-show="item.classState == 0 || item.classState == 1">
          <span>已报名</span>
        </p>
        <img :src="item.imagePath" class="listImg" />
        <div>
          <p class="listInfo">课程：{{ item.name }}</p>
          <p class="listInfo">讲师：{{ item.teacherName }}</p>
          <p class="btnBox">
            <mt-button
              class="btn"
              v-if="item.classState == 2"
              @click="lookLive(item.id)"
              size="small"
              >进入直播</mt-button
            >
            <mt-button
              class="btn"
              v-if="item.classState == 3 && item.category == 1"
              @click="lookLive(item.id)"
              size="small"
              >回放</mt-button
            >
            <mt-button
              class="btn"
              v-if="item.classState == 3 && item.category == 2"
              @click="lookBack(item.id)"
              size="small"
              >观看</mt-button
            >
            <mt-button
              class="btn"
              v-if="item.classState == 3 && item.evaluateInfo == null"
              size="small"
              @click="openEvaluate(item.id)"
              >评价</mt-button
            >
            <mt-button
              class="btn"
              v-if="item.classState == 3 && item.evaluateInfo != null"
              size="small"
              >查看评价</mt-button
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
import headerTop from "@/components/header";
import { mySubjectList, evaluateSubject } from "@/utils/api";
import { XInput, XButton,LoadMore,Rater,Group,XTextarea} from "vux";
export default {
  name: "CourseList",
  components: {
    headerTop,
    XInput,
    XButton,
    LoadMore,
    Rater,
    Group,
    XTextarea
  },
  data() {
    return {
      title: "我的课程",
      queryParams: {
        name: "",
        pageIndex: 1,
        pageSize: 12,
      },
      courseList: [],
      //评价弹窗
      rateShow: false,
      //评价信息
      evaluateInfo: {
        subjectId: "",
        evaluateLevel: 0,
        content: "",
      },
      //加载动画
      load:false,
      //加载更多
      noMore:false,
      //限定轮次
      isScroll: true,
    };
  },
  created() {
    this.loading.open()
    this.getList();
  },
  methods: {
    //获取列表
    getList() {
      this.loading.close();
      mySubjectList(this.queryParams).then((res) => {
        if (res.code == 200) {
          this.courseList =[];
          this.courseList = res.data.dataList;
        } else {
          this.toast(res.msg);
        }
      });
    },
    //点击评价
    openEvaluate(id) {
      this.rateShow = true;
      this.evaluateInfo.content = "";
      this.evaluateInfo.evaluateLevel = 1;
      this.evaluateInfo.subjectId = id;
    },
    showEvaluateInfo(item) {
      //查看评价
      this.rateShow = true;
      this.evaluateInfo.content = item.content;
      this.isAbled = true;
      this.evaluateInfo.evaluateLevel = Number(item.evaluateLevel);
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
    lookLive(item) {
      //直播回放
      this.$router.push({
        path: "/liveCourse",
        query: { id: item },
      });
    },
    lookBack(item) {
      //观看
      this.$router.push({
        path: "/recordCourse",
        query: { id: item },
      });
    },
    //加载更多
    loadMore(){
      let scrollDom = document.getElementById("scroll");
      //判断是否滚到底部
      if (
        scrollDom.scrollHeight - scrollDom.scrollTop ==
        scrollDom.clientHeight
      ){
        if(this.isScroll){
          this.isScroll = false;
          this.load = true;
          this.noMore = false;
          this.queryParams.pageIndex += 1;
          mySubjectList(this.queryParams).then(res =>{
            if(res.code ==200){
              let newArr = [];
              newArr = res.data.dataList;
              if (newArr.length > 0){
                this.isScroll = true;
                this.load = false;
                newArr.forEach((item, index) => {
                  this.courseList.push(item);
                });
              }else{
                this.isScroll = false;
                this.load = false;
                this.noMore = true;
                return false
              }
            }
          })
        }
      }
    }
  },
};
</script>
<style  scoped>
@import "../assets/style/public.css";
.searchBox {
  margin-top: 50px;
  display: flex;
  justify-content: space-around;
  box-sizing: border-box;
  padding: 10px;
}
.serchInput {
  width: 80%;
  height: 25px;
  font-size: 10px !important;
  padding: 0 !important;
  border: 1px solid #ff7a01;
  border-radius: 30px;
}
.inputBox {
  width: 83%;
  display: flex;
  line-height: 30px;
  border: 1px solid #ccc;
  height: 34px;
  padding: 2px 0;
  border-radius: 4px;
}
.right {
  height: 25px;
  font-size: 10px;
  border-radius: 30px;
  background: #ff7a01;
  color: #fff;
  padding: 0 15px;
}
.listBox {
  box-sizing: border-box !important;
  padding: 0 10px;
  height: calc(100vh - 100px);
  padding-bottom: 10px;
  overflow-y: scroll !important;
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
</style>