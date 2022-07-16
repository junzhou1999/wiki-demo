package org.abc.wiki.service;

import org.abc.wiki.domain.Ebook;
import org.abc.wiki.mapper.EbookMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class EbookService {

	@Resource
	private EbookMapper ebookMapper;

	public List<Ebook> list() {
		return ebookMapper.selectByExample(null);
	}
}
