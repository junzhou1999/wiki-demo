package org.abc.wiki.controller;

import org.abc.wiki.domain.Test;
import org.abc.wiki.service.TestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController // (Controller返回的是页面)加ResponseBody
public class TestController {

	@Value("${test.user}")
	private String testValue;

	@Resource
	private TestService testService;

	@Resource
	private RedisTemplate redisTemplate;

	private static final Logger LOG =
			LoggerFactory.getLogger(TestController.class);

	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	public String sayHello() {
		return "Hello World." + testValue;
	}

	@RequestMapping(value = "/hello/post/{user}", method = RequestMethod.POST)

	public String sayHelloByPost(@PathVariable String user) {
		return "Hello World" + " " + user;
	}

	@RequestMapping(value = "/hello/post", method = RequestMethod.POST)
	public String sayHelloByPost2(@RequestParam String user) {
		return "Hello World" + " " + user;
	}

	@GetMapping("/test/list")
	public List<Test> list() {
		return testService.list();
	}

	@PostMapping("/test/redis/set/{key}/{value}")
	public String set(@PathVariable String key, @PathVariable String value) {
		redisTemplate.opsForValue().set(key, value, 24 * 3600, TimeUnit.SECONDS);
		LOG.info("key: {}, value: {}", key, value);
		return "success";
	}

	@GetMapping("/test/redis/get/{key}")
	public Object get(@PathVariable String key) {
		Object obj = redisTemplate.opsForValue().get(key);
		LOG.info("key: {}, value: {}", key, obj);
		return obj;
	}

}
