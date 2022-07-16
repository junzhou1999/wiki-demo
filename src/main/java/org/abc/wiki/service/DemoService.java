package org.abc.wiki.service;

import org.abc.wiki.domain.Demo;
import org.abc.wiki.mapper.DemoMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DemoService {

	@Resource
	private DemoMapper demoMapper;

	public List<Demo> list() {
		return demoMapper.selectByExample(null);
	}

	public Demo findById(Long id) {
		return demoMapper.selectByPrimaryKey(id);
	}

	public int insert(Demo demo) {
		return demoMapper.insertSelective(demo);
	}
}
