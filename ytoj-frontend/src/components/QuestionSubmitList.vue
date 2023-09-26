<template>
  <div id="questionSubmitView">
    <a-form :model="form" layout="inline" style="position: static; right: 0">
      <a-form-item field="language" label="编程语言" style="width: 240px">
        <a-select
          v-model="form.language"
          :style="{ width: '320px' }"
          placeholder="选择编程语言"
        >
          <a-option>java</a-option>
          <a-option>cpp</a-option>
          <a-option>go</a-option>
          <a-option>html</a-option>
        </a-select>
      </a-form-item>
      <a-form-item style="max-width: 300px" field="tags" label="判题结果">
        <a-select
          v-model="form.status"
          :style="{ width: '320px' }"
          placeholder="选择判题结果"
        >
          <a-option>success</a-option>
          <a-option>Wrong Answer</a-option>
          <a-option>Compile Error</a-option>
          <a-option>Memory Limit Exceeded</a-option>
          <a-option>Time Limit Exceeded</a-option>
          <a-option>Presentation Error</a-option>
          <a-option>Output Limit Exceeded</a-option>
          <a-option>Waiting</a-option>
          <a-option>Dangerous Operation</a-option>
          <a-option>Runtime Error</a-option>
        </a-select>
      </a-form-item>
      <a-form-item>
        <a-button type="primary" @click="doSumbit">搜索</a-button>
      </a-form-item>
    </a-form>
    <a-divider size="0" />
    <a-table
      :columns="columns"
      :data="dataList"
      :pagination="{
        showTotal: true,
        pageSize: form.pageSize,
        current: form.current,
        total: total,
      }"
      @page-change="onPageChange"
    >
    </a-table>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, watchEffect } from "vue";
import {
  QuestionControllerService,
  QuestionSubmitQueryRequest,
  QuestionUpdateRequest,
} from "../../generated";
import message from "@arco-design/web-vue/es/message";
import { useRoute, useRouter } from "vue-router";

const router = useRouter();
const route = useRoute();

const dataList = ref([]);
const total = ref(0);
const form = ref<QuestionSubmitQueryRequest>({
  language: "java",
  pageSize: 7,
  sortField: "createTime",
  sortOrder: "descend",
  // status: 1,
  questionId: route.params?.id as unknown as number,
});

const loadData = async () => {
  try {
    const res =
      await QuestionControllerService.listMyQuestionSubmitByPageUsingPost(
        form.value
      );
    if (res.code === 0) {
      dataList.value = res.data.records;
      total.value = res.data.total;
    } else {
      message.error("加载失败:" + res.message);
    }
  } catch (e) {
    message.error(e as string);
  }
};

const onPageChange = (page: number) => {
  form.value = {
    ...form.value,
    current: page,
  };
};

watchEffect(() => {
  loadData();
});

onMounted(() => {
  loadData();
});
const columns = [
  {
    title: "编程语言",
    dataIndex: "language",
  },
  {
    title: "判题结果",
    dataIndex: "judgeInfo.message",
  },
  {
    title: "运行内存",
    dataIndex: "judgeInfo.memory",
  },
  {
    title: "运行时间",
    dataIndex: "judgeInfo.time",
  },
  {
    title: "判题状态",
    dataIndex: "status",
  },
  {
    title: "提交时间",
    dataIndex: "createTime",
  },
];

const toQuestionPage = async (question: QuestionUpdateRequest) => {
  window.open(`/view/question/${question.id}`);
};

const doSumbit = async () => {
  form.value = {
    ...form.value,
    current: 1,
  };
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
#questionSubmitView {
  max-width: 1280px;
  margin: 0 auto;
}
</style>
