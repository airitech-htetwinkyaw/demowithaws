<template>
  <div class="back">
    <div class="div-center common form-color">
      <div class="content form-color">
        <h3 class="hdcenter">承認コードを入力してください</h3>
        <form>
          <div class="code">
            <p v-bind:class="[(resend === true)? 'resend-msg' : 'code-msg']"><span>{{this.infoMsg}}</span></p>
          </div>
          <div class="form-group">
            <label for="name">氏名</label>
            <input type="text" class="form-control login-input" id="name" disabled="true" v-model="userForm.username" />
          </div>
          <br />
          <div class="form-group">
            <label for="confirmCode">承認コード</label>
            <input type="number" class="form-control  login-input" id="confirmCode" placeholder="承認コードを入力してください" v-on:keyup="updateErrorContent()" v-model="confirmCode"/>
            <label class="error-message" v-if="errorMsg !==''">{{ errorMsg }}</label>
          </div>
          <div class="code">
          コード紛失の場合
          <a class="lnk" type="button" v-on:click="resendConfirmationCode()">コードの再送信</a>
          </div>
          
          <div class="code-container">
            <button type="button" class="code-btn" v-on:click="confirmSignUp()">承認</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template> 

<script>
import { Auth } from 'aws-amplify';
import router from "../router"; 
import UserDataService from '../services/UserDataService';
    const  months = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
 export default {
  data() {
    return {
      months:months,
      confirmCode: "",
      errorMsg: "",
                userForm: {
                    username: "",
                    email: "",
                    contact: "",
                    gender: "",
                    password: "",
                    year : "",
                    month: "",
                    day: ""
                },  
                infoMsg:'' ,
                resend:false,
                chk:""
    };
  },
    methods: {
        async confirmSignUp() {
            try {
              
                await Auth.confirmSignUp(this.userForm.username, this.confirmCode);
                if(this.chk === "confirm"){
                  router.push("/complete").catch(() => {}); 
                }else{
                  this.registerToDynamoDB();
                }
                //router.push("/complete").catch(() => {}); 
            } catch (error) {
                if(this.confirmCode === ''){
                    this.errorMsg = '承認コードを入力してください。';
                }else{                    
                    this.errorMsg = '無効な確認コードが提供されました。再試行してください。';
                }
            }
        },
        async resendConfirmationCode() {
            try {
                await Auth.resendSignUp(this.userForm.username);
                this.resend = true;
                this.infoMsg = this.userForm.email + '宛に承認コードを再送信しました。';
            } catch (err) {
                console.log('error resending code: ', err);
            }
    },        
    updateErrorContent(){
      if(this.errorMsg !== ''){
        this.errorMsg = '';
      }
     },
    registerToDynamoDB() {
          
          localStorage.setItem('confirm', 'confirm');

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
            })
            .then(()=>{ 
                if(this.status === true)  {
                  router.push("/complete").catch(() => {}); 
                } 
            })
            .catch(() => {});   
        }
    },  
        mounted() {
          
          if(localStorage.getItem("torokuData") !== null){
            var data = JSON.parse(localStorage.getItem("torokuData"));
            this.userForm.username = data.username.toLowerCase()
            this.userForm.email = data.email
            this.userForm.contact = data.contact
            this.userForm.gender = data.gender
            this.userForm.year = data.year
            this.userForm.month = data.month
            this.userForm.day = data.day
            this.userForm.password = data.password
            this.infoMsg = this.userForm.email + '宛に承認コードを送信しました。';
          }else{
          //if(localStorage.getItem('loginName') !== null){ //admin が追加したユーザーの承認   
            this.userForm.username = localStorage.getItem('loginName'); //mmt
            this.infoMsg = 'メールアドレスに送信した承認コードを使用してください。';
          }

          
          this.chk = localStorage.getItem("complete");
  }
};
</script>-->

