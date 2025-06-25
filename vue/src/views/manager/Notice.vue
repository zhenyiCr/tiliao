<template>
  <div>
    <div class="card-div" >
      <div style="display: flex; align-items: center;">
        <el-button type="primary" plain @click="handleAdd">新增公告</el-button>
        <el-button type="danger" plain @click="delBatch">批量删除</el-button>
      </div>
      <el-input
          placeholder="请输入标题查询"
          v-model="data.title"
          @input="load"
          clearable
          style="margin-left: auto; width:30%"
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
        <el-table-column type="selection" width="55" />
        <el-table-column prop="title" label="公告标题" />
        <el-table-column prop="content" label="公告内容" />
        <el-table-column prop="time" label="发布时间" />
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

    <el-dialog title="公告信息" v-model="data.formVisible" width="40%" destroy-on-close>
      <el-form ref="form" :model="data.form" label-width="70px" style="padding: 20px">
        <el-form-item prop="title" label="公告标题">
          <el-input v-model="data.form.title" placeholder="请输入公告标题"></el-input>
        </el-form-item>
        <el-form-item prop="content" label="公告内容">
          <el-input type="textarea" :rows="4" v-model="data.form.content" placeholder="请输入公告内容"></el-input>
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

const data = reactive({
  formVisible: false,
  form: {},
  tableData: [],
  pageNum: 1,
  pageSize: 10,
  total: 0,
  title: null,
  ids: []
});

const load = () => {
  request.get('/notice/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      title: data.title
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
  request.post('/notice/add', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('已成功发布公告');
      data.formVisible = false;
      load();
    } else {
      ElMessage.error(res.msg);
    }
  });
};

const update = () => {
  request.put('/notice/update', data.form).then(res => {
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
  ElMessageBox.confirm('此操作不可逆！', '删除公告', { type: 'warning' }).then(res => {
    request.delete('/notice/delete/' + id).then(res => {
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
  ElMessageBox.confirm('此操作不可逆！', '删除公告', { type: 'warning' }).then(res => {
    request.delete("/notice/delete/batch", { data: data.ids }).then(res => {
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
  data.title = null;
  load();
};

load();
</script>

<style>
.card-div {
  background-color: #fff;
  border-radius: 5px;
  padding: 10px;
  box-shadow: 0 0 10px rgba(0,0,0,.1);
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: flex-end; /* 使搜索栏靠右对齐 */
  align-items: center;
  justify-content: space-between;
}

.el-input {
  width: auto !important;
}
</style>