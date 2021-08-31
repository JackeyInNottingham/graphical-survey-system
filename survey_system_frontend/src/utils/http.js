import axios from 'axios';
import store from "../store";
import router from "../router";

const Axios = axios.create({
  timeout: 10000,
  baseURL: '/api'
})

Axios.interceptors.request.use(config => {
  let token = store.state.token;
  if (token) {
    config.headers["token"] = token;
  }
  return config;
}, error=>{
  return Promise.reject(error);
})

Axios.interceptors.response.use(data=>{
  if (data.data.code === 401){
    store.commit("logout")
    router.push({name: 'login'})
  } else if (data.data.code === 404){
    router.push({name: '404'})
  }
  return data;
})

export default Axios;
