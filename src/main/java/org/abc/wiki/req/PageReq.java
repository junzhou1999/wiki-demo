package org.abc.wiki.req;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

// 请求的查询分页
public class PageReq {

	// 第几页
	@NotNull(message = "【页码】不能为空")
	private int page;

	// 一页几条
	@NotNull(message = "【每页条数】不能为空")
	@Max(value = 999, message = "【每页条数】不能超过999")
	private int size;

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer("PageReq{");
		sb.append("page=").append(page);
		sb.append(", size=").append(size);
		sb.append('}');
		return sb.toString();
	}
}