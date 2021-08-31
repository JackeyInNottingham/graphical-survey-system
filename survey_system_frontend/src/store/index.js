import Vue from 'vue';
import Vuex from 'vuex';
Vue.use(Vuex);

const state = {
  login: false,
  role: '',
  username: '',
  token: ''
}

const getters = {
  getLogin(state){
    return state.login;
  },
  getRole(state){
    return state.role;
  },
  getUsername(state){
    return state.username;
  },
  getToken(state){
    return state.token;
  }
}

const mutations = {
  login(state){
    state.login = true;
  },
  logout(state){
    state.login = false;
    state.role = '';
    state.username = '';
    state.token = '';
  },
  setRole(state, role){
    state.role = role;
  },
  setUsername(state, username){
    state.username = username;
  },
  setToken(state, token){
    state.token = token;
  }
}

const actions = {
  invokeLogin(context){
    context.commit('login');
  },
  invokeLogout(context){
    context.commit('logout');
    window.sessionStorage.clear();
  },
  invokeSetRole(context, role){
    context.commit('setRole', role);
  },
  invokeSetUsername(context, username) {
    context.commit('setUsername', username);
  },
  invokeSetToken(context, token){
    context.commit('setToken', token);
  }
}


const store = new Vuex.Store({
  state: status,
  getters,
  mutations,
  actions
});

export default store;
