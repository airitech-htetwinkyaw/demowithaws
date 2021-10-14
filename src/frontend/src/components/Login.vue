<template>
  <div class="back">
    <div class="div-center common form-color">
      <div class="content form-color">
        <h2>ログイン</h2>
        <hr />
        <form>
          <div class="form-group">
            <label class="error-message" v-if="errorMsg !==''">{{ errorMsg }}</label>
            <a class="lnk" type="button" v-on:click="changePassword()" v-if="requiredUpdate">パスワード変更</a>
          </div>
          <div class="form-group">
            <label for="name">氏名</label>
            <input type="text" class="form-control login-input" id="name" placeholder="メールアドレスを入力してくだい" v-on:keyup="updateErrorContent()" v-model="username"/>
          </div>
          <br />
          <div class="form-group">
            <label for="password">パスワード</label>
            <input type="password" class="form-control  login-input" id="password" placeholder="パスワードを入力してください" v-on:keyup="updateErrorContent()" v-model="password"/>
          </div>
          <br />
          <button type="button" class="login-btn" v-on:click="login()">ログイン</button>
          &nbsp;&nbsp;
          <a class="lnk" type="button" v-on:click="accountCreate()">アカウントの作成</a>
          <hr />
          パスーワード忘れた方は
          <a class="lnk" type="button" v-on:click="forgotPassword()">こちら</a>
        </form>
      </div>
    </div>
  </div>
</template> 

<script>
import router from "../router";
import { Auth } from 'aws-amplify';
import UserDataService from '../services/UserDataService';
export default {
  name: "Login",
  data() {
    return {
      username:"",
      email: "",
      password: "",
      errorMsg: "",
      users: [],
      role:"",
      requiredUpdate:false,
      checkStatus:false
    };
  },
  methods: {
     changePassword() {
       localStorage.setItem('function','changepassword');
       router.push("/changepassword").catch(() => {});
    },   
    login2() {
      Auth.signIn(this.username, this.password)
                .then(() =>{
                  UserDataService.selectRole(this.username)//to check role
                  .then(response => {
                    this.role = response.data;
                    if(this.role === "admin" || this.role === "moderator"){
                        router.push("/userslist").catch(() => {});
                    }else{
                        router.push("/home").catch(() => {});
                    }
                  }).catch(()=>{});
                }).catch(()=>{
                  this.errorMsg = "氏名またはパスワードが間違っています。"; 
                });
        },    
    accountCreate() {
        localStorage.setItem('beforepage','login');
        router.push("/signup").catch(() => {});
    },
    login() {
      if (this.username === "" && this.password === "") {
        this.errorMsg = "氏名とパスワードを入力してください。";
      } else if (this.username === "") {
        this.errorMsg = "氏名を入力してください。";
      } else if (this.password === "") {
        this.errorMsg = "パスワードを入力してください。";
      } else {
          Auth.signIn(this.username, this.password)
                .then(() =>{
                  UserDataService.selectRole(this.username)//to check role
                  .then(response => {
                    this.role = response.data;
                    if(this.role === "admin" || this.role === "moderator"){
                        router.push("/userslist").catch(() => {});
                    }else{
                        router.push("/home").catch(() => {});
                    }
                  }).catch(()=>{});
                }).catch(err=>{
                  if(err.message === 'User is not confirmed.'){
                    
                    // this.errorMsg = "パスワードの変更が必要です。"; 
                    // this.requiredUpdate = true;
                    console.log("main err: "+err.message)
                  UserDataService.checkUser(this.username,this.password)
                  .then(response => {
                      this.checkStatus = response.data; 
                      console.log("status: "+this.checkStatus)
                      if(this.checkStatus){     
                        localStorage.setItem('loginName',this.username);
                        Auth.resendSignUp(this.username)
                        .then(()=>{
                          localStorage.setItem('complete',"confirm");
                          router.push("/codeinsert").catch(() => {});  
                        })          
                        //this.errorMsg = "パスワードの変更が必要です。"; 
                        //this.requiredUpdate = true;   
                    }else{
                      this.errorMsg = "氏名またはパスワードが間違っています。"; 
                      this.requiredUpdate = false;
                    }   
                  })
                  .catch(err => {console.log("err: "+err.message)});
                  }else{                    
                    this.errorMsg = "氏名またはパスワードが間違っています。"; 
                    this.requiredUpdate = false;
                  }
                  //this.errorMsg = "メールアドレスまたはパスワードが間違っています。"; 
                });

          // UserDataService.checkUser(this.email,this.password)
          // .then(response => {
          //     this.users = response.data; 
          // })
          // .then(()=>{
          //   if(this.users.length === 1){                
          //       localStorage.setItem('login', true); 

          //      if(this.users[0].role === "管理者" || this.users[0].role === "モデレータ"){                     
          //         localStorage.setItem('role', this.users[0].role); 
          //         router.push("/userslist").catch(() => {});   
          //       }else{
          //         if(this.email === 'moderator@gmail.com' && this.password === 'moderator'){                   
          //           localStorage.setItem('role', 'モデレータ'); 
          //           router.push("/userslist").catch(() => {});   
          //         }else{
          //         router.push("/home").catch(() => {});

          //         }   
          //         //router.push("/home").catch(() => {});   
          //       }           
          //   }else{
          //     this.errorMsg = "メールアドレスまたはパスワードが間違っています。"; 
          //   }   
          // })
          // .catch(() => {});     
      }
    },
    updateErrorContent(){
      if(this.errorMsg === "氏名を入力してください。"){
        if(this.username.length > 0){
          this.errorMsg = '';
        }
      }else if(this.errorMsg === "パスワードを入力してください。"){
        if(this.password.length > 0){
          this.errorMsg = '';
        }
      }else if(this.errorMsg === "氏名とパスワードを入力してください。"){
        if(this.username.length > 0){
          this.errorMsg = 'パスワードを入力してください。';
        }
        if(this.password.length > 0){
          this.errorMsg = '氏名を入力してください。';
        }
      }else{
        this.errorMsg = '';
      }
    },
    forgotPassword(){
       localStorage.setItem('function','forgotpassword');
      router.push("/forget").catch(() => {});
    }
  },
  mounted(){    
      //管理者アカウント自動作成
    //   var data = {
    //     username: "Aung Aung",
    //     email: "aungaung@gmail.com",
    //     password: "222222",
    //     contact: "09045663456",
    //     birth_date: "1995-06-12",
    //     gender: 0,
    //     role: "管理者",
    //     created_date: "2021-10-01 15:05:05",
    //     updated_date: "2021-10-01 15:05:05"
    // }
     // UserDataService.addUser(data);
      localStorage.setItem('login', false);       
  }
};
</script>-->

