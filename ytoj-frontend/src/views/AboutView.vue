<template>
  <a-space
    direction="vertical"
    size="large"
    fill
    style="display: flex; justify-content: center"
  >
    <a-card
      :style="{
        maxWidth: '1400px',
        margin: '0 auto',
        padding: '15px',
        display: 'flex',
      }"
    >
      <a-row class="grid-demo" style="min-width: 242%">
        <a-col :span="6">
          <a-space size="large">
            <a-avatar :size="120">Arco</a-avatar>
          </a-space>
        </a-col>
        <a-col :span="18">
          <a-descriptions
            column="2"
            size="large"
            style="max-width: 1200px; margin: 0 auto"
            :data="data"
            layout="horizontal"
          />
        </a-col>
      </a-row>
    </a-card>
    <a-card
      :style="{
        maxWidth: '1400px',
        margin: '0 auto',
        padding: '15px',
        display: 'flex',
      }"
    >
      <a-form
        ref="formRef"
        size="Medium"
        :model="form"
        :style="{ width: '600px' }"
        @submit="handleSubmit"
      >
        <a-form-item
          field="name"
          label="Username"
          :rules="[
            { required: true, message: 'name is required' },
            { minLength: 5, message: 'must be greater than 5 characters' },
          ]"
          :validate-trigger="['change', 'input']"
        >
          <a-input
            v-model="form.name"
            placeholder="please enter your username..."
          />
        </a-form-item>
        <a-form-item
          field="section"
          label="Section"
          :rules="[{ match: /section one/, message: 'must select one' }]"
        >
          <a-select
            v-model="form.section"
            placeholder="Please select ..."
            allow-clear
          >
            <a-option value="section one">Section One</a-option>
            <a-option value="section two">Section Two</a-option>
            <a-option value="section three">Section Three</a-option>
          </a-select>
        </a-form-item>
        <a-form-item
          field="province"
          label="Province"
          :rules="[{ required: true, message: 'province is required' }]"
        >
          <a-cascader
            v-model="form.province"
            :options="options"
            placeholder="Please select ..."
            allow-clear
            style="height: 10px"
          />
        </a-form-item>
        <a-form-item>
          <a-space>
            <a-button html-type="submit">Submit</a-button>
            <a-button @click="$refs.formRef.resetFields()">Reset</a-button>
          </a-space>
        </a-form-item>
      </a-form>
    </a-card>
  </a-space>
</template>
<script setup lang="ts">
// https://arco.design/vue/component/descriptions
import { reactive } from "vue";

const data = [
  {
    label: "User Name",
    value: "王立群",
  },
  {
    label: "Account ID",
    value: "fzly-50034297",
  },
  {
    label: "Residence",
    value: "Beijing",
  },
  {
    label: "Hometown",
    value: "177******52",
  },
  {
    label: "Registration time",
    value: "1970-11-25 06:49:35",
  },
];
const handleSubmit = ({ values, errors }) => {
  console.log("values:", values, "\nerrors:", errors);
};

const form = reactive({
  size: "medium",
  name: "",
  age: undefined,
  section: "",
  province: "haidian",
  time: "",
  radio: "radio one",
  score: 5,
  switch: false,
  multiSelect: ["section one"],
  treeSelect: "",
});
const options = [
  {
    value: "beijing",
    label: "Beijing",
    children: [
      {
        value: "chaoyang",
        label: "ChaoYang",
        children: [
          {
            value: "datunli",
            label: "Datunli",
          },
        ],
      },
      {
        value: "haidian",
        label: "Haidian",
      },
      {
        value: "dongcheng",
        label: "Dongcheng",
      },
      {
        value: "xicheng",
        label: "XiCheng",
      },
    ],
  },
  {
    value: "shanghai",
    label: "Shanghai",
    children: [
      {
        value: "shanghaishi",
        label: "Shanghai",
        children: [
          {
            value: "huangpu",
            label: "Huangpu",
          },
        ],
      },
    ],
  },
];
const treeData = [
  {
    key: "node1",
    title: "Node1",
    children: [
      {
        key: "node2",
        title: "Node2",
      },
    ],
  },
  {
    key: "node3",
    title: "Node3",
    children: [
      {
        key: "node4",
        title: "Node4",
      },
      {
        key: "node5",
        title: "Node5",
      },
    ],
  },
];
</script>
