<template>
    <a-layout-footer style="text-align: center">
        电子书@junzhousky<span v-show="user.id">，欢迎：{{user.name}}</span>
    </a-layout-footer>
</template>

<script lang="ts">
    import {defineComponent, computed, onMounted} from 'vue';
    import store from "@/store";
    import {Tool} from "@/util/tool";

    export default defineComponent({
        name: 'the-footer',

        setup() {
            // const user = computed(function () {
            //         return store.state.user;
            //     }
            // );

            const user = computed(
                () => {
                    return store.state.user;
                });
            let webSocket: any;
            let wsToken: any;
            const onOpen = () => {
                console.log('WebSocket连接成功，状态码：', webSocket.readyState)
            };
            const onMessage = (event: any) => {
                console.log('WebSocket收到消息：', event.data);
            };
            const onError = () => {
                console.log('WebSocket连接错误，状态码：', webSocket.readyState)
            };
            const onClose = () => {
                console.log('WebSocket连接关闭，状态码：', webSocket.readyState)
            };
            // 绑定函数
            const initWebSocket = () => {
                // 连接成功
                webSocket.onopen = onOpen;
                // 收到消息的回调
                webSocket.onmessage = onMessage;
                // 连接错误
                webSocket.onerror = onError;
                // 连接关闭的回调
                webSocket.onclose = onClose;
            };
            onMounted(() => {
                if ('WebSocket' in window) {
                    wsToken = Tool.uuid(10)
                    // 地址：ws://127.0.0.1:5921/ws/xxx
                    webSocket = new WebSocket(process.env.VUE_APP_WS_SERVER + '/ws/' + wsToken);
                    initWebSocket();
                    // 关闭
                    // webSocket.close();
                } else {
                    alert('当前浏览器不支持！');
                }
            });

            return {
                user
            }
        }
    });
</script>