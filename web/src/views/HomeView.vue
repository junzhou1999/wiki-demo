<template>
    <a-layout>
        <a-layout-sider width="200" style="background: #fff">
            <a-menu
                    mode="inline"
                    :style="{ height: '100%', borderRight: 0 }"
            >
                <a-sub-menu key="sub1">
                    <template #title>
              <span>
                <user-outlined/>
                subnav 111
              </span>
                    </template>
                    <a-menu-item key="1">option1</a-menu-item>
                    <a-menu-item key="2">option2</a-menu-item>
                    <a-menu-item key="3">option3</a-menu-item>
                    <a-menu-item key="4">option4</a-menu-item>
                </a-sub-menu>
                <a-sub-menu key="sub2">
                    <template #title>
              <span>
                <laptop-outlined/>
                subnav 2
              </span>
                    </template>
                    <a-menu-item key="5">option5</a-menu-item>
                    <a-menu-item key="6">option6</a-menu-item>
                    <a-menu-item key="7">option7</a-menu-item>
                    <a-menu-item key="8">option8</a-menu-item>
                </a-sub-menu>
                <a-sub-menu key="sub3">
                    <template #title>
              <span>
                <notification-outlined/>
                subnav 3
              </span>
                    </template>
                    <a-menu-item key="9">option9</a-menu-item>
                    <a-menu-item key="10">option10</a-menu-item>
                    <a-menu-item key="11">option11</a-menu-item>
                    <a-menu-item key="12">option12</a-menu-item>
                </a-sub-menu>
            </a-menu>
        </a-layout-sider>
        <a-layout-content
                :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
        >
            <pre>
{{ebooks}}
            </pre>
        </a-layout-content>
    </a-layout>
</template>

<script lang="ts">
    import {defineComponent, onMounted, reactive, toRef} from 'vue';
    import axios from 'axios';

    export default defineComponent({
        name: 'HomeView',
        setup() {
            console.log('setup()');
            /**
             * reactive里面放的是对象，book属性默认是空数组
             * 这样ebooks对象的book属性就变成响应式变量
             */
            const ebooks = reactive({books: []});  //1

            onMounted(() => {
                console.log('onMounted()');
                axios.get('http://localhost:5921/ebook/list?name=Oracle').then(
                    (response) => {
                        console.log(response);
                        const data = response.data;
                        ebooks.books = data.content;  //2
                    });
            });  // onMounted

            return {
                // toRef(对象,属性)
                ebooks: toRef(ebooks, 'books')  //3，这里的返回名称任意，用于html引用
            }
        }  // setup
    });
</script>
