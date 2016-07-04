package com.sirding.model;
/**
 * 方法参数信息
 * @author zc.ding
 * @date 2016年7月2日
 *
 */
public class MethodParamInfo {

	private Object obj;
	private String className;
	private String methodName;
	private int index;
	private String regex;
	
	public MethodParamInfo(){}
	
	public MethodParamInfo(String className, String methodName, int index, String regex){
		this.className = className;
		this.methodName = methodName;
		this.index = index;
		this.regex = regex;
	}
	
	public MethodParamInfo(int index, String className, Object obj){
		this.index = index;
		this.className = className;
		this.obj = obj;
	}
	
	public Object getObj() {
		return obj;
	}
	public void setObj(Object obj) {
		this.obj = obj;
	}

	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getRegex() {
		return regex;
	}
	public void setRegex(String regex) {
		this.regex = regex;
	}

	/**
	 * 以索引+类型作为key<br/>
	 * eg : 1_String
	 * @return
	 * @author zc.ding
	 * @date 2016年7月2日
	 */
	public String getKey(){
		String suffix = "";
		if(this.methodName != null && this.methodName.length() > 0){
			suffix = this.methodName;
		}
		if(index == -1){
			return this.className + "_" + suffix;
		}
		String key = this.index + "_" + this.className + "_" + suffix;
		if(key.endsWith("_")){
			return key.substring(0, key.length() - 1);
		}
		return this.index + "_" + this.className + "_" + suffix;
	}
	
	/**
	 * 
	 * @return
	 * @author zc.ding
	 * @date 2016年7月2日
	 */
	public String getAopMpiKey(){
		if(index == -1){
			return this.className;
		}
		return this.index + "_" + this.className;
	}
}
