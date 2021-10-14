<template>
<div class="back-update">        
      <div>
        <a @click="logout()" class="home-link" >ログアウト</a>
        <router-link to="/profile" class="home-link">プロフィール</router-link>
        <router-link to="/home" class="home-link">ホーム</router-link>
      </div>
    <div class="back-update div-password-update common">
            <div class="content">
            <h2 class="title">{{ userData.username }} のパスワードの変更</h2> 
            <form @submit.prevent="updatePassword">
                <div class="upper-container">
                    <div class="row">
                        <div class="col-lg-4 col-md-6 col-sm-12 col-xs-12">
                             <img id="profileImage" v-if="userData.gender === 0" style="{width:1500px;height:150px;}" src="@/assets/male_profile.png" />
                            <img id="profileImage" v-else style="{width:1500px;height:150px;}" src="@/assets/female_profile.png" />
                        </div>
                        <div class="col-lg-8 col-md-6 col-sm-12 col-xs-12">
                             <label class="psw-label" >{{ userData.username }}<br />{{ userData.email }}<br />{{userData.contact }}</label>  
                        </div>
                    </div>
                </div>

                <div class="div-item">
                    <input type="password" id="nowpassword" name="nowPassword" class="psw-input" placeholder="現在のパスワード"  v-model="nowPassword" :class="{ 'is-invalid': isSubmitted }" v-on:keyup="updateNowErrorContent()" /> 
                    <div v-if="isSubmitted" class="invalid-feedback">
                        <span class="password-span" v-if="nowErrorMsg1 !== ''">{{ nowErrorMsg1 }}</span> 
                        <span class="password-span" v-if="nowErrorMsg !== ''">{{ nowErrorMsg}}</span>
                    </div>
                </div>  

                <div class="div-item">
                    <input type="password" id="newpassword" name="newPassword" class="psw-input" placeholder="新しいパスワード" v-model="newPassword" :class="{ 'is-invalid': isSubmitted}" v-on:keyup="updateErrorContent()" />
                    <div v-if="isSubmitted" class="invalid-feedback">                        
                        <span class="password-span" v-if="newErrorMsg1 !== ''">{{ newErrorMsg1 }}</span>
                    </div>
                </div>

                <div class="div-item">
                    <input type="password" id="confirmPassword" name="newConfirmPassword" class="psw-input" placeholder="新しいパスワードの確認" v-model="newConfirmPassword" :class="{ 'is-invalid': isSubmitted }" v-on:keyup="updateNewErrorContent()" />
                    <div v-if="isSubmitted" class="invalid-feedback">
                        <span class="password-span" v-if="newErrorMsg2 !== ''">{{ newErrorMsg2}}</span> 
                        <span class="password-span" v-if="newErrorMsg !== ''" >{{ newErrorMsg }}</span>
                    </div>
                </div>

                <div class="password-btn-container">
                    <div class="button-wrapper">
                        <input type="button" name="cancel" value="キャンセル" id="form-cancel" class="password-update-btn" @click="backToProfile()" /> 
                        &nbsp;&nbsp;
                        <input type="submit" name="update" value="パスワード更新" id="form-update" class="password-update-btn" />
                    </div>
                </div>
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
        data() {
            return {
                id:'',
                userData: {
                    username:"",
                    email:"",
                    contact:"",
                    gender:"",
                    password:"",
                },
                    nowPassword: "",
                    newPassword: "",
                    newConfirmPassword: "",
                now_user:[],
                isSubmitted: false,
                nowErrorMsg: '',
                nowErrorMsg1: '',
                newErrorMsg1: '',
                newErrorMsg: '',
                newErrorMsg2: '',
            };
        },
        methods: {   
            backToProfile() {
                router.push("/profile");
            },            
            updatePassword() {
                    this.isSubmitted = true;
                    if(this.nowPassword === ""){
                        console.log("reach 1");
                        this.nowErrorMsg1 = '現在のパスワードを入力してください。'
                    }else{
                        console.log("reach 2");
                        this.nowErrorMsg1 = ''
                        console.log("now: "+this.nowPassword);
                        console.log("new: "+this.userData.password);
                        if((this.nowPassword !== this.userData.password)){
                            this.nowErrorMsg = '入力されたパスワードは正しくありません。現在のパスワードを再入力してください。';
                        }else{
                            this.nowErrorMsg = ''
                        }
                    }
                    if(this.newPassword === ""){
                        console.log("reach 3");
                        this.newErrorMsg1 = '新しいパスワードを入力してください。'
                    }else{
                    console.log("reach 4");
                        if(this.newPassword.length < 8){
                            this.newErrorMsg1 = 'パスワードを8桁以上入力してください。'
                        }
                        // else if(this.newPassword.length > 30){
                        //     this.newErrorMsg1 = 'パスワードを30桁以下入力してください。'
                        // }
                        else{
                            this.newErrorMsg1 = ''
                        }
                    }
                    if(this.newConfirmPassword === ""){
                    console.log("reach 5");
                        this.newErrorMsg2 = '確認のため、新しいパスワードをもう一度入力してください。'
                    }else{
                    console.log("reach 6");
                        this.newErrorMsg2 = ''
                    }

                    if(this.newErrorMsg2 === ''){
                        if(this.newPassword !== this.newConfirmPassword){
                            this.newErrorMsg = '入力されたパスワードは一致しません。新しいパスワードを両方のボックスに入力してください。';
                        }else{
                            this.newErrorMsg = '';
                        }
                    }

                    if(this.nowErrorMsg === '' && this.nowErrorMsg1 === '' && this.newErrorMsg1 === '' &&  this.newErrorMsg2 === '' &&  this.newErrorMsg === ''){
                        Auth.changePassword(this.now_user, this.userData.password, this.newPassword);
                        UserDataService.updatePassword(this.id,this.newPassword)
                        .then(response => {
                            this.status = response.data
                            if(this.status){
                                this.successAlert();
                            }
                        })
                        .catch(() => {}); 
                    }
            }, 
            updateNowErrorContent(){
                    this.nowErrorMsg = '';
                    this.nowErrorMsg1 = '';
            },
            updateNewErrorContent(){
                    this.newErrorMsg2 = ''
                    this.newErrorMsg = '';
            },
            updateErrorContent(){
                    this.newErrorMsg1 = ''
            },
            successAlert() {
                this.$alert(
                "パスワードの変更は正常に完了しました。",
                "パスワードの変更完了",
                "success"
                ).then(() => router.push('/profile'));
            },
            logout(){      
                this.$confirm("アカウントをログアウトします。宜しいですか？", "ログアウトの確認") 
                .then(() => {
                    this.signOut();
                    })
                    .catch(() => {});  
            },
             async signOut() {
                try {
                    await Auth.signOut({ global: true });
                    localStorage.setItem('login', false);
                    router.push("/")
                } catch (error) {
                    console.log('error signing out: ', error);
                }
            }
       } ,
  mounted() {
          var data = JSON.parse(localStorage.getItem("nowuser"));
          this.userData = data;
    //       Auth.currentAuthenticatedUser()
    // .then(user => {
    //         this.now_user = user;
    //         this.nowusername = user.username;
    //         UserDataService.getNowUserID(this.nowusername)
    //         .then(response=>{
    //             this.id = response.data;
    //         })
    //         .then(() => { 
    //             UserDataService.getNowUser(this.id)
    //             .then(response => {
    //                 this.userData = response.data;

    //                 var birth_date = this.userData.birth_date;
    //                 var year = birth_date.substring(0.,birth_date.indexOf('-'))
    //                 var month = parseInt(birth_date.substring(birth_date.indexOf('-')+1,birth_date.lastIndexOf('-'))) - 1
    //                 var day = birth_date.substring(birth_date.lastIndexOf('-')+1,birth_date.length)
    //                 this.userData.birth_date = parseInt(day) + ' '+ this.months[parseInt(month)] + ' ' + year;
    //             })
    //             .catch(() => {}); 
    //         });
    // })
    // .catch(() => {});
  }
    };
</script>


