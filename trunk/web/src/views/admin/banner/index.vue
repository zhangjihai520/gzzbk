<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="68px"
    >
      <el-form-item label="轮播标题" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入轮播标题"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="创建时间">
        <el-date-picker
          v-model="dateRange"
          size="small"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select
          v-model="queryParams.status"
          placeholder="轮播状态"
          clearable
          size="small"
        >
          <el-option
            v-for="dict in bannerStatus"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
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
          >新增</el-button
        >
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="noticeList">
      <el-table-column label="图片" align="center" prop="picUrl" width="100">
        <template slot-scope="scope">
          <el-image :src="scope.row.picUrl" style="height: 40px"></el-image>
        </template>
      </el-table-column>
      <el-table-column
        label="轮播标题"
        align="center"
        prop="name"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="创建时间"
        align="center"
        prop="createTime"
        width="150"
      />
      <el-table-column
        label="状态"
        align="center"
        prop="status"
        :formatter="statusFormat"
        width="100"
      />
      <el-table-column
        label="外链地址"
        align="center"
        prop="linkUrl"
        width="150"
      />
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            v-if="scope.row.status == 2"
            @click="handleChange(scope.row)"
            >修改</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            id="0"
            @click="handleUpdate($event, scope.row.id, scope.row.name)"
            >删除</el-button
          >
          <el-button
            size="small"
            type="text"
            id="1"
            v-if="scope.row.status == 2"
            @click="handleUpdate($event, scope.row.id, scope.row.name)"
            >上架</el-button
          >
          <el-button
            size="small"
            type="text"
            id="2"
            v-if="scope.row.status == 1"
            @click="handleUpdate($event, scope.row.id, scope.row.name)"
            >下架</el-button
          >
          <el-button
            size="small"
            type="text"
            @click="handleDetail(scope.row)"
            >详情</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改公告对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="780px">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="轮播banner" prop="name" style="width:500px">
          <el-input v-model="form.name" placeholder="请输入轮播标题" :disabled="isDetail"/>
        </el-form-item>
        <el-form-item label="外链地址" prop="linkUrl"  style="width:500px">
          <el-input v-model="form.linkUrl" placeholder="地址以http或者https开头" :disabled="isDetail"/>
        </el-form-item>
        <el-form-item label="轮播图片" prop="picUrl">
          <el-upload
            class="avatar-uploader"
            :action="uploadURL"
            :data="{ fileType: 5 }"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload"
            accept=".jpg,.jpeg,.png,.gif"
            :disabled="isDetail"
          >
            <img v-if="imageUrl" :src="imageUrl" class="avatar" />
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer" style="padding-top: 20px">
        <el-button type="primary" @click="submitForm" v-show="!isDetail">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getBannerList,
  saveBanner,
  changeBannerStatus,
  getBannerDetail,
} from "@/api/manage";
import { bannerStatus } from "../../components/enum/index";
import { formatDate } from "@/utils";
export default {
  name: "banner",
  data() {
    return {
      //图片上传地址
      uploadURL: process.env.VUE_APP_BASE_API + "/busscommon/upload",
      // 遮罩层
      loading: true,
      //是否是详情
      isDetail:false,
      // 总条数
      total: 0,
      // 公告表格数据
      noticeList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      //时间段
      dateRange: [],
      //上传图片
      imageUrl: "",
      //轮播状态
      bannerStatus: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: undefined,
        beginTime: undefined,
        endTime: undefined,
      },
      // 表单参数
      form: {
        id: "",
        name: "",
        picUrl: "",
        linkUrl: "",
      },
      // 表单校验
      rules: {
        name: [
          { required: true, message: "轮播标题不能为空", trigger: "blur" },
        ],
        linkUrl: [
          { required: true, message: "外链地址不能为空", trigger: "blur" },
        ],
      },
    };
  },
  created() {
    this.getList();
    this.bannerStatus = bannerStatus;
  },
  methods: {
    /** 查询公告列表 */
    getList() {
      this.loading = true;
      getBannerList(this.queryParams).then((res) => {
        if (res.code == 200) {
          this.noticeList = res.data.dataList;
          res.data.total == undefined
            ? (this.total = 0)
            : (this.total = res.data.total);
          this.loading = false;
        }
      });
    },
    // 公告状态字典翻译
    statusFormat(row, column) {
      return this.selectDictLabel(this.bannerStatus, row.status);
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.isDetail =false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: "",
        name: "",
        picUrl: "",
        linkUrl: "",
      };
      this.imageUrl = "";
      this.isDetail =false;
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.queryParams.beginTime = formatDate(this.dateRange[0]);
      this.queryParams.endTime = formatDate(this.dateRange[1]);
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
      this.title = "添加轮播";
    },
    /** 修改按钮操作 */
    handleChange(row) {
      this.reset();
      const noticeId = row.id;
      getBannerDetail({ bannerId: noticeId }).then((res) => {
        console.log(res);
        this.form = res.data;
        this.imageUrl = res.data.picUrl;
        this.open = true;
        this.title = "修改轮播";
      });
    },

     /** 详情按钮 */
    handleDetail(row) {
      this.reset();
      this.isDetail =true;
      const noticeId = row.id;
      getBannerDetail({ bannerId: noticeId }).then((res) => {
        this.form = res.data;
        this.imageUrl = res.data.picUrl;
        this.open = true;
        this.title = "轮播详情";
      });
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.picUrl == "") {
            this.msgError("请上传图片");
            return false;
          }
          console.log(this.form)
          if (this.form.bannerId != undefined) {
            saveBanner(this.form).then((res) => {
              if (res.code === 200) {
                if (res.status == 1) {
                  this.msgSuccess("修改成功");
                  this.getList();
                } else {
                  this.msgError("修改失败");
                }
                this.open = false;
              } else {
                this.msgError(res.msg);
              }
            });
          } else {
            saveBanner(this.form).then((res) => {
              if (res.code === 200) {
                if (res.status == 1) {
                  this.msgSuccess("新增成功");
                  this.getList();
                } else {
                  this.msgError("新增失败");
                }
                this.open = false;
              } else {
                this.msgError(response.msg);
              }
            });
          }
        }
      });
    },
    /** 删除、上架、下架 */
    handleUpdate(event, id, rowName) {
      const name = rowName;
      let status = event.currentTarget.id;
      let title = "";
      if (status == 1) {
        title = "上架";
      } else if (status == 2) {
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
          return changeBannerStatus({ bannerId: id, status: status });
        })
        .then((res) => {
          if (res.code == 200) {
            if (res.status == 1) {
              this.getList();
              this.msgSuccess(title + "成功");
            } else if (res.status == 2) {
              this.msgError(title + "失败");
            } else if (res.status == 3) {
              this.msgError(title + "最多5张");
            }
          } else {
            this.msgError(res.msg);
          }
        })
        .catch(function () {});
    },
    handleAvatarSuccess(res, file) {
      //图片上传
      if (res.code == 200) {
        this.imageUrl = URL.createObjectURL(file.raw);
        this.form.picUrl = res.url;
      }
    },
    beforeAvatarUpload(file) {
      //图片大小
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isLt2M) {
        this.$message.error("上传图片大小不能超过 2MB!");
      }
      return isLt2M;
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
</style>