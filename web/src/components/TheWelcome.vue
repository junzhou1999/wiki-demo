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
        <a-col :span="18">
            <!-- 为 ECharts 准备一个定义了宽高的 DOM -->
            <div id="main" style="width:100%; height:300px;"></div>
        </a-col>
        <a-col :span="6">
            <!-- 为 ECharts 准备一个定义了宽高的 DOM -->
            <div id="main2" style="width:100%; height:300px;"></div>
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
                    const data = response.data;  // 0是今天的，1是昨天的
                    if (data.success) {
                        const statisticResp = data.content;
                        // 今日快照表的总观看数
                        statistic.value.viewCount = statisticResp[0].viewCount;
                        statistic.value.voteCount = statisticResp[0].voteCount;
                        // 数据库increase字段就是今天总历史阅读量(view_count)-昨天总阅读量，所以今天阅读量=今天增长的阅读量
                        statistic.value.todayViewCount = statisticResp[0].viewIncrease;
                        statistic.value.todayVoteCount = statisticResp[0].voteIncrease;

                        const now = new Date();
                        const nowRate = (now.getHours() * 60 + now.getMinutes()) / (60 * 24);
                        // console.log(nowRate);
                        // todayforcastCount: 今天预计阅读量 = 目前阅读量 / 当前时间点占全天
                        statistic.value.todayforcastCount = parseInt(String(statistic.value.todayViewCount / nowRate));

                        if (statisticResp.length == 1 || statisticResp[1].viewIncrease == 0) {
                            statistic.value.todayforcastRate = 100;  // 服务上线或昨没有阅读数，避免取不到昨天的数据
                        } else {
                            // todayforcastRate: 今天预计阅读率 = (今天阅读量 - 昨天阅读量) / 昨天阅读量
                            statistic.value.todayforcastRate =
                                (statistic.value.todayforcastCount - statisticResp[1].viewIncrease) / statisticResp[1].viewIncrease * 100;
                        }
                        statistic.value.todayforcastRateAbs =
                            Math.abs(statistic.value.todayforcastRate);
                    }
                })
            };

            const get30Statistic = () => {
                axios.get("/ebook-snapshot/get-30-statistic").then((response) => {
                    const data = response.data;
                    if (data.success) {
                        init30Echarts(data.content);
                    }
                })
            };

            const init30Echarts = (list: any) => {
                // 基于准备好的dom，初始化echarts实例
                const myChart = echarts.init(document.getElementById('main'));

                const xAxis = [];  // 横轴，30天日期
                const viewSeries = [];  // 每天的阅读量
                const voteSeries = [];  // 每天的点赞量
                for (let i = 0; i < list.length; i++) {
                    const record = list[i];
                    xAxis.push(record.date);
                    viewSeries.push(record.viewIncrease);
                    voteSeries.push(record.voteIncrease);
                }

                const option = {
                    title: {
                        text: '近一个月访问趋势'
                    },
                    tooltip: {
                        trigger: 'axis'
                    },
                    legend: {},
                    toolbox: {
                        show: true,
                        feature: {
                            magicType: {type: ['line', 'bar']},
                            saveAsImage: {}
                        },
                        right: 50
                    },
                    xAxis: {
                        name: '日期',
                        nameLocation: 'middle',
                        nameTextStyle: {padding: 25},
                        verticalAlign: 'bottom',
                        type: 'category',
                        boundaryGap: false,
                        data: xAxis
                    },
                    yAxis: {
                        name: '数量',
                        nameLocation: 'middle',
                        nameGap: 50,
                        nameRotate: 0,
                        type: 'value'
                    },
                    series: [
                        {
                            name: '阅读量',
                            type: 'line',
                            data: viewSeries,
                            markPoint: {
                                data: [
                                    {type: 'max', name: 'Max'},
                                    {type: 'min', name: 'Min'}
                                ]
                            },
                            markLine: {
                                data: [{type: 'average', name: 'Avg'}]
                            }
                        },
                        {
                            name: '点赞量',
                            type: 'line',
                            data: voteSeries,
                            markPoint: {
                                data: [
                                    {type: 'max', name: 'Max'},
                                    {type: 'min', name: 'Min'}
                                ]
                            },
                            markLine: {
                                data: [{type: 'average', name: 'Avg'}]
                            }
                        }
                    ]
                };
                // 绘制图表
                myChart.setOption(option);
            };

            const initClock = () => {
                // 基于准备好的dom，初始化echarts实例
                const myChart = echarts.init(document.getElementById('main2'));

                const option = {
                    animationDurationUpdate: 300,
                    series: [
                        {
                            name: 'hour',
                            type: 'gauge',
                            startAngle: 90,
                            endAngle: -270,
                            min: 0,
                            max: 12,
                            splitNumber: 12,
                            clockwise: true,
                            axisLine: {
                                lineStyle: {
                                    width: 10,
                                    color: [[1, 'rgba(0,0,0,0.7)']],
                                    shadowColor: 'rgba(0, 0, 0, 0.5)',
                                    shadowBlur: 8
                                }
                            },
                            splitLine: {
                                distance: 2,  // 修改分割线间隔
                                lineStyle: {
                                    shadowColor: 'rgba(0, 0, 0, 0.3)',
                                    shadowBlur: 3,
                                    shadowOffsetX: 1,
                                    shadowOffsetY: 2
                                }
                            },
                            axisLabel: {
                                fontSize: 18,
                                distance: 13,  // 小时数字间隔
                                formatter: function (value: any) {
                                    if (value === 0) {
                                        return '';
                                    }
                                    return value + '';
                                }
                            },
                            // anchor: {
                            //     show: true,
                            //     icon: 'path://M532.8,70.8C532.8,70.8,532.8,70.8,532.8,70.8L532.8,70.8C532.7,70.8,532.8,70.8,532.8,70.8z M456.1,49.6c-2.2-6.2-8.1-10.6-15-10.6h-37.5v10.6h37.5l0,0c2.9,0,5.3,2.4,5.3,5.3c0,2.9-2.4,5.3-5.3,5.3v0h-22.5c-1.5,0.1-3,0.4-4.3,0.9c-4.5,1.6-8.1,5.2-9.7,9.8c-0.6,1.7-0.9,3.4-0.9,5.3v16h10.6v-16l0,0l0,0c0-2.7,2.1-5,4.7-5.3h10.3l10.4,21.2h11.8l-10.4-21.2h0c6.9,0,12.8-4.4,15-10.6c0.6-1.7,0.9-3.5,0.9-5.3C457,53,456.7,51.2,456.1,49.6z M388.9,92.1h11.3L381,39h-3.6h-11.3L346.8,92v0h11.3l3.9-10.7h7.3h7.7l3.9-10.6h-7.7h-7.3l7.7-21.2v0L388.9,92.1z M301,38.9h-10.6v53.1H301V70.8h28.4l3.7-10.6H301V38.9zM333.2,38.9v10.6v10.7v31.9h10.6V38.9H333.2z M249.5,81.4L249.5,81.4L249.5,81.4c-2.9,0-5.3-2.4-5.3-5.3h0V54.9h0l0,0c0-2.9,2.4-5.3,5.3-5.3l0,0l0,0h33.6l3.9-10.6h-37.5c-1.9,0-3.6,0.3-5.3,0.9c-4.5,1.6-8.1,5.2-9.7,9.7c-0.6,1.7-0.9,3.5-0.9,5.3l0,0v21.3c0,1.9,0.3,3.6,0.9,5.3c1.6,4.5,5.2,8.1,9.7,9.7c1.7,0.6,3.5,0.9,5.3,0.9h33.6l3.9-10.6H249.5z M176.8,38.9v10.6h49.6l3.9-10.6H176.8z M192.7,81.4L192.7,81.4L192.7,81.4c-2.9,0-5.3-2.4-5.3-5.3l0,0v-5.3h38.9l3.9-10.6h-53.4v10.6v5.3l0,0c0,1.9,0.3,3.6,0.9,5.3c1.6,4.5,5.2,8.1,9.7,9.7c1.7,0.6,3.4,0.9,5.3,0.9h23.4h10.2l3.9-10.6l0,0H192.7z M460.1,38.9v10.6h21.4v42.5h10.6V49.6h17.5l3.8-10.6H460.1z M541.6,68.2c-0.2,0.1-0.4,0.3-0.7,0.4C541.1,68.4,541.4,68.3,541.6,68.2L541.6,68.2z M554.3,60.2h-21.6v0l0,0c-2.9,0-5.3-2.4-5.3-5.3c0-2.9,2.4-5.3,5.3-5.3l0,0l0,0h33.6l3.8-10.6h-37.5l0,0c-6.9,0-12.8,4.4-15,10.6c-0.6,1.7-0.9,3.5-0.9,5.3c0,1.9,0.3,3.7,0.9,5.3c2.2,6.2,8.1,10.6,15,10.6h21.6l0,0c2.9,0,5.3,2.4,5.3,5.3c0,2.9-2.4,5.3-5.3,5.3l0,0h-37.5v10.6h37.5c6.9,0,12.8-4.4,15-10.6c0.6-1.7,0.9-3.5,0.9-5.3c0-1.9-0.3-3.7-0.9-5.3C567.2,64.6,561.3,60.2,554.3,60.2z',
                            //     showAbove: false,
                            //     offsetCenter: [0, '-35%'],
                            //     size: 120,
                            //     keepAspect: true,
                            //     itemStyle: {
                            //         color: '#707177'
                            //     }
                            // },
                            pointer: {
                                icon: 'path://M2.9,0.7L2.9,0.7c1.4,0,2.6,1.2,2.6,2.6v115c0,1.4-1.2,2.6-2.6,2.6l0,0c-1.4,0-2.6-1.2-2.6-2.6V3.3C0.3,1.9,1.4,0.7,2.9,0.7z',
                                width: 12,
                                length: '55%',
                                offsetCenter: [0, '8%'],
                                itemStyle: {
                                    color: '#C0911F',
                                    shadowColor: 'rgba(0, 0, 0, 0.3)',
                                    shadowBlur: 8,
                                    shadowOffsetX: 2,
                                    shadowOffsetY: 4
                                }
                            },
                            detail: {
                                show: false
                            },
                            title: {
                                offsetCenter: [0, '30%']
                            },
                            data: [
                                {
                                    value: 0
                                }
                            ],
                            axisTick: {
                                distance: 2,  // 修改刻度线位置
                            },
                        },
                        {
                            name: 'minute',
                            type: 'gauge',
                            startAngle: 90,
                            endAngle: -270,
                            min: 0,
                            max: 60,
                            clockwise: true,
                            axisLine: {
                                show: false
                            },
                            splitLine: {
                                show: false
                            },
                            axisTick: {
                                show: false
                            },
                            axisLabel: {
                                show: false
                            },
                            pointer: {
                                icon: 'path://M2.9,0.7L2.9,0.7c1.4,0,2.6,1.2,2.6,2.6v115c0,1.4-1.2,2.6-2.6,2.6l0,0c-1.4,0-2.6-1.2-2.6-2.6V3.3C0.3,1.9,1.4,0.7,2.9,0.7z',
                                width: 8,
                                length: '70%',
                                offsetCenter: [0, '8%'],
                                itemStyle: {
                                    color: '#C0911F',
                                    shadowColor: 'rgba(0, 0, 0, 0.3)',
                                    shadowBlur: 8,
                                    shadowOffsetX: 2,
                                    shadowOffsetY: 4
                                }
                            },
                            anchor: {
                                show: true,
                                size: 20,
                                showAbove: false,
                                itemStyle: {
                                    borderWidth: 15,
                                    borderColor: '#C0911F',
                                    shadowColor: 'rgba(0, 0, 0, 0.3)',
                                    shadowBlur: 8,
                                    shadowOffsetX: 2,
                                    shadowOffsetY: 4
                                }
                            },
                            detail: {
                                show: false
                            },
                            title: {
                                offsetCenter: ['0%', '-40%']
                            },
                            data: [
                                {
                                    value: 0,
                                    name: 'ROLEX'
                                }
                            ]
                        },
                        {
                            name: 'second',
                            type: 'gauge',
                            startAngle: 90,
                            endAngle: -270,
                            min: 0,
                            max: 60,
                            animationEasingUpdate: 'bounceOut',
                            clockwise: true,
                            axisLine: {
                                show: false
                            },
                            splitLine: {
                                show: false
                            },
                            axisTick: {
                                show: false
                            },
                            axisLabel: {
                                show: false
                            },
                            pointer: {
                                icon: 'path://M2.9,0.7L2.9,0.7c1.4,0,2.6,1.2,2.6,2.6v115c0,1.4-1.2,2.6-2.6,2.6l0,0c-1.4,0-2.6-1.2-2.6-2.6V3.3C0.3,1.9,1.4,0.7,2.9,0.7z',
                                width: 4,
                                length: '85%',
                                offsetCenter: [0, '8%'],
                                itemStyle: {
                                    color: '#C0911F',
                                    shadowColor: 'rgba(0, 0, 0, 0.3)',
                                    shadowBlur: 8,
                                    shadowOffsetX: 2,
                                    shadowOffsetY: 4
                                }
                            },
                            anchor: {
                                show: true,
                                size: 15,
                                showAbove: true,
                                itemStyle: {
                                    color: '#C0911F',
                                    shadowColor: 'rgba(0, 0, 0, 0.3)',
                                    shadowBlur: 8,
                                    shadowOffsetX: 2,
                                    shadowOffsetY: 4
                                }
                            },
                            detail: {
                                show: false
                            },
                            title: {
                                color: '#006039',
                                fontSize: 18,
                                fontWeight: 800,
                                fontFamily: 'Georgia',
                                offsetCenter: ['0%', '-30%']
                            },
                            data: [
                                {
                                    value: 0
                                }
                            ]
                        }
                    ]
                };

                setInterval(function () {
                    const date = new Date();
                    const second = date.getSeconds();
                    const minute = date.getMinutes() + second / 60;
                    const hour = (date.getHours() % 12) + minute / 60;
                    // option.animationDurationUpdate = 300;
                    myChart.setOption({
                        series: [
                            {
                                name: 'hour',
                                animation: hour !== 0,
                                data: [{value: hour}]
                            },
                            {
                                name: 'minute',
                                animation: minute !== 0,
                                data: [{value: minute}]
                            },
                            {
                                animation: second !== 0,
                                name: 'second',
                                data: [{value: second, name: "ROLEX"}]
                            },
                        ],

                    });
                }, 1000);

                // 绘制图表
                myChart.setOption(option);
            };

            onMounted(() => {
                getStatistic();
                get30Statistic();
                initClock();
            });

            return {
                statistic
            }
        },
    });
</script>
