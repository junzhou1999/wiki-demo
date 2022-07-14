package org.abc.wiki.controller;

import org.springframework.web.bind.annotation.*;

@RestController // (Controller返回的是页面)加ResponseBody
public class TestController {

	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	public String sayHello() {
		return "Hello World.";
	}

	@RequestMapping(value = "/hello/post/{user}", method = RequestMethod.POST)

	public String sayHelloByPost(@PathVariable String user) {
		return "Hello World" + " " + user;
	}

	@RequestMapping(value = "/hello/post", method = RequestMethod.POST)
	public String sayHelloByPost2(@RequestParam String user) {
		return "Hello World" + " " + user;
	}
}
