<template>
  <div class="back">
    <div class="div-center common form-color">
      <div class="content form-color">
        <h3 v-if="codeSubmit && !userErr && !nouserErr">パスワード変更</h3>
        <h3 v-else>認証コード発行</h3>
        <hr />
        <form @submit.prevent="sendCode">
            <span v-show="nouserErr && !userErr" class="fontred">該当データはありません。</span> 
            <span v-show="chpwdSubmit && invalidCode" class="fontred">不正認証コードです。</span> 
			<div class="form-group">
                <label for="username" class="forget-label">ユーザ名</label>
                <input type="text" v-model="username" class="form-control  login-input" id="username" :disabled="codeSubmit==true && !userErr && !nouserErr"
                :class="{ 'is-invalid': codeSubmit && $v.username.$error }"/>                
                <div v-if="codeSubmit && $v.username.$error" class="invalid-feedback">
                        <span v-if="!$v.username.required">名前を入力してください</span>
                    </div>         
            </div>
            <div v-show="codeSubmit && !userErr && !nouserErr" class="form-group">
                <label for="code"  class="forget-label">認証コード</label>
                <input type="text" v-model="code" class="form-control  login-input" id="code" :class="{ 'is-invalid': chpwdSubmit && $v.code.$error }"/>
                <div v-if="chpwdSubmit && $v.code.$error" class="invalid-feedback">
                    <span v-if="!$v.code.required">認証コードを入力してください</span>
                </div>               
            </div>
            <div v-show="codeSubmit && !userErr && !nouserErr" class="form-group">
                <label for="newPwd"  class="forget-label">新しいパスワード</label>
                <input type="password" class="form-control  login-input" v-model="newpassword" id="newpassword" name="newpassword"
                    :class="{ 'is-invalid': chpwdSubmit && $v.newpassword.$error }" />
                <div v-if="chpwdSubmit && $v.newpassword.$error" class="invalid-feedback">
                    <span v-if="!$v.newpassword.required">パスワードを入力してください</span>
                    <span v-if="!$v.newpassword.minLength">パスワードを5桁以上入力してください</span>
                    <span v-if="!$v.newpassword.maxLength">パスワードを30桁以下入力してください</span>
                </div>            
            </div>
            <div v-show="codeSubmit && !userErr && !nouserErr" class="form-group">
                <label for="conPwd" class="forget-label">新しいパスワード（確認）</label>
                <input type="password" v-model="cnewpassword"  class="form-control  login-input" id="cnewpassword" name="cnewpassword"
                    :class="{ 'is-invalid': chpwdSubmit && $v.cnewpassword.$error }" />
                <div v-if="chpwdSubmit && $v.cnewpassword.$error" class="invalid-feedback">
                    <span v-if="!$v.cnewpassword.required">パスワード（確認）を入力してください</span>
                    <span v-else-if="!$v.cnewpassword.sameAsPassword">パスワード（確認）はパスワードと一致していません</span>
                </div>
            </div>
           
            <div class="container" style="margin-top:10px">
                <button v-show="!codeSubmit || userErr || nouserErr" type="button" class="profile-btn" v-on:click="sendCode()">コード送信</button>
                <button v-show="codeSubmit && !userErr && !nouserErr" type="button" class="profile-btn" v-on:click="chgPwd()">変更</button>
                &nbsp;&nbsp;
                <button type="button" class="profile-btn" v-on:click="tologin()">キャンセル</button>
            </div>     
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import router from "../router";
import {required, email , minLength , sameAs } from "vuelidate/lib/validators";
import UserDataService from '../services/UserDataService';
import { Auth } from 'aws-amplify';
export default {
  name: "Login",
  data() {
    return {
            username:"",
            // email:"",
            code:"",
            newpassword: "",
            cnewpassword: "",  
            codeSubmit: false,
            userErr:false,//ユーザ未登録
            chpwdSubmit: false,
            chpwdErr:false,
            nouserErr:false,//User Poolに存在ないデータ
            invalidCode:false,//認証が不正の場合
            allerr:false,//code,pwd,cpwdの何かにエラーが発生した場合
            requestFunction:""
        };
    
  },
  validations:{
    username:{
        required
    },
    code:{
        required
    },
    email: {
            required,
            email
        },
    newpassword: {
        required,
        minLength: minLength(8)
    },
    cnewpassword: {
        required,
        sameAsPassword: sameAs('newpassword')
    },
  },//validation
  methods: {    
        tologin() {
            router.push({name:'login'});
        },    
        sendCode() {
            this.$v.$touch();
            if (this.$v.username.$invalid) {
                this.codeSubmit = true;
                this.userErr = true;
                return;
            }
            //to test chgPwd
            // this.username = "myatmon";
            // this.codeSubmit = false;        
            // this.userErr = true;
            // this.codeSubmit = true;
            // this.userErr = false;
            // this.nouserErr = false;
            
            if(this.requestFunction === 'forgotpassword'){
                            Auth.forgotPassword(this.username)
                .then(()=>{ 
                        this.codeSubmit = false;
                        this.userErr = true;
                        this.codeSubmit = true;
                        this.userErr = false;
                        this.nouserErr = false;
                })
                .catch(()=>{
                            this.userErr = false;//入力エラーが表示してない場合
                            this.nouserErr=true;
                            return;
                        });   
            }else{
                Auth.resendSignUp(this.username)
                .then(()=>{ 
                        this.codeSubmit = false;
                        this.userErr = true;
                        this.codeSubmit = true;
                        this.userErr = false;
                        this.nouserErr = false;
                })
                .catch(()=>{
                            this.userErr = false;//入力エラーが表示してない場合
                            this.nouserErr=true;
                            return;
                        });  
            

            }

            // Auth.forgotPassword(this.username)
            //     .then(()=>{ 
            //             this.codeSubmit = false;
            //             this.userErr = true;
            //             this.codeSubmit = true;
            //             this.userErr = false;
            //             this.nouserErr = false;
            //     })
            //     .catch(()=>{
            //                 this.userErr = false;//入力エラーが表示してない場合
            //                 this.nouserErr=true;
            //                 return;
            //             });                
        },
        chgPwd(){       
            this.$v.$touch();
            if (this.$v.code.$invalid || this.$v.newpassword.$invalid || this.$v.cnewpassword.$invalid) {
                this.chpwdSubmit = true;
                this.allerr = true;
                return;
            }else{
                this.chpwdSubmit = true;
                this.allerr = false;
            }
            if(this.requestFunction === 'forgotpassword'){
                Auth.forgotPasswordSubmit(this.username, this.code, this.cnewpassword)
                .then(()=>{
                        this.invalidCode = false;
                        UserDataService.forgotPassword(this.username,this.cnewpassword)
                        .then(response => {
                            this.status = response.data
                            if(this.status){                
                                router.push({name:'login'});
                            }else{
                                this.err = this.status;
                            }
                        }).catch(() => {});                 
                    })
                .catch(()=>{                
                        this.invalidCode = true;
                    });
            }else{
                Auth.confirmSignUp(this.username, this.code)
                .then(()=>{
                        this.invalidCode = false
                        Auth.currentAuthenticatedUser()
                        .then(user=>{
                            console.log(user.username+" "+user.email)
                            router.push({name:'login'});
                        }).catch((err) => {console.log(err.message)});
                        //Auth.changePassword(this.now_user, this.userData.password, this.newPassword);
                        // Auth.completeNewPassword(this.username, this.cnewpassword)
                        // .then(()=>{
                        //     router.push({name:'login'});
                        // }).catch((err) => {console.log(err.message)});
                        // UserDataService.forgotPassword(this.username,this.cnewpassword)
                        // .then(response => {
                        //     this.status = response.data
                        //     if(this.status){                
                        //         router.push({name:'login'});
                        //     }else{
                        //         this.err = this.status;
                        //     }
                        // }).catch((err) => {console.log(err.message)});           
                    })
                .catch((err)=>{ 
                    console.log("2: "+err.message)               
                        this.invalidCode = true;
                    });

            }
            // Auth.forgotPasswordSubmit(this.username, this.code, this.cnewpassword)
            //     .then(()=>{
            //             this.invalidCode = false;
            //             UserDataService.forgotPassword(this.username,this.cnewpassword)
            //             .then(response => {
            //                 this.status = response.data
            //                 if(this.status){                
            //                     router.push({name:'login'});
            //                 }else{
            //                     this.err = this.status;
            //                 }
            //             }).catch(() => {});                 
            //         })
            //     .catch(()=>{                
            //             this.invalidCode = true;
            //         });
        },
    },
    mounted (){
        this.requestFunction = localStorage.getItem('function');
    }
};
</script>

