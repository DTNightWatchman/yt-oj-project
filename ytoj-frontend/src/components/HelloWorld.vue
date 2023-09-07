<template>
  <!--  <MdEditor :value="value" :handle-change="handleChange" />-->
  <CodeEditor :value="value" :handle-change="handleChange" />
  {{ value }}
</template>

<script setup lang="ts">
import { ref } from "vue";
import CodeEditor from "@/components/CodeEditor.vue";

const value = ref("");

const handleChange = (v: string) => {
  value.value = v;
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped></style>
