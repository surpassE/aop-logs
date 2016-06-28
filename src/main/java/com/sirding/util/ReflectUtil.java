package com.sirding.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 反射的工具类
 * @author surpassE
 * @time 2016-01-22
 *
 */
public class ReflectUtil {

	/**
	 * 执行setXXX方法
	 * 
	 * @param obj 
	 * @param field 要调用的属性方法
	 * @param value 要设置的属性值
	 */
	public static void callSetMethod(Object obj, Field field, Object value){
		try {
			if(value != null){
				String methodName = getSetMethod(field.getName());
				Method method = null;
				if(field.getType() == String.class){
					method = obj.getClass().getDeclaredMethod(methodName, String.class);
					if(value.getClass() == ArrayList.class){
						@SuppressWarnings("unchecked")
						List<String> list = (List<String>)value;
						String msg = "";
						if(list != null){
							for(String tmp : list){
								msg += tmp + ",";
							}
							if(msg != null && msg.endsWith(",")){
								msg = msg.substring(0, msg.length() - 1);
							}
						}
						method.invoke(obj, msg);
					}else{
						method.invoke(obj, value.toString());
					}
				}else if(field.getType() == Integer.class){
					method = obj.getClass().getDeclaredMethod(methodName, Integer.class);
					method.invoke(obj, Integer.parseInt(value.toString()));
				}else if(field.getType() == Boolean.class){
					method = obj.getClass().getDeclaredMethod(methodName, Boolean.class);
					method.invoke(obj, Boolean.parseBoolean(value.toString()));
				}else if(field.getType() == Long.class){
					method = obj.getClass().getDeclaredMethod(methodName, Long.class);
					method.invoke(obj, Long.parseLong(value.toString()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 通过isXXX()或是getXXX()获得obj中对应的属性值
	 * 
	 * @param obj
	 * @param field
	 * @return
	 */
	public static Object callIsOrGetMethod(Object obj, Field field){
		Object value = null;
		try {
			if(obj != null){
				Class<?> clazz = obj.getClass();
				Object type = field .getType();
				//属性名称
				String fieldName = field.getName();
				String methodName = null;
				if(type instanceof Boolean && fieldName.startsWith("is")){
					methodName = getIsMethod(fieldName);
				}else{
					methodName = getGetMethod(fieldName);
				}
				Method method = clazz.getDeclaredMethod(methodName);
				value = method.invoke(obj);
			}
		} catch (Exception e) {}
		return value;
	}

	/**
	 * 调用指定方法名称的方法并获得返回值
	 * 
	 * @param obj
	 * @param methodName 指定要执行的方法名称
	 * @param args 要调用方法依赖的参数列表
	 * @return
	 */
	public static Object callAssignedMethod(Object obj, String methodName, Object... args){
		Object value = null;
		try {
			if(obj != null && methodName != null && methodName.length() > 0){
				Class<?> clazz = obj.getClass();
				Method[] methodArr = clazz.getMethods();
				if(methodArr != null){
					for(Method method : methodArr){
						if(methodName.equals(method.getName())){
							value = method.invoke(obj, args);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}
	
	/**
	 * 获得boolean类型值得取值方法
	 * 
	 * @param fieldName
	 * @return
	 */
	public static String getIsMethod(String fieldName){
		return "is" + toUpcaseFirstLetter(fieldName);
	}

	public static String getGetMethod(String fieldName){
		return "get" + toUpcaseFirstLetter(fieldName);
	}

	public static String getSetMethod(String fieldName){
		return "set" + toUpcaseFirstLetter(fieldName);
	}
	
	/**
	 * 将field首个字母转为大写
	 * @param filed
	 * @return
	 */
	public static String toUpcaseFirstLetter(String fieldName){
		//获得首字符
		String head = fieldName.substring(0,1);
		//将首字符转为大写
		String upperCaseHead = head.toUpperCase();
		return fieldName.replaceFirst(head, upperCaseHead);
	}
}
