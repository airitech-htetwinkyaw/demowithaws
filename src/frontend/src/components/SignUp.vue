<template>
<div class="back-singup">
    <div class="div-signup common">
        <div class="content">
            <div>
                <h2 class="alert-heading hdcenter">{{ titleMessage }}</h2>
            </div>            
            <form @submit.prevent="isAdmin ? update() : handleSubmit()">
                <div class="form-group">
                    <label for="name">名前</label>
                    <input type="text" v-model="userForm.username" id="name" name="name" 
                    class="signup-input"  :class="{ 'is-invalid': isSubmitted && $v.userForm.username.$error }"  :disabled="isAdmin" />
                    <div v-if="isSubmitted && $v.userForm.username.$error" class="invalid-feedback">
                        <span v-if="!$v.userForm.username.required">名前を入力してください</span>
                        <span v-if="!$v.userForm.username.maxLength">名前を15桁以下入力してください</span>
                    </div>
                </div>
                <div class="form-group">
                    <label for="email">メールアドレス</label>
                    <input type="email" v-model="userForm.email" id="email" name="email" class="signup-input"
                        :class="{ 'is-invalid': isSubmitted && $v.userForm.email.$error }"  :disabled="isAdmin" />
                    <div v-if="isSubmitted && $v.userForm.email.$error" class="invalid-feedback">
                        <span v-if="!$v.userForm.email.required">メールアドレスを入力してください</span>
                        <span v-if="!$v.userForm.email.email">正しいメールアドレスを入力してください</span>
                    </div>   
                    <span v-show="this.err" class="fontred">メールアドレスは登録済みです。</span> 
                </div>

                <div class="form-group">
                    <label for="mobile">連絡先</label>
                    <input type="number" v-model="userForm.contact" id="mobile" name="mobile" class="signup-input"
                        :class="{ 'is-invalid': isSubmitted && $v.userForm.contact.$error }" />
                    <div v-if="isSubmitted && $v.userForm.contact.$error" class="invalid-feedback">
                        <span v-if="!$v.userForm.contact.required">連絡先を入力してください</span>
                        <span v-if="!$v.userForm.contact.minLength">連絡先を11桁以上入力してください</span>
                        <span v-if="!$v.userForm.contact.maxLength">連絡先を15桁以下入力してください</span>
                    </div>
                </div>
                <div class="form-group">
                    <label for="dob">生年月日</label>
                    <div class="dob">
                        <select v-model="userForm.year" class="signup-bd-year-label"  :required="true">
                            <option disabled value="" selected>年</option>
                            <option v-for="(year,key) in years"  :key="key">{{ year }}</option>                       
                        </select><label class="bd-dash">-</label>  

                        <select v-model="userForm.month" class="signup-bd-month-label" :required="true">
                            <option disabled value="" selected>月</option>
                            <option v-for="(month, key) in months" :key="key">{{ month }}</option>                       
                        </select><label class="bd-dash">-</label>
                        
                        <select v-model="userForm.day" class="signup-bd-day-label" :required="true">
                            <option disabled value="" selected>日</option>
                            <option v-for="day in 31" :key="day">{{ day }}</option>                  
                        </select>                      
                    </div>
                </div>
                <div class="form-group">
                    <label for="gender">性別</label>
                    <div class="form-group" :class="{ 'is-invalid': isSubmitted && $v.userForm.gender.$error }">
                        <div class="form-check form-check-inline" :class="{ 'is-invalid': isSubmitted && $v.userForm.gender.$error }">
                            <input class="form-check-input" type="radio" name="gender" v-model="userForm.gender" id="gender1" value="0">
                            <label class="form-check-label" for="gender1">男性</label>
                        </div>
                        <div class="form-check form-check-inline" :class="{ 'is-invalid': isSubmitted && $v.userForm.gender.$error }">
                            <input class="form-check-input" type="radio" name="gender" v-model="userForm.gender" id="gender2" value="1">
                            <label class="form-check-label" for="gender2">女性</label>
                        </div>

                        <div v-if="isSubmitted && $v.userForm.gender.$error" class="invalid-feedback">
                            <span v-if="!$v.userForm.gender.required">性別を選択してください</span>
                        </div>                    
                    </div>
                </div>

                <div class="form-group">
                    <label for="password">パスワード</label>
                    <input type="password" v-model="userForm.password" id="password" name="password" class="signup-input"
                        :class="{ 'is-invalid': isSubmitted && $v.userForm.password.$error }" />
                    <div v-if="isSubmitted && $v.userForm.password.$error" class="invalid-feedback">
                        <span v-if="!$v.userForm.password.required">パスワードを入力してください</span>
                        <span v-if="!$v.userForm.password.minLength">パスワードを5桁以上入力してください</span>
                    </div>
                </div>

                <div class="form-group">
                    <label for="confirmPassword">パスワード（確認）</label>
                    <input type="password" v-model="userForm.confirmPassword" id="confirmPassword" name="confirmPassword"
                        class="signup-input" :class="{ 'is-invalid': isSubmitted && $v.userForm.confirmPassword.$error }" />
                    <div v-if="isSubmitted && $v.userForm.confirmPassword.$error" class="invalid-feedback">
                        <span v-if="!$v.userForm.confirmPassword.required">パスワード（確認）を入力してください</span>
                        <span v-else-if="!$v.userForm.confirmPassword.sameAsPassword">パスワード（確認）はパスワードと一致していません</span>
                    </div>
                </div>

                <div class="form-group form-check">
                    <input type="checkbox" v-model="userForm.accept" @change="$v.userForm.accept.$touch()" id="accept" class="form-check-input">
                    <label class="form-check-label" :class="{ 'is-invalid': isSubmitted && $v.userForm.accept.$error }" for="accept">同意</label>

                    <div v-if="isSubmitted && $v.userForm.accept.$error" class="invalid-feedback">
                        <span v-if="!$v.userForm.accept.required">同意にチェックしてください</span>
                    </div>
                </div>
                <br />
                <div class="container">
                    <div class="button-wrapper">
                       <input type="button" name="reset" value="リセット" id="form-reset" class="profile-btn" v-on:click="reset()" v-if="!isAdmin" />
                        &nbsp;&nbsp;                        
                        <input type="submit" name="sub" value="確認" id="form-agree_target" class="profile-btn" v-if="!isAdmin" />
                        <input type="submit" name="sub" value="更新" id="form-agree_target" class="profile-btn" v-if="isAdmin" />
                        &nbsp;&nbsp;
                        <input type="button" name="cancel" value="キャンセル" id="form-cancel" class="profile-btn" v-on:click="cancel()">
                    </div>
                </div>
            </form>
        </div>
    </div>        
</div>
</template>



<script>
    import {required, email, minLength, maxLength, sameAs } from "vuelidate/lib/validators";
    import router from "../router";
    import UserDataService from '../services/UserDataService';
    const  months = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
    export default {
        props: ['backData'],
        data() {
            return {
                userForm: {
                    username: "",
                    email: "",
                    contact: "",
                    gender: "",
                    password: "",
                    confirmPassword: "",
                    accept: "", 
                    birth_date:"",
                    year : "",
                    month: "",
                    day: "",
                },
                months,                
                isSubmitted: false,
                usrData: "",
                titleMessage:"",
                isAdmin:false,
                err:false,
            };
        },
        validations: {
            userForm: {
                username: {
                    required,
                    maxLength: maxLength(15)
                },
                email: {
                    required,
                    email
                },
                contact: {
                    required,
                    minLength: minLength(11),
                    maxLength: maxLength(15)
                },
                gender: {
                    required
                },
                password: {
                    required,
                    minLength: minLength(8)
                },
                confirmPassword: {
                    required,
                    sameAsPassword: sameAs('password')
                },
                accept: {
                    required (val) {
                      return val
                    }
                }
            }
        },
        methods: {
            handleSubmit() {
                this.isSubmitted = true;
                this.$v.$touch();
                if (this.$v.$invalid) {
                    return;
                }
                // UserDataService.findEmail(this.userForm.email)
                // .then(response => {
                //     this.status = response.data;
                //     this.err = this.status;
                // })
                // .then(()=>{ 
                //     if(this.status === false)  {
                //         localStorage.setItem('torokuData',JSON.stringify(this.userForm));
                //         router.push("/confirm").catch(() => {});
                //     } 
                // })
                // .catch(() => {}); 

                
                localStorage.setItem('torokuData',JSON.stringify(this.userForm));
                router.push("/confirm").catch(() => {});
                
            },
            reset(){
                Object.assign(this.$data, this.$options.data.call(this))
                var beforepage = localStorage.getItem('beforepage');
                if(beforepage === 'login'){
                    this.titleMessage = "ユーザー情報入力";
                }else{
                    this.titleMessage = "ユーザー情報変更";
                }
            },
            cancel(){
                if(this.isAdmin){
                    router.push({name: 'userslist'});
                }else{
                    router.push({name: 'login'});
                }
            },
            update(){
                this.isSubmitted = true;
                this.$v.$touch();
                if (this.$v.$invalid) {
                    return;
                }

                var birthdate = ''
                var birth_month = this.months.indexOf(this.userForm.month) + 1
                if(parseInt( birth_month) < 10 && parseInt( this.userForm.day) < 10){
                    birthdate = this.userForm.year + '-0' + birth_month + '-0' + this.userForm.day;
                }else if(parseInt( birth_month) < 10 ){
                    birthdate = this.userForm.year + '-0' + birth_month + '-' + this.userForm.day;
                }else if(parseInt( this.userForm.day) < 10){
                    birthdate = this.userForm.year + '-' + birth_month + '-0' + this.userForm.day;        
                }else{
                    birthdate = this.userForm.year + '-' + birth_month + '-' + this.userForm.day;
                }

                this.userForm.birth_date = birthdate;
                
                var data = {
                    // username: this.userForm.name,
                    // email: this.userForm.email,
                    contact: this.userForm.contact,
                    birth_date: this.userForm.birth_date,
                    gender: this.userForm.gender,
                    password:this.userForm.password
                };     

                var id = localStorage.getItem('selectedid');
                console.log("contact: "+data.contact);

                UserDataService.updateUser(id , data)
                .then(response => {
                    this.status = response.data
                    if(this.status === true){
                        this.successAlert();
                    }
                }).catch(() => {}); 
            },
            successAlert() {
                this.$alert(
                "情報変更は正常に完了しました。",
                "情報変更完了",
                "success"
                ).then(() => router.push('/userslist'));
            },
        },
        created : function(){
            if(this.backData !=null){
                this.userForm.username = this.backData.username;
                this.userForm.email = this.backData.email;
                this.userForm.contact = this.backData.contact;
                this.userForm.year = this.backData.year;
                this.userForm.month = this.backData.month;
                this.userForm.day = this.backData.day;
                this.userForm.gender = this.backData.gender;
                this.userForm.accept = this.backData.accept;
            }

            var beforepage = localStorage.getItem('beforepage');
            if(beforepage === 'login'){
                this.titleMessage = "ユーザー情報入力";
                this.isAdmin = false;
            }else{
                this.titleMessage = "ユーザー情報変更";
            }
            
            if(this.titleMessage === "ユーザー情報変更"){
                this.isAdmin = true;
                var id = localStorage.getItem('selectedid');
            UserDataService.getNowUser(parseInt(id))
            .then(response => {
                this.userForm = response.data;
                var birth_date = this.userForm.birth_date;
                this.userForm.year = birth_date.substring(0.,birth_date.indexOf('-'))
                var month = birth_date.substring(birth_date.indexOf('-')+1,birth_date.lastIndexOf('-'))
                this.userForm.month = this.months[parseInt(month)-1];
                this.userForm.day = parseInt(birth_date.substring(birth_date.lastIndexOf('-')+1,birth_date.length))

                this.userForm.confirmPassword = response.data.password;  
            })
            .catch(() => {}); 
            }
        },
        computed : {
            years: function () {
            const year = new Date().getFullYear()
            return Array.from({length: year - 1900}, (value, index) => 1901 + index)
            }
        }
    

    };
</script>

