<template>
  <div
    id="code-editor"
    ref="codeEditorRef"
    style="min-height: 523px; height: 59vh"
  ></div>
</template>

<script setup lang="ts">
import * as monaco from "monaco-editor";
import { onMounted, ref, toRaw, watch } from "vue";
import { defineProps, withDefaults } from "vue/dist/vue";

interface Props {
  value: string;
  language: string;
  handleChange: (v: string) => void;
}

const props = withDefaults(defineProps<Props>(), {
  value: () => "",
  language: () => "java",
  handleChange: (v: string) => {
    console.log(v);
  },
});

const codeEditorRef = ref();
const codeEditor = ref();

watch(
  () => props.language,
  () => {
    codeEditor.value = monaco.editor.create(codeEditorRef.value, {
      value: props.value,
      language: props.language,
      automaticLayout: true,
      minimap: {
        enabled: true,
      },
      // lineNumbers: "off",
      // roundedSelection: false,
      // scrollBeyondLastLine: false,
      readOnly: false,
      theme: "vs-dark",
    });
  }
);

onMounted(() => {
  if (!codeEditorRef.value) {
    return;
  }
  // Hover on each property to see its docs!
  codeEditor.value = monaco.editor.create(codeEditorRef.value, {
    value: props.value,
    language: props.language,
    automaticLayout: true,
    minimap: {
      enabled: true,
    },
    // lineNumbers: "off",
    // roundedSelection: false,
    // scrollBeyondLastLine: false,
    readOnly: false,
    theme: "vs-dark",
  });

  codeEditor.value.onDidChangeModelContent(() => {
    props.handleChange(toRaw(codeEditor.value).getValue());
  });
});
</script>

<style scoped></style>