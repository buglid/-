<template>
  <div class="app-container" v-loading="loading">
    <div class="tu" v-for="n in 6" :key="10000+n" :id="`main${n}`"></div>
  </div>
</template>

<script>
import { cluster } from "@/api/system/pollution";
import * as echarts from 'echarts';
export default {
  name: "Pollution",
  data() {
    return {
      // 遮罩层
      loading: false,
    };
  },
  created() {
    if(this.$route.params.num){
      this.getList();
    }else{
      this.$router.push({path: 'lrr', replace: true})
    }
  
  },
  methods: {
    getList() {
      const option = {
          title: [
            {
              text: 'PM2.5',
              left: 'center'
            },
            // {
            //   text: 'upper: Q3 + 1.5 * IQR \nlower: Q1 - 1.5 * IQR',
            //   borderColor: '#999',
            //   borderWidth: 1,
            //   textStyle: {
            //     fontWeight: 'normal',
            //     fontSize: 14,
            //     lineHeight: 20
            //   },
            //   left: '10%',
            //   top: '90%'
            // }
          ],
          dataset: [
            {
              // prettier-ignore
              // source: [
              //           [850, 740, 900, 1070, 930, 850, 950, 980, 980, 880, 1000, 980, 930, 650, 760, 810, 1000, 1000, 960, 960],
              //           [960, 940, 960, 940, 880, 800, 850, 880, 900, 840, 830, 790, 810, 880, 880, 830, 800, 790, 760, 800],
              //           [880, 880, 880, 860, 720, 720, 620, 860, 970, 950, 880, 910, 850, 870, 840, 840, 850, 840, 840, 840],
              //           [890, 810, 810, 820, 800, 770, 760, 740, 750, 760, 910, 920, 890, 860, 880, 720, 840, 850, 850, 780],
              //           [890, 840, 780, 810, 760, 810, 790, 810, 820, 850, 870, 870, 810, 740, 810, 940, 950, 800, 810, 870]
              //       ]
              source: []
            },
            {
              transform: {
                type: 'boxplot',
                config: { itemNameFormatter: '簇 {value}' }
              }
            },
            {
              fromDatasetIndex: 1,
              fromTransformResult: 1
            }
          ],
          tooltip: {
            trigger: 'item',
            axisPointer: {
              type: 'shadow'
            }
          },
          grid: {
            left: '10%',
            right: '10%',
            bottom: '15%'
          },
          xAxis: {
            type: 'category',
            boundaryGap: true,
            nameGap: 30,
            splitArea: {
              show: false
            },
            splitLine: {
              show: false
            }
          },
          yAxis: {
            type: 'value',
            name: '',
            splitArea: {
              show: true
            }
          },
          series: [
            {
              name: 'boxplot',
              type: 'boxplot',
              datasetIndex: 1
            },
            {
              name: 'outlier',
              type: 'scatter',
              datasetIndex: 2
            }
          ]
      };
      const option2= {
          title: [
            {
              text: 'PM10',
              left: 'center'
            },
            // {
            //   text: 'upper: Q3 + 1.5 * IQR \nlower: Q1 - 1.5 * IQR',
            //   borderColor: '#999',
            //   borderWidth: 1,
            //   textStyle: {
            //     fontWeight: 'normal',
            //     fontSize: 14,
            //     lineHeight: 20
            //   },
            //   left: '10%',
            //   top: '90%'
            // }
          ],
          dataset: [
            {
              // prettier-ignore
              // source: [
              //           [850, 740, 900, 1070, 930, 850, 950, 980, 980, 880, 1000, 980, 930, 650, 760, 810, 1000, 1000, 960, 960],
              //           [960, 940, 960, 940, 880, 800, 850, 880, 900, 840, 830, 790, 810, 880, 880, 830, 800, 790, 760, 800],
              //           [880, 880, 880, 860, 720, 720, 620, 860, 970, 950, 880, 910, 850, 870, 840, 840, 850, 840, 840, 840],
              //           [890, 810, 810, 820, 800, 770, 760, 740, 750, 760, 910, 920, 890, 860, 880, 720, 840, 850, 850, 780],
              //           [890, 840, 780, 810, 760, 810, 790, 810, 820, 850, 870, 870, 810, 740, 810, 940, 950, 800, 810, 870]
              //       ]
              source: []
            },
            {
              transform: {
                type: 'boxplot',
                config: { itemNameFormatter: '簇 {value}' }
              }
            },
            {
              fromDatasetIndex: 1,
              fromTransformResult: 1
            }
          ],
          tooltip: {
            trigger: 'item',
            axisPointer: {
              type: 'shadow'
            }
          },
          grid: {
            left: '10%',
            right: '10%',
            bottom: '15%'
          },
          xAxis: {
            type: 'category',
            boundaryGap: true,
            nameGap: 30,
            splitArea: {
              show: false
            },
            splitLine: {
              show: false
            }
          },
          yAxis: {
            type: 'value',
            name: '',
            splitArea: {
              show: true
            }
          },
          series: [
            {
              name: 'boxplot',
              type: 'boxplot',
              datasetIndex: 1
            },
            {
              name: 'outlier',
              type: 'scatter',
              datasetIndex: 2
            }
          ]
      };
      const option3 = {
          title: [
            {
              text: 'SO2',
              left: 'center'
            },
            // {
            //   text: 'upper: Q3 + 1.5 * IQR \nlower: Q1 - 1.5 * IQR',
            //   borderColor: '#999',
            //   borderWidth: 1,
            //   textStyle: {
            //     fontWeight: 'normal',
            //     fontSize: 14,
            //     lineHeight: 20
            //   },
            //   left: '10%',
            //   top: '90%'
            // }
          ],
          dataset: [
            {
              // prettier-ignore
              // source: [
              //           [850, 740, 900, 1070, 930, 850, 950, 980, 980, 880, 1000, 980, 930, 650, 760, 810, 1000, 1000, 960, 960],
              //           [960, 940, 960, 940, 880, 800, 850, 880, 900, 840, 830, 790, 810, 880, 880, 830, 800, 790, 760, 800],
              //           [880, 880, 880, 860, 720, 720, 620, 860, 970, 950, 880, 910, 850, 870, 840, 840, 850, 840, 840, 840],
              //           [890, 810, 810, 820, 800, 770, 760, 740, 750, 760, 910, 920, 890, 860, 880, 720, 840, 850, 850, 780],
              //           [890, 840, 780, 810, 760, 810, 790, 810, 820, 850, 870, 870, 810, 740, 810, 940, 950, 800, 810, 870]
              //       ]
              source: []
            },
            {
              transform: {
                type: 'boxplot',
                config: { itemNameFormatter: '簇 {value}' }
              }
            },
            {
              fromDatasetIndex: 1,
              fromTransformResult: 1
            }
          ],
          tooltip: {
            trigger: 'item',
            axisPointer: {
              type: 'shadow'
            }
          },
          grid: {
            left: '10%',
            right: '10%',
            bottom: '15%'
          },
          xAxis: {
            type: 'category',
            boundaryGap: true,
            nameGap: 30,
            splitArea: {
              show: false
            },
            splitLine: {
              show: false
            }
          },
          yAxis: {
            type: 'value',
            name: '',
            splitArea: {
              show: true
            }
          },
          series: [
            {
              name: 'boxplot',
              type: 'boxplot',
              datasetIndex: 1
            },
            {
              name: 'outlier',
              type: 'scatter',
              datasetIndex: 2
            }
          ]
      };
      const option4 = {
          title: [
            {
              text: 'NO2',
              left: 'center'
            },
            // {
            //   text: 'upper: Q3 + 1.5 * IQR \nlower: Q1 - 1.5 * IQR',
            //   borderColor: '#999',
            //   borderWidth: 1,
            //   textStyle: {
            //     fontWeight: 'normal',
            //     fontSize: 14,
            //     lineHeight: 20
            //   },
            //   left: '10%',
            //   top: '90%'
            // }
          ],
          dataset: [
            {
              // prettier-ignore
              // source: [
              //           [850, 740, 900, 1070, 930, 850, 950, 980, 980, 880, 1000, 980, 930, 650, 760, 810, 1000, 1000, 960, 960],
              //           [960, 940, 960, 940, 880, 800, 850, 880, 900, 840, 830, 790, 810, 880, 880, 830, 800, 790, 760, 800],
              //           [880, 880, 880, 860, 720, 720, 620, 860, 970, 950, 880, 910, 850, 870, 840, 840, 850, 840, 840, 840],
              //           [890, 810, 810, 820, 800, 770, 760, 740, 750, 760, 910, 920, 890, 860, 880, 720, 840, 850, 850, 780],
              //           [890, 840, 780, 810, 760, 810, 790, 810, 820, 850, 870, 870, 810, 740, 810, 940, 950, 800, 810, 870]
              //       ]
              source: []
            },
            {
              transform: {
                type: 'boxplot',
                config: { itemNameFormatter: '簇 {value}' }
              }
            },
            {
              fromDatasetIndex: 1,
              fromTransformResult: 1
            }
          ],
          tooltip: {
            trigger: 'item',
            axisPointer: {
              type: 'shadow'
            }
          },
          grid: {
            left: '10%',
            right: '10%',
            bottom: '15%'
          },
          xAxis: {
            type: 'category',
            boundaryGap: true,
            nameGap: 30,
            splitArea: {
              show: false
            },
            splitLine: {
              show: false
            }
          },
          yAxis: {
            type: 'value',
            name: '',
            splitArea: {
              show: true
            }
          },
          series: [
            {
              name: 'boxplot',
              type: 'boxplot',
              datasetIndex: 1
            },
            {
              name: 'outlier',
              type: 'scatter',
              datasetIndex: 2
            }
          ]
      };
      const option5 = {
          title: [
            {
              text: 'CO',
              left: 'center'
            },
            // {
            //   text: 'upper: Q3 + 1.5 * IQR \nlower: Q1 - 1.5 * IQR',
            //   borderColor: '#999',
            //   borderWidth: 1,
            //   textStyle: {
            //     fontWeight: 'normal',
            //     fontSize: 14,
            //     lineHeight: 20
            //   },
            //   left: '10%',
            //   top: '90%'
            // }
          ],
          dataset: [
            {
              // prettier-ignore
              // source: [
              //           [850, 740, 900, 1070, 930, 850, 950, 980, 980, 880, 1000, 980, 930, 650, 760, 810, 1000, 1000, 960, 960],
              //           [960, 940, 960, 940, 880, 800, 850, 880, 900, 840, 830, 790, 810, 880, 880, 830, 800, 790, 760, 800],
              //           [880, 880, 880, 860, 720, 720, 620, 860, 970, 950, 880, 910, 850, 870, 840, 840, 850, 840, 840, 840],
              //           [890, 810, 810, 820, 800, 770, 760, 740, 750, 760, 910, 920, 890, 860, 880, 720, 840, 850, 850, 780],
              //           [890, 840, 780, 810, 760, 810, 790, 810, 820, 850, 870, 870, 810, 740, 810, 940, 950, 800, 810, 870]
              //       ]
              source: []
            },
            {
              transform: {
                type: 'boxplot',
                config: { itemNameFormatter: '簇 {value}' }
              }
            },
            {
              fromDatasetIndex: 1,
              fromTransformResult: 1
            }
          ],
          tooltip: {
            trigger: 'item',
            axisPointer: {
              type: 'shadow'
            }
          },
          grid: {
            left: '10%',
            right: '10%',
            bottom: '15%'
          },
          xAxis: {
            type: 'category',
            boundaryGap: true,
            nameGap: 30,
            splitArea: {
              show: false
            },
            splitLine: {
              show: false
            }
          },
          yAxis: {
            type: 'value',
            name: '',
            splitArea: {
              show: true
            }
          },
          series: [
            {
              name: 'boxplot',
              type: 'boxplot',
              datasetIndex: 1
            },
            {
              name: 'outlier',
              type: 'scatter',
              datasetIndex: 2
            }
          ]
      };
      const option6 = {
          title: [
            {
              text: 'O3',
              left: 'center'
            },
            // {
            //   text: 'upper: Q3 + 1.5 * IQR \nlower: Q1 - 1.5 * IQR',
            //   borderColor: '#999',
            //   borderWidth: 1,
            //   textStyle: {
            //     fontWeight: 'normal',
            //     fontSize: 14,
            //     lineHeight: 20
            //   },
            //   left: '10%',
            //   top: '90%'
            // }
          ],
          dataset: [
            {
              // prettier-ignore
              // source: [
              //           [850, 740, 900, 1070, 930, 850, 950, 980, 980, 880, 1000, 980, 930, 650, 760, 810, 1000, 1000, 960, 960],
              //           [960, 940, 960, 940, 880, 800, 850, 880, 900, 840, 830, 790, 810, 880, 880, 830, 800, 790, 760, 800],
              //           [880, 880, 880, 860, 720, 720, 620, 860, 970, 950, 880, 910, 850, 870, 840, 840, 850, 840, 840, 840],
              //           [890, 810, 810, 820, 800, 770, 760, 740, 750, 760, 910, 920, 890, 860, 880, 720, 840, 850, 850, 780],
              //           [890, 840, 780, 810, 760, 810, 790, 810, 820, 850, 870, 870, 810, 740, 810, 940, 950, 800, 810, 870]
              //       ]
              source: []
            },
            {
              transform: {
                type: 'boxplot',
                config: { itemNameFormatter: 'expr {value}' }
              }
            },
            {
              fromDatasetIndex: 1,
              fromTransformResult: 1
            }
          ],
          tooltip: {
            trigger: 'item',
            axisPointer: {
              type: 'shadow'
            }
          },
          grid: {
            left: '10%',
            right: '10%',
            bottom: '15%'
          },
          xAxis: {
            type: 'category',
            boundaryGap: true,
            nameGap: 30,
            splitArea: {
              show: false
            },
            splitLine: {
              show: false
            }
          },
          yAxis: {
            type: 'value',
            name: '',
            splitArea: {
              show: true
            }
          },
          series: [
            {
              name: 'boxplot',
              type: 'boxplot',
              datasetIndex: 1
            },
            {
              name: 'outlier',
              type: 'scatter',
              datasetIndex: 2
            }
          ]
      };
      const loading = this.$loading({
          lock: true,
          text: 'Loading',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
      });
      cluster(this.$route.params).then(response => {
        console.log("@response",response)
        this.count = Object.keys.length;
        const allData = [[],[],[],[],[],[]]
        Object.keys(response).map((key,item)=>{
          const list = response[key]
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
        console.log('@allData',allData)
        //显示
        for(let i=0;i<6;i++){
          const myChart = echarts.init(document.getElementById(`main${i+1}`));
          let o;
          switch(i){
            case 0: o = {...option};break;
            case 1: o = {...option2};break;
            case 2: o = {...option3};break;
            case 3: o = {...option4};break;
            case 4: o = {...option5};break;
            case 5: o = {...option6};break;
          }
          o.dataset[0].source = allData[i];
          console.log('@option',o)
          o && myChart.setOption(o);
        }
      });
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
</style>
