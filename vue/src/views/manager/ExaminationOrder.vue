<template>
  <div>
    <div class="card1">
      <div style="display: flex; align-items: center;">
          <el-button type="danger" plain @click="delBatch" v-if="data.user.role === 'ADMIN'">批量删除</el-button>
        </div>
        <el-input
            placeholder="请输入订单编号查询"
            v-model="data.orderId"
            @input="load"
            clearable
            style="width: 30%; margin-left: auto;"
        >
          <template #prepend>
            <el-icon><Search /></el-icon>
          </template>
          <template #append>
            <el-button icon="el-icon-search" @click="load"></el-button>
          </template>
        </el-input>
    </div>

    <div class="card" style="margin-bottom: 5px">
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
            <div v-if="data.user.role === 'USER'">
              <el-button type="danger" size="small" @click="changeStatus(scope.row, '已取消')"
                         v-if="scope.row.status === '待审批'">取消</el-button>
              <el-button type="info" size="small" @click="changeStatus(scope.row, '待检查')"
                         v-if="scope.row.status === '待支付'">支付</el-button>
            </div>
          <div  v-if="data.user.role === 'DOCTOR'">
            <el-button type="primary" size="small" @click="audit(scope.row)"
                       v-if="scope.row.status ==='待审批'">审批</el-button>
            <el-button type="success" size="small" @click="changeStatus(scope.row, '待上传报告')"
                       v-if="scope.row.status ==='待检查'">检查完成</el-button>
            <el-button type="success" size="small" @click="audit(scope.row)"
                       v-if="scope.row.status ==='待上传报告'">上传报告</el-button>
          </div>
          <div  v-if="data.user.role === 'ADMIN'">
            <el-button type="danger" circle :icon="Delete" @click="del(scope.row.id)"></el-button>
          </div>
          </template>
        </el-table-column>

      </el-table>
    </div>
    <div class="card" v-if="data.total">
      <el-pagination @current-change="load" background layout="prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total" />
    </div>

    <el-dialog title="订单信息" v-model="data.formVisible" width="40%" destroy-on-close>
      <el-form ref="form" :model="data.form" label-width="70px" style="padding: 20px">
        <el-form-item prop="status" label="订单状态" v-if="data.form.status === '待审批'">
          <el-radio-group v-model="data.form.status1">
            <el-radio-button value="审批通过" label="审批通过">审批通过</el-radio-button>
            <el-radio-button value="审批拒绝" label="审批拒绝">审批拒绝</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item prop="feedback" label="反馈" v-if="data.form.status === '待审批'">
          <el-input type="textarea" :rows="3" placeholder="请输入信息" v-model="data.form.feedback"></el-input>
        </el-form-item>
        <el-form-item label="上传报告" v-if="data.form.status === '待上传报告'">
          <el-upload
              :action="baseUrl + '/files/upload'"
              :headers="{ 'token': data.user.token }"
              :on-success="handleFileUpload"
              list-type="picture"
          >
            <el-button type="primary">上传</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item prop="comment" label="建议" v-if="data.form.status === '待上传报告'">
          <el-input type="textarea" :rows="5" placeholder="请输入信息" v-model="data.form.comment"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="data.formVisible = false">取 消</el-button>
          <el-button type="primary" @click="saveOrder">确 定</el-button>
        </span>
      </template>
    </el-dialog>
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

const download = (file) => {
  window.open(file);  // 确保file是一个有效的URL
}

const baseUrl = import.meta.env.VITE_BASE_URL+'/api'
const handleFileUpload = (res) => {
  data.form.file = res.data
}

const audit = (row) => { //对某个订单进行审批操作
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
}

const saveOrder = () => {
  data.form.status = data.form.status1
  if (data.form.status === '审批通过') {
    data.form.status = '待支付'
  }
  if (data.form.file) {  // 已上传报告
    data.form.status = '已完成'
  }
  request.put('/examinationOrder/update', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('操作成功');
      load()
      data.formVisible = false
    } else {
      ElMessage.error(res.msg);
    }
  })
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

// const save = () => {
//   data.form.id ? update() : add();
// };

const del = (id) => {
  ElMessageBox.confirm('此操作不可逆！', '删除订单', { type: 'warning' }).then(res => {
    request.delete('/examinationOrder/delete/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success("删除已成功");
        load();
      } else {
        ElMessage.error(res.msg);
      }
    });
  }).catch(err => {
    console.error(err);
  });
};

const delBatch = () => {
  if (!data.ids.length) {
    ElMessage.warning("请选择数据");
    return;
  }
  ElMessageBox.confirm('此操作不可逆！', '删除订单', { type: 'warning' }).then(res => {
    request.delete("/examinationOrder/delete/batch", { data: data.ids }).then(res => {
      if (res.code === '200') {
        ElMessage.success('删除已成功');
        load();
      } else {
        ElMessage.error(res.msg);
      }
    });
  }).catch(err => {
    console.error(err);
  });
};

const handleSelectionChange = (rows) => {
  data.ids = rows.map(v => v.id);
};

const reset = () => {
  data.orderId = null;
  load();
};

load();
</script>

<style>
.card1 {
  background-color: #fff;
  border-radius: 5px;
  padding: 10px 20px 10px 10px;
  margin-top: 2px;
  //box-shadow: 0 0 10px rgba(0,0,0,.1);
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 5px;
}

</style>