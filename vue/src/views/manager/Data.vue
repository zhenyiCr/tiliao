<template>
  <div>
    <div class="card" style="margin: 10px 2px; padding: 20px">
      <div id="line" style="height: 400px"></div>
    </div>
    <div style="margin: 10px 2px">
      <el-row :gutter="10">
        <el-col :span="12">
          <div style="height: 400px; padding: 20px" class="card" id="pie"></div>
        </el-col>
        <el-col :span="12" >
          <div style="height: 400px; padding: 20px" class="card" id="bar"></div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { reactive, onMounted } from "vue";
import request from "@/utils/request.js";
import * as echarts from 'echarts'

const lineOption = {
  title: {
    text: '预约趋势图',
    left: 'center'
  },
  tooltip: {
    trigger: 'axis'
  },
  legend: {
    left: 'left'
  },
  xAxis: {
    type: 'category',
    data: []
  },
  yAxis: {
    type: 'value'
  },
  series: [
    {
      data: [],
      type: 'line',
      smooth: true
    },
  ]
}

const pieOption = {
  title: {
    text: '不同职称医生分布统计',
    left: 'center'
  },
  tooltip: {
    trigger: 'item'
  },
  legend: {
    orient: 'vertical',
    left: 'left'
  },
  series: [
    {
      type: 'pie',
      center: ['50%', '60%'],
      radius: '50%',
      data: [],
      label: {
        show: true,
        formatter(param) {
          return param.name + param.value + '个 (' + param.percent + '%)';
        }
      },
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
      }
    }
  ]
}

const barOption = {
  title: {
    text: '不同科室医生数量统计',
    left: 'center'
  },
  tooltip: {
    trigger: 'axis'
  },
  legend: {
    left: 'left'
  },
  grid: {
    left: '15%',
    bottom:'10%'
  },
  xAxis: {
    type: 'category',
    data: [],
    axisLabel: {
      interval:0,
      rotate:45
    }
  },
  yAxis: {
    type: 'value'
  },
  series: [
    {
      data: [],
      type: 'bar',
      smooth: true,
      itemStyle: {
        normal: {
          color: function(params) { // 根据索引返回对应的颜色
            let colorList = ['#ffaa2e','#32C5E9','#fa4c4c','#08b448','#FFDB5C','#ff9f7f','#fb7293','#E062AE','#E690D1','#e7bcf3']
            return colorList[params.dataIndex];
          }
        }
      },
    }
  ]
}

// const data = reactive({
//   countData: {}
// })
//
// request.get('/getCountData').then(res => {
//   data.countData = res.data
// })

// 等页面所有元素加载完成后再设置 echarts图表
onMounted(() => {
  // 请求数据
  // 初始化图表
  // 折线图
  let lineDom = document.getElementById('line');
  let lineChart = echarts.init(lineDom);

  request.get('/lineData').then(res => {
    lineOption.xAxis.data = res.data.dateList
    lineOption.series[0].data = res.data.moneyList
    lineChart.setOption(lineOption)
  })

  // 饼图
  let pieDom = document.getElementById('pie');
  let pieChart = echarts.init(pieDom);

  request.get('/pieData').then(res => {
    pieOption.series[0].data = res.data
    pieChart.setOption(pieOption)
  })

  // 柱状图
  let barDom = document.getElementById('bar');
  let barChart = echarts.init(barDom);

  request.get('/barData').then(res => {
    barOption.xAxis.data = res.data.departmentList
    barOption.series[0].data = res.data.countList
    barChart.setOption(barOption)
  })
})
</script>