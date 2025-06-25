<template>
  <div class="departments-container" style="width: 80%; margin: 0 auto;">
    <!-- 搜索框 -->
    <el-row :gutter="10" style="margin-top: 5px; margin-bottom: 20px; text-align: center">
      <el-col :span="24">
        <el-input
            placeholder="请输入科室名称查询"
            v-model="data.name"
            @input="handleSearch"
            clearable
            style="width: 100%; margin: 0 auto"
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

    <!-- 科室列表 -->
    <div class="card" style="margin-bottom: 5px; display: flex; flex-wrap: wrap; justify-content: center; gap: 20px; padding: 20px;">
      <div
          class="department-card"
          v-for="dept in data.tableData"
          :key="dept.id"
          :style="{ width: '80%', overflow: 'hidden', textOverflow: 'ellipsis', display: 'flex', alignItems: 'center', height: '60px' }"
          @mouseover="hoverDept(dept)"
          @mouseleave="unhoverDept"
          @click="showDetails(dept)"
      >
        <div class="department-name">{{ dept.name }}</div>
        <div class="department-info" v-html="dept.information"></div>
      </div>
    </div>

    <!-- 分页 -->
    <div class="pagination-container">
      <el-pagination @current-change="load" background layout="prev, pager, next" :page-size="10" v-model:current-page="data.pageNum" :total="data.total" />
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue';
import request from "@/utils/request.js";
import { ElMessageBox } from 'element-plus';
import { Search } from '@element-plus/icons-vue';

const data = reactive({
  formVisible: false,
  form: {},
  tableData: [],
  pageNum: 1,
  pageSize: 10,
  total: 0,
  name: null,
  ids: []
});

const hoveredDept = ref(null);

const isHovered = (dept) => {
  return hoveredDept.value && hoveredDept.value.id === dept.id;
};

const hoverDept = (dept) => {
  hoveredDept.value = dept;
};

const unhoverDept = () => {
  hoveredDept.value = null;
};

const showDetails = (dept) => {
  ElMessageBox({
    title: dept.name,
    message: dept.information,
    dangerouslyUseHTMLString: true,
  });
};

const handleSearch = () => {
  data.pageNum = 1; // 重置页码为第一页
  load();
};

const load = () => {
  request.get('/department/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      name: data.name
    }
  }).then(res => {
    if (res.code === '200') {
      data.tableData = res.data?.list || [];
      data.total = res.data?.total;
    }
  }).catch(error => {
    console.error('Failed to load departments:', error);
  });
};

const reset = () => {
  data.name = null;
  data.pageNum = 1;
  load();
};

load();
</script>

<style scoped>
.departments-container {
  max-width: 1200px; /* 限制最大宽度 */
}

.department-card {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  padding: 10px;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  transition: background-color 0.3s;
  cursor: pointer;
  height: auto; /* 根据内容自动调整高度 */
  min-height: 80px; /* 设置最小高度 */
  width: 100%; /* 保持每个卡片占满整行 */
}

.department-card:hover {
  background-color: #f5f5f5;
}

.department-name {
  flex-shrink: 0;
  width: 10%;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  font-weight: bold; /* 加粗科室名字 */
  font-size: 16px;
}

.department-info {
  flex-grow: 1;
  width: 90%;
  overflow: hidden;
  max-height: 4.5em; /* 根据行高和行数调整 */
  line-height: 2;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2; /* 限制行数 */
  -webkit-box-orient: vertical;
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
</style>