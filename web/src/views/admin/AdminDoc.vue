<template>
    <a-layout>
        <a-layout-content
                :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
        >
            <p>
                <a-form layout="inline">
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
                    :data-source="level1"
                    :pagination="false"
                    :loading="loading"
            >
                <template #bodyCell="{ column, record }">
                    <template v-if="column.key === 'action'">
                        <!-- 按钮渲染 -->
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
                </template>
            </a-table>
        </a-layout-content>
    </a-layout>

    <!-- 对话框，内含表单 -->
    <a-modal
            v-model:visible="modalVisible"
            title="文档表单"
            :confirm-loading="modalLoading"
            @ok="handleModalOk"
    >
        <a-form :model="doc" :label-col="{ span:4 }" :wrapper-col="{ span:22 }">
            <a-form-item label="名称">
                <a-input v-model:value="doc.name"/>
            </a-form-item>
            <a-form-item label="父文档">
                <a-select
                        ref="select"
                        v-model:value="doc.parent"
                >
                    <a-select-option value="0">
                        顶级文档
                    </a-select-option>
                    <!-- v-for"item in list key:"自定义的键" value:"传给后台的值" -->
                    <a-select-option v-for="c in level1" :key="c.id" :value="c.id" :disabled="c.id === doc.id">
                        {{c.name}}
                    </a-select-option>
                </a-select>
            </a-form-item>
            <a-form-item label="排序">
                <a-input-number v-model:value="doc.sort" style="width: 100%" :min="1"/>
            </a-form-item>
        </a-form>
    </a-modal>
</template>

<script lang="ts">
    import {defineComponent, onMounted, ref} from 'vue';
    import axios from 'axios';
    import {message} from "ant-design-vue";
    import {Tool} from "@/util/tool";

    export default defineComponent({
        name: 'AdminDoc',
        setup() {
            const level1 = ref();
            const loading = ref(false);

            const columns = [
                {
                    title: '名称',
                    dataIndex: 'name',
                },
                {
                    title: '父文档',
                    dataIndex: 'parent',
                },
                {
                    title: '排序',
                    dataIndex: 'sort',
                },
                {
                    title: 'Action',
                    key: 'action',
                }
            ];


            /**
             *  父: [{
             *      id:
             *      name:
             *      parent:
             *      children: [{
             *         id:
             *         name:
             *         parent:
             *         children: [{..}]
             *      }]
             *    }]
             */
            const handleQuery = () => {
                loading.value = true;
                axios.get("/doc/all").then((response) => {
                    loading.value = false;
                    const data = response.data;
                    if (data.success) {
                        const docs = data.content;
                        level1.value = [];
                        level1.value = Tool.array2Tree(docs, 0);
                    } else {
                        message.error(data.message)
                    }
                });
            };

            /**
             * 表格点击页码时触发
             */
            const handleTableChange = () => {
                handleQuery();
            };

            // 表单
            const modalVisible = ref<boolean>(false);
            const modalLoading = ref<boolean>(false);
            const handleModalOk = () => {
                modalLoading.value = true;
                // axios.post："application/json"
                axios.post("/doc/save", doc.value).then((response) => {
                    const data = response.data;
                    modalLoading.value = false;   // 无论成功与否loading效果都要终止
                    if (data.success) {
                        modalVisible.value = false;

                        // 刷新页面
                        handleQuery();
                    } else {
                        message.error(data.message)
                    }
                });
            };

            const doc = ref({});
            // 编辑
            const edit = (record: any) => {
                // 把表格中docs的数据项复制给新的变量doc
                doc.value = Tool.copy(record);  // 复制多一个值，以免对原来record的引用进行修改
                modalVisible.value = true;  // 弹出对话框
            };

            // 新增
            const add = () => {
                doc.value = {};
                modalVisible.value = true;  // 弹出对话框
            };

            // 删除
            const handleDelete = (id: number) => {
                axios.delete("/doc/delete/" + id).then((response) => {
                    const data = response.data;
                    if (data.success) {   // CommonResp.success
                        // 刷新页面
                        handleQuery();
                    }
                });
            };

            onMounted(() => {
                handleQuery();  // 初始的时候也要查一次
            });

            return {
                level1,
                columns,
                loading,
                handleTableChange,

                modalVisible,
                modalLoading,
                handleModalOk,
                doc,

                edit,
                add,
                handleDelete,

                handleQuery

            }
        }
    });
</script>
