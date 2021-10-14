<template>
  <div class="home">
    <form>
      <div>
        <label class="home-label">カテゴリリスト</label>
        <a @click="logout()" class="home-link" >ログアウト</a>
        <a @click="profile()" class="home-link">プロフィール</a>
      </div>
      <div class="row">
        <div class="column">
          <div class="card">      
              <center class="img-bg-fe"><img class="box" id="frontend" src="@/assets/frontend.png" /></center>
              <button type="button" class="text-box text-box-frontend" v-on:click="goToFrontEnd()">フロントエンド</button>
          </div>
        </div>

        <div class="column">
          <div class="card">
              <center class="img-bg-be"><img class="box img-bg-be" id="backend" src="@/assets/backend.png" /></center>
              <button type="button" class="text-box text-box-backend" v-on:click="goToBackEnd()">バックエンド</button>
          </div>
        </div>
      </div>
  
      <div class="row">
        <div class="column">
          <div class="card">
              <center class="img-bg-db"><img class="box img-bg-db" id="database" src="@/assets/database.png" /></center>
              <button type="button" class="text-box text-box-db" v-on:click="goToDatabase()">データベース</button>
          </div>
        </div>
  
        <div class="column">
          <div class="card">
              <center class="img-bg-aws"><img class="box img-bg-aws" id="aws" src="@/assets/aws.png" /></center>
              <button type="button" class="text-box  text-box-aws" v-on:click="goToAWS()">アマゾンウェブサービス</button>
          </div>
        </div>
      </div>
    </form>
  </div>
</template>
 
<script>
import router from '../router';
import { Auth } from 'aws-amplify';
import UserDataService from '../services/UserDataService';
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
    goToFrontEnd() {
      localStorage.setItem("category",0);
      router.push("/front-end");
    },    
    goToBackEnd() {
      localStorage.setItem("category",1);
      router.push("/back-end");
    },
    goToDatabase() {
      localStorage.setItem("category",2);
      router.push("/database");
    },
    goToAWS() {
      localStorage.setItem("category",3);
      router.push("/aws");
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
    },
    profile(){
            Auth.currentAuthenticatedUser()
    .then(user => {
            this.nowusername = user.username;
            UserDataService.getNowUserID(this.nowusername)
            .then(response=>{
                this.id = response.data;
            })
            .then(() => { 
            //UserDataService.getNowUserByUsername(this.nowusername)
                UserDataService.getNowUser(this.id)
                .then(response => {
                    this.userData = response.data;

                    var birth_date = this.userData.birth_date;
                    var year = birth_date.substring(0.,birth_date.indexOf('-'))
                    var month = parseInt(birth_date.substring(birth_date.indexOf('-')+1,birth_date.lastIndexOf('-'))) - 1
                    var day = birth_date.substring(birth_date.lastIndexOf('-')+1,birth_date.length)
                    this.userData.birth_date = parseInt(day) + ' '+ this.months[parseInt(month)] + ' ' + year;

                    localStorage.setItem("nowuser",JSON.stringify(this.userData));
                    router.push("/profile");
                })
                .catch(() => {}); 
            });
    })
    .catch(() => {});
    }
  },
};
</script>