import Vue from 'vue'
import VueRouter from 'vue-router'
import { Auth } from 'aws-amplify';

Vue.use(VueRouter)
 
  const routes = [
  {
    path: '/',
    name: 'login',
    component: () => import('../components/Login.vue')
  },
  {
    path: '/signup',
    name: 'signup',
    component: () => import('../components/SignUp.vue'),
    props: true
  },
  {
    path: '/confirm',
    name: 'confirm',
    component: () => import('../components/Confirm.vue')
  },
  {
    path: '/codeinsert',
    name: 'codeinsert',
    component: () => import('../components/SignUpConfirm.vue')
  },
  {
    path: '/complete',
    name: 'complete',
    component: () => import('../components/Complete.vue'),
    props: true,
   // beforeEnter: checkSignUp
  },
  {
    path: '/home',
    name: 'home',
    component: () => import('../components/Home.vue'),
    beforeEnter: checkLogin
  },
  {
    path: '/userslist',
    name: 'userslist',
    component: () => import('../components/UsersList.vue'),
    beforeEnter: checkLogin
  },
  {
    path: '/infoupdate',
    name: 'infoupdate',
    component: () => import('../components/SignUp.vue')
  },
  {
    path: '/profile',
    name: 'profile',
    component: () => import('../components/Profile.vue'),
   // beforeEnter: checkLogin
  },
  {
    path: '/update',
    name: 'update',
    component: () => import('../components/Update.vue'),
   // beforeEnter: checkLogin
  },
  {
    path: '/password-update',
    name: 'password-update',
    component: () => import('../components/PasswordUpdate.vue'),
    //beforeEnter: checkLogin
  },
  {
    path: '/front-end',
    name: 'front-end',
    //component: () => import('../components/FrontEndLanguages.vue'),
    component: () => import('../components/LanguageList.vue'),
   // beforeEnter: checkLogin
  },
  {
    path: '/back-end',
    name: 'back-end',
    //component: () => import('../components/BackEndLanguages.vue'),
    component: () => import('../components/LanguageList.vue'),
    //beforeEnter: checkLogin
  },
  {
    path: '/database',
    name: 'database',
    //component: () => import('../components/Database.vue'),
    component: () => import('../components/LanguageList.vue'),
    //beforeEnter: checkLogin
  },
  {
    path: '/aws',
    name: 'aws',
    //component: () => import('../components/AmazonWebService.vue'),
    component: () => import('../components/LanguageList.vue'),
    //beforeEnter: checkLogin
  },
  {
    path: '/forget',
    name: 'forget',
    component: () => import('../components/ForgotPassword.vue')
  },
  {
    path: '/changepassword',
    name: 'changepassword',
    component: () => import('../components/ForgotPassword.vue')
  }
]
 
const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

function checkLogin(to, from, next) {
  Auth.currentAuthenticatedUser()
    .then(() => {
      next();   
    })
    .catch(()=>{
      next({
        path: "/",
      });
    });
}

// function checkLogin(to, from, next) {
//   if (localStorage.getItem('login') === null || localStorage.getItem('login') === 'false') {
//   //if (store.state.authenticated === null || store.state.authenticated === false) {
//     next({
//       path: "/",
//     });
//   } else {
//     next();
//   }
// }

// function checkSignUp(to, from, next) {
//   if (localStorage.getItem('confirm') === 'confirm') {
//     next();
//   } else {
//     next({
//       path: "/",
//     });
//   }
// }

export default router