<template>
 <div class="back-list">
    <div class="label-div">
        <label class="userlist-label">{{loginPerson.username}}<br/>{{loginPerson.email}}<br/>{{loginPerson.role}}</label>
    </div>
    <a @click="logout()" class="home-link" >ログアウト</a>
       <download-csv
            :data="users"
            :name="dataFile"
            :labels="labels"
            :fields="fields"
            v-on:export-finished="exported"
        >
    <a class="home-link" >リストダウンロード</a>
       </download-csv>
    <h3 class="list-title">ユーザーリスト</h3>
    <h3></h3>

    <div class="all-body">
        <div class="textbox-wrapper">
            <div>
                <label>氏名：</label>
                <input type="text"  id="name" name="name" class="list-input"  placeholder="例:Watanabe" v-model="searchUsername" />
            </div>
            <div>
                <label>メールアドレス：</label>
                <input type="text"  id="name" name="name" class="list-input"  placeholder="例: example@gmail.com"  v-model="searchEmail" />
            </div>
            <div>
                <label>生年月日：</label>
                <input type="text"  id="name" name="name" class="list-input"   placeholder="例:yyyy-mm-dd"  v-model="searchBirthDate" />
            </div>
            <div>
                <label>権限：</label>
                <select class="role-box" v-model="searchRole">
                    <option v-for="(role, key) in roles" :key="key" >{{ role }}</option>                       
                </select>
            </div>     
            <div>
                <input type="button" name="save" value="検索" id="form-agree_target" class="list-btn" @click="search()" />
            </div>    
             
            <div>
                <input type="button" name="save" value="クリア" id="form-agree_target" class="list-btn" @click="clear()" />
            </div> 

             <div class="role-btn-container">
                    <input type="button" name="roleupdate" value="権限更新" id="roleUpdate" class="role-btn-update"  @click="updateRoles()" >
            </div>
            <div class="role-btn-container">
                    <input type="button" name="addMember" value="新規登録" id="addMember" class="role-btn-update"  @click="addMember()" >
            </div> 

            <!-- <div class="role-btn-container">
                    <input type="button" name="download" value="ダウンロード" id="download" class="role-btn-update"  @click="download()" >
            </div>          -->
        </div> 
        <br>
        <br>
        <div v-bind:class="[(isAdmin === true)? 'wrapper' : 'wrapper-moderator']">
            <div id="header">氏名</div>
            <div id="header">メールアドレス</div>
            <div id="header">連絡先</div>
            <div id="header">生年月日</div>
            <div id="header">性別</div>
            <div id="header">権限</div>
            <div id="header" v-if="isAdmin">情報更新</div>
            <div id="header" v-if="isAdmin">削除</div>
            <div id="header">権限更新</div>
        </div>
        <div v-for="(user, k) in getItems" :key="k" v-bind:class="[(isAdmin === true)? 'wrapper-child' : 'wrapper-moderator-child']" >                      
            <div>{{ user.username }}</div>
            <div>{{ user.email }}</div>        
            <div>{{ user.contact }}</div>
            <div>{{ user.birth_date }}</div>
            <div v-if="user.gender === 0"> 男性 </div>
            <div v-else> 女性 </div>
            <div v-if="user.role === 'user'">一般ユーザー</div>
            <div v-else-if="user.role === 'admin'">管理者</div>
            <div v-else>モデレータ</div>
            <div v-if="isAdmin">
                <!-- <a class="lnk" type="button" v-on:click="updateUser(user.id)">更新</a>     -->
                <center><input type="button" name="save" value="更新" id="form-agree_target" class="list-btn-update" @click="updateUser(user.id)"></center>        
            </div>
            <div v-if="isAdmin">
                <!-- <a class="lnk" type="button" v-on:click="deleteConfirm(user.id)">削除</a> -->
                 <center><input type="button" name="save" value="削除" id="form-agree_target" class="list-btn-delete" @click="deleteConfirm(user.id,user.username)"></center>
            </div>
            <div>
                <input type="checkbox" id="accept" class="form-check-input" :checked="selectedId.includes(user.id)" :disabled="user.role === '管理者' && !isAdmin" @click="addId(user.id)">
            </div>
        </div>  
        <div v-if="users.length === 0">
            <br>
            <center><h3>検索した条件と当てはまるデータがございません。</h3></center>
        </div> 
        <br>
        <!--pagination start-->
        <!-- <div class="pgn-wrapper"> -->
        <div v-show="users.length > perPage">
            <vuejs-paginate
                :page-count="getPaginateCount"
                :prev-text="'<'"
                :next-text="'>'"
                :click-handler="paginateClickCallback"
                :container-class="'pagination'"
                :page-class="'page-item'"
                :page-link-class="'page-link'"
                :prev-class="'page-item'"
                :prev-link-class="'page-link'"
                :next-class="'page-item'"
                :next-link-class="'page-link'"
                :first-last-button="true"
                :first-button-text="'<<'"
                :last-button-text="'>>'"
        ></vuejs-paginate>
        </div>
        <!-- <a @click="download()" class="home-link" >ユーザーリストダウンロード</a>
        </div> -->
        <!--pagination end-->    
    </div>
</div>
</template>

<script>

import UserDataService from '../services/UserDataService.js';
import router from '../router';
import { Auth } from 'aws-amplify';
import JsonCSV from '../components/JsonCSV.vue'
import VueJsPaginate from "vuejs-paginate";//for paginate
import COGNITO from '../aws-sdk/script.js';

const  roles = ["全て", "一般ユーザー", "モデレータ", "管理者"];

export default { 
    components: {
    "vuejs-paginate": VueJsPaginate, //for paginate
    'download-csv': JsonCSV,//for csv download
  },
  data() {
    return {
        roles,
        searchUsername:"",
        searchEmail:"",
        searchBirthDate:"",
        searchRole:"全て",
        isAdmin:false,
        id:'',
        users: {
            id:'',
            username: "",
            email: "",
            password:"",
            contact: "",
            birth_date:"",
            gender:"",
            role:"",
            created_date:"",
            updated_date:""
        },
        loginPerson: {
            username: "",
            email: "",
            password:"",
            contact: "",
            birth_date:"",
            gender:"",
            role:"",
            created_date:"",
            updated_date:""
        },
        selectedId:[],
        //for paginate start
        currentPage: 1,
        perPage: 5,
        //for paginate end
        //for csv download        
      dataFile: 'users_list.csv',
      labels: {
            id:"ID",
            username: "氏名",
            email: "メールアドレス",
            contact: "連絡先",
            birth_date:"生年月日",
            gender:"性別",
            role:"権限",
            created_date:"作成日",
            updated_date:"更新日"
      },
      fields: ['id', 'username', 'email', 'contact', 'birth_date', 'gender', 'role', 'created_date', 'updated_date'],
      isExported: false
    };
  },
  methods: {
    exported() {
      if(this.users.gender === '0'){
          this.users.gender = '男性'
      }else{
          this.users.gender = '女性'
      }
      this.isExported = true
      setTimeout(() => {
        this.isExported = false
        this.$alert(
      "リストのダウンロードが完了しました。",
      "ダウンロード完了",
      "success"
    );
      }, 3 * 1000)
    },
    retrieveAllUsers(){
        this.selectedId = [];
        UserDataService.getAll()
        .then(response => {
            this.users = response.data; 

            //this.usersData = response.data;//for paginate
            }).catch(()=>{});        
      },
      retrieveAllUsersByRole(role){
        UserDataService.getAllByRole(role)
        .then(response => {
            this.users = response.data; 
        }).catch(()=>{});   
    },
    async deleteConfirm(id,username) {
    this.$confirm("このユーザーのアカウントを削除します。宜しいですか？", "削除の確認") 
    .then(() => {
            this.deleteUser(id,username);
      })
      .catch(() => {});
  },
  async deleteUser(id,username){
            //delete from user pool
            COGNITO.adminDeleteUser(username)
                
            //delete from dyanmoDB
            UserDataService.delete(id)
            .then(response => {
                this.status = response.data
                if(this.status){
                    this.successAlert();
                }
            })
            .catch(() => {}); 
  },
  successAlert() {
    this.$alert(
      "アカウントを正常に削除しました。",
      "アカウント削除完了",
      "success"
    ).then(() => {   
        this.retrieveAllUsers();
    }).then(() => router.push('/userslist')).catch(()=>{});
  },
  successRoleAlert() {
    this.$alert(
      "権限の更新処理は正常に完了しました。",
      "権限更新完了",
      "success"
    ).then(() => {        
        this.retrieveAllUsers();
    }).then(() => router.push('/userslist')).catch(()=>{});
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
    clear(){
        this.searchUsername = "";
        this.searchEmail = "";
        this.searchBirthDate = "";
        this.searchRole = '全て'

        this.retrieveAllUsers();
        
    },
    search(){
        if(this.searchRole === '全て'){
            if(this.searchUsername !== "" && this.searchEmail !=="" && this.searchBirthDate !==""){
            UserDataService.searchByNameEmailBirthDate(this.searchUsername,this.searchEmail,this.searchBirthDate)
            .then(response => {
                this.users = response.data; 
            }).catch(()=>{});
        }else if(this.searchUsername !== "" && this.searchEmail !==""){
            UserDataService.searchByUserNameAndEmail(this.searchUsername,this.searchEmail)
            .then(response => {
                this.users = response.data; 
            }).catch(()=>{});            
        }else if(this.searchUsername !== "" && this.searchBirthDate !==""){
            UserDataService.searchByUserNameAndBirthDate(this.searchUsername,this.searchBirthDate)
            .then(response => {
                this.users = response.data; 
            }).catch(()=>{});                        
        }else if(this.searchEmail !=="" && this.searchBirthDate !==""){
            UserDataService.searchByEmailAndBirthDate(this.searchEmail,this.searchBirthDate)
            .then(response => {
                this.users = response.data; 
            }).catch(()=>{});                                    
        }else if(this.searchUsername !== ""){
           UserDataService.searchByUserName(this.searchUsername)
           .then(response => {
                this.users = response.data; 
            }).catch(()=>{});                                                 
        }else if(this.searchEmail !==""){
            UserDataService.searchByEmail(this.searchEmail)
            .then(response => {
                this.users = response.data; 
            }).catch(()=>{});                                      
            
        }else if(this.searchBirthDate !== ""){
            UserDataService.searchByBirthDate(this.searchBirthDate)
            .then(response => {
                this.users = response.data; 
            }).catch(()=>{});                                    

        }else{          
            this.retrieveAllUsers();
        }
        }else{
            var requestRole;
            if(this.searchRole === '一般ユーザー'){
                requestRole = 'user';
            }
            else if(this.searchRole === '管理者'){
                requestRole = 'admin';
            }else if(this.searchRole === 'モデレータ'){
                requestRole = 'moderator';
            }
            if(this.searchUsername !== "" && this.searchEmail !=="" && this.searchBirthDate !==""){
                UserDataService.searchByNameEmailBirthDateRole(this.searchUsername,this.searchEmail,this.searchBirthDate,requestRole)
                .then(response => {
                    this.users = response.data; 
                }).catch(()=>{});
            }else if(this.searchUsername !== "" && this.searchEmail !==""){
                UserDataService.searchByUserNameAndEmailAndRole(this.searchUsername,this.searchEmail,requestRole)
                .then(response => {
                    this.users = response.data; 
                }).catch(()=>{});            
            }else if(this.searchUsername !== "" && this.searchBirthDate !==""){
                UserDataService.searchByUserNameAndBirthDateAndRole(this.searchUsername,this.searchBirthDate,requestRole)
                .then(response => {
                    this.users = response.data; 
                }).catch(()=>{});                        
            }else if(this.searchEmail !=="" && this.searchBirthDate !==""){
                UserDataService.searchByEmailAndBirthDateAndRole(this.searchEmail,this.searchBirthDate,requestRole)
                .then(response => {
                    this.users = response.data; 
                }).catch(()=>{});                                    
            }else if(this.searchUsername !== ""){
            UserDataService.searchByUserNameAndRole(this.searchUsername,requestRole)
            .then(response => {
                    this.users = response.data; 
                }).catch(()=>{});                                                 
            }else if(this.searchEmail !==""){
                UserDataService.searchByEmailAndRole(this.searchEmail,requestRole)
                .then(response => {
                    this.users = response.data; 
                }).catch(()=>{});                                      
                
            }else if(this.searchBirthDate !== ""){
                UserDataService.searchByBirthDateAndRole(this.searchBirthDate,requestRole)
                .then(response => {
                    this.users = response.data; 
                }).catch(()=>{});                                    

            }else{        
                this.retrieveAllUsersByRole(requestRole);
            }
        }
    },
    updateUser(id){        
        localStorage.setItem('beforepage','userslist');
        localStorage.setItem('selectedid',id);
        router.push("/infoupdate")
    },
    addId(id){
        if(this.selectedId.indexOf(id) === -1){
            this.selectedId.push(id);
        }else{
            this.selectedId.splice(this.selectedId.indexOf(id),1);
        }
      },
    updateRoles(){
            var requestRole = '全て';
            if(this.searchRole === '一般ユーザー'){
                requestRole = 'user';
            }
            else if(this.searchRole === '管理者'){
                requestRole = 'admin';
            }else if(this.searchRole === 'モデレータ'){
                requestRole = 'moderator';
            }
        if(requestRole !== '全て'){
            UserDataService.updateRoles(this.selectedId,requestRole)
            .then(response => {
                this.status = response.data
                if(this.status){
                    this.successRoleAlert();
                }
            })
            .catch(() => {}); 
        }else{
            this.warningAlert();
        }
        
    },
    warningAlert() {
    this.$alert(
      "権限を選択してください。",
      "",
      "warning"
    ).then(() => {});
  },
  //for paginate
  paginateClickCallback: function (pageNum) {
      this.currentPage = Number(pageNum);
    },
  addMember(){
        localStorage.setItem("addmember","addmember");
        router.push("/signup");
    },
    getCurrentLoginPerson(){
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
                    this.loginPerson = response.data;
                })
                .catch(() => {}); 
            });
    })
    .catch(() => {});
    }
  },
    mounted() {
        this.getCurrentLoginPerson();
        this.retrieveAllUsers();//for paginate
        var role = localStorage.getItem('role');
        if(role === '管理者'){
            this.isAdmin = true;
        }
  },
  //for paginate
  computed: {
    getItems: function () {
        let start = (this.currentPage - 1) * this.perPage;
        let end = this.currentPage * this.perPage;
        if(this.users.length > this.perPage){
            return this.users.slice(start, end);
        }else{
            return this.users;
        }
    },
    getPaginateCount: function () {
      return Math.ceil(this.users.length / this.perPage);
    }
  },
}

</script>