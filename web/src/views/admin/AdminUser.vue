<template>
    <a-layout>
        <a-layout-content
                :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
        >
            <p>
                <a-form :model="queryParam" layout="inline">
                    <a-form-item>
                        <a-input-search
                                v-model:value="queryParam.loginName"
                                placeholder="登录账号"
                                enter-button="查询"
                                size="large"
                                @search="handleQuery({page:1, size:pagination.pageSize, loginName:queryParam.loginName})"
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
                    :data-source="users"
                    :pagination="pagination"
                    :loading="loading"
                    @change="handleTableChange"
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
            title="用户表单"
            :confirm-loading="modalLoading"
            @ok="handleModalOk"
    >
        <a-form :model="user" :label-col="{ span:4 }" :wrapper-col="{ span:22 }">
            <a-form-item label="用户名">
                <!-- 前端编辑不允许修改登录 -->
                <a-input v-model:value="user.loginName" :disabled="!!user.id"/>
            </a-form-item>
            <a-form-item label="昵称">
                <a-input v-model:value="user.name"/>
            </a-form-item>
            <a-form-item label="密码" v-show="!user.id">
                <a-input v-model:value="user.password"/>
            </a-form-item>
        </a-form>
    </a-modal>
</template>

<script lang="ts">
    import {defineComponent, onMounted, ref} from 'vue';
    import axios from 'axios';
    import {message} from "ant-design-vue";
    import {Tool} from "@/util/tool";

    // js定义变量是存在的
    declare let hexMd5: any;
    declare let KEY: any;

    export default defineComponent({
        name: 'AdminUser',
        setup() {
            const users = ref();
            const pagination = ref({
                current: 1,   // 当前页（动态）
                pageSize: 3,  // 分页条数（静态）
                total: 0      // 列表总数（动态）
            });
            const loading = ref(false);

            const columns = [
                {
                    title: '用户名',
                    dataIndex: 'loginName',
                },
                {
                    title: '昵称',
                    dataIndex: 'name',
                },
                {
                    title: '密码',
                    dataIndex: 'password',
                },
                {
                    title: 'Action',
                    key: 'action',
                }
            ];

            /**
             * 向后端指定分页参数查询获取，并获取后端确切的数据和分页参数
             **/
            const handleQuery = (params: any) => {
                loading.value = true;
                axios.get("/user/list", {
                    params: {
                        page: params.page,
                        size: params.size,
                        loginName: queryParam.value.loginName
                    }
                }).then((response) => {
                    loading.value = false;
                    const data = response.data;
                    if (data.success) {
                        users.value = data.content.list;

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
                // 密码加密传输
                user.value.password = hexMd5(user.value.password + KEY);

                // 把选择框的内容给到user
                axios.post("/user/save", user.value).then((response) => {
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

            // 表单的user存储的值
            const user = ref();
            // 编辑
            const edit = (record: any) => {
                // 把表格中users的数据项复制给新的变量user
                user.value = Tool.copy(record);  // 复制多一个值，以免对原来record的引用进行修改
                modalVisible.value = true;  // 弹出对话框
            };

            // 新增
            const add = () => {
                user.value = {};
                modalVisible.value = true;  // 弹出对话框
            };

            // 删除
            const handleDelete = (id: number) => {
                axios.delete("/user/delete/" + id).then((response) => {
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
                handleQuery({page: 1, size: pagination.value.pageSize});
            });

            return {
                users,
                pagination,
                columns,
                loading,
                handleTableChange,

                modalVisible,
                modalLoading,
                handleModalOk,
                user,

                edit,
                add,
                handleDelete,

                queryParam,
                handleQuery,
            }
        }
    });
</script>
