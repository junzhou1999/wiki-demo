package org.abc.wiki.job;

import org.abc.wiki.service.EbookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class EbookJob {
	private static final Logger LOG =
			LoggerFactory.getLogger(EbookJob.class);

	@Resource
	private EbookService ebookService;

	/**
	 * 每秒的44秒，隔一分钟更新一次电子书信息
	 * 注：这一分钟错过了，就重新算新的一分钟了，隔16秒（44+16）都已经算另一分钟了
	 */
	@Scheduled(cron = "44/60 * * * * ? ")
	public void cron() {
		LOG.info("开始更新电子书下的文档数据");
		long start = System.currentTimeMillis();
		ebookService.updateEbookInfo();
		LOG.info("结束更新电子书下的文档数据，耗时：{}", System.currentTimeMillis() - start);
	}

}
