<template>
  <div class="container">
    <header-top />
    <nav-bar />
    <el-row>
      <el-col  :span="16" :offset="4">
        <div class="grid-content search_box">
          <el-input
            size="60"
            clearable
            placeholder="请输入教师名字"
            v-model="queryParams.userTrueName"
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
      <el-col  :span="16" :offset="4">
        <p class="grid-content teacher_title">
          名师风采<span>({{ total }}个名师)</span>
        </p>
      </el-col>
    </el-row>
    <el-row>
      <el-col
         :span="16" :offset="4"
        class="teacher_box"
        v-loading="fullscreenLoading"
      >
        <div
          class="grid-content teacher_item"
          :key="index"
          v-for="(item, index) in teacherList"
          :class="index % 4 == 0 ? '' : 'left'"
        >
          <div class="teacher_info">
            <el-image class="teacher_img" :src="item.userFace"></el-image>
            <!-- <img src="@/assets/student/default_user.jpg" class="teacher_img" /> -->
            <div class="info_detail">
              <p>{{ item.userTrueName }}</p>
              <p>{{ item.schoolName }}</p>
              <p>作品：{{ item.opusCount }}</p>
              <el-button
                type="warning"
                :data-id="item.id"
                class="cars_btn"
                @click="lookDetail"
                >查看详情</el-button
              >
            </div>
          </div>
          <div class="new_work">
            <h3 class="work_title">最新作品</h3>
            <p :key="i" v-for="(v, i) in item.newestOpusList.slice(0, 4)">
              <el-link :underline="false" @click="lookLive( v.id)">{{ v.name }}</el-link>
            </p>
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
      style="background: #f4f6f5;margin-right:20%"
    />
    <footer-bottom />
  </div>
</template>

<script>
import HeaderTop from "./components/header_top";
import NavBar from "./components/nav_bar";
import FooterBottom from "@/components/footer";
import { getTeacherList } from "@/api/student";
export default {
  name: "inLive_teacher",
  components: {
    HeaderTop,
    NavBar,
    FooterBottom,
  },
  data() {
    return {
      fullscreenLoading: false,
      queryParams: {
        pageIndex: 1,
        pageSize: 12,
        userTrueName: "",
      },
      total: 0,
      teacherList: [],
    };
  },
  mounted() {
    this.getList();
  },
  methods: {
    getList() {
      this.fullscreenLoading = true;
      getTeacherList(this.queryParams).then((res) => {
        clearTimeout();
        if (res.code == 200) {
          this.total = res.data.total;
          this.teacherList = res.data.dataList;
          setTimeout(() => {
            this.fullscreenLoading = false;
          }, 500);
        }
      });
    },
    lookDetail(e) {
      let teacherId = e.currentTarget.dataset.id;
      this.$router.push({
        path: "/student/teacher_info",
        query: { id: teacherId },
      });
      console.log(this.$router);
    },
    lookLive(item) {
      this.$router.push({
        path: "/student/inLive_course",
        query: { id: item },
      });
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
.teacher_box {
  margin-top: 20px;
  min-height: calc(100vh - 230px);
}
.teacher_title {
  font-size: 18px;
  font-weight: 800;
  color: #FFBA00;
  margin-top: 30px;
}
.teacher_item {
  width: 24%;
  height: 200px;
  background: #fff;
  float: left;
  font-size: 12px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  border-radius: 5px;
  margin-top: 10px;
  padding-bottom: 10px;
  & > .teacher_info {
    display: flex;
    width: 100%;
    height: 100px;
    box-sizing: border-box;
    padding: 10px;
  }
}
.teacher_img {
  width: 45%;
  height:88px;
}
.info_detail {
  margin-left: 10px;
  & > p {
    color: #555;
    margin-top: 5px;
    font-size: 12px;
  }
  & > .cars_btn {
    font-size: 14px;
    padding: 5px;
    margin-top: 10px;
  }
}
.new_work {
  margin: 10px 0 0 10px;
  & > .work_title {
    color: #FFBA00;
  }
  & > p {
    margin-top: 5px;
  }
}
.left {
  margin-left: 10px;
}
@media screen and(min-width:1900px) {
  .teacher_item{
    height: 240px;
  }
  .new_work{
    margin-top: 20px;
  }
  .teacher_info{
    height: 120px !important;
  }
  .teacher_img{
    width:42%;
    height: 120px;
  }
  .info_detail>p{
    margin-top: 10px;
  }
}
</style>
