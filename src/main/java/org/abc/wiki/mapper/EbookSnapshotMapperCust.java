package org.abc.wiki.mapper;

import org.abc.wiki.resp.StatisticResp;

import java.util.List;

public interface EbookSnapshotMapperCust {
	public void genSnapshot();

	public List<StatisticResp> getStatistic();
}