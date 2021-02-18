<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="68px"
    >
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
          format="yyyy-MM-dd"
          value-format="yyyy-MM-dd HH:mm:ss"
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

    <el-row :gutter="10" class="mb8">
      <!-- <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          >创建课程</el-button
        >
      </el-col> -->
    </el-row>

    <el-table v-loading="loading" :data="typeList">
      <el-table-column
        label="课程名称"
        align="center"
        prop="name"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="教师"
        align="center"
        prop="teacherName"
        :show-overflow-tooltip="true"
        width="100"
      />
      <el-table-column
        label="适用年级"
        align="center"
        prop="gradeName"
        :formatter="nullFormat"
        :show-overflow-tooltip="true"
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
          <el-button size="small" type="text"  id="1" @click="hanldeDetail($event, scope.row.id)"
            >详情</el-button
          >
          <el-button
            size="small"
            type="text"
            v-if="
              (scope.row.status == 1 && scope.row.createrFlag == 1) ||
              (scope.row.status == 5 && scope.row.createrFlag == 1)
            "
            @click="hanldeUpdate(scope.row.id)"
            icon="el-icon-edit"
            >修改</el-button
          >

          <el-button
            size="small"
            type="text"
            id="7"
            @click="handleUpdate($event, scope.row.id, scope.row.name)"
            v-if="
              (scope.row.status == 6 && scope.row.createrFlag == 1) ||
              (scope.row.status == 8 && scope.row.createrFlag == 1)
            "
            >上架</el-button
          >
          <el-button
            size="small"
            type="text"
            icon="el-icon-delete"
            id="0"
            v-show="
              (scope.row.status == 1 && scope.row.createrFlag == 1) ||
              (scope.row.status == 5 && scope.row.createrFlag == 1) ||
              (scope.row.status == 8 && scope.row.createrFlag == 1)
            "
            style="color: red"
            @click="handleUpdate($event, scope.row.id, scope.row.name)"
            >删除</el-button
          >
          <el-button
            size="small"
            type="text"
            v-if="scope.row.status == 2"
            id='2'
            @click="hanldeDetail($event,scope.row.id)"
            >审核</el-button
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
        <el-form-item label="报名时间" v-show="showSignTime" class="dialogItem">
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
            :on-success="successImg"
            :before-upload="beforeImg"
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
              v-show="!chapterLessons[index].videoPath || !isDetail"
            >
              <el-upload
                :action="uploadURL"
                :data="{ fileType: 3 }"
                :show-file-list="false"
                :on-success="(value) => successVideo(index, value)"
                :before-upload="beforeVideo"
                :disabled="isDetail"
                :on-progress="(value) => uploadVideoProcess(index, value)"
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
            <el-button
              type="primary"
              v-if="!isDetail"
              @click="addLessons"
              icon="el-icon-plus"
              style="margin-top: 40px"
              circle
            ></el-button>
            <el-button
              type="danger"
              @click="subLessons"
              style="margin-top: 40px"
              :id="index"
              v-if="chapterLessons.length > 1 && !isDetail"
              icon="el-icon-minus"
              circle
            ></el-button>
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

      <el-form ref="form" :model="checkParams"  v-show="isCheck">
        <el-form-item prop="status">
          <el-radio-group v-model="checkParams.status">
            <el-radio :label="3">同意</el-radio>
            <el-radio :label="4">驳回</el-radio>
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
        <el-button
          type="primary"
          v-show="!isDetail"
          id="3"
          :loading="btnStatus"
          @click="submitForm"
          >提交审核</el-button
        >
        <el-button
          type="success"
          v-show="!isDetail"
          id="1"
          :loading="btnStatus"
          @click="saveSubmit"
          >暂存</el-button
        >
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
  listType,
  getType,
  delType,
  addType,
  updateType,
  exportType,
} from "@/api/system/dict/type";
import * as schoolServe from "@/api/school/index";
import { statusEnum, courseType, builders } from "../../components/enum/index";
import { formatDate } from "@/utils";
import { gradeOptions, courseOptions } from "@/api/common";
export default {
  name: "Dict",
  data() {
    return {
      // 遮罩层
      loading: true,
      dialogVisible: false,
      //报名时间显示和是否显示章节
      showSignTime: false,
      //点击状态
      btnStatus: false,
      //视频预览开关
      videoOpen: false,
      //视频预览地址
      videoUrl: "",
      // 总条数
      total: 0,
      //封面
      imageUrl: "",
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
      uploadURL: process.env.VUE_APP_BASE_API + "/usscommon/upload",
      // 查询参数
      queryParams: {
        pageIndex: 1,
        pageSize: 10,
        beginTime: undefined,
        endTime: undefined,
        category: undefined,
        name: undefined,
        status: undefined,
        schoolId: undefined,
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
        signUpBeginTime: "",
        signUpEndTime: "",
        beginTime: "",
        endTime: "",
        status: "",
        category: 1,
        chapterLessons: [],
      },
      checkParams: {
        subjectId: "",
        status: 3,
        auditOpinion: "",
      },
      //章节参数
      chapterLessons: [
        {
          name: "",
          videoPath: "",
          teacherId: "",
          sort: 1,
        },
      ],
      loadProgress: 0, // 动态显示进度条
      progressFlag: [{ isShow: false }], // 关闭进度条
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
      },
    };
  },
  created() {
    this.getList();
    this.statusOptions = statusEnum;
    this.categoryOptions = courseType;
    this.getGradList();
    this.getCourseList();
    this.getTeacherList();
  },
  methods: {
    /** 查询字典类型列表 */
    getList() {
      this.loading = true;
      schoolServe.getToDoSubjectList(this.queryParams).then((res) => {
        if (res.code == 200) {
          this.total = res.data.total;
          this.typeList = res.data.dataList;
          this.loading = false;
        }
      });
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
      schoolServe.teacherOptions().then((res) => {
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
      this.tapShow = false;
      this.btnStatus = false;
      this.isCheck = false;
      this.resetForm("form");
    },
    //添加章节
    addLessons() {
      let num = this.chapterLessons.length;
      num += 1;
      this.chapterLessons.push({
        name: "",
        videoPath: "",
        teacherId: "",
        sort: num,
      });
      this.progressFlag.push({ isShow: false });
    },
    //减去章节
    subLessons(e) {
      if (this.chapterLessons.length == 1) {
        return false;
      } else {
        let index = e.currentTarget.id;
        this.chapterLessons.splice(index, 1);
        this.progressFlag.splice(index, 1);
        for (let i = 0; i < this.chapterLessons.length; i++) {
          this.chapterLessons[i].sort = i + 1;
        }
      }
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
    hanldeDetail(e, row){
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
      schoolServe.getSubjectDetail(this.detailParams).then((res) => {
        if (res.code == 200) {
          this.open = true;
          this.form = res.data;
          this.imageUrl = res.data.imagePath;
          if (res.data.category == 1) {
            this.showSignTime = true;
          } else if (res.data.category == 2) {
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
              this.progressFlag.push({ isShow: false });
            }
          }
        }
      });
    },

    // 修改按钮
    hanldeUpdate(row) {
      this.reset();
      this.detailParams.subjectId = row;
      this.title = "修改课程";
      schoolServe.getSubjectDetail(this.detailParams).then((res) => {
        if (res.code == 200) {
          this.isDetail = false;
          this.open = true;
          this.form = res.data;
          this.chapterLessons = res.data.chapterLessons;
          let len = res.data.chapterLessons.length;
          this.imageUrl = res.data.imagePath;
          if (res.data.category == 1) {
            this.showSignTime = true;
          } else if (res.data.category == 2) {
            this.showSignTime = false;
          }
          this.getSignTime(res);
          this.getOpenTime(res);
          if (res.data.chapterLessons.length == 0) {
            this.chapterLessons = [
              {
                name: "",
                videoPath: "",
                teacherId: "",
                sort: 1,
              },
            ];
            this.progressFlag.push({ isShow: false });
          } else {
            let len = res.data.chapterLessons.length;
            for (let i = 0; i < len; i++) {
              this.progressFlag.push({ isShow: false });
            }
          }
        }
      });
    },
    /** 提交按钮 */
    submitForm: function (e) {
      this.form.status = e.currentTarget.id;
      if (e.currentTarget.id == 3) {
        this.$refs["form"].validate((valid) => {
          if (valid) {
            for (let i in this.chapterLessons) {
              if (
                this.chapterLessons[i].name == "" ||
                this.chapterLessons[i].videoPath == "" ||
                this.chapterLessons[i].sort == ""
              ) {
                this.msgError("请将章节信息填写完整");
                return false;
              }
            }
            if (this.openTime == null || this.openTime.length == 0) {
              return this.msgError("课程时间不能为空");
            }
            if (this.form.imagePath == "" || this.form.imagePath == null) {
              return this.msgError("请上传课程封面");
            }
            this.form.beginTime = this.openTime[0];
            this.form.endTime = this.openTime[1];
            this.form.chapterLessons = this.chapterLessons;
            let t3 = this.form.beginTime;
            let t4 = this.form.endTime;
            if (t3 != "" && t4 != "") {
              if (new Date(t4).getTime() <= new Date(t3).getTime()) {
                this.msgError("课程时间选择错误");
                return false;
              }
            }
            if (this.form.id != undefined) {
              schoolServe.saveSubject(this.form).then((res) => {
                this.btnStatus = true;
                if (res.code == 200 && res.status == 1) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.msgError(res.msg);
                }
              });
            } else {
              schoolServe.saveSubject(this.form).then((res) => {
                this.btnStatus = true;
                if (res.code == 200 && res.status == 1) {
                  this.msgSuccess("新增成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.msgError(res.msg);
                }
              });
            }
          }
        });
      }
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
          return schoolServe.changeStatus({ subjectId: id, status: status });
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
      let ansower;
      if (this.checkParams.status == 3) {
        ansower = "审核";
      } else {
        ansower = "驳回";
      }
      if (this.checkParams.status == 4 && this.checkParams.auditOpinion == "") {
        this.msgError(ansower + "意见不能为空");
        return false;
      }
      this.open = false;
      schoolServe.changeStatus(this.checkParams).then((res) => {
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

    successImg(res, file) {
      //图片上传
      if (res.code == 200) {
        this.imageUrl = URL.createObjectURL(file.raw);
        this.form.imagePath = res.url;
      }
    },
    beforeImg(file) {
      //图片大小
      const isLt2M = file.size / 1024 / 1024 < 20;
      if (!isLt2M) {
        this.$message.error("上传图片大小不能超过 20MB!");
      }
      return isLt2M;
    },
    successVideo(index, res) {
      //视频上传
      if (res.code == 200) {
        this.chapterLessons[index].videoPath = res.url;
      }
    },
    beforeVideo(file) {
      //视频大小
      const isLt2M = file.size / 1024 / 1024 < 800;
      if (!isLt2M) {
        this.$message.error("上传视频大小不能超过 800MB!");
      }
      return isLt2M;
    },
    uploadVideoProcess(index, event) {
      this.progressFlag[index].isShow = true; // 显示进度条
      this.btnStatus = true;
      this.loadProgress = parseInt(event.percent); // 动态获取文件上传进度
      if (this.loadProgress >= 100) {
        this.loadProgress = 100;
        setTimeout(() => {
          this.progressFlag[index].isShow = false;
          this.btnStatus = false;
        }, 1000); // 一秒后关闭进度条
      }
    },
    saveSubmit(e) {
      //暂存
      this.form.status = e.currentTarget.id;
      this.form.chapterLessons = this.chapterLessons;
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
        schoolServe.saveSubject(this.form).then((res) => {
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