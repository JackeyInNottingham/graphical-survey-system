<template>
  <div class="register-wrapper">
    <div class="register-content">
      <div class="register-main">
        <h1 class="register-title">register</h1>
        <el-form :model="registerVo" :rules="registerRule" ref="registerForm" @keyup.enter.native="registerVoSubmit()"
                 status-icon
                 label-width="80px">
          <el-form-item prop="username" label="username" required>
            <el-input v-model="registerVo.username" placeholder="username" clearable></el-input>
          </el-form-item>
          <el-form-item prop="password" label="password" required>
            <el-input v-model="registerVo.password" type="password" placeholder="password" clearable></el-input>
          </el-form-item>
          <el-form-item prop="email" label="email" required>
            <el-input v-model="registerVo.email" placeholder="email" clearable></el-input>
          </el-form-item>
          <el-form-item label="captcha" prop="captcha" required>
            <el-input type="text" v-model="registerVo.captcha" placeholder="- - - -" clearable>
              <template slot="append">
                <el-image :src="src" @click="refreshCode" style="width: 80px; height: 33px"></el-image>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item label-width="0px">
            <el-button type="primary" @click="registerVoSubmit()">register</el-button>
          </el-form-item>
        </el-form>
        <el-link @click="backLogin()" type="primary">already have an account? click to login!</el-link>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  name: "register",
  data() {
    var checkEmail = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('please input your email'));
      } else {
        var pattern = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
        if (pattern.test(this.registerVo.email)) {
          callback();
        }
        callback('please input a correct email')
      }
    }

    return {
      src: "",
      registerVo: {
        username: '',
        password: '',
        email: '',
        captcha: ''
      },
      registerRule: {
        username: [
          {max: 15, message: "no more than 15 characters", trigger: 'blur'}
        ],
        password: [
          {max: 20, message: "no more than 20 characters", trigger: 'blur'}
        ],
        email: [
          {max: 30, message: "no more than 30 characters", trigger: 'blur'},
          {validator: checkEmail, trigger: 'blur'}
        ],
      }
    }
  },
  methods: {
    // register function
    registerVoSubmit() {
      this.$refs['registerForm'].validate((valid) => {
        if (valid) {
          this.$axios.post("/sys/register", this.registerVo, {
            params: {
              captchaId: sessionStorage.getItem("captchaId")
            }
          }).then(data => {
            if (data.data.success) {
              this.$message.success(data.data.msg);
              sessionStorage.removeItem("captchaId");
              this.$router.push({name: 'login'});
            } else {
              this.refreshCode();
              this.$message.error(data.data.code + ":" + data.data.msg);
            }
          }).catch(err => {
            this.refreshCode();
            console.log(err);
          })
        } else {
          this.refreshCode();
          return false;
        }
      })
    },

    backLogin() {
      sessionStorage.removeItem("captchaId");
      this.$router.push({name: 'login'});
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
  },
  created() {
    this.refreshCode();
  }
}
</script>
<style>
.register-wrapper {
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  overflow: hidden;
  background-size: 100% 100%;
}

.register-content {
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  margin: auto;
  height: 450px;
  width: 400px;
  background-color: #F0F3F4;
  opacity: .8;
}

.register-main {
  text-align: center;
  color: black;
  padding: 20px 20px 10px 20px;
}
</style>
