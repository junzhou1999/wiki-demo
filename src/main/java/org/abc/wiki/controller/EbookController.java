package org.abc.wiki.controller;

import org.abc.wiki.domain.Ebook;
import org.abc.wiki.service.EbookService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/ebook")
public class EbookController {

	@Resource
	private EbookService ebookService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public List<Ebook> list() {
		return ebookService.list();
	}

}
