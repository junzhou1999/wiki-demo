package org.abc.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.abc.wiki.domain.Doc;
import org.abc.wiki.domain.DocExample;
import org.abc.wiki.mapper.DocMapper;
import org.abc.wiki.req.DocQueryReq;
import org.abc.wiki.req.DocSaveReq;
import org.abc.wiki.resp.DocQueryResp;
import org.abc.wiki.resp.PageResp;
import org.abc.wiki.util.CopyUtil;
import org.abc.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DocService {

	private static final Logger LOG =
			LoggerFactory.getLogger(DocService.class);

	@Resource
	private DocMapper docMapper;

	@Resource
	private SnowFlake snowFlake;

	public PageResp<DocQueryResp> list(DocQueryReq docQueryReq) {
		DocExample docExample = new DocExample();
		// where条件类
		DocExample.Criteria criteria = docExample.createCriteria();
		docExample.setOrderByClause("id");   // PostreSQL更新完后顺序会改变的

		// 第几页，每页几个数据项，分页和查询之间如果有其他的select语句，会使得分页效果失效
		if (docQueryReq.getPage() != 0 && docQueryReq.getSize() != 0) {
			PageHelper.startPage(docQueryReq.getPage(), docQueryReq.getSize());
		}
		List<Doc> docList = docMapper.selectByExample(docExample);
		PageInfo<Object> pageInfo = new PageInfo<>(docList);
		LOG.info("总行数：{}", pageInfo.getTotal());
		LOG.info("总页数：{}", pageInfo.getPages());

		PageResp<DocQueryResp> pageResp = new PageResp<>();
		List<DocQueryResp> respList = CopyUtil.copyList(docList, DocQueryResp.class);

		pageResp.setTotal(pageInfo.getTotal());
		pageResp.setList(respList);
		return pageResp;
	}

	// 一个性取出表内容
	public List<DocQueryResp> all() {
		DocExample docExample = new DocExample();
		docExample.setOrderByClause("sort asc");  // 以排序字段显示
		List<Doc> docList = docMapper.selectByExample(docExample);
		List<DocQueryResp> respList = CopyUtil.copyList(docList, DocQueryResp.class);
		return respList;
	}

	/**
	 * 更新和新增操作
	 */
	public void save(DocSaveReq req) {
		Doc doc = CopyUtil.copy(req, Doc.class);
		if (ObjectUtils.isEmpty(req.getId())) {
			// 新增
			doc.setId(snowFlake.nextId());
			docMapper.insertSelective(doc);
		} else {
			// 更新，有id主键的就是更新"where id=?"
			docMapper.updateByPrimaryKey(doc);
		}
	}

	/**
	 * 删除操作
	 */
	public void delete(Long id) {
		docMapper.deleteByPrimaryKey(id);
	}
}
