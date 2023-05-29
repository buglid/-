<template>
  <div class="app-container" v-loading="loading">
     <div style="display:flex;">
        <div id="mainid" class="tu"></div>
        <div style="width:800px;height:500px;border:1px solid #ddd;margin-left:50px">
          <span>该盒图的中位数为：{{median}}</span>
          <br>
          <span>污染程度：{{ wuran }}</span>
          <br>
          <span>{{ wuranwu }}</span>
        </div>
     </div>
     <div id="mainid2" class="tu2"></div>
     <!-- 列表信息------------------------------------------------------>
     <el-table v-loading="loading" :data="dataList" style="max-height:600px;overflow-y: auto;">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="编号" align="center" prop="id" />
      <el-table-column label="地区" align="center" prop="areaId">
          <template slot-scope="scope">
              <span>{{ cityName(scope.row.areaId) }}</span>
          </template>
      </el-table-column>
      <el-table-column label="日期" align="center" prop="date" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.date, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="PM2.5" align="center" prop="pm25" />
      <el-table-column label="PM10" align="center" prop="pm10" />
      <el-table-column label="SO2" align="center" prop="so2" />
      <el-table-column label="NO2" align="center" prop="no2" />
      <el-table-column label="CO" align="center" prop="co" />
      <el-table-column label="O3" align="center" prop="o3" />
      <el-table-column label="入库日期" align="center" prop="enterDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.enterDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="用户" align="center" prop="userId" />
      <el-table-column label="备注" align="center" prop="remarks" />
    </el-table>
  </div>
</template>

<script>
import * as echarts from 'echarts';
import {citys,filterCitys} from '../../../assets/city'
import { hetu } from "../../../assets/echartsOptions";
export default {
  name: "BoxDiagramDetail",
  data() {
    return {
      // 遮罩层
      loading: false,
      allData:[],
      dataList:[],
      title:'',
      median:'',
      showData:'',
      wuran: '',
      wuranwu: ''
    };
  },
  computed:{
    cityName(){
      return (code)=>{
        const item = filterCitys.find(x=>x.city_code==code);
        const name = item?item.city_name:'';
        return name
      }
    }
  },
  created(){
    if(this.$route.params.title){
      this.$route.meta.title = this.$route.params.title+'详情'
    }
    if(this.$route.params.path){
      this.$route.meta.activeMenu = '/cluster/'+this.$route.params.path
    }
  },
  mounted() {
    if(this.$route.params.myData){
      this.allData = this.$route.params.myData;
      this.dataList = this.$route.params.filterList;
      this.title = this.$route.params.myTitle
      this.median = this.$route.params.median
      this.showData = this.$route.params.showData
      this.getList();
      this.initLine();  
      
    }else{
      const path = this.$route.params.path?this.$route.params.path:'lrr'
      this.$router.push({path: `/cluster/${path}`, replace: true})
    }
  },
  methods: {
    getList() {
      const _that = this;
      const myChart = echarts.init(document.getElementById("mainid"));
      let str =  JSON.stringify(hetu)
      const option = JSON.parse(str);
      option.dataset[0].source = this.showData;
      option.title[0].text = this.title;

      if(this.title === "PM2.5"){
          if(this.median < 100){
            this.wuran = "无污染"
          }else if(this.median < 150){
            this.wuran = "PM2.5轻度污染"
          }else if(this.median < 200){
            this.wuran = "PM2.5中度污染"
          }else if(this.median > 200){
            this.wuran = "PM2.5严重污染"
          }
          this.wuranwu = "PM2.5，指空气动力学当量直径小于或等于2.5微米（一微米等于百万分之一米）的悬浮颗粒物。它在大气中滞留时间长，传输距离远，含多种有毒有害物质，而且与其他空气污染物存在着复杂的转化关系。PM2.5易于滞留在终末细支气管和肺泡中，其中某些还可以穿透肺泡进入血液，也更易于吸附各种有毒的有机物和重金属元素，对健康的危害极大。PM2.5除来自自然界的风沙尘土、森林火灾、海水喷溅等，更主要来自工业生产、公路扬尘、建筑扬尘以及人类生产生活使用的能源燃烧等。"
      }else if(this.title === "PM10"){
        if(this.median < 150){
            this.wuran = "无污染"
          }else{
            this.wuran = "PM10污染"
          }
          this.wuranwu = "可吸入颗粒物，通常是指粒径在10微米以下的颗粒物，又称PM10。可吸入颗粒物在环境空气中持续的时间很长，对人体健康和大气能见度的影响都很大。通常来自在未铺的沥青、水泥的路面上行驶的机动车、材料的破碎碾磨处理过程以及被风扬起的尘土。可吸入颗粒物被人吸入后，会积累在呼吸系统中，引发许多疾病，对人类危害大。可吸入颗粒物的浓度以每立方米空气中可吸入颗粒物的毫克数表示。国家环保总局1996年颁布修订的《环境空气质量标准（GB3095-1996）》中将飘尘改称为可吸入颗粒物，作为正式大气环境质量标准。"
      }else if(this.title === "SO2"){
        if(this.median < 100){
            this.wuran = "无污染"
          }else if(this.median < 200){
            this.wuran = "SO2轻度污染"
          }else{
            this.wuran = "SO2严重污染"
          }
          this.wuranwu = "它主要来自矿物燃烧、含硫矿石冶炼和硫酸、磷肥生产等。全世界SO2的人为排放量每年约1.5亿吨，矿物燃料燃烧产生的占70%以上。自然产生的SO2数量很少，主要是生物腐烂生成的硫化氢在大气中氧化而成。SO2的排放源，90%以上集中在北半球的城市和工业区，造成这些地区大气污染问题。SO2在大气中会氧化而成硫酸雾或硫酸盐气溶胶，是环境酸化的前驱物。它能使植物叶肉组织受损伤。对人主要是刺激呼吸道，可使气管和支气管的管腔缩小，气道阻力增大。SO2和飘尘具有协同效应，二者对人体健康的影响往往是不可分的。如伦敦烟雾事件、马斯河谷事件和多诺拉等烟雾事件，都是这种协同作用所造成的危害。"
      }else if(this.title === "NO2"){
        if(this.median < 80){
            this.wuran = "无污染"
          }else{
            this.wuran = "NO2污染"
          }
          this.wuranwu = "氮氧化物 (nitrogen oxides)包括多种化合物，如一氧化二氮(N2O)、一氧化氮 (N0)、二氧化氮(NO2)、三氧化二氮 (N203)、四氧化二氮(N204)和五氧化二氮(N205)等。除二氧化氮以外，其他氮氧化物均极不稳定，遇光、湿或热变成二氧化氮及一氧化氮，一氧化氮又变为二氧化氮。因此，职业环境中接触的是几种气体混合物常称为硝烟 （气），主要为一氧化氮和二氧化氮，并以二氧化氮为主。氮氧化物都具有不同程度的毒性。"
      }else if(this.title === "CO"){
        if(this.median < 400){
            this.wuran = "无污染"
          }else{
            this.wuran = "CO污染"
          }
          this.wuranwu = "CO是无色、无臭的有毒气体，是排放量最大的大气污染物。矿物燃料燃烧、石油炼制、钢铁冶炼、固体废物焚烧都要排放大量的CO。据估计，每年人为排放一氧化碳总量为3—4亿吨，其中一半以上来自汽车废气。CO能与血红蛋白结合，降低输氧能力，严重时可使人窒息死亡。CO能参与光化学烟雾的形成反应而造成危害。CO的人为来源主要是矿物燃料燃烧、石油炼制、钢铁冶炼、固体废物焚烧等。CO是排放量最大的大气污染物 [2]  。据估计，每年人为排放CO总量为3-4亿吨，其中一半以上来自汽车废气。过去曾认为CO大部分来自人类活动，研究表明，CO的自然排放量比人为排放量大几倍，主要来自森林火灾、海洋和陆地生物的腐烂等过程。CO在大气中的存留时间约为几个月。大气中CO的消除作用现已知道的有HO自由基氧化作用，形成CO2，更主要是土壤微生物的代谢过程。"
      }else if(this.title === "O3"){
        if(this.median < 400){
            this.wuran = "无污染"
          }else{
            this.wuran = "O3污染"
          }
          this.wuranwu = "臭氧是一种强氧化剂，对健康和环境有较大危害。短期接触的影响：臭氧刺激眼睛和呼吸道。可能对中枢神经系统有影响，导致警惕性和表现能力受损。吸入臭氧气体可能引起肺水肿。影响可能延迟。液体臭氧可能导致冻伤。长期或反复接触的影响 ：反复或长时间吸入气体，可能对肺脏有影响。该物质可能对环境有危害。对植物应当给予特别注意。"
      }
      


      myChart.setOption(option);
    },
    initLine(){
      const myChart = echarts.init(document.getElementById("mainid2"));
      const times = [];
      const data = [];
      this.dataList.forEach(x=>{
         times.push(this.parseTime(x.date,'{y}-{m}-{d}'))
      });
      const option = {
          xAxis: {
            type: 'category',
            data: times
          },
          yAxis: {
            type: 'value'
          },
          series: [
            {
              data: this.allData,
              type: 'line'
            }
          ]
        }
        myChart.setOption(option);
    }
  }
};
</script>

<style scoped>
.app-container{
    height: 100%;
    /* border: 1px solid red; */
}

.tu{
  width: 600px;
  height: 500px;
  border: 1px solid #ddd;
  margin-bottom: 30px;
}

.tu2{
  width: 860px;
  height: 500px;
  border: 1px solid #ddd;
  margin-bottom: 30px;
}
</style>
