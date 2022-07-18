package org.abc.wiki.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Cross-Origin Resource Sharing
 * 跨域：一个资源去访问另一个不同域名或者同域名不同端口的资源
 * 比如我们本例中的vue使用的是8080端口而不是本机的80端口
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")  // 任何的映射请求地址
				.allowedOriginPatterns("*")   // 任何的来源地址，可以指定地址+端口
				.allowedHeaders(CorsConfiguration.ALL)
				.allowedMethods(CorsConfiguration.ALL)
				.allowCredentials(true)  // 允许前端带上它的凭证，比如cookie
				.maxAge(3600);  // 1小时内不用预检（option请求）
	}
}
