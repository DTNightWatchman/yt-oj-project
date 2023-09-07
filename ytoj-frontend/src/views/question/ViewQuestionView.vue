<template>
  <div id="viewQuestionsView">
    <a-row :gutter="[24, 24]">
      <a-col :md="12" :xs="24">
        <a-card>
          <a-tabs default-active-key="question">
            <a-tab-pane v-if="question" key="question" title="题目">
              <a-row class="grid-demo" justify="space-between" align="center">
                <a-col :span="16">
                  <div>
                    <h1>{{ question.title }}</h1>
                  </div>
                </a-col>
                <a-col :span="8">
                  <div style="text-align: right">
                    <a-tag
                      v-for="(tag, index) of question.tags"
                      :key="index"
                      color="green"
                      style="margin-right: 10px"
                    >
                      {{ tag }}
                    </a-tag>
                  </div>
                </a-col>
              </a-row>
              <a-descriptions
                title="判题条件"
                :column="{ xs: 1, md: 2, lg: 3 }"
              >
                <a-descriptions-item label="时间限制">
                  {{ question.judgeConfig.timeLimit ?? 0 }}
                </a-descriptions-item>
                <a-descriptions-item label="内存限制">
                  {{ question.judgeConfig.memoryLimit ?? 0 }}
                </a-descriptions-item>
                <a-descriptions-item label="堆栈限制">
                  {{ question.judgeConfig.stackLimit ?? 0 }}
                </a-descriptions-item>
              </a-descriptions>

              <MdReader :value="question.content || ''" />
            </a-tab-pane>
            <a-tab-pane key="comment" title="评论"> 评论区</a-tab-pane>
            <a-tab-pane key="answer" title="答案"> 答案</a-tab-pane>
          </a-tabs>
        </a-card>
      </a-col>
      <a-col :md="12">
        <a-form :model="form" layout="inline">
          <a-form-item
            field="language"
            label="编程语言"
            style="min-width: 240px"
          >
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
        </a-form>

        <CodeEditor
          :value="form.code as string"
          :language="form.language as string"
          :handle-change="changeCode"
        />
        <a-divider size="0" />
        <a-button type="primary" style="min-width: 200px" @click="doSubmit">
          提交代码
        </a-button>
      </a-col>
    </a-row>
  </div>
</template>

<script setup lang="ts">
import { defineProps, onMounted, ref, withDefaults } from "vue";
import {
  QuestionControllerService,
  QuestionSubmitAddRequest,
  QuestionSubmitControllerService,
  QuestionVO,
} from "../../../generated";
import message from "@arco-design/web-vue/es/message";
import CodeEditor from "@/components/CodeEditor.vue";
import MdReader from "@/components/MdReader.vue";

const question = ref<QuestionVO>();

interface Props {
  id: number;
}

const props = withDefaults(defineProps<Props>(), {
  id: () => 0,
});
const loadData = async () => {
  const res = await QuestionControllerService.getQuestionVoByIdUsingGet(
    props.id
  );
  if (res.code == 0) {
    question.value = res.data;
  } else {
    message.error("加载题目失败:" + res.message);
  }
};

const form = ref<QuestionSubmitAddRequest>({
  language: "java",
  code: "",
});

const doSubmit = async () => {
  if (!question.value?.id) {
    return;
  }
  const res = await QuestionSubmitControllerService.doQuestionSubmitUsingPost({
    ...form.value,
    questionId: question.value.id,
  });
  if (res.code === 0) {
    message.success("提交成功");
  } else {
    message.error("提交失败," + res.message);
  }
};

const changeCode = (value: string) => {
  form.value.code = value;
};

onMounted(() => {
  loadData();
});
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
#viewQuestionsView {
  max-width: 1800px;
  margin: 0 auto;
}
</style>
