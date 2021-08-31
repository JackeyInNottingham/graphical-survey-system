<template>
  <div>
    <el-button @click="showDialog">
      <el-icon class="el-icon-edit"></el-icon>
      change password
    </el-button>


    <el-dialog title="Change Password" :visible.sync="changePasswordVisible" width="600px"
               @close="resetPasswordChangeForm" center>
      <el-form :model="passwordChangeForm" ref="passwordChangeForm" :rules="passwordChangeRules" label-width="170px"
               style="width: 95%;">
        <el-row type="flex" justify="center">
          <el-col>
            <el-form-item label="Original Password" prop="oldPassword">
              <el-input v-model="passwordChangeForm.oldPassword" clearable :show-password="true"
                        placeholder="please input original password"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col>
            <el-form-item label="New Password" prop="newPassword">
              <el-input v-model="passwordChangeForm.newPassword" clearable :show-password="true"
                        placeholder="please input new password"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col>
            <el-form-item label="Repeat New Password" prop="checkNewPassword">
              <el-input v-model="passwordChangeForm.checkNewPassword" clearable :show-password="true"
                        placeholder="please input new password again"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row type="flex" justify="center">
          <el-button type="primary" @click="submitPasswordChangeForm('passwordChangeForm')">submit</el-button>
          <el-button @click="resetPasswordChangeForm">cancel</el-button>
        </el-row>
      </el-form>
    </el-dialog>

  </div>
</template>

<script>
export default {
  name: "password",

  data() {
    let checkPassword = (rule, value, callback) => {
      if (value !== this.passwordChangeForm.newPassword) {
        callback(new Error('the two passwords do not match'));
      } else {
        callback();
      }
    };

    return {
      headers: {
        token: this.$store.state.token
      },

      changePasswordVisible: false,
      passwordChangeForm: {
        oldPassword: "",
        newPassword: "",
        checkNewPassword: "",
      },
      passwordChangeRules: {
        oldPassword: [
          {required: true, message: "please input original password", trigger: "blur"},
          {max: 20, message: "max length is 20", trigger: "blur"}
        ],
        newPassword: [
          {required: true, message: "please input new password", trigger: "blur"},
          {max: 20, message: "max length is 20", trigger: "blur"}
        ],
        checkNewPassword: [
          {required: true, message: "please input new password again", trigger: "blur"},
          {max: 20, message: "max length is 20", trigger: "blur"},
          {validator: checkPassword, trigger: "blur"}
        ]
      },
    }
  },

  methods: {

    showDialog() {
      this.changePasswordVisible = true;
    },

    //password change function
    changePassword() {
      this.changePasswordVisible = true;
    },
    resetPasswordChangeForm() {
      this.changePasswordVisible = false;
      this.$refs["passwordChangeForm"].clearValidate();
      this.passwordChangeForm = {
        oldPassword: "",
        newPassword: "",
        checkNewPassword: "",
      };
    },
    submitPasswordChangeForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$axios.post("/user/changePassword", {
            oldPassword: this.passwordChangeForm.oldPassword,
            newPassword: this.passwordChangeForm.newPassword
          }).then(data => {
            if (data.data.success) {
              this.$message.success(data.data.msg);
              this.changePasswordVisible = false;
              this.logout();
            } else {
              this.$message.error(data.data.msg);
            }
          }).catch(error => {
            this.$message.error(error);
          })
        } else {
          return false;
        }
      })
    },

    logout() {
      this.$store.commit('logout');
      this.$router.push({name: 'login'});
    },


  }
}
</script>

<style scoped>

</style>
