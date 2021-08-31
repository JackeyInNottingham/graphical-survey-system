<template>
  <el-container class="container">
    <el-header class="header">
      <div class="header-content">
        <div style="float: left; cursor: pointer;" @click="$router.push({name: 'home'})">
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

    <el-main class="body">
      <div class="result-wrapper">
        <div class="title-section">
          <span class="title" v-text="surveyBoardVo.title"></span>
        </div>
        <div class="title-section">
          author:&nbsp&nbsp<span v-text="surveyBoardVo.author"></span>
        </div>
        <div class="title-section">
          create time:&nbsp&nbsp<span v-text="surveyBoardVo.createTime"></span>
        </div>

        <div
          v-for="(image, index) in surveyBoardVo.images"
          :key="index"
          v-show="index === cur"
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
              v-show="index < surveyBoardVo.images.length - 1"
              class="el-icon-arrow-right far-right"
              @click="nextImage()"
            ></i>
          </div>
          <div
            class="img-wrapper"
          >
            <canvas
              ref="canvas"
              @click="clickImg($event, index)"
              :width="image.width"
              :height="image.height"
            >
            </canvas>
            <el-image
              :src="'/api/image/getImageByPath?path=' + image.path"
              fit="contain"
              :style="'width: ' + image.width + 'px; height: ' + image.height + 'px'"
              id="img"
              style="display: flex; justify-content: center; align-items: center"
            >
              <div slot="placeholder">
                LOADING
                <span class="dot">...</span>
              </div>
              <div slot="error">
                <i class="el-icon-picture-outline"></i>
                ERROR
              </div>
            </el-image>
          </div>
          <div class="img-description">
          <span v-text="image.description"
                style="width: 800px;word-break: break-all;white-space: normal; text-align: center"></span>
          </div>
          <div style="display: flex; justify-content: center">
            <el-button @click="clearCanvas(index)" size="small">clean</el-button>
          </div>
        </div>
        <el-row type="flex" align="middle" justify="center" style="margin-top: 15px">
          <el-button
            @click="submitResult"
            type="primary"
            :disabled="!this.answerVo.some((element)=>{return element.points.length > 0})"
            size="small"
          >submit
          </el-button>
          <el-button
            @click="clearAllCanvas"
            type="warning"
            size="small"
          >reset
          </el-button>
        </el-row>
      </div>
    </el-main>
  </el-container>
</template>

<script>

export default {
  name: "detail",

  data() {
    return {
      cur: 0,

      surveyBoardVo: {
        title: "",
        author: "",
        createTime: "",
        images: []
      },

      answerVo: [],
    }
  },

  methods: {
    getSurveyVo(id) {
      this.$axios.get("/part/getSurveyBoard", {
        params: {
          id: id,
        }
      }).then(data => {
        if (data.data.success) {
          let images = data.data.surveyBoardVo.images;
          for (let index in images) {
            this.resize(images[index]);
            this.answerVo.push({
              imageId: images[index].id,
              points: []
            })
          }
          this.surveyBoardVo = data.data.surveyBoardVo;
        } else {
          this.$message.error(data.data.msg);
          this.$router.go(-1);
        }
      }).catch(error => {
        this.$message.error(error)
      })
    },


    resize(image) {
      let width = image.width;
      let height = image.height;
      if (width > 1100 || height > 520) {
        if (width / height > 1100 / 520) {
          image.width = 1100;
          image.height = 1100 * height / width;
        } else {
          image.height = 520;
          image.width = 520 * width / height;
        }
      }
    },

    previousImage() {
      this.cur = this.cur - 1;
    },

    nextImage() {
      this.cur = this.cur + 1;
    },

    // get relative position of click event
    clickImg(event, index) {
      //get absolute position
      let x = event.offsetX;
      let y = event.offsetY;

      //get the context
      let canvas = this.$refs["canvas"];
      let context = canvas[index].getContext("2d");

      context.beginPath();
      context.arc(x, y, 5, 0, 360, false);
      context.fillStyle = "red";
      context.fill();
      context.closePath();

      x = x / this.surveyBoardVo.images[index].width;
      y = y / this.surveyBoardVo.images[index].height;

      this.answerVo[index].points.push({
        x: x,
        y: y
      });
    },

    clearCanvas(index) {
      let canvas = this.$refs["canvas"][index];
      canvas.width = this.surveyBoardVo.images[index].width;
      canvas.height = this.surveyBoardVo.images[index].height;
      this.answerVo[index].points = [];
    },

    submitResult() {
      this.$axios.post("/part/submitAnswer", this.answerVo).then(data=>{
        if (data.data.success) {
          console.log(data.data);
          this.$message.success(data.data.msg);
          this.$router.push({name: 'home'})
        } else {
          this.$message.error(data.data.msg);
        }
      }).catch(err => {
        this.$message.error(err);
      })
    },

    clearAllCanvas() {
      for (let index in this.surveyBoardVo.images) {
        this.clearCanvas(index);
      }
    }

  },

  mounted() {
    this.getSurveyVo(this.$route.params.id);
  }


}
</script>

<style scoped>
.container {
  height: 100%;
  width: 100%;
}

.header {
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

.title-section {
  display: flex;
  justify-content: center;
  align-items: center;
}

.title {
  font-size: xx-large;
  font-weight: bold;
  margin: 15px auto 15px
}

.body {
  display: flex;
  justify-content: center;
  width: 100%;
  height: 100%;
  padding: 0;
  background-color: #ebecee;
}

.result-wrapper {
  width: 1200px;
  height: 830px;
  padding: 10px;
  background-color: white;
}

.pagination-holder {
  width: 1200px;
  height: 50px;
  font-size: 50px;
  position: absolute;
  z-index: 1;
  margin: auto;
  left: 0;
  top: 0;
  right: 0;
  bottom: 0;
}

.far-left {
  float: left;
  cursor: pointer;
}

.far-right {
  float: right;
  cursor: pointer;
}

canvas {
  position: absolute;
  z-index: 2;
}

.img-wrapper {
  position: relative;
  height: 540px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.img-description {
  height: 80px;
  display: flex;
  justify-content: center;
}

</style>
