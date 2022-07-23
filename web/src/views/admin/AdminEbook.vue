<template>
    <a-layout>
        <a-layout-content
                :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
        >
            <p>
                <a-form :model="queryParam" layout="inline">
                    <a-form-item>
                        <a-input-search
                                v-model:value="queryParam.name"
                                placeholder="名称"
                                enter-button="查询"
                                size="large"
                                @search="handleQuery({page:1, size:pagination.pageSize, name:queryParam.name})"
                        />
                    </a-form-item>
                    <a-form-item>
                        <a-button type="primary" @click="add()" size="large">
                            新增
                        </a-button>
                    </a-form-item>
                </a-form>
            </p>
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
                        <!-- 删除的确认框 -->
                        <a-popconfirm
                                title="删除后不可恢复，确认删除？"
                                ok-text="确认"
                                cancel-text="取消"
                                @confirm="handleDelete(record.id)"
                        >
                            <a-button type="danger">
                                删除
                            </a-button>
                        </a-popconfirm>
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
    import {message} from "ant-design-vue";

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
                        page: params.page,
                        size: params.size,
                        name: queryParam.value.name
                    }
                }).then((response) => {
                    loading.value = false;
                    const data = response.data;
                    if (data.success) {
                        ebooks.value = data.content.list;

                        // 重置分页按钮
                        pagination.value.current = params.page;
                        pagination.value.total = data.content.total;
                    } else {
                        message.error(data.message)
                    }
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
                    modalLoading.value = false;   // 无论成功与否loading效果都要终止
                    if (data.success) {
                        modalVisible.value = false;

                        // 刷新页面
                        handleQuery({
                            page: pagination.value.current,
                            size: pagination.value.pageSize
                        });
                    } else {
                        message.error(data.message)
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

            // 删除
            const handleDelete = (id: number) => {
                axios.delete("/ebook/delete/" + id).then((response) => {
                    const data = response.data;
                    if (data.success) {   // CommonResp.success
                        // 刷新页面
                        handleQuery({
                            page: pagination.value.current,
                            size: pagination.value.pageSize
                        });
                    }
                });
            };

            // 查询的参数
            const queryParam = ref();
            queryParam.value = {};

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
                add,
                handleDelete,

                queryParam,
                handleQuery

            }
        }
    });
</script>
