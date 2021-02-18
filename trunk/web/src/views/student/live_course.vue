<template>
  <div class="container">
    <header-top />
    <nav-bar />
    <el-row>
      <el-col :span="16" :offset="4">
        <div class="grid-content search_box">
          <el-date-picker
            class="time"
            v-model="value"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
          >
          </el-date-picker>
          <div class="search_input">
            <el-input placeholder="请输入课程名称" v-model="input">
              <el-button
                slot="append"
                icon="el-icon-search"
                @click="handSearch"
              ></el-button>
            </el-input>
          </div>
        </div>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="16" :offset="4" class="course_box">
        <div class="timeLine">
          <div class="item" v-for="(item, index) in testList" :key="index">
            <div class="itemTime">{{ item.liveDate }}</div>
            <div class="itemContent" v-for="(v, i) in item.liveList" :key="i">
              <p class="liveTime">
                <span>{{ v.liveTimeRange }}</span>
              </p>
              <p class="itemTitle">{{ v.name }}</p>

              <p class="itemTeacher">
                <i class="el-icon-user"></i><span>{{ v.teacherName }}</span>
              </p>

              <p class="itemState" v-if="v.classState == -1">待报名</p>
              <p class="itemState" v-if="v.classState == 0">报名中</p>
              <p class="itemState" v-if="v.classState == 1">待上课</p>
              <p class="itemState" v-if="v.classState == 2" style="color:red;border:1px solid red">上课中</p>
              <p class="itemState" v-if="v.classState == 3">已结束</p>
              <p
                class="itemBtn"
                v-show="v.classState == -1 || v.classState == 1"
              >
                敬请期待
              </p>
              <p
                class="itemBtn"
                v-show="v.classState == 0 && v.registerStatus == 0"
                @click="regist(v.subjectId)"
              >
                报名
              </p>

              <p
                class="itemBtn"
                v-show="v.classState == 0 && v.registerStatus == 1"
              >
                已报名
              </p>
              <p
                class="itemBtn"
                v-show="v.classState == 2 && v.registerStatus == 1"
                @click="lookLive(v.subjectId)"
              >
                进入直播
              </p>
              <p
                class="itemBtn"
                v-show="v.classState == 3"
                @click="lookLive(v.subjectId)"
              >
                回放
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
import { getPreLiveList } from "@/api/student";
import { getToken } from "@/utils/auth";
// import { formatDate } from "@/utils";
export default {
  name: "live_course",
  components: {
    HeaderTop,
    NavBar,
    FooterBottom,
  },
  data() {
    return {
      value: "",
      input: "",
      currentPage: 1,
      testList: [],
      queryParams: {
        beginTime: "",
        endTime: "",
        name: "",
      },
      courseShow: false,
    };
  },
  mounted() {
    this.getList();
  },
  methods: {
    getList() {
      getPreLiveList(this.queryParams).then((res) => {
        if (res.code == 200) {
          this.testList = res.data;
        }
      });
    },
    lookLive(item) {
      //进入直播
      this.$router.push({
        path: "/student/inLive_course",
        query: { id: item },
      });
    },
    handSearch() {
      //搜索
      if (this.value != null) {
        this.queryParams.beginTime = this.value[0];
        this.queryParams.endTime = this.value[1];
      } else {
        this.queryParams.beginTime = "";
        this.queryParams.endTime = "";
      }
      this.queryParams.name = this.input;
      this.getList();
    },
    regist(item) {
      //报名
      if (getToken()) {
        this.$store
          .dispatch("RegistSubject", { subjectId: item })
          .then((res) => {
            if (res.code == 200 && res.status == 1) {
              this.msgSuccess("报名成功");
              this.getList();
            } else {
              
              this.msgError(res.msg);
            }
          });
      } else {
        this.$router.push("/");
      }
    },
  },
};
</script>
<style lang='scss' scoped>
@import "@/assets/styles/public.scss";
.search_box {
  width: 100%;
  background: #fff;
  height: 60px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  border-radius: 5px;
  margin-top: 20px;
  display: flex;
  justify-content: space-between;
}
.time {
  margin-top: 12px;
  border: none;
}
.search_input {
  margin-top: 12px;
  transform: translateX(-15px);
}
.course_box {
  margin-top: 20px;
  min-height: calc(100vh - 230px);
}
.item {
  margin-left: 30px; /*用左边margin显示时间线*/
  width: calc(100% - 30px); /*因为左边部分用于显示时间线，所以右边部分减去30px*/
  height: auto; /*高度由内容决定*/
  position: relative;
  margin-bottom: 10px;
  cursor: pointer;
  padding-left: 10px;
  padding-bottom: 10px;
}
.item::before {
  content: "";
  width: 13px;
  height: 13px;
  border-radius: 100%;
  background-color: #91c2fc;
  position: absolute;
  left: -15px;
}
.item::after {
  content: "";
  width: 3px;
  height: calc(100% + 10px); /*加上10px是item底部的margin*/
  background-color: #91c2fc;
  position: absolute;
  top: 0;
  left: -10px;
}
.item:last-of-type::after {
  width: 0px;
}
.item:hover::before {
  transform: scale3d(1.2, 1.2, 1);
  background-color: #ffba00;
}

.item:hover::after {
  transform: scale3d(1.1, 1, 1);
  background-color: #ffba00;
}
.itemTime {
  color: #ffba00;
  font-size: 16px;
}
.itemContent {
  font-size: 14px;
  margin-top: 10px;
  width: 100%;
  min-height: 50px;
  line-height: 50px;
  background: #fff;
  padding-left: 10px;
  color: #666;
  display: flex;
  border-radius: 5px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  justify-content: space-around;
}
.itemContent:hover {
  background: #ddd;
}
.itemTitle {
  width: 45%;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.itemTeacher {
  width: 15%;
  text-align: left;
}
.itemState {
  width: 100px;
  text-align: center;
  border: 1px solid rgb(97, 190, 139);
  color: rgb(97, 190, 139);
  height: 30px;
  line-height: 30px;
  padding: 0 5px;
  border-radius: 5px;
  margin-top: 10px;
}
.itemBtn {
  border: 1px solid #ccc;
  height: 30px;
  line-height: 30px;
  padding: 0 10px;
  border-radius: 5px;
  margin-top: 10px;
  background: #ffba00;
  color: #fff;
}
.itemBtn:hover {
  border-color: #ffba00;
  color: #fff;
  transform: scale(1.2);
}
.liveTime {
  width: 10%;
  // padding-right: 10px;
}
</style>