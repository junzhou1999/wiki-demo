<template>
  <a-layout>
    <the-header></the-header>
    <router-view/>
    <the-footer></the-footer>
  </a-layout>
</template>

<script lang="ts">
  import {defineComponent} from 'vue';
  // import组件，页面中所对应的组件名字是组件所在script所起的名字
  import TheHeader from '@/components/TheHeader.vue';
  import TheFooter from '@/components/TheFooter.vue';

  export default defineComponent({
    name: 'App',
    components: {
      TheHeader,
      TheFooter,
    },
  });
</script>
<style>
  .ant-row-rtl #components-layout-demo-top-side-2 .logo {
    float: right;
    margin: 16px 0 16px 24px;
  }

  .site-layout-background {
    background: #fff;
  }
</style>
