package org.abc.wiki.controller;

import org.abc.wiki.req.EbookReq;
import org.abc.wiki.resp.CommonResp;
import org.abc.wiki.resp.EbookResp;
import org.abc.wiki.resp.PageResp;
import org.abc.wiki.service.EbookService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/ebook")
public class EbookController {

	@Resource
	private EbookService ebookService;

/*
	// 前端可以直接传递name参数进来
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public CommonResp<List<Ebook>> list(String name) {
		List<Ebook> list = ebookService.list(name);
		CommonResp<List<Ebook>> resp = new CommonResp<>();
		resp.setContent(list);
		return resp;
	}
*/

	/**
	 * @param ebookReq 只要类里边的名字跟前端传进来的参数匹配，Spring会自动映射
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public CommonResp list(EbookReq ebookReq) {
		PageResp<EbookResp> list = ebookService.list(ebookReq);
		CommonResp<PageResp<EbookResp>> resp = new CommonResp<>();
		resp.setContent(list);
		return resp;
	}
}
