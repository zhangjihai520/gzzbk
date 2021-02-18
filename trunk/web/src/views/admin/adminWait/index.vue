<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="68px"
    >
      <el-form-item label="学校名称" prop="schoolId">
        <el-autocomplete
          v-model="queryParams.schoolName"
          :fetch-suggestions="querySearchAsync"
          value-key="name"
          placeholder="请输入内容"
          @select="handleSelect"
        ></el-autocomplete>
      </el-form-item>
      <el-form-item label="课程名称" prop="name">
        <el-input
          v-model.trim="queryParams.name"
          placeholder="请输入课程名称"
          clearable
          size="small"
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="课程类型" prop="category">
        <el-select
          v-model="queryParams.category"
          placeholder="课程类型"
          clearable
          size="small"
          style="width: 100px"
        >
          <el-option
            v-for="dict in categoryOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <!-- <el-form-item label="课程状态" prop="status">
        <el-select
          v-model="queryParams.status"
          placeholder="课程状态"
          clearable
          size="small"
          style="width: 100px"
        >
          <el-option
            v-for="dict in statusOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item> -->
      <el-form-item label="课程时间">
        <el-date-picker
          v-model="dateRange"
          size="small"
          style="width: 240px"
          format="yyyy-MM-dd "
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
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

    <!-- <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          >创建课程</el-button
        >
      </el-col>
    </el-row> -->

    <el-table v-loading="" :data="typeList">
      <el-table-column
        label="学校"
        align="center"
        prop="schoolName"
        :show-overflow-tooltip="true"
      />

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
        :show-overflow-tooltip="true"
        :formatter="nullFormat"
        width="100"
      />
      <el-table-column
        label="课程类型"
        align="center"
        prop="category"
        :formatter="typeFormat"
        :show-overflow-tooltip="true"
        width="100"
      >
      </el-table-column>
      <el-table-column
        label="课程状态"
        align="center"
        prop="status"
        :formatter="statusFormat"
        width="100"
      />
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
        width="180"
      ></el-table-column>
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="small"
            type="text"
            id="1"
            @click="hanldeDetail($event, scope.row.id)"
            >详情</el-button
          >

          <el-button
            size="small"
            type="text"
            id="7"
            @click="handleUpdate($event, scope.row.id, scope.row.name)"
            v-if="
              (scope.row.status == 6 && scope.row.createrFlag == 2) ||
              (scope.row.status == 8 && scope.row.createrFlag == 2)
            "
            >上架</el-button
          >
          <el-button
            size="small"
            type="text"
            id="8"
            @click="handleUpdate($event, scope.row.id, scope.row.name)"
            v-if="scope.row.status == 7 && scope.row.createrFlag == 2"
            >下架</el-button
          >
          <el-button
            size="small"
            type="text"
            v-if="scope.row.status == 3"
            id="2"
            @click="hanldeDetail($event, scope.row.id)"
            >审核</el-button
          >
          <el-button
            size="small"
            type="text"
            icon="el-icon-delete"
            style="color: red"
            id="0"
            v-show="
              (scope.row.status == 1 && scope.row.createrFlag == 2) ||
              (scope.row.status == 8 && scope.row.createrFlag == 2)
            "
            @click="handleUpdate($event, scope.row.id, scope.row.name)"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <pagination
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
        :label-position="'top'"
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
        <el-form-item
          label="允许报名人数"
          prop="allowSignUpCount"
          v-show="showSignTime"
        >
          <el-input-number
            v-model="form.allowSignUpCount"
            :min="0"
            :disabled="isDetail"
            class="diangItem"
          ></el-input-number>
        </el-form-item>
        <el-form-item label="报名时间" v-show="showSignTime">
          <el-date-picker
            class="time"
            v-model="signTime"
            type="daterange"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd HH:mm:ss"
            :picker-options="expireTimeOption"
            range-separator="~"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            style="width: 350px"
            :disabled="isDetail"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="课程时间" class="dialogItem">
          <el-date-picker
            class="time"
            v-model="openTime"
            type="datetimerange"
            format="yyyy-MM-dd HH:mm:ss"
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
        <br />
        <el-form-item label="封面" prop="imagePath" class="dialogItem">
          <el-upload
            class="avatar-uploader"
            :action="uploadURL"
            :data="{ fileType: 2 }"
            :show-file-list="false"
            :disabled="isDetail"
            accept=".jpg,.jpeg,.png,.gif"
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
        <el-form v-show="!showSignTime">
          <div v-for="(item, index) in chapterLessons" :key="index">
            <el-form-item label="章节名称" prop="name" class="dialogItem">
              <el-input
                v-model="chapterLessons[index].name"
                placeholder="请输入章节名称"
                clearable
                size="small"
                style="width: 240px"
                :disabled="isDetail"
              ></el-input>
            </el-form-item>
            <el-form-item
              label="章节视频地址"
              prop="videoPath"
              class="dialogItem"
              v-show="!chapterLessons[index].videoPath"
            >
              <el-upload
                :action="uploadURL"
                :data="{ fileType: 3 }"
                :show-file-list="false"
                :disabled="isDetail"
                accept=".mp4"
              >
                <el-button
                  size="small"
                  type="primary"
                  slot="tip"
                  disabled
                  v-show="
                    parseInt(progressFlag[index].loadProgress) < 100 &&
                    parseInt(progressFlag[index].loadProgress) > 0
                  "
                  >点击上传</el-button
                >
                <el-button
                  size="small"
                  type="primary"
                  v-show="
                    parseInt(progressFlag[index].loadProgress) == 100 ||
                    parseInt(progressFlag[index].loadProgress) == 0
                  "
                  >点击上传</el-button
                >
              </el-upload>
              <el-input
                :value="chapterLessons[index].videoPath"
                style="display: none"
              ></el-input>

              <el-progress
                v-show="progressFlag[index].isShow"
                :percentage="progressFlag[index].loadProgress"
              ></el-progress>
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
                :fetch-suggestions="chapterSearch"
                :disabled="isDetail"
                value-key="name"
                placeholder="请输入内容"
                @select="chapterSelect($event, index)"
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
      </el-form>
      <div class="block" style="width: 40%; margin-left: 40px" v-show="tapShow">
        <h4>审核记录</h4>
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
                {{ item.remark == null ? "意见：无 " : "意见：" + item.remark }}
              </p>
            </el-card>
          </el-timeline-item>
        </el-timeline>
      </div>
      <el-form ref="form" :model="checkParams" v-show="isCheck">
        <el-form-item prop="status">
          <el-radio-group v-model="checkParams.status">
            <el-radio :label="6">同意</el-radio>
            <el-radio :label="5">驳回</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item prop="auditOpinion">
          <el-input
            v-model="checkParams.auditOpinion"
            placeholder="请输入审核意见"
            clearable
            size="small"
            type="textarea"
            rows="4"
            resize="none"
            style="width: 350px"
            maxlength="200"
            show-word-limit
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="checkCourse" v-show="isCheck">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <el-dialog
      width="600px"
      title="视频预览"
      :close-on-click-modal="false"
      :visible.sync="videoOpen"
      :before-close="close"
    >
      <div slot="footer" class="dialog-footer">
        <video
          id="myVideo"
          width="560"
          height="240"
          controls="controls"
          autoplay="autoplay"
          ref="myVideo"
        >
          <source :src="videoUrl" type="video/mp4" />
        </video>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getToDoSubjectList,
  getSubjectDetail,
  changeStatus,
} from "@/api/manage";
import { statusEnum, courseType, builders } from "../../components/enum/index";
import { formatDate } from "@/utils";
import {
  schoolOptions,
  gradeOptions,
  courseOptions,
  teacherOptions,
} from "@/api/common";

export default {
  name: "Dict",
  data() {
    return {
      // 遮罩层
      loading: true,
      timeout: null,
      //视频预览开关
      videoOpen: false,
      //视频预览地址
      videoUrl: "",
      //报名时间显示
      showSignTime: false,
      //点击状态
      btnStatus: false,
      //封面
      imageUrl: "",
      // 总条数
      total: 0,
      // 字典表格数据
      typeList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      tapShow: false,
      //是否是详情
      isDetail: false,
      // 状态数据字典
      statusOptions: [],
      //老师
      teacherOption: [],
      //课程状态
      schoolOptions: [],
      //课程类型
      categoryOptions: [],
      //年级
      gradeOptions: [],
      //学科
      courseOptions: [],
      // 日期范围
      dateRange: [],
      //报名时间范围
      signTime: [],
      //开课时间
      openTime: [],
      //日期限制
      expireTimeOption: {
        disabledDate(date) {
          return date.getTime() < Date.now() - 24 * 60 * 60 * 1000; // 今天和以后可选
        },
      },
      uploadURL: process.env.VUE_APP_BASE_API + "/busscommon/upload",
      // 查询参数
      queryParams: {
        pageIndex: 1,
        pageSize: 10,
        beginTime: undefined,
        endTime: undefined,
        category: undefined,
        name: undefined,
        status: undefined,
        schoolId: "",
        schoolName: "",
      },
      // 表单参数
      form: {
        id: undefined,
        name: "",
        gradeId: "",
        courseId: "",
        imagePath: "",
        allowSignUpCount: 0,
        remark: "",
        beginTime: "",
        endTime: "",
        signUpBeginTime: "",
        signUpEndTime: "",
        status: "",
        category: 1,
        chapterLessons: [],
      },

      //章节参数
      chapterLessons: [
        {
          name: "",
          videoPath: "",
          teacherId: "",
          sort: "",
        },
      ],
      //审核参数
      checkParams: {
        subjectId: "",
        status: 6,
        auditOpinion: "",
      },
      progressFlag: [{ isShow: false, loadProgress: 0 }], // 关闭进度条
      //详情参数
      detailParams: {
        subjectId: "",
      },
      //是否是审核
      isCheck: false,
      // 表单校验
      rules: {
        name: [
          { required: true, message: "课程名称不能为空", trigger: "blur" },
        ],
        beginTime: [
          { required: true, message: "开课时间不能为空", trigger: "blur" },
        ],
        endTime: [
          { required: true, message: "结束时间不能为空", trigger: "blur" },
        ],
      },
    };
  },
  created() {
    this.getSchoolOptions();
    this.getList();
    this.getGradList();
    this.getCourseList();
    this.getTeacherList();
    this.statusOptions = statusEnum;
    this.categoryOptions = courseType;
  },
  computed: {},
  methods: {
    querySearchAsync(queryString, cb) {
      var restaurants = this.schoolOptions;
      var results = queryString
        ? restaurants.filter(this.createStateFilter(queryString))
        : restaurants;

      clearTimeout(this.timeout);
      this.timeout = setTimeout(() => {
        cb(results);
      }, 1000 * Math.random());
    },
    createStateFilter(queryString) {
      return (state) => {
        return (
          state.name.toLowerCase().indexOf(queryString.toLowerCase()) === 0
        );
      };
    },
    handleSelect(item) {
      this.queryParams.schoolId = item.id;
    },
    chapterSelect(item, index) {
      this.chapterLessons[index].teacherId = item.id;
    },
    chapterSearch(queryString, cb) {
      var restaurants = this.teacherOption;
      var results = queryString
        ? restaurants.filter(this.createStateFilter(queryString))
        : restaurants;

      clearTimeout(this.timeout);
      this.timeout = setTimeout(() => {
        cb(results);
      }, 1000 * Math.random());
    },

    /** 查询字典类型列表 */
    getList() {
      this.loading = true;
      getToDoSubjectList(this.queryParams).then((res) => {
        if (res.code == 200) {
          this.total = res.data.total;
          this.typeList = res.data.dataList;
          this.loading = false;
        }
      });
    },
    //学校下拉
    getSchoolOptions() {
      schoolOptions().then((res) => {
        if (res.code == 200) {
          this.schoolOptions = res.data;
        }
      });
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

    //老师下拉
    getTeacherList() {
      teacherOptions().then((res) => {
        if (res.code == 200) {
          this.teacherOption = res.data;
        }
      });
    },
    // 状态翻译
    statusFormat(row, column) {
      return this.selectDictLabel(this.statusOptions, row.status);
    },
    // 类型状态字典翻译
    typeFormat(row, column) {
      return this.selectDictLabel(this.categoryOptions, row.category);
    },
    // 类型状态字典翻译
    LevelFormat(row, column) {
      return this.selectDictLabel(builders, row.createrFlag);
    },
    //去除空字段
    nullFormat(row, column) {
      let text = row[column.property];
      if (text == null || text == "" || text == undefined) {
        return "不限";
      } else {
        return text;
      }
    },
    /** 搜索按钮操作 */
    handleQuery() {
      let newArr = [];
      this.schoolOptions.forEach((item) => {
        newArr.push(item.name);
      });
      if (this.queryParams.schoolName !="" && !newArr.includes(this.queryParams.schoolName) ) {
        this.msgError("您选择的学校不存在,请重新选择");
        return false;
      }
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
      this.queryParams.schoolName =""
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
      this.schoolName = "";
    },
    // 取消按钮
    cancel() {
      this.open = false;
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
        chapterLessons: [],
      };
      this.imageUrl = "";
      this.signTime = [];
      this.openTime = [];
      this.showSignTime = false;
      this.btnStatus = false;
      this.tapShow = false;
      this.isCheck = false;
      this.chapterLessons = [
        {
          name: "",
          videoPath: "",
          teacherId: "",
          sort: 1,
        },
      ];
      this.resetForm("form");
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
    // 详情按钮
    hanldeDetail(e,row) {
      this.reset();
      let id = e.currentTarget.id;
      this.detailParams.subjectId = row;
      this.isDetail = true;
       if (id == 1) {
        this.title = "课程详情";
        this.isCheck = false;
      } else {
        this.title = "审核";
        this.isCheck = true;
        this.checkParams.auditOpinion = "";
        this.checkParams.subjectId = row;
      }
      getSubjectDetail(this.detailParams).then((res) => {
        if (res.code == 200) {
          console.log(res.data);
          this.open = true;
          this.form = res.data;
          this.imageUrl = res.data.imagePath;
          if (res.data.category == 1) {
            this.showSignTime = true;
          } else {
            this.showSignTime = false;
          }
          this.chapterLessons = res.data.chapterLessons;
          this.getSignTime(res);
          this.getOpenTime(res);
          if (res.data.reviewRecords && res.data.reviewRecords.length > 0) {
            this.tapShow = true;
          } else {
            this.tapShow = false;
          }
          let len = res.data.chapterLessons.length;
          if (len == 0 || len == undefined) {
            return false;
          } else {
            for (let i = 0; i < len; i++) {
              this.progressFlag.push({ isShow: false, loadProgress: 0 });
            }
          }
        }
      });
    },
    /** 删除、上架、下架 */
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
          if (res.code == 200 && res.status == 1) {
            this.getList();
            this.msgSuccess(title + "成功");
          } else {
            this.msgError(res.msg);
          }
        })
        .catch(function () {});
    },
   
    checkCourse() {
      //审核
      let ansower;
      if (this.checkParams.status == 6) {
        ansower = "审核";
      } else {
        ansower = "驳回";
      }
      if (this.checkParams.status == 5 && this.checkParams.auditOpinion == "") {
        this.msgError(ansower + "意见不能为空");
        return false;
      }
       this.open = false;
      changeStatus(this.checkParams).then((res) => {
        if (res.code == 200 && res.status == 1) {
          this.msgSuccess(ansower + "成功");
          this.getList();
        } else if (res.status == 2) {
          this.msgError(ansower + "失败");
        } else {
          this.msgError(res.msg);
        }
      });
    },
    lookVideo(src) {
      if (src == "" || src == null) {
        this.msgError("视频地址为空");
        return false;
      }
      this.videoOpen = true;
      var my_video = document.getElementById("myVideo");
      my_video.src = src;
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
<style lang="scss" scoped>
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
.dialogItem::before {
  display: inline;
  content: "*";
  color: red;
  padding-right: 5px;
}
.player {
  display: inline-block;
  color: #409eff;
  cursor: pointer;
  transform: translateY(45px);
  // z-index: 99;
}
</style>