package com.sirding;

import com.sirding.annotation.Action;
import com.sirding.annotation.AssertParam;
import com.sirding.enums.OperType;

public class OperateLog {

	@Action(desc = "测试方法1用户[{0}],其密码为[{1}]")
	public void test1(String name, String pwd){
		System.out.println("name:" + name + ",pwd:" + pwd);
	}

	@Action(className = "User", operType = OperType.TYPE1)
	public void test2(User user){
		user.setOldUser(new User("old_zc.ding", "old_pwd"));
		System.out.println("name:" + user.getName() + ",pwd:" + user.getPwd());
	}

	@Action(asserParams = {
			@AssertParam(param = "0", value = "zc.ding", desc = "测试方法3-zc.ding用户[{0}],密码[{1}]"),
			@AssertParam(param = "0", value = "sirding", desc = "测试方法3-sirding用户[{0}],密码[{1}]")
	})
	public void test3(String name, String pwd){
		System.out.println("name:" + name + ",pwd:" + pwd);
	}

	@Action(desc = "测试test4-用户[${User.name}],密码[{User.pwd}]")
	public void test4(User user){
		System.out.println("name:" + user.getName() + ",pwd:" + user.getPwd());
	}
	
	@Action(asserParams = {
			@AssertParam(param = "${User.name}", value = "zc.ding", desc = "测试方法5用户[{User.name}],密码[{User.pwd}]"),
			@AssertParam(param = "${User.name}", value = "sirding", desc = "测试方法5用户[{User.name}],密码[{User.pwd}]")
	})
	public void test5(User user,String name, String pwd){
		System.out.println("user.name:" + user.getName() + ",user.pwd:" + user.getPwd() + ",name:" + name + ",pwd:" + pwd);
	}
	
	@Action(asserParams = {
			@AssertParam(param = "${0_User.name}", value = "zc.ding", desc = "测试方法6用户[{0_User.name}],密码[{0_User.pwd}]"),
			@AssertParam(param = "${0_User.name}", value = "sirding", desc = "测试方法6用户[{1_User.name}],密码[{1_User.pwd}]")
	})
	public void test6(User user, User user2){
		System.out.println("user.name:" + user.getName() + ",user.pwd:" + user.getPwd());
		System.out.println("user2.name:" + user2.getName() + ",user2.pwd:" + user2.getPwd());
	}
}
