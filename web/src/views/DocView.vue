<template>
    <a-layout-content
            :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
        <h3 v-if="docLength === 0">对不起，找不到相关文档！</h3>
        <a-row>
            <a-col :span="6">
                <a-input v-model:value="searchValue" style="margin-bottom: 8px; width: 80%" placeholder="文档搜索"/>
                <a-tree
                        :tree-data="level1"
                        :fieldNames="{title:'name', key: 'id', value: 'id'}"
                        @select="onSelect"
                        :defaultSelectedKeys="defaultSelectedKeys"
                        style="font-size: medium"
                        @expand="onExpand"
                        :expanded-keys="expandedKeys"
                        :auto-expand-parent="autoExpandParent"
                >
                    <template #title="{ name }">
                        <span v-if="name.indexOf(searchValue) > -1">
                            {{ name.substr(0, name.indexOf(searchValue)) }}
                        <span style="color: #f50">{{ searchValue }}</span>
                            {{ name.substr(name.indexOf(searchValue) + searchValue.length) }}
                        </span>
                        <span v-else>{{ name }}</span>
                    </template>
                </a-tree>
            </a-col>
            <a-col :span="18">
                <div :innerHTML="displayHtml">
                </div>
            </a-col>
        </a-row>
    </a-layout-content>
</template>

<script lang="ts">
    import {defineComponent, onMounted, ref, watch} from 'vue';
    import axios from "axios";
    import {Tool} from "@/util/tool";
    import {message} from "ant-design-vue";
    import {useRoute} from "vue-router";

    export default defineComponent({
        name: 'DocView',

        setup() {
            onMounted(() => {
                handleQuery();
            });

            // 获取路由信息
            const route = useRoute();

            const doc = ref();  // 当前选中的文档信息
            doc.value = {};

            const level1 = ref();  // null，一级文档树，children属性就是二级文档
            level1.value = [];  // []，初始化时赋值一个空数组，为了解决v-if level1.length为空时发生报错。
            const docLength = ref();  // 先获取文档的长度再判断是否为0

            // 搜索以及展开的列表（动态）
            const searchValue = ref<string>('');
            const expandedKeys = ref();

            const parentsId: any[] = [];  // 选中文档的所有父文档
            expandedKeys.value = parentsId;  // 一打开会展开所有的文档列表
            const autoExpandParent = ref<boolean>(true);


            const defaultSelectedKeys = ref();  // 默认选中的文档
            defaultSelectedKeys.value = [];

            const displayHtml = ref();  // 显示的文档内容
            displayHtml.value = '';

            /**
             * 查询文档表，并转换成树形数组
             */
            const handleQuery = () => {
                axios.get("/doc/find/" + route.query.ebookId).then((response) => {
                    const data = response.data;
                    if (data.success) {
                        const docs = data.content;

                        // 先清空数据再执行查询
                        level1.value = [];
                        level1.value = Tool.array2Tree(docs, 0);

                        if (Tool.isNotEmpty(level1)) {
                            docLength.value = level1.value.length;
                            getAllParentId(level1.value);  // 初始展开所有的列表
                            defaultSelectedKeys.value = [level1.value[0].id];
                            handleQueryContent(level1.value[0].id);
                        }
                    } else {
                        message.error(data.message)
                    }
                });
            };

            /**
             * 查询文档内容表，并给到前端页面显示
             */
            const handleQueryContent = (id: number) => {
                axios.get("/doc/find-content/" + id).then((response) => {
                    const data = response.data;
                    if (data.success) {
                        if (Tool.isEmpty(data.content)) {
                            displayHtml.value = '<h3>此页为空。</h3>';
                        } else {
                            displayHtml.value = data.content;
                        }
                    } else {
                        message.error(data.message)
                    }
                });
            };

            // 加载文档对应的Html文本
            const onSelect = (selectedKeys: any, info: any) => {
                if (Tool.isNotEmpty(selectedKeys)) {
                    handleQueryContent(selectedKeys[0]);  // 加载内容
                    doc.value = info.selectedNodes[0];  // 获取信息
                }
            };

            // 生成文档id和name的列表数据
            const dataList: { id: any; name: any; }[] = [];
            const generateList = (data: any) => {
                for (let i = 0; i < data.length; i++) {
                    const node = data[i];
                    const id = node.id;
                    const name = node.name;
                    dataList.push({id, name});
                    if (node.children) {
                        generateList(node.children);
                    }
                }
            };

            // 获取指定文档的所有父文档
            const getParentId = (
                id: string | number,
                tree: any,
            ): string | number | undefined => {
                let parentId;
                for (let i = 0; i < tree.length; i++) {
                    const node = tree[i];
                    if (node.children) {
                        if (node.children.some((item: { id: string | number; }) => item.id === id)) {
                            parentId = node.id;
                        } else if (getParentId(id, node.children)) {
                            parentId = getParentId(id, node.children);
                        }
                    }
                }
                console.log("ccc " + parentId);
                return parentId;
            };

            watch(searchValue, value => {
                generateList(level1.value);
                const expanded = dataList
                    .map((item: any) => {
                        if (item.name.indexOf(value) > -1) {
                            return getParentId(item.id, level1.value);
                        }
                        return null;
                    })
                    .filter((item, i, self) => item && self.indexOf(item) === i);
                expandedKeys.value = expanded;
                searchValue.value = value;
                autoExpandParent.value = true;
            });

            const onExpand = (keys: string[]) => {
                expandedKeys.value = keys;
                autoExpandParent.value = false;
            };

            // 获取level1所有的父文档，用于一打开页面显示
            const getAllParentId = (array: any) => {
                for (let i = 0; i < array.length; i++) {
                    const node = array[i];
                    if (node.children) {
                        parentsId.push(node.id);
                        getAllParentId(node.children);
                    }
                }
            };

            return {
                level1,
                displayHtml,
                defaultSelectedKeys,
                onSelect,

                docLength,
                searchValue,
                autoExpandParent,
                expandedKeys,
                onExpand,
            };
        },
    });
</script>