<template>

  <div>
    <el-container>
      <el-header style="font-size: 12px" height="40px">

        <el-row :gutter="10" type="flex" align="middle">
          <el-col :span="3.5">
            <el-input
              placeholder="please input username"
              v-model="username"
              size="small"
              clearable
              @change="changePage"
            >
            </el-input>
          </el-col>

          <el-col :span="3">
            <el-select v-model="status" clearable placeholder="please select status" size="small"
                       @change="changePage">
              <el-option
                v-for="state in states"
                :key="state.value"
                :label="state.label"
                :value="state.value"
              >
              </el-option>
            </el-select>
          </el-col>

          <el-col :span="3">
            <el-select v-model="role" clearable placeholder="please select role" size="small"
                       @change="changePage">
              <el-option
                v-for="role in roles"
                :key="role.id"
                :label="role.name"
                :value="role.id">
              </el-option>
            </el-select>
          </el-col>

          <el-col :span="2">
            <el-button
              icon="el-icon-search"
              size="small"
              @click="changePage"
              type="primary"
            >
              search
            </el-button>
          </el-col>

          <el-col :span="3" :offset="9">
            <el-button
              type="primary"
              icon="el-icon-circle-plus"
              size="small"
              @click="viewAddNewUserDialog"
            >New User
            </el-button>
          </el-col>

        </el-row>

      </el-header>

      <el-main style="height: 725px; width: 830px; padding-bottom: 0">
        <!--        table container-->
        <el-table
          :data="userList"
          :stripe="true"
          :border="true"
        >
          <el-table-column prop="username" label="username" width="120" resizable>
          </el-table-column>
          <el-table-column prop="email" label="email" width="180" resizable>
          </el-table-column>
          <el-table-column prop="active" label="status" width="120" resizable>
            <template slot-scope="scope">
              <el-tag
                :type="scope.row.status? 'success': 'danger'"
                v-text="scope.row.status? 'active': 'banned'"
                disable-transitions>
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="role" label="role" width="120" resizable>
            <template slot-scope="scope">
              <el-tag
                :type="scope.row.role === 'admin' ? 'success' : 'primary'"
                v-text="scope.row.role"
                disable-transitions>
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="operation">
            <template slot-scope="scope">
              <el-button
                type="primary"
                icon="el-icon-edit-outline"
                @click="viewEditUserDialog(scope.row.id)"
                size="small"
              >
                edit
              </el-button>
              <el-button
                size="small"
                type="danger"
                icon="el-icon-delete"
                @click="deleteUser(scope.row.id)"
              >
                delete
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-main>

      <el-footer height="15px">
        <el-row type="flex" align="bottom" justify="center">
          <el-pagination
            background
            @current-change="changePage"
            :page-count="10"
            :current-page.sync='curPage'
            :page-size.sync='pageSize'
            layout="prev, pager, next"
            :total=this.totalNum>
          </el-pagination>
        </el-row>
      </el-footer>

    </el-container>

    <!--    create new user or edit user info dialog-->
    <el-dialog :title="dialogTitle" :visible.sync="dialogTableVisible" @closed="resetUserVo" width="500px">
      <el-form :model="userVo" :rules="rules" ref="userVo" label-width="100px" size="medium">

        <el-form-item label="username" prop="username">
          <el-input v-model="userVo.username" :disabled="this.dialogTitle === 'Edit User Info'"></el-input>
        </el-form-item>
        <el-form-item label="password" prop="password">
          <el-input v-model="userVo.password" type="password" :disabled="this.dialogTitle === 'Add New User'"></el-input>
        </el-form-item>
        <el-form-item label="email" prop="email">
          <el-input v-model="userVo.email"></el-input>
        </el-form-item>
        <el-form-item label="status" prop="status">
          <el-radio-group v-model="userVo.status">
            <el-radio :label="true">activate</el-radio>
            <el-radio :label="false">ban</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="role" prop="roleId">
          <el-radio-group v-model="userVo.roleId">
            <el-radio :label="1" :value="1">admin</el-radio>
            <el-radio :label="2" :value="2">researcher</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-row type="flex" justify="center">
          <el-button type="primary" @click="submitForm" size="small">submit</el-button>
          <el-button @click="closeDialog" type="warning" size="small">cancel</el-button>
        </el-row>
      </el-form>
    </el-dialog>

  </div>

</template>

<script>
export default {
  name: "user",

  data() {
    var checkEmail = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('email is required'));
      } else {
        var pattern = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
        if (pattern.test(this.userVo.email)) {
          callback();
        }
        callback('please input a correct email')
      }
    }

    return {

      dialogTitle: '',
      dialogTableVisible: false,
      rules: {
        username: [
          {required: true, message: "username is required", trigger: 'blur'},
          {max: 15, message: "no more than 15 characters", trigger: 'blur'}
        ],
        password: [
          {required: true, message: "password is required", trigger: 'blur'},
          {max: 20, message: "no more than 20 characters", trigger: 'blur'}
        ],
        email: [
          {required: true, message: "email is required", trigger: 'blur'},
          {max: 30, message: "no more than 30 characters", trigger: 'blur'},
          {validator: checkEmail, trigger: 'blur'}
        ],
      },

      userVo: {
        id: "",
        username: "",
        password: "123456",
        email: "",
        status: true,
        roleId: 2,
      },

      //filter component properties
      username: '',
      role: '',
      roles: [],
      status: "",
      states: [
        {
          value: "true",
          label: 'active',
        },
        {
          value: "false",
          label: 'banned',
        }
      ],


      //pagination component properties
      curPage: 1,
      pageSize: 10,

      totalNum: 1,
      userList: [],

    }
  },

  methods: {

    // create new user
    viewAddNewUserDialog() {
      this.dialogTitle = "Add New User";
      this.dialogTableVisible = true;
    },

    closeDialog() {
      this.dialogTableVisible = false;
      this.resetUserVo();
    },

    submitForm(){
      this.$refs["userVo"].validate((valid)=>{
        if(valid){
          if(this.dialogTitle === "Add New User"){
            this.addNewUser();
          }else if(this.dialogTitle === "Edit User Info"){
            this.updateUser();
          }else{
            this.$message.error("Bad Request");
          }
        }else{
          return false;
        }
      })
    },

    addNewUser(){
      this.$axios.post("/user/addUser", this.userVo).then(data=>{
        if (data.data.success){
          this.$message.success("New User Added");
          this.curPage = this.pageSize;
          this.getUserList();
          this.closeDialog();
        }else {
          this.$message.error(data.data.msg);
        }
      }).catch(error=>{
        this.$message.error(error);
      })
    },

    resetUserVo() {
      this.dialogTitle = "";
      this.$refs['userVo'].resetFields();
    },

    // get list function
    changePage() {
      if (this.username !== '' || this.role !== '' || this.status.length !== 0){
        this.filtrateUser();
      }else{
        this.getUserList();
      }
    },

    //filtrate user
    filtrateUser() {
      if (this.username !== '' || this.role !== '' || this.status.length !== 0){
        this.$axios.get('/user/filtrateUser', {
          params: {
            username: this.username,
            role: this.role,
            active: this.status,
            page: this.curPage,
            pageSize: this.pageSize
          }
        }).then(data=>{
            if (data.data.success) {
              this.userList = data.data.userList.list;
              this.totalNum = data.data.userList.total;
            }else{
              this.userList = [];
              this.$message.error(data.data.msg);
            }
        }).catch(err=>{
          this.$message.error(err);
        })
      }
    },

    //get full user list
    getUserList() {
      this.$axios.get("/user/getUserListByPage", {
        params: {
          page: this.curPage,
          pageSize: this.pageSize
        }
      }).then(data => {
        if (data.data.success) {
          this.userList = data.data.userList.list;
          this.totalNum = data.data.userList.total;
        } else {
          this.$message.error(data.data.msg);
          this.userList = [];
          this.totalNum = 0;
        }
      }).catch(err => {
        console.log(err);
        this.$message.error(err);
      })
    },


    getRoles(){
      this.$axios.get("/role/roleList").then(data=>{
        if(data.data.success){
          this.roles = data.data.roles;
        } else {
          this.$message.error(data.data.msg);
        }
      }).catch(err=>{
        this.$message.error(err);
      })
    },

    viewEditUserDialog(id){
      this.dialogTitle = "Edit User Info";
      this.$axios.get("/user/getUserInfo", {
        params: {
          id: id
        }
      }).then(data=>{
        if (data.data.success){
          this.userVo = data.data.userInfo;
        }else{
          this.$message.error(data.data.msg);
          console.log(data.data.msg);
        }
      }).catch(error=>{
        this.$message.error(error);
        console.log(error);

      })
      this.dialogTableVisible = true;
    },

    updateUser(){
      this.$axios.put("/user/updateUserInfo", this.userVo).then(data=>{
        if(data.data.success){
          this.$message.success(data.data.msg);
          this.getUserList();
          this.closeDialog();
        } else {
          this.$message.error(data.data.msg);
        }
      }).catch(error=>{
        this.$message.error(error);
      })
    },


    deleteUser(id){
      this.$confirm('Are you sure to delete this user', '', {
        confirmButtonText: 'confirm',
        cancelButtonText: 'cancel',
        type: 'warning',
        center: true
      }).then(() => {
        this.$axios.delete("/user/deleteUserById", {
          params: {
            id: id
          }
        }).then(data=>{
          if (data.data.success){
            this.$message.success(data.data.msg);
            this.getUserList();
          }else{
            this.$message.error(data.data.msg);
          }
        }).catch(error=>{
          this.$message.error(error);
        })
      }).catch();
    },

  },

  created() {
    this.getRoles();
    this.getUserList();
  }
}
</script>

<style scoped>
</style>
