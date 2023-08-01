<template>
  <div id="app">
    <template v-if="route.path.startsWith('/user')">
      <UserLayout />
    </template>
    <template v-else>
      <BasicLayout />
    </template>
  </div>
</template>

<style></style>
<script setup lang="ts">
import BasicLayout from "@/layouts/BasicLayout.vue";
import { onMounted } from "vue";
import { useRoute } from "vue-router";
import UserLayout from "@/layouts/UserLayout.vue";

const route = useRoute();
/**
 * 全局初始化，全局单次调用的代码
 */
const doInit = () => {
  console.log("hello 初始化");
};

onMounted(() => {
  doInit();
});
</script>
