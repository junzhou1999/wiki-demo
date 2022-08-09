package org.abc.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.abc.wiki.Exception.BusinessException;
import org.abc.wiki.Exception.BusinessExceptionCode;
import org.abc.wiki.domain.User;
import org.abc.wiki.domain.UserExample;
import org.abc.wiki.mapper.UserMapper;
import org.abc.wiki.req.UserQueryReq;
import org.abc.wiki.req.UserSaveReq;
import org.abc.wiki.resp.UserQueryResp;
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
public class UserService {

	private static final Logger LOG =
			LoggerFactory.getLogger(UserService.class);

	@Resource
	private UserMapper userMapper;

	@Resource
	private SnowFlake snowFlake;

	public PageResp<UserQueryResp> list(UserQueryReq req) {
		UserExample userExample = new UserExample();
		// where条件类
		UserExample.Criteria criteria = userExample.createCriteria();
		userExample.setOrderByClause("id");   // PostreSQL更新完后顺序会改变的
		// 如果不为空，用户名要完全匹配，如果为空，查全部的用户
		if (!ObjectUtils.isEmpty(req.getLoginName())) {
			criteria.andLoginNameEqualTo(req.getLoginName());
		}
		// 第几页，每页几个数据项，分页和查询之间如果有其他的select语句，会使得分页效果失效
		if (req.getPage() != 0 && req.getSize() != 0) {
			PageHelper.startPage(req.getPage(), req.getSize());
		}
		List<User> userList = userMapper.selectByExample(userExample);
		PageInfo<Object> pageInfo = new PageInfo<>(userList);
		LOG.info("总行数：{}", pageInfo.getTotal());
		LOG.info("总页数：{}", pageInfo.getPages());

		PageResp<UserQueryResp> pageResp = new PageResp<>();
		List<UserQueryResp> respList = CopyUtil.copyList(userList, UserQueryResp.class);

		pageResp.setTotal(pageInfo.getTotal());
		pageResp.setList(respList);
		return pageResp;
	}

	/**
	 * 更新和新增操作
	 */
	public void save(UserSaveReq req) {
		User user = CopyUtil.copy(req, User.class);
		if (ObjectUtils.isEmpty(req.getId())) {
			User userDB = findByLoginName(req.getLoginName());
			if (ObjectUtils.isEmpty(userDB)) {
				// 新增
				user.setId(snowFlake.nextId());
				userMapper.insert(user);
			} else {
				// 用户名已存在，抛出自定义异常
				throw new BusinessException(BusinessExceptionCode.USER_LOGIN_NAME_EXIST);
			}
		} else {
			// 后端更新
			user.setLoginName(null);  // 无论什么原因，登录名都不能修改
			userMapper.updateByPrimaryKeySelective(user);
		}
	}

	/**
	 * 删除操作
	 */
	public void delete(Long id) {
		userMapper.deleteByPrimaryKey(id);
	}

	public User findByLoginName(String loginName) {
		UserExample userExample = new UserExample();
		// where条件类
		UserExample.Criteria criteria = userExample.createCriteria();
		criteria.andLoginNameEqualTo(loginName);
		List<User> list = userMapper.selectByExample(userExample);

		if (ObjectUtils.isEmpty(list))
			return null;
		return list.get(0);
	}
}
