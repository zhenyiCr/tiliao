<template>
  <div>
    <div class="card1">
      <div style="display: flex; align-items: center;">
      <el-button type="primary" plain @click="handleAdd">新增医生</el-button>
      <el-button type="danger" plain @click="delBatch">批量删除</el-button>
    </div>
      <el-input
          placeholder="请输入医生姓名查询"
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

    <div class="card" style="margin-bottom: 5px">
      <el-table tooltip-effect="dark" stripe :data="data.tableData" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="username" label="账号" />
        <el-table-column prop="avatar" label="头像">
          <template v-slot="scope">
            <el-image style="width: 40px; height: 40px; border-radius: 50%; display: block" v-if="scope.row.avatar"
                      :src="scope.row.avatar" :preview-src-list="[scope.row.avatar]" preview-teleported></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="姓名" />
        <el-table-column prop="gender" label="性别" />
        <el-table-column prop="departmentName" label="科室" />
        <el-table-column prop="titleName" label="职称" />
        <el-table-column prop="contact" label="联系电话" />
        <el-table-column prop="description" label="个人背景" show-overflow-tooltip />
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

    <el-dialog title="医生信息" v-model="data.formVisible" width="40%" destroy-on-close>
      <el-form ref="formRef" :model="data.form" :rules="data.rules" label-width="70px" style="padding: 20px">
        <el-form-item prop="username" label="账号">
          <el-input v-model="data.form.username" placeholder="请输入账号"></el-input>
        </el-form-item>
        <el-form-item prop="avatar" label="头像">
          <el-upload
              :action="baseUrl + '/files/upload'"
              :on-success="handleFileUpload"
              list-type="picture">
            <el-button type="primary">点击上传</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item prop="name" label="姓名">
          <el-input v-model="data.form.name" placeholder="请输入姓名"></el-input>
        </el-form-item>
        <el-form-item prop="gender" label="性别">
          <el-select v-model="data.form.gender" placeholder="请选择性别">
            <el-option label="男" value="男"></el-option>
            <el-option label="女" value="女"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="科室">
          <el-select style="width:100%" v-model="data.form.departmentId">
            <el-option v-for="item in data.departmentList" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="职称">
          <el-select  style="width:100%" v-model="data.form.titleId">
            <el-option v-for="item in data.titleList" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="contact" label="电话">
          <el-input v-model="data.form.contact" placeholder="请输入联系电话"></el-input>
        </el-form-item>
        <el-form-item prop="description" label="个人背景">
          <el-input v-model="data.form.description" type="textarea" placeholder="请输入个人背景"></el-input>
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
import { reactive, ref } from "vue";
import request from "@/utils/request.js";
import { ElMessage, ElMessageBox } from "element-plus";
import {Delete, Edit, Search} from "@element-plus/icons-vue";

const baseUrl = import.meta.env.VITE_BASE_URL+'/api';
const formRef = ref(null);

const data = reactive({
  formVisible: false,
  form: {
    id: null,
    username: '',
    avatar: '',
    name: '',
    gender: '男',
    contact: '',
    departmentId: null,
    titleId: null,
    description: ''
  },
  rules: {
    username: [
      { required: true, message: '请输入账号', trigger: 'blur' },
      { pattern: /^[a-zA-Z0-9]+$/, message: '账号只能包含英文和数字', trigger: 'blur' }
    ],
    contact: [
      { required: true, message: '请输入联系电话', trigger: 'blur' },
      { pattern: /^\d{11}$/, message: '联系电话必须是11位数字', trigger: 'blur' }
    ]
  },
  tableData: [],
  pageNum: 1,
  pageSize: 10,
  total: 0,
  name: null,
  ids: [],
  departmentList: [],
  titleList: []
});

request.get('/department/selectAll').then(res => {
  data.departmentList = res.data;
});

request.get('/title/selectAll').then(res => {
  data.titleList = res.data;
});

const load = () => {
  request.get('/doctor/selectPage', {
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

const delBatch = () => {
  if (!data.ids.length) {
    ElMessage.warning("请选择要删除的数据")
    return
  }
  ElMessageBox.confirm('此操作不可逆！', '删除科室', { type: 'warning' }).then(res => {
    request.delete("/doctor/delete/batch", {data: data.ids}).then(res => {
      if (res.code === '200') {
        ElMessage.success('删除已成功')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {
    console.error(err)
  })
}

const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row));
  data.formVisible = true;
};

const add = () => {
  request.post('/doctor/add', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('已成功新增医生');
      data.formVisible = false;
      load();
    } else {
      ElMessage.error(res.msg);
    }
  });
};

const update = () => {
  request.put('/doctor/update', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('更新已成功');
      data.formVisible = false;
      load();
    } else {
      ElMessage.error(res.msg);
    }
  });
};

const save = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      if (data.form.id) {
        update();
      } else {
        add();
      }
    }
  });
};

const del = (id) => {
  ElMessageBox.confirm('此操作不可逆！', '删除医生信息', { type: 'warning' }).then(res => {
    request.delete('/doctor/delete/' + id).then(res => {
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

const handleSelectionChange = (rows) => {
  data.ids = rows.map(v => v.id);
};

const handleFileUpload = (res) => {
  data.form.avatar = res.data;
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