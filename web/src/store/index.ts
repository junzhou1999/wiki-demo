import {createStore} from 'vuex'

declare let SessionStorage: any;
const USER = 'USER';  // 存储在session的key值

const store = createStore({
  state: {
    // 全局变量
    user: SessionStorage.get(USER) || {}  // 避免空指针异常
  },
  getters: {},
  mutations: {
    // 对变量操作，同步，用commit触发
    setUser(state, user) {
      state.user = user;
      SessionStorage.set(USER, user);
    }
  },
  actions: {
    // 对变量操作，异步
  },
  modules: {}
});

export default store;
