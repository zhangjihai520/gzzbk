<template>
  <el-dialog
    title="学生信息"
    :visible="studentInfo.dialogVisible"
    width="80%"
    :destroy-on-close="true"
    @close="$emit('close')"
  >
    <div v-loading="loading">
      <table class="main" cellpadding="0" cellspacing="0" border="0">
        <!-- 基本信息 -->
        <tr>
          <td colspan="5" class="title">基本信息</td>
        </tr>
        <tr>
          <td class="label">采集序号</td>
          <td>{{applyInfo.sampleId}}</td>
          <td class="label">学籍号</td>
          <td>{{applyInfo.studentCode}}</td>
          <td rowspan="4" style="text-align:center">
            <img alt="照片" :src="applyInfo.photo" />
          </td>
        </tr>
        <tr>
          <td class="label">姓名</td>
          <td>{{applyInfo.name}}</td>
          <td class="label">性别</td>
          <td>{{sexText}}</td>
        </tr>
        <tr>
          <td class="label">学校</td>
          <td>{{applyInfo.schoolName}}</td>
          <td class="label">班级</td>
          <td>{{applyInfo.className}}</td>
        </tr>
        <tr>
          <td class="label">身份证号</td>
          <td colspan="3">{{applyInfo.identify}}</td>
        </tr>
        <!-- 家庭信息 -->
        <tr>
          <td colspan="5" class="title">家庭信息</td>
        </tr>
        <tr class="text-center">
          <td>称呼</td>
          <td>姓名</td>
          <td colspan="2">身份证号</td>
          <td>联系电话</td>
        </tr>
        <tr class="text-center">
          <td>父亲</td>
          <td>{{applyInfo.parentName1}}</td>
          <td colspan="2">{{applyInfo.parentIdentify1}}</td>
          <td>{{applyInfo.parentTelephone1}}</td>
        </tr>
        <tr class="text-center">
          <td>母亲</td>
          <td>{{applyInfo.parentName2}}</td>
          <td colspan="2">{{applyInfo.parentIdentify2}}</td>
          <td>{{applyInfo.parentTelephone2}}</td>
        </tr>
        <!-- 户口信息 -->
        <tr>
          <td colspan="5" class="title">户口信息</td>
        </tr>
        <tr>
          <td class="label">户口本地址</td>
          <td colspan="4">{{applyInfo.residenceAddress}}</td>
        </tr>
        <tr>
          <td class="label">所属派出所</td>
          <td colspan="2">{{applyInfo.policeName?applyInfo.policeName:"无"}}</td>
          <td class="label">与学生关系</td>
          <td>{{applyInfo.residenceReleationName}}</td>
        </tr>
      </table>
      <table class="main" cellpadding="0" cellspacing="0" border="0" style="border-top:none;">
        <!-- 房产信息 -->
        <tr>
          <td colspan="4" class="title">房产信息</td>
        </tr>
        <tr>
          <td class="label" style="width: 160px;">是否有房产</td>
          <td colspan="3">{{applyInfo.isHouse?"有房产":"无房产（放弃房产资格）"}}</td>
        </tr>
        <tr>
          <td class="label">房产所在地</td>
          <td colspan="3">{{applyInfo.districtName?applyInfo.districtName:"无"}}</td>
        </tr>
        <tr>
          <!-- <td class="label" style="width: 160px;">房产来源</td>
          <td>{{applyInfo.houseTypeName}}</td>-->
          <td class="label" style="width: 160px;">房产编号</td>
          <td colspan="3">{{applyInfo.houseCode?applyInfo.houseCode:"无"}}</td>
        </tr>
        <tr>
          <td class="label">产权人姓名</td>
          <td>{{applyInfo.houseOwnerName?applyInfo.houseOwnerName:"无"}}</td>
          <td class="label">产权人身份证</td>
          <td>{{applyInfo.houseOwnerIdentify?applyInfo.houseOwnerIdentify:"无"}}</td>
        </tr>
        <tr>
          <td class="label">建筑面积(平方米)</td>
          <td>{{applyInfo.houseSize?applyInfo.houseSize:"无"}}</td>
          <td class="label">规划用途</td>
          <td>{{houseUseName?houseUseName:"无"}}</td>
        </tr>
        <tr>
          <!-- <td class="label">登记时间</td>
          <td>{{applyInfo.houseTime}}</td>-->
          <td class="label">共有人姓名</td>
          <td colspan="3">{{applyInfo.houseCoowner?applyInfo.houseCoowner:"无"}}</td>
        </tr>
        <tr>
          <td class="label">房产证地址</td>
          <td colspan="3">{{applyInfo.houseAddress?applyInfo.houseAddress:"无"}}</td>
        </tr>
        <tr>
          <!-- <td class="label">房产依据</td>
          <td>{{applyInfo.houseTypeName}}</td>-->
          <td class="label">房屋查询码</td>
          <td colspan="3">{{applyInfo.houseSearchCode?applyInfo.houseSearchCode:"无"}}</td>
        </tr>
      </table>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="$emit('close')" size="small">关闭</el-button>
    </span>
  </el-dialog>
</template>

<script>
import {
  studentStatusEunm,
  sexEnum,
  nationSelectEnum,
  planUseSelectEnum,
  saveErrorMessage
} from "@/utils/enum";
export default {
  name: "StudentInfo",
  data() {
    return { loading: false, applyInfo: {} };
  },
  props: ["studentInfo"],
  computed: {
    sexText: function() {
      return sexEnum[this.applyInfo.sex];
    },
    nationName: function() {
      let nation = nationSelectEnum.find(n => n.id == this.applyInfo.nationId);
      return nation && nation.name;
    },
    houseUseName: function() {
      let houseUse = planUseSelectEnum.find(
        n => n.id == this.applyInfo.houseUse
      );
      return houseUse && houseUse.name;
    }
  },
  watch: {
    studentInfo: function(newStudentInfo, oldStudentInfo) {
      if (newStudentInfo.dialogVisible && newStudentInfo.sampleId > 0)
        this.initData();
    }
  },
  created() {},
  methods: {
    initData() {
      this.loading = true;
      this.applyInfo = {};
      this.$store
        .dispatch("getStudentDetail", { sampleId: this.studentInfo.sampleId })
        .then(res => {
          this.applyInfo = res.data;
          this.loading = false;
        });
    }
  }
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
@import "~@/assets/styles/variables.scss";
h2 {
  text-align: center;
}
.main {
  border: 1px solid #999;
  width: 850px;
  margin: 0 auto;

  td {
    padding: 10px 5px;
    border-right: 1px solid #999;
    border-bottom: 1px solid #999;
  }
  .title {
    background-color: #ccc;
    text-align: center;
    font-weight: bold;
  }
  .label {
    text-align: right;
  }
  img {
    width: 120px;
    height: 160px;
  }
}
</style>
