<template>
  <el-container>
    <el-header>
      <el-row :gutter="15">
        <el-col :span="7" class="logo">
          <img alt="logo" src="@/assets/image/logo.png" />
        </el-col>
        <el-col :span="12">&nbsp;</el-col>
        <el-col :span="5" class="user-info">
          您好：{{name}}，
          <el-link :underline="false" type="primary" @click="logout">退出</el-link>
        </el-col>
      </el-row>
    </el-header>
    <el-main v-loading="loading">
      <el-row :gutter="10" class="total">
        <el-col :span="8">
          <div class="title">计划数</div>
          <div class="item">
            <div class="img img-c-1">
              <i class="el-icon-tickets" />
            </div>
            <div class="subtitle">城区民办初中招生总计划数</div>
            <div class="num">{{summaryData.planSummary1}}</div>
          </div>
          <div class="item">
            <div class="img img-c-2">
              <i class="el-icon-tickets" />
            </div>
            <div class="subtitle">市管民办初中招生计划数</div>
            <div class="num">{{summaryData.planSummary2}}</div>
          </div>
          <div class="item">
            <div class="img img-c-3">
              <i class="el-icon-tickets" />
            </div>
            <div class="subtitle">区管民办初中招生计划数</div>
            <div class="num">{{summaryData.planSummary3}}</div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="title">报名数</div>
          <div class="item">
            <div class="img img-c-1">
              <i class="el-icon-star-off" />
            </div>
            <div class="subtitle">城区民办初中招生总报名数</div>
            <div class="num">
              {{summaryData.fillSummary1}}
              <span
                class="range"
              >(占计划数：{{getRate(summaryData.fillSummary1, summaryData.planSummary1)}}%)</span>
            </div>
          </div>
          <div class="item">
            <div class="img img-c-2">
              <i class="el-icon-star-off" />
            </div>
            <div class="subtitle">市管民办初中招生报名数</div>
            <div class="num">
              {{summaryData.fillSummary2}}
              <span
                class="range"
              >(占计划数：{{getRate(summaryData.fillSummary2, summaryData.planSummary2)}}%)</span>
            </div>
          </div>
          <div class="item">
            <div class="img img-c-3">
              <i class="el-icon-star-off" />
            </div>
            <div class="subtitle">区管民办初中招生报名数</div>
            <div class="num">
              {{summaryData.fillSummary3}}
              <span
                class="range"
              >(占计划数：{{getRate(summaryData.fillSummary3, summaryData.planSummary3)}}%)</span>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="title">录取数</div>
          <div class="item">
            <div class="img img-c-1">
              <i class="el-icon-circle-check" />
            </div>
            <div class="subtitle">城区民办初中招生总录取数</div>
            <div class="num">{{summaryData.admissionSummary1}}</div>
          </div>
          <div class="item">
            <div class="img img-c-2">
              <i class="el-icon-circle-check" />
            </div>
            <div class="subtitle">市管民办初中招生录取数</div>
            <div class="num">{{summaryData.admissionSummary2}}</div>
          </div>
          <div class="item">
            <div class="img img-c-3">
              <i class="el-icon-circle-check" />
            </div>
            <div class="subtitle">区管民办初中招生录取数</div>
            <div class="num">{{summaryData.admissionSummary3}}</div>
          </div>
        </el-col>
      </el-row>
      <el-card class="search">
        <el-tabs :value="activeName" @tab-click="searchChange">
          <el-tab-pane label="查询学校" name="school">
            <el-form :inline="true">
              <el-form-item label="学校" prop="schoolId">
                <el-select
                  v-model="schoolId"
                  size="small"
                  filterable
                  placeholder="请选择或输入学校"
                  style="width:300px;"
                >
                  <el-option
                    v-for="dict in schoolOptions"
                    :key="dict.id"
                    :label="dict.name"
                    :value="parseInt(dict.id)"
                  />
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-button
                  type="primary"
                  icon="el-icon-search"
                  @click="searchSchool"
                  size="small"
                >搜索</el-button>
              </el-form-item>
            </el-form>
          </el-tab-pane>
          <el-tab-pane label="查询学生" name="student">
            <el-form :model="queryParams" ref="queryForm" :inline="true">
              <el-form-item label="所属小学" prop="primarySchoolId">
                <el-select
                  v-model="queryParams.primarySchoolId"
                  size="small"
                  filterable
                  placeholder="请选择或输入学校"
                  style="width:300px;"
                >
                  <el-option
                    v-for="dict in primarySchoolOptions"
                    :key="dict.id"
                    :label="dict.name"
                    :value="parseInt(dict.id)"
                  />
                </el-select>
              </el-form-item>
              <el-form-item label="报名学校" prop="schoolId">
                <el-select
                  v-model="queryParams.schoolId"
                  size="small"
                  filterable
                  placeholder="请选择或输入学校"
                  style="width:300px;"
                >
                  <el-option
                    v-for="dict in schoolOptions"
                    :key="dict.id"
                    :label="dict.name"
                    :value="parseInt(dict.id)"
                  />
                </el-select>
              </el-form-item>
              <el-form-item label="录取类型" prop="admissionType">
                <el-select v-model="queryParams.admissionType" size="small" placeholder="请选择录取类型">
                  <el-option
                    v-for="dict in admissionTypeOptions"
                    :key="dict.id"
                    :label="dict.name"
                    :value="parseInt(dict.id)"
                  />
                </el-select>
              </el-form-item>
              <el-form-item label="录取状态" prop="status">
                <el-select v-model="queryParams.status" size="small" placeholder="请选择录取状态">
                  <el-option
                    v-for="dict in statusOptions"
                    :key="dict.id"
                    :label="dict.name"
                    :value="parseInt(dict.id)"
                  />
                </el-select>
              </el-form-item>
              <el-form-item label="学生" prop="searchKey">
                <el-input
                  v-model="queryParams.searchKey"
                  size="small"
                  style="width:240px;"
                  placeholder="请输入采集序号/学籍号/学生姓名"
                />
              </el-form-item>
              <el-form-item>
                <el-button
                  type="primary"
                  icon="el-icon-search"
                  @click="searchStudent"
                  size="small"
                >搜索</el-button>
                <el-button icon="el-icon-refresh" @click="resetQuery" size="small">重置</el-button>
              </el-form-item>
            </el-form>
          </el-tab-pane>
        </el-tabs>
      </el-card>
      <el-card v-show="isSchool" class="school-item" :key="index"  v-for="(school, index) in searchSchoolList">
        <div slot="header">{{school.schoolName}}</div>
        <el-row :gutter="10">
          <el-col :span="5">计划招生总数：{{school.recruitCount}}</el-col>
          <el-col :span="5">直升招生数：{{school.straightupRecruitCount}}</el-col>
          <el-col :span="5">教职工子女招生数：{{school.staffCount}}</el-col>
          <el-col
            v-if="school.buildCount>0 && school.otherRecruitCount>0"
            :span="9"
          >校外招生数（含直升剩余数+楼盘招生数）：{{school.publicRecruitCount}}（{{school.otherRecruitCount}}+{{school.buildCount}}）</el-col>
          <el-col
            v-else-if="school.buildCount>0"
            :span="9"
          >校外招生数（含楼盘招生数）：{{school.publicRecruitCount}}（{{school.buildCount}}）</el-col>
          <el-col
            v-else-if="school.otherRecruitCount>0"
            :span="9"
          >校外招生数（含直升剩余数）：{{school.publicRecruitCount}}（{{school.otherRecruitCount}}）</el-col>
          <el-col v-else :span="9">校外招生数：{{school.publicRecruitCount}}</el-col>
          <el-col :span="12">
            <div :id="`straightup${school.schoolCode}`" :style="{height: '300px'}"></div>
          </el-col>
          <el-col :span="12">
            <div :id="`public${school.schoolCode}`" :style="{height: '300px'}"></div>
          </el-col>
        </el-row>
      </el-card>
      <el-card v-show="!isSchool" class="school-item">
        <el-table :data="tableData.dataList" stripe style="width: 100%">
          <el-table-column prop="primarySchoolName" label="毕业学校"></el-table-column>
          <el-table-column prop="sampleId" label="采集序号"></el-table-column>
          <el-table-column prop="studentName" label="姓名"></el-table-column>
          <el-table-column prop="fillDateTime" label="报名时间"></el-table-column>
          <el-table-column prop="schoolName" label="报名学校"></el-table-column>
          <el-table-column prop="phone1" label="联系电话1"></el-table-column>
          <el-table-column prop="phone2" label="联系电话2"></el-table-column>
          <el-table-column prop="phone3" label="联系电话3"></el-table-column>
          <el-table-column prop="admissionTypeName" label="录取类型"></el-table-column>
          <el-table-column prop="statusName" label="录取状态"></el-table-column>
        </el-table>
        <el-pagination
          background
          layout="prev, pager, next"
          :hide-on-single-page="true"
          :total="tableData.total"
          :page.sync="queryParams.pageIndex"
          :limit.sync="queryParams.pageSize"
          @current-change="handleQuery"
        />
      </el-card>
    </el-main>
    <el-footer height="40px">版权所有&nbsp;©&nbsp;2020&nbsp;&nbsp;南昌市共享云直播&nbsp;&nbsp;技术支持：江西软云科技股份有限公司</el-footer>
  </el-container>
</template>

<script>
import { mapState } from "vuex";
import echarts from "echarts";
import {
  admissionTypeSelectEnum,
  admissionStatusSelectEnum
} from "@/utils/enum";

export default {
  name: "ShowData",
  data() {
    return {
      loading: false,
      activeName: "school",
      summaryData: {},
      schoolId: 0,
      queryParams: {
        primarySchoolId: 0,
        schoolId: 0,
        admissionType: 0,
        status: 0,
        searchKey: "",
        pageIndex: 1,
        pageSize: 10
      },
      schoolList: [],
      searchSchoolList: [],
      tableData: { total: 0, dataList: [] },
      schoolOptions: [],
      primarySchoolOptions: [],
      admissionTypeOptions: admissionTypeSelectEnum,
      statusOptions: admissionStatusSelectEnum
    };
  },
  computed: {
    ...mapState({ name: state => state.user.name }),
    isSchool: function() {
      return this.activeName == "school";
    }
  },
  created() {
    const defaultSearchOption = { id: 0, name: "请选择(可输入筛选)" };
    //小学学校
    this.$store.dispatch("GetPrimarySchoolList").then(res => {
      this.primarySchoolOptions = res.schoolList;
      this.primarySchoolOptions &&
        this.primarySchoolOptions.splice(0, 0, defaultSearchOption);
    });
    //民办学校
    this.$store.dispatch("GetSchoolList").then(res => {
      this.schoolOptions = res.schoolList;
      this.schoolOptions &&
        this.schoolOptions.splice(0, 0, defaultSearchOption);
    });
    this.getSummaryData();
    this.getAllSchoolData();
  },
  mounted() {},
  methods: {
    getSummaryData() {
      this.loading = true;
      this.$store.dispatch("GetSummaryData", this.queryParams).then(res => {
        this.summaryData = res.data;
        this.loading = false;
      });
    },
    searchChange(tab) {
      this.activeName = tab.name;
    },
    getAllSchoolData() {
      this.loading = true;
      this.$store
        .dispatch("GetShowDataSchoolList", this.queryParams)
        .then(res => {
          this.schoolList = res.data;
          this.searchSchoolList = res.data;
          this.$nextTick(() => {
            this.initChartList();
          });
          this.loading = false;
        });
    },
    searchSchool() {
      const searchSchoolList = [];
      this.schoolList.map(school => {
        if (school.schoolId == this.schoolId || this.schoolId == 0) {
          searchSchoolList.push(school);
        }
      });
      this.searchSchoolList = searchSchoolList;
      this.$nextTick(() => {
        this.initChartList();
      });
    },
    searchStudent() {
      this.handleQuery(1);
    },
    handleQuery(pageIndex) {
      this.loading = true;
      this.queryParams.pageIndex = pageIndex;
      this.$store
        .dispatch("GetRecruitAdmissionResultList", this.queryParams)
        .then(res => {
          this.tableData = res.data;
          this.loading = false;
        });
    },
    resetQuery() {
      this.$refs["queryForm"].resetFields();
    },
    initChartList() {
      const { searchSchoolList } = this;
      const straightupXAxisData = ["直升计划数", "直升报名数", "直升录取数"];
      const publicXAxisData = ["校外计划数", "校外报名数", "校外录取数"];
      searchSchoolList.map(school => {
        this.initChart(`straightup${school.schoolCode}`, straightupXAxisData, [
          school.straightupRecruitCount,
          school.straightupFillCount,
          school.straightupAdmissionCount
        ]);
        this.initChart(`public${school.schoolCode}`, publicXAxisData, [
          school.publicRecruitCount,
          school.publicFillCount,
          school.publicAdmissionCount
        ]);
      });
    },

    initChart(id, xAxisData, seriesData) {
      const chart = echarts.init(document.getElementById(id));
      chart.setOption({
        color: ["#3398DB"],
        grid: {
          left: "3%",
          right: "4%",
          bottom: "3%",
          containLabel: true
        },
        xAxis: [
          {
            type: "category",
            data: xAxisData,
            axisTick: {
              alignWithLabel: true
            }
          }
        ],
        yAxis: [
          {
            type: "value"
          }
        ],
        series: [
          {
            type: "bar",
            barMaxWidth: "40",
            label: {
              show: true,
              position: "top"
            },
            data: seriesData
          }
        ]
      });

      // this.chart = echarts.init(document.getElementById("myChart"));

      // this.chart.setOption({
      //   color: ["#3398DB"],
      //   tooltip: {
      //     trigger: "axis",
      //     axisPointer: {
      //       // 坐标轴指示器，坐标轴触发有效
      //       type: "shadow" // 默认为直线，可选为：'line' | 'shadow'
      //     }
      //   },
      //   grid: {
      //     left: "3%",
      //     right: "4%",
      //     bottom: "3%",
      //     containLabel: true
      //   },
      //   xAxis: [
      //     {
      //       type: "category",
      //       data: ["直升计划数", "直升报名数", "直升录取数"],
      //       axisTick: {
      //         alignWithLabel: true
      //       }
      //     }
      //   ],
      //   yAxis: [
      //     {
      //       type: "value"
      //     }
      //   ],
      //   series: [
      //     {
      //       name: "直升",
      //       type: "bar",
      //       barMaxWidth: "40",
      //       label: {
      //         show: true,
      //         position: "top"
      //       },
      //       data: [100, 52, 200]
      //     }
      //   ]
      // });
      // this.chart2 = echarts.init(document.getElementById("myChart2"));

      // this.chart2.setOption({
      //   color: ["#3398DB"],
      //   tooltip: {
      //     trigger: "axis",
      //     axisPointer: {
      //       // 坐标轴指示器，坐标轴触发有效
      //       type: "shadow" // 默认为直线，可选为：'line' | 'shadow'
      //     }
      //   },
      //   grid: {
      //     left: "3%",
      //     right: "4%",
      //     bottom: "3%",
      //     containLabel: true
      //   },
      //   xAxis: [
      //     {
      //       type: "category",
      //       data: ["校外计划数", "校外报名数", "校外录取数"],
      //       axisTick: {
      //         alignWithLabel: true
      //       }
      //     }
      //   ],
      //   yAxis: [
      //     {
      //       type: "value"
      //     }
      //   ],
      //   series: [
      //     {
      //       name: "直升",
      //       type: "bar",
      //       barMaxWidth: "40",
      //       label: {
      //         show: true,
      //         position: "top"
      //       },
      //       data: [100, 52, 200]
      //     }
      //   ]
      // });
    },
    getRate(num1, num2) {
      return ((num1 / num2) * 100).toFixed(2);
    },

    logout() {
      this.$confirm("确定注销并退出系统吗？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        this.$store.dispatch("LogOut").then(() => {
          location.reload();
        });
      });
    }
  }
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
@import "~@/assets/styles/variables.scss";

.el-container {
  width: 100%;
  overflow: hidden;
}
.el-header {
  padding: 0;
  font-size: 14px;
  .el-menu-item {
    font-size: 16px;
  }
  .logo {
    cursor: pointer;
    img {
      height: 40px;
      margin: 10px 0 10px 20px;
    }
  }
  .user-info {
    line-height: 60px;
    text-align: right;
    .el-link {
      margin-right: 20px;
    }
  }
}

.el-main {
  min-height: calc(100vh - 100px);
  background-color: #f4f7fc;
  .total {
    padding: 20px;
    .title {
      margin: 0 0 20px 40px;
      font-family: "黑体";
      color: #242f57;
      font-size: 20px;
    }
    .item {
      margin: 0 40px 5px 20px;
      padding: 15px;
      background-color: #fff;
      height: 90px;
      border-radius: 13px;
      border: 1px solid #eaedf7;
      font-family: "微软雅黑";
      .img {
        padding: 15px;
        float: left;
        width: 60px;
        height: 60px;
        text-align: center;
        i {
          font-size: 30px;
        }
      }
      .img-c-1 {
        background-color: #d5eaff;
        i {
          color: #0080ff;
        }
      }
      .img-c-2 {
        background-color: #daf7f0;
        i {
          color: #00b988;
        }
      }
      .img-c-3 {
        background-color: #fff5ca;
        i {
          color: #ffcf1f;
        }
      }
      .subtitle {
        margin-left: 70px;
        padding: 5px 0;
        color: #97a0c3;
        font-size: 16px;
      }
      .num {
        margin: 5px 0 0 70px;
        color: #242f57;
        font-weight: bold;
        font-size: 20px;
      }
      .range {
        padding-left: 10px;
        font-size: 14px;
        color: #999;
      }
    }
  }
  .search {
    margin: 0 60px 0 40px;
    border-radius: 13px;
    .el-form-item {
      margin-bottom: 0;
    }
  }
  .school-item {
    margin: 10px 60px 10px 40px;
  }
  .el-pagination {
    text-align: right;
    padding-top: 15px;
  }
}
.el-footer {
  line-height: 40px;
  font-size: 14px;
  color: #333;
  text-align: center;
}
</style>
