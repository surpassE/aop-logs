package com.sirding.parse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.sirding.annotation.Action;
import com.sirding.annotation.AssertParam;
import com.sirding.util.ReflectUtil;

public class AnnotationParse {

	protected static Logger logger = Logger.getLogger(AnnotationParse.class);
	
	public String getLogMsg(Action action, Object[] args){
		String msg = this.replaceParam(action.desc(), args);
		//判断是否通过参数反射获取要记录的日志信息
		String className = action.className();
		if(className != null && className.length() > 0){
			for(Object obj : args){
				//对象的为className或是对象的绝对路径包含className
				if(obj.getClass().toString() == className || obj.getClass().toString().toUpperCase().endsWith(className.toUpperCase())){
					String methodName = action.rollback();
					msg += "-" + (String)ReflectUtil.callAssignedMethod(obj, methodName, action.operType());
				}
			}
		}
		
		//判断是否通过断言参数值获得日志信息
		AssertParam[] arr = action.asserParams();
		if(arr != null && arr.length > 0){
			for(AssertParam ap : arr){
				if(ap.value() != null && ap.value().equalsIgnoreCase((String)(args[ap.index()]))){
					msg += "-" + ap.desc();
					for(int i = 0; i < args.length; i++){
						Object o = args[i];
						msg = msg.replace("{" + i + "}", o == null ? "" : o.toString());
					}
					msg = this.replaceParam(msg, args);
					break;
				}
			}
		}
		return null;
	}
	
	/**
	 * 将msg中{?}用args中指定索引的值替换掉
	 * @param msg 要替换的数据信息
	 * @param args 替换值得数组
	 * @return
	 */
	private String replaceParam(String msg, Object[] args){
		if(msg != null && args != null && args.length > 0){
			for(int i = 0; i < args.length; i++){
				Object o = args[i];
				msg = msg.replace("{" + i + "}", o == null ? "" : o.toString());
			}
		}
		return msg;
	}
	
	public static void main(String[] args) {
		String msg = "nihao111{A3a.bb}world{cc.dd}";
		Pattern pattern = Pattern.compile("(\\{[A-Za-z0-9]+\\.[A-Za-z0-9]+\\})");
//		Pattern pattern = Pattern.compile("(\\{[a-z]+)(\\d+)\\}"); 

		Matcher m = pattern.matcher(msg);
		while(m.find()){
			String tmp = m.group();
			System.out.println(tmp);
		}
		System.out.println("okok...");
	}
}
