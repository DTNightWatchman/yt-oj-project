<template>
  <div id="manageQuestionView">
    <h2>题目管理</h2>
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
          <a-button type="primary" @click="doUpdate(record)">修改</a-button>
          <a-button status="danger" @click="doDelete(record)">删除</a-button>
        </a-space>
      </template>
      <template #tags="{ record }">
        <a-space wrap>
          <a-tag v-for="(tag, index) of record.tags" :key="index" color="green">
            {{ tag }}
          </a-tag>
        </a-space>
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

const show = ref(true);

const dataList = ref([]);
const total = ref(0);

const searchParams = ref<QuestionQueryRequest>({
  pageSize: 10,
  current: 1,
});

const loadData = async () => {
  try {
    const res = await QuestionControllerService.listQuestionVoByPageUsingPost(
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
//{id: "1696167116826931202", title: "A + B", content: "题目内容", tags: ["A", "B"],…}
const columns = [
  {
    title: "id",
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
    title: "提交数",
    dataIndex: "submitNum",
  },
  {
    title: "通过数",
    dataIndex: "acceptedNum",
  },
  {
    title: "用户id",
    dataIndex: "userId",
  },
  {
    title: "创建时间",
    dataIndex: "createTime",
  },
  {
    title: "Optional",
    slotName: "optional",
  },
];
const doDelete = async (question: any) => {
  const res = await QuestionControllerService.deleteQuestionUsingPost({
    id: question.id,
  });
  if (res.code === 0) {
    message.success("删除成功");
    loadData();
  } else {
    message.error("删除失败");
  }
};
const doUpdate = async (question: QuestionUpdateRequest) => {
  router.push({
    path: `/update/question/${question.id}`,
  });
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped></style>
