<template>
<div class="back-profile">
        
    <div>
        <a @click="logout()" class="home-link" >ログアウト</a>
        <router-link to="/home" class="home-link">ホーム</router-link>
    </div>
    <div class=" back-profile div-profile common">

            <div class="content">
                
            <h2 class="title">プロフィール</h2>
            <form >
                <div class="form-group">
                     <center> 
                         <img id="profileImage" v-if="userData.gender === 0" style="{width:1500px;height:150px;}" src="@/assets/male_profile.png" />
                         <img id="profileImage" v-else style="{width:1500px;height:150px;}" src="@/assets/female_profile.png" />
                    </center>
                </div>
                <div class="form-group">
                    <label class="profile-label-title" for="name">氏名</label>
                    <label class="profile-label">{{ userData.username }}</label>
                    
                </div>
                <div class="form-group">
                    <label class="profile-label-title" for="email">メールアドレス</label>
                    <label class="profile-label">{{ userData.email }}</label>
                    
                </div>

                <div class="form-group">
                    <label class="profile-label-title" for="mobile">連絡先</label>
                    <label class="profile-label">{{ userData.contact }}</label>                 
                </div>

                <div class="form-group">
                    <label class="profile-label-title" for="dob">生年月日</label>
                    <label class="profile-label">{{ userData.birth_date }}</label>
                </div>


                <div class="form-group">
                    <label class="profile-label-title" for="gender">性別</label>                   
                    <label class="profile-label" v-if="userData.gender === 0">男性</label>       
                    <label class="profile-label" v-else>女性</label>
                </div>    

                <div class="profile-btn-container">
                    <div class="button-wrapper">
                        <!-- <input type="button" name="sub" value="アカウント削除" id="form-agree_target" class="profile-btn" v-on:click="deleteUser()"> -->
                        <input type="button" name="sub" value="アカウント削除" id="form-agree_target" class="profile-btn" 
                        @click="deleteConfirm()"> 
                        &nbsp;&nbsp;
                        <input type="button" name="psw" value="パスワード変更" id="form-psw" class="profile-btn" v-on:click="passwordUpdate()">
                        &nbsp;&nbsp;
                        <input type="button" name="cancel" value="情報変更" id="form-cancel" class="profile-btn" v-on:click="infoUpdate()">
                    </div>
                </div>
            </form>
        </div>
    </div>        
</div>
</template>



<script>
import router from '../router';
import { Auth } from 'aws-amplify';
//import http from "../http-common";
import UserDataService from '../services/UserDataService';
//import axios from 'axios';
import COGNITO from '../aws-sdk/script.js';
const  months = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
export default { 
  data() {
    return {
        months: months,
        id:'',
        nowusername:'',
        status:'',
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
        }
    };
  },
  methods: {
    deleteConfirm() {
    this.$confirm("アカウントを削除します。宜しいですか？", "削除の確認") 
    .then(() => {
            //delete from cognito
            Auth.currentAuthenticatedUser()
            .then(user => {
                //delete from cognito
                COGNITO.deleteUser(user.signInUserSession.accessToken.jwtToken);
                
                //delete from dynamoDB
                UserDataService.delete(this.id)
                .then(response => {
                    this.status = response.data
                    if(this.status){
                        this.successAlert();
                    }
                })
                .catch(() => {}); 
            })
      })
      .catch(() => {});
  },
  successAlert() {
    this.$alert(
      "アカウントを正常に削除しました。",
      "アカウント削除完了",
      "success"
    ).then(() => router.push('/'));
  },
  infoUpdate(){
      router.push('/update');
  },passwordUpdate(){
      router.push('/password-update');
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
  },  
  mounted() {
      
          var data = JSON.parse(localStorage.getItem("nowuser"));
          this.userData = data;
          console.log("username:"+this.userData.username);
          console.log("bd: "+this.userData.birth_date);
    //   Auth.currentAuthenticatedUser()
    // .then(user => {
    //         this.nowusername = user.username;
    //         UserDataService.getNowUserID(this.nowusername)
    //         .then(response=>{
    //             this.id = response.data;
    //         })
    //         .then(() => { 
    //         //UserDataService.getNowUserByUsername(this.nowusername)
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
}

</script>
