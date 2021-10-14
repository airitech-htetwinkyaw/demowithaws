"use strict";
//exports.__esModule = true;
var AWS = require("aws-sdk");
AWS.config.region = 'us-east-1'; // Region
AWS.config.credentials = new AWS.CognitoIdentityCredentials({
    IdentityPoolId: 'us-east-1:18e5eed0-2bbf-4b9a-a9e5-4104e6b98a2d',
});
AWS.config.apiVersions = {
    cognitoidentityserviceprovider: '2016-04-18',
    // other service API versions
};
var cognitoidentityserviceprovider = new AWS.CognitoIdentityServiceProvider();
// var params = {
//     UserPoolId: 'us-east-1_qkmNTAwow', /* required */
//   };

export default{
    adminDeleteUser(username){
        var params = {
            UserPoolId: 'us-east-1_qkmNTAwow', /* required */
            Username: username /* required */
          };
         cognitoidentityserviceprovider.adminDeleteUser(params, function(err, data) {
            if (err) {
                console.log("err reach");
                return false;
                //console.log(err, err.stack); // an error occurred
            }else{
                console.log(data);           // successful response
                return true;
            }   
          });
          
    },
    deleteUser(accessToken){
        var params = {
            AccessToken: accessToken /* required */
          };
          cognitoidentityserviceprovider.deleteUser(params, function(err, data) {
            if (err) {
                console.log("err reach");
                //console.log(err, err.stack); // an error occurred
            }else{
                console.log(data);           // successful response
            }   
          })
    }
}

