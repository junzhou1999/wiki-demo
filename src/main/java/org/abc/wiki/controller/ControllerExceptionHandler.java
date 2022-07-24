package org.abc.wiki.controller;

import org.abc.wiki.resp.CommonResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice  // 统一异常处理、数据预处理等
public class ControllerExceptionHandler {

	private static final Logger LOG =
			LoggerFactory.getLogger(ControllerExceptionHandler.class);

	/**
	 * 校验异常BindException统一处理
	 * valid校验出现异常时用下面handler处理
	 *
	 * @param e
	 * @return
	 */
	@ExceptionHandler(value = BindException.class)
	@ResponseBody
	public CommonResp validExceptionHandler(BindException e) {
		CommonResp commonResp = new CommonResp();
		LOG.warn("参数校验失败：{}", e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
		commonResp.setSuccess(false);
		commonResp.setMessage(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
		return commonResp;   // 错误参数还是以CommonResp形式返回给前端
	}

	////可以写更多的异常处理
}