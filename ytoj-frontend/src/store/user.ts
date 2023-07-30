// initial state
import { StoreOptions } from "vuex";

// getters
const getters = {};

export default {
  namespaced: true,
  state: () => ({
    loginUser: {
      username: "未登录",
    },
  }),
  getters,
  actions: {
    getLoginUser({ commit, state }, paylaod) {
      commit("updateUser", { userName: "YT" });
    },
  },
  mutations: {
    updateUser(state, payload) {
      state.loginUser = payload;
    },
  },
} as StoreOptions<any>;
