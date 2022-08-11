<template>
    <a-layout-footer style="text-align: center">
        电子书@junzhousky{{user.name}}
    </a-layout-footer>
</template>

<script lang="ts">
    import {defineComponent, computed} from 'vue';
    import store from "@/store";

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

            return {
                user
            }
        }
    });
</script>