<template>
<div class="out">
    <div class="out inner">            
        <h2 v-if="isApprove === 'register'">ご登録ありがとうございました。</h2>
        <h2 v-else>ご承認ありがとうございました。</h2>
        <div class="container">
            <div class="button-wrapper">
                <input type="submit" name="sub" value="ログイン画面へ" id="form-agree_target" class="complete-btn" v-on:click="login">
            </div>
        </div>
    </div>     
</div>

</template>
<script>
    import store from '../router/store';
    import router from "../router";
    export default {
        data(){
          return{
            isApprove:""
          }
        },
        methods: {
        login(){
            localStorage.removeItem("torokuData");//ユーザー登録データ    
            localStorage.removeItem ('addmember');//adminから追加したデータ 
            localStorage.removeItem('complete');   
             router.push({
                name: 'login',                
            });
        },
    },
    beforeRouteEnter(to, from, next) {
          next(vm => {
            vm.prevRoute = from;
            if (performance.navigation.type != performance.navigation.TYPE_RELOAD) {  
              store.commit("signupLink", vm.prevRoute.name);   
           }
           vm.prevRoute = store.state.signupLink;
            if(vm.prevRoute === null){
              router.push("/").catch(() => {});
            }else{
              router.push("/complete").catch(() => {});
            }
          });          
        },
        mounted() {
          
          this.isApprove = localStorage.getItem("complete");
        }
}
</script>

