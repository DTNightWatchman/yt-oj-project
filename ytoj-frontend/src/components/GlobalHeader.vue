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
        <a-dropdown>
          <div
            v-if="
              store.state?.user?.loginUser.userName &&
              store.state?.user?.loginUser.userName !== '未登录'
            "
          >
            <a-avatar id="avatar">
              <img
                alt="avatar"
                :src="store.state?.user?.loginUser.userAvatar"
              />
            </a-avatar>
          </div>

          <a-button v-else style="margin-right: 10px">未登录账号</a-button>
          <template #content>
            <a-doption>
              <router-link class="router-link-exact-active" to="/about"
                >关于我的
              </router-link>
            </a-doption>
            <a-dgroup>
              <a-doption
                v-if="
                  store.state?.user?.loginUser.userName &&
                  store.state?.user?.loginUser.userName !== '未登录'
                "
                @click="doLogout"
                >退出登录
              </a-doption>
              <a-doption v-else>现在登录</a-doption>
            </a-dgroup>
          </template>
        </a-dropdown>
      </div>
    </a-col>
  </a-row>
</template>

<script setup lang="ts">
import { routes } from "@/router/routes";
import { useRoute, useRouter } from "vue-router";
import { computed, onMounted, ref } from "vue";
import { useStore } from "vuex";
import checkAccess from "@/access/checkAccess";
import accessEnum from "@/access/accessEnum";
import { UserControllerService } from "../../generated";

const store = useStore();
const router = useRouter();
const route = useRoute();
const selectKey = ref(["/"]);

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

const doLogout = async () => {
  await UserControllerService.userLogoutUsingPost();
  router.push("/user/login");
};

console.log(store.state);
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

avatar:hover {
  cursor: pointer;
}

/* 移除<router-link>的下划线 */
.router-link-exact-active {
  text-decoration: none;
  color: black;
}
</style>
