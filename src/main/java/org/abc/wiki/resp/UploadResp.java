package org.abc.wiki.resp;

public class UploadResp {
	private String url;   // 图片 src ，必须

	private String alt;   // 图片描述文字，非必须

	private String href;  // 图片的链接，非必须

	private String fileName;

	private String fileType;

	public String getUrl() {
		return url;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
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
		return "ImgUploadResp{" +
				"url='" + url + '\'' +
				", alt='" + alt + '\'' +
				", href='" + href + '\'' +
				", fileName='" + fileName + '\'' +
				", fileType='" + fileType + '\'' +
				'}';
	}
}
