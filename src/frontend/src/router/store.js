import Vue from 'vue'
import Vuex from "vuex"

Vue.use(Vuex);

const store = new Vuex.Store(
  {
      state: {
          signupLink:"",
          link:"",
      },
      mutations: {         
        signupLink(state, status) {
            state.signupLink = status;
        },
        link(state, status) {
            state.link = status;
        }
      },
  }
);

export default store;