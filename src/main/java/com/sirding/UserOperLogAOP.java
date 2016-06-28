package com.sirding;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.sirding.annotation.Action;
import com.sirding.annotation.AssertParam;

/**
 * 用户操作日志的切面，配置说明，需要在spring-mvc.xml、spring.xml中添加
 * <aop:aspectj-autoproxy proxy-target-class="true" />配置，其次通过<bean>节点注入切面，如果在spring.xml中注入切面
 * 那么可以切controller和service。例如当前配置切入点为@Pointcut("@annotation(com.surpass.system.aop.Action)")那么在controller
 * 或是service中含有@Action注解的方法都会被切面拦截。
 * 如果切面是在spring-mvc.xml中注入的，那么切面智能拦截controller中含有@Action注解的方法
 * @author zc.ding
 * @time 2016-06-28
 *
 */
@Aspect
public class UserOperLogAOP {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(UserOperLogAOP.class);
	
	public UserOperLogAOP(){}

	@Pointcut("@annotation(com.surpass.system.aop.Action)")
	//	@Pointcut("(execution(* com.surpass.system.filters.JspFilter.*(..)))")
	public void pointCut(){}


	//	@Before("pointCut()")
	//	@Before("(execution(* com.surpass.system.interceptors.LogInterceptor.postHandle(..)))")
	public void before(JoinPoint point){
		logger.debug("执行Before===" + point.getTarget() + "==" + point.getSignature().getName());
		if("preHandle".equals(point.getSignature().getName())){
		}
	}

	//	@AfterReturning("pointCut()")
	//	@AfterReturning("(execution(* com.surpass.business.usermanager.service.impl.UserServiceImpl.*(..)))")
	public void afterReturning(){
		logger.debug("执行AfterReturning方法...");
	}

	@Around("pointCut()")
	public Object around(ProceedingJoinPoint joinPoint){
		Object value = null;
		boolean isWeave = false;
		Action action = this.getAction(joinPoint);
		Object[] args = joinPoint.getArgs();
//		HttpServletRequest request = this.getRequest(args);
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		
		String userName = this.getUserName(request);
		String fromIp = "";
		try {
			if(action != null && args != null && args.length > 0 && request != null && userName != null){
				fromIp = request.getRemoteAddr();
				isWeave = true;
			}
			if(isWeave){
//				this.saveLog(userName, action, args, fromIp, "开始");
			}
			value = joinPoint.proceed();
			if(isWeave){
				this.saveLog(userName, action, args, fromIp, "成功");
			}
		} catch (Throwable e) {
			e.printStackTrace();
			if(isWeave){
				this.saveLog(userName, action, args, fromIp, "失败");
			}
		}
		return value;
	}

	/**
	 * 执行日志记录的操作
	 * @param userInfo 用户信息
	 * @param action 用户操作的注解
	 * @param args 切面的参数列表
	 * @param fromIp 访问用户的地址
	 * @param sta 用户操作的状态
	 * 
	 */
	private String saveLog(String userName, Action action, Object[] args, String fromIp, String sta){
		/*
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
		msg = this.formatMsg(msg);
		if(msg == null || msg.length() <= 0){
			return "";
		}
		String info = userName + "," + fromIp + "," + msg + "," + sta;
		logger.debug(info);
		
		if(SystemCons.OS_IS_LINUX){
			SyslogUtil.log(info, "http", "utf8");
		}else{
			String sql = "INSERT INTO log_http_msg VALUES(id, '" + DateUtil.getNowTime() + "', 'http:', '"
					+ userName + "', '" + fromIp + "', '" + msg + "', '" + sta + "', '0')";
			List<String> list = new ArrayList<String>();
			list.add(sql);
			try {
				DbUtil.executeBetchSql(DbUtil.getConn(), list);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return msg;
		*/
		return null;
	}

	/**
	 * 获得管理员id
	 * @param request
	 * @return
	 */
	private String getUserName(HttpServletRequest request){
/*
		String userName = null;
		UserInfo ui = null;
		if(request != null){
			userName = request.getParameter("userName");
			//			String fromIp = request.getRemoteAddr();
			Object object = request.getSession().getAttribute(Constants.SESSION_USER);
			if(object instanceof UserInfo){
				ui = (UserInfo) object;
				if(ui != null && ui.getUsername() != null){
					userName = ui.getUsername();
					if(SystemCons.SUPER_USERNAME.equals(userName) && SystemCons.SUPER_PASSWORD.equals(ui.getPassword())){
						return null;
					}
					if(ui.getUserRole() != null && ui.getUserRole().getRoleId() > 0){
						userName += "[" + ui.getUserRole().getRoleName() + "]";
					}
				}
			}
		}
		return userName;
	*/
		return null;
	}

	/**
	 * 从参数中获得HttpServletRequest
	 * @param args
	 * @return
	 */
	public HttpServletRequest getRequest(Object[] args){
		if(args != null && args.length > 0){
			for(Object obj : args){
//				org.apache.catalina.connector.RequestFacade
				if(obj.getClass() == HttpServletRequest.class || "class org.apache.catalina.connector.RequestFacade".equals(obj.getClass().toString())){
					return (HttpServletRequest)obj;
				}
			}
		}
		return null;
	}

	/**
	 * 获得切入点的注解信息
	 * @param joinPoint
	 * @return
	 */
	protected Action getAction(JoinPoint joinPoint){
		String className = joinPoint.getTarget().getClass().getName();
		String methodName = joinPoint.getSignature().getName();
		try {
			Class<?> clazz = Class.forName(className);
			Method[] arr = clazz.getMethods();
			if(arr != null){
				for(Method m : arr){
					if(m.getName().equals(methodName)){
						Action as = m.getAnnotation(Action.class);
						if(as != null){
							return as;
						}
					}
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 格式化msg信息，去掉msg的中第一个“-”和最后一个“-”
	 * @param msg
	 * @return
	 */
	private String formatMsg(String msg){
		if(msg != null && msg.startsWith("-")){
			msg = msg.substring(1, msg.length());
		}
		if(msg != null && msg.endsWith("-")){
			msg = msg.substring(0, msg.length() - 1);
		}
		return msg;
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
}
