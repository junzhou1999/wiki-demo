package org.abc.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.abc.wiki.domain.Category;
import org.abc.wiki.domain.CategoryExample;
import org.abc.wiki.mapper.CategoryMapper;
import org.abc.wiki.req.CategoryQueryReq;
import org.abc.wiki.req.CategorySaveReq;
import org.abc.wiki.resp.CategoryQueryResp;
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
public class CategoryService {

	private static final Logger LOG =
			LoggerFactory.getLogger(CategoryService.class);

	@Resource
	private CategoryMapper categoryMapper;

	@Resource
	private SnowFlake snowFlake;

	public PageResp<CategoryQueryResp> list(CategoryQueryReq categoryQueryReq) {
		CategoryExample categoryExample = new CategoryExample();
		// where条件类
		CategoryExample.Criteria criteria = categoryExample.createCriteria();
		categoryExample.setOrderByClause("id");   // PostreSQL更新完后顺序会改变的

		// 第几页，每页几个数据项，分页和查询之间如果有其他的select语句，会使得分页效果失效
		if (categoryQueryReq.getPage() != 0 && categoryQueryReq.getSize() != 0) {
			PageHelper.startPage(categoryQueryReq.getPage(), categoryQueryReq.getSize());
		}
		List<Category> categoryList = categoryMapper.selectByExample(categoryExample);
		PageInfo<Object> pageInfo = new PageInfo<>(categoryList);
		LOG.info("总行数：{}", pageInfo.getTotal());
		LOG.info("总页数：{}", pageInfo.getPages());

		PageResp<CategoryQueryResp> pageResp = new PageResp<>();
		List<CategoryQueryResp> respList = CopyUtil.copyList(categoryList, CategoryQueryResp.class);

		pageResp.setTotal(pageInfo.getTotal());
		pageResp.setList(respList);
		return pageResp;
	}

	/**
	 * 更新和新增操作
	 */
	public void save(CategorySaveReq req) {
		Category category = CopyUtil.copy(req, Category.class);
		if (ObjectUtils.isEmpty(req.getId())) {
			// 新增
			category.setId(snowFlake.nextId());
			categoryMapper.insertSelective(category);
		} else {
			// 更新，有id主键的就是更新"where id=?"
			categoryMapper.updateByPrimaryKey(category);
		}
	}

	/**
	 * 删除操作
	 */
	public void delete(Long id) {
		categoryMapper.deleteByPrimaryKey(id);
	}
}
