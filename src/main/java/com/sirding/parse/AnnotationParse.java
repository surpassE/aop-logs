package com.sirding.parse;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.sirding.annotation.Action;
import com.sirding.annotation.AssertParam;
import com.sirding.model.MethodParamInfo;
import com.sirding.util.ReflectUtil;

public class AnnotationParse {

	protected static Logger logger = Logger.getLogger(AnnotationParse.class);
	private Pattern pattern = Pattern.compile("(\\$*\\{[0-9_]*[A-Za-z0-9]+\\.[A-Za-z0-9]+\\})");

	
	public String getLogMsg(Action action, Object[] args){
		String desc = action.desc();
		String msg = this.replaceParamByIndex(desc, args);
		msg = this.replaceParamByType(msg, args);
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
				desc = ap.desc();
				boolean flag = false;
				String param = ap.param();
				String value = ap.value();
				if(param != "" && param.length() > 0 && value != "" && value.length() > 0){
					if(param.matches("[0-9]+")){
						int index = Integer.parseInt(param);
						if(args[index] != null && value.equalsIgnoreCase(args[index].toString())){
							flag = true;
						}
					}else if(param.matches(pattern.pattern())){
						Map<String, MethodParamInfo> aopMap = this.methodParamToMap(args);
						MethodParamInfo mpi = this.paramToMethodParamInfo(param);
						MethodParamInfo aopMpi = aopMap.get(mpi.getKey());
						if(aopMpi != null){
							Object val = ReflectUtil.callAssignedMethod(aopMpi.getObj(), mpi.getMethodName());
							if(val != null && val.toString().equalsIgnoreCase(value)){
								flag = true;
							}
						}
					}
					if(flag){
						msg += this.replaceParamByIndex(desc, args);
						msg = this.replaceParamByType(msg, args);
						break;
					}
				}
			}
		}
		return msg;
	}
	
	/**
	 * 将msg中{?}用args中指定索引的值替换掉
	 * @param msg 要替换的数据信息
	 * @param args 替换值得数组
	 * @return
	 */
	private String replaceParamByIndex(String msg, Object[] args){
		if(msg != null && msg.length() > 0 && args != null && args.length > 0){
			for(int i = 0; i < args.length; i++){
				Object o = args[i];
				msg = msg.replace("{" + i + "}", o == null ? "" : o.toString());
			}
		}
		return msg;
	}
	
	/**
	 * 
	 * @param msg
	 * @param args
	 * @return
	 * @author zc.ding
	 * @date 2016年7月2日
	 */
	private String replaceParamByType(String msg, Object[] args){
		Map<String, MethodParamInfo> map = this.getRegexToMap(msg);
		Map<String, MethodParamInfo> aopMap = this.methodParamToMap(args);
		for(String key : map.keySet()){
			MethodParamInfo mpi = map.get(key);
			String methodName = mpi.getMethodName();
			MethodParamInfo aopMpi = aopMap.get(mpi.getAopMpiKey());
			if(aopMpi != null){
				Object value = ReflectUtil.callIsOrGetMethod(aopMpi.getObj(), methodName);
				if(value != null){
					String regex = mpi.getRegex();
					regex = regex.replaceAll("\\$", "\\\\\\$").replaceAll("\\{", "\\\\\\{").replaceAll("\\}", "\\\\\\}");
					msg = msg.replaceAll(regex, value.toString());
				}
			}
		}
		return msg;
	}
	
	/**
	 * 将方法的参数转为map对象
	 * @param args
	 * @return
	 * @author zc.ding
	 * @date 2016年7月2日
	 */
	private Map<String, MethodParamInfo> methodParamToMap(Object[] args){
		Map<String, MethodParamInfo> map = new HashMap<String, MethodParamInfo>();
		if(args != null){
			for(int i = 0; i < args.length; i++){
				Object obj = args[i];
				if(obj != null){
					String className = obj.getClass().toString();
					className = className.substring(className.lastIndexOf(".") + 1);
					MethodParamInfo mpi = new MethodParamInfo(i, className, obj);
					map.put(mpi.getKey(), mpi);
					map.put(className, mpi);
				}
			}
		}
		return map;
	}
	
	/**
	 * 
	 * @param msg
	 * @return
	 * @author zc.ding
	 * @date 2016年7月2日
	 */
	private Map<String, MethodParamInfo> getRegexToMap(String msg){
		Map<String, MethodParamInfo> map = new HashMap<String, MethodParamInfo>();
		Matcher m = pattern.matcher(msg);
		while(m.find()){
			MethodParamInfo mpi = this.paramToMethodParamInfo(m.group());
			map.put(mpi.getKey(), mpi);
		}
		return map;
	}
	
	/**
	 * 将${User.userName}转为MethodParamInfo对象
	 * @param param
	 * @return
	 * @author zc.ding
	 * @date 2016年7月2日
	 */
	private MethodParamInfo paramToMethodParamInfo(String param) {
		String regex = param;
		String className = "";
		String methodName = "";
		int index = -1;
		param = param.replaceAll("\\{|\\}|\\$", "");
		String[] arr = param.split("\\.");
		methodName = arr[1];
		String[] clazzArr = arr[0].split("_");
		if(clazzArr.length == 2){
			className = clazzArr[1];
			index = Integer.parseInt(clazzArr[0]);
		}else{
			className = clazzArr[0];
		}
		MethodParamInfo mpi = new MethodParamInfo(className, methodName, index, regex);
		return mpi;
	}
	
	public static void main(String[] args) {
		String msg = "nihao111{1_A3a.bb}world{cc.dd}";
		String regex = "(\\$*\\{[0-9_]*[A-Za-z0-9]+\\.[A-Za-z0-9]+\\})";
		Pattern pattern = Pattern.compile(regex);
		Matcher m = pattern.matcher(msg);
		while(m.find()){
			String tmp = m.group();
			System.out.println(tmp);
		}
		System.out.println("okok...");
		
		String desc = "{User.name}";
		System.out.println(desc.matches(pattern.pattern()));
	}
	

}
