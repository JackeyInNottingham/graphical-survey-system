<template>
  <el-container class="home">
    <el-header class="home-header">
      <div class="home-header-content">
        <div style="float: left;">
          Graphical Survey Sytem
        </div>
        <span style="float: right;" v-if="!$store.state.login">
          <el-link
            class="user-link"
            type="success"
            icon="el-icon-user"
            @click="$router.push({name: 'login'})"
          >
            login
          </el-link>
          <el-link
            class="user-link"
            type="primary"
            icon="el-icon-edit"
            @click="$router.push({name: 'register'})"
          >
            register
          </el-link>
        </span>
        <span style="float: right;" v-if="$store.state.login">
          <el-link
            class="user-link"
            type="success"
            icon="el-icon-user-solid"
            @click="$router.push({name: 'dashboard'})"
            :underline="false"
          >
            personal center
          </el-link>
        </span>
      </div>
    </el-header>

    <el-main class="home-body">
      <div class="home-body-search-wrapper">
        <el-row :gutter="10" type="flex" align="middle" justify="center" style="margin-top: 5px">

          <el-col :span="4">
            <el-input
              placeholder="please input title"
              v-model="title"
              size="medium"
              clearable
              @change="changePage"
            >
            </el-input>
          </el-col>

          <el-col :span="4">
            <el-input
              placeholder="please input author"
              v-model="author"
              size="medium"
              clearable
              @change="changePage"
            >
            </el-input>
          </el-col>

          <el-col :span="2">
            <el-button
              icon="el-icon-search"
              size="medium"
              @click="changePage"
              type="primary"
            >
              search
            </el-button>
          </el-col>

        </el-row>
      </div>
      <div class="home-body-wrapper">
        <div class="survey-list">
          <el-table
            :data="surveyList"
            stripe
            style="width: 100%"
            header-align="center"
          >
            <el-table-column
              prop="title"
              label="title"
              width="650"
            >
              <template slot-scope="scope">
                <el-link
                  type="primary"
                  :underline="false"
                  v-text="scope.row.title"
                  @click="showSurvey(scope.row.id)"
                ></el-link>
              </template>
            </el-table-column>
            <el-table-column
              prop="imageNumber"
              label="image number"
              width="150">
            </el-table-column>
            <el-table-column
              prop="author"
              label="author"
              width="180">
            </el-table-column>
            <el-table-column
              prop="createTime"
              label="create time">
            </el-table-column>
          </el-table>
        </div>

        <el-row class="home-body-pagination" type="flex" align="bottom" justify="center">
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
      </div>
    </el-main>

  </el-container>
</template>

<script>
export default {
  name: "home",
  data() {
    return {

      //search component
      title: "",
      author: "",

      //displayed survey list with compressed images
      surveyList: [],

      // page component properties
      curPage: 1,
      pageSize: 13,
      totalNum: 0,
    }
  },
  methods: {

    showSurvey(id) {
      this.$router.push({
        name: 'detail',
        params: {
          id: id
        }
      })
    },

    changePage() {
      if (this.title !== "" || this.author !== "") {
        this.filtrateSurveyList();
      } else {
        this.getSurveyList();
      }
    },

    filtrateSurveyList() {
      this.$axios.get("/part/filtrateSurveySampleListByPage", {
          params: {
            title: this.title,
            author: this.author,
            page: this.curPage,
            pageSize: this.pageSize
          }
        }
      ).then(data => {
        if (data.data.success) {
          this.surveyList = data.data.surveyList.list;
          this.totalNum = data.data.surveyList.total;
        } else {
          this.surveyList = [];
          this.totalNum = 0;
          this.$message.error(data.data.msg);
        }
      }).catch(error => {
        this.$message.error(error);
      })
    },

    getSurveyList() {
      this.$axios.get("/part/getSurveySampleListByPage", {
          params: {
            page: this.curPage,
            pageSize: this.pageSize
          }
        }
      ).then(data => {
        if (data.data.success) {
          this.surveyList = data.data.surveyList.list;
          this.totalNum = data.data.surveyList.total;
        } else {
          this.surveyList = [];
          this.totalNum = 0;
          this.$message.error(data.data.msg);
        }
      }).catch(error => {
        this.$message.error(error);
      })
    },


  },
  created() {
    this.getSurveyList();
  }
}
</script>

<style scoped>
.home {
  height: 100%;
  width: 100%;
}

.home-header {
  background-color: #373d41;
  color: #fff;
  font-size: 20px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.home-header-content{
  width: 100%;
  height: 36px;
}

.home-body {
  display: flex;
  flex-direction: column;
  width: 100%;
  height: 100%;
  padding: 0;
  background-color: #ebecee;
}

.home-body-search-wrapper {
  flex-grow: 1;
  width: 1200px;
  height: 50px;
  margin: 10px auto 0;
  padding: 10px;
  background-color: white;
}

.home-body-wrapper {
  width: 1200px;
  height: 740px;
  margin: 10px auto 10px;
  padding: 10px;
  background-color: white;
}

.survey-list{
  height: 680px;
}

.home-body-pagination {
  padding-top: 20px;
}
</style>
