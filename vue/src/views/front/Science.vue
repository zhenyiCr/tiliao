<template>
  <div>
    <!-- 搜索框 -->
    <el-row :gutter="10" style="margin-top: 5px; margin-bottom: 20px; text-align: center">
      <el-col :span="24">
        <el-input
            placeholder="请输入资讯标题搜索，点击标题可查看完整内容哦!"
            v-model="searchTitle"
            @input="handleSearch"
            clearable
            style="width: 80%; margin: 0 auto"
        >
          <template #prepend>
            <el-icon><Search /></el-icon>
          </template>
          <template #append>
            <el-button icon="el-icon-search" @click="handleSearch"></el-button>
          </template>
        </el-input>
      </el-col>
    </el-row>

    <!-- 资讯列表 -->
    <el-row :gutter="10">
      <el-col :span="24" v-for="item in data.tableData" :key="item.id">
        <div class="card2" style="padding: 15px; margin-bottom: 10px; display: flex; grid-gap: 20px">
          <img :src="item.picture" alt="" style="width: 100px; height: 100px;border-radius: 5%">
          <div style="flex: 1; width: 0">
            <div style="font-size: 18px" class="line1 title" @click="goPage('/front/scienceDetail?id=' + item.id)"><strong>{{ item.title }}</strong></div>
            <div style="color: #666; margin: 20px 0; line-height: 24px; text-align: justify" class="line3" v-html="item.content"></div>
            <div style="color: #666">
              <span style="margin-right: 20px"><el-icon size="14"><User /></el-icon> {{ item.authorName }}</span>
              <!-- <span style="margin-right: 20px"><el-icon size="14"><View /></el-icon> {{ item.viewCount }}</span> -->
              <span><el-icon size="14"><Clock /></el-icon> {{ item.publishTime }}</span>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 分页组件 -->
    <div class="pagination-container" v-if="data.total">
      <el-pagination @current-change="load" background layout="prev, pager, next" :page-size="10" v-model:current-page="data.pageNum" :total="data.total" />
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import request from "@/utils/request.js";
import { Search } from '@element-plus/icons-vue';
import { User, Clock } from '@element-plus/icons-vue';

const data = reactive({
  tableData: [],
  pageNum: 1,
  pageSize: 10,
  total: 0,
})
const searchTitle = ref('')

const goPage = (path) => {
  location.href = path
}

const handleSearch = () => {
  if (searchTitle.value.trim() === '') {
    load()
  } else {
    request.get('/science/selectPage', {
      params: {
        title: searchTitle.value,
        pageNum: data.pageNum,
        pageSize: data.pageSize,
      }
    }).then(res => {
      if (res.code === '200') {
        data.tableData = res.data?.list || []
        data.total = res.data?.total
      }
    })
  }
}

const load = (pageNum) => {
  request.get('/science/selectPage', {
    params: {
      pageNum: pageNum,
      pageSize: data.pageSize,
    }
  }).then(res => {
    if (res.code === '200') {
      data.tableData = res.data?.list || []
      data.total = res.data?.total
      data.pageNum = pageNum
    }
  })
}
load(data.pageNum)
</script>

<style>
.title {
  cursor: pointer;
}
.title:hover {
  color: #2c77e8;
}
.card2 {
  transition: box-shadow 0.3s ease-in-out;
}
.card2:hover {
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}
/* 自定义el-input样式 */
.el-input__inner {
  border: none; /* 去除边框 */
  outline: none; /* 去除焦点时的轮廓 */
}
/* 分页组件样式 */
.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
</style>