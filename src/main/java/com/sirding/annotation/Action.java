package com.sirding.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.sirding.enums.OperType;

/**
 * 用于记录用户的详细操作，具体用法见示例
 * @author zc.ding
 * @date 2016-06-27
 * eg1：
 * @Action(description = "添加SFTS配置信息", operType = OperType.ADD, className = "SftsConfig", rollback = "fitLog")
 * public Json addSfts(HttpServletRequest reqeust, SftsConfig sftsConfig){略}
 * 日志处理接口@link UserOperLogAOP 中会@link SftsConfig#fitLog从而得到要存储的日志信息
 * 
 * eg2:
 * @Action(asserParams = {
 *		@AssertParam(index = 1, value="on", desc = "激活用户[{0}]"),
 *		@AssertParam(index = 1, value="off", desc = "锁定用户[{0}]")})
 * public Json chgSftsUserSta(String serviceId, String oper){
 *	...处理过程略
 * }
 * 日志处理接口@link UserOperLogAOP 中会将{0}用serviceId的值替换，目前只支持基本数据类型。
 * 
 */
//设置注解可以用在哪些地方  METHOD表示方法上
@Target(ElementType.METHOD)
//只有是RUNTIME时才可以在运行时通过反射获得注解的信息
@Retention(RetentionPolicy.RUNTIME)
public @interface Action {
	/**
	 * 操作日志的描述信息
	 * @default ""
	 * @return
	 */
	public String desc() default "";
	
	/**
	 * 操作日志的类型
	 * @default null
	 * @return
	 */
	public OperType operType() default OperType.NULL;
	
	/**
	 * 默认的参数的类型,可以是实体类的完整名称，也可以是类的名称
	 * eg com.jtsec.model.UserInfo or UserInfo
	 * @default ""
	 * @return
	 */
	public String className() default "";
	
	/**
	 * 组装日志信息的回调方法
	 * @default fitLog
	 * @return
	 */
	public String rollback() default "fitLog";
	
	/**
	 * 断言指定位置的参数值是否与期待的值一致
	 * @default {}
	 * @return
	 */
	AssertParam[] asserParams() default {};
	
}
