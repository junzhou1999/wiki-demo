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
            <a-menu-item key="/admin/user">
                <router-link to="/admin/user">用户管理</router-link>
            </a-menu-item>
            <a-menu-item key="/admin/ebook">
                <router-link to="/admin/ebook">电子书管理</router-link>
            </a-menu-item>
            <a-menu-item key="/admin/category">
                <router-link to="/admin/category">分类管理</router-link>
            </a-menu-item>
            <a-menu-item key="/about">
                <router-link to="/about">关于我们</router-link>
            </a-menu-item>
            <a class="login-menu" v-show="user.id">
                <span v-show="user.id">您好：{{user.name}}</span>
            </a>
            <a class="login-menu" @click="showloginModal" v-show="!user.id">
                <span v-show="!user.id">登录</span>
            </a>
        </a-menu>
    </a-layout-header>
    <a-modal
            v-model:visible="loginModalVisible"
            title="登录表单"
            :confirm-loading="loginModalLoading"
            @ok="handleLogin"
    >
        <a-form :model="loginUser" :label-col="{ span:4 }" :wrapper-col="{ span:22 }">
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
    import {defineComponent, ref} from 'vue';
    import axios from "axios";
    import {message} from "ant-design-vue";
    import {Tool} from "@/util/tool";

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

            // 登录后的用户信息
            const user = ref();
            user.value = {};

            // 登录
            const handleLogin = () => {
                console.log("开始登录");
                if (Tool.isNotEmpty(loginUser.value.loginName) && Tool.isNotEmpty(loginUser.value.password)) {
                    loginModalLoading.value = true;
                    loginUser.value.password = hexMd5(loginUser.value.password + KEY);
                    axios.post('/user/login', loginUser.value).then((response) => {
                        const data = response.data;
                        loginModalLoading.value = false;
                        if (data.success) {
                            loginModalVisible.value = false;
                            user.value = data.content;  // 得到返回信息
                            message.success("登录成功！");
                        } else {
                            message.error(data.message);
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

            return {
                loginModalVisible,
                loginModalLoading,
                loginUser,
                showloginModal,
                handleLogin,

                user
            }
        },
    });
</script>

<style>
    .login-menu {
        /*float: right; // 菜单使用了flex布局，float失效：参考：http://www.imooc.com/wiki/css3lesson/flex.html*/
        position: absolute;
        right: 60px; /* 右边边距 */
        color: white;
        padding-left: 10px; /* 当前菜单距离前一个菜单的间隙 */
    }
</style>