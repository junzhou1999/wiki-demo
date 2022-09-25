package org.abc.wiki.controller;

import org.abc.wiki.req.EbookQueryReq;
import org.abc.wiki.req.EbookSaveReq;
import org.abc.wiki.resp.CommonResp;
import org.abc.wiki.resp.EbookQueryResp;
import org.abc.wiki.resp.UploadResp;
import org.abc.wiki.resp.PageResp;
import org.abc.wiki.service.EbookService;
import org.abc.wiki.util.UploadUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;

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
	public CommonResp list(@Valid EbookQueryReq req) {
		PageResp<EbookQueryResp> list = ebookService.list(req);
		CommonResp<PageResp<EbookQueryResp>> resp = new CommonResp<>();
		resp.setContent(list);
		return resp;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public CommonResp save(@RequestBody @Valid EbookSaveReq ebookSaveReq) {
		ebookService.save(ebookSaveReq);
		CommonResp resp = new CommonResp();
		return resp;
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public CommonResp delete(@PathVariable Long id) {
		ebookService.delete(id);
		CommonResp resp = new CommonResp();
		return resp;
	}

	@RequestMapping(value = "/upload-cover", method = RequestMethod.POST)
	public CommonResp uploadImg(@RequestPart(value = "coverImg") MultipartFile file) {
		UploadResp upload = UploadUtil.upload(file, "cover");  // 上传的分类目录
		CommonResp<UploadResp> resp = new CommonResp<>();
		resp.setContent(upload);
		return resp;
	}
}
