<template>
  <div class="dashboard-container">
    <!-- 搜索表单 -->
    <el-form :model="tableData" label-width="80px" :inline="true" size="small">
      <el-form-item label="搜索内容">
        <el-input
          v-model="searchContent"
          placeholder="请输入搜索内容"
          clearable=""
        >
        </el-input>
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="getRoleList"
          >搜索</el-button
        >
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleCreateRole"
          >新增</el-button
        >
      </el-form-item>
    </el-form>

    <!-- 角色列表 -->
    <el-table
      :data="tableData.list"
      @sort-change="handleSortChange"
    >
      <el-table-column type="index" width="60" />
      <el-table-column prop="name" label="角色名称" sortable="custom" />
      <el-table-column prop="description" label="角色描述" sortable="custom" />
      <el-table-column prop="createTime" label="创建时间" sortable="custom" />
      <el-table-column prop="updateTime" label="更新时间" sortable="custom" />
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button
            type="text"
            size="small"
            icon="el-icon-edit"
            @click="handleEdit(scope.row)"
            >编辑</el-button
          >
          <el-button
            type="text"
            size="small"
            icon="el-icon-delete"
            style="color: red"
            @click="handleDelete([scope.row.id])"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页功能------在后端实现 -->
    <el-pagination
      class="pagination"
      @size-change="startPagination"
      @current-change="startPagination"
      :current-page.sync="tableData.pageNum"
      :page-sizes="[10, 20, 30, 40]"
      :page-size.sync="tableData.pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="tableData.total"
    >
    </el-pagination>

    <!-- 角色编辑/创建窗口 -->
    <el-dialog
      :title="roleEditForm.id ? '新增角色' : '角色编辑'"
      :visible.sync="roleEditDialogVisible"
      width="50%"
    >
      <el-form
        ref="roleEditForm"
        :model="roleEditForm"
        label-width="80px"
        :rules="roleEditFormRules"
      >
        <el-form-item label="角色名称" prop="name">
          <el-input v-model="roleEditForm.name" />
        </el-form-item>
        <el-form-item label="角色描述" prop="description">
          <el-input v-model="roleEditForm.description" />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="roleEditDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="addOrUpdateRole">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "Role",
  data() {
    return {
      tableData: {
        searchContent: "",
        list: [
          {
            id: 1,
            name:'普通用户',
            description: "描述",
            createTime: "",
            updateTime: "",
          },
        ],
        currentPage: 1,
        pageSize: 10,
        total: 1,
      },
      roleEditForm:{
        id:'',
        name:'',
        description:''
      },
      roleEditFormRules:{
        name:[
          {required:true,trigger:'blur',validator:this.validateName}
        ]
      },
      roleEditDialogVisible:false,
    };
  },
  methods:{
    // 新增角色
    handleCreateRole(){
      for(const key in this.roleEditForm){
        this.roleEditForm[key]='';
      }
      this.openRoleEditDialog();
    },
    // 打开角色编辑窗口
    openRoleEditDialog(){
      this.roleEditDialogVisible=true;
      this.$nextTick(()=>{
        this.$refs.roleEditForm.clearValidate()
      })
    },
    // 删除角色
    handleDelete(id){
      this.$confirm('此操作将永久删除该角色，是否继续？','提示',{
        confirmButtonText:'确定',
        concelButtonText:'取消',
        type:'warning'
      }).then(()=>{
        //对接删除角色接口

        // RoleApi.deleteRoles([id]).then(()=>{
        //   this.$message.success('删除成功')
        //   this.getRoleList()
        // })
      }).catch(()=>{
        this.$message.info('已取消删除')
      })
    },
    // 编辑角色
    handleEdit(row){
      for(const key in this.roleEditForm){
        this.roleEditForm[key]=row[key]
      }
      this.openRoleEditDialog()
    }
  }
};
</script>