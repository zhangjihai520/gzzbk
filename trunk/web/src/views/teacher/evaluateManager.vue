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
                  @click="getEvaluateList(item.id, item.name)"
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
            <h2 class="title">全部评价</h2>

            <el-form :inline="true">
              <el-form-item label="课程:">
                <span class="title">{{ courseTitle }}</span>
              </el-form-item>
            </el-form>

            <ul class="evaluateBox">
              <li
                v-for="(item, index) in evaluateList"
                :key="index"
                class="evaluateItem"
              >
               <el-avatar
                  :size="30"
                  :src="item.userFace"
                >
                  <img
                    src="https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png"
                  />
                </el-avatar>
                <!-- <img :src="item.userFace" alt="" class="senImg" /> -->
                <div class="sendInfo">
                  <p>{{ item.userTrueName }}</p>
                  <p>{{ item.content }}</p>
                  <p style="display: flex">
                    <el-rate
                      v-model="item.evaluateLevel"
                      disabled
                      show-score
                      text-color="#ff9900"
                    >
                    </el-rate>
                    <el-button
                      type="text"
                      style="margin-left: 20px"
                      size="mini"
                      icon="el-icon-delete"
                      @click="delEvalMsg(item.evaluateId)"
                      >删除</el-button
                    >
                  </p>
                </div>
              </li>
            </ul>
            <pagination
              v-show="evaluaTotal > 0"
              :total="evaluaTotal"
              :page.sync="evaluateParams.pageIndex"
              :limit.sync="evaluateParams.pageSize"
              @pagination="pageMessage"
              :page-sizes="[20, 50, 100, 200]"
              small
            />
          </el-col>
        </el-row>
      </main>
    </el-container>
  </el-container>
</template>

<script>
import HeaderTop from "./components/headerTop";
import Slider from "./components/slider";
import { getChaptersBySubjectId, changeEvaluateStatus } from "@/api/teacher";
export default {
  components: {
    HeaderTop,
    Slider,
  },
  name: "evaluateManager",
  data() {
    return {
      title: "评价管理",
      loading: true,
      queryParams: {
        pageNum: 1,
        pageSize: 20,
      },
      courseParams: {
        pageIndex: 1,
        pageSize: 20,
      },
      lessonValue: "",
      chapterlesson: [],
      courseTitle: "",

      courserList: [],
      courseToal: 0,
      evaluaTotal: 0,
      evaluateParams: {
        subjectId: "",
        pageIndex: 1,
        pageSize: 20,
      },
      evaluateList: [],
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
          if ( res.data.dataList != null && res.data.dataList.length >0 ) {
            this.getEvaluateList(
              this.courserList[0].id,
              this.courserList[0].name
            );
          }
        }
      });
    },
    getCourse() {
      //获取课程列表
      this.$store.dispatch("GetSubjectList", this.courseParams).then((res) => {
        if (res.code == 200) {
          this.courserList = res.data.dataList;
          this.courseToal = res.data.total;
          this.loading = false;
        }
      });
    },

    getEvaluateList(id, text) {
      //获取评价列表
      this.evaluateParams.subjectId = id;
      this.courseTitle = text;
      this.$store
        .dispatch("GetEvaluateDetail", this.evaluateParams)
        .then((res) => {
          if (res.code == 200) {
            this.loading = false;
            this.evaluateList = res.data.dataList;
            this.evaluaTotal = res.data.total;
          }
        });
    },
    pageMessage() {
      //评价翻页
      this.$store
        .dispatch("GetEvaluateDetail", this.evaluateParams)
        .then((res) => {
          if (res.code == 200) {
            this.loading = false;
            this.evaluaTotal = res.data.total;
            this.evaluateList = res.data.dataList;
          }
        });
    },
    getchapterlesson(id) {
      //获取章节
      getChaptersBySubjectId({ subjectId: id }).then((res) => {
        if (res.code == 200) {
          this.chapterlesson = res.data;
          this.lessonValue = res.data[0].id;
        }
      });
    },
    newList() {
      //点击章节获取详情
      this.$store
        .dispatch("GetEvaluateDetail", { subjectId: this.lessonValue })
        .then((res) => {
          if (res.code == 200) {
            this.loading = false;
            this.evaluateList = res.data.dataList;
          }
        });
    },
    delEvalMsg(itemId) {
      changeEvaluateStatus({ id: itemId }).then((res) => {
        if (res.code == 200) {
          if (res.status == 1) {
            this.msgSuccess("评价删除成功");
            this.pageMessage();
          } else {
            this.msgError("评价删除失败！");
          }
        }
      });
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
.evaluateItem:hover {
  background: #ccc;
}
</style>
