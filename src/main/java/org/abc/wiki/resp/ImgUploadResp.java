package org.abc.wiki.resp;

public class ImgUploadResp {
	private String url;   // 图片 src ，必须

	private String alt;   // 图片描述文字，非必须

	private String href;  // 图片的链接，非必须

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAlt() {
		return alt;
	}

	public void setAlt(String alt) {
		this.alt = alt;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	@Override
	public String toString() {
		return "ImgUpload{" +
				"url='" + url + '\'' +
				", alt='" + alt + '\'' +
				", href='" + href + '\'' +
				'}';
	}
}
