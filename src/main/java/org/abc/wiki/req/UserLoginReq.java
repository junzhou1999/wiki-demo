package org.abc.wiki.req;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class UserLoginReq {
	private Long id;

	@NotEmpty(message = "【用户名】不能为空")
	private String loginName;

	// 登录时不用给前端台明显的规则信息
	@NotEmpty(message = "【密码】不能为空")
	@Pattern(regexp = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,32}$",
			message = "【密码】规则不正确")
	private String password;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "UserLoginReq{" +
				"id=" + id +
				", loginName='" + loginName + '\'' +
				", password='" + password + '\'' +
				'}';
	}
}