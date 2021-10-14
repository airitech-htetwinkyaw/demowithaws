<template>
  <div id="app">
    <div class="container mt-5">
      <div class="row">
        <div class="col-md-12">
          <h1>Students List</h1>
          <p>Please add students whom you like or respect, and describe them in detail.</p>
          <table class="table col-md-12">
            <thead class="thead-dark">
              <tr>
                <th>ID</th>
                <th>NAME</th>
              </tr>
            </thead>
              <tbody v-if="students.length > 0">
              <tr v-for="(student,key) in students" :key="key">
                                <td>{{ student.id }}</td>
                                <td>{{ student.name }}</td>
              </tr>
            </tbody> 
             <!-- <tbody v-if="students.length > 1">
              <tr v-for="(student,key) in students" :key="key">
                                <td>{{ student.id }}</td>
                                <td>{{ student.name }}</td>
              </tr>
            </tbody>
            <tbody v-else-if="parseInt(students.id) > 0">
              <tr>
                  <td>{{ this.students.id }}</td>
                  <td>{{ this.students.name }}</td>
              </tr>
            </tbody>          -->
            <tbody v-else>
              <tr>
                  <td colspan="2">{{this.nodataMsg}}</td>
              </tr>
            </tbody>
          </table>
           <br>
           <div>
            <label>番号:</label>
            <input type="number" class="testing-input" id="exampleInputEmail1" placeholder="IDを入力してくだい" v-model="inputID"/>
          </div>
          <br />
          <div >
            <label>氏名:</label>
            <input type="text" class="testing-input" id="password" placeholder="氏名を入力してください" v-model="inputName"/>
          </div>
          
          <br>
                <div class="container">
                    <div class="button-wrapper">
                        <input type="button" name="save" value="登録" id="form-agree_target" class="profile-btn" v-on:click="addStudent()">
                        &nbsp;&nbsp;
                        <input type="button" name="back" value="更新" id="form-cancel" class="profile-btn" v-on:click="updateStudent()">
                        &nbsp;&nbsp;
                        <input type="button" name="back" value="削除" id="form-cancel" class="profile-btn" v-on:click="deleteStudent()">&nbsp;&nbsp;
                        <input type="button" name="back" value="全て表示" id="form-cancel" class="profile-btn" v-on:click="getAllStudents()">&nbsp;&nbsp;
                        <input type="button" name="back" value="対象のみ表示" id="form-cancel" class="profile-btn" v-on:click="showStudent()">
                    </div>
                </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
//import http from "../http-common";
    export default {
        data() {
            return {
              inputID:"",
              inputName:"",
              nodataMsg:"",
                students: {
                    id: "",
                    name: ""
                },
                err:true  
            }
        },
        methods:{
          addStudent(){
            var data = {
              id: this.inputID,
              name: this.inputName
            };
            axios
            .post('https://6vysmhj2pk.execute-api.us-east-1.amazonaws.com/dev/students/add',data)
            .then(response => {
                  this.status = response.data;
                  this.err = this.status;
            })
            .then(()=>{ 
                  this.getAllStudents();
              })
              .catch(() => {});   
          },
          updateStudent(){
              var data = {
                id: this.inputID,
                name: this.inputName
              };
              axios
              .put('https://6vysmhj2pk.execute-api.us-east-1.amazonaws.com/dev/students/update/'+this.inputID,data)
              .then(response => {
                    this.status = response.data;
                    this.err = this.status;
              })
              .then(()=>{ 
                    this.getAllStudents();
                })
                .catch(() => {});  
          },
          deleteStudent(){
              axios
              .delete('https://6vysmhj2pk.execute-api.us-east-1.amazonaws.com/dev/students/delete/'+this.inputID)
              .then(response => {
                    this.status = response.data;
                    this.err = this.status;
              })
              .then(()=>{ 
                    this.getAllStudents();
                })
                .catch(() => {});
          },
          showStudent(){
            axios
            .get('https://6vysmhj2pk.execute-api.us-east-1.amazonaws.com/dev/students/get/'+this.inputID)
            .then(response => {
              this.students = response.data
              console.log("Length: "+this.students.length);
              if(parseInt(this.students.id) > 0){
                this.nodataMsg = '';
              }else{
                this.nodataMsg = '検索したID'+this.inputID+'と当てはまるデータがございません。';
              }
            })
            .catch(function(error){ 
              alert('Studentsデータの取得に失敗しましたっ！' );
              console.log(error);
            });
          },
          getAllStudents(){
            axios
            .get('https://6vysmhj2pk.execute-api.us-east-1.amazonaws.com/dev/students/get/0')
            .then(response => {
              this.students = response.data
              console.log("Length: "+this.students.length);
            })
            .catch(function(error){ 
              alert('Studentsデータの取得に失敗しましたっ！' );
              console.log(error);
            });
          }
        },
        mounted() {
          this.getAllStudents();   
        }
    }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h3 {
  margin: 40px 0 0;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
</style>
