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
                                    @confirm="showConfirm(record.id)"
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
                <a-tree-select
                        v-model:value="doc.parent"
                        show-search
                        style="width: 100%"
                        :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
                        placeholder="Please select"
                        allow-clear
                        tree-default-expand-all
                        :tree-data="treeSelectData"
                        :fieldNames="{label:'name', key: 'id', value: 'id'}"
                >
                    <!-- 注意上边的value填的应该是要修改或新增的id -->
                </a-tree-select>
            </a-form-item>
            <a-form-item label="排序">
                <a-input-number v-model:value="doc.sort" style="width: 100%" :min="1"/>
            </a-form-item>
            <a-form-item label="文档">
                <div style="border: 1px solid #ccc">
                    <Toolbar
                            style="border-bottom: 1px solid #ccc"
                            :editor="editorRef"
                            :defaultConfig="toolbarConfig"
                            mode="default"
                    />
                    <Editor
                            style="height: 500px; overflow-y: hidden;"
                            v-model="valueHtml"
                            :defaultConfig="editorConfig"
                            mode="default"
                            @onCreated="handleCreated"
                    />
                </div>
            </a-form-item>
        </a-form>
    </a-modal>
</template>

<script lang="ts">
    import {defineComponent, onMounted, ref, createVNode, onBeforeUnmount, shallowRef} from 'vue';
    import axios from 'axios';
    import {message, Modal} from "ant-design-vue";
    import {Tool} from "@/util/tool";
    import {useRoute} from "vue-router";
    import WarningOutlined from "@ant-design/icons-vue/WarningOutlined";
    import '@wangeditor/editor/dist/css/style.css'; // 引入 css
    import {Editor, Toolbar} from '@wangeditor/editor-for-vue';

    export default defineComponent({
        name: 'AdminDoc',
        components: {Editor, Toolbar},
        setup() {
            // 获取路由信息
            const route = useRoute();

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
                        // 先清空数据再执行查询
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
            const treeSelectData = ref();
            treeSelectData.value = [];
            const modalVisible = ref<boolean>(false);
            const modalLoading = ref<boolean>(false);

            // wangEditor变量
            const editorRef = shallowRef();
            const valueHtml = ref();  // 内容HTML
            const toolbarConfig = {};
            const editorConfig = {placeholder: '请输入内容...'};
            onBeforeUnmount(() => {  // 组件销毁时，也及时销毁编辑器
                const editor = editorRef.value;
                if (editor == null) return;
                editor.destroy();
            });
            const handleCreated = (editor: any) => {
                editorRef.value = editor; // 记录 editor 实例，重要！
            };

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

                // 自身和子文档不可以作为自身的父文档
                treeSelectData.value = Tool.copy(level1.value);
                setDisable(treeSelectData.value, record.id);

                // 插入顶级文档选项
                treeSelectData.value.unshift({id: 0, name: '顶级文档'});

            };

            // 新增
            const add = () => {
                // 编辑的时候doc会拿到ebookId的值，而新增的时候没有
                doc.value = {
                    ebookId: route.query.ebookId
                };
                modalVisible.value = true;  // 弹出对话框

                // 修正数据源和插入顶级文档选项
                treeSelectData.value = Tool.copy(level1.value);
                treeSelectData.value.unshift({id: 0, name: '顶级文档'});

            };

            const setDisable = (treeSelectData: any, id: any) => {
                console.log(JSON.stringify(treeSelectData));
                //遍历数组，即遍历某一层节点
                for (let i = 0; i < treeSelectData.length; i++) {
                    const node = treeSelectData[i];
                    if (node.id === id) {
                        //如果当前节点就是目标节点
                        console.log("disabled", node);
                        //将目标节点设置为disabled
                        node.disabled = true;

                        //遍历所有节点，将所有节点全部加上disabled
                        const children = node.children;
                        if (Tool.isNotEmpty(children)) {
                            for (let j = 0; j < children.length; j++) {
                                setDisable(children, children[j].id);
                            }
                        }
                    } else {
                        //如果当前节点不是目标节点，则到子节点在找找看
                        const children = node.children;
                        if (Tool.isNotEmpty(children)) {
                            setDisable(children, id);
                        }
                    }
                }
            };

            const delIds: Array<string> = [];  // 要删除的文档名称
            const names: Array<string> = [];   // 要删除的文档id
            const getDelIds = (treeSelectData: any, id: any) => {
                //遍历数组，即遍历某一层节点
                for (let i = 0; i < treeSelectData.length; i++) {
                    const node = treeSelectData[i];
                    if (node.id === id) {
                        //如果当前节点就是目标节点
                        console.log("delete", node);
                        // 加入到结果集
                        delIds.push(id);
                        names.push(node.name);

                        //遍历所有节点
                        const children = node.children;
                        if (Tool.isNotEmpty(children)) {
                            for (let j = 0; j < children.length; j++) {
                                getDelIds(children, children[j].id);
                            }
                        }
                    } else {
                        //如果当前节点不是目标节点，则到子节点在找找看
                        const children = node.children;
                        if (Tool.isNotEmpty(children)) {
                            getDelIds(children, id);
                        }
                    }
                }
            };

            // 删除前的二次确认
            const showConfirm = (id: number) => {
                delIds.length = 0;  // 先清空存储器数据
                names.length = 0;
                getDelIds(level1.value, id);
                Modal.confirm({
                    title: '重要提醒！',
                    icon: createVNode(WarningOutlined),
                    content: '将删除【' + names.join(', ') + '】，此操作将不可恢复！',
                    onOk() {
                        handleDelete();
                    },
                    // eslint-disable-next-line @typescript-eslint/no-empty-function
                    onCancel() {
                    },
                });
            };

            // 删除
            const handleDelete = () => {
                axios.delete("/doc/delete/" + delIds.join(',')).then((response) => {
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
                treeSelectData,
                showConfirm,

                handleQuery,

                editorRef,
                valueHtml,
                toolbarConfig,
                editorConfig,
                handleCreated

            }
        }
    });
</script>
