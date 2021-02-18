<template>
  <el-container class="container">
    <slider />
    <el-container class="box">
      <el-header class="headerTop">
        <header-top :title="toptitle" />
      </el-header>
      <main class="formBox">
        <el-form
          :model="queryParams"
          ref="queryForm"
          :inline="true"
          label-width="68px"
        >
          <el-form-item label="课程类别" prop="category">
            <el-select
              v-model="queryParams.category"
              placeholder="请选择"
              clearable
              size="small"
              style="width: 140px"
            >
              <el-option
                v-for="dict in typeOptions"
                :key="dict.dictValue"
                :label="dict.dictLabel"
                :value="dict.dictValue"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="课程状态" prop="status">
            <el-select
              v-model="queryParams.status"
              placeholder="请选择"
              clearable
              size="small"
              style="width: 140px"
            >
              <el-option
                v-for="dict in statusOptions"
                :key="dict.dictValue"
                :label="dict.dictLabel"
                :value="dict.dictValue"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="课程时间">
            <el-date-picker
              class="time"
              v-model="dateRange"
              type="daterange"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd HH:mm:ss"
              range-separator="~"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              style="width: 240px"
            >
            </el-date-picker>
          </el-form-item>
          <el-form-item label="课程名称" prop="name">
            <el-input
              v-model="queryParams.name"
              placeholder="请输入课程名称"
              clearable
              size="small"
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item>
            <el-button
              type="primary"
              icon="el-icon-search"
              size="mini"
              @click="handleQuery"
              >搜索</el-button
            >

            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
              >重置</el-button
            >
          </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button
              type="primary"
              icon="el-icon-plus"
              size="mini"
              @click="handleAdd"
              >创建课程</el-button
            >
          </el-col>
        </el-row>
        <el-table v-loading="loading" :data="typeList">
          <el-table-column width="5" align="center" />

          <el-table-column
            label="课程名称"
            align="center"
            prop="name"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="适用年级"
            align="center"
            prop="gradeName"
            width="80"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="学科"
            align="center"
            prop="courseName"
            width="100"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="课程类别"
            align="center"
            prop="category"
            :formatter="typeFormat"
            width="120"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="课程状态"
            align="center"
            prop="status"
            width="100"
            :formatter="statusFormat"
            :show-overflow-tooltip="true"
          >
          </el-table-column>
          <el-table-column
            label="数据来源"
            align="center"
            prop="createrFlag"
            :formatter="LevelFormat"
            :show-overflow-tooltip="true"
            width="100"
          />
          <el-table-column
            label="课程时间"
            align="center"
            prop="classTime"
            :show-overflow-tooltip="true"
          />

          <el-table-column
            label="操作"
            align="center"
            class-name="small-padding fixed-width"
            width="250"
          >
            <template slot-scope="scope">
              <el-button
                size="small"
                type="info"
                @click="hanldeDetail(scope.row.id)"
                >详情</el-button
              >
              <el-button
                size="small"
                type="primary"
                id="7"
                @click="handleUpdate($event, scope.row.id, scope.row.name)"
                v-if="
                  (scope.row.status == 6 && scope.row.createrFlag == 0) ||
                  (scope.row.status == 8 && scope.row.createrFlag == 0)
                "
                >上架</el-button
              >

              <el-button
                size="small"
                type="success"
                id="8"
                v-if="scope.row.status == 7 && scope.row.createrFlag == 0"
                @click="handleUpdate($event, scope.row.id, scope.row.name)"
                >下架</el-button
              >
              <el-button
                size="small"
                type="warning"
                v-if="
                  (scope.row.status == 1 && scope.row.createrFlag == 0) ||
                  (scope.row.status == 4 && scope.row.createrFlag == 0) ||
                  (scope.row.status == 5 && scope.row.createrFlag == 0)
                "
                @click="hanldeUpdate(scope.row.id)"
                >修改</el-button
              >
              <el-button
                size="small"
                type="danger"
                id="0"
                v-show="
                  (scope.row.status == 1 && scope.row.createrFlag == 0) ||
                  (scope.row.status == 4 && scope.row.createrFlag == 0) ||
                  (scope.row.status == 5 && scope.row.createrFlag == 0) ||
                  (scope.row.status == 8 && scope.row.createrFlag == 0)
                "
                @click="handleUpdate($event, scope.row.id, scope.row.name)"
                >删除</el-button
              >
              <el-button
                type="primary"
                size="small"
                @click="getCode(scope.row.id)"
                v-if="scope.row.status == 7 && scope.row.category == 1 && scope.row.classState ==1"
                >上课</el-button
              >
            </template>
          </el-table-column>
        </el-table>

        <pagination
          style="background: #f4f6f5"
          v-show="total > 0"
          :total="total"
          :page.sync="queryParams.pageIndex"
          :limit.sync="queryParams.pageSize"
          @pagination="getList"
        />

        <!-- 添加或修改参数配置对话框 -->
        <el-dialog
          :title="title"
          :visible.sync="open"
          width="1300px"
          append-to-body
        >
          <el-form
            ref="form"
            :model="form"
            :inline="true"
            :rules="rules"
            label-width="110px"
            :label-position="labelPosition"
          >
            <el-form-item label="课程名称" prop="name">
              <el-input
                v-model="form.name"
                placeholder="请输入课程名称"
                clearable
                class="diangItem"
                size="small"
                :disabled="isDetail"
              ></el-input>
            </el-form-item>
            <el-form-item label="适用年级" prop="gradeId">
              <el-select
                v-model="form.gradeId"
                placeholder="不限"
                clearable
                size="small"
                class="diangItem"
                :disabled="isDetail"
              >
                <el-option
                  v-for="dict in gradeOptions"
                  :key="dict.id"
                  :label="dict.name"
                  :value="dict.id"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="学科" prop="courseId">
              <el-select
                v-model="form.courseId"
                placeholder="不限"
                clearable
                size="small"
                class="diangItem"
                :disabled="isDetail"
              >
                <el-option
                  v-for="dict in courseOptions"
                  :key="dict.id"
                  :label="dict.name"
                  :value="dict.id"
                />
              </el-select>
            </el-form-item>

            <el-form-item label="报名时间" v-show="form.category == 1 ? true : false" class="dialogItem">
              <el-date-picker
                class="time"
                v-model="signTime"
                type="datetimerange"
                format="yyyy-MM-dd HH:mm"
                :picker-options="expireTimeOption"
                value-format="yyyy-MM-dd HH:mm:ss"
                range-separator="~"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                style="width: 380px"
                :disabled="isDetail"
              >
              </el-date-picker>
            </el-form-item>
            <el-form-item label="课程时间" class="dialogItem">
              <el-date-picker
                class="time"
                v-model="openTime"
                type="datetimerange"
                format="yyyy-MM-dd HH:mm"
                :picker-options="expireTimeOption"
                value-format="yyyy-MM-dd HH:mm:ss"
                range-separator="~"
                start-placeholder="开始时间"
                end-placeholder="结束时间"
                style="width: 380px"
                :disabled="isDetail"
              >
              </el-date-picker>
            </el-form-item>
            <el-form-item label="允许报名人数" prop="allowSignUpCount" v-show="form.category == 1 ? true : false">
              <el-input-number
                v-model="form.allowSignUpCount"
                :min="0"
                :disabled="isDetail"
                class="diangItem"
              ></el-input-number>
            </el-form-item>
            <el-form-item label="封面地址" prop="imagePath" class="dialogItem">
              <el-upload
                class="avatar-uploader"
                :action="uploadURL"
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
                :before-upload="beforeAvatarUpload"
                :disabled="isDetail"
                accept=".jpg,.jpeg,.png,.gif"
                :data="{ fileType: 2 }"
              >
                <img v-if="imageUrl" :src="imageUrl" class="avatar" />
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
              </el-upload>
            </el-form-item>
            <el-form-item label="简介" prop="remark" style="margin-left: 212px">
              <el-input
                v-model="form.remark"
                placeholder="请输入内容"
                clearable
                size="small"
                type="textarea"
                style="width: 350px"
                rows="8"
                resize="none"
                :disabled="isDetail"
                maxlength="200"
                show-word-limit
              ></el-input>
            </el-form-item>
          </el-form>
          <el-form :inline="true" v-show="form.category == 1 ? false : true">
            <div v-for="(item, index) in chapterLessons" :key="index">
              <el-form-item label="章节名称" prop="name" >
                <el-input
                  v-model="chapterLessons[index].name"
                  placeholder="请输入章节名称"
                  clearable
                  size="small"
                  style="width: 240px"
                  :disabled="isDetail"
                ></el-input>
              </el-form-item>
              <el-form-item>
                <div
                  class="player"
                  v-show="chapterLessons[index].videoPath"
                  @click="lookVideo(chapterLessons[index].videoPath)"
                >
                  预览
                </div>
              </el-form-item>
              <el-form-item label="教师" prop="teacherName">
                <el-autocomplete
                  v-model="chapterLessons[index].teacherName"
                  :disabled="isDetail"
                  value-key="name"
                  placeholder="请输入内容"
                  style="width: 160px"
                ></el-autocomplete>
              </el-form-item>
              <el-form-item label="排序" prop="sort">
                <el-input
                  v-model="chapterLessons[index].sort"
                  :min="0"
                  :disabled="isDetail"
                  style="width: 160px"
                ></el-input>
              </el-form-item>
            </div>
          </el-form>
          <div
            class="block"
            style="width: 40%; margin-left: 40px"
            v-show="showCheck"
          >
            <h4>审核步骤</h4>
            <el-timeline>
              <el-timeline-item
                v-for="(item, index) in form.reviewRecords"
                :key="index"
                :timestamp="item.timestamp"
                placement="top"
              >
                <el-card>
                  <h4>{{ item.content }}</h4>
                  <p>
                    {{
                      item.remark == null ? "意见：无 " : "意见：" + item.remark
                    }}
                  </p>
                </el-card>
              </el-timeline-item>
            </el-timeline>
          </div>

          <div slot="footer" class="dialog-footer">
            <el-button
              type="primary"
              v-if="!isDetail"
              :loading="btnStatus"
              id="2"
              @click="submitForm"
              >提交审核</el-button
            >
            <el-button
              type="success"
              v-if="!isDetail"
              :loading="btnStatus"
              id="1"
              @click.once="saveSubmit"
              >暂存</el-button
            >
            <el-button @click="cancel">取 消</el-button>
          </div>
        </el-dialog>

        <el-dialog title="授权码" :visible.sync="dialogVisible" width="30%">
          <span>{{ copyCode }}</span>
          <span slot="footer" class="dialog-footer">
            <el-button type="primary" @click="copy">复制</el-button>
          </span>
        </el-dialog>

        <el-dialog
          width="600px"
          title="视频预览"
          :close-on-click-modal="false"
          :visible.sync="videoOpen"
          :before-close="close"
        >
          <div  class="dialog-footer">
            <video
              id="myVideo"
              width="560"
              height="240"
              controls="controls"
              autoplay="autoplay"
              ref="myVideo"
            >
              <source  :src="videoUrl" type="video/mp4" />
            </video>
          </div>
        </el-dialog>
      </main>
    </el-container>
  </el-container>
</template>

<script>
import HeaderTop from "./components/headerTop";
import Slider from "./components/slider";
import { statusEnum, courseType, builders } from "../components/enum/index";
import { formatDate } from "@/utils";
import { gradeOptions, courseOptions } from "@/api/common";
import { changeStatus, getSubjectDetail, getAuthCode } from "@/api/teacher";
export default {
  name: "teaIndex",
  components: {
    HeaderTop,
    Slider,
  },
  data() {
    return {
      toptitle: "课程管理",
      //创建课程弹窗布局位置
      labelPosition: "top",
      //点击状态
      btnStatus: false,
      // 遮罩层
      loading: true,
      dialogVisible: false,
      //是否展示审核步骤
      showCheck: false,
      //开课时间
      openTime: [],
      // 总条数
      total: 0,
      copyCode: "",
      imageUrl: "",
      // 字典表格数据
      typeList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      //是否是详情
      isDetail: false,
      // 状态数据字典
      statusOptions: [],
      //年级
      gradeOptions: [],
      //学科
      courseOptions: [],
      // 日期范围
      dateRange: [],
      //报名时间范围
      signTime: [],
      //日期限制
      expireTimeOption: {
        disabledDate(date) {
          return date.getTime() < Date.now() - 24 * 60 * 60 * 1000; // 今天和以后可选
        },
      },
      // 查询参数
      queryParams: {
        pageIndex: 1,
        pageSize: 10,
        beginTime: "",
        endTime: "",
        category: undefined,
        name: undefined,
        status: undefined,
      },
      uploadURL: process.env.VUE_APP_BASE_API + "/busscommon/upload",
      // 表单参数
      form: {
        id: undefined,
        name: "",
        gradeId: "",
        courseId: "",
        imagePath: "",
        allowSignUpCount: 0,
        remark: "",
        signUpBeginTime: "",
        signUpEndTime: "",
        beginTime: "",
        endTime: "",
        status: "",
        category: 1,
      },
      //详情参数
      detailParams: {
        subjectId: "",
      },
      //章节
      chapterLessons: [],
      //视频预览开关
      videoOpen: false,
      //视频预览地址
      videoUrl: "",
      // 表单校验
      rules: {
        name: [
          { required: true, message: "课程名称不能为空", trigger: "blur" },
        ],
        signTime: [
          { required: true, message: "报名时间不能为空", trigger: "blur" },
        ],
        // beginTime: [
        //   { required: true, message: "开课时间不能为空", trigger: "blur" },
        // ],
        // endTime: [
        //   { required: true, message: "结束时间不能为空", trigger: "blur" },
        // ],
      },
    };
  },
  created() {
    this.getList();
    this.statusOptions = statusEnum;
    this.typeOptions = courseType;
    this.getGradList();
    this.getCourseList();
  },
  methods: {
    /** 查询字典类型列表 */
    /** 课程列表查询 */
    getList() {
      this.loading = true;
      this.$store.dispatch("GetSubjectList", this.queryParams).then((res) => {
        if (res.code == 200) {
          this.total = res.data.total;
          this.typeList = res.data.dataList;
          this.loading = false;
        }
      });
    },
    // 状态字典翻译
    statusFormat(row, column) {
      return this.selectDictLabel(this.statusOptions, row.status);
    },
    // 类型状态字典翻译
    typeFormat(row, column) {
      return this.selectDictLabel(this.typeOptions, row.category);
    },
    // 类型状态字典翻译
    LevelFormat(row, column) {
      return this.selectDictLabel(builders, row.createrFlag);
    },
    gradName(item) {
      for (let i = 0; i < this.gradeOptions.length; i++) {
        if (item == this.gradeOptions[i].id) {
          console.log(item);
          return this.gradeOptions[i].name;
        }
      }
    },
    //年级下拉
    getGradList() {
      gradeOptions().then((res) => {
        if (res.code == 200) {
          this.gradeOptions = res.data;
        }
      });
    },
    //学科下拉
    getCourseList() {
      courseOptions().then((res) => {
        if (res.code == 200) {
          this.courseOptions = res.data;
        }
      });
    },

    // 取消按钮
    cancel() {
      this.open = false;
      this.isDetail = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: undefined,
        name: "",
        gradeId: "",
        courseId: "",
        imagePath: "",
        allowSignUpCount: 0,
        remark: "",
        signUpBeginTime: "",
        signUpEndTime: "",
        beginTime: "",
        endTime: "",
        status: "",
        category: 1,
      };
      this.imageUrl = "";
      this.signTime = [];
      this.openTime = [];
      this.showCheck = false;
      this.btnStatus = false;
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageIndex = 1;
      if (this.dateRange != null) {
        this.queryParams.beginTime = this.dateRange[0];
        this.queryParams.endTime = this.dateRange[1];
      } else {
        this.queryParams.beginTime = "";
        this.queryParams.endTime = "";
      }
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      (this.isDetail = false), (this.title = "创建课程");
    },
    getSignTime(res) {
      //获取报名时间
      if (res.data.signUpBeginTime != null && res.data.signUpEndTime != null) {
        this.signTime.push(res.data.signUpBeginTime);
        this.signTime.push(res.data.signUpEndTime);
      } else {
        this.signTime = [];
      }
    },
    getOpenTime(res) {
      //获取报名时间
      if (res.data.beginTime != null && res.data.endTime != null) {
        this.openTime.push(res.data.beginTime);
        this.openTime.push(res.data.endTime);
      } else {
        this.openTime = [];
      }
    },
    // 修改按钮
    hanldeUpdate(row) {
      this.reset();
      this.detailParams.subjectId = row;
      getSubjectDetail(this.detailParams).then((res) => {
        if (res.code == 200) {
          this.form = res.data;
          this.getSignTime(res);
          this.getOpenTime(res);
          this.imageUrl = res.data.imagePath;
          this.isDetail = false;
          this.open = true;
          this.title = "修改课程";
        }
      });
    },

    // 详情按钮
    hanldeDetail(row) {
      this.reset();
      this.detailParams.subjectId = row;
      getSubjectDetail(this.detailParams).then((res) => {
        if (res.code == 200) {
          this.form = res.data;
          this.getSignTime(res);
          this.getOpenTime(res);
          this.imageUrl = res.data.imagePath;
          this.chapterLessons = res.data.chapterLessons;
          
          if (res.data.reviewRecords && res.data.reviewRecords.length > 0) {
            this.showCheck = true;
          } else {
            this.showCheck = false;
          }
          this.isDetail = true;
          this.open = true;
          this.title = "课程详情";
        }
      });
    },
    /** 提交审核 */
    submitForm: function (e) {
      this.form.status = e.currentTarget.id;
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.signTime == null || this.signTime.length == 0) {
            return this.msgError("报名时间不能为空");
          }
          if (this.openTime == null || this.openTime.length == 0) {
            return this.msgError("课程时间不能为空");
          }
          if (this.form.imagePath == "" || this.form.imagePath == null) {
            return this.msgError("请上传课程封面");
          }
          this.form.signUpBeginTime = this.signTime[0];
          this.form.signUpEndTime = this.signTime[1];
          this.form.beginTime = this.openTime[0];
          this.form.endTime = this.openTime[1];

          let t1 = this.form.signUpBeginTime;
          let t2 = this.form.signUpEndTime;
          let t3 = this.form.beginTime;
          let t4 = this.form.endTime;
          if (t1 != "" && t2 != "" && t3 != "" && t4 != "") {
            if (new Date(t2).getTime() <= new Date(t1).getTime()) {
              this.msgError("报名时间选择错误！");
              return false;
            }
            if (new Date(t3).getTime() <= new Date(t2).getTime()) {
              this.msgError("课程时间要大于报名时间！");
              return false;
            }
            if (new Date(t4).getTime() <= new Date(t3).getTime()) {
              this.msgError("课程时间选择错误");
              return false;
            }
          }
          this.btnStatus = true;
          if (this.form.id != undefined) {
            this.$store.dispatch("SaveSubject", this.form).then((res) => {
              if (res.code == 200) {
                this.open = false;
                if (res.status == 1) {
                  this.msgSuccess("修改成功");
                  this.loading = true;
                  this.getList();
                } else {
                  this.msgError("修改失败");
                }
              } else {
                this.msgError(res.msg);
              }
            });
          } else {
            this.$store.dispatch("SaveSubject", this.form).then((res) => {
              this.open = false;
              if (res.code == 200) {
                if (res.status == 1) {
                  this.msgSuccess("创建成功");
                  this.loading = true;
                  this.getList();
                } else {
                  this.msgError("创建失败");
                }
              } else {
                this.msgError(res.msg);
              }
            });
          }
        }
      });
    },
    //暂存
    saveSubmit(e) {
      this.form.status = e.currentTarget.id;
      if (this.signTime != null) {
        this.form.signUpBeginTime = this.signTime[0];
        this.form.signUpEndTime = this.signTime[1];
      }
      if (this.openTime != null) {
        this.form.beginTime = this.openTime[0];
        this.form.endTime = this.openTime[1];
      }
      let name = this.form.name;

      if (name == "") {
        this.msgError("课程名称不能为空");
        return false;
      } else {
        this.$store.dispatch("SaveSubject", this.form).then((res) => {
          this.open = false;
          if (res.code == 200) {
            if (res.status == 1) {
              this.msgSuccess("暂存成功");
              this.loading = true;
              this.getList();
            } else {
              this.msgError("暂存失败");
            }
          } else {
            this.msgError(res.msg);
          }
        });
      }
    },
    /** 上下架 */
    handleUpdate(event, id, rowName) {
      const name = rowName;
      let status = event.currentTarget.id;
      let title = "";
      if (status == 7) {
        title = "上架";
      } else if (status == 8) {
        title = "下架";
      } else if (status == 0) {
        title = "删除";
      }
      this.$confirm(
        "是否确认" + title + '课程名称为"' + name + '"的数据项?',
        "警告",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(function () {
          return changeStatus({ subjectId: id, status: status });
        })
        .then((res) => {
          if(res.code ==200 && res.status==1){
             this.getList();
             this.msgSuccess(title + "成功");
          }else{
            this.msgError(res.msg)
          }
         
        })
        .catch(function () {});
    },
    handleAvatarSuccess(res, file) {
      //图片上传成功
      if (res.code == 200) {
        this.imageUrl = URL.createObjectURL(file.raw);
        this.form.imagePath = res.url;
      }
    },
    beforeAvatarUpload(file) {
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isLt2M) {
        this.$message.error("上传头像图片大小不能超过 2MB!");
      }
      return isLt2M;
    },
    getCode(itemId) {
      //获取授权码
      this.copyCode = "";
      this.dialogVisible = true;
      getAuthCode({ subjectId: itemId }).then((res) => {
        if (res.code == 200) {
          this.copyCode = res.msg;
        }
      });
    },
    copy() {
      //复制
      this.dialogVisible = false;
      if (this.copyCode == "") {
        return false;
      }
      let data = this.copyCode;
      let oInput = document.createElement("input");
      oInput.value = data;
      document.body.appendChild(oInput);
      oInput.select();
      document.execCommand("Copy");
      this.$message({
        message: "复制成功",
        type: "success",
      });
      oInput.remove();
    },
    lookVideo(src) {
      
      //视频预览
      if(src =="" || src ==null){
        this.msgError("视频地址为空");
        return false
      } 
      this.videoOpen = true;
      this.videoUrl =src;  
      let my_video =document.getElementById("myVideo");
      my_video.src =this.videoUrl;
      my_video.play();
      
    },
    close() {
      let my_video =document.getElementById("myVideo");
      this.videoOpen = false;
      my_video.pause()
    },
    
  },
};
</script>
<style lang='scss' scoped>
@import "@/assets/styles/public.scss";
.headerTop {
  border-bottom: 1px solid #ccc;
  background: #fff;
}
.formBox {
  padding: 15px;
}
.box {
  width: calc(100vw - 200px);
}
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
  border: 1px dashed #d9d9d9;
}
.avatar {
  width: 160px;
  height: 160px;
  display: block;
}
.el-form-item {
  margin-left: 40px;
}
.diangItem {
  width: 350px;
}
.player {
  display: inline-block;
  color: #409eff;
  cursor: pointer;
  // transform: translateY(45px);
  // z-index: 99;
}
.dialogItem::before {
  display: inline;
  content: "*";
  color: red;
  padding-right: 5px;
}
</style>