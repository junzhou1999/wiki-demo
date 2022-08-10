package org.abc.wiki.controller;

import org.abc.wiki.req.UserLoginReq;
import org.abc.wiki.req.UserQueryReq;
import org.abc.wiki.req.UserResetPwdReq;
import org.abc.wiki.req.UserSaveReq;
import org.abc.wiki.resp.CommonResp;
import org.abc.wiki.resp.UserLoginResp;
import org.abc.wiki.resp.UserQueryResp;
import org.abc.wiki.resp.PageResp;
import org.abc.wiki.service.UserService;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

	@Resource
	private UserService userService;

	/**
	 * @param req 只要类里边的名字跟前端传进来的参数匹配，Spring会自动映射
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public CommonResp list(@Valid UserQueryReq req) {
		PageResp<UserQueryResp> list = userService.list(req);
		CommonResp<PageResp<UserQueryResp>> resp = new CommonResp<>();
		resp.setContent(list);
		return resp;
	}

	// 新增或编辑用户信息
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public CommonResp save(@RequestBody @Valid UserSaveReq req) {
		// 对传进来的数据加密
		req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
		userService.save(req);
		CommonResp resp = new CommonResp();
		return resp;
	}

	// 删除用户
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public CommonResp delete(@PathVariable Long id) {
		userService.delete(id);
		CommonResp resp = new CommonResp();
		return resp;
	}

	// 密码重置
	@RequestMapping(value = "/reset-password", method = RequestMethod.POST)
	public CommonResp resetPwd(@RequestBody @Valid UserResetPwdReq req) {
		// 对传进来的数据加密
		req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
		userService.resetPwd(req);
		CommonResp resp = new CommonResp();
		return resp;
	}

	// 用户登录
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public CommonResp login(@RequestBody @Valid UserLoginReq req) {
		// 对传进来的数据加密
		req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
		UserLoginResp userLoginResp = userService.login(req);
		CommonResp<UserLoginResp> resp = new CommonResp<>();
		resp.setContent(userLoginResp);
		return resp;
	}
}
