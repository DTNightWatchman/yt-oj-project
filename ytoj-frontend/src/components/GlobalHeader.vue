<template>
  <a-row
    id="globalHeader"
    class="grid-demo"
    style="margin-bottom: 16px; background-color: rgb(35, 35, 36)"
    align="center"
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
            <div class="title-bar">
              <img
                class="logo"
                src="../assets/logo.png"
                alt="logo"
                width="20"
              />
              <div class="title">YT OJ</div>
            </div>
          </a-menu-item>
          <a-menu-item v-for="item in routes" :key="item.path">
            {{ item.name }}
          </a-menu-item>
        </a-menu>
      </div>
    </a-col>
    <a-col flex="100px">
      <div>
        <div style="color: white">
          {{ store.state.user.loginUser.username ?? "未登录账号" }}
        </div>
      </div>
    </a-col>
  </a-row>
</template>

<script setup lang="ts">
import { routes } from "@/router/routes";
import { useRoute, useRouter } from "vue-router";
import { ref } from "vue";
import { useStore } from "vuex";

const router = useRouter();
const route = useRoute();
const selectKey = ref(["/"]);

// 路由跳转后，更新选中的菜单项
router.afterEach((to, from, failure) => {
  selectKey.value = [to.path];
});

const doMenuClick = (key: string) => {
  router.push({
    path: key,
  });
};

const store = useStore();
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
</style>
