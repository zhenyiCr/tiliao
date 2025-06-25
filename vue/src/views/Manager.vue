<!--管理员页面-->
<template>
  <div class="manager-container">
    <!--头部-->
    <div class="manager-header">
      <div class="manager-header-left">
        <img src="@/assets/imgs/logo.png" alt="">
        <div class="title">医院预约管理系统</div>
      </div>
      <div class="manager-header-middle">
        <el-breadcrumb separator="/">
          <el-breadcrumb-item :to="{ path: '/manager/home' }">您正在浏览 ...</el-breadcrumb-item>
          <el-breadcrumb-item>{{ router.currentRoute.value.meta.name }}</el-breadcrumb-item>
        </el-breadcrumb>
      </div>
      <div class="manager-header-right">
        <!--头像的位置下拉菜单-->
        <el-dropdown style="cursor: pointer">
          <div class="manager-header-right-block">
            <img style="width: 40px; height: 40px; border-radius: 50%;" :src="data.user.avatar" alt="">
            <span class="manager-header-right-name">{{ data.user.name }}</span>
            <el-icon color="#fff"><arrow-down /></el-icon>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="router.push('/manager/person')">个人中心</el-dropdown-item>
              <el-dropdown-item @click="router.push('/manager/password')">修改密码</el-dropdown-item>
              <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>

    <!--主体部分-->
    <div style="display: flex">
      <div class="manager-body-left">
        <el-menu :default-active="router.currentRoute.value.path"
                 router
        >
          <el-menu-item index="/manager/home">
            <el-icon><House /></el-icon>
            <span>首页</span>
          </el-menu-item>

          <el-menu-item index="/manager/data" v-if="data.user.role === 'ADMIN'">
            <el-icon><DataLine /></el-icon>
            <span>数据统计</span>
          </el-menu-item>

          <el-sub-menu index="1"  v-if="data.user.role === 'ADMIN'">
            <template #title>
              <el-icon><Menu /></el-icon>
              <span>信息管理</span>
            </template>
            <el-menu-item index="/manager/department">科室信息</el-menu-item>
            <el-menu-item index="/manager/title">职称信息</el-menu-item>
            <el-menu-item index="/manager/booking">在线预约信息</el-menu-item>
            <el-menu-item index="/manager/examinationOrder">订单报表信息</el-menu-item>
          </el-sub-menu>

          <el-sub-menu index="2"  v-if="data.user.role === 'ADMIN'">
            <template #title>
              <el-icon><Platform /></el-icon>
              <span>系统管理</span>
            </template>
            <el-menu-item index="/manager/science">健康资讯</el-menu-item>
            <el-menu-item index="/manager/notice">系统公告</el-menu-item>
          </el-sub-menu>

          <el-sub-menu index="3"  v-if="data.user.role === 'ADMIN'">
            <template #title>
              <el-icon><User /></el-icon>
              <span>用户管理</span>
            </template>
            <el-menu-item index="/manager/user">用户信息</el-menu-item>
            <el-menu-item index="/manager/doctor">医生信息</el-menu-item>
            <el-menu-item index="/manager/admin">管理员信息</el-menu-item>
          </el-sub-menu>

          <el-sub-menu index="4"  v-if="data.user.role === 'DOCTOR'">
            <template #title>
              <el-icon><User /></el-icon>
              <span>信息管理</span>
            </template>
            <el-menu-item index="/manager/examinationOrder">订单报表信息</el-menu-item>
          </el-sub-menu>
        </el-menu>
      </div>
      <div class="manager-body-right">
        <RouterView />
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive } from "vue";
import router from "@/router/index.js";
import {ElMessage} from "element-plus";

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}')
})

const logout = () => {
  localStorage.removeItem('xm-user')
  router.push('/login')
}

const updateUser = () => {
  data.user =  JSON.parse(localStorage.getItem('xm-user') || '{}')
}

if (!data.user.id) {
  logout()
  ElMessage.error('请登录！')
}
</script>

<style scoped>
@import "@/assets/css/manager.css";

body {
  background: linear-gradient(to bottom right, white, #CCE2FD);
}
</style>