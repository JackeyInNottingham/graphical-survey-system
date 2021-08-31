<template>

  <div>
    <el-header style="font-size: 12px" height="40px">

      <el-row :gutter="10" type="flex" align="middle">
        <el-col :span="3">
          <el-input
            placeholder="please input title"
            v-model="title"
            size="small"
            clearable
            @change="changePage"
          >
          </el-input>
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

      </el-row>

    </el-header>

    <el-main style="height: 725px; padding-bottom: 0">
      <!--        table container-->
      <el-table :data="mySurveyList" :stripe="true" :border="true">
        <el-table-column prop="title" label="title" width="600" resizable>
        </el-table-column>
        <el-table-column prop="createTime" label="create time" width="250" resizable>
        </el-table-column>
        <el-table-column prop="imageNumber" label="image number" width="200" resizable>
        </el-table-column>
        <el-table-column label="operation" resizable>
          <template slot-scope="scope">
            <!--TODO: 创建结果窗口-->
            <el-button
              type="info"
              icon="el-icon-document"
              @click="showResult(scope.row.id)"
              size="small"
            >
              result
            </el-button>
            <el-button
              type="danger"
              icon="el-icon-delete"
              @click="deleteMySurvey(scope.row.id)"
              size="small"
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

    <el-dialog
      title="result"
      :visible.sync="resultDialogVisible"
      :fullscreen="false"
      width="70%"
      @opened="drawResult()"
      :destroy-on-close="true"
      @close="cur = 0"
      center
    >
      <div class="result-wrapper">
        <div
          v-for="(result, index) in surveyResult"
          :key="index"
          v-show="index === cur"
          class="result-player"
        >
          <div
            class="pagination-holder"
          >
            <i
              v-show="index > 0"
              class="el-icon-arrow-left far-left"
              @click="previousImage()"
            ></i>
            <i
              v-show="index < surveyResult.length - 1"
              class="el-icon-arrow-right far-right"
              @click="nextImage()"
            ></i>
          </div>
          <div
            class="img-wrapper"
          >
            <canvas
              ref="canvas"
            >
            </canvas>
          </div>
        </div>
      </div>

      <div class="dialog-footer">
        <el-button type="primary" @click="downloadImg" size="small">download</el-button>
        <el-button type="info" @click="resultDialogVisible=false" size="small">close</el-button>
      </div>
    </el-dialog>

  </div>


</template>

<script>
export default {
  name: "MySurvey",

  data() {

    return {
      title: "",

      mySurveyList: [],

      // page component properties
      curPage: 1,
      pageSize: 10,
      totalNum: 0,

      //result dialog
      resultDialogVisible: false,
      cur: 0,
      surveyResult: []
    }
  },

  methods: {
    changePage() {
      if (this.title !== "") {
        this.filtrateMySurveyList();
      } else {
        this.getMySurveyList();
      }
    },

    filtrateMySurveyList() {
      this.$axios.get("/survey/filtrateMySurveyList", {
        params: {
          title: this.title,
          page: this.curPage,
          pageSize: this.pageSize
        }
      }).then(data => {
        if (data.data.success) {
          this.mySurveyList = data.data.mySurveyList.list;
          this.totalNum = data.data.mySurveyList.total;
        } else {
          this.$message.error(data.data.msg);
          this.mySurveyList = [];
          this.totalNum = 0;
        }
      }).catch(error => {
        this.$message.error(error);
      })
    },


    //get full survey list
    getMySurveyList() {
      this.$axios.get("/survey/getMySurveyListByPage", {
        params: {
          page: this.curPage,
          pageSize: this.pageSize
        }
      }).then(data => {
        if (data.data.success) {
          this.mySurveyList = data.data.mySurveyList.list;
          this.totalNum = data.data.mySurveyList.total;
        } else {
          this.$message.error(data.data.msg);
          this.mySurveyList = [];
          this.totalNum = 0;
        }
      }).catch(err => {
        console.log(err);
        this.$message.error(err);
      })
    },

    deleteMySurvey(id) {
      this.$confirm('Are you sure to delete this survey', '', {
        confirmButtonText: 'confirm',
        cancelButtonText: 'cancel',
        type: 'warning',
        center: true
      }).then(() => {
          this.$axios.delete("/survey/deleteMySurveyById", {
            params: {
              id: id
            }
          }).then(data => {
            if (data.data.success) {
              this.$message.success(data.data.msg);
              this.getMySurveyList();
            } else {
              this.$message.error(data.data.msg);
            }
          }).catch(error => {
            this.$message.error(error);
          })
        }
      ).catch();
    },



    showResult(id) {
      this.$axios.get("/survey/getResult", {
        params: {
          id: id
        }
      }).then(data => {
        if (data.data.success) {
          this.surveyResult = data.data.surveyResult;
        } else {
          this.$message.error(data.data.msg);
        }
      }).catch(error => {
        this.$message.error(error);
      })
      this.resultDialogVisible = true;
    },


    previousImage() {
      this.cur = this.cur - 1;
    },

    nextImage() {
      this.cur = this.cur + 1;
    },

    drawResult() {
      let canvas = this.$refs["canvas"];
      let result = this.surveyResult;

      for (let idx in result) {
        //resize canvas
        let width = result[idx].width;
        let height = result[idx].height;
        if (width > 1100 || height > 520) {
          if (width / height > 1100 / 520) {
            width = 1100;
            height = 1100 * result[idx].height / result[idx].width;
          } else {
            height = 520;
            width = 520 * result[idx].width / result[idx].height;
          }
        }

        canvas[idx].width = width;
        canvas[idx].height = height;

        let context = canvas[idx].getContext("2d");

        //get image
        let image = new Image();
        image.src = "/api/image/getImageByPath?path=" + result[idx].path;
        let _this = this;

        //draw image and points
        image.onload = function () {
          context.drawImage(image, 0, 0, width, height)
          _this.drawPoints(context, result[idx].points, width, height);
        }
      }
    },

    downloadImg() {
      for (let result of this.surveyResult){
        let image = new Image();
        let _this = this;
        image.src = "/api/image/getImageByPath?path=" + result.path;
        image.onload = function () {
          let downloader = document.createElement("canvas");
          downloader.width = result.width;
          downloader.height = result.height;
          let context = downloader.getContext("2d");
          context.drawImage(this, 0, 0, result.width, result.height);
          _this.drawPoints(context, result.points, result.width, result.height);
          downloader.toBlob(blob => {
            let url = URL.createObjectURL(blob);
            let eleLink = document.createElement('a');
            eleLink.download = "result";
            eleLink.href = url;
            eleLink.click();
            eleLink.remove();
          })
        }
      }
    },

    drawPoints(context, points, width, height) {
      for (let point of points) {
        context.beginPath();
        context.arc(width * point.x, height * point.y, 5, 0, 360, false);
        context.fillStyle = "red";
        context.fill();
        context.closePath();
      }
    },
  },

  created() {
    this.getMySurveyList();
  }
}
</script>

<style scoped>

.result-wrapper {
  display: flex;
  justify-content: center;
  align-content: center;
  height: 550px;
}

.img-wrapper {
  height: 540px;
  width: 1100px;
  display: flex;
  margin: auto;
  justify-content: center;
  align-items: center;
}

.pagination-holder {
  position: absolute;
  width: 100%;
  height: 50px;
  font-size: 50px;
  z-index: 1;
  margin: auto;
  left: 0;
  top: 0;
  right: 0;
  bottom: 0;
}

.result-player {
  width: 1100px;
  height: 550px;
  display: flex;
  justify-content: center;
  align-content: center
}

.far-left {
  float: left;
  cursor: pointer;
}

.far-right {
  float: right;
  cursor: pointer;
}

.dialog-footer {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: 20px
}
</style>
