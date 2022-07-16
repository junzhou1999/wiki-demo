package org.abc.wiki.service;

import org.abc.wiki.domain.Ebook;
import org.abc.wiki.domain.EbookExample;
import org.abc.wiki.mapper.EbookMapper;
import org.abc.wiki.req.EbookReq;
import org.abc.wiki.resp.EbookResp;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class EbookService {

	@Resource
	private EbookMapper ebookMapper;

	/*
	public List<Ebook> list(String name) {
		EbookExample ebookExample = new EbookExample();
		// where条件类
		EbookExample.Criteria criteria = ebookExample.createCriteria();

		criteria.andNameLike("%" + name + "%");
		return ebookMapper.selectByExample(ebookExample);
	}
	*/

	public List<EbookResp> list(EbookReq ebookReq) {
		EbookExample ebookExample = new EbookExample();
		// where条件类
		EbookExample.Criteria criteria = ebookExample.createCriteria();

		// 模糊查询条件
		criteria.andNameLike("%" + ebookReq.getName() + "%");

		ArrayList<EbookResp> respList = new ArrayList<>();
		// Ebook信息转换成EbookResp
		List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);
		for (Ebook ebook : ebookList) {
			EbookResp ebookResp = new EbookResp();
			// 使用Spring的工具可以把属性直接copy到另一个实体类
			BeanUtils.copyProperties(ebook, ebookResp);
			respList.add(ebookResp);
		}

		return respList;
	}
}
