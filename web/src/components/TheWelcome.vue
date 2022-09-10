<template>
    <a-row>
        <a-col :span="24">
            <a-card>
                <a-row>
                    <a-col :span="8">
                        <a-statistic title="文档总阅读量" :value="statistic.viewCount">
                            <template #suffix>
                                <read-outlined/>
                            </template>
                        </a-statistic>
                    </a-col>
                    <a-col :span="8">
                        <a-statistic title="文档总点赞量" :value="statistic.voteCount">
                            <template #suffix>
                                <like-outlined/>
                            </template>
                        </a-statistic>
                    </a-col>

                    <a-col :span="8">
                        <a-statistic title="点赞率"
                                     :value="statistic.voteCount / statistic.viewCount * 100"
                                     :precision="2"
                                     suffix="%"
                                     :value-style="{ color: '#ffd427' }">
                        </a-statistic>
                    </a-col>
                </a-row>
            </a-card>
        </a-col>
    </a-row>

    <br>

    <a-row :gutter="24">
        <a-col :span="12">
            <a-card>
                <a-row>
                    <a-col :span="12">
                        <a-statistic title="今日阅读" :value="statistic.todayViewCount">
                            <template #suffix>
                                <book-outlined/>
                            </template>
                        </a-statistic>
                    </a-col>
                    <a-col :span="12">
                        <a-statistic title="今日点赞" :value="statistic.todayVoteCount">
                            <template #suffix>
                                <like-outlined/>
                            </template>
                        </a-statistic>
                    </a-col>
                </a-row>
            </a-card>
        </a-col>

        <a-col :span="12">
            <a-card>
                <a-row>
                    <a-col :span="12">
                        <a-statistic title="预计今日阅读"
                                     :value="statistic.todayforcastCount"
                                     :value-style="{color: '#4e25ff'}">
                            <template #suffix>
                                <book-outlined/>
                            </template>
                        </a-statistic>
                    </a-col>
                    <a-col :span="12">
                        <a-statistic title="预计今日阅读增长"
                                     :value="statistic.todayforcastRateAbs"
                                     :precision="2"
                                     suffix="%"
                                     :value-style="statistic.todayforcastRate > 0 ? {color: '#ffd427'}: {color: '#ff293b'}">
                            <template #prefix>
                                <arrow-up-outlined v-if="statistic.todayforcastRate >= 0"/>
                                <arrow-down-outlined v-if="statistic.todayforcastRate < 0"/>
                            </template>
                        </a-statistic>
                    </a-col>
                </a-row>
            </a-card>
        </a-col>
    </a-row>

    <br>

    <a-row>
        <a-col :span="24">
            <!-- 为 ECharts 准备一个定义了宽高的 DOM -->
            <div id="main" style="width:100%; height:300px;"></div>
        </a-col>
    </a-row>

</template>

<script lang="ts">
    import {defineComponent, ref, onMounted} from 'vue';
    import axios from "axios";

    declare let echarts: any;

    export default defineComponent({
        name: 'the-welcome',
        setup() {
            const statistic = ref();
            statistic.value = {};
            const getStatistic = () => {
                axios.get("/ebook-snapshot/get-statistic").then((response) => {
                    const data = response.data;
                    if (data.success) {
                        const statisticResp = data.content;
                        // 今日快照表的总观看数
                        statistic.value.viewCount = statisticResp[1].viewCount;
                        statistic.value.voteCount = statisticResp[1].voteCount;
                        // 数据库increase字段就是今天总历史阅读量(view_count)-昨天总阅读量，所以今天阅读量=今天增长的阅读量
                        statistic.value.todayViewCount = statisticResp[1].viewIncrease;
                        statistic.value.todayVoteCount = statisticResp[1].voteIncrease;

                        const now = new Date();
                        const nowRate = (now.getHours() * 60 + now.getMinutes()) / (60 * 24);
                        // console.log(nowRate);
                        // todayforcastCount: 今天预计阅读量 = 目前阅读量 / 当前时间点占全天
                        statistic.value.todayforcastCount = parseInt(String(statistic.value.todayViewCount / nowRate));

                        // todayforcastRate: 今天预计阅读率 = (今天阅读量 - 昨天阅读量) / 昨天阅读量
                        statistic.value.todayforcastRate =
                            (statistic.value.todayforcastCount - statisticResp[0].viewIncrease) / statisticResp[0].viewIncrease * 100;
                        statistic.value.todayforcastRateAbs =
                            Math.abs(statistic.value.todayforcastRate);
                    }
                })
            };

            const demo = () => {
                // 基于准备好的dom，初始化echarts实例
                var myChart = echarts.init(document.getElementById('main'));

                // 绘制图表
                myChart.setOption({
                    title: {
                        text: 'ECharts 入门示例'
                    },
                    tooltip: {},
                    xAxis: {
                        data: ['衬衫', '羊毛衫', '雪纺衫', '裤子', '高跟鞋', '袜子']
                    },
                    yAxis: {},
                    series: [
                        {
                            name: '销量',
                            type: 'bar',
                            data: [5, 20, 36, 10, 10, 20]
                        }
                    ]
                });
            };

            onMounted(() => {
                getStatistic();
                demo();
            });

            return {
                statistic
            }
        },
    });
</script>
