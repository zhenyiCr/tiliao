<template>
  <div class="login-container">
    <div class="login-box">
      <div style="font-weight: bold; font-size: 24px; text-align: center; margin-bottom: 30px; color: #1450aa">用 户 注 册</div>
      <el-form ref="formRef" :model="data.form" :rules="data.rules">
        <el-form-item prop="name">
          <el-input :prefix-icon="User" size="large" v-model="data.form.name" placeholder="请输入姓名"></el-input>
        </el-form-item>
        <el-form-item prop="username">
          <el-input :prefix-icon="User" size="large" v-model="data.form.username" placeholder="请输入账号"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input show-password :prefix-icon="Lock" size="large" v-model="data.form.password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input show-password :prefix-icon="Lock" size="large" v-model="data.form.confirmPassword" placeholder="请确认密码"></el-input>
        </el-form-item>
        <el-form-item prop="contact">
          <el-input :prefix-icon="Phone" size="large" v-model="data.form.contact" placeholder="请输入联系电话（选填）"></el-input>
        </el-form-item>
        <el-form-item prop="email">
          <el-input :prefix-icon="Message" size="large" v-model="data.form.email" placeholder="请输入邮箱（选填）"></el-input>
        </el-form-item>
        <el-form-item prop="gender">
          <el-radio-group v-model="data.form.gender" size="large">
            <el-radio label="男">男</el-radio>
            <el-radio label="女">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item>
          <el-button size="large" type="primary" style="width: 100%" @click="register">注 册</el-button>
        </el-form-item>
        <div style="text-align: right">
          <a href="/login">已有账号? 由此登录</a>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import { User, Lock, Phone, Message } from "@element-plus/icons-vue";
import request from "@/utils/request.js";
import { ElMessage } from "element-plus";
import router from "@/router/index.js";

const data = reactive({
  form: {
    name: '',
    username: '',
    password: '',
    confirmPassword: '',
    gender: '男',
    contact: '',
    email: ''
  },
  rules: {
    name: [
      { required: true, message: '请输入姓名', trigger: 'blur' }
    ],
    username: [
      { required: true, message: '账号不能为空', trigger: 'blur' },
      { min: 3, max: 20, message: '账号长度在 3 到 20 个字符之间', trigger: 'blur' },
      { pattern: /^[a-zA-Z0-9_]+$/, message: '账号只能包含字母、数字和下划线', trigger: 'blur' }
    ],
    password: [
      { required: true, message: '密码不能为空', trigger: 'blur' },
      { min: 6, max: 20, message: '密码长度在 6 到 20 个字符之间', trigger: 'blur' }
    ],
    confirmPassword: [
      { required: true, message: '请确认密码', trigger: 'blur' },
      { validator: (rule, value, callback) => {
          if (value !== data.form.password) {
            callback(new Error('两次输入密码不一致'));
          } else {
            callback();
          }
        }, trigger: 'blur' }
    ],
    contact: [
      { validator: (rule, value, callback) => {
          if (value) {
            if (!/^\d{11}$/.test(value)) {
              callback(new Error('联系电话是11位数字'));
            } else {
              callback();
            }
          } else {
            callback();
          }
        }, trigger: 'blur' }
    ],
    email: [
      { validator: (rule, value, callback) => {
          if (value) {
            if (!/^\w+([.-]?\w+)*@\w+([.-]?\w+)*(\.\w{2,3})+$/.test(value)) {
              callback(new Error('请输入正确的邮箱地址'));
            } else {
              callback();
            }
          } else {
            callback();
          }
        }, trigger: ['blur', 'change'] }
    ]
  }
});

const formRef = ref();

const register = () => {
  formRef.value.validate(valid => {
    if (valid) { // 表示表单校验通过
      request.post('/register', data.form).then(res => {
        if (res.code === '200') {
          ElMessage.success('注册成功');
          router.push('/login');
        } else {
          ElMessage.error(res.msg);
        }
      });
    }
  });
};
</script>

<style scoped>
.login-container {
  height: 100vh;
  overflow: hidden;
  display: flex;
  justify-content: center;
  align-items: center;
  background-image: url('@/assets/imgs/register-background.jpg'); /* 确保路径正确 */
  background-size: cover; /* 使背景图片覆盖整个容器 */
  background-position: center; /* 居中背景图片 */
  background-repeat: no-repeat; /* 防止背景图片重复 */
}
.login-box {
  width: 400px;
  padding: 30px;
  border-radius: 5px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  background-color: rgba(255, 255, 255, 1);
}
</style>