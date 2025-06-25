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

    <div class="card-container" style="width: 80%; margin: 0 auto;">
      <el-row :gutter="8">
        <el-col :span="6" v-for="item in data.tableData" :key="item.doctorId">
          <div class="card3" style="margin-bottom: 10px; padding: 15px">
            <img :src="item.doctorAvatar" alt="" style=" border-radius: 50%; height: 30%;">
            <div style="margin: 10px 0"><span style="color: #666">医生：</span>{{ item.doctorName }}</div>
            <div style="margin: 10px 0"><span style="color: #666">科室：</span>{{ item.departmentName }}</div>
            <div style="display: flex; align-items: center">
              <div style="flex: 1">
                <span style="color: #666">挂号费：</span><span style="color: red; font-size: 16px">￥{{ item.money }}元</span>
              </div>
              <el-button type="primary" @click="handleReserve(item)">立即预约</el-button>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <div class="pagination-container" v-if="data.total">
      <el-pagination
          @current-change="load"
          background
          layout="prev, pager, next"
          :page-size="data.pageSize"
          v-model:current-page="data.pageNum"
          :total="data.total"
      />
    </div>

    <el-dialog title="在线预约" v-model="data.formVisible" width="35%" destroy-on-close>
      <el-form ref="formRef" :rules="data.rules" :model="data.form" label-width="90px" style="padding: 20px">
        <el-form-item prop="reserveDate">
          <template #label>
            <span>预约日期</span>
          </template>
          <el-date-picker
              :disabled-date="disabledDate"
              style="width: 100%"
              v-model="data.form.reserveDate"
              type="date"
              placeholder="预约日期"
              format="YYYY-MM-DD"
              value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item label="预约时间" prop="time">
          <div style="display: flex; align-items: center; grid-gap: 10px">
            <el-time-picker
                style="flex: 1"
                start="08:30"
                v-model="data.form.time"
                format="HH:mm"
                value-format="HH:mm"
                placeholder="开始时间"
            />
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="data.formVisible = false">取 消</el-button>
          <el-button type="primary" @click="addReserve">确 定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from "vue";
import request from "@/utils/request.js";
import { Search } from "@element-plus/icons-vue";
import { ElMessage } from 'element-plus';

const searchName = ref('');
const formRef = ref();

// 搜索医生
const searchDoctors = () => {
  data.name = searchName.value; // 将搜索名称赋值给 data.name
  data.pageNum = 1; // 重置分页信息
  load(); // 调用 load 方法加载数据
};

// 时间验证器
const timeValidator = (rule, value, callback) => {
  if (!data.form.time) {
    callback(new Error('请输入预约时间'));
  } else {
    callback();
  }
}

// 数据状态
const data = reactive({
  tableData: [],
  pageNum: 1,
  pageSize: 12,
  total: 0,
  name: null, // 初始时没有搜索条件
  formVisible: false,
  form: {},
  rules: {
    reserveDate: [
      { required: true, message: '请选择预约日期', trigger: 'change' }
    ],
    time: [
      { validator: timeValidator, trigger: 'change' }
    ]
  }
});

// 禁用过去的日期 用户只能选择明天开始的日期
const disabledDate = (date) => {
  return date.getTime() < new Date().getTime();
}

const handleReserve = (item) => { //处理预约 当点击“立即预约”时触发
  data.form = {
    doctorId: item.doctorId,
    money: item.money,
    reserveDate: '',
    time: '',
  };
  data.formVisible = true;
  console.log('预约的医生ID:', item.doctorId); // 打印医生ID
};

const addReserve = () => { //新增预约 当点击“确定”预约时触发
  formRef.value.validate((valid) => {
    if (valid) {
      request.post('/examinationOrder/add', data.form).then(res => {
        if (res.code === '200') {
          ElMessage.success('预约成功');
          data.formVisible = false;
          reset(); // 重置并重新加载数据
        } else {
          ElMessage.error(res.msg);
        }
      });
    }
  });
};

const load = () => {
  request.get('/booking/selectByDoctorName', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      name: data.name || ''
    }
  }).then(res => {
    console.log('Loaded data:', res);
    if (res.code === '200') {
      data.tableData = res.data?.list || [];
      console.log('Updated tableData:', data.tableData); // 在这里打印 tableData
      data.total = res.data?.total || 0;
    } else {
      ElMessage.error(res.msg);
    }
  }).catch(error => {
    console.error('数据加载失败:', error);
    ElMessage.error('加载数据失败，请稍后再试');
  });
};

onMounted(() => {
  load();
});

const resetSearch = () => {
  searchName.value = '';
  data.name = null;
  data.pageNum = 1;
  load();
};

const reset = () => {
  data.name = null;
  data.pageNum = 1;
  load();
};
</script>

<style>
.card-container {
  width: 100%;
  display: flex;
  flex-wrap: wrap;
  justify-content: flex-start;
}

.pagination-container {
  display: flex;
  justify-content: center; /* 水平居中 */
  align-items: center;
  margin-top: 20px;
  width: 100%;
}
</style>
