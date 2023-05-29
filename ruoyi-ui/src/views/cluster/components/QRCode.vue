<template>
  <div class="app-container2">
    <div class="title-1">请付完款完成后续操作</div>
    <div class="tu">
     <div class="title-1">
       <img src='@/assets/images/buy(2).jpg'>
     </div>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts';
export default {
  name: "QRCode",
  data() {
    return {
      // 遮罩层
      loading: false,
    };
  },
  computed:{
  },
  mounted() {
   
  },
  methods: {
   
  }
};
</script>

<style scoped>

.el-dialog__body{
  padding: 10px 10px;
}

.title-1{
  text-align: center;
    font-size: 30px;
    margin-bottom: 10px;
}

.tu{
  height: 400px;
  border: 1px solid #ddd;
}

</style>
