// initial state
import { StoreOptions } from "vuex";

// getters
const getters = {};

export default {
  namespaced: true,
  state: () => ({
    loginUser: {
      username: "未登录",
      role: "notLogin",
    },
  }),
  getters,
  actions: {
    getLoginUser({ commit, state }, paylaod) {
      // todo 缓存请求服务端获取用户信息
      commit("updateUser", { username: "YT" });
    },
  },
  mutations: {
    updateUser(state, payload) {
      state.loginUser = payload;
    },
  },
} as StoreOptions<any>;
