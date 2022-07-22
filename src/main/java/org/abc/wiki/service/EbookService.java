package org.abc.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.abc.wiki.domain.Ebook;
import org.abc.wiki.domain.EbookExample;
import org.abc.wiki.mapper.EbookMapper;
import org.abc.wiki.req.EbookReq;
import org.abc.wiki.resp.EbookResp;
import org.abc.wiki.resp.PageResp;
import org.abc.wiki.util.CopyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class EbookService {

	private static final Logger LOG =
			LoggerFactory.getLogger(EbookService.class);

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

	public PageResp<EbookResp> list(EbookReq ebookReq) {
		EbookExample ebookExample = new EbookExample();
		// where条件类
		EbookExample.Criteria criteria = ebookExample.createCriteria();

		// 模糊查询条件，动态SQL语句
		if (!ObjectUtils.isEmpty(ebookReq.getName())) {
			criteria.andNameLike("%" + ebookReq.getName() + "%");
		}

		// 第几页，每页几个数据项，分页和查询之间如果有其他的select语句，会使得分页效果失效
		PageHelper.startPage(ebookReq.getPage(), ebookReq.getSize());
		List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);
		PageInfo<Object> pageInfo = new PageInfo<>(ebookList);
		LOG.info("总行数：{}", pageInfo.getTotal());
		LOG.info("总页数：{}", pageInfo.getPages());

/*弃用
		ArrayList<EbookResp> respList = new ArrayList<>();
		// Ebook信息转换成EbookResp
		for (Ebook ebook : ebookList) {
			EbookResp ebookResp = new EbookResp();
			// 使用Spring的工具可以把属性直接copy到另一个实体类
			BeanUtils.copyProperties(ebook, ebookResp);
			respList.add(ebookResp);
		}
*/

		PageResp<EbookResp> pageResp = new PageResp<>();
		List<EbookResp> respList = CopyUtil.copyList(ebookList, EbookResp.class);

		pageResp.setTotal(pageInfo.getTotal());
		pageResp.setList(respList);
		return pageResp;
	}
}
