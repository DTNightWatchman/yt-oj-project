<template>
  <div>登录用户</div>
</template>

<script setup lang="ts">
import { routes } from "@/router/routes";
import { useRoute, useRouter } from "vue-router";
import { computed, ref, watch, watchEffect } from "vue";
import { useStore } from "vuex";
import checkAccess from "@/access/checkAccess";
import ACCESS_ENUM from "@/access/accessEnum";

const store = useStore();
const router = useRouter();
const route = useRoute();
const selectKey = ref(["/"]);

const loginUser = ref(store.state.user.loginUser);

// 展示在菜单的路由
const visibleRoutes = computed(() => {
  const loginUser = store.state.user.loginUser;

  return routes.filter((item, index) => {
    if (item.meta?.hideInMenu) {
      return false;
    }

    if (!checkAccess(loginUser, item?.meta?.access as string)) {
      return false;
    }

    return true;
  });
});

// 路由跳转后，更新选中的菜单项
router.afterEach((to, from, failure) => {
  selectKey.value = [to.path];
});

const doMenuClick = (key: string) => {
  router.push({
    path: key,
  });
};

console.log(store.state);

setTimeout(() => {
  console.log(visibleRoutes.value);
  store.dispatch("user/getLoginUser", {
    userName: "摆渡人",
    userRole: ACCESS_ENUM.ADMIN,
  });
  console.log(store.state.user.loginUser);
}, 3000);

setTimeout(() => {
  console.log("???", visibleRoutes.value);
}, 6000);
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.title-bar {
  align-items: center;
  display: flex;
}

.arco-menu {
  font-size: 18px;
}

img {
  width: 38px;
}

.title {
  color: white;
  font-size: larger;
  font-weight: lighter;
  margin-left: 10px;
}
</style>
