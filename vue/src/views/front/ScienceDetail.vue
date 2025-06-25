<template>
  <div>
    <div class="card" style="padding: 20px">
      <div><el-icon size="26" style="cursor: pointer" @click="router.back()"><Back /></el-icon></div>
      <div style="text-align: center; font-size: 24px; font-weight: bold">{{ data.science.title }}</div>

      <div style="color: #666; text-align: center; margin: 20px 0">
        <span style="margin-right: 20px"><el-icon size="14" style="top: 2px"><User /></el-icon> {{ data.science.authorName }}</span>
        <span><el-icon size="14" style="top: 2px"><Clock /></el-icon> {{ data.science.publishTime }}</span>
      </div>

      <div style="text-align: justify; line-height: 24px; padding: 0 50px">
        <div v-html="data.science.content"></div>
      </div>
    </div>

  </div>
</template>

<script setup>
import { reactive } from "vue";
import request from "@/utils/request.js";
import router from "@/router/index.js";
// 确保 load 函数在组件加载时被调用
import { onMounted } from 'vue';
onMounted(() => {
  load();
});

const data = reactive({
  id: router.currentRoute.value.query.id,
  science: {}
})

const load = () => {
  request.get('/science/selectById/' + data.id).then(res => {
    if (res.code === '200') {
      data.science = res.data
    }
  })
}

</script>

<style>
.title {
  cursor: pointer;
}
.title:hover {
  color: red;
  text-decoration: underline;
}
</style>