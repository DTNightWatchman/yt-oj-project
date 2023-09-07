// initial state
import { StoreOptions } from "vuex";
import ACCESS_ENUM from "@/access/accessEnum";
import { UserControllerService } from "../../generated";

// getters
const getters = {};

export default {
  namespaced: true,
  state: () => ({
    loginUser: {
      userName: "未登录",
      userRole: ACCESS_ENUM.NOT_LOGIN,
    },
  }),
  getters,
  actions: {
    async getLoginUser({ commit, state }, payload) {
      // 缓存请求服务端获取用户信息
      const res = await UserControllerService.getLoginUserUsingGet();
      //alert("??");
      if (res.code === 0) {
        commit("updateUser", res.data);
      } else {
        commit("updateUser", {
          ...state.loginUser,
          userRole: ACCESS_ENUM.NOT_LOGIN,
        });
      }

      //alert(JSON.stringify(state.loginUser));
    },
  },
  mutations: {
    updateUser(state, payload) {
      state.loginUser = payload;
      //alert(JSON.stringify(state.loginUser));
    },
  },
} as StoreOptions<any>;
