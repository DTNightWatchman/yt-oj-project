<template>
  <a-row
    id="globalHeader"
    class="grid-demo"
    style="margin-bottom: 16px; background-color: rgb(35, 35, 36)"
    align="center"
    :wrap="false"
  >
    <a-col flex="auto">
      <div>
        <a-menu
          mode="horizontal"
          theme="dark"
          :selected-keys="selectKey"
          :default-selected-keys="['1']"
          @menu-item-click="doMenuClick"
        >
          <a-menu-item
            key="0"
            :style="{ padding: 0, marginRight: '38px' }"
            disabled
          >
            <div class="title-bar" style="cursor: pointer">
              <img
                class="logo"
                src="../assets/logo.png"
                alt="logo"
                :width="20"
              />
              <div class="title">YT OJ</div>
            </div>
          </a-menu-item>
          <a-menu-item v-for="item in visibleRoutes" :key="item.path">
            {{ item.name }}
          </a-menu-item>
        </a-menu>
      </div>
    </a-col>
    <a-col flex="100px">
      <div>
        <div style="color: white">
          {{ store.state?.user?.loginUser.userName ?? "未登录账号" }}
        </div>
      </div>
    </a-col>
  </a-row>
</template>

<script setup lang="ts">
import { routes } from "@/router/routes";
import { useRoute, useRouter } from "vue-router";
import { computed, onMounted, ref, watch, watchEffect } from "vue";
import { useStore } from "vuex";
import checkAccess from "@/access/checkAccess";
import ACCESS_ENUM from "@/access/accessEnum";
import {
  BaseResponse_LoginUserVO_,
  UserControllerService,
} from "../../generated";
import message from "@arco-design/web-vue/es/message";

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

onMounted(() => {
  selectKey.value = [route.path];
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

// onMounted(async () => {
//   try {
//     const res: BaseResponse_LoginUserVO_ =
//       await UserControllerService.getLoginUserUsingGet();
//     if (res.code === 0) {
//       await store.dispatch("user/getLoginUser", {
//         userName: res.data?.userName,
//         userRole: res.data?.userRole,
//       });
//       await router.push("/");
//     } else {
//       message.error("登录失败:" + res.message);
//     }
//   } catch (e) {
//     message.error("登录服务异常");
//   }
// });
// setTimeout(() => {
//   console.log(visibleRoutes.value);
//   store.dispatch("user/getLoginUser", {
//     userName: "摆渡人",
//     userRole: ACCESS_ENUM.ADMIN,
//   });
//   console.log(store.state.user.loginUser);
// }, 3000);
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
