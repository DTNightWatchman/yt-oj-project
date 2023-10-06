<template>
  <div id="questionsView">
    <a-form :model="searchParams" layout="inline">
      <a-form-item style="min-width: 240px" field="title" label="名称">
        <a-input v-model="searchParams.title" placeholder="请输入搜索名称" />
      </a-form-item>
      <a-form-item style="min-width: 240px" field="tags" label="标签">
        <a-input-tag v-model="searchParams.tags" placeholder="请输入标签" />
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
        pageSize: searchParams.pageSize,
        current: searchParams.current,
        total: total,
      }"
      @page-change="onPageChange"
    >
      <template #optional="{ record }">
        <a-space>
          <a-button type="primary" @click="toQuestionPage(record)"
            >做题
          </a-button>
        </a-space>
      </template>
      <template #tags="{ record }">
        <a-space wrap>
          <a-tag v-for="(tag, index) of record.tags" :key="index" color="green">
            {{ tag }}
          </a-tag>
        </a-space>
      </template>
      <template #createTime="{ record }">
        {{ record.createTime }}
      </template>
    </a-table>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, watchEffect } from "vue";
import {
  QuestionControllerService,
  QuestionQueryRequest,
  QuestionUpdateRequest,
} from "../../../generated";
import message from "@arco-design/web-vue/es/message";
import { useRouter } from "vue-router";

const router = useRouter();

const dataList = ref([]);
const total = ref(0);

const form = ref({
  name: "",
  post: "",
  isRead: false,
});

const searchParams = ref<QuestionQueryRequest>({
  title: "",
  tags: [],
  pageSize: 7,
  current: 1,
});

const loadData = async () => {
  try {
    const res =
      await QuestionControllerService.listQuestionShowVoByPageUsingPost(
        searchParams.value
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
  searchParams.value = {
    ...searchParams.value,
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
    title: "题号",
    dataIndex: "id",
  },
  {
    title: "标题",
    dataIndex: "title",
  },
  {
    title: "标签",
    slotName: "tags",
  },
  {
    title: "通过率",
    dataIndex: "acceptedRate",
  },
  {
    title: "创建时间",
    slotName: "createTime",
  },
  {
    title: "Optional",
    slotName: "optional",
  },
];

const toQuestionPage = async (question: QuestionUpdateRequest) => {
  window.open(`/view/question/${question.id}`);
};

const doSumbit = async () => {
  searchParams.value = {
    ...searchParams.value,
    current: 1,
  };
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
#questionsView {
  max-width: 1280px;
  margin: 0 auto;
}
</style>
