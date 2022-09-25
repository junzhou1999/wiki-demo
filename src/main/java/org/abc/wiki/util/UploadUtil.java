package org.abc.wiki.util;

import org.abc.wiki.Exception.BusinessException;
import org.abc.wiki.Exception.BusinessExceptionCode;
import org.abc.wiki.resp.UploadResp;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Component
public class UploadUtil {

	private static SnowFlake snowFlake;

	private static HttpServletRequest request;

	@Resource
	public void setSnowFlake(SnowFlake snowFlake) {
		UploadUtil.snowFlake = snowFlake;
	}

	@Resource
	public void setRequest(HttpServletRequest request) {
		UploadUtil.request = request;
	}

	private static final String UPLOAD_PATH =
			System.getProperty("user.home") + File.separator + "upload-files";

	/**
	 * @param file
	 * @param dirName 上传的分类目录
	 * @return
	 */
	public static UploadResp upload(MultipartFile file, String dirName) {
		// 获取后缀
		String fileType = file.getContentType();
		String suffix = fileType.substring(fileType.indexOf("/") + 1);
		// 生成文件名
		Long prefix = snowFlake.nextId();
		String fileName = prefix + "." + suffix;
		// 生成绝对目录文件
		File dest = new File(UPLOAD_PATH + File.separator + dirName + File.separator + fileName);
		if (!dest.getParentFile().exists()) {
			dest.getParentFile().mkdirs();
		}
		try {
			Files.copy(file.getInputStream(), dest.toPath());
		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessException(BusinessExceptionCode.UPLOAD_FILES_FAIL);
		}
		UploadResp resp = new UploadResp();
		String url = "http://" + request.getServerName() + "/upload-files/" + dirName + "/" + fileName;
		resp.setUrl(url);
		resp.setAlt(fileName);
		resp.setHref(url);
		resp.setFileName(fileName);
		resp.setFileType(fileType);
		return resp;
	}
}
