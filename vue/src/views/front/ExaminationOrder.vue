<template>
  <div>
    <!-- 搜索框 -->
    <el-row :gutter="10" style="margin-top: 5px; margin-bottom: 20px; text-align: center">
      <el-col :span="24">
        <el-input
            placeholder="请输入订单编号查询"
            v-model="data.orderId"
            @input="load"
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

    <div class="card-container" style="width: 80%; margin-bottom: 5px">
      <el-table stripe :data="data.tableData" @selection-change="handleSelectionChange">
        <el-table-column label="详情" width="60px" type="expand">
          <template #default="scope">
            <div style="padding: 10px">
              <el-descriptions title="订单详细信息" border>
                <el-descriptions-item label="订单号">{{ scope.row.orderId }}</el-descriptions-item>
                <el-descriptions-item label="用户">{{ scope.row.userName }}</el-descriptions-item>
                <el-descriptions-item label="医生">{{ scope.row.doctorName }}</el-descriptions-item>
                <el-descriptions-item label="预约日期">{{ scope.row.reserveDate }}</el-descriptions-item>
                <el-descriptions-item label="预约时间">{{ scope.row.time }}</el-descriptions-item>
                <el-descriptions-item label="建议">{{ scope.row.comment }}</el-descriptions-item>
                <el-descriptions-item label="费用">{{ scope.row.money }} 元</el-descriptions-item>
                <el-descriptions-item label="订单状态">{{ scope.row.status }}</el-descriptions-item>
                <el-descriptions-item label="创建时间">{{ scope.row.createTime }}</el-descriptions-item>
                <el-descriptions-item label="检查时间">{{ scope.row.checkTime }}</el-descriptions-item>
                <el-descriptions-item label="反馈">{{ scope.row.feedback }}</el-descriptions-item>
              </el-descriptions>
            </div>
          </template>
        </el-table-column>
        <el-table-column type="selection" width="55" v-if="data.user.role === 'ADMIN'" />
        <el-table-column prop="orderId" label="订单号" width="200px"></el-table-column>
        <el-table-column prop="userName" label="用户"></el-table-column>
        <el-table-column prop="doctorName" label="医生">
          <template #default="scope">
            <span v-if="scope.row.doctorName">{{ scope.row.doctorName }}</span>
            <span v-else>-</span> <!-- 默认值 -->
          </template>
        </el-table-column>
        <el-table-column prop="reserveDate" label="预约日期"></el-table-column>
        <el-table-column prop="file" label="检查结果">
          <template #default="scope">
            <el-button type="primary" plain v-if="scope.row.file" @click="download(scope.row.file)">下载</el-button>
          </template>
        </el-table-column>
        <el-table-column prop="money" label="费用"></el-table-column>
        <el-table-column prop="status" label="订单状态">
          <template #default="scope">
            <el-tag type="danger" v-if="scope.row.status === '已取消'">已取消</el-tag>
            <el-tag type="primary" v-if="scope.row.status === '待审批'">待审批</el-tag>
            <el-tag type="danger" v-if="scope.row.status === '审批拒绝'">审批拒绝</el-tag>
            <el-tag type="info" v-if="scope.row.status === '待支付'">待支付</el-tag>
            <el-tag type="primary" v-if="scope.row.status === '待检查'">待检查</el-tag>
            <el-tag type="info" v-if="scope.row.status === '待上传报告'">待上传报告</el-tag>
            <el-tag type="success" v-if="scope.row.status === '已完成'">已完成</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100" fixed="right">
          <template v-slot="scope">
            <div>
              <el-button type="danger" size="small" @click="changeStatus(scope.row, '已取消')"
                         v-if="scope.row.status === '待审批'">取消</el-button>
              <el-button type="info" size="small" @click="changeStatus(scope.row, '待检查')"
                         v-if="scope.row.status === '待支付'">支付</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="pagination-container" v-if="data.total">
      <el-pagination @current-change="load" background layout="prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total" />
    </div>

  </div>
</template>

<script setup>
import { reactive } from "vue";
import request from "@/utils/request.js";
import { ElMessage, ElMessageBox } from "element-plus";
import { Delete, Edit, Search } from "@element-plus/icons-vue";

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  formVisible: false,
  form: {},
  tableData: [],
  pageNum: 1,
  pageSize: 10,
  total: 0,
  orderId: null,
  ids: []
});

const load = () => {
  request.get('/examinationOrder/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      orderId: data.orderId
    }
  }).then(res => {
    if (res.code === '200') {
      data.tableData = res.data?.list || [];
      data.total = res.data?.total;
    }
  });
};


const download = (file) => {
  window.open(file)
}

const changeStatus = (row, status) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.form.status = status
  if (status === '已取消' || '待上传报告') {
    ElMessageBox.confirm('您确认操作吗？', '确认', {type:"warning"}).then(res =>{
      request.put('/examinationOrder/update', data.form).then(res => {
        if (res.code === '200') {
          ElMessage.success('操作成功');
          load();
        } else {
          ElMessage.error(res.msg);
        }
      })
    }).catch()
  }
}


const handleSelectionChange = (rows) => {
  data.ids = rows.map(v => v.id);
};


load();
</script>

<style>
.card-container {
  width: 80%;
  margin: 0 auto;
  display: flex;
  flex-wrap: wrap;
  justify-content: flex-start;
}
.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

</style>