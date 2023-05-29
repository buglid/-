export const hetu = {
    title: [
        {
          text: '',
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
      // legend: {
      // //  name:'pm2.5'
      // },
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
            config: { itemNameFormatter: '簇 {value}'}
          }
        },
        {
          fromDatasetIndex: 1,
          fromTransformResult: 1,
        }
      ],
      tooltip: {
        trigger: 'item',
        axisPointer: {
          type: 'shadow',
        },
        formatter(param){
            return [
                'Experiment ' + param.name + ': ',
                'upper: ' + param.data[5],
                'Q3: ' + param.data[4],
                'median: ' + param.data[3],
                'Q1: ' + param.data[2],
                'lower: ' + param.data[1]
            ].join('<br/>');
        },
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
          show: false,
          lineStyle: {
            color: "rgba(0,205,225,0.25)",
          },
        },
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
          id:'',
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
}


