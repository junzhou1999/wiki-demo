package org.abc.wiki.controller;

import org.abc.wiki.resp.CommonResp;
import org.abc.wiki.resp.StatisticResp;
import org.abc.wiki.service.EbookSnapshotService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/ebook-snapshot")
public class EbookSnapshotController {

	@Resource
	private EbookSnapshotService ebookSnapshotService;

	@RequestMapping(value = "/get-statistic", method = RequestMethod.GET)
	public CommonResp getStatistic() {
		List<StatisticResp> list = ebookSnapshotService.getStatistic();
		CommonResp<List<StatisticResp>> resp = new CommonResp<>();
		resp.setContent(list);
		return resp;
	}
}
