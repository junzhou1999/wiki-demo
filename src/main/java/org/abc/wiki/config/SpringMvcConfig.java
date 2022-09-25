package org.abc.wiki.config;

import org.abc.wiki.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {

	@Resource
	private LoginInterceptor loginInterceptor;

	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(loginInterceptor)
				.addPathPatterns("/**")       // 拦截所有接口
				.excludePathPatterns(         // 不拦截的接口
						"/test/**",               // 测试接口不拦截
						"/user/login",            // 登录接口不拦截
						"/category/all",          // 分类接口不拦截
						"/ebook/list",            // 查看电子书不拦截，其他增删改拦截
						"/doc/find/**",           // 文档信息
						"/doc/find-content/**",   // 文档内容
						"/doc/vote/**",           // 点赞功能
						"/ebook-snapshot/**"      // 数据展示
				);
	}
}
