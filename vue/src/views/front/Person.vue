<template>
  <div style="width: 40%; margin: 5px auto; background: linear-gradient(to bottom right, white, #CCE2FD); border-radius: 10px;" class="card">
    <el-form ref="userForm" :model="data.user" :rules="data.rules" label-width="60px" style="padding: 20px">
      <div style="text-align: center; margin-bottom: 20px">
        <el-upload
            :action="baseUrl + '/files/upload'"
            :on-success="handleFileUpload"
            :show-file-list="false"
            class="avatar-uploader"
        >
          <img v-if="data.user.avatar" :src="data.user.avatar" class="avatar" />
          <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
        </el-upload>
      </div>
      <el-form-item prop="username" label="用户名">
        <el-input disabled v-model="data.user.username" placeholder="请输入用户名"></el-input>
      </el-form-item>
      <el-form-item prop="name" label="姓名">
        <el-input v-model="data.user.name" placeholder="请输入姓名"></el-input>
      </el-form-item>
      <el-form-item prop="gender" label="性别">
        <el-select v-model="data.user.gender" placeholder="请选择">
          <el-option label="男" value="男"></el-option>
          <el-option label="女" value="女"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="contact" label="电话">
        <el-input v-model="data.user.contact" placeholder="请输入联系电话"></el-input>
      </el-form-item>
      <el-form-item prop="email" label="邮箱">
        <el-input v-model="data.user.email" placeholder="请输入邮箱"></el-input>
      </el-form-item>
      <div style="text-align: center">
        <el-button type="primary" @click="update">保 存</el-button>
      </div>
    </el-form>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import request from "@/utils/request.js";
import { ElMessage } from "element-plus";
import { Plus } from '@element-plus/icons-vue';

const baseUrl = import.meta.env.VITE_BASE_URL+'/api';
const userForm = ref(null);

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  rules: {
    contact: [
      { required: true, message: '请输入联系电话', trigger: 'blur' },
      { pattern: /^\d{11}$/, message: '联系电话必须是11位数字', trigger: 'blur' }
    ],
    email: [
      { required: true, message: '请输入邮箱', trigger: 'blur' },
      { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }
    ]
  }
});

const handleFileUpload = (res) => {
  data.user.avatar = res.data;
};

const emit = defineEmits(['updateUser']);
const update = () => {
  userForm.value.validate((valid) => {
    if (valid) {
      request.put('/user/update', data.user).then(res => {
        if (res.code === '200') {
          ElMessage.success('保存成功');
          localStorage.setItem('xm-user', JSON.stringify(data.user));
          emit('updateUser');
        } else {
          ElMessage.error(res.msg);
        }
      });
    }
  });
};
</script>

<style>
.avatar-uploader {
  height: 120px;
}
.avatar-uploader .avatar {
  width: 120px;
  height: 120px;
  display: block;
}
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 50%;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}
.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}
.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  text-align: center;
}
</style>