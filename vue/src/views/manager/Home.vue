<template>
  <div>
    <div class="card1" >{{ data.user?.name }}，欢迎使用医院预约管理系统！</div>
    <div style="display: flex">
      <div class="card2">
        <div style="font-weight: bold; font-size: 18px; padding: 10px 0 30px 10px">系统公告</div>
        <el-timeline style="max-width: 600px">
          <el-timeline-item
              v-for="(item, index) in data.noticeData"
              :key="index"
              :timestamp="item.time"
          >
            {{ item.content }}
          </el-timeline-item>
        </el-timeline>
      </div>
      <div style="flex: 50%"></div>
    </div>
  </div>
</template>

<script setup>

import {reactive} from "vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  noticeData: []
})

const loadNotice = () => {
  request.get('/notice/selectAll').then(res => {
    if (res.code === '200') {
      data.noticeData = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}
loadNotice()
</script>

<style scoped>
.card1 {
  background-color: #fff;
  border-radius: 5px;
  padding: 10px;
  box-shadow: 0 0 10px rgba(0,0,0,.1);
  width: 100%;
  margin-bottom: 5px;
  margin-top:5px
}

.card2 {
  background-color: #fff;
  border-radius: 5px;
  padding: 10px;
  box-shadow: 0 0 10px rgba(0,0,0,.1);
  width: 100%;
  margin-bottom: 5px;
  margin-top:5px;
  flex: 50%;
  height: 100%
}
</style>