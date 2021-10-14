package com.training.demowithaws;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "users_info")
public class User {

	@DynamoDBHashKey
	private int id;

	@DynamoDBAttribute
	private String username;

	@DynamoDBAttribute
	private String email;

	@DynamoDBAttribute
	private String password;

	@DynamoDBAttribute
	private String contact;

	@DynamoDBAttribute
	private String birth_date;

	@DynamoDBAttribute
	private int gender;

	@DynamoDBAttribute
	private String role;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getBirth_date() {
		return birth_date;
	}

	public void setBirth_date(String birth_date) {
		this.birth_date = birth_date;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getCreated_date() {
		return created_date;
	}

	public void setCreated_date(String created_date) {
		this.created_date = created_date;
	}

	public String getUpdated_date() {
		return updated_date;
	}

	public void setUpdated_date(String updated_date) {
		this.updated_date = updated_date;
	}

	@DynamoDBAttribute
	private String created_date;

	@DynamoDBAttribute
	private String updated_date;
	
	public User(int id, String username, String email, String password, String contact, String birth_date, int gender,
            String role, String created_date, String updated_date) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.contact = contact;
        this.birth_date = birth_date;
        this.gender = gender;
        this.role = role;
        this.created_date = created_date;
        this.updated_date = updated_date;
    }

    public User() {
		
	}
	
	public User(int id , String username) {
		this.id = id;
		this.username = username;		
	}
}

