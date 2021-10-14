<template>
    <div id="app">
        <router-view />
    </div>
</template>

// <script>
// import { Auth } from 'aws-amplify';
// import router from './router';

// export default {
//     name: 'App',
//     methods: {
//         async logout() {
//             try {
//                 await Auth.signOut();
//                 alert("Log out!!!!!");
//                 router.push("/")
//             } catch (error) {
//                 alert(error.message);
//             }
//         },
//     },
// };
// </script>

<style>
  @import './assets/main.css';
</style>
