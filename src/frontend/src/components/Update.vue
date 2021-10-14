<template>
<div class="back-update">        
      <div>
        <a @click="logout()" class="home-link" >ログアウト</a>
        <router-link to="/profile" class="home-link">プロフィール</router-link>
        <router-link to="/home" class="home-link">ホーム</router-link>
      </div>

    <div class="back-update div-update common">
            <div class="content">
            <h2 class="title">情報変更</h2>
            <form @submit.prevent="updateInfo">
                <div class="div-item">
                    <label class="update-label-title" for="name">氏名</label> 
                    <!-- <input type="text" id="name" name="name" class="update-label" v-model="now_user[0].username"
                        :class="{ 'is-invalid': isSubmitted && $v.userData.name.$error }" />    -->
                    <input type="text" id="name" name="name" class="update-label-name" v-model="userData.username"
                        :class="{ 'is-invalid': isSubmitted}" disabled="true" />    
                    
                    <div v-if="isSubmitted" class="invalid-feedback">                   
                        <span class="update-span" v-if="nameErrorMsg !==''" >{{ nameErrorMsg }}</span> 
                    </div>            
                </div>
                <!-- <div class="div-item">
                    <label class="update-label-title" for="email">メールアドレス</label>
                    <input type="email" id="email" name="email" class="update-label" v-model="userData.email" :class="{ 'is-invalid': isSubmitted }" v-on:keyup="showWarning()"/>  
                    <div v-if="isSubmitted" class="invalid-feedback">            
                    <span class="update-span" v-if="mailErrorMsg !==''"> {{ mailErrorMsg }} </span>  
                    </div> 
                    <span class="update-span-warning" v-if="warningMessage1 !==''" >{{ warningMessage1 }}</span>               
                    <span class="update-span-warning" v-if="warningMessage2 !==''"> {{ warningMessage2 }}</span>  
                </div> -->

                <div class="div-item">
                    <label class="update-label-title" for="mobile">連絡先</label>
                    <input type="text" id="contact" name="contact" class="update-label" v-model="userData.contact" 
                        :class="{ 'is-invalid': isSubmitted}" />                        
                    
                    <div v-if="isSubmitted" class="invalid-feedback">                   
                    <span class="update-span" v-if="contactErrorMsg!==''" > {{ contactErrorMsg }} </span>
                    </div>                           
                </div>

                <div class="div-item">
                    <label class="update-label-title" for="dob">生年月日</label>
                        <select id="years" class="bd-label" v-model="birthday.year">
                            <option v-for="(value, key) in years" :key="key">{{ value }}</option>
                        </select><label class="bd-dash">-</label>
                        <select id="months" class="bd-month-label" v-model="birthday.month">
                            <option v-for="(value, key) in months" :key="key" > {{ value }} </option>
                        </select><label class="bd-dash">-</label>
                        <select id="days" class="bd-label" v-model="birthday.day">
                            <option v-for="day in 31" :key="day">{{ day }}</option>
                        </select>
                </div>


                <div class="div-item">
                    <label class="update-label-title" for="gender">性別</label> 
                   
                        <div class="form-check form-check-inline gender-label">
                            <input class="form-check-input" type="radio" name="gender" id="gender1" value="male" :checked="userData.gender == '0'">
                            <label class="form-check-label" for="gender1" >男性</label>
                        </div>
                        <div class="form-check form-check-inline gender-label">
                            <input class="form-check-input" type="radio" name="gender" id="gender2" value="female"  :checked="userData.gender == '1'">
                            <label class="form-check-label" for="gender2">女性</label>
                        </div>   
                </div>   

                <div class="profile-btn-container">
                    <div class="button-wrapper">
                        <input type="button" name="cancel" value="キャンセル" id="form-cancel" class="update-btn" @click="backToProfile()"> 
                        &nbsp;&nbsp;
                        <input type="submit" name="update" value="更新" id="form-update" class="update-btn">
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
    const  months = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
    export default {
        data() {
            return {
                months: months,
                id:'',
                userData: {
                    id:"",
                    username: "",
                    email: "",
                    password: "",
                    contact: "",
                    gender:"",
                    birth_date:"",
                    role:"",
                    created_date:"",
                    updated_date:""
                },
                birthday:{
                    year:"",
                    month:"",
                    day:""
                },
                isSubmitted: false,
                nameErrorMsg:"",
                // mailErrorMsg:"",
                contactErrorMsg:"",
                warningMessage1:"",
                warningMessage2:"",
                // nowEmail:"",
                // sameMail:""
            };
        },
        methods: {   
            backToProfile() {
                router.push("/profile");
            },            
            updateInfo() {
                this.isSubmitted = true;

                // if(this.userData.name === ''){
                //     this.nameErrorMsg = '氏名を入力してください。';
                // }else{
                //     if(this.userData.name.length > 15){
                //         this.nameErrorMsg = '氏名を15桁以下入力してください';
                //     }else{
                //         this.nameErrorMsg = '';
                //     }
                // }

                // if(this.userData.email === ''){
                //     this.mailErrorMsg = 'メールアドレスを入力してください。';
                // }else{
                //     if ( /^(([^<>()[\]\\.,;:\s@']+(\.[^<>()\\[\]\\.,;:\s@']+)*)|('.+'))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/.test(this.userData.email) ) {
                //         this.mailErrorMsg = '';
                //     }
                //     else {
                //         this.mailErrorMsg = '有効なメールアドレスを入力してください。';
                //     }
                // }

                if(this.userData.contact === ''){
                        this.contactErrorMsg = '連絡先を入力してください';
                }else{
                     if(this.userData.contact.length > 15){
                        this.contactErrorMsg = '連絡先を15桁以下入力してください';
                    }else if(this.userData.contact.length < 11){
                        this.contactErrorMsg = '連絡先を11桁以上入力してください';
                    }else{
                        this.contactErrorMsg = '';
                    }
                }


                // if(this.nowEmail === this.userData.email){
                //     this.sameMail = true;
                // }else{
                //     this.sameMail = false;
                //     http
                //     .get("/users/findmail/"+this.userData.email)
                //     .then(response => {
                //         this.status = response.data
                //         if(this.status === true){
                //             this.mailErrorMsg = 'メールアドレスは登録済みです。';
                //             this.warningMessage1 = '';
                //             this.warningMessage2 = '';
                //         }
                //     })
                // }
                
                // if(this.nameErrorMsg !== '' || this.mailErrorMsg !=='' || this.contactErrorMsg !=='' ){
                if(this.contactErrorMsg !=='' ){
                    return;
                }

                
                var birthdate = ''
                var birth_month = this.months.indexOf(this.birthday.month) + 1
                if(parseInt( birth_month) < 10 && parseInt( this.birthday.day) < 10){
                    birthdate = this.birthday.year + '-0' + birth_month + '-0' + this.birthday.day;
                }else if(parseInt( birth_month) < 10 ){
                    birthdate = this.birthday.year + '-0' + birth_month + '-' + this.birthday.day;
                }else if(parseInt( this.birthday.day) < 10){
                    birthdate = this.birthday.year + '-' + birth_month + '-0' + this.birthday.day;        
                }else{
                    birthdate = this.birthday.year + '-' + birth_month + '-' + this.birthday.day;
                }

                this.userData.birth_date = birthdate;
                
                var data = {
                    id:this.userData.id,
                    username: this.userData.username,
                    email: this.userData.email,
                    password: this.userData.password,
                    contact: this.userData.contact,
                    gender:this.userData.gender,
                    birth_date:this.userData.birth_date,
                    role:this.userData.role,
                    created_date:this.userData.created_date,
                    updated_date:this.userData.updated_date
                };                

                UserDataService.updateUser(this.id , data)
                .then(response => {
                    this.status = response.data
                    if(this.status === true){
                        this.successAlert();
                        // if(this.sameMail){
                        //     this.successAlert();
                        // }else{                            
                        //     this.successAlertLogin();
                        // }
                    }
                })
                .catch(() => {}); 
            },
            // showWarning(){
            //         if(this.nowEmail !== this.userData.email){
            //             this.warningMessage1 = 'メールアドレスを変更すると、ログインユーザーも変更され、';
            //             this.warningMessage2 = '再ログインが必要になります。';
            //         }else{
            //             this.warningMessage1 = '';
            //             this.warningMessage2 = '';
            //         }
            // },
            successAlert() {
                this.$alert(
                "情報変更は正常に完了しました。",
                "情報変更完了",
                "success"
                ).then(() => router.push('/profile'));
            },
            // successAlertLogin() {
            //     this.$alert(
            //     "情報変更は正常に完了しました。メールアドレスを変更したため、再ログインお願いいたします。",
            //     "情報変更完了",
            //     "success"
            //     ).then(() => router.push('/'));
            // },
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
        },
        computed : {
        years: function () {
        // `this` は vm インスタンスを指します
        const year = new Date().getFullYear()
        return Array.from({length: year - 1900}, (value, index) => 1901 + index)
      }
    } ,
  mounted() {
          var data = JSON.parse(localStorage.getItem("nowuser"));
          this.userData = data;
            var birth_date = this.userData.birth_date;
                    this.birthday.day = birth_date.substring(0.,birth_date.indexOf(' '))
                    this.birthday.month = birth_date.substring(birth_date.indexOf(' ')+1,birth_date.lastIndexOf(' '))
                    this.birthday.year = birth_date.substring(birth_date.lastIndexOf(' ')+1,birth_date.length)
    //   Auth.currentAuthenticatedUser()
    // .then(user => {
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
    //                 this.birthday.year = birth_date.substring(0.,birth_date.indexOf('-'))
    //                 var month = parseInt(birth_date.substring(birth_date.indexOf('-')+1,birth_date.lastIndexOf('-'))) - 1
    //                 this.birthday.month = this.months[parseInt(month)];
    //                 this.birthday.day = birth_date.substring(birth_date.lastIndexOf('-')+1,birth_date.length)
    //                 this.userData.birth_date = parseInt(this.birthday.day) + ' '+ this.birthday.month + ' ' + this.birthday.year;
    //             })
    //             .catch(() => {}); 
    //         });
    // })
    // .catch(() => {});
  }
    };
</script>



