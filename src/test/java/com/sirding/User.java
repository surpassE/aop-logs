package com.sirding;

import com.sirding.enums.OperType;

public class User {

	private String name;
	private String pwd;
	private User oldUser;
	
	public User(){}
	public User(String name, String pwd){
		this.name = name;
		this.pwd = pwd;
	}
	
	public void showMsg(User user, String userName){
		System.out.println(user.getName());
		System.out.println(userName);
	}
	
	public void setOldUser(User user) {
		this.oldUser = user;
	}
	
	public String fitLog(OperType operType){
		String msg = "";
		switch (operType) {
		case TYPE1:
			msg += "name:" + oldUser.getName() + ",pwd:" + oldUser.getPwd();
			break;
		default:
			break;
		}
		return msg;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	
}
