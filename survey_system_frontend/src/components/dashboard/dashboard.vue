<template>
  <el-container class="dashboard">
    <el-header class="dashboard-header">
      <div class="header-content">
        <div style="float: left; cursor: pointer;" @click="$router.push({name: 'home'})">
          Graphical Survey Sytem
        </div>
        <span style="float: right">
          <span style="font-size: medium" v-text="'welcome, ' + $store.state.username"></span>
          <el-button type="info" @click="logout()" size="small">logout</el-button>
        </span>
      </div>
    </el-header>
    <el-container class="dashboard-body">
      <el-aside class="dashboard-aside" width="220px">
        <el-menu
          default-active="1"
          background-color="#333744"
          text-color="#fff"
          active-text-color="#ffd04b"
          unique-opened
        >
          <el-menu-item
            index="1"
            v-if="permitted(['admin', 'researcher'])"
            @click="switchContent('welcome')"
          >
            <i class="el-icon-s-home"></i>
            <span slot="title">welcome</span>
          </el-menu-item>
          <el-menu-item
            index="2"
            v-if="permitted(['admin'])"
            @click="switchContent('userList')"
          >
            <i class="el-icon-user"></i>
            <span slot="title">user management</span>
          </el-menu-item>
          <el-submenu index="2">
            <template slot="title">
              <i class="el-icon-folder-opened"></i>
              <span>survey management</span>
            </template>
            <el-menu-item
              index="2-1"
              v-if="permitted(['admin', 'researcher'])"
              @click="switchContent('createSurvey')"
            >
              <i class="el-icon-document-add"></i>
              <span slot="title">create survey</span>
            </el-menu-item>
            <el-menu-item
              index="2-2"
              v-if="permitted(['admin'])"
              @click="switchContent('surveyList')"
            >
              <i class="el-icon-document-copy"></i>
              <span slot="title">all surveys</span>
            </el-menu-item>
            <el-menu-item
              index="2-3"
              v-if="permitted(['admin', 'researcher'])"
              @click="switchContent('mySurvey')"
            >
              <i class="el-icon-document"></i>
              <span slot="title">my surveys</span>
            </el-menu-item>
          </el-submenu>
          <el-menu-item
            index="3"
            v-if="permitted(['admin', 'researcher'])"
            @click="switchContent('password')"
          >
            <i class="el-icon-setting"></i>
            <span slot="title">password management</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-main class="dashboard-main">
        <div :is="currentContent"></div>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import welcome from "./content/welcome";
import userList from "./content/userList";
import createSurvey from "./content/createSurvey";
import surveyList from "./content/surveyList";
import password from "./content/password";
import mySurvey from "./content/mySurvey";

export default {
  name: "dashboard",

  components: {
    welcome: welcome,
    userList: userList,
    createSurvey: createSurvey,
    surveyList: surveyList,
    password: password,
    mySurvey: mySurvey
  },

  data() {
    return {
      currentContent: welcome,
      role: this.$store.state.role,
    }
  },

  methods: {
    permitted(roles) {
      return roles.includes(this.role);
    },

    switchContent(path) {
      this.currentContent = path;
    },

    logout() {
      this.$store.commit('logout');
      this.$router.push({name: 'login'});
    },
  },
}
</script>

<style scoped>
.dashboard {
  height: 100%;
  width: 100%;
}

.dashboard-header {
  background-color: #373d41;
  color: #fff;
  font-size: 20px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.header-content {
  width: 100%;
  height: 36px;
}

.dashboard-body {
  width: 100%;
}

.dashboard-aside {
  background-color: #333744;
}

.dashboard-main {
  background-color: white;
  padding: 20px 0px 0px 20px;
}

</style>
