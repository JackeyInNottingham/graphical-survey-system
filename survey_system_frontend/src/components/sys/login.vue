<template>
  <div class="login-wrapper">
    <div class="login-content">
      <div class="login-main">
        <h1 class="login-title">login</h1>
        <el-form :model="loginVo" ref="loginForm" @keyup.enter.native="loginVoSubmit()" status-icon label-width="80px">
          <el-form-item prop="username" label="username" required>
            <el-input v-model="loginVo.username" placeholder="username" clearable></el-input>
          </el-form-item>
          <el-form-item prop="password" label="password" required>
            <el-input v-model="loginVo.password" type="password" placeholder="password" clearable></el-input>
          </el-form-item>
          <el-form-item label="captcha" prop="captcha" required>
            <el-input type="text" v-model="loginVo.captcha" placeholder="- - - -" clearable>
              <template slot="append">
                <el-image :src="src" @click="refreshCode" style="width: 80px; height: 33px"></el-image>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item label-width="0px">
            <el-button type="primary" @click="loginVoSubmit()">login</el-button>
          </el-form-item>
        </el-form>
        <el-row>
          <el-col :span="1">
            <el-link @click="backHome()" type="primary">home</el-link>
          </el-col>
          <el-col :span="1" :offset="20">
            <el-link @click="registerAccount()" type="primary">register</el-link>
          </el-col>
        </el-row>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  name: "login",
  data() {
    return {
      src: "",
      loginVo: {
        username: '',
        password: '',
        captcha: ''
      },
    }
  },
  methods: {
    // login function
    loginVoSubmit() {
      this.$refs['loginForm'].validate((valid) => {
        if (valid) {
          this.$axios.post("/sys/login", this.loginVo, {
            params: {
              captchaId: sessionStorage.getItem("captchaId")
            }
          }).then(data => {
            if (data.data.success) {
              sessionStorage.removeItem("captchaId");
              this.$store.dispatch('invokeSetToken', data.data.token);
              this.$store.dispatch('invokeLogin');
              this.$store.dispatch('invokeSetRole', data.data.role);
              this.$store.dispatch('invokeSetUsername', data.data.username);
              this.$router.push({name: 'dashboard'});
            } else {
              this.refreshCode();
              this.$message.error(data.data.code + ":" + data.data.msg);
            }
          }).catch(err => {
            this.refreshCode();
            console.log(err);
          })
        } else {
          return false;
        }
      })
    },

    // get captcha image function
    refreshCode() {
      this.$axios.get("/sys/getCaptchaImage").then(data => {
        if (data.data.success) {
          this.src = "data:image/png;base64," + data.data.captcha;
          sessionStorage.setItem("captchaId", data.data.captchaId);
        } else {
          this.$message.error(data.data.code + ":" + data.data.msg);
        }
      }).catch(err => {
        console.log(err);
      })
    },

    backHome(){
      sessionStorage.removeItem("captchaId");
      this.$router.push({name: 'home'});
    },

    // click to register function
    registerAccount() {
      sessionStorage.removeItem("captchaId");
      this.$router.push("register");
    }
  },
  created() {
    this.refreshCode();
  }
}
</script>
<style>
.login-wrapper {
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  overflow: hidden;
  background-size: 100% 100%;
}

.login-content {
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  margin: auto;
  height: 390px;
  width: 400px;
  background-color: #F0F3F4;
  opacity: .8;
}

.login-main {
  text-align: center;
  color: black;
  padding: 20px 20px 10px 20px;
}
</style>
