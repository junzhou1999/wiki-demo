package org.abc.wiki.controller;

import org.abc.wiki.Exception.BusinessException;
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

	/**
	 * 校验业务BusinessException统一处理
	 *
	 * @param e
	 * @return
	 */
	@ExceptionHandler(value = BusinessException.class)
	@ResponseBody
	public CommonResp validExceptionHandler(BusinessException e) {
		CommonResp commonResp = new CommonResp();
		LOG.warn("业务异常：{}", e.getCode().getDesc());
		commonResp.setSuccess(false);
		commonResp.setMessage(e.getCode().getDesc());
		return commonResp;   // 错误参数还是以CommonResp形式返回给前端
	}

	/**
	 * 统一异常处理
	 *
	 * @param e
	 * @return
	 */
	@ExceptionHandler(value = Exception.class)
	@ResponseBody
	public CommonResp validExceptionHandler(Exception e) {
		CommonResp commonResp = new CommonResp();
		LOG.error("系统异常：", e);
		commonResp.setSuccess(false);
		commonResp.setMessage("系统出现异常，请联系管理员");
		return commonResp;   // 错误参数还是以CommonResp形式返回给前端
	}
}
