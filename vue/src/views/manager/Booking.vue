<template>
  <div>
    <!-- 操作按钮和搜索框 -->
    <div class="card1">
      <div style="display: flex; align-items: center; justify-content: space-between; width: 100%;">
        <div style="display: flex; align-items: center;">
          <el-button type="primary" plain @click="handleAdd">增加在线预约</el-button>
          <el-button type="danger" plain style="margin-left: 10px" @click="delBatch">批量删除</el-button>
        </div>
        <el-input
            v-model="data.name"
            style="width: 30%;"
            placeholder="请输入医生姓名查询"
            @input="load"
            clearable
        >
          <template #prepend>
            <el-icon><Search /></el-icon>
          </template>
          <template #append>
            <el-button icon="el-icon-search" @click="load"></el-button>
          </template>
        </el-input>
      </div>
    </div>

    <!-- 表格 -->
    <div class="card" style="margin-bottom: 5px">
      <el-table stripe :data="data.tableData" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" />
        <el-table-column label="照片">
          <template #default="scope">
            <el-image style="width: 50px; height: 50px; border-radius: 50%" :src="scope.row.doctorAvatar" :preview-src-list="[scope.row.doctorAvatar]" preview-teleported></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="doctorName" label="医生"></el-table-column>
        <el-table-column prop="departmentName" label="所属科室"></el-table-column>
        <el-table-column prop="money" label="挂号费"></el-table-column>
        <el-table-column label="修改或删除" width="100" fixed="right">
          <template v-slot="scope">
            <el-button type="primary" circle :icon="Edit" @click="handleEdit(scope.row)"></el-button>
            <el-button type="danger" circle :icon="Delete" @click="del(scope.row.id)"></el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="card" v-if="data.total">
      <el-pagination @current-change="load" background layout="prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total" />
    </div>

    <!-- 在线预约信息对话框 -->
    <el-dialog title="在线预约信息" v-model="data.formVisible" width="40%" destroy-on-close>
      <el-form ref="form" :model="data.form" label-width="70px" style="padding: 20px">
        <el-form-item label="所属科室" prop="departmentId">
          <el-select style="width: 100%" v-model="data.form.departmentId">
            <el-option v-for="item in data.departmentList" :key="item.id" :value="item.id" :label="item.name"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="费用" prop="money">
          <el-input v-model="data.form.money" placeholder="费用"></el-input>
        </el-form-item>
        <el-form-item label="负责医生" prop="doctorId">
          <el-select style="width: 100%" v-model="data.form.doctorId">
            <el-option v-for="item in data.doctorList" :key="item.id" :value="item.id" :label="item.name"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="data.formVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">确 定</el-button>
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

//响应式数据对象
//当这些属性的值发生变化时，组件会自动更新以反映这些变化
const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  formVisible: false,
  form: {},
  tableData: [],
  pageNum: 1,//当前页码
  pageSize: 10,//每页数据条数
  total: 0,
  name: null,
  ids: [],
  doctorList: [],
  departmentList: []
});

// 获取医生列表
request.get('/doctor/selectAll').then(res => {
  data.doctorList = res.data;
});

// 获取科室列表
request.get('/department/selectAll').then(res => {
  data.departmentList = res.data;
});

//加载预约数据
const load = () => {
  request.get('/booking/selectByDoctorName', {
    params: {
      name: data.name || '', // 确保 name 为 null 时传递空字符串
      pageNum: data.pageNum,
      pageSize: data.pageSize
    }
  }).then(res => {
    if (res.code === '200') {
      data.tableData = res.data?.list || [];
      data.total = res.data?.total;
    } else {
      ElMessage.error(res.msg);
    }
  }).catch(error => {
    console.error('Error loading data:', error);
    ElMessage.error('加载数据失败，请稍后再试');
  });
};

//处理增加预约
const handleAdd = () => {
  data.form = {};
  data.formVisible = true;
};

const delBatch = () => {
  if (!data.ids.length) {
    ElMessage.warning("请选择要删除的数据");
    return;
  }
  ElMessageBox.confirm('此操作不可逆！', '删除在线预约', { type: 'warning' }).then(() => {
    request.delete("/booking/delete/batch", { data: data.ids }).then(res => {
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

//处理编辑预约
//将选中的预约记录行的数据复制到 data.form 对象中。
// 使用 JSON.stringify(row) 将 row 对象转换为一个JSON字符串
// 然后使用 JSON.parse(...) 将这个JSON字符串再转换回一个对象
// 这个过程称为“深拷贝”，目的是创建 row 数据的一个完全独立的副本，以避免直接修改原始数据，因为直接修改原始数据可能会导致数据绑定的意外行为
const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row));
  data.formVisible = true;
};

//新增预约
const add = () => {
  request.post('/booking/add', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('已成功新增预约');
      data.formVisible = false;
      load();
    } else {
      ElMessage.error(res.msg);
    }
  });
};

//更新预约
const update = () => {
  request.put('/booking/update', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('更新已成功');
      data.formVisible = false;
      load();
    } else {
      ElMessage.error(res.msg);
    }
  });
};

//保存预约信息
const save = () => {
  data.form.id ? update() : add();
};

const del = (id) => {
  ElMessageBox.confirm('此操作不可逆！', '删除预约', { type: 'warning' }).then(() => {
    request.delete('/booking/delete/' + id).then(res => {
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

//处理表格选择变化
const handleSelectionChange = (rows) => {
  data.ids = rows.map(v => v.id);
};

const reset = () => {
  data.name = null;
  load();
};

// 初始加载数据
load();
</script>

<style>
.card1 {
  background-color: #fff;
  border-radius: 5px;
  padding: 10px 20px 10px 10px;
  margin-top: 2px;
  box-shadow: 0 0 10px rgba(0, 0, 0, .1);
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 5px;
}
</style>