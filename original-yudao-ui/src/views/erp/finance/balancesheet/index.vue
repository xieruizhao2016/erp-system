<template>
  <ContentWrap>
    <DocAlert title="【财务】资产负债表" url="https://doc.iocoder.cn/erp/finance-balance-sheet/" />

    <!-- 顶部3个卡片：资产总额、负债总额、所有者权益 -->
    <el-row :gutter="16" class="mb-4">
      <el-col :md="8" :sm="24" :xs="24">
        <div class="flex flex-col gap-2 bg-[var(--el-bg-color-overlay)] p-6 rounded">
          <div class="flex items-center justify-between text-gray-500">
            <span>资产总额</span>
          </div>
          <div class="flex flex-row items-baseline justify-between">
            <CountTo
              prefix="￥"
              :end-val="statistics?.assetTotal || 0"
              :decimals="2"
              :duration="500"
              class="text-3xl text-blue-600"
            />
          </div>
        </div>
      </el-col>
      <el-col :md="8" :sm="24" :xs="24">
        <div class="flex flex-col gap-2 bg-[var(--el-bg-color-overlay)] p-6 rounded">
          <div class="flex items-center justify-between text-gray-500">
            <span>负债总额</span>
          </div>
          <div class="flex flex-row items-baseline justify-between">
            <CountTo
              prefix="￥"
              :end-val="statistics?.liabilityTotal || 0"
              :decimals="2"
              :duration="500"
              class="text-3xl text-orange-600"
            />
          </div>
        </div>
      </el-col>
      <el-col :md="8" :sm="24" :xs="24">
        <div class="flex flex-col gap-2 bg-[var(--el-bg-color-overlay)] p-6 rounded">
          <div class="flex items-center justify-between text-gray-500">
            <span>所有者权益</span>
          </div>
          <div class="flex flex-row items-baseline justify-between">
            <CountTo
              prefix="￥"
              :end-val="statistics?.equityTotal || 0"
              :decimals="2"
              :duration="500"
              class="text-3xl text-green-600"
            />
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 第二行：两个饼状图 -->
    <el-row :gutter="16" class="mb-4">
      <el-col :md="12" :sm="24" :xs="24">
        <el-card shadow="never" v-loading="loading">
          <template #header>
            <CardTitle title="资产构成" />
          </template>
          <Echart :height="400" :options="assetPieChartOptions" />
        </el-card>
      </el-col>
      <el-col :md="12" :sm="24" :xs="24">
        <el-card shadow="never" v-loading="loading">
          <template #header>
            <CardTitle title="负债构成" />
          </template>
          <Echart :height="400" :options="liabilityPieChartOptions" />
        </el-card>
      </el-col>
    </el-row>

    <!-- 第三行：曲线图 -->
    <el-row :gutter="16" class="mb-4">
      <el-col :span="24">
        <el-card shadow="never" v-loading="loading">
          <template #header>
            <CardTitle title="资产和负债趋势（按月）" />
          </template>
          <Echart :height="400" :options="trendLineChartOptions" />
        </el-card>
      </el-col>
    </el-row>
  </ContentWrap>
</template>

<script setup lang="ts">
import { EChartsOption } from 'echarts'
import { CardTitle } from '@/components/Card'
import { BalanceSheetApi, BalanceSheetStatisticsVO } from '@/api/erp/finance/balancesheet'
import { useMessage } from '@/hooks/web/useMessage'

/** ERP 资产负债表 列表 */
defineOptions({ name: 'ErpFinanceBalanceSheet' })

const message = useMessage()
const loading = ref(true) // 加载中
const statistics = ref<BalanceSheetStatisticsVO>() // 统计数据

/** 资产饼状图配置 */
const assetPieChartOptions = reactive<EChartsOption>({
  tooltip: {
    trigger: 'item',
    formatter: '{a} <br/>{b}: ￥{c} ({d}%)'
  },
  legend: {
    orient: 'vertical',
    left: 'left',
    top: 'middle'
  },
  series: [
    {
      name: '资产构成',
      type: 'pie',
      radius: ['40%', '70%'],
      avoidLabelOverlap: false,
      itemStyle: {
        borderRadius: 10,
        borderColor: '#fff',
        borderWidth: 2
      },
      label: {
        show: true,
        formatter: '{b}: ￥{c}\n({d}%)'
      },
      emphasis: {
        label: {
          show: true,
          fontSize: 16,
          fontWeight: 'bold'
        }
      },
      data: []
    }
  ]
}) as EChartsOption

/** 负债饼状图配置 */
const liabilityPieChartOptions = reactive<EChartsOption>({
  tooltip: {
    trigger: 'item',
    formatter: '{a} <br/>{b}: ￥{c} ({d}%)'
  },
  legend: {
    orient: 'vertical',
    left: 'left',
    top: 'middle'
  },
  series: [
    {
      name: '负债构成',
      type: 'pie',
      radius: ['40%', '70%'],
      avoidLabelOverlap: false,
      itemStyle: {
        borderRadius: 10,
        borderColor: '#fff',
        borderWidth: 2
      },
      label: {
        show: true,
        formatter: '{b}: ￥{c}\n({d}%)'
      },
      emphasis: {
        label: {
          show: true,
          fontSize: 16,
          fontWeight: 'bold'
        }
      },
      data: []
    }
  ]
}) as EChartsOption

/** 趋势曲线图配置 */
const trendLineChartOptions = reactive<EChartsOption>({
  tooltip: {
    trigger: 'axis',
    axisPointer: {
      type: 'cross'
    },
    formatter: (params: any) => {
      let result = params[0].name + '<br/>'
      params.forEach((item: any) => {
        result += item.marker + item.seriesName + ': ￥' + item.value.toFixed(2) + '<br/>'
      })
      return result
    }
  },
  legend: {
    data: ['资产总额', '负债总额'],
    top: 10
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    containLabel: true
  },
  xAxis: {
    type: 'category',
    boundaryGap: false,
    data: []
  },
  yAxis: {
    type: 'value',
    axisLabel: {
      formatter: (value: number) => {
        return '￥' + (value / 10000).toFixed(0) + '万'
      }
    }
  },
  series: [
    {
      name: '资产总额',
      type: 'line',
      smooth: true,
      data: [],
      itemStyle: {
        color: '#409EFF'
      },
      areaStyle: {
        color: {
          type: 'linear',
          x: 0,
          y: 0,
          x2: 0,
          y2: 1,
          colorStops: [
            { offset: 0, color: 'rgba(64, 158, 255, 0.3)' },
            { offset: 1, color: 'rgba(64, 158, 255, 0.1)' }
          ]
        }
      }
    },
    {
      name: '负债总额',
      type: 'line',
      smooth: true,
      data: [],
      itemStyle: {
        color: '#E6A23C'
      },
      areaStyle: {
        color: {
          type: 'linear',
          x: 0,
          y: 0,
          x2: 0,
          y2: 1,
          colorStops: [
            { offset: 0, color: 'rgba(230, 162, 60, 0.3)' },
            { offset: 1, color: 'rgba(230, 162, 60, 0.1)' }
          ]
        }
      }
    }
  ]
}) as EChartsOption

/** 获取统计数据 */
const getStatistics = async () => {
  loading.value = true
  try {
    statistics.value = await BalanceSheetApi.getBalanceSheetStatistics()
    
    // 更新资产饼状图数据
    if (statistics.value?.assetComposition && statistics.value.assetComposition.length > 0) {
      assetPieChartOptions.series[0].data = statistics.value.assetComposition.map((item) => ({
        value: item.amount,
        name: item.name
      }))
    } else {
      assetPieChartOptions.series[0].data = []
    }
    
    // 更新负债饼状图数据
    if (statistics.value?.liabilityComposition && statistics.value.liabilityComposition.length > 0) {
      liabilityPieChartOptions.series[0].data = statistics.value.liabilityComposition.map((item) => ({
        value: item.amount,
        name: item.name
      }))
    } else {
      liabilityPieChartOptions.series[0].data = []
    }
    
    // 更新趋势曲线图数据
    if (statistics.value?.monthlyTrend && statistics.value.monthlyTrend.length > 0) {
      const months = statistics.value.monthlyTrend.map((item) => item.month)
      const assetData = statistics.value.monthlyTrend.map((item) => item.assetTotal)
      const liabilityData = statistics.value.monthlyTrend.map((item) => item.liabilityTotal)
      
      trendLineChartOptions.xAxis.data = months
      trendLineChartOptions.series[0].data = assetData
      trendLineChartOptions.series[1].data = liabilityData
    } else {
      trendLineChartOptions.xAxis.data = []
      trendLineChartOptions.series[0].data = []
      trendLineChartOptions.series[1].data = []
    }
  } catch (error: any) {
    console.error('获取统计数据失败:', error)
    message.error('获取统计数据失败: ' + (error?.message || '请检查后端服务是否已重启'))
    // 设置默认值
    statistics.value = {
      assetTotal: 0,
      liabilityTotal: 0,
      equityTotal: 0,
      assetComposition: [],
      liabilityComposition: [],
      monthlyTrend: []
    }
  } finally {
    loading.value = false
  }
}

/** 初始化 **/
onMounted(() => {
  getStatistics()
})
</script>

<style lang="scss" scoped>
.rounded {
  border-radius: 4px;
}
</style>
