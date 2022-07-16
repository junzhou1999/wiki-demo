package org.abc.wiki.controller;

import org.abc.wiki.domain.Demo;
import org.abc.wiki.service.DemoService;
import org.abc.wiki.util.DateUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/demo")
public class DemoController {

	@Resource
	private DemoService demoService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public List<Demo> list() {
		return demoService.list();
	}

	@RequestMapping(value = "/find/{id}", method = RequestMethod.POST)
	public Demo findById(@PathVariable Long id) {
		return demoService.findById(id);
	}

	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public int insert(@RequestBody Map<String, Object> map) {
		Long id = null;
		if (map.get("id") != null) {
			id = Long.valueOf(map.get("id").toString());
		}
		String name = (String) map.get("name");
		Integer age = (Integer) map.get("age");
		String sex = (String) map.get("sex");
		BigDecimal height = new BigDecimal(map.get("height").toString());
		Date birthday = DateUtil.str2Date(map.get("birthday").toString());
		Demo demo = new Demo(id, name, age, sex, height, birthday);
		return demoService.insert(demo);
	}
}
