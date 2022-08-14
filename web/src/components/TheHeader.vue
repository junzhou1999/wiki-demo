<template>
    <a-layout-header class="header">
        <div class="logo"/>
        <a-menu
                theme="dark"
                mode="horizontal"
                :style="{ lineHeight: '64px' }"
        >
            <a-menu-item key="/">
                <router-link to="/">首页</router-link>
            </a-menu-item>
            <a-menu-item key="/about" :style="{order: 5}">
                <router-link to="/about">关于我们</router-link>
            </a-menu-item>
            <a-menu-item key="/admin/user" :style="user.id?{}:{display: 'none'}">
                <router-link to="/admin/user">用户管理</router-link>
            </a-menu-item>
            <a-menu-item key="/admin/ebook" :style="user.id?{}:{display: 'none'}">
                <router-link to="/admin/ebook">电子书管理</router-link>
            </a-menu-item>
            <a-menu-item key="/admin/category" :style="user.id?{}:{display: 'none'}">
                <router-link to="/admin/category">分类管理</router-link>
            </a-menu-item>
            <a class="login-menu" :style="{right: '150px'}" v-show="user.id">
                您好：{{user.name}}
            </a>
            <a class="login-menu" :style="{right: '60px'}" @click="showloginModal" v-show="!user.id">
                登录
            </a>
            <a-popconfirm
                    title="是否退出登录？"
                    ok-text="确认"
                    cancel-text="取消"
                    @confirm="handleLogout"
            >
                <a class="login-menu" :style="{right: '60px'}" v-show="user.id">
                    退出登录
                </a>
            </a-popconfirm>

        </a-menu>
    </a-layout-header>
    <a-modal
            v-model:visible="loginModalVisible"
            title="登录表单"
            :confirm-loading="loginModalLoading"
            @ok="handleLogin"
    >
        <a-form :model="loginUser" :label-col="{ span:4 }" :wrapper-col="{ span:22 } ">
            <a-form-item label="用户名" name="loginName" :rules="[{ required: true, message: '请输入用户名！' }]">
                <!-- 前端编辑不允许修改登录 -->
                <a-input v-model:value="loginUser.loginName"/>
            </a-form-item>
            <a-form-item label="密码" name="password" :rules="[{ required: true, message: '请输入密码！' }]">
                <a-input-password v-model:value="loginUser.password"/>
            </a-form-item>
        </a-form>

    </a-modal>
</template>

<script lang="ts">
    import {computed, defineComponent, ref} from 'vue';
    import axios from "axios";
    import {message} from "ant-design-vue";
    import {Tool} from "@/util/tool";
    import store from "@/store";

    // js定义变量是存在的
    declare let hexMd5: any;
    declare let KEY: any;

    export default defineComponent({
        name: 'the-header',
        setup() {
            // 表单
            const loginModalVisible = ref<boolean>(false);
            const loginModalLoading = ref<boolean>(false);
            // 登录的用户信息
            const loginUser = ref({
                loginName: '',
                password: ''
            });

            // 登录后的用户信息，属于动态被监听状态，还是属于响应式变量
            const user = computed(
                () => {
                    return store.state.user;
                });

            // 登录
            const handleLogin = () => {
                console.log("开始登录");
                if (Tool.isNotEmpty(loginUser.value.loginName) && Tool.isNotEmpty(loginUser.value.password)) {
                    loginModalLoading.value = true;
                    const loginUserReq = Tool.copy(loginUser.value);  // 多复制一个变量，以免登陆错误时会显示转换后的密码
                    loginUserReq.password = hexMd5(loginUserReq.password + KEY);
                    axios.post('/user/login', loginUserReq).then((response) => {
                        const data = response.data;
                        loginModalLoading.value = false;
                        if (data.success) {
                            loginModalVisible.value = false;
                            store.commit("setUser", data.content);  // 触发函数
                            message.success("登录成功！");
                        } else {
                            message.error(data.message);
                            loginUser.value.password = '';
                        }
                    });
                }
                if (Tool.isEmpty(loginUser.value.loginName)) {
                    message.error('【用户名】不能为空');
                }
                if (Tool.isEmpty(loginUser.value.password)) {
                    message.error('【密码】不能为空');
                }
            };

            const showloginModal = () => {
                loginModalVisible.value = true;
            };

            // 退出登录
            const handleLogout = () => {
                console.log("开始退出登录");
                axios.get('/user/logout/' + user.value.token).then((response) => {
                    const data = response.data;
                    if (data.success) {
                        store.commit("setUser", {});  // 触发函数
                        message.success("退出成功！");
                    } else {
                        message.error(data.message);
                    }
                });
                loginUser.value.password = '';
            };
            return {
                loginModalVisible,
                loginModalLoading,
                loginUser,
                showloginModal,
                handleLogin,

                user,

                handleLogout
            }
        },
    });
</script>

<style>
    .login-menu {
        /*float: right; // 菜单使用了flex布局，float失效：参考：http://www.imooc.com/wiki/css3lesson/flex.html*/
        position: absolute;
        color: white;
        padding-left: 30px; /* 当前菜单距离前一个菜单的间隙 */
    }
</style>