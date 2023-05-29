<template>
  <div>
    <div style="margin-top: 20px;margin-left: 136px;">
      <el-row>
        <el-col>
          <el-button
            type="warning"
            plain
            icon="el-icon-download"
            size="mini"
            @click="handleExport"
            v-hasPermi="['system:pollution:export']"
          >导出原始数据</el-button>
          <el-button
            type="warning"
            plain
            icon="el-icon-download"
            size="mini"
            @click="handleExport2"
            v-hasPermi="['system:pollution:export']"
          >导出聚类数据</el-button>
        </el-col>
      </el-row>
    </div>
    <div class="app-container" v-loading="loading">
      <div class="tu" v-for="n in 6" :key="10000+n" :id="`main${n}`"></div>
    </div>
    <div v-loading="loading">
      <div class="tu2" v-for="n in 6" :key="1000+n" :id="`mainchart${n}`"></div>
    </div>
  </div>
</template>

<script>
import { cluster,SSC,SP,Rcut,Ncut,DBSCAN,KMeans,DP,CLUB,EPC} from "@/api/system/pollution";
import * as echarts from 'echarts';
import { hetu } from "../../../assets/echartsOptions";
export default {
  name: "BoxDiagram",
  data() {
    return {
      // 遮罩层
      loading: false,
      allData:[],
      dataList:[],
      median:'',
      clusterStr:''
    };
  },
  mounted() {
    if(this.$route.params.title){
      this.$route.meta.title = this.$route.params.title
    }
    if(this.$route.params.path){
      this.$route.meta.activeMenu = '/cluster/'+this.$route.params.path
    }
    if(this.$route.params.num){
      this.getList();
    }else{
      const path = this.$route.params.path?this.$route.params.path:'lrr'
      this.$router.push({path: path, replace: true})
    }
  
  },
  methods: {
    handleExport(){
      const p = {
        ...this.$route.params
      }
      this.download('system/pollution/export', {
        ...p
      }, `原始数据_${new Date().getTime()}.xlsx`)
    },
    handleExport2(){
      const p = {
        ...this.$route.params,
        clusterstr:this.clusterStr
      }
      this.download('system/pollution/exportcluster', {
        ...p
      }, `聚类数据_${new Date().getTime()}.xlsx`)

    },
    getList() {
      const _that = this;
      let str =  JSON.stringify(hetu)
      const option = JSON.parse(str);
      option.title[0].text = 'PM2.5';
      option.series[0].id = 'pm2.5';
      option.tooltip.formatter = this.formatter
      const option2 = JSON.parse(str);
      option2.title[0].text = 'PM10';
      option2.series[0].id = 'pm10';
      option2.tooltip.formatter = this.formatter
      const option3 = JSON.parse(str);
      option3.title[0].text = 'SO2';
      option3.series[0].id = 'so2';
      option3.tooltip.formatter = this.formatter
      const option4 = JSON.parse(str);
      option4.title[0].text = 'NO2';
      option4.series[0].id = 'no2';
      option4.tooltip.formatter = this.formatter
      const option5 = JSON.parse(str);
      option5.title[0].text = 'CO';
      option5.series[0].id = 'co';
      option5.tooltip.formatter = this.formatter
      const option6 = JSON.parse(str);
      option6.title[0].text = 'O3';
      option6.series[0].id = 'o3';
      option6.tooltip.formatter = this.formatter
     
      const loading = this.$loading({
          lock: true,
          text: 'Loading',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
      });
      const options = {option,option2,option3,option4,option5,option6}
      const myPath = this.$route.params.path;
      console.log(myPath);
      if(myPath=='lrr'){
          cluster(this.$route.params).then(response => {
            _that.clusterStr = response.clusterStr
            _that.handlerResposeData(response.dic,loading,options);
        }).catch(() => {
          loading.close()
        });
      }else if(myPath=='ssc'){
        console.log("运行至SSC");
          SSC(this.$route.params).then(response => {
          _that.clusterStr = response.SSCStr
          _that.handlerResposeData(response.SSCdic,loading,options);
        }).catch(() => {
          loading.close()
        });
      }else if(myPath=='Rcut'){
        console.log("运行至Rcut");
          Rcut(this.$route.params).then(response => {
          _that.clusterStr = response.RcutStr
          _that.handlerResposeData(response.Rcutdic,loading,options);
        }).catch(() => {
          loading.close()
        });
      }else if(myPath=='SP'){
        console.log("运行至SP");
          SP(this.$route.params).then(response => {
          _that.clusterStr = response.SPStr
          _that.handlerResposeData(response.SPdic,loading,options);
        }).catch(() => {
          loading.close()
        });
      }else if(myPath=='Ncut'){
        console.log("运行至Ncut");
          Ncut(this.$route.params).then(response => {
          _that.clusterStr = response.NcutStr
          _that.handlerResposeData(response.Ncutdic,loading,options);
        }).catch(() => {
          loading.close()
        });
      }else if(myPath=='DBSCAN'){
        console.log("运行至DBSCAN");
          DBSCAN(this.$route.params).then(response => {
          _that.clusterStr = response.DBSCANStr
          _that.handlerResposeData(response.DBSCANdic,loading,options);
        }).catch(() => {
          loading.close()
        });
      }else if(myPath=='KMeans'){
        console.log("运行至KMeans");
          KMeans(this.$route.params).then(response => {
          _that.clusterStr = response.KMeansStr
          _that.handlerResposeData(response.KMeansdic,loading,options);
        }).catch(() => {
          loading.close()
        });
      }else if(myPath=='DP'){
        console.log("运行至DP");
          DP(this.$route.params).then(response => {
          _that.clusterStr = response.DPStr
          _that.handlerResposeData(response.DPdic,loading,options);
        }).catch(() => {
          loading.close()
        });
      }else if(myPath=='CLUB'){
        console.log("运行至CLUB");
          CLUB(this.$route.params).then(response => {
          _that.clusterStr = response.CLUBStr
          _that.handlerResposeData(response.CLUBdic,loading,options);
        }).catch(() => {
          loading.close()
        });
      }else if(myPath=='EPC'){
        console.log("运行至EPC");
          EPC(this.$route.params).then(response => {
          _that.clusterStr = response.EPCStr
          _that.handlerResposeData(response.EPCdic,loading,options);
        }).catch(() => {
          loading.close()
        });
      }

    },
    
    handlerResposeData(response,loading,options){
      const {option,option2,option3,option4,option5,option6} = {...options}
      const _that = this;
      console.log("@response",response)
        this.count = Object.keys.length;
        const allData = [[],[],[],[],[],[]]
        Object.keys(response).map((key,item)=>{
          const list = response[key]
          _that.dataList.push(list);
          console.log('@list',list)
          const arr1 = []
          const arr2 = []
          const arr3 = []
          const arr4 = []
          const arr5 = []
          const arr6 = []
          list.forEach(x=>{
            arr1.push(x.pm25)
            arr2.push(x.pm10)
            arr3.push(x.so2)
            arr4.push(x.no2)
            arr5.push(x.co)
            arr6.push(x.o3)
          });
          allData[0].push(arr1)
          allData[1].push(arr2)
          allData[2].push(arr3)
          allData[3].push(arr4)
          allData[4].push(arr5)
          allData[5].push(arr6)
        });
        loading.close();
        _that.allData = allData;
        console.log('@allData',allData)
        //显示
        for(let i=0;i<6;i++){
          const myChart = echarts.init(document.getElementById(`main${i+1}`));
          let o;
          switch(i){
            case 0: o = {...option};
            // console.log('@pm2.5',allData[i])
            break;
            case 1: o = {...option2};break;
            case 2: o = {...option3};break;
            case 3: o = {...option4};break;
            case 4: o = {...option5};break;
            case 5: o = {...option6};break;
          }
          o.dataset[0].source = allData[i];
          const myChart2 = echarts.init(document.getElementById(`mainchart${i+1}`));
          switch(i){
            case 0: _that.initCharts_pm25(allData[i],myChart2);break;
            case 1: _that.initCharts_pm10(allData[i],myChart2);break;
            case 2: _that.initCharts_so2(allData[i],myChart2);break;
            case 3: _that.initCharts_no2(allData[i],myChart2);break;
            case 4: _that.initCharts_co(allData[i],myChart2);break;
            case 5: _that.initCharts_o3(allData[i],myChart2);break;
          }
          // console.log('@option',o)
          o && myChart.setOption(o);
          myChart.on('click', function(params) {
            // console.log('@boxdiagram-medians',medians)
            const id = params.seriesId;
            const name = params.name;
            let myData;
            const myType = parseInt(name.replace("簇 ",''));
            let myTitle;
            switch(id){
              case 'pm2.5':myData = _that.allData[0][myType];myTitle='PM2.5';break;
              case 'pm10':myData = _that.allData[1][myType];myTitle='PM10';break;
              case 'so2':myData = _that.allData[2][myType];myTitle='SO2';break;
              case 'no2':myData = _that.allData[3][myType];myTitle='NO2';break;
              case 'co':myData = _that.allData[4][myType];myTitle='CO';break;
              case 'o3':myData = _that.allData[5][myType];myTitle='O3';break;
            }
            const filterList = _that.dataList[myType];
            const showData = allData[myType];
            //获取对应的数据，跳转对应的页面
            // console.log('@myData',myData)
            // console.log('@filterList',filterList)
            _that.$router.push({name:'BoxDiagramDetail',params:{showData,myData,filterList,myTitle,median:_that.median,title:_that.$route.params.title,path:_that.$route.params.path}})
          });
        }
    },
    formatter(param){
        this.median = param.data[3]
        return [
            'Experiment ' + param.name + ': ',
            'upper: ' + param.data[5],
            'Q3: ' + param.data[4],
            'median: ' + param.data[3],
            'Q1: ' + param.data[2],
            'lower: ' + param.data[1]
        ].join('<br/>');
    },
    initCharts_pm25(dataList,myEchart){
      console.log('@聚类',dataList)
      const legendData = []
      const seriesData = []
      const xAxisData = []
      let maxVal = 0;
      dataList.forEach((item,index)=>{
          const name = '簇'+index;
          legendData.push(name)
          seriesData.push( {
              name: name,
              type: 'line',
              stack: 'Total',
              data: item
          })
          maxVal = item.length>maxVal?item.length:maxVal
      });
      for(let i=0;i<maxVal;i++){
        xAxisData.push(i+1)
      }
      const option = {
          title: {
            text: 'PM25'
          },
          tooltip: {
            trigger: 'axis'
          },
          legend: {
            data: legendData
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
          },
          toolbox: {
            feature: {
              saveAsImage: {}
            }
          },
          xAxis: {
            type: 'category',
            boundaryGap: false,
            data: xAxisData
          },
          yAxis: {
            type: 'value'
          },
          series: seriesData
      };
      myEchart.setOption(option)
    },
    initCharts_pm10(dataList,myEchart){
      console.log('@聚类',dataList)
      const legendData = []
      const seriesData = []
      const xAxisData = []
      let maxVal = 0;
      dataList.forEach((item,index)=>{
          const name = '簇'+index;
          legendData.push(name)
          seriesData.push( {
              name: name,
              type: 'line',
              stack: 'Total',
              data: item
          })
          maxVal = item.length>maxVal?item.length:maxVal
      });
      for(let i=0;i<maxVal;i++){
        xAxisData.push(i+1)
      }
      const option = {
          title: {
            text: 'PM10'
          },
          tooltip: {
            trigger: 'axis'
          },
          legend: {
            data: legendData
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
          },
          toolbox: {
            feature: {
              saveAsImage: {}
            }
          },
          xAxis: {
            type: 'category',
            boundaryGap: false,
            data: xAxisData
          },
          yAxis: {
            type: 'value'
          },
          series: seriesData
      };
      myEchart.setOption(option)
    },
    initCharts_so2(dataList,myEchart){
      console.log('@聚类',dataList)
      const legendData = []
      const seriesData = []
      const xAxisData = []
      let maxVal = 0;
      dataList.forEach((item,index)=>{
          const name = '簇'+index;
          legendData.push(name)
          seriesData.push( {
              name: name,
              type: 'line',
              stack: 'Total',
              data: item
          })
          maxVal = item.length>maxVal?item.length:maxVal
      });
      for(let i=0;i<maxVal;i++){
        xAxisData.push(i+1)
      }
      const option = {
          title: {
            text: 'SO2'
          },
          tooltip: {
            trigger: 'axis'
          },
          legend: {
            data: legendData
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
          },
          toolbox: {
            feature: {
              saveAsImage: {}
            }
          },
          xAxis: {
            type: 'category',
            boundaryGap: false,
            data: xAxisData
          },
          yAxis: {
            type: 'value'
          },
          series: seriesData
      };
      myEchart.setOption(option)
    },
    initCharts_no2(dataList,myEchart){
      console.log('@聚类',dataList)
      const legendData = []
      const seriesData = []
      const xAxisData = []
      let maxVal = 0;
      dataList.forEach((item,index)=>{
          const name = '簇'+index;
          legendData.push(name)
          seriesData.push( {
              name: name,
              type: 'line',
              stack: 'Total',
              data: item
          })
          maxVal = item.length>maxVal?item.length:maxVal
      });
      for(let i=0;i<maxVal;i++){
        xAxisData.push(i+1)
      }
      const option = {
          title: {
            text: 'NO2'
          },
          tooltip: {
            trigger: 'axis'
          },
          legend: {
            data: legendData
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
          },
          toolbox: {
            feature: {
              saveAsImage: {}
            }
          },
          xAxis: {
            type: 'category',
            boundaryGap: false,
            data: xAxisData
          },
          yAxis: {
            type: 'value'
          },
          series: seriesData
      };
      myEchart.setOption(option)
    },
    initCharts_co(dataList,myEchart){
      console.log('@聚类',dataList)
      const legendData = []
      const seriesData = []
      const xAxisData = []
      let maxVal = 0;
      dataList.forEach((item,index)=>{
          const name = '簇'+index;
          legendData.push(name)
          seriesData.push( {
              name: name,
              type: 'line',
              stack: 'Total',
              data: item
          })
          maxVal = item.length>maxVal?item.length:maxVal
      });
      for(let i=0;i<maxVal;i++){
        xAxisData.push(i+1)
      }
      const option = {
          title: {
            text: 'CO'
          },
          tooltip: {
            trigger: 'axis'
          },
          legend: {
            data: legendData
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
          },
          toolbox: {
            feature: {
              saveAsImage: {}
            }
          },
          xAxis: {
            type: 'category',
            boundaryGap: false,
            data: xAxisData
          },
          yAxis: {
            type: 'value'
          },
          series: seriesData
      };
      myEchart.setOption(option)
    },
    initCharts_o3(dataList,myEchart){
      console.log('@聚类',dataList)
      const legendData = []
      const seriesData = []
      const xAxisData = []
      let maxVal = 0;
      dataList.forEach((item,index)=>{
          const name = '簇'+index;
          legendData.push(name)
          seriesData.push( {
              name: name,
              type: 'line',
              stack: 'Total',
              data: item
          })
          maxVal = item.length>maxVal?item.length:maxVal
      });
      for(let i=0;i<maxVal;i++){
        xAxisData.push(i+1)
      }
      const option = {
          title: {
            text: 'O3'
          },
          tooltip: {
            trigger: 'axis'
          },
          legend: {
            data: legendData
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
          },
          toolbox: {
            feature: {
              saveAsImage: {}
            }
          },
          xAxis: {
            type: 'category',
            boundaryGap: false,
            data: xAxisData
          },
          yAxis: {
            type: 'value'
          },
          series: seriesData
      };
      myEchart.setOption(option)
    },
  }
};
</script>

<style scoped>
.app-container{
    height: 100%;
    /* border: 1px solid red; */
    display: flex;
    flex-direction: row;
    flex-wrap: wrap;
    justify-content: space-around;
}

.tu{
  width: 600px;
  height: 500px;
  border: 1px solid #ddd;
  margin-bottom: 30px;
  
}

.tu2{
  width: 1300px;
  height: 500px;
  margin: auto;
  border: 1px solid #ddd;
  margin-bottom: 20px;
}
</style>
