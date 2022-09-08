package org.abc.wiki.service;

import org.abc.wiki.mapper.EbookSnapshotMapperCust;
import org.abc.wiki.resp.StatisticResp;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class EbookSnapshotService {

	@Resource
	private EbookSnapshotMapperCust ebookSnapshotMapperCust;

	public void genSnapshot() {
		ebookSnapshotMapperCust.genSnapshot();
	}

	public List<StatisticResp> getStatistic() {
		return ebookSnapshotMapperCust.getStatistic();
	}
}