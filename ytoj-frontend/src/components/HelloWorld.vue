<template>
  <MdEditor :value="value" :handle-change="handleChange" />
  <div>hello</div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import MdEditor from "@/components/MdEditor.vue";

const value = ref("");

const handleChange = (v: string) => {
  value.value = v;
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped></style>
