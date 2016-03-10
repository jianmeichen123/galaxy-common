package com.galaxyinternet.framework.core.validator;

import java.util.List;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import com.galaxyinternet.framework.core.model.Result;

/**
 * @Description: 处理数据的后台验证，并返回验证结果
 * @author keifer
 * @date 2016年3月10日
 */
public class ValidatorResultHandler {
	public static Result handle(BindingResult result) {
		Result validationResult = new Result();
		if (result.hasErrors()) {
			List<ObjectError> list = result.getAllErrors();
			ObjectError oe = list.get(0);
			validationResult.addError(oe.getDefaultMessage());
		} else {
			validationResult.addOK("");
		}
		return validationResult;
	}
}
