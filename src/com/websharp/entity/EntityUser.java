package com.websharp.entity;

public class EntityUser {

	public String UserID = "";
	public String UserName = "";
	public String Password = "";
	public String Telephone = "";
	public String Email = "";
	public String NavigationUrl = "";
	public int ID = 0;

	public String getUserID() {
		return UserID;
	}

	public void setUserID(String userID) {
		UserID = userID;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}

	public String getTelephone() {
		return Telephone;
	}

	public void setTelephone(String telephone) {
		Telephone = telephone;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getNavigationUrl() {
		return NavigationUrl;
	}

	public void setNavigationUrl(String navigationUrl) {
		NavigationUrl = navigationUrl;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

}
