<template>
    <a-layout>
        <a-layout-sider width="200" style="background: #fff">
            <a-menu
                    mode="inline"
                    :style="{ height: '100%', borderRight: 0 }"
            >
                <a-sub-menu key="sub1">
                    <template #title>
              <span>
                <user-outlined/>
                subnav 111
              </span>
                    </template>
                    <a-menu-item key="1">option1</a-menu-item>
                    <a-menu-item key="2">option2</a-menu-item>
                    <a-menu-item key="3">option3</a-menu-item>
                    <a-menu-item key="4">option4</a-menu-item>
                </a-sub-menu>
                <a-sub-menu key="sub2">
                    <template #title>
              <span>
                <laptop-outlined/>
                subnav 2
              </span>
                    </template>
                    <a-menu-item key="5">option5</a-menu-item>
                    <a-menu-item key="6">option6</a-menu-item>
                    <a-menu-item key="7">option7</a-menu-item>
                    <a-menu-item key="8">option8</a-menu-item>
                </a-sub-menu>
                <a-sub-menu key="sub3">
                    <template #title>
              <span>
                <notification-outlined/>
                subnav 3
              </span>
                    </template>
                    <a-menu-item key="9">option9</a-menu-item>
                    <a-menu-item key="10">option10</a-menu-item>
                    <a-menu-item key="11">option11</a-menu-item>
                    <a-menu-item key="12">option12</a-menu-item>
                </a-sub-menu>
            </a-menu>
        </a-layout-sider>
        <a-layout-content
                :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
        >
            <!-- 取后端数据，电子书不多，不再分页，每行显示多个 -->
            <a-list item-layout="vertical" size="large" :data-source="ebooks" :grid="{ gutter: 20, column: 3 }">
                <template #renderItem="{ item }">   <!-- ebooks给到item变量，循环遍历 -->
                    <a-list-item key="item.name">   <!-- 后端组件 -->
                        <template #actions>
          <span v-for="{ type, text } in actions" :key="type">
            <component :is="type" style="margin-right: 8px"/>
            {{ text }}   <!-- 点赞收藏文字 -->
          </span>
                        </template>
                        <a-list-item-meta :description="item.description">   <!-- 描述 -->
                            <template #title>
                                <a :href="item.href">{{ item.name }}</a>
                            </template>
                            <template #avatar>
                                <a-avatar :src="item.cover"/>    <!-- 图标 -->
                            </template>
                        </a-list-item-meta>
                    </a-list-item>
                </template>
            </a-list>
        </a-layout-content>
    </a-layout>
</template>

<script lang="ts">
    import {defineComponent, onMounted, ref} from 'vue';
    import axios from 'axios';
    import {StarOutlined, LikeOutlined, MessageOutlined} from '@ant-design/icons-vue';

    export default defineComponent({
        name: 'HomeView',
        StarOutlined,
        LikeOutlined,
        MessageOutlined,

        setup() {
            const ebooks = ref();  //1

            onMounted(() => {
                axios.get('/ebook/list', {
                    params: {
                        page: 1,
                        size: 999
                    }
                }).then(
                    (response) => {
                        const data = response.data;
                        ebooks.value = data.content.list;  //2，一定要取的是ref的.value
                    });
            });  // onMounted

            const actions: Record<string, string>[] = [
                {type: 'StarOutlined', text: '156'},
                {type: 'LikeOutlined', text: '156'},
                {type: 'MessageOutlined', text: '2'},
            ];

            return {
                ebooks,  //3
                actions,
            }
        }  // setup
    });
</script>

<style scoped>
    /*scoped，只在当前页面显示，下是图标样式*/
    .ant-avatar {
        width: 50px;
        height: 50px;
        line-height: 50px;
        border-radius: 8%;
        margin: 5px 0;
    }
</style>
