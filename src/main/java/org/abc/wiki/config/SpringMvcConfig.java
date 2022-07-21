//package org.abc.wiki.config;
//
//import org.abc.wiki.interceptor.LogInterceptor;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import javax.annotation.Resource;
//
//@Configuration
//public class SpringMvcConfig implements WebMvcConfigurer {
//
//	@Resource
//	private LogInterceptor logInterceptor;
//
//	public void addInterceptors(InterceptorRegistry registry) {
//		registry.addInterceptor(logInterceptor)
//				.addPathPatterns("/**")  // 拦截所有接口
//				.excludePathPatterns("/login");  // 不拦截登录也页面
//	}
//}
