package org.abc.wiki.controller;

import org.abc.wiki.req.EbookQueryReq;
import org.abc.wiki.req.EbookSaveReq;
import org.abc.wiki.resp.CommonResp;
import org.abc.wiki.resp.EbookQueryResp;
import org.abc.wiki.resp.PageResp;
import org.abc.wiki.service.EbookService;
import org.springframework.web.bind.annotation.*;

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
	 * @param ebookQueryReq 只要类里边的名字跟前端传进来的参数匹配，Spring会自动映射
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public CommonResp list(EbookQueryReq ebookQueryReq) {
		PageResp<EbookQueryResp> list = ebookService.list(ebookQueryReq);
		CommonResp<PageResp<EbookQueryResp>> resp = new CommonResp<>();
		resp.setContent(list);
		return resp;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public CommonResp save(@RequestBody EbookSaveReq ebookSaveReq) {
		ebookService.save(ebookSaveReq);
		CommonResp resp = new CommonResp();
		return resp;
	}
}
