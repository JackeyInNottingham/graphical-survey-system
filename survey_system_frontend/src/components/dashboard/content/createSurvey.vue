<template>
  <div style="width: 1440px;">
    <el-row type="flex" align="middle">
      <el-col :span="2" class="label content-font">
        title:
      </el-col>
      <el-col :span="4">
        <el-input v-model="title" size="small" placeholder="title"></el-input>
      </el-col>
    </el-row>

    <el-row v-for="(image, index) in images" :key="index" type="flex" align="middle">
      <el-col
        :span="2"
        class="label content-font"
        v-text="index + 1"
      >
      </el-col>

      <el-col :span="5">
        <img v-if="image.path" :src="'/api/image/getImageByPath?path=' + image.path" class="avatar">
        <el-upload
          v-else
          accept=".jpeg, .JPEG, .jpg, .JPG, .png, .PNG"
          class="avatar-uploader"
          action="#"
          :http-request="(item)=>uploadFile(item, image)"
          :show-file-list="false"
          :before-upload="checkFileSize">
          <i class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload>
      </el-col>

      <el-col :span="8">
        <el-input
          type="textarea"
          placeholder="please input description"
          v-model="image.description"
          maxlength="255"
          :autosize="{ minRows: 3, maxRows: 5}"
          show-word-limit
        >
        </el-input>
      </el-col>

      <el-col :span="3" :offset="2">
        <el-button type="danger" @click="deleteElement(index)" size="small">
          <el-icon class="el-icon-delete"></el-icon>
        </el-button>
      </el-col>
    </el-row>

    <el-row>
      <el-col :span="2" class="label content-font">
        <p></p>
      </el-col>
      <el-col :span="3">
        <el-link
          v-if="images.length < 5"
          type="primary"
          class="content-font"
          @click="addImage"
        >
          <el-icon class="el-icon-circle-plus-outline"></el-icon>
          add an image
        </el-link>
      </el-col>
    </el-row>

    <el-row type="flex" align="middle" justify="center">
      <el-button type="primary" @click="createSurvey"  size="small">create</el-button>
    </el-row>
  </div>
</template>

<script>
export default {
  name: "createSurvey",

  data() {
    return {
      title: "",
      images: [
        {
          width: 0,
          height: 0,
          path: "",
          description: "",
        }
      ],
    }
  },

  methods: {
    addImage() {
      this.images.push({
        width: 0,
        height: 0,
        path: "",
        description: "",
      })
    },

    uploadFile(item, image) {
      let formData = new FormData();
      formData.append("picture", item.file);
      this.$axios.post("/survey/uploadPicture", formData).then(data => {
        if (data.data.success) {
          image.width = data.data.width;
          image.height = data.data.height;
          image.path = data.data.path;
          this.$message.success(data.data.msg);
        } else {
          this.$message.error(data.data.msg);
        }
      }).catch(error => {
        this.$message.error(error);
      })
    },

    checkFileSize(file) {
      let is5M = file.size / 1024 / 1024 < 5;
      if (!is5M) {
        this.$message.error("file size should be less than 5M");
      }
      return is5M;
    },

    deleteElement(index){
      this.$confirm('are you sure to delete this image？', 'warning', {
        confirmButtonText: 'yes',
        cancelButtonText: 'no',
        type: 'warning'
      }).then(() => {
        if (this.images[index].path !== ""){
          this.deleteImage(index);
        } else {
          this.images.splice(index, 1);
        }
      });
    },

    deleteImage(index){
      this.$axios.delete("/image/deleteImageByPath", {
        params: {
          path: this.images[index].path
        }
      }).then(data=>{
        if (data.data.success) {
          this.images.splice(index, 1);
        } else {
          this.$message.error(data.data.msg);
        }
      })
    },

    valid(){
      if (!this.title){
        this.$message.error("please input title")
        return false;
      }

      if (this.images.length < 1){
        this.$message.error("no image is uploaded");
        return false;
      }


      for (let index in this.images){
        if (this.images[index].path === ""){
          console.log(this.images[index]);
          this.$message.error("please upload an image in image: " + (parseInt(index) + 1));
          return false;
        }
      }

      return true;
    },

    createSurvey(){
      if (this.valid()){
        this.$axios.post("/survey/createSurvey", {
          title: this.title,
          imageNumber: this.images.length,
          images: this.images,
          createTime: new Date()
        }).then(data=>{
          if (data.data.success){
            this.title = "";
            this.images = [
              {
                width: 0,
                height: 0,
                path: "",
                description: "",
              }
            ];
            this.$message.success(data.data.msg);
          }
        })
      }
    }
  },

  beforeDestroy() {
    for (let idx in this.images){
      if (this.images[idx].path){
        this.deleteImage(idx);
      }
    }
  }

}
</script>

<style scoped>


.avatar-uploader {
  border: 1px dashed black;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 101px;
  height: 101px;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 100px;
  height: 100px;
  line-height: 100px;
  text-align: center;
}
.avatar {
  width: 100px;
  height: 100px;
  display: block;
}

.el-row {
  margin-bottom: 20px;
  display: flex;
  flex-wrap: wrap
}

.label {
  display: flex;
  justify-content: center;
  align-items: center;
}

.content-font {
  font-size: large;
  font-weight: bold;
}
</style>
