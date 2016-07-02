package com.sirding;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.junit.*;

import com.sirding.annotation.Action;
import com.sirding.parse.AnnotationParse;
public class TestAopLogs {

	static AnnotationParse annotationParse;
	static Logger logger = Logger.getLogger(TestAopLogs.class);
	@Before
	public void before(){
		annotationParse = new AnnotationParse();
	}
	@Test
	public void test1(){
		Object[] args = new Object[]{"zc.ding", "jtsec"};
		this.callMethod("test1", args);
		logger.debug("okok......测试test1");
	}
	
	@Test
	public void test2(){
		User user = new User("zc.ding", "jtsec");
		user.setOldUser(new User("old_zc.ding", "old_pwd"));
		Object[] args = new Object[]{user};
		this.callMethod("test2", args);
		logger.debug("测试test2...okok...");
	}
	
	@Test
	public void test3(){
		Object[] args = new Object[]{"sirding", "jtsec"};
		this.callMethod("test3", args);
		logger.debug("测试test3...okok...");
	}
	
	@Test
	public void test4(){
		Object[] args = new Object[]{new User("zc.ding", "jtsec")};
		this.callMethod("test4", args);
		logger.debug("测试test4...okok...");
	}
	
	@Test
	public void demo(){
		User user = new User();
		Class<?> clazz = user.getClass();
		Field[] fields = clazz.getDeclaredFields();
		if(fields != null){
			for(Field field : fields){
				logger.debug(field.getName());
			}
		}
	}
	
	@Test
	public void demo1(){
		String tmp = "abcd$User.pwd";
		String msg = "$User.pwd";
		String regex = msg.replaceAll("\\$", "\\\\\\$");
		logger.debug(regex);
		logger.debug(tmp.replaceAll(regex, "aa"));
		
		String a = "aa{bb}";
		logger.debug(a.replaceAll("\\{", ""));
	}
	
	public void callMethod(String methodName, Object[] args){
		OperateLog operateLog = new OperateLog();
		Method method = this.getMethod(operateLog, methodName);
		Action action = method.getAnnotation(Action.class);
		String msg = "";
		if(action != null){
			msg = annotationParse.getLogMsg(action, args);
		}
		logger.debug(msg);
		logger.debug("okok......");
	}
	
	private Method getMethod(OperateLog obj, String methodName){
		Method[] methods = obj.getClass().getDeclaredMethods();
		if(methods != null){
			for(Method method : methods){
				if(methodName.equalsIgnoreCase(method.getName())){
					return method;
				}
			}
		}
		return null;
	}
}
