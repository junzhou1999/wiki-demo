package org.abc.wiki.controller;

import org.abc.wiki.resp.CommonResp;
import org.abc.wiki.service.DocService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/doc")
public class ContentController {

	@Resource
	private DocService docService;  // 用回原来的service

	@RequestMapping(value = "/find-content/{id}", method = RequestMethod.GET)
	public CommonResp findContent(@PathVariable Long id) {
		CommonResp<String> resp = new CommonResp<>();
		resp.setContent(docService.findContent(id));
		return resp;
	}
}
