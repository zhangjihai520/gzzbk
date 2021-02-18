<template>
  <div class="container">
    <header-top />
    <nav-bar />
    <el-row>
      <el-col :span="16" :offset="4">
        <div class="grid-content search_box">
          <el-input
            size="60"
            clearable
            placeholder="请输入课程名称"
            v-model="queryParams.name"
            @clear="getList"
          >
            <el-button
              slot="append"
              icon="el-icon-search"
              @click="getList"
            ></el-button>
          </el-input>
        </div>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="16" :offset="4">
        <div class="grid-content class_box">
          <span class="titles" >年级：</span>
          <div
            class="class_item"
            v-for="(item, index) in classList"
            :key="index"
            :class="{ active: currentSort == item.id }"
            @click="active(item.id)"
          >
            {{ item.name }}
          </div>
        </div>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="16" :offset="4">
        <div class="grid-content class_box">
          <span class="titles">学科：</span>
          <div
            class="class_item"
            v-for="(item, index) in courseList"
            :key="index"
            :class="{ active: currentCourse == item.id }"
            @click="active2(item.id)"
          >
            {{ item.name }}
          </div>
        </div>
      </el-col>
    </el-row>
    <el-row>
      <el-col
        :span="16"
        :offset="4"
        class="course_box"
        v-loading="fullscreenLoading"
      >
        <div
          class="grid-content course_item"
          v-for="(item, index) in subjectList"
          :key="index"
          :class="index % 4 == 0 ? '' : 'left'"
        >
          <div class="clearfix">
            <p class="playNum">播放量：{{ item.playCount }}</p>
            <el-image class="coures_img" :src="item.imagePath"></el-image>
            <div>
              <p class="courserInfo">
                课程名称：<span>{{ item.name }}</span>
              </p>
              <p class="courserInfo">
                课时：<span>{{ item.classHour }}</span>
              </p>
              <p class="courserInfo" style="display: flex">
                <span>评分：</span>
                <el-rate
                  v-model="item.commentScore"
                  disabled
                  show-score
                  text-color="#ff9900"
                >
                </el-rate>
              </p>
              <el-button
                type="warning"
                class="cars_btn"
                @click="lookLive(item.id)"
                >观看</el-button
              >
              <el-button
                type="warning"
                class="cars_btn"
                @click="openEvaluate(item.id)"
                v-show="item.evaluateStatus == 1"
                >评价</el-button
              >
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageIndex"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
      style="background: #f4f6f5; margin-right: 20%"
    />
    <footer-bottom />

    <!-- 评价 -->
    <el-dialog title="评价" :visible.sync="dialogFormVisible2">
      <el-form>
        <div class="block" style="display: flex">
          <span class="demonstration">评分：</span>
          <el-rate v-model="evaluateInfo.evaluateLevel" show-score text-color="#ff9900" ></el-rate>
        </div>
        <el-form-item style="margin-top: 5px">
          <el-input
            type="textarea"
            placeholder="请输入内容"
            v-model="evaluateInfo.content"
            maxlength="200"
            show-word-limit
            class="textarea"
            resize="none"
            rows="5"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible2 = false">取 消</el-button>
        <el-button type="warning" @click="sendEvaluateInfo">发布</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import HeaderTop from "./components/header_top";
import NavBar from "./components/nav_bar";
import FooterBottom from "@/components/footer";
import { getSubjectList } from "@/api/student";
import { gradeOptions, courseOptions } from "@/api/common";
import { getToken } from "@/utils/auth";
export default {
  name: "course_contant",
  components: {
    HeaderTop,
    NavBar,
    FooterBottom,
  },
  data() {
    return {
      input: "",
      currentSort: "",
      currentCourse: "",
      fullscreenLoading: false,
      dialogFormVisible2: false,
      queryParams: {
        pageIndex: 1,
        pageSize: 12,
        name: "",
        courseId: "",
        gradeId: "",
      },
      total: 0,
      classList: [],
      courseList: [],
      subjectList: [],
      //评价
      evaluateInfo: {
        subjectId: "",
        evaluateLevel: 1,
        content: "",
      },
    };
  },
  created() {
    this.getGradList();
    this.getCourseList();
    this.getList();
  },
  methods: {
    //年级下拉
    getGradList() {
      gradeOptions().then((res) => {
        if (res.code == 200) {
          this.classList = res.data;
          this.classList.unshift({
            id: "",
            name: "全部",
          });
        }
      });
    },
    //学科下拉
    getCourseList() {
      courseOptions().then((res) => {
        if (res.code == 200) {
          this.courseList = res.data;
          this.courseList.unshift({ id: "", name: "全部" });
        }
      });
    },
    active(id) {
      this.currentSort = id;
      this.currentCourse = "";
      this.fullscreenLoading = true;
      this.queryParams.gradeId = id;
      this.queryParams.courseId = this.currentCourse;
      this.getList();
    },
    active2(id) {
      this.currentCourse = id;
      this.fullscreenLoading = true;
      this.queryParams.courseId = id;
      this.getList();
    },
    getList() {
      getSubjectList(this.queryParams).then((res) => {
        clearTimeout();
        if (res.data) {
          this.subjectList = res.data.dataList;
          this.total = res.data.total;
          setTimeout(() => {
            this.fullscreenLoading = false;
          }, 500);
        }
      });
    },
    lookLive(item) {
      this.$router.push({
        path: "/student/inLive_detail",
        query: { id: item },
      });
    },
    openEvaluate(id) {
          //点击评价
      if (getToken()) {
        this.dialogFormVisible2 = true;
        this.evaluateInfo.content = "";
        this.evaluateInfo.evaluateLevel = 1;
        this.evaluateInfo.subjectId = id;
      }else{
        this.$router.push('/login')
      }
    },
    sendEvaluateInfo() {
      //发布评价
      if (this.evaluateInfo.content == "") {
        this.msgError("评价内容不能为空");
      } else {
        this.$store
          .dispatch("EvaluateSubject", this.evaluateInfo)
          .then((res) => {
            this.dialogFormVisible2 = false;
            if (res.code == 200 && res.status==1) {
              this.msgSuccess("评价成功");
              this.getList();
            } else {
              this.msgError(res.msg);
            }
          });
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
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  margin-top: 20px;
}
.class_box {
  margin-top: 15px;
  color: #555;
  font-size: 16px;
  line-height: 16px;
  & > .class_item {
    float: left;
    padding: 10px 15px;
    height: 36px;
    border-radius: 36px;
    cursor: pointer;
    font-size:14px;
    
  }
}
.active {
  color: #fff;
  background: #ffba00;
}
.course_box {
  margin-top: 20px;
  min-height: calc(100vh - 230px);
}
.clearfix {
  width: 100%;
  clear: both;
  box-sizing: border-box;
}
.course_item {
  width: 24%;
  min-height: 200px;
  box-sizing: border-box !important;
  background: #fff;
  float: left;
  font-size: 12px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  border-radius: 5px;
  margin-top: 10px;
  padding-bottom: 10px;
  cursor: pointer;
}
.course_item:hover {
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.3);
  transform: scale(1.03);
  -webkit-transform: scale(1.03);
}
.courserInfo {
  padding-left: 5px;
  color: #999;
  margin-top: 8px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.course_title {
  font-size: 14px;
  color: #000;
  font-weight: bold;
}
.coures_img {
  width: 100%;
  height: 138px;
  border-top-right-radius: 5px;
  border-top-left-radius: 5px;
}
.playNum {
  position: absolute;
  transform: translateY(115px);
  padding-left: 5px;
  z-index: 99;
  color: #fff !important;
}
.left {
  margin-left: 1.33%;
}
.cars_btn {
  float: right;
  margin-right: 10px;
}
.titles{
  padding: 10px; 
  float: left;
  font-size:14px
}
@media screen and (min-width: 1900px) {
  .coures_img {
    height: 151px;
  }
}
</style>
