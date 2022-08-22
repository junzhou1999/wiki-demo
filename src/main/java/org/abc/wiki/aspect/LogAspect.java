package org.abc.wiki.aspect;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.spring.PropertyPreFilters;
import org.abc.wiki.util.RequestContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

@Aspect  // AOP注解
@Component
public class LogAspect {

	private final static Logger LOG = LoggerFactory.getLogger(LogAspect.class);

	/**
	 * 定义一个切点Pointcut，针对controller所有的方法，所有的参数
	 */
	@Pointcut("execution(public * org.abc.*.controller..*Controller.*(..))")
	public void controllerPointcut() {
	}

	/**
	 * 前置通知，执行业务代码之前要做的事情
	 * @param joinPoint 连接点joinPoint直接拿到请求接口的参数
	 * @throws Throwable
	 */
	@Before("controllerPointcut()")
	public void doBefore(JoinPoint joinPoint) throws Throwable {

		// 开始打印请求日志
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		Signature signature = joinPoint.getSignature();
		String name = signature.getName();

		// 打印请求信息
		LOG.info("------------- 开始 -------------");
		LOG.info("请求地址: {} {}", request.getRequestURL().toString(), request.getMethod());
		LOG.info("类名方法: {}.{}", signature.getDeclaringTypeName(), name);
		LOG.info("远程地址: {}", request.getRemoteAddr());

		// 程序一开始就获取远程IP并放入本地线程变量
		RequestContext.setRemoteAddr(getRemoteIp(request));

		// 打印请求参数
		Object[] args = joinPoint.getArgs();
		// LOG.info("请求参数: {}", JSONObject.toJSONString(args));

		Object[] arguments = new Object[args.length];
		for (int i = 0; i < args.length; i++) {
			if (args[i] instanceof ServletRequest
					|| args[i] instanceof ServletResponse
					|| args[i] instanceof MultipartFile) {
				continue;
			}
			arguments[i] = args[i];
		}
		// 排除字段，敏感字段或太长的字段不显示
		String[] excludeProperties = {"password", "file"};
		PropertyPreFilters filters = new PropertyPreFilters();
		PropertyPreFilters.MySimplePropertyPreFilter excludefilter = filters.addFilter();
		excludefilter.addExcludes(excludeProperties);
		LOG.info("请求参数: {}", JSONObject.toJSONString(arguments, excludefilter));
	}

	/**
	 * 环绕通知，业务前面增加执行代码，后面增加一点执行代码
	 * @param proceedingJoinPoint
	 * @return
	 * @throws Throwable
	 */
	@Around("controllerPointcut()")
	public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		long startTime = System.currentTimeMillis();    // 前，等于@Before
		Object result = proceedingJoinPoint.proceed();  // 业务执行
		// 排除字段，敏感字段或太长的字段不显示            // 后
		String[] excludeProperties = {"password", "file"};
		PropertyPreFilters filters = new PropertyPreFilters();
		PropertyPreFilters.MySimplePropertyPreFilter excludefilter = filters.addFilter();
		excludefilter.addExcludes(excludeProperties);
		LOG.info("返回结果: {}", JSONObject.toJSONString(result, excludefilter));
		LOG.info("------------- 结束 耗时：{} ms -------------", System.currentTimeMillis() - startTime);
		return result;
	}

	/**
	 * 使用nginx做反向代理，需要用该方法才能取到真实的远程IP
	 *
	 * @param request
	 * @return
	 */
	public String getRemoteIp(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
// 后置通知	@After()
}
