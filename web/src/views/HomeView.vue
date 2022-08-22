<template>
    <a-layout>
        <a-layout-sider width="200" style="background: #fff">
            <a-menu :style="{ height: '100%', borderRight: 0 }"
                    mode="inline"
                    @click="handleClick"
            >
                <a-menu-item key="welcome">
                    <template #icon>
                        <MailOutlined/>
                    </template>
                    欢迎
                </a-menu-item>

                <!-- 有子菜单的用a-sub-menu标签 -->
                <!-- 最深3层的菜单 -->
                <a-sub-menu v-for="item in level1" :key="item.id" :title="item.name">

                    <template #icon>
                        <UserAddOutlined/>
                    </template>

                    <template v-for="child in item.children" :key="child.id">
                        <a-menu-item v-if="child.children == null" :key="child.id">
                            <template #icon>
                                <RightSquareOutlined/>
                            </template>
                            <span>{{child.name}}</span>
                        </a-menu-item>
                    </template>
                </a-sub-menu>
            </a-menu>
        </a-layout-sider>
        <a-layout-content
                :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
        >
            <div v-show="isShowWelcome" class="welcome">
                <h1>欢迎使用junzhousky的电子书网页服务</h1>
            </div>
            <!-- 取后端数据，电子书不多，不再分页，每行显示多个 -->
            <a-list v-show="!isShowWelcome" item-layout="vertical" size="large"
                    :data-source="ebooks" :grid="{ gutter: 20, column: 3 }">
                <template #renderItem="{ item }">   <!-- ebooks给到item变量，循环遍历 -->
                    <a-list-item key="item.name">   <!-- 后端组件 -->
                        <a-list-item-meta :description="item.description">   <!-- 描述 -->
                            <template #title>
                                <router-link :to="'/doc?ebookId=' + item.id">
                                    {{ item.name }}
                                </router-link>
                            </template>
                            <template #avatar>
                                <a-avatar :src="item.cover"/>    <!-- 图标 -->
                            </template>
                        </a-list-item-meta>
                        <template #actions>
                            <span>
                                <component v-bind:is="'ReadOutlined'" style="margin-right: 8px"/>
                                {{ item.docCount }}
                            </span>
                            <span>
                                <component v-bind:is="'LikeOutlined'" style="margin-right: 8px"/>
                                {{ item.voteCount }}
                            </span>
                            <span>
                                <component v-bind:is="'EyeOutlined'" style="margin-right: 8px"/>
                                {{ item.viewCount }}
                            </span>
                        </template>
                    </a-list-item>
                </template>
            </a-list>
        </a-layout-content>
    </a-layout>
</template>

<script lang="ts">
    import {defineComponent, onMounted, ref} from 'vue';
    import axios from 'axios';
    import {
        ReadOutlined,
        LikeOutlined,
        EyeOutlined,
        MailOutlined,
        RightSquareOutlined,
        UserAddOutlined
    } from '@ant-design/icons-vue';
    import {Tool} from "@/util/tool";
    import {message} from "ant-design-vue";

    export default defineComponent({
        name: 'HomeView',
        ReadOutlined,
        LikeOutlined,
        EyeOutlined,
        MailOutlined,
        RightSquareOutlined,
        UserAddOutlined,

        setup() {
            const ebooks = ref();  //1

            onMounted(() => {
                handleQueryCategory();
            });  // onMounted

            const level1 = ref();
            const handleQueryCategory = () => {
                axios.get("/category/all").then((response) => {
                    const data = response.data;
                    if (data.success) {
                        const categorys = data.content;
                        level1.value = [];
                        level1.value = Tool.array2Tree(categorys, 0);
                    } else {
                        message.error(data.message)
                    }
                });
            };

            const isShowWelcome = ref(true);
            let categoryId2 = 0;
            const handleClick = (value: any) => {
                if (value.key == 'welcome') {
                    isShowWelcome.value = true;
                } else {
                    isShowWelcome.value = false;
                    categoryId2 = value.key;
                    handleQueryEbook();
                }
                // isShowWelcome.value = value.key == 'welcome';
            };

            const handleQueryEbook = () => {
                axios.get('/ebook/list', {
                    params: {
                        page: 1,
                        size: 999,
                        category2Id: categoryId2  // category2Id要映射到请求类
                    }
                }).then(
                    (response) => {
                        const data = response.data;
                        ebooks.value = data.content.list;
                    });
            };

            return {
                ebooks,  //3
                level1,

                handleClick,
                isShowWelcome
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
