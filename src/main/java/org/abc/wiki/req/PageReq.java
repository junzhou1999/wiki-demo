package org.abc.wiki.req;

// 请求的查询分页
public class PageReq {

	// 第几页
	private int page;

	// 一页几行
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