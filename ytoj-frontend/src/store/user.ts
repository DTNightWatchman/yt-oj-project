// initial state
import { StoreOptions } from "vuex";
import ACCESS_ENUM from "@/access/accessEnum";

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
    getLoginUser({ commit, state }, payload) {
      // todo 缓存请求服务端获取用户信息
      //alert(JSON.stringify(state.loginUser));
      //alert(JSON.stringify(payload));
      commit("updateUser", payload);
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
