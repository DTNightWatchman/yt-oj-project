<template>
  <router-view />
</template>

<style scoped>
#userLayout {
}

#userLayout .header {
  box-shadow: rgb(239, 239, 239) 1px 1px 1px;
}

#userLayout .content {
  background: linear-gradient(to right, #eee, #fff);
  padding: 20px;
}

#userLayout .footer {
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
import LoginHeader from "@/components/LoginHeader.vue";
</script>
