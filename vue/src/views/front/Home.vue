<template>
  <div class="home-container">
    <div class="carousel-wrapper"
         @mouseenter="showArrows = true"
         @mouseleave="showArrows = false">
      <el-carousel
          height="450px"
          :arrow="showArrows ? 'always' : 'never'"
          :autoplay="true"
          interval="5000"
          loop
          type="fade"
          indicator-position="outside"
      >
        <el-carousel-item v-for="item in data.carouselImgs" :key="item">
          <img :src="item" alt="" class="carousel-image">
        </el-carousel-item>
      </el-carousel>
    </div>
    <div class="middle">系列专题</div>
    <div class="card-container">
      <div class="card1" v-for="(item, index) in data.cardImgs" :key="index" @mouseenter="scaleImage(index, 1.1)" @mouseleave="scaleImage(index, 1)">
        <img :src="item" alt="" class="card-image">
      </div>
    </div>
    <footer class="footer">
      <p>欢迎使用！@医院预约管理系统</p>
    </footer>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue';
import img1 from '@/assets/imgs/imgs-home/1.png';
import img2 from '@/assets/imgs/imgs-home/2.png';
import img3 from '@/assets/imgs/imgs-home/3.png';
import img4 from '@/assets/imgs/imgs-home/4.png';
import img5 from '@/assets/imgs/imgs-home/5.png';
import img6 from '@/assets/imgs/imgs-home/6.png';

const data = reactive({
  carouselImgs: [img1, img2, img3], // 轮播图的图片路径
  cardImgs: [img4, img5, img6], // 卡片的图片路径
});
const showArrows = ref(false); // 控制箭头显示的响应式变量
const scale = reactive({}); // 存储每个图片的缩放比例

const scaleImage = (index, factor) => {
  scale[index] = factor;
};
</script>

<style scoped>
.home-container {
  width: 100%;
  box-sizing: border-box;
  margin: auto;
  position: relative;
}

.carousel-wrapper {
  width: 80%;
  margin: 20px auto; /* 调整轮播图容器的外边距 */
  overflow: hidden;
  position: relative;
}

.carousel-image {
  width: 100%;
  height: auto;
  display: block;
}

.middle {
  width: 80%;
  margin: 20px auto; /* 使标题居中且占满80%的宽度 */
  text-align: left; /* 文字靠左对齐 */
  font-size: 24px; /* 根据需要调整字体大小 */
  font-weight: bold; /* 加粗字体 */
}

.card-container {
  width: 80%;
  margin: 20px auto;
  display: grid;
  grid-template-columns: repeat(3, 1fr); /* 3列布局 */
  gap: 20px;
}

.card1 {
  transition: transform 0.3s ease-in-out;
  box-shadow: none;
  overflow: hidden;
  position: relative;
}

.card-image {
  width: 100%;
  height: auto;
  display: block;
  transform: scale(var(--scale-factor, 1)); /* 设置默认值为1 */
  transition: transform 0.3s ease-in-out;
}

.footer {
  width: 100%;
  text-align: center;
  padding: 20px;
  //background-color: #87CEFA;
  background-color: rgba(204, 226, 253, 0.5);
  color: #333;
}
</style>