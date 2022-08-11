import { createStore } from 'vuex'

const store = createStore({
  state: {
    // 全局变量
    user: {}
  },
  getters: {},
  mutations: {
    // 对变量操作，同步，用commit触发
    setUser(state, user) {
      state.user = user;
    }
  },
  actions: {
    // 对变量操作，异步
  },
  modules: {}
});

export default store;
