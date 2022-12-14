package org.abc.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.abc.wiki.Exception.BusinessException;
import org.abc.wiki.Exception.BusinessExceptionCode;
import org.abc.wiki.domain.Content;
import org.abc.wiki.domain.Doc;
import org.abc.wiki.domain.DocExample;
import org.abc.wiki.mapper.ContentMapper;
import org.abc.wiki.mapper.DocMapper;
import org.abc.wiki.mapper.DocMapperCust;
import org.abc.wiki.req.DocQueryReq;
import org.abc.wiki.req.DocSaveReq;
import org.abc.wiki.resp.DocQueryResp;
import org.abc.wiki.resp.PageResp;
import org.abc.wiki.util.CopyUtil;
import org.abc.wiki.util.RedisUtil;
import org.abc.wiki.util.RequestContext;
import org.abc.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

@Service
public class DocService {

	private static final Logger LOG =
			LoggerFactory.getLogger(DocService.class);

	@Resource
	private DocMapper docMapper;

	@Resource
	private DocMapperCust docMapperCust;

	@Resource
	private ContentMapper contentMapper;

	@Resource
	private SnowFlake snowFlake;

	@Resource
	private RedisUtil redisUtil;

	@Resource
	private WsService wsService;

	private final String IMG_PATH =
			System.getProperty("user.home") + File.separator + "upload-files" + File.separator + "images";

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

	// 按电子书编号取文档内容
	public List<DocQueryResp> findByEbookId(Long ebookId) {
		DocExample docExample = new DocExample();
		docExample.setOrderByClause("sort asc");  // 以排序字段显示

		// 查询条件
		DocExample.Criteria criteria = docExample.createCriteria();
		criteria.andEbookIdEqualTo(ebookId);

		List<Doc> docList = docMapper.selectByExample(docExample);
		List<DocQueryResp> respList = CopyUtil.copyList(docList, DocQueryResp.class);
		return respList;
	}

	/**
	 * 更新和新增操作
	 */
	@Transactional
	public void save(DocSaveReq req) {
		Doc doc = CopyUtil.copy(req, Doc.class);
		Content content = CopyUtil.copy(req, Content.class);  // 复制id和content文档内容
		if (ObjectUtils.isEmpty(req.getId())) {
			// 新增
			doc.setId(snowFlake.nextId());
			docMapper.insertSelective(doc);

			// 当这里出现异常时，事务会终止
			// int a = 10/0;

			content.setId(doc.getId());  // 两张表共用一个id
			contentMapper.insert(content);
		} else {
			// 更新，有id主键的就是更新"where id=?"
			docMapper.updateByPrimaryKey(doc);
			// 如果更新不了，代表文档表还没有内容，那就新建一个
			if (contentMapper.updateByPrimaryKey(content) == 0) {
				// 新增文档内容
				contentMapper.insert(content);
			}
		}
	}

	/**
	 * 删除操作
	 */
	public void delete(Long id) {
		docMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 删除多个操作
	 */
	public void delete(List<String> ids) {
		DocExample docExample = new DocExample();
		DocExample.Criteria criteria = docExample.createCriteria();
		criteria.andIdIn(ids);
		docMapper.deleteByExample(docExample);
	}

	public String findContent(Long id) {
		Content content = contentMapper.selectByPrimaryKey(id);
		// 查看文档，阅读数加1
		docMapperCust.increaseViewCount(id);
		if (ObjectUtils.isEmpty(content)) {  // 判空是判空整个对象，不是判空对象的某个属性
			return "";
		}
		return content.getContent();
	}

	/**
	 * 点赞功能
	 * 远程IP加DOC_ID作为点赞成功的key，24小时内同一篇文章只能点赞一遍
	 *
	 * @param id
	 */
	public void vote(Long id) {
		String remoteAddr = RequestContext.getRemoteAddr();
		if (redisUtil.validateRepeat("DOC_VOTE_" + remoteAddr + "_" + id, 24 * 3600)) {
			throw new BusinessException(BusinessExceptionCode.VOTE_REPEAT);
		} else {
			docMapperCust.increaseVoteCount(id);
		}

		// 推送消息
		Doc docDB = docMapper.selectByPrimaryKey(id);
		String logId = MDC.get("LOG_ID");
		wsService.sendInfo("【" + docDB.getName() + "】被点赞啦！", logId);
	}
}
