package org.abc.wiki.controller;

import org.abc.wiki.req.DocQueryReq;
import org.abc.wiki.req.DocSaveReq;
import org.abc.wiki.resp.CommonResp;
import org.abc.wiki.resp.DocQueryResp;
import org.abc.wiki.resp.PageResp;
import org.abc.wiki.service.DocService;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/doc")
public class DocController {

	@Resource
	private DocService docService;

	/**
	 * @param docQueryReq 只要类里边的名字跟前端传进来的参数匹配，Spring会自动映射
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public CommonResp list(@Valid DocQueryReq docQueryReq) {
		PageResp<DocQueryResp> list = docService.list(docQueryReq);
		CommonResp<PageResp<DocQueryResp>> resp = new CommonResp<>();
		resp.setContent(list);
		return resp;
	}

	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public CommonResp all() {
		List<DocQueryResp> list = docService.all();
		CommonResp<List<DocQueryResp>> resp = new CommonResp<>();
		resp.setContent(list);
		return resp;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public CommonResp save(@RequestBody @Valid DocSaveReq docSaveReq) {
		docService.save(docSaveReq);
		CommonResp resp = new CommonResp();
		return resp;
	}

	@RequestMapping(value = "/delete/{strIds}", method = RequestMethod.DELETE)
	public CommonResp delete(@PathVariable String strIds) {
		CommonResp resp = new CommonResp();
		if (ObjectUtils.isEmpty(strIds)) {
			resp.setSuccess(false);
			resp.setMessage("删除编号不能为空！");
			return resp;
		}
		List<String> list = Arrays.asList(strIds.split(","));
		docService.delete(list);
		return resp;
	}
}