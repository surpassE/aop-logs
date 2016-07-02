package com.sirding.annotation;

/**
 * 断言参数是否与期待的一致
 * @author zc.ding
 * @date 2016-06-27
 *
 */
public @interface AssertParam {

	/**
	 * 参数的索引位置, 所以值从0开始
	 * @default -1
	 * @return
	 */
	int index() default -1;
	
	String param() default "";
	
	/**
	 * 参数期待的值
	 * @default ""
	 * @return
	 */
	String value() default "";
	
	/**
	 * 说明信息, 信息中可包含{0}、{1}等配置，
	 * eg1 ：
	 * @Action(asserParams = {
	 *		@AssertParam(index = 1, value="on", desc = "激活用户[{0}]"),
	 *		@AssertParam(index = 1, value="off", desc = "锁定用户[{0}]")})
	 * public Json chgSftsUserSta(String serviceId, String oper){
	 *	...处理过程略
	 * }
	 * 那么{0}将被serviceId的值替换，目前只支持基本数据类型。
	 * eg2:
	 * @Action(asserParams = {
	 * 		@AssertParam(index = 1, value="", desc = "添加用户[{User.userName}]")
	 * })
	 * public void addUser(User user){
	 * 	处理流程...
	 * }
	 * 
	 * @return
	 */
	String desc() default "";
}
