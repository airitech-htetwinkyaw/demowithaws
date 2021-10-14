
<template>
<div class="back">
    <div class="div-confirm common">
        <div class="content">
            <h3 class="title">入力した情報を確認してください</h3>
            <form @submit.prevent="register" id="confirm">             
                <div class="form-group">
                    <table class="center">
                    <colgroup>
                        <col style="width: 40%;"><col>
                    </colgroup>
                    <tbody>
                    <tr>
                        <td scope="col">名前</td>
                        <td scope="col">{{ this.userForm.username }}</td>
                    </tr> 
                    <tr>
                        <td scope="col">メールアドレス</td>
                        <td scope="col">{{ this.userForm.email }}</td>
                    </tr> 
                    <tr>
                        <td scope="col">連絡先</td>
                        <td scope="col">{{ this.userForm.contact }}</td>
                    </tr>
                    <tr>
                        <td scope="col">生年月日</td>
                        <td scope="col">{{ this.userForm.year }} {{ this.userForm.month }} {{ this.userForm.day }}</td>
                    </tr>
                    <tr>
                        <td scope="col">性別</td>                                    
                        <td scope="col" v-if="this.userForm.gender == '0'">男性</td>
                        <td scope="col" v-else>女性</td>
                    </tr>
                    </tbody>
                </table>
                <br>
                <div class="container">
                    <div class="button-wrapper">
                        <input type="button" name="save" value="登録" id="form-agree_target" class="profile-btn" v-on:click="register()">
                        &nbsp;&nbsp;
                        <input type="button" name="back" value="戻る" id="form-cancel" class="profile-btn" v-on:click="back">
                    </div>
                </div>
                </div>
            </form>
        </div>
    </div>
</div>
</template>
<script>
    import store from '../router/store';
    import router from "../router"; 
    import { Auth } from 'aws-amplify';
import UserDataService from '../services/UserDataService';
        const  months = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
    export default {
        //props: ['usrData'],
        data() {
            return {
      months:months,
                userForm: {
                    username: "",
                    email: "",
                    contact: "",
                    gender: "",
                    password: "",
                    confirmPassword: "",
                    accept: "",
                    year : "",
                    month: "",
                    day: ""
                },   
                err:true,
                chkaddmember:""
            }
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
              router.push("/confirm").catch(() => {});
            }
          });          
        },
        methods: {
        async register() {
            try {
                //this.registerToDynamoDB();
                await Auth.signUp({
                    username: this.userForm.username,
                    email:this.userForm.email,
                    password: this.userForm.password,
                    attributes: {email: this.userForm.email},
                });               
                if(this.chkaddmember === "addmember"){
                    this.registerToDynamoDB();                   
                }else{ 
                    localStorage.setItem('complete',"register");
                    router.push("/codeinsert").catch(() => {}); 
                }
            } catch (error) {
                console.log(error.message);
                alert(error.message);
            }
        },
        back(){             
                router.push({
                name: 'signup',
                params: {backData : this.userForm}
                }).catch(() => {});
            },
    registerToDynamoDB() {

          var birth_date = ''
          var birth_month = this.months.indexOf(this.userForm.month) + 1
          if(parseInt( birth_month) < 10 && parseInt( this.userForm.day) < 10){
            birth_date = this.userForm.year + '-0' + birth_month + '-0' + this.userForm.day;
          }else if(parseInt( birth_month) < 10 ){
            birth_date = this.userForm.year + '-0' + birth_month + '-' + this.userForm.day;
          }else if(parseInt( this.userForm.day) < 10){
            birth_date = this.userForm.year + '-' + birth_month + '-0' + this.userForm.day;        
          }else{
            birth_date = this.userForm.year + '-' + birth_month + '-' + this.userForm.day;
          }
          var data = {
          username: this.userForm.username,
          email: this.userForm.email,
          password: this.userForm.password,
          contact: this.userForm.contact,
          birth_date: birth_date,
          gender: this.userForm.gender,
          role:'user'
          };
          UserDataService.addUser(data)
            .then(response => {
                this.status = response.data;
                if(this.status === true)  {
                  router.push("/userslist").catch(() => {}); 
                } 
            })
            .catch(() => {});   
        }                
        },  
        mounted() {
          var data = JSON.parse(localStorage.getItem("torokuData"));
          this.userForm.username = data.username
          this.userForm.email = data.email
          this.userForm.contact = data.contact
          this.userForm.gender = data.gender
          this.userForm.year = data.year
          this.userForm.month = data.month
          this.userForm.day = data.day
          this.userForm.password = data.password

          this.chkaddmember = localStorage.getItem("addmember");
  }
    }
</script>