package org.abc.wiki.controller;

import org.abc.wiki.req.CategoryQueryReq;
import org.abc.wiki.req.CategorySaveReq;
import org.abc.wiki.resp.CommonResp;
import org.abc.wiki.resp.CategoryQueryResp;
import org.abc.wiki.resp.PageResp;
import org.abc.wiki.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/category")
public class CategoryController {

	@Resource
	private CategoryService categoryService;

	/**
	 * @param categoryQueryReq 只要类里边的名字跟前端传进来的参数匹配，Spring会自动映射
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public CommonResp list(@Valid CategoryQueryReq categoryQueryReq) {
		PageResp<CategoryQueryResp> list = categoryService.list(categoryQueryReq);
		CommonResp<PageResp<CategoryQueryResp>> resp = new CommonResp<>();
		resp.setContent(list);
		return resp;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public CommonResp save(@RequestBody @Valid CategorySaveReq categorySaveReq) {
		categoryService.save(categorySaveReq);
		CommonResp resp = new CommonResp();
		return resp;
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public CommonResp delete(@PathVariable Long id) {
		categoryService.delete(id);
		CommonResp resp = new CommonResp();
		return resp;
	}
}
