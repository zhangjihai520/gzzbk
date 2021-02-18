<template>
  <div class="container">
    <header-top />
    <nav-bar />
    <el-row>
      <el-col :span="16" :offset="4">
        <el-tabs
          :tab-position="tabPosition"
          class="info_box"
          @tab-click="handleClick"
        >
          <el-tab-pane label="用户信息" id="1">
            <el-form
              :label-position="right"
              label-width="80px"
              :model="formLabelAlign"
            >
              <el-form-item label="姓名：">
                <el-input
                  v-model="formLabelAlign.userName"
                  :disabled="true"
                  style="width: 260px"
                ></el-input>
              </el-form-item>
              <el-form-item label="学校：">
                <el-input
                  v-model="formLabelAlign.schoolName"
                  :disabled="true"
                  style="width: 260px"
                ></el-input>
              </el-form-item>
              <el-form-item label="年级：">
                <el-input
                  v-model="formLabelAlign.class"
                  :disabled="true"
                  style="width: 260px"
                ></el-input>
              </el-form-item>
              <el-form-item label="邮箱：">
                <el-input
                  v-model="formLabelAlign.email"
                  :disabled="true"
                  style="width: 260px"
                ></el-input>
              </el-form-item>
            </el-form>
          </el-tab-pane>
          <el-tab-pane label="我的课程" id="2">
            <el-tabs v-model="activeName">
              <div
                class="grid-content course_item"
                :key="index"
                v-for="(item, index) in courseList"
                :class="index % 4 == 0 ? '' : 'left'"
              >
                <div class="course_info">
                  <el-image class="course_img" :src="item.imagePath"></el-image>
                  <div class="info_detail">
                    <p class="info_text">课程：{{ item.name }}</p>
                    <p style="margin-top: 10px">讲师：{{ item.teacherName }}</p>
                    <div class="btnBox">
                      <el-button
                        type="info"
                        size="mini"
                        disabled
                        class="cars_btn"
                        v-if="item.classState == 0 || item.classState == 1"
                        >已报名</el-button
                      >
                      <el-button
                        type="warning"
                        size="mini"
                        class="cars_btn"
                        v-if="item.classState == 2"
                        @click="lookLive(item.id)"
                        >进入直播</el-button
                      >
                      <el-button
                        type="warning"
                        size="mini"
                        class="cars_btn"
                        v-if="item.classState == 3 && item.category==1"
                        @click="lookLive(item.id)"
                        >回放</el-button
                      >
                      <el-button
                        type="warning"
                        size="mini"
                        class="cars_btn"
                        v-if="item.classState == 3 && item.category==2"
                        @click="lookBack(item.id)"
                        >观看</el-button
                      >
                      <el-button
                        type="warning"
                        size="mini"
                        class="cars_btn"
                        v-if="item.classState == 3 && item.evaluateInfo == null"
                        :data-id="item.id"
                        @click="openEvaluate"
                        >评价</el-button
                      >
                      <el-button
                        type="warning"
                        size="mini"
                        class="cars_btn"
                        v-if="item.classState == 3 && item.evaluateInfo != null"
                        @click="showEvaluateInfo(item.evaluateInfo)"
                        >查看评价</el-button
                      >
                    </div>
                  </div>
                </div>
              </div>
            </el-tabs>
            <pagination
              style="margin-right:2%"
              v-show="total > 0"
              :total="total"
              :page.sync="queryParams.pageIndex"
              :limit.sync="queryParams.pageSize"
              @pagination="getList"
            />
          </el-tab-pane>
        </el-tabs>
      </el-col>
    </el-row>
    <footer-bottom />
    <!-- 评价 -->
    <el-dialog title="评价" :visible.sync="dialogFormVisible2">
      <el-form>
        <div class="block" style="display: flex">
          <span class="demonstration">评分：</span>
          <el-rate
            :disabled="isAbled"
            v-model="evaluateInfo.evaluateLevel"
            text-color="#ff9900"
            show-score
          ></el-rate>
        </div>
        <el-form-item style="margin-top: 5px">
          <el-input
            type="textarea"
            placeholder="请输入内容"
            v-model="evaluateInfo.content"
            :disabled="isAbled"
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
        <el-button v-if="!isAbled" type="warning" @click="sendEvaluateInfo"
          >发布</el-button
        >
      </div>
    </el-dialog>
  </div>
</template>

<script>
import HeaderTop from "./components/header_top";
import NavBar from "./components/nav_bar";
import FooterBottom from "@/components/footer";
import profile from "@/assets/image/profile.jpg";
import { getUserInfo } from "@/api/common";

export default {
  name: "student_info",
  components: {
    HeaderTop,
    NavBar,
    FooterBottom,
  },
  data() {
    return {
      tabPosition: "left",
      right: "right",
      activeName: "first",
      pageNation1: false,
      pageNation2: false,
      dialogFormVisible: false,
      dialogFormVisible2: false,
      formLabelAlign: {},

      rules: {
        textarea: [
          { required: true, message: "输入内容不能为空", trigger: "blur" },
        ],
      },
      total: 0,
      queryParams: {
        name: "",
        pageIndex: 1,
        pageSize: 12,
      },
      courseList: [],
      evaluateInfo: {
        subjectId: "",
        evaluateLevel: 0,
        content: "",
      },
      isAbled: false,
      form: {
        textarea: "",
      },
    };
  },
  mounted() {
    this.userInfo();
  },
  methods: {
    handleClick(tab, event) {
      if (tab.$attrs.id == 1) {
        this.userInfo();
      } else if (tab.$attrs.id == 2) {
        this.getList();
      }
    },
    userInfo() {
      //获取用户信息
      getUserInfo().then((res) => {
        this.formLabelAlign = res.data;
      });
    },
    getList() {
      //获取我的课程
      this.$store.dispatch("GetMySubjectList", this.queryParams).then((res) => {
        if (res.code == 200) {
          this.total = res.data.total;
          this.courseList = res.data.dataList;
        }
      });
    },
    resetForm() {
      this.dialogFormVisible = true;
      this.form.textarea = "";
    },

    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.dialogFormVisible = false;
        } else {
          return false;
        }
      });
    },

    openEvaluate(e) {
      //评价
      this.dialogFormVisible2 = true;
      this.evaluateInfo.content = "";
      this.isAbled = false;
      this.evaluateInfo.evaluateLevel = 0;
      this.evaluateInfo.subjectId = e.currentTarget.dataset.id;
    },
    showEvaluateInfo(item) {
      //查看评价
      this.dialogFormVisible2 = true;
      this.evaluateInfo.content = item.content;
      this.isAbled = true;
      this.evaluateInfo.evaluateLevel = Number(item.evaluateLevel);
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
    lookLive(item) {
      //直播
      this.$router.push({
        path: "/student/inLive_course",
        query: { id: item },
      });
    },
    lookBack(item) {
      //观看
      this.$router.push({
        path: "/student/inLive_detail",
        query: { id: item },
      });
    },
  },
};
</script>
<style lang='scss' scoped>
@import "@/assets/styles/public.scss";
.info_box {
  min-height: calc(100vh - 230px);
  background: #fff;
  margin-top: 20px;
  padding: 20px 0;
  box-sizing: border-box;
}
.course_item {
  width: 24%;
  float: left;
  margin-top: 10px;
  cursor: pointer;
}
.course_item:hover{
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.3);
  .course_img{
     transform: scale(1.03);
  -webkit-transform: scale(1.03);
  }
 
}
.left {
  margin-left: 10px;
}
.course_info {
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
  border-radius: 5px;
  padding: 10px;
  border: 1px solid #ccc;
  max-width: 400px;
}
.course_img {
  width: 100%;
  height: 112px;
  border-radius: 5px;
}
.info_detail {
  font-size: 12px;
  color: #444;
}
.info_text {
  margin-top: 5px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.btnBox {
  text-align: right;
}
.cars_btn {
  margin-top: 10px;
}
.userBox {
  display: flex;
  margin-top: 30px;
  cursor: pointer;
}
.userImg {
  width: 28px;
  height: 28px;
  border-radius: 50%;
}
.userName {
  font-size: 18px;
  margin: 0;
  margin-top: -5px;
  padding-left: 5px;
}
.btn {
  position: relative;
  top: -20px;
  z-index: 999 !important;
}
.pagination-container{
  padding: 0 !important;
}
@media screen and(min-width:1900px) {
  .course_info{
    min-height: 180px;
  }
  .course_img{
    height: 173px;
  }
}
</style>
