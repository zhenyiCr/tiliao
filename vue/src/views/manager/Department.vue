<template>
  <div>
    <!-- 操作按钮和搜索框 -->
    <div class="card1">
      <div style="display: flex; align-items: center;">
        <el-button type="primary" plain @click="handleAdd">新增科室</el-button>
        <el-button type="danger" plain @click="delBatch">批量删除</el-button>
      </div>
      <el-input
          placeholder="请输入科室名称查询"
          v-model="data.name"
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

    <!-- 表格 -->
    <div class="card" style="margin-bottom: 5px">
      <el-table stripe :data="data.tableData" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="name" label="科室类型" />
        <el-table-column prop="information" label="科室介绍" />
        <el-table-column label="修改或删除" width="100" fixed="right">
          <template v-slot="scope">
            <el-button type="primary" circle :icon="Edit" @click="handleEdit(scope.row)"></el-button>
            <el-button type="danger" circle :icon="Delete" @click="del(scope.row.id)"></el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 分页 -->
    <div class="card" v-if="data.total">
      <el-pagination @current-change="load" background layout="prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total" />
    </div>

    <!-- 科室信息对话框 -->
    <el-dialog title="科室信息" v-model="data.formVisible" width="40%" destroy-on-close>
      <el-form ref="form" :model="data.form" label-width="70px" style="padding: 20px">
        <el-form-item prop="name" label="科室">
          <el-input v-model="data.form.name" placeholder="请输入科室名称"></el-input>
        </el-form-item>
        <el-form-item prop="information" label="科室介绍">
          <el-input v-model="data.form.information" type="textarea" placeholder="请输入科室介绍"></el-input>
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
import { Delete, Edit } from "@element-plus/icons-vue";
import { Search } from '@element-plus/icons-vue'; // 确保导入Search图标

const data = reactive({
  formVisible: false,
  form: {},
  tableData: [],
  pageNum: 1,
  pageSize: 4,
  total: 0,
  name: null,
  ids: []
});

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
  });
};

const handleAdd = () => {
  data.form = {};
  data.formVisible = true;
};

const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row));
  data.formVisible = true;
};

const add = () => {
  request.post('/department/add', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('已成功新增科室');
      data.formVisible = false;
      load();
    } else {
      ElMessage.error(res.msg);
    }
  });
};

const update = () => {
  request.put('/department/update', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('更新已成功');
      data.formVisible = false;
      load();
    }
  });
};

const save = () => {
  data.form.id ? update() : add();
};

const del = (id) => {
  ElMessageBox.confirm('此操作不可逆！', '删除科室', { type: 'warning' }).then(res => {
    request.delete('/department/delete/' + id).then(res => {
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
    ElMessage.warning("请选择要删除的数据");
    return;
  }
  ElMessageBox.confirm('此操作不可逆！', '删除科室', { type: 'warning' }).then(res => {
    request.delete("/department/delete/batch", {data: data.ids}).then(res => {
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
  data.name = null;
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