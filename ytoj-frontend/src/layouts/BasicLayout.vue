<template>
  <div id="basicLayout">
    <a-layout style="min-height: 100vh">
      <a-layout-header class="header">
        <GlobalHeader />
      </a-layout-header>
      <a-layout-content class="content">
        <router-view />
      </a-layout-content>
      <a-layout-footer class="footer">
        <a-link href="https://gitee.com/YTbaiduren/yt-oj-project">
          <template #icon>
            <icon-github />
          </template>
          YT OJ by YT摆渡人
        </a-link>
      </a-layout-footer>
    </a-layout>
  </div>
</template>

<style scoped>
#basicLayout {
}

#basicLayout .header {
  box-shadow: rgb(239, 239, 239) 1px 1px 1px;
}

#basicLayout .content {
  background: linear-gradient(to right, #eee, #fff);
  padding: 20px;
}

#basicLayout .footer {
  background: #efefef;
  padding: 16px;
  position: sticky;
  bottom: 0;
  left: 0;
  right: 0;
  text-align: center;
}

header.arco-layout-header.header {
  height: 68px;
}
</style>
<script setup lang="ts">
import { IconGithub } from "@arco-design/web-vue/es/icon";
import GlobalHeader from "@/components/GlobalHeader.vue";
</script>
