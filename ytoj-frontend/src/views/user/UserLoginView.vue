<template>
  <div class="userLoginView">
    <a-form :model="form" :style="{ width: '600px' }" @submit="handleSubmit">
      <a-form-item field="userAccount" tooltip="请输入账号" label="用户账号">
        <a-input
          v-model="form.userAccount"
          placeholder="please enter your username..."
        />
      </a-form-item>
      <a-form-item field="userPassword" label="密码">
        <a-input-password
          v-model="form.userPassword"
          placeholder="请输入密码"
        />
      </a-form-item>
      <a-form-item>
        <a-button html-type="submit">提交</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>
<script setup lang="ts">
import { reactive } from "vue";
import {
  BaseResponse_LoginUserVO_,
  UserControllerService,
  UserLoginRequest,
} from "../../../generated";
import message from "@arco-design/web-vue/es/message";

const form = reactive({
  userAccount: "",
  userPassword: "",
} as UserLoginRequest);
/**
 * 提交表单
 * @param data
 */
const handleSubmit = async () => {
  try {
    const res: BaseResponse_LoginUserVO_ =
      await UserControllerService.userLoginUsingPost(form);
    if (res.code === 0) {
      message.success("登录成功:" + res.data);
    } else {
      message.error("登录失败:" + res.message);
    }
  } catch (e) {
    message.error("登录服务异常");
  }
  alert(JSON.stringify(form));
};
</script>
