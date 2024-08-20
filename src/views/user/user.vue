<template>
  <div class="dashboard-container">
    <!-- 搜索表单 -->
    <el-form :model="tableData" label-width="80px" :inline="true" size="small">
      <el-form-item label="用户名称">
        <el-input
          v-model="tableData.name"
          placeholder="请输入用户名称"
          clearable=""
        >
        </el-input>
      </el-form-item>
      <el-form-item label="创建时间">
        <el-date-picker
          v-model="tableData.minCreateTime"
          type="datetime"
          placeholder="起始时间"
          class="date-picker"
        >
        </el-date-picker>
        <el-date-picker
          v-model="tableData.maxCreateTime"
          type="datetime"
          placeholder="截止时间"
          class="date-picker"
        >
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="getUserList"
          >搜索</el-button
        >
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
          >重置</el-button
        >
      </el-form-item>
    </el-form>

    <!-- 用户操作按钮 -->
    <div>
      <el-button
        type="primary"
        plain
        icon="el-icon-plus"
        size="mini"
        @click="handleCreateUser"
        >新增</el-button
      >
      <el-button
        type="danger"
        plain
        icon="el-icon-delete"
        size="mini"
        @click="handleBatchDelete"
        >删除</el-button
      >
      <el-button
        type="info"
        plain
        icon="el-icon-upload2"
        size="mini"
        @click="handleImportUser"
        >导入</el-button
      >
    </div>

    <!-- 用户列表 -->
    <el-table
      :data="tableData.list"
      @selection-change="(val) => (tableData.selection = val)"
      @sort-change="handleSortChange"
    >
      <el-table-column type="index" width="60" />
      <el-table-column type="selection" width="50" />
      <el-table-column width="50">
        <!-- 头像列 -->
        <template slot-scope="scope">
          <img :id="'avatar-' + scope.row.id" class="table-avatar" />
          <!-- <img class="table-avater" :src="scope.row.avatar"> -->
          <!-- 直接使用:src绑定会导致刷新失败 -->
        </template>
      </el-table-column>
      <el-table-column prop="userName" label="用户名" sortable="custom" />
      <el-table-column prop="trueName" label="真实姓名" sortable="custom" />
      <!-- <el-table-column prop="roleList" label="角色" sortable="custom" /> -->
      <el-table-column prop="createTime" label="创建时间" sortable="custom" />
      <el-table-column
        prop="status"
        label="是否激活"
        sortable="custom"
        width="100"
      >
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.status"
            :active-value="1"
            :inactive-value="0"
            @change="handleSwitch(scope.row)"
          />
        </template>
      </el-table-column>
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
      @size-change="getUserList"
      @current-change="getUserList"
      :current-page.sync="tableData.pageNum"
      :page-sizes="[10, 20, 30, 40]"
      :page-size.sync="tableData.pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="tableData.total"
    >
    </el-pagination>

    <!-- 用户编辑/创建窗口 -->
    <el-dialog
      class="user-edit-dialog"
      :title="userEditForm.id ? '用户编辑' : '新增用户'"
      :visible.sync="userEditDialogVisible"
      width="50%"
      top="8vh"
    >
      <el-form
        ref="userEditForm"
        status-icon
        :model="userEditForm"
        label-width="80px"
        :rules="userEditForm.id ? userUpdateRules : userCreateRules"
      >
        <el-form-item label="用户名" prop="userName">
          <el-input v-model="userEditForm.userName" />
        </el-form-item>
        <el-form-item label="真实姓名">
          <el-input v-model="userEditForm.trueName" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="userEditForm.password" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="userEditForm.email" />
        </el-form-item>
        <el-form-item label="性别">
          <el-radio-group v-model="userEditForm.gender">
            <el-radio :label="0">男</el-radio>
            <el-radio :label="1">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="userEditForm.address" />
        </el-form-item>
        <el-form-item label="简介">
          <el-input v-model="userEditForm.introduction" />
        </el-form-item>
        <el-form-item label="电话">
          <el-input v-model="userEditForm.phone" />
        </el-form-item>
        <!-- <el-form-item label="角色" prop="roleIds">
          <el-select
            v-model="userEditForm.roleIds"
            multiple
            placeholder="请选择角色"
          >
            <el-option
              v-for="role in allRoles"
              :key="role.id"
              :label="role.name"
              :value="role.id"
            />
          </el-select>
        </el-form-item> -->
        <!-- <el-form-item label="头像"> -->
          <!-- <el-upload
                        class="avatar-uploader"
                        action=""
                        :auto-upload="false"
                        :show-file-list="false"
                        :on-change="file=>handleAvatarChange(file)"> -->
          <!-- <img v-if="avatarUploadData.url" :src="avatarUploadData.url" class="avatar">
                        <i v-else class="el-icon-plus avatar-uploader-icon"></i> -->
          <!-- </el-upload> -->
          <!-- <el-upload
            class="avatar-uploader"
            action="https://jsonplaceholder.typicode.com/posts/"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload"
          >
            <img v-if="avatarurl" :src="avatarurl" class="avatar" />
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload> -->

          <!-- <el-button v-if="avatarUploadData.raw" size="mini" @click="resetUploadData(false)">重置</el-button> -->
        <!-- </el-form-item> -->
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="userEditDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="addOrUpdateUser">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>
  
  <script>
import * as UserApi from "@/api/user";
export default {
  name: "User",
  data() {
    return {
      tableData: {
        name: "",
        minCreateTime: "",
        maxCreateTime: "",
        list: [],
        selection: "",
        pageNum: 1,
        pageSize: 10,
        total: 1,
      },
      userEditForm: {
        id: "",
        userName: "",
        trueName: "",
        password: "",
        email: "",
        gender: "",
        address: "",
        introduction: "",
        phone: "",
        roleIds: [],
        avatarurl: "",
        createTime: "",
      },
      userEditDialogVisible: false,
      allRoles: [
        { id: 0, name: "editor" },
        { id: 1, name: "admin" },
      ],
      userCreateRules: {
        userName: [
          {
            required: true,
            trigger: "blur",
            validator: this.userNameValidator,
          },
        ],
        password: [
          {
            required: true,
            trigger: "change",
            validator: this.passwordValidator,
          },
        ],
        roleIds: [
          { required: true, trigger: "change", validator: this.roleValidator },
        ],
      },
      userUpdateRules: {
        userName: [
          {
            required: true,
            trigger: "blur",
            validator: this.userNameValidator,
          },
        ],
        password: [{ trigger: "change", validator: this.passwordValidator }],
        roleId: [
          { required: true, trigger: "change", validator: this.roleValidator },
        ],
      },
      currentEditRow: null, // 当前编辑行
    };
  },
  mounted() {
    this.getUserList();
  },
  methods: {
    // 用户名验证
    // userNameValidator(value, callback) {
    //     console.log('用户名验证');

    //   if (!value) {
    //     callback(new Error("请输入用户名"));
    //   } else if (
    //     this.userEditForm.id &&
    //     value === this.currentEditRow.userName
    //   ) {
    //     callback();
    //   } else {
    //     // 使用接口判断用户名是否重名
    //     // checkUserName(value).then(res=>{
    //     //     callback(res.data.data?new Error('用户名已存在'):undefined)
    //     // })
    //   }
    // },
    // 密码验证
    // passwordValidator(value, callback) {
    //     console.log('密码验证');

    //   if (!value && this.userEditForm.id) {
    //     callback();
    //   } else if (!value || value.length < 6) {
    //     callback(new Error("密码长度不能小于6位"));
    //   } else {
    //     callback();
    //   }
    // },
    // 角色验证
    // roleValidator(value, callback) {
    //     console.log('角色验证');

    //   if (!value || value.length === 0) {
    //     callback(new Error("角色不能为空"));
    //   } else {
    //     callback();
    //   }
    // },
    handleAvatarSuccess(res, file) {
      this.avatarurl = URL.createObjectURL(file.raw);
    },
    beforeAvatarUpload(file) {
      const isJPG = file.type === "image/jpeg";
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isJPG) {
        this.$message.error("上传头像图片只能是 JPG 格式!");
      }
      if (!isLt2M) {
        this.$message.error("上传头像图片大小不能超过 2MB!");
      }
      return isJPG && isLt2M;
    },
    // 获取用户列表
    getUserList() {
        function formatLocalDate(date) {
            if(date==null||date.length==0){
                return ''
            }
          return (
            date.getFullYear() +
            "-" +
            (date.getMonth() + 1).toString().padStart(2, "0") +
            "-" +
            date.getDate().toString().padStart(2, "0")
          );
        }
        let searchdata={
            name:this.tableData.name,
            minCreateTime:formatLocalDate(this.tableData.minCreateTime),
            maxCreateTime:formatLocalDate(this.tableData.maxCreateTime)
        }
        console.log(searchdata);
        
      // 调用getUsers接口
      UserApi.getUsers(searchdata).then((res) => {
        for (let j = 0; j < res.data.length; j++) {
          let arr = [];
          let ro = res.data[j].roleIds;
          for (let i = 0; i < ro.length; i++) {
            if (ro[i] == "1") {
              arr.push("admin");
            } else {
              arr.push("editor");
            }
          }
          res.data[j].roleIds = arr;
        }
        console.log(res);
        this.tableData.list = res.data;
        this.tableData.total = res.total;
        // 更新头像
        // this.$nextTick(()=>{
        //     this.tableData.list.forEach(row=>{
        //         this.getAvatar(row.id,row)
        //     })
        // })
      });
    },
    handleCreateUser() {
      this.userEditDialogVisible = true;
    },
    // 批量删除
    handleBatchDelete() {
      console.log(this.tableData.selection);
      // 将tableData.selection中的id提取出来，传递给handleDelete
      this.$confirm("此操作将永久删除这些用户，是否继续？", "提示", {
        confirmButtonText: "确定",
        concelButtonText: "取消",
        typer: "warning",
      }).then(() => {
        console.log(this.tableData.selection);

        for (let i = 0; i < this.tableData.selection.length; i++) {
          console.log(i);
          console.log(this.tableData.selection[i].id);

          UserApi.deleteUser(this.tableData.selection[i].id).then(() => {
            this.getUserList();
          });
        }
        this.$message.success("删除成功");
        console.log("删除完成");

        // 有bug
      });
    },
    handleImportUser() {},
    //切换用户激活状态
    handleSwitch() {},
    // 编辑用户信息
    handleEdit(row) {
      for (const key in this.userEditForm) {
        console.log(key);
        console.log(row[key]);
        console.log(this.userEditForm[key]);
        
        this.userEditForm[key] = row[key];
      }
      this.userEditDialogVisible = true;
    },
    // 删除用户
    handleDelete(row) {
      this.$confirm("此操作将永久删除该用户，是否继续？", "提示", {
        confirmButtonText: "确定",
        concelButtonText: "取消",
        typer: "warning",
      }).then(() => {
        console.log(row[0]);
        UserApi.deleteUser(row[0]).then(() => {
          this.$message.success("删除成功");
          this.getUserList();
        });
      });
    },
    resetQuery() {},
    handleSortChange() {},
    handleAvatarChange(file) {},
    // 添加或更新用户
    addOrUpdateUser() {
      if (this.userEditForm.id.length == 0) {
        var currentDate = new Date();
        function formatLocalDate(date) {
          return (
            date.getFullYear() +
            "-" +
            (date.getMonth() + 1).toString().padStart(2, "0") +
            "-" +
            date.getDate().toString().padStart(2, "0")
          );
        }
        this.userEditForm.createTime = formatLocalDate(currentDate);
        if (this.userEditForm.avatarurl.length == 0) {
          this.userEditForm.avatarurl =
            "https://img.picui.cn/free/2024/08/20/66c44b37d4284.jpg";
        }
        let ro = "";
        let ros = this.userEditForm.roleIds;
        for (let i = 0; i < ros.length; i++) {
          ro += ros[i];
        }
        this.userEditForm.roleIds = ro;
        console.log(this.userEditForm);

        UserApi.addUser(this.userEditForm).then((res) => {
          this.userEditDialogVisible = false;
          this.userEditForm = {
            id: "",
            userName: "",
            trueName: "",
            password: "",
            email: "",
            gender: "",
            address: "",
            introduction: "",
            phone: "",
            roleIds: [],
            avatarurl: "",
            createTime: "",
          };
          this.getUserList();
        });
      } else {
        console.log(this.userEditForm.roleIds);
        
        console.log("更新用户");
        let ro = "";
        let ros = this.userEditForm.roleIds;
        for (let i = 0; i < ros.length; i++) {
          ro += ros[i];
        }
        this.userEditForm.roleIds = ro;
        console.log(this.userEditForm);
        UserApi.updateUser(this.userEditForm).then((res) => {
          this.userEditDialogVisible = false;
          this.userEditForm = {
            id: "",
            userName: "",
            trueName: "",
            password: "",
            email: "",
            gender: "",
            address: "",
            introduction: "",
            phone: "",
            roleIds: [],
            avatarurl: "",
            createTime: "",
          };
          this.getUserList();
        });
      }
    },
  },
};
</script>
  
  <style scoped>
.date-picker {
  margin-right: 10px;
  width: 160px;
}
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409eff;
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
  