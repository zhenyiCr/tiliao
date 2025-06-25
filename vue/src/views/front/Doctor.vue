<template>
  <div>
    <!-- 搜索框 -->
    <el-row :gutter="10" style="margin-top: 5px; margin-bottom: 20px; text-align: center">
      <el-col :span="24">
        <el-input
            placeholder="请输入医生姓名查询"
            v-model="searchName"
            @input="searchDoctors"
            clearable
            style="width: 80%; margin: 0 auto"
        >
          <template #prepend>
            <el-icon><Search /></el-icon>
          </template>
          <template #append>
            <el-button icon="el-icon-search" @click="searchDoctors"></el-button>
          </template>
        </el-input>
      </el-col>
    </el-row>

    <div class="card-container">
      <div class="doctor-card" v-for="doctor in doctors" :key="doctor.id" @click="showDetails(doctor)">
        <div class="doctor-avatar"><img :src="doctor.avatar" alt="Doctor's avatar" style="width: 60px; height: 60px; border-radius: 50%;"/></div>
        <div class="doctor-info">
          <h3 class="doctor-name">{{ doctor.name }}</h3>
          <p>性别：{{ doctor.gender }}</p>
          <p>科室：{{ doctor.departmentName }}</p>
          <p>职称：{{ doctor.titleName }}</p>
          <p class="doctor-description">个人简介：{{ doctor.description }}</p>
        </div>
      </div>
    </div>

    <el-pagination class="pagination-container"
        background
        layout="prev, pager, next"
        :total="total"
        :page-size="pageSize"
        :current-page="currentPage"
        @current-change="handlePageChange"
        style="text-align: center; margin-top: 20px;"
    ></el-pagination>

    <el-dialog title="医生详细信息" v-model="detailVisible" width="30%">
      <div v-if="selectedDoctor">
        <img :src="selectedDoctor.avatar" alt="Doctor's avatar" style="width: 100px; height: 100px; border-radius: 50%;"/>
        <h3>{{ selectedDoctor.name }}</h3>
        <p><strong>账号：</strong>{{ selectedDoctor.username }}</p>
        <p><strong>性别：</strong>{{ selectedDoctor.gender }}</p>
        <p><strong>科室：</strong>{{ selectedDoctor.departmentName }}</p>
        <p><strong>职称：</strong>{{ selectedDoctor.titleName }}</p>
        <p><strong>联系方式：</strong>{{ selectedDoctor.contact }}</p>
        <p><strong>个人简介：</strong>{{ selectedDoctor.description }}</p>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="detailVisible = false">关闭</el-button>
      </span>
    </el-dialog>

  </div>

</template>

<script setup>
import { reactive, ref, computed } from "vue";
import request from "@/utils/request.js";
import { Search } from '@element-plus/icons-vue';

const searchName = ref('');
const doctors = ref([]);
const detailVisible = ref(false);
const selectedDoctor = ref(null);
const currentPage = ref(1); // 当前页
const pageSize = 15; // 每页显示条目数
const total = ref(0); // 总记录数

// 计算属性用于获取当前页需要展示的医生列表
const paginatedDoctors = computed(() => {
  const start = (currentPage.value - 1) * pageSize;
  const end = start + pageSize;
  return doctors.value.slice(start, end);
});

const searchDoctors = () => {
  request.get('/doctor/selectPage', {
    params: {
      name: searchName.value,
      pageNum: currentPage.value,
      pageSize: pageSize
    }
  }).then(res => {
    if (res.code === '200') {
      const pageInfo = res.data;
      doctors.value = pageInfo.list || []; // 医生数据列表
      total.value = pageInfo.total || 0; // 总记录数
    }
  });
};

const showDetails = (doctor) => {
  selectedDoctor.value = doctor;
  detailVisible.value = true;
};

const handlePageChange = (newPage) => {
  currentPage.value = newPage;
  searchDoctors();
};

searchDoctors(); // 初始加载数据
</script>

<style scoped>
.card-container {
  width: 80%;
  margin: 20px auto;
  display: flex;
  flex-wrap: wrap;
  justify-content: flex-start;
}
.doctor-card {
  border: 1px solid #ebeef5;
  border-radius: 4px;
  transition: all 0.3s;
  width: calc(20% - 20px);
  margin: 10px;
  cursor: pointer;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
}
.doctor-card:hover {
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}
.doctor-avatar {
  margin-bottom: 10px;
}
.doctor-info {
  text-align: left; /* Align text to the left */
  width: 100%; /* Ensure the info block takes full width of the card */
}
.doctor-info h3.doctor-name {
  margin: 0;
  font-size: 1.2em;
}
.doctor-info p {
  margin: 5px 0;
}
.doctor-description {
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2; /* Limit to 2 lines */
  -webkit-box-orient: vertical;
  word-wrap: break-word; /* Ensure long words are broken */
}
.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
</style>