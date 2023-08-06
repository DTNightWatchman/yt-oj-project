<template>
  <div id="userLayout" class="userLoginView">
    <div id="contentClass">
      <div
        class="logoClass"
        :style="{
          maxWidth: '200px',
          textAlign: 'center',
          margin: '0 auto',
        }"
      >
        <img
          class="logo"
          src="http://127.0.0.1:9000/oj-object-bucket/1691314587255.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=oj-project-user%2F20230806%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20230806T094302Z&X-Amz-Expires=604800&X-Amz-SignedHeaders=host&X-Amz-Signature=c5d9f58b952a3d6b5f1ffa11a153d4485c043e3bae7e4118c046f2d0656cdf12"
        />
        <div style="max-width: 140px" class="title">在线OJ系统</div>
      </div>
      <a-form
        label-align="left"
        :model="form"
        :style="{
          maxWidth: '450px',
          textAlign: 'center',
          margin: '0 auto',
          marginTop: '20px',
        }"
        @submit="handleSubmit"
      >
        <a-form-item field="userAccount" tooltip="请输入账号" label="用户账号">
          <a-input v-model="form.userAccount" placeholder="请输入账号" />
        </a-form-item>
        <a-form-item field="userPassword" tooltip="请输入密码" label="密码">
          <a-input-password
            v-model="form.userPassword"
            placeholder="请输入密码"
          />
        </a-form-item>
        <a-form-item>
          <a-button
            type="primary"
            html-type="submit"
            style="margin-left: 15%; width: 150px"
            >登录
          </a-button>
        </a-form-item>
      </a-form>
    </div>
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
  // alert(JSON.stringify(form));
};
</script>

<style>
#userLayout {
  height: 100vh;
  background: url("../../assets/loginBackground.png") 0% 0% / 100% 100%;
}

#contentClass {
  padding: 10%;
}

.logoClass {
  display: flex;
  height: 50px;
  text-align: center;
}

.logoClass .logo {
  margin-right: 10px;
}

.logoClass .title {
  text-align: center;
  font-size: 25px;
  font-weight: 1000;
  line-height: 50px;
  color: rgb(24, 144, 255);
}
</style>
