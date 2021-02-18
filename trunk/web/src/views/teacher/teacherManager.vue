<template>
  <el-container class="container">
    <slider  ref="slider"/>
    <el-container>
      <el-header class="headerTop">
        <header-top :title="title"></header-top>
      </el-header>
      <main style="padding: 15px">
        <el-tabs
          v-model="activeName"
          @tab-click="handleClick"
          style="padding: 15px; background: #fff"
        >
          <el-tab-pane label="查看个人信息" name="first">
            <el-form
              ref="newForm"
              :model="newForm"
              label-width="100px"
              v-loading="loading"
            >
              <el-upload
                class="avatar-uploader"
                :show-file-list="false"
                disabled
                :action="pictureAction"
                :on-success="handleAvatarSuccess"
                :before-upload="beforeAvatarUpload"
                accept=".jpg,.jpeg,.png,.gif"
                :data="{ fileType: 1 }"
              >
                <img v-if="imageUrl != ''" :src="imageUrl" class="avatar" />
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
              </el-upload>
              <el-form-item label="姓名：" class="formItem" prop="userTrueName">
                <el-input v-model="form.userTrueName" disabled></el-input>
              </el-form-item>
              <el-form-item label="学校" prop="schoolName">
                <el-input
                  v-model="form.schoolName"
                  disabled
                  style="width: 300px"
                ></el-input>
              </el-form-item>
              <el-form-item label="年级" class="formItem" prop="gradeName">
                <el-input v-model="form.gradeName" disabled></el-input>
              </el-form-item>

              <el-form-item label="学科" class="formItem" prop="courseName">
                <el-input v-model="form.courseName" disabled></el-input>
              </el-form-item>
              <el-form-item
                label="电话号码："
                style="width: 400px"
                prop="phone"
              >
                <el-input v-model="form.phone" disabled></el-input>
              </el-form-item>
              <el-form-item label="个人简介" prop="comment">
                <el-input
                  v-model="form.comment"
                  placeholder="请输入内容"
                  clearable
                  size="small"
                  type="textarea"
                  style="width: 380px"
                  maxlength="200"
                  rows="9"
                  resize="none"
                  disabled
                ></el-input>
              </el-form-item>
            </el-form>
          </el-tab-pane>
          <el-tab-pane label="修改个人信息" name="second">
            <el-form
              ref="form"
              :model="form"
              :rules="rules"
              label-width="100px"
              v-loading="loading"
            >
              <el-upload
                class="avatar-uploader"
                :show-file-list="false"
                :action="pictureAction"
                :on-success="handleAvatarSuccess"
                :before-upload="beforeAvatarUpload"
                accept=".jpg,.jpeg,.png,.gif"
                :data="{ fileType: 1 }"
              >
                <img v-if="imageUrl != ''" :src="imageUrl" class="avatar" />
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
              </el-upload>
              <el-form-item
                label="姓名："
                style="width: 300px"
                prop="userTrueName"
              >
                <el-input v-model="form.userTrueName" disabled></el-input>
              </el-form-item>
              <el-form-item label="学校名称" prop="schoolId">
                <el-autocomplete
                  v-model="form.schoolName"
                  :fetch-suggestions="querySearchAsync"
                  value-key="name"
                  placeholder="请输入内容"
                  @select="handleSelect"
                  style="width: 300px"
                ></el-autocomplete>
              </el-form-item>
              <el-form-item label="年级" prop="gradeId">
                <el-select
                  v-model="form.gradeId"
                  placeholder="请选择"
                  clearable
                  size="small"
                  style="width: 200px"
                >
                  <el-option
                    v-for="dict in gradeOption"
                    :key="dict.id"
                    :label="dict.name"
                    :value="dict.id"
                  />
                </el-select>
              </el-form-item>

              <el-form-item label="学科" prop="courseId">
                <el-select
                  v-model="form.courseId"
                  placeholder="请选择"
                  clearable
                  size="small"
                  style="width: 200px"
                >
                  <el-option
                    v-for="dict in courseOption"
                    :key="dict.id"
                    :label="dict.name"
                    :value="dict.id"
                  />
                </el-select>
              </el-form-item>
              <el-form-item
                label="电话号码："
                style="width: 400px"
                prop="phone"
              >
                <el-input v-model="form.phone" maxlength="11"></el-input>
              </el-form-item>
              <el-form-item label="个人简介" prop="comment">
                <el-input
                  v-model="form.comment"
                  placeholder="请输入内容"
                  clearable
                  size="small"
                  type="textarea"
                  style="width: 380px"
                  maxlength="200"
                  rows="9"
                  resize="none"
                  show-word-limit
                ></el-input>
              </el-form-item>
              <el-form-item class="btns">
                <el-button size="medium" type="primary" @click="changeInfo"
                  >修改</el-button
                >
              </el-form-item>
            </el-form>
          </el-tab-pane>
        </el-tabs>
      </main>
    </el-container>
  </el-container>
</template>

<script>
import HeaderTop from "./components/headerTop";
import Slider from "./components/slider";
import profile from "@/assets/image/profile.jpg";
import { getUserInfo, updateUserInfo } from "@/api/teacher";
import { gradeOptions, courseOptions, schoolOptions } from "@/api/common";
import { setSchoolId } from "@/utils/auth";
export default {
  components: {
    HeaderTop,
    Slider,
  },
  name: "teacherManager",
  data() {
    return {
      title: "个人中心",
      activeName: "second",
      loading: true,
      //学校下拉
      schoolOption: [],
      //年级下拉
      gradeOption: [],
      //学科下拉
      courseOption: [],
      imageUrl: "",
      form: {},
      newForm: {},
      pictureAction: process.env.VUE_APP_BASE_API + "/busscommon/upload",
      rules: {
        schoolId: [
          { required: true, message: "学校不能为空" },
        ],
      },
    };
  },
  created() {
    this.getInfo();
  },
  methods: {
    //获取信息
    getInfo() {
      getUserInfo().then((res) => {
        if (res.code == 200) {
          this.loading = false;
          this.form = res.data;
          this.newForm = res.data;
          let gradeId = this.form.gradeId;
          this.getGradeOption(gradeId);
          this.getCourseOptions();
          this.getSchoolOptions();
          this.imageUrl = res.data.userFace;
          setSchoolId(res.data.schoolId);
        }
      });
    },
    //年级
    getGradeOption(id) {
      gradeOptions().then((res) => {
        if (res.code == 200) {
          let gradeId = id;
          this.gradeOption = res.data;
        }
      });
    },
    //学科
    getCourseOptions() {
      courseOptions().then((res) => {
        if (res.code == 200) {
          this.courseOption = res.data;
        }
      });
    },
    //学校
    getSchoolOptions() {
      schoolOptions().then((res) => {
        if (res.code == 200) {
          this.schoolOption = res.data;
        }
      });
    },
    //修改信息
    changeInfo() {
      let newArr =[];
      this.schoolOption.forEach((item) =>{
         newArr.push(item.name)
      })
      if(!newArr.includes(this.form.schoolName)){
        this.msgError("您选择的学校不存在,请重新选择");
          return false;
      }
      let checkPhone = /^1(3[0-9]|5[0-3,5-9]|7[1-3,5-8]|8[0-9])\d{8}$/;
      if (!checkPhone.test(this.form.phone) && this.form.phone != "") {
        this.msgError("您输入的手机号码有误");
        return false;
      }

      this.$refs["form"].validate((valid) => {
        if (valid) {
          updateUserInfo(this.form).then((res) => {
            if (res.code == 200 && res.status==1) {
              this.msgSuccess("修改成功");
              this.getInfo();
              this.$refs.slider.changeRouter()
       
            } else {
              this.msgError(res.msg);
            }
          });
        }
      });
    },

    //图片处理
    handleClick(tab, event) {
      this.getInfo();
    },
    handleAvatarSuccess(res, file) {
      if (res.code == 200) {
        this.imageUrl = URL.createObjectURL(file.raw);
        this.form.userFace = res.url;
        // this.changeInfo();
      }
    },
    beforeAvatarUpload(file) {
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isLt2M) {
        this.$message.error("上传头像图片大小不能超过 2MB!");
      }
      return isLt2M;
    },
    querySearchAsync(queryString, cb) {
      var restaurants = this.schoolOption;
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
      this.form.schoolId = item.id;
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
.uersImg {
  width: 100px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  border-radius: 6px;
}
.avatar-uploader {
  margin-bottom: 22px;
  margin-left: 80px;
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
  width: 100px;
  height: 100px;
  line-height: 100px;
  text-align: center;
}
.avatar,
.uersImg {
  width: 100px;
  height: 100px;
  display: block;
  border-radius: 6px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}
.formItem {
  width: 300px;
}

</style>
