<template>
    <div class="dashboard-container">
        <!-- 搜索表单 -->
      <el-form :model="tableData" label-width="80px" :inline="true" size="small">
        <el-form-item label="用户名称">
            <el-input 
                v-model="tableData.name" 
                placeholder="请输入用户名称"
                clearable="">
            </el-input>
        </el-form-item>
        <el-form-item label="创建时间">
            <el-date-picker
                v-model="tableData.minCreateTime"
                type="datetime"
                placeholder="起始时间"
                class="date-picker">
            </el-date-picker>
            <el-date-picker
                v-model="tableData.maxCreateTime"
                type="datetime"
                placeholder="截止时间"
                class="date-picker">
            </el-date-picker>
        </el-form-item>
        <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="getUserList">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 用户操作按钮 -->
       <div>
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleCreateUser">新增</el-button>
        <el-button type="danger" plain icon="el-icon-delete" size="mini" @click="handleBatchDelete">删除</el-button>
        <el-button type="info" plain icon="el-icon-upload2" size="mini" @click="handleImportUser">导入</el-button>
       </div>

       <!-- 用户列表 -->
        <el-table
            :data="tableData.list"
            @selection-change="val=>tableData.selection=val"
            @sort-change="handleSortChange">
            <el-table-column type="index" width="60" />
            <el-table-column type="selection" width="50" />
            <el-table-column width="50">
                <!-- 头像列 -->
                <template slot-scope="scope">
                    <img :id="'avatar-' +scope.row.id" class="table-avatar">
                    <!-- <img class="table-avater" :src="scope.row.avatar"> -->
                     <!-- 直接使用:src绑定会导致刷新失败 -->
                </template>
            </el-table-column>
            <el-table-column prop="userName" label="用户名" sortable="custom" />
            <el-table-column prop="trueName" label="真实姓名" sortable="custom" />
            <el-table-column prop="roleList" label="角色" sortable="custom" />
            <el-table-column prop="createTime" label="创建时间" sortable="custom" />
            <el-table-column prop="status" label="是否激活" sortable="custom" width="100">
                <template slot-scope="scope">
                    <el-switch v-model="scope.row.status" :active-value="1" :inactive-value="0" @change="handleSwitch(scope.row)"/>
                </template>
            </el-table-column>
            <el-table-column label="操作">
                <template slot-scope="scope">
                    <el-button type="text" size="small" icon="el-icon-edit" @click="handleEdit(scope.row)">编辑</el-button>
                    <el-button type="text" size="small" icon="el-icon-delete" style="color: red;" @click="handleDelete([scope.row.id])">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <!-- 分页功能------在后端实现 -->
        <el-pagination
            class="pagination"
            @size-change="getUserList"
            @current-change="getUserList"
            :current-page.sync="tableData.pageNum"
            :page-sizes="[10, 20, 30, 40]"
            :page-size.sync="tableData.pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="tableData.total">
        </el-pagination>

        <!-- 用户编辑/创建窗口 -->
        <el-dialog class="user-edit-dialog" :title="userEditForm.id?'用户编辑':'新增用户'" :visible.sync="userEditDialogVisible" width="50%" top="8vh">
            <el-form
                ref="userEditForm"
                status-icon
                :model="userEditForm"
                label-width="80px"
                :rules="userEditForm.id?userUpdateRules:userCreateRules">
                <el-form-item label="用户名" prop="userName">
                    <el-input v-model="userEditForm.userName"/>
                </el-form-item>
                <el-form-item label="真实姓名">
                    <el-input v-model="userEditForm.trueName"/>
                </el-form-item>
                <el-form-item label="密码" prop="password">
                    <el-input v-model="userEditForm.password"/>
                </el-form-item>
                <el-form-item label="邮箱" prop="email">
                    <el-input v-model="userEditForm.email"/>
                </el-form-item>
                <el-form-item label="性别">
                    <el-radio-group v-model="userEditForm.gender">
                        <el-radio :label="0">男</el-radio>
                        <el-radio :label="1">女</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="地址">
                    <el-input v-model="userEditForm.address"/>
                </el-form-item>
                <el-form-item label="简介">
                    <el-input v-model="userEditForm.introduction"/>
                </el-form-item>
                <el-form-item label="电话">
                    <el-input v-model="userEditForm.phone"/>
                </el-form-item>
                <el-form-item label="角色" prop="roleIds">
                    <el-select v-model="userEditForm.roleIds" multiple placeholder="请选择角色">
                        <el-option v-for="role in allRoles" :key="role.id" :label="role.name" :value="role.id"/>
                    </el-select>
                </el-form-item>
                <el-form-item label="头像">
                    <!-- <el-upload
                        class="avatar-uploader"
                        action=""
                        :auto-upload="false"
                        :show-file-list="false"
                        :on-change="file=>handleAvatarChange(file)"> -->
                        <!-- <img v-if="avatarUploadData.url" :src="avatarUploadData.url" class="avatar">
                        <i v-else class="el-icon-plus avatar-uploader-icon"></i> -->
                    <!-- </el-upload> -->
                    <el-upload
                        class="avatar-uploader"
                        action="https://jsonplaceholder.typicode.com/posts/"
                        :show-file-list="false"
                        :on-success="handleAvatarSuccess"
                        :before-upload="beforeAvatarUpload">
                        <img v-if="imageUrl" :src="imageUrl" class="avatar">
                        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                    </el-upload>

                    <!-- <el-button v-if="avatarUploadData.raw" size="mini" @click="resetUploadData(false)">重置</el-button> -->
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="userEditDialogVisible=false">取消</el-button>
                <el-button type="primary" @click="addOrUpdateUser">确定</el-button>
            </span>
        </el-dialog>
    </div>
  </template>
  
  <script>
//   import * as UserApi from '@/api/user'
  export default {
    name: 'User',
    data() {
      return {
        tableData:{
            name:'',
            minCreateTime:'',
            maxCreateTime:'',
            list:[{
                id:1,
                userName:'张三',
                trueName:'张三',
                roleList:[],
                status:true,
            },{
                id:2,
                userName:'李四',
                trueName:'李四',
                roleList:[],
                status:true,
            },{
                id:3,
                userName:'王二',
                trueName:'王二',
                roleList:[],
                status:true,
            },
            ],
            selection:'',
            pageNum:1,
            pageSize:10,
            total:1,
        },
        userEditForm:{
            id:'',
            userName:'',
            trueName:'',
            password:'',
            email:'',
            gender:'',
            address:'',
            introduction:'',
            phone:'',
            roleIds:[],
        },
        userEditDialogVisible:false,
        allRoles:[],
        userCreateRules:{
            userName:[{required:true,trigger:'blur',validator:this.userNameValidator}],
            password:[{required:true,trigger:'change',validator:this.passwordValidator}],
            roleIds:[{required:true,trigger:'change',validator:this.roleValidator}],
        },
        userUpdateRules:{
            userName:[{required:true,trigger:'blur',validator:this.userNameValidator}],
            password:[{trigger:'change',validator:this.passwordValidator}],
            roleId:[{required:true,trigger:'change',validator:this.roleValidator}],
        },
        currentEditRow:null // 当前编辑行   
      }
    },
    mounted(){
        this.getUserList()
    },
    methods:{
        // 用户名验证
        userNameValidator(rule,value,callback){
            if(!value){
                callback(new Error('请输入用户名'))
            }else if(this.userEditForm.id && value===this.currentEditRow.userName){
                callback()
            }else{
                // 使用接口判断用户名是否重名 
                // checkUserName(value).then(res=>{
                //     callback(res.data.data?new Error('用户名已存在'):undefined)
                // })
            }
        },
        // 密码验证
        passwordValidator(rule,value,callback){
            if(!value && this.userEditForm.id){
                callback()
            }else if(!value || value.length < 6){
                callback(new Error('密码长度不能小于6位'))
            }else{
                callback()
            }
        },
        // 角色验证
        roleValidator(rule,value,callback){
            if(!value || value.length===0){
                callback(new Error('角色不能为空'))
            }else{
                callback()
            }
        },
        handleAvatarSuccess(res, file) {
            this.imageUrl = URL.createObjectURL(file.raw);
        },
        beforeAvatarUpload(file) {
            const isJPG = file.type === 'image/jpeg';
            const isLt2M = file.size / 1024 / 1024 < 2;

            if (!isJPG) {
            this.$message.error('上传头像图片只能是 JPG 格式!');
            }
            if (!isLt2M) {
            this.$message.error('上传头像图片大小不能超过 2MB!');
            }
            return isJPG && isLt2M;
        },
        // 获取用户列表
        getUserList(){
            // 调用getUsers接口
            // UserApi.getUsers(this.tableData).then(res=>{
            //     this.tableData.list=res.data.data.content
            //     this.tableData.total=res.data.data.totalElements
            //     // 更新头像
            //     // this.$nextTick(()=>{
            //     //     this.tableData.list.forEach(row=>{
            //     //         this.getAvatar(row.id,row)
            //     //     })
            //     // })
            // })
        },
        handleCreateUser(){
            this.userEditDialogVisible=true;
        },
        // 批量删除
        handleBatchDelete(){
            // 将tableData.selection中的id提取出来，传递给handleDelete
            console.log(this.tableData.selection);
            for(let i in this.tableData.selection){
                console.log(this.tableData.selection[i].id);
                this.handleDelete(this.tableData.selection[i].id)
            }
        },
        handleImportUser(){

        },
        //切换用户激活状态
        handleSwitch(){

        },
        // 编辑用户信息
        handleEdit(row){
            for(const key in this.userEditForm){
                this.userEditForm[key]=row[key];
            }
            this.userEditDialogVisible=true;
        },
        // 删除用户
        handleDelete(row){
            console.log(row);
            
            this.$confirm('此操作将永久删除该用户，是否继续？','提示',{
                confirmButtonText:'确定',
                concelButtonText:'取消',
                typer:'warning'
            }).then(()=>{
                // LoadingUtils.createFullScreenLoading('正在删除...')
                // UserApi.deleteUsers(userIds).then(()=>{
                //     this.$message.success('删除成功')
                //     this.getUserList()
                // }).finally(()=>{
                //     LoadingUtils.closeFullScreenLoading()
                // })

                //对接删除用户接口
            })
        },
        resetQuery(){

        },
        handleSortChange(){

        },
        handleAvatarChange(file){

        },
        // 添加或更新用户
        addOrUpdateUser(){
            this.$refs.userEditForm.validate(valid=>{
                if(valid){
                    // 如果验证用过就调用添加或者更新用户的接口
                }
            })
        }
    }
  }
  </script>
  
  <style scoped>
  .date-picker{
    margin-right: 10px;
    width:160px;
  }
  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }
  .avatar {
    width: 178px;
    height: 178px;
    display: block;
  }
  </style>
  