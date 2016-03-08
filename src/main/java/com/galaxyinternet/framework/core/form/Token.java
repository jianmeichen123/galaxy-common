package com.galaxyinternet.framework.core.form;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.servlet.http.HttpSession;

/**
 * @description 用于重复提交表单时，预先生成令牌并保存在{@link HttpSession}作用域中， <br>
 *              在表单处理结束后，移除{@link HttpSession}作用域中的令牌。 处理表单请求的业务方向，需要添加该注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Token {
	public String TOKEN = "TOKEN";

	/**
	 * @return 是否移除，默认“是”
	 */
	boolean remove() default true;
}
