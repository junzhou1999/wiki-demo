package org.abc.wiki.service;

import org.abc.wiki.domain.Test;
import org.abc.wiki.mapper.TestMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TestService {

	@Resource
	private TestMapper testMapper;

	public List<Test> list() {
		return testMapper.findAll();
	}
}
