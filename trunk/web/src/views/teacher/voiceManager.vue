<template>
  <el-container class="container">
    <slider />
    <el-container>
      <el-header class="headerTop">
        <header-top :title="title" />
      </el-header>
      <main>
        <el-row v-loading="loading">
          <el-col :span="10" class="course">
            <h2 class="title">全部课程</h2>
            <div>
              <ul class="list">
                <li
                  v-for="(item, index) in courserList"
                  :key="index"
                  class="list-item"
                  @click="getMessage(item.id, item.name)"
                >
                  <img :src="item.imagePath" class="imgs" />
                  <div class="corseInfo">
                    <p>课程名称：{{ item.name }}</p>
                    <p>课程类型：{{ item.category == 1 ? "直播" : "录播" }}</p>
                    <p>课程时间：{{ item.classTime }}</p>
                  </div>
                </li>
              </ul>
              <pagination
                small
                v-show="courseToal > 0"
                :total="courseToal"
                :page.sync="courseParams.pageIndex"
                :limit.sync="courseParams.pageSize"
                @pagination="getCourse"
                :page-sizes="[20, 50, 100, 200]"
              />
            </div>
          </el-col>
          <el-col :span="13" class="evaluate">
            <h2 class="title">全部留言</h2>
            <el-form :inline="true">
              <el-form-item label="课程:">
                <span class="title">{{ courseTitle }}</span>
              </el-form-item>
              <el-form-item label="章节：">
                <el-select v-model="lessonValue" @change="newList">
                  <el-option
                    v-for="dict in chapterlesson"
                    :key="dict.id"
                    :label="dict.name"
                    :value="dict.id"
                  />
                </el-select>
              </el-form-item>
            </el-form>
            <ul class="evaluateBox">
              <li
                v-for="(vlaue, index) in leaveMessage"
                :key="index"
                class="evaluateItem"
              >
                <el-avatar :size="30" :src="vlaue.userFace">
                  <img
                    src="https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png"
                  />
                </el-avatar>
                <!-- <img :src="vlaue.userFace" alt="" class="senImg" /> -->
                <div class="sendInfo">
                  <p>
                    {{ vlaue.userName }}
                    <span class="mark" v-show="vlaue.children == null"
                      >未回复</span
                    >
                  </p>

                  <p>{{ vlaue.content }}</p>
                  <p>
                    <span>{{ vlaue.createTime }}</span>
                    <el-tooltip
                      content="回复"
                      placement="top"
                      effect="light"
                      style="padding-left: 15px"
                    >
                      <el-button
                        type="text"
                        size="mini"
                        class="el-icon-s-comment"
                        @click="clearnMessage(vlaue.id)"
                      ></el-button>
                    </el-tooltip>
                    <el-button
                      type="text"
                      size="mini"
                      icon="el-icon-delete"
                      @click="delLeaveMesg(vlaue.id)"
                      >删除</el-button
                    >
                  </p>
                  <el-collapse v-model="activeNames" class="message">
                    <el-collapse-item
                      title="查看回复"
                      :name="vlaue.id"
                      v-show="vlaue.children != null"
                    >
                      <div
                        v-for="(item2, index2) in vlaue.children"
                        :key="index2"
                        class="overBox"
                      >
                        <div class="overList">
                          <p>
                            <span style="color: #009eef">我： </span
                            >{{ item2.content }}
                          </p>
                          <p>{{ item2.createTime }}</p>
                        </div>
                      </div>
                    </el-collapse-item>
                  </el-collapse>
                </div>
              </li>
            </ul>
            <pagination
              small
              v-show="messageTotal > 0"
              :total="messageTotal"
              :page.sync="messageParams.pageIndex"
              :limit.sync="messageParams.pageSize"
              :page-sizes="[20, 50, 100, 200]"
              @pagination="pageMessage"
            />
          </el-col>
        </el-row>

        <!-- 弹窗 -->
        <el-dialog title="回复" :visible.sync="dialogFormVisible">
          <el-form :model="form">
            <el-form-item>
              <el-input
                type="textarea"
                placeholder="请输入内容"
                v-model="form.content"
                maxlength="200"
                show-word-limit
                class="textarea"
                resize="none"
                rows="5"
              >
              </el-input>
              <div class="send_box">
                <span class="fileInput">
                  <el-button
                    type="primary"
                    class="cars_btn"
                    @click="sendMessage"
                    >发送</el-button
                  >
                </span>
              </div>
            </el-form-item>
          </el-form>
        </el-dialog>
      </main>
    </el-container>
  </el-container>
</template>

<script>
import HeaderTop from "./components/headerTop";
import Slider from "./components/slider";
import {
  getChaptersBySubjectId,
  changeLeaveMessageStatus,
} from "@/api/teacher";
export default {
  components: {
    HeaderTop,
    Slider,
  },
  name: "evaluateManager",
  data() {
    return {
      title: "留言管理",
      loading: true,
      dialogFormVisible: false,
      form: {
        subjectId: "",
        replyId: "",
        content: "",
        chapterlessonId: "",
      },
      courseParams: {
        pageIndex: 1,
        pageSize: 20,
        status: 7,
      },
      queryParams: {
        pageNum: 1,
        pageSize: 20,
      },
      courseTitle: "",
      courserList: [],
      courseToal: 0,
      messageTotal: 0,
      messageParams: {
        subjectId: "",
        chapterlessonId: "",
        pageIndex: 1,
        pageSize: 20,
      },
      activeNames: [],
      leaveMessage: [],
      chapterlesson: [],
      lessonValue: "",
    };
  },
  created() {
    this.getfirst();
  },
  computed: {},
  methods: {
    //初始
    getfirst() {
      this.$store.dispatch("GetSubjectList", this.courseParams).then((res) => {
        if (res.code == 200) {
          this.loading = false;
          this.courserList = res.data.dataList;
          this.courseToal = res.data.total;
          if (res.data.dataList != null && res.data.dataList.length > 0) {
            this.courseTitle = res.data.dataList[0].name;
            this.messageParams.subjectId = this.courserList[0].id;
            this.form.subjectId = this.courserList[0].id;
            getChaptersBySubjectId({ subjectId: this.courserList[0].id }).then(
              (response) => {
                if (response.code == 200) {
                  this.chapterlesson = response.data;
                  if (this.lessonValue != null) {
                    this.lessonValue = response.data[0].id;
                    this.messageParams.chapterlessonId = response.data[0].id;
                    this.form.chapterlessonId = this.lessonValue;
                  }

                  this.$store
                    .dispatch("GetLeavemessage", this.messageParams)
                    .then((res) => {
                      if (res.code == 200) {
                        this.loading = false;
                        this.leaveMessage = res.data.dataList;
                        this.messageTotal = res.data.total;
                      }
                    });
                }
              }
            );
          }
        }
      });
    },
    getCourse() {
      //获取课程列表
      this.$store.dispatch("GetSubjectList", this.courseParams).then((res) => {
        if (res.code == 200) {
          this.loading = false;
          this.courserList = res.data.dataList;
          this.courseToal = res.data.total;
        }
      });
    },
    getMessage(id, text) {
      //点击课程获取留言
      this.messageParams.subjectId = id;
      this.courseTitle = text;
      this.form.subjectId = id;
      this.getchapterlesson(id);
    },

    pageMessage() {
      this.$store
        .dispatch("GetLeavemessage", this.messageParams)
        .then((res) => {
          if (res.code == 200) {
            this.loading = false;
            this.messageTotal = res.data.total;

            this.leaveMessage = res.data.dataList;
          }
        });
    },
    clearnMessage(id) {
      //清除输入框
      this.dialogFormVisible = true;
      this.form.content = "";
      this.form.replyId = id;
    },
    sendMessage() {
      //回复留言
      if (this.form.content.trim() != "") {
        this.$store.dispatch("SaveMessage", this.form).then((res) => {
          this.dialogFormVisible = false;
          if (res.code == 200 &&res.status ==1) {
            this.msgSuccess("回复成功");
            this.loading = true;
            this.$store
              .dispatch("GetLeavemessage", this.messageParams)
              .then((res) => {
                if (res.code == 200) {
                  this.loading = false;
                  this.leaveMessage = res.data.dataList;
                }else{
                   this.loading = false;
                   this.msgError(res.msg)
                }
              });
          }else{
            this.msgError(res.msg);
            this.loading = false;
          }
        });
      } else {
        this.msgError("输入不能为空");
      }
    },

    getchapterlesson(id) {
      //获取章节
      getChaptersBySubjectId({ subjectId: id }).then((res) => {
        if (res.code == 200) {
          this.chapterlesson = res.data;
          this.lessonValue = res.data[0].id;
          this.messageParams.chapterlessonId = this.lessonValue;
          this.form.chapterlessonId = this.lessonValue;
          this.$store
            .dispatch("GetLeavemessage", this.messageParams)
            .then((res) => {
              if (res.code == 200) {
                this.loading = false;
                this.messageTotal = res.data.total;
                this.leaveMessage = res.data.dataList;
              }
            });
        }
      });
    },
    newList() {
      this.messageParams.chapterlessonId = this.lessonValue;
      this.form.chapterlessonId = this.lessonValue;
      //点击章节获取详情
      this.$store
        .dispatch("GetLeavemessage", this.messageParams)
        .then((res) => {
          if (res.code == 200) {
            this.loading = false;
            this.leaveMessage = res.data.dataList;
          }
        });
    },
    delLeaveMesg(itemId) {
      //删除留言
      changeLeaveMessageStatus({ id: itemId }).then((res) => {
        if (res.code == 200) {
          if (res.status == 1) {
            this.msgSuccess("留言删除成功！");
            this.pageMessage();
          } else {
            this.msgError("留言删除失败！");
          }
        }
      });
    },
    errorHandler() {
      return true;
    },
  },
};
</script>
<style lang='scss' scoped>
@import "@/assets/styles/public.scss";
.container {
  width: 100%;
}
.headerTop {
  border-bottom: 1px solid #ccc;
  background: #fff;
}
.course,
.evaluate {
  height: calc(100vh - 100px);
  background: #fff;
  margin: 10px 0;
  padding: 15px;
  margin-left: 15px;
  border-radius: 10px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}
.title {
  padding: 10px 15px;
  font-weight: bolder;
  font-size: 14px;
}
.list {
  list-style: none;
  margin: 0;
  padding: 0;
  height: calc(100vh - 220px);
  overflow: auto;
  cursor: pointer;
}
.list-item {
  display: flex;
  padding: 10px 15px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  height: 100px;
  cursor: pointer;
}
.list-item:hover {
  background: rgba(0, 0, 0, 0.1);
}
.imgs {
  width: 100px;
}
.corseInfo {
  & > p {
    margin: 10px;
    color: #777;
    font-size: 12px;
  }
}
.evaluateBox {
  list-style: none;
  margin: 0;
  padding: 0;
  height: calc(100vh - 280px);
  overflow: auto;
  cursor: pointer;
  .evaluateItem {
    box-sizing: border-box;
    padding: 10px 15px;
    width: 100%;
    text-align: justify;
    text-justify: newspaper;
    word-break: break-all;
    display: flex;
    border-bottom: 1px solid #ccc;

    .sendInfo {
      width: 100%;
      & > p {
        margin: 5px 0px 0px 5px;
        color: #777;
        font-size: 14px;
      }
      & > p:first-child {
        color: #009eef;
        font-size: 16px;
      }
    }
  }
}
.overImg {
  width: 30px;
  height: 30px;
  border-radius: 100%;
}
.overBox {
  padding: 5px 0;
  display: flex;
  .overList {
    padding-left: 5px;
    & > p {
      font-size: 14px;
      margin-top: 2px;
      color: #777;
    }
  }
}
.send_box {
  height: 55px;
  padding: 10px 10px 10px;
  line-height: 33px;
  background-color: #fff;
}
.fileInput {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
  color: #4d4d4d;
  font-size: 14px;
  font-variant: tabular-nums;
  line-height: 1.5;
  list-style: none;
  font-feature-settings: "tnum";
  position: relative;
  top: -0.09em;
  display: inline-block;
  line-height: 1;
  white-space: nowrap;
  vertical-align: middle;
  outline: none;
  cursor: pointer;
  margin-left: 20px;
  .triggerDesc {
    display: flex;
    .triggerName {
      color: #989b9b;
      line-height: 1;
      margin-left: 3px;
      margin-top: 2px;
    }
  }
}
.fileInput:last-of-type {
  float: right;
}
.emotionList {
  margin: 0 auto;
  height: 150px;
  width: 400px;
  overflow-y: auto;
  img {
    padding: 5px 5px;
  }
}
.fileInput input {
  position: absolute;
  width: 80px;
  top: 0px;
  opacity: 0;
}
.mark {
  font-size: 12px;
  margin-left: 5px;
  color: red;
}
</style>
