import http from "../http-common";

class UserDataService {
    checkUser(username,password){
        return http.get('/get/check/' + username + '/'+ password);
    }

    getAll() {
        return http.get('/get/all')
    }

    getAllByRole(role) {
        return http.get('/search/role/'+ role )
    }

    getNowUserID(username){
        return http.get("/get/nowuserid/"+username);
    }

    getNowUser(id){
        return http.get("/get/" + id);
    }
    getNowUserByUsername(username){
        return http.get("/get/username/" + username);
    }

    selectRole(username) {
        return http.get("/get/getrole/" + username);
    }

    searchByNameEmailBirthDate(username,email,birth_date){
        return http.get("/search/nameemailbd/"+ username +"/"+ email +"/"+ birth_date);
    }

    searchByUserNameAndEmail(username, email){
        return http.get("/search/nameemail/"+username+"/"+email);
    }

    searchByUserNameAndBirthDate(username,birth_date){
        return http.get("/search/namebd/"+ username +"/"+ birth_date);
    }

    searchByEmailAndBirthDate(email,birth_date){
        return http.get("/search/emailbd/"+ email +"/"+ birth_date);
    }

    searchByUserName(username){
        return http.get("/search/username/"+ username);
    }

    searchByEmail(email){
        return http.get("/search/email/"+ email);
    }

    searchByBirthDate(birth_date){
        return http.get("/search/birthdate/"+ birth_date);
    } 

    searchByNameEmailBirthDateRole(username,email,birth_date,role){
        return http.get("/search/nameemailbdrole/"+ username +"/"+ email +"/"+ birth_date+"/"+role);
    }

    searchByUserNameAndEmailAndRole(username, email,role){
        return http.get("/search/nameemailrole/"+username+"/"+email+"/"+role);
    }

    searchByUserNameAndBirthDateAndRole(username,birth_date,role){
        return http.get("/search/namebdrole/"+ username +"/"+ birth_date+"/"+role);
    }

    searchByEmailAndBirthDateAndRole(email,birth_date,role){
        return http.get("/search/emailbdrole/"+ email +"/"+ birth_date+"/"+role);
    }

    searchByUserNameAndRole(username,role){
        return http.get("/search/usernamerole/"+ username+"/"+role);
    }

    searchByEmailAndRole(email,role){
        return http.get("/search/emailrole/"+ email+"/"+role);
    }

    searchByBirthDateAndRole(birth_date,role){
        return http.get("/search/bdrole/"+ birth_date+"/"+role);
    }

       

    findEmail(email){
        return http.get("/get/findmail/"+ email);
    }

    updateRoles(id,role){
        return http.put("/update/role/" + id+'/'+ role);
    }

    updatePassword(id,newPassword){
        return http.put("/update/passwordupdate/" + id +'/'+ newPassword);
    }

    updateUser(id,data){
        return http.put("/update/" +id , data);
    }

    forgotPassword(username,cnewpassword){
        return http.put("/update/forgotpassword/" + username+'/'+ cnewpassword);
    }

    addUser(data){
        return http.post("/add",data);
    }

    delete(id){
        return http.delete("/delete/" + id);
    }
}
export default new UserDataService();