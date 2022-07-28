<template>
    <a-layout>
        <a-layout-sider width="200" style="background: #fff">
            <a-menu :style="{ height: '100%', borderRight: 0 }"
                    mode="inline"
            >
                <a-menu-item>
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
                            <span>{{child.name}}</span>
                        </a-menu-item>
                        <a-sub-menu v-else :key="child.id" :title="child.name">
                            <a-menu-item v-for="child1 in child.children" :key="child1.id">
                                <span>{{child1.name}}</span>
                            </a-menu-item>
                        </a-sub-menu>
                    </template>
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
    import {StarOutlined, LikeOutlined, MessageOutlined, MailOutlined, UserAddOutlined} from '@ant-design/icons-vue';
    import {Tool} from "@/util/tool";
    import {message} from "ant-design-vue";

    export default defineComponent({
        name: 'HomeView',
        StarOutlined,
        LikeOutlined,
        MessageOutlined,
        MailOutlined,
        UserAddOutlined,

        setup() {
            const ebooks = ref();  //1

            onMounted(() => {
                handleQueryCategory();
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

            const level1 = ref();
            const handleQueryCategory = () => {
                axios.get("/category/all").then((response) => {
                    const data = response.data;
                    if (data.success) {
                        const categorys = data.content;
                        level1.value = [];
                        level1.value = Tool.array2Tree(categorys, 0);
                        console.log("aaa " + JSON.stringify(level1.value));
                    } else {
                        message.error(data.message)
                    }
                });
            };

            return {
                ebooks,  //3
                actions,

                level1
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
