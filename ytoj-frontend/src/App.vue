<template>
  <div id="app">
    <BasicLayout />
  </div>
</template>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  //text-align: center; color: #2c3e50;
}
</style>
<script setup lang="ts">
import BasicLayout from "@/layouts/BasicLayout.vue";
import { useRouter } from "vue-router";
import { useStore } from "vuex";

const router = useRouter();
const store = useStore();
router.beforeEach((to, from, next) => {
  // 仅管理员可见
  if (
    to.meta?.access === "canAdmin" &&
    store.state.user.loginUser?.role != null
  ) {
    next("/noAuth");
    return;
  }
  next();
  console.log(to);
});
</script>
