<template>
  <div class="about">
    <h1>This is an about page</h1>
    <div>
      <button onclick="func">get</button>
    </div>
  </div>
</template>
<script setup lang="ts">
const func = () => {
  alert("hello");
};
</script>
