package com.training.demowithaws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.lambda.runtime.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AWS Lambda function with S3 trigger.
 * 
 */
public class UserHandler {
	
	static final Logger log = LoggerFactory.getLogger(UserHandler.class);   


    private int now_user_id;
    public int getNowUserId(){
        return now_user_id;
    }

    public void setNowUserId(int now_user_id){
        this.now_user_id = now_user_id;
    }

    public int generateID(){
		AmazonDynamoDB adb = AmazonDynamoDBClientBuilder.defaultClient();
        DynamoDBScanExpression dynamoDBScanExpression = new DynamoDBScanExpression();
		DynamoDBMapper mapper = new DynamoDBMapper(adb);
        List<User> scanResult = mapper.scan(User.class, dynamoDBScanExpression);
        if(scanResult.size() == 0){
            return 1;
        }
        int nextID = scanResult.size() + 1;
        return nextID;
    }

    private String getNowDate(){
		Date now = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = df.format(now);
		return date;
	}

	public Object handleRequest(Request request, Context context){
		AmazonDynamoDB adb = AmazonDynamoDBClientBuilder.defaultClient();
		DynamoDBMapper mapper = new DynamoDBMapper(adb);
		User user = null;
		List<User> usersList = new ArrayList<User>();
        Map<String, String> attributeNames = new HashMap<String, String>();
        Map<String, AttributeValue> attributeValues = new HashMap<String, AttributeValue>();
        DynamoDBScanExpression dynamoDBScanExpression = new DynamoDBScanExpression();

        switch(request.getHttpMethod()){
            case "GET" :
                switch(request.getRequestUrl()){
                    case "id" :
                        user = mapper.load(User.class,request.getId());
                        if (user == null) {
                            throw new ResourceNotFoundException("Resource Not Found "+request.getId());
                        }
                        usersList.add(user);
                        return user;
                    case "nowuserid" :
                        if(attributeNames.size() > 0){
                            attributeNames.clear();
                        }
                        if(attributeValues.size() > 0){
                            attributeValues.clear();
                        }
                        attributeNames.put("#username", "username");                
                        attributeValues.put(":username", new AttributeValue(request.getUsername()));
                    
                        dynamoDBScanExpression.withFilterExpression("#username = :username")
                                            .withExpressionAttributeNames(attributeNames)
                                            .withExpressionAttributeValues(attributeValues);
                        List<User> userResultList = mapper.scan(User.class, dynamoDBScanExpression);
                        if (userResultList.size() == 0) {
                            return 0;
                        }
                        return userResultList.get(0).getId();
                    case "all" :
                        List<User> scanResult = mapper.scan(User.class, dynamoDBScanExpression);
                        for (User u : scanResult) {
                            usersList.add(u); 
                        }
                        
                        for (int i = 0; i < usersList.size(); i++) {

                            for (int j = usersList.size() - 1; j > i; j--) {
                                if (usersList.get(i).getId() > usersList.get(j).getId()) {
                                
                                    User tmp = usersList.get(i);
                                    usersList.set(i,usersList.get(j));
                                    usersList.set(j,tmp);
                                
                                }
                                
                            }
                                
                        }  
                        return usersList;
                    case "findmail" :
                        if(attributeNames.size() > 0){
                            attributeNames.clear();
                        }
                        if(attributeValues.size() > 0){
                            attributeValues.clear();
                        }
                        //Map<String, String> attributeNames = new HashMap<String, String>();
                        attributeNames.put("#email", "email");
                    
                        //Map<String, AttributeValue> attributeValues = new HashMap<String, AttributeValue>();
                        attributeValues.put(":email", new AttributeValue(request.getEmail()));
                    
                        dynamoDBScanExpression.withFilterExpression("#email = :email")
                                              .withExpressionAttributeNames(attributeNames)
                                              .withExpressionAttributeValues(attributeValues);
                        List<User> mailResultList = mapper.scan(User.class, dynamoDBScanExpression);
                        if (mailResultList.size() == 0) {
                            return false;
                        }
                        return true;                    
                    case "username" :
                        if(attributeNames.size() > 0){
                            attributeNames.clear();
                        }
                        if(attributeValues.size() > 0){
                            attributeValues.clear();
                        }
                        //Map<String, String> attributeNames = new HashMap<String, String>();
                        attributeNames.put("#username", "username");
                    
                        //Map<String, AttributeValue> attributeValues = new HashMap<String, AttributeValue>();
                        attributeValues.put(":username", new AttributeValue(request.getUsername()));
                    
                        dynamoDBScanExpression.withFilterExpression("#username = :username")
                                              .withExpressionAttributeNames(attributeNames)
                                              .withExpressionAttributeValues(attributeValues);
                        List<User> IDList = mapper.scan(User.class, dynamoDBScanExpression);
                        if (IDList.size() == 0) {
                            return 0;
                        }
                        return IDList.get(0);
                    case "check" :
                        if(attributeNames.size() > 0){
                            attributeNames.clear();
                        }
                        if(attributeValues.size() > 0){
                            attributeValues.clear();
                        }
                        attributeNames.put("#username", "username");
                        attributeNames.put("#password", "password");
                    
                        attributeValues.put(":username", new AttributeValue(request.getUsername()));
                        attributeValues.put(":password", new AttributeValue(request.getPassword()));
                    
                        dynamoDBScanExpression.withFilterExpression("#username = :username AND #password = :password")
                                              .withExpressionAttributeNames(attributeNames)
                                              .withExpressionAttributeValues(attributeValues);
                        List<User> checkResultList = mapper.scan(User.class, dynamoDBScanExpression);
                        if (checkResultList.size() == 0) {
                            return false;
                        }
                        setNowUserId(checkResultList.get(0).getId());

                        return true;
                    case "getRole":
                        if(attributeNames.size() > 0){
                            attributeNames.clear();
                        }
                        if(attributeValues.size() > 0){
                            attributeValues.clear();
                        }
                        attributeNames.put("#username", "username");
                        attributeValues.put(":username", new AttributeValue(request.getUsername()));
                        dynamoDBScanExpression.withFilterExpression("#username = :username")
                        .withExpressionAttributeNames(attributeNames)
                        .withExpressionAttributeValues(attributeValues);
                        usersList = mapper.scan(User.class, dynamoDBScanExpression);
                        if (usersList.size() == 0) {
                        return false;
                        }
                        return usersList.get(0).getRole();
                    
                    case "search":
                        switch(request.getSearchBy()){
                            case "nameemailbdrole":
                                    if(attributeNames.size() > 0){
                                        attributeNames.clear();
                                    }
                                    if(attributeValues.size() > 0){
                                        attributeValues.clear();
                                    }
                                    attributeNames.put("#username", "username");
                                    attributeNames.put("#email", "email");
                                    attributeNames.put("#birth_date", "birth_date");
                                    attributeNames.put("#role", "role");
                                    attributeValues.put(":username", new AttributeValue(request.getUsername()));
                                    attributeValues.put(":email", new AttributeValue(request.getEmail()));
                                    attributeValues.put(":birth_date", new AttributeValue(request.getBirth_date()));
                                    attributeValues.put(":role", new AttributeValue(request.getRole()));
                                    dynamoDBScanExpression.withFilterExpression("(#username = :username OR contains(#username,:username)) AND (#email = :email OR contains(#email,:email)) AND (#birth_date = :birth_date OR contains(#birth_date,:birth_date)) AND #role = :role")
                                    .withExpressionAttributeNames(attributeNames)
                                    .withExpressionAttributeValues(attributeValues);
                                    break;                            
                            case "nameemailrole":
                                if(attributeNames.size() > 0){
                                    attributeNames.clear();
                                }
                                if(attributeValues.size() > 0){
                                    attributeValues.clear();
                                }
                                attributeNames.put("#username", "username");
                                attributeNames.put("#email", "email");
                                attributeNames.put("#role", "role");
                                attributeValues.put(":username", new AttributeValue(request.getUsername()));
                                attributeValues.put(":email", new AttributeValue(request.getEmail()));
                                attributeValues.put(":role", new AttributeValue(request.getRole()));
                                dynamoDBScanExpression.withFilterExpression("(#username = :username OR contains(#username,:username)) AND (#email = :email OR contains(#email,:email)) AND #role = :role")
                                        .withExpressionAttributeNames(attributeNames)
                                        .withExpressionAttributeValues(attributeValues);
                                    break;
                            case "namebdrole":
                                if(attributeNames.size() > 0){
                                    attributeNames.clear();
                                }
                                if(attributeValues.size() > 0){
                                    attributeValues.clear();
                                }
                                attributeNames.put("#username", "username");
                                attributeNames.put("#birth_date", "birth_date");
                                attributeNames.put("#role", "role");
                                attributeValues.put(":username", new AttributeValue(request.getUsername()));
                                attributeValues.put(":birth_date", new AttributeValue(request.getBirth_date()));
                                attributeValues.put(":role", new AttributeValue(request.getRole()));
                                dynamoDBScanExpression
                                        .withFilterExpression("(#username = :username OR contains(#username,:username)) AND (#birth_date = :birth_date OR contains(#birth_date,:birth_date)) AND #role = :role")
                                        .withExpressionAttributeNames(attributeNames)
                                        .withExpressionAttributeValues(attributeValues);
                                    break;
                            case "emailbdrole":
                                if(attributeNames.size() > 0){
                                    attributeNames.clear();
                                }
                                if(attributeValues.size() > 0){
                                    attributeValues.clear();
                                }
                                attributeNames.put("#email", "email");
                                attributeNames.put("#birth_date", "birth_date");
                                attributeNames.put("#role", "role");
                                attributeValues.put(":email", new AttributeValue(request.getEmail()));
                                attributeValues.put(":birth_date", new AttributeValue(request.getBirth_date()));
                                attributeValues.put(":role", new AttributeValue(request.getRole()));
                                dynamoDBScanExpression.withFilterExpression("(#email = :email OR contains(#email,:email)) AND (#birth_date = :birth_date OR contains(#birth_date,:birth_date)) AND #role = :role")
                                        .withExpressionAttributeNames(attributeNames)
                                        .withExpressionAttributeValues(attributeValues);
                                    break;
                            case "nameemailbd":
                                    if(attributeNames.size() > 0){
                                        attributeNames.clear();
                                    }
                                    if(attributeValues.size() > 0){
                                        attributeValues.clear();
                                    }
                                    attributeNames.put("#username", "username");
                                    attributeNames.put("#email", "email");
                                    attributeNames.put("#birth_date", "birth_date");
                                    attributeValues.put(":username", new AttributeValue(request.getUsername()));
                                    attributeValues.put(":email", new AttributeValue(request.getEmail()));
                                    attributeValues.put(":birth_date", new AttributeValue(request.getBirth_date()));
                                    dynamoDBScanExpression.withFilterExpression("(#username = :username OR contains(#username,:username)) AND (#email = :email OR contains(#email,:email)) AND (#birth_date = :birth_date OR contains(#birth_date,:birth_date))")
                                    .withExpressionAttributeNames(attributeNames)
                                    .withExpressionAttributeValues(attributeValues);
                                    break;                            
                            case "nameemail":
                                if(attributeNames.size() > 0){
                                    attributeNames.clear();
                                }
                                if(attributeValues.size() > 0){
                                    attributeValues.clear();
                                }
                                attributeNames.put("#username", "username");
                                attributeNames.put("#email", "email");
                                attributeValues.put(":username", new AttributeValue(request.getUsername()));
                                attributeValues.put(":email", new AttributeValue(request.getEmail()));
                                dynamoDBScanExpression.withFilterExpression("(#username = :username OR contains(#username,:username)) AND (#email = :email OR contains(#email,:email))")
                                        .withExpressionAttributeNames(attributeNames)
                                        .withExpressionAttributeValues(attributeValues);
                                break;
                            case "namebd":
                                if(attributeNames.size() > 0){
                                    attributeNames.clear();
                                }
                                if(attributeValues.size() > 0){
                                    attributeValues.clear();
                                }
                                attributeNames.put("#username", "username");
                                attributeNames.put("#birth_date", "birth_date");
                                attributeValues.put(":username", new AttributeValue(request.getUsername()));
                                attributeValues.put(":birth_date", new AttributeValue(request.getBirth_date()));
                                dynamoDBScanExpression
                                        .withFilterExpression("(#username = :username  OR contains(#username,:username)) AND (#birth_date = :birth_date  OR contains(#birth_date,:birth_date))")
                                        .withExpressionAttributeNames(attributeNames)
                                        .withExpressionAttributeValues(attributeValues);
                                    break;
                            case "emailbd":
                                if(attributeNames.size() > 0){
                                    attributeNames.clear();
                                }
                                if(attributeValues.size() > 0){
                                    attributeValues.clear();
                                }
                                attributeNames.put("#email", "email");
                                attributeNames.put("#birth_date", "birth_date");
                                attributeValues.put(":email", new AttributeValue(request.getEmail()));
                                attributeValues.put(":birth_date", new AttributeValue(request.getBirth_date()));
                                dynamoDBScanExpression.withFilterExpression("(#email = :email  OR contains(#email,:email)) AND (#birth_date = :birth_date  OR contains(#birth_date,:birth_date))")
                                        .withExpressionAttributeNames(attributeNames)
                                        .withExpressionAttributeValues(attributeValues);
                                    break;
                            case "usernamerole":
                                if(attributeNames.size() > 0){
                                    attributeNames.clear();
                                }
                                if(attributeValues.size() > 0){
                                    attributeValues.clear();
                                }
                                attributeNames.put("#username", "username");
                                attributeNames.put("#role", "role");
                                attributeValues.put(":username", new AttributeValue(request.getUsername()));
                                attributeValues.put(":role", new AttributeValue(request.getRole()));
                                dynamoDBScanExpression.withFilterExpression("(#username = :username  OR contains(#username,:username)) AND #role = :role")
                                .withExpressionAttributeNames(attributeNames)
                                .withExpressionAttributeValues(attributeValues);
                                break; 
                            case "emailrole":
                                if(attributeNames.size() > 0){
                                    attributeNames.clear();
                                }
                                if(attributeValues.size() > 0){
                                    attributeValues.clear();
                                }
                                attributeNames.put("#email", "email");
                                attributeNames.put("#role", "role");
                                attributeValues.put(":email", new AttributeValue(request.getEmail()));
                                attributeValues.put(":role", new AttributeValue(request.getRole()));
                                dynamoDBScanExpression.withFilterExpression("(#email = :email  OR contains(#email,:email)) AND #role = :role")
                                .withExpressionAttributeNames(attributeNames)
                                .withExpressionAttributeValues(attributeValues);
                                break; 
                            case "bdrole":
                                if(attributeNames.size() > 0){
                                    attributeNames.clear();
                                }
                                if(attributeValues.size() > 0){
                                    attributeValues.clear();
                                }
                                attributeNames.put("#birth_date", "birth_date");
                                attributeNames.put("#role", "role");
                                attributeValues.put(":birth_date", new AttributeValue(request.getBirth_date()));
                                attributeValues.put(":role", new AttributeValue(request.getRole()));
                                dynamoDBScanExpression.withFilterExpression("(#birth_date = :birth_date  OR contains(#birth_date,:birth_date)) AND #role = :role")
                                .withExpressionAttributeNames(attributeNames)
                                .withExpressionAttributeValues(attributeValues);
                                break; 
                            case "role":
                                if(attributeNames.size() > 0){
                                    attributeNames.clear();
                                }
                                if(attributeValues.size() > 0){
                                    attributeValues.clear();
                                }
                                attributeNames.put("#role", "role");
                                attributeValues.put(":role", new AttributeValue(request.getRole()));
                                dynamoDBScanExpression.withFilterExpression("#role = :role")
                                .withExpressionAttributeNames(attributeNames)
                                .withExpressionAttributeValues(attributeValues);
                                break; 
                            case "username":
                                if(attributeNames.size() > 0){
                                    attributeNames.clear();
                                }
                                if(attributeValues.size() > 0){
                                    attributeValues.clear();
                                }
                                attributeNames.put("#username", "username");
                                attributeValues.put(":username", new AttributeValue(request.getUsername()));
                                dynamoDBScanExpression.withFilterExpression("#username = :username OR contains(#username,:username)")
                                        .withExpressionAttributeNames(attributeNames)
                                        .withExpressionAttributeValues(attributeValues);

                                break; 
                            case "email":
                                if(attributeNames.size() > 0){
                                    attributeNames.clear();
                                }
                                if(attributeValues.size() > 0){
                                    attributeValues.clear();
                                }
                                attributeNames.put("#email", "email");
                                attributeValues.put(":email", new AttributeValue(request.getEmail()));
                                dynamoDBScanExpression.withFilterExpression("#email = :email OR contains(#email,:email)")
                                        .withExpressionAttributeNames(attributeNames)
                                        .withExpressionAttributeValues(attributeValues);
                                break; 
                            case "birthdate":
                                if(attributeNames.size() > 0){
                                    attributeNames.clear();
                                }
                                if(attributeValues.size() > 0){
                                    attributeValues.clear();
                                }
                                attributeNames.put("#birth_date", "birth_date");
                                attributeValues.put(":birth_date", new AttributeValue(request.getBirth_date()));
                                dynamoDBScanExpression.withFilterExpression("#birth_date = :birth_date OR contains(#birth_date,:birth_date)")
                                        .withExpressionAttributeNames(attributeNames)
                                        .withExpressionAttributeValues(attributeValues);
                                break; 
                            case "default":
                                    break;
                        }
                        
                        List<User> scanList = mapper.scan(User.class, dynamoDBScanExpression);
                        for (User u : scanList) {
                            usersList.add(u); 
                        }
                        for (int i = 0; i < usersList.size(); i++) {

                            for (int j = usersList.size() - 1; j > i; j--) {
                                if (usersList.get(i).getId() > usersList.get(j).getId()) {
                                
                                    User tmp = usersList.get(i);
                                    usersList.set(i,usersList.get(j));
                                    usersList.set(j,tmp);
                                
                                }
                                
                            }
                                
                        }   
                        return usersList;
                    case "default":
                        break;
                }            
			case "POST" :
                user = request.getUser();
                user.setId(generateID());
                user.setCreated_date(getNowDate());
                user.setUpdated_date(getNowDate());
				try {
                    mapper.save(user);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
                
                //mapper.save(user);
				//return true;
                
			case "DELETE" :
                user = mapper.load(User.class,request.getId());
                if (user == null) {
                    throw new ResourceNotFoundException("Resource Not Found "+request.getId());
                }
                //delete from DynamoDB
                mapper.delete(user);

                //delete from Cognito
                // String username = "args[0]";

                // final AmazonIdentityManagement iam =
                //     AmazonIdentityManagementClientBuilder.defaultClient();

                // DeleteUserRequest req = new DeleteUserRequest()
                //     .withUserName(username);

                // try {
                //     iam.deleteUser(req);
                // } catch (DeleteConflictException e) {
                //     System.out.println("Unable to delete user. Verify user is not" +
                //             " associated with any resources");
                //     throw e;
                // }

                // System.out.println("Successfully deleted IAM user " + username);


                return true;
            case "PUT" :
                switch(request.getRequestUrl()){
                    case "id" :
                        user = mapper.load(User.class,request.getId());
                        User updateUser = request.getUser();
                        user.setContact(updateUser.getContact());
                        user.setBirth_date(updateUser.getBirth_date());
                        user.setGender(updateUser.getGender());
                        user.setPassword(updateUser.getPassword());
                        user.setUpdated_date(getNowDate());
                        mapper.save(user);
                        return true;
                    case "role" : 
                            String [] requestID = request.getSelectedID().split(",");
                            int[] selectedID = new int[requestID.length];
                            for(int i=0;i<requestID.length;i++){
                                selectedID[i] = Integer.parseInt(requestID[i]);
                            }
                            boolean result = false;
                            for(int j = 0; j< selectedID.length ; j++){
                                user = mapper.load(User.class,selectedID[j]);
                                user.setRole(request.getRole());
                                user.setUpdated_date(getNowDate());
                                mapper.save(user);
                                result = true;
                            }                      
                            return result;
                    case "passwordupdate" :
                        user = mapper.load(User.class,request.getId());
                        user.setPassword(request.getPassword());
                        user.setUpdated_date(getNowDate());
                        mapper.save(user);
                        //return user;
                        return true;
                    // case "forgotpassword" :
                    //     user = new User();
                    //     user.setId(request.getId());
                    //     user.setPassword(request.getPassword());
                    //     user.setUpdated_date(getNowDate());
                    //     mapper.save(user);
                    //     return true;
                    case "forgotpassword":
                        attributeNames.clear();
                        attributeValues.clear();
                        attributeNames.put("#username", "username");
                        attributeValues.put(":username", new AttributeValue(request.getUsername()));
                        dynamoDBScanExpression.withFilterExpression("#username = :username")
                        .withExpressionAttributeNames(attributeNames)
                        .withExpressionAttributeValues(attributeValues);
                        usersList = mapper.scan(User.class, dynamoDBScanExpression);
                        if (usersList.size() == 0) {
                            return false;
                        }
                        try {
                            user = usersList.get(0);
                            user.setPassword(request.getPassword());
                            user.setUpdated_date(getNowDate());
                            mapper.save(user);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return true;
                }
			case "default" :
				break;
        }		
		return null;
	}

}
