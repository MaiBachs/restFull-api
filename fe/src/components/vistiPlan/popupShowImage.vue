<script setup>
import { defineProps, ref,computed } from "vue";
const props = defineProps({
    filebase64VisitPlan: {
    type: Object,
    required: true,
    default: () => ({}),
  },
});
const imageScale = ref(1);
const showOut = ref(false);

const imageStyle = computed(() => {
  return {
    width: "auto",
    maxHeight: "450px",
    margin: "0px auto",
    transform: `scale(${imageScale.value})`,
  };
});

const zoomIn = () => {
  imageScale.value += 0.1;
  if (imageScale.value > 1) {
    showOut.value = false;
  }
};

const zoomOut = () => {
  if (imageScale.value > 1) {
    imageScale.value -= 0.1;
  }
  if (imageScale.value <= 1) {
    showOut.value = true;
  }
};
function downloadFileImage(content,fileName) {
    const downloadLink = document.createElement('a');
    downloadLink.href = content;
    downloadLink.download = fileName;
    downloadLink.click();
}
</script>
<template>
    <div id="carouselExampleDark" class="carousel carousel-dark slide">
    <div class="carousel-inner">
      <div class="carousel-item" v-for="(slide, index) in props.filebase64VisitPlan" :key="index" :class="{ 'active': index === 0 }">
        <img :src="slide.content" id="mapImg" class="d-block" :style="imageStyle" style="width: auto; max-height: 450px; margin: 0px auto;">
        <div class="carousel-caption">
          <p @click="downloadFileImage(slide.content ,slide.file_name)"><i class="fa fa-download text-white" aria-hidden="true"></i></p>
        </div>
      </div>
    </div>
    <button class="carousel-control-prev" type="button" data-bs-target="#carouselExampleDark" data-bs-slide="prev">
      <span class="carousel-control-prev-icon" aria-hidden="true"></span>
      <span class="visually-hidden">Previous</span>
    </button>
    <button class="carousel-control-next" type="button" data-bs-target="#carouselExampleDark" data-bs-slide="next">
      <span class="carousel-control-next-icon" ariahidden="true"></span>
      <span class="visually-hidden">Next</span>
    </button>

    <div style="text-align: center; padding: 15px 0px;">
      <div class="zom-img">
        <button class="m-r-10" type="button" @click="zoomIn"><i class="icon-plus"></i></button>
        <button :disabled="showOut" type="button" @click="zoomOut"><i class="icon-minus"></i></button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.zom-img button {
    background-color: transparent;
    border: 1px solid #cdcdcd;
    width: 30px;
    height: 30px;
    border-radius: 50%;

}

.zom-img button:hover {
    background-color: #e85656;
}
.zom-img button:disabled:hover ,
.zom-img button[disabled]:hover {
    background-color: transparent;
}
.carousel-inner {
    min-height: 400px;
}
</style>