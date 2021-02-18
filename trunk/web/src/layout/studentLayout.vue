<template>
  <el-container>
    <div class="header">
      <el-row :gutter="15">
        <el-col :xs="24" :span="7" class="logo">
          <img alt="logo" :src="logoImg" @click="goIndex" />
        </el-col>
        <el-col :xs="24" :span="17" class="user-info">
          您好：{{user.name}}，
          <el-link :underline="false" type="primary" href="/student/index">回到首页</el-link>&nbsp;&nbsp;|&nbsp;&nbsp;
          <el-link :underline="false" type="primary" @click="logout">退出</el-link>
        </el-col>
      </el-row>
    </div>
    <el-main>
      <app-main />
    </el-main>
    <el-footer>版权所有&nbsp;©&nbsp;2020&nbsp;&nbsp;南昌市共享云直播&nbsp;&nbsp;技术支持：江西软云科技股份有限公司</el-footer>
    <el-dialog
      title="南昌市2020年城区民办初中学校网上报名须知"
      :visible.sync="msgVisible"
      width="80%"
      center
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      :show-close="false"
    >
      <div class="msg">
        <p>一、城区民办初中学校招生范围</p>
        <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;根据省教育厅民办义务教育学校应在审批地范围内招生的要求，经市级审批机关批准设立的民办初中可在全市范围内招收：①具有南昌市户籍的小学毕业生；②具有南昌市学籍的小学毕业生；③具有南昌市房产的小学毕业生。经县（区）审批机关批准设立的民办初中可在本县（区）范围内招收：①具有本县（区）户籍的小学毕业生；②具有本县（区）学籍的小学毕业生；③具有本县（区）房产的小学毕业生。</p>
        <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;上述所指户籍，其对应的公安户籍派出所，须在市（县、区）范围；所指的学籍对应的学籍管理教育行政归属，须在市（县、区）范围；涉及的房产必须是通过房产管理部门登记备案的房产。</p>
        <p>二、城区民办初中学校网上报名</p>
        <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;7月1日至5日（报名期间，每天9:00至17:00开放填报），符合民办学校填报资格条件且自愿选择民办学校就读的小学毕业生，登录市教育考试院的城区民办初中学校网上招生录取平台进行注册报名、信息完善确认和志愿填报（凭本人唯一的小升初信息采集序号和密码）。本人登录密码由系统随机生成，无法修改，请妥善保管，切勿外泄。</p>
        <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;每位学生仅限填报1个城区民办学校志愿，志愿信息一经提交确定，不得修改。</p>
        <p>三、城区民办初中学校资格查核</p>
        <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;7月6日至10日，对填报城区民办学校志愿的小学毕业生进行信息比对和资格查核。学生网上填报的户籍、学籍和房产等信息必须真实、有效，对填报信息查实失真、无效的，一律取消该生民办学校录取资格。</p>
        <p>四、城区民办初中学校招生录取</p>
        <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;有意愿直升人数超过直升招生计划的或校外报名人数超过校外招生计划的，7月25日，全部实行电脑随机派位录取。凡报名人数未超过对应招生计划的民办初中（含直升报名人数未超过直升招生计划和校外招生报名人数未超过校外招生计划），对所有填报了该校该类志愿的学生进行注册登记录取。</p>
        <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;民办初中招收举办者子女、本校及所隶属高校（单位）教职工子女，经教育行政部门核定后，按对应招生计划优先录取。民办初中招收配套楼盘业主子女或业主本人（楼盘未达配建标准开发商自身配建学校或当地政府之前已按相关政策认定属于配套入学范围），其业主子女或业主本人按照配套入学方式提前录取，录取人数计入校外招生电脑随机派位计划数。经教育行政部门核定招录的教职工子女或楼盘生不需登录民办初中学校网上招生录取平台进行注册填报。</p>
        <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;7月28日至8月1日，各民办初中学校组织录取学生缴费并网上确认,网上确认截止时间为8月1日17：00。8月2日至6日，正式录取的学生到招生学校办理报到入校手续。</p>
        <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;对已填报城区民办初中志愿的学生，凡被民办初中录取但放弃缴费及录取资格的，均按统筹类别由教育部门依据其学区学校学位容量情况，相对就近统筹安排义务教育学位。凡未被民办初中录取的，由有关教育部门根据学区划分和生源类别安排入学。</p>
        <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;请根据自己的实际情况谨慎考虑为您的孩子选择公办或是民办学校，但无论您做何选择，也需要我们大家共同遵守教育法律法规的要求，希望您不要盲从，应充分相信每一所学校的老师，都会像爱护自己的孩子一样，爱护自己的学生，关注他们的成长。衷心感谢您对南昌教育事业的关心、理解和支持！</p>
        <el-checkbox v-model="checked">本人已知晓今年城区民办初中学校招生要求，并承诺如实准确填报。</el-checkbox>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button :disabled="!checked" type="primary" @click="submit">确 定</el-button>
      </span>
    </el-dialog>
  </el-container>
</template>

<script>
import { AppMain } from "./components";
import { mapState } from "vuex";
import logoImg from "@/assets/image/logo.png";
import Cookies from "js-cookie";

export default {
  name: "StudentLayout",
  data() {
    return { logoImg: logoImg, msgVisible: false, checked: false };
  },
  components: {
    AppMain
  },
  computed: {
    ...mapState({
      user: state => state.student.studentInfo
    })
  },
  created() {
    const user = this.user;
    const isFirstLogin = Cookies.get("isFirstLogin");
    console.log(user, isFirstLogin);
    if (user && user.isFirstLogin == 1 && !isFirstLogin) {
      this.msgVisible = true;
    }
  },
  methods: {
    handleSelect(index, indexPath) {
      this.$router.push({
        path: index,
        params: { data: "query" }
      });
    },
    logout() {
      this.$store.dispatch("StudentLogOut").then(() => {
        this.$router.push({ path: "/student/login" });
      });
    },
    goIndex() {
      this.$router.push({ path: "/student/index" });
    },
    submit() {
      this.msgVisible = false;
      Cookies.set("isFirstLogin", false);
      // this.$store.dispatch("confirmTreaty").then(res => {
      //   if (res.status) {
      //     this.msgVisible = false;
      //   }
      // });
    }
  }
};
</script>

<style lang="scss" scoped>
@import "~@/assets/styles/mixin.scss";
@import "~@/assets/styles/variables.scss";

.el-container {
  width: 1050px;
  margin: 0 auto;
}
.el-main {
  padding: 10px 0;
}
.header {
  padding: 0;
  font-size: 14px;
  .el-menu-item {
    font-size: 16px;
  }
  .el-col {
    height: 70px;
  }
  .logo {
    padding: 10px 0;
    cursor: pointer;
    img {
      width: 250px;
      height: 50px;
    }
  }
  .user-info {
    line-height: 70px;
    text-align: right;
    padding-right: 20px;
    .el-link {
      line-height: 1;
    }
  }
}

.el-footer {
  font-size: 14px;
  color: #333;
  text-align: center;
  line-height: 60px;
}

.msg {
  line-height: 170%;
  font-size: 15px;
}

@media screen and (max-width: 700px) {
  .el-container {
    width:  900px;
    margin: 0 auto;
  }
  .header {
    padding: 0;
    .el-col {
      height: auto;
    }
    .user-info {
      line-height: 1;
      padding-right: 10px;
    }
  }
}
</style>
