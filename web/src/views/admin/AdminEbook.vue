<template>
    <a-layout>
        <a-layout-content
                :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
        >
            <a-button type="primary" @click="add()" size="large">
                新增
            </a-button>
            <!-- 表格 -->
            <a-table
                    :columns="columns"
                    :row-key="record => record.id"
                    :data-source="ebooks"
                    :pagination="pagination"
                    :loading="loading"
                    @change="handleTableChange"
            >
                <!-- 封面渲染，遍历数组里面的cover -->
                <template #cover="{ text: cover }">
                    <img v-if="cover" :src="cover" alt="avatar"/>
                </template>
                <!-- 按钮 -->
                <template v-slot:action="{ text, record }">
                    <!-- 按钮之间要有空格 -->
                    <a-space size="small">
                        <a-button type="primary" @click="edit(record)">
                            编辑
                        </a-button>
                        <a-button type="danger">
                            删除
                        </a-button>
                    </a-space>
                </template>
            </a-table>
        </a-layout-content>
    </a-layout>

    <!-- 对话框，内含表单 -->
    <a-modal
            v-model:visible="modalVisible"
            title="电子书表单"
            :confirm-loading="modalLoading"
            @ok="handleModalOk"
    >
        <a-form model="ebook" :label-col="{ span: 4 }" :wrapper-col="wrapperCol">
            <a-form-item label="名称">
                <a-input v-model:value="ebook.name"/>
            </a-form-item>
            <a-form-item label="封面">
                <a-input v-model:value="ebook.cover"/>
            </a-form-item>
            <a-form-item label="分类一">
                <a-input-number v-model:value="ebook.category1Id" style="width: 100%" :min="1"/>
            </a-form-item>
            <a-form-item label="分类二">
                <a-input-number v-model:value="ebook.category2Id" style="width: 100%" :min="1"/>
            </a-form-item>
            <a-form-item label="描述">
                <a-input v-model:value="ebook.description"/>
            </a-form-item>
        </a-form>
    </a-modal>
</template>

<script lang="ts">
    import {defineComponent, onMounted, ref} from 'vue';
    import axios from 'axios';

    export default defineComponent({
        name: 'AdminEbook',
        setup() {
            const ebooks = ref();
            const pagination = ref({
                current: 1,   // 当前页（动态）
                pageSize: 3,  // 分页条数（静态）
                total: 0      // 列表总数（动态）
            });
            const loading = ref(false);

            const columns = [
                {
                    title: '封面',
                    dataIndex: 'cover',
                    slots: {customRender: 'cover'}   // 渲染封面
                },
                {
                    title: '名称',
                    dataIndex: 'name'
                },
                {
                    title: '分类一',
                    key: 'category1Id',       // 自定义设置的key
                    dataIndex: 'category1Id'  // 数据项中对应的路径
                },
                {
                    title: '分类二',
                    key: 'category2Id',
                    dataIndex: 'category2Id'
                },
                {
                    title: '文档数',
                    dataIndex: 'docCount'
                },
                {
                    title: '阅读数',
                    dataIndex: 'viewCount'
                },
                {
                    title: '点赞数',
                    dataIndex: 'voteCount'
                },
                {
                    title: 'Action',
                    key: 'action',
                    slots: {customRender: 'action'}
                }
            ];

            /**
             * 向后端指定分页参数查询获取，并获取后端确切的数据和分页参数
             **/
            const handleQuery = (params: any) => {
                loading.value = true;
                axios.get("/ebook/list", {
                    params: {
                        // 只用下面这两个属性作为参数
                        page: params.page,
                        size: params.size
                    }
                }).then((response) => {
                    loading.value = false;
                    const data = response.data;
                    ebooks.value = data.content.list;

                    // 重置分页按钮
                    pagination.value.current = params.page;
                    pagination.value.total = data.content.total;
                });
            };

            /**
             * 表格点击页码时触发
             */
            const handleTableChange = (pagination: any) => {
                handleQuery({
                    page: pagination.current,
                    size: pagination.pageSize
                    // 可以有其他参数
                });
            };

            // 表单
            const modalVisible = ref<boolean>(false);
            const modalLoading = ref<boolean>(false);
            const handleModalOk = () => {
                modalLoading.value = true;
                // axios.post："application/json"
                axios.post("/ebook/save", ebook.value).then((response) => {
                    const data = response.data;
                    if (data.success) {
                        modalVisible.value = false;
                        modalLoading.value = false;

                        // 刷新页面
                        handleQuery({
                            page: pagination.value.current,
                            size: pagination.value.pageSize
                        });
                    }
                });
            };

            const ebook = ref({});
            // 编辑
            const edit = (record: any) => {
                ebook.value = record;    // 把表格中ebooks的数据项复制给新的变量ebook
                modalVisible.value = true;  // 弹出对话框
            };

            // 新增
            const add = () => {
                ebook.value = {};
                modalVisible.value = true;  // 弹出对话框
            };

            onMounted(() => {
                handleQuery({
                    page: 1,
                    size: pagination.value.pageSize
                });  // 初始的时候也要查一次
            });

            return {
                ebooks,
                pagination,
                columns,
                loading,
                handleTableChange,

                modalVisible,
                modalLoading,
                handleModalOk,
                ebook,

                edit,
                add
            }
        }
    });
</script>
