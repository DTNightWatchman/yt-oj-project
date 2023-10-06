<template>
  <div id="addQuestionView">
    <h2>创建题目</h2>
    <a-form :model="form">
      <a-form-item field="title" label="标题">
        <a-input v-model="form.title" placeholder="请输入标题" />
      </a-form-item>
      <a-form-item field="tags" label="标签">
        <a-input-tag v-model="form.tags" placeholder="请选择标签" allow-clear />
      </a-form-item>
      <a-form-item field="content" label="题目内容">
        <MdEditor
          :value="form.content as string"
          :handle-change="onContentChange"
        />
      </a-form-item>
      <a-form-item field="answer" label="答案">
        <MdEditor
          :value="form.answer as string"
          :handle-change="onAnswerChange"
        />
      </a-form-item>

      <a-form-item label="题目限制" :content-flex="false" :merge-props="false">
        <a-space direction="vertical" style="min-width: 480px">
          <a-form-item field="judgeConfig.timeLimit" label="时间限制">
            <a-input-number
              v-model="form.judgeConfig.timeLimit"
              placeholder="请输入时间消耗"
              mode="button"
              min="0"
              size="large"
            />
          </a-form-item>
          <a-form-item field="judgeConfig.stackLimit" label="堆栈限制">
            <a-input-number
              v-model="form.judgeConfig.stackLimit"
              placeholder="请输入堆栈限制"
              mode="button"
              min="0"
              size="large"
            />
          </a-form-item>
          <a-form-item field="judgeConfig.memoryLimit" label="内存限制">
            <a-input-number
              v-model="form.judgeConfig.memoryLimit"
              placeholder="请输入内存限制"
              mode="button"
              min="0"
              size="large"
            />
          </a-form-item>
        </a-space>
      </a-form-item>
      <a-form-item
        label="测试用例配置"
        :content-flex="false"
        :merge-props="false"
      >
        <a-form :model="form" :style="{ minWidth: '100%' }" label-align="left">
          <a-form-item
            v-for="(judgeCaseItem, index) of form.judgeCase"
            :field="`form.judgeCase[${index}].input`"
            :label="`测试用例-${index}`"
            :key="index"
          >
            <a-form-item
              :field="`form.judgeCase[${index}].input`"
              :label="`输入用例-${index}`"
              :key="index"
              :style="{ minWidth: '400px', marginRight: '20px' }"
            >
              <a-textarea
                v-model="judgeCaseItem.input"
                placeholder="请输入测试输入用例"
              />
              <!--              <a-input-->
              <!--                v-model="judgeCaseItem.input"-->
              <!--                placeholder="请输入测试输入用例"-->
              <!--              />-->
            </a-form-item>

            <a-form-item
              :field="`form.judgeCase[${index}].output`"
              :label="`输出用例-${index}`"
              :key="index"
              :style="{ minWidth: '400px' }"
            >
              <a-textarea
                v-model="judgeCaseItem.output"
                placeholder="请输入测试输入用例"
              />
            </a-form-item>
            <a-button
              @click="handleDelete(index)"
              :style="{ marginLeft: '10px' }"
              status="danger"
              >删除
            </a-button>
          </a-form-item>
          <div>
            <a-button @click="handleAdd" type="online" status="success"
              >新增测试用例
            </a-button>
          </div>
        </a-form>
      </a-form-item>

      <a-form-item>
        <a-button type="primary" @click="doSubmit">提交</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from "vue";
import MdEditor from "@/components/MdEditor.vue";
import { QuestionControllerService } from "../../../generated";
import message from "@arco-design/web-vue/es/message";
import { useRoute } from "vue-router";

const route = useRoute();
// 判断是否是更新的路由
const updatePage = route.path.includes("update");

const form = ref({
  title: "",
  tags: [],
  answer: "",
  content: "",
  judgeConfig: {
    memoryLimit: 1000,
    stackLimit: 1000,
    timeLimit: 1000,
  },
  judgeCase: [
    {
      input: "",
      output: "",
    },
  ],
});

/**
 * 根据id获取数据
 */
const loadData = async () => {
  const id = route.params.questionId;
  if (!id) {
    return;
  }
  const res = await QuestionControllerService.getQuestionByIdUsingGet(
    id as any
  );
  console.log(res);
  if (res.code === 0) {
    form.value = res.data as any;
    if (!form.value.judgeCase) {
      form.value.judgeCase = [];
    }
    if (!form.value.judgeConfig) {
      form.value.judgeConfig = {
        memoryLimit: 1000,
        stackLimit: 1000,
        timeLimit: 1000,
      };
    } else {
      if (form.value.judgeConfig.memoryLimit) {
        form.value.judgeConfig.memoryLimit = parseInt(
          form.value.judgeConfig.memoryLimit.toString()
        );
      }
      if (form.value.judgeConfig.stackLimit) {
        form.value.judgeConfig.stackLimit = parseInt(
          form.value.judgeConfig.stackLimit.toString()
        );
      }
      if (form.value.judgeConfig.timeLimit) {
        form.value.judgeConfig.timeLimit = parseInt(
          form.value.judgeConfig.timeLimit.toString()
        );
      }
    }
  } else {
    message.error("加载失败");
  }
};

onMounted(() => {
  loadData();
});

const handleAdd = () => {
  form?.value?.judgeCase?.push({
    input: "",
    output: "",
  });
};
const handleDelete = (index: number) => {
  form.value.judgeCase?.splice(index, 1);
};

const onContentChange = (value: string) => {
  form.value.content = value;
};

const onAnswerChange = (value: string) => {
  form.value.answer = value;
};

const doSubmit = async () => {
  console.log(form.value);
  if (updatePage) {
    try {
      const res = await QuestionControllerService.updateQuestionUsingPost(
        form.value
      );
      if (res.code === 0) {
        message.success("更新成功");
      } else {
        message.error("更新失败:" + res.message);
      }
    } catch (e) {
      message.error(e as string);
    }
    return;
  }
  try {
    const res = await QuestionControllerService.addQuestionUsingPost(
      form.value
    );
    if (res.code === 0) {
      message.success("创建成功");
    } else {
      message.error("创建失败:" + res.message);
    }
  } catch (e) {
    message.error(e as string);
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped></style>
