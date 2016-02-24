package com.galaxyinternet.framework.core.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

/**
 * 运算辅助类
 */
public class NumberUtils {

	/**
	 * <br/>
	 * 方法描述: 将含有科学计数法的数值进行格式化 参数为double类型的
	 * 
	 * @param money
	 * @return
	 */
	public static String toFormat(double number) {
		return perc(number, 2);
	}

	/**
	 * <br/>
	 * 方法描述: 将含有科学计数法的数值进行格式化 参数为double类型的
	 * 
	 * @param money
	 * @return
	 */
	public static String toFormatNoSplit(double number) {
		return perc(number, 2).replace(",", "");
	}

	/**
	 * <br/>
	 * 方法描述: 将含有科学计数法的数值进行格式化 参数为double类型的
	 * 
	 * @param money
	 * @return
	 */
	public static double toFormatNoSplit(String number) {
		return Double.parseDouble(perc(Double.parseDouble(number), 2).replace(",", ""));
	}

	/**
	 * 将含有科学计数法的数值进行格式化 参数为Double类型的
	 * 
	 * @param money
	 * @return
	 */
	public static String toFormat(Double number) {
		double num = number.doubleValue();
		return perc(num, 2);
	}

	/**
	 * 将含有科学计数法的数值进行格式化 参数为Double类型的
	 * 
	 * @param money
	 * @return
	 */
	public static String toFormat(String number) {
		double num = Double.parseDouble(number);
		return perc(num, 2);
	}

	/**
	 * 四舍五入方法,科学计数法的转换,保留两位小数
	 * 
	 * @param d
	 *            值
	 * @return
	 */
	public static String perc(double d) {
		return perc(d, 2);
	}

	/**
	 * 四舍五入方法,科学计数法的转换,保留指定位的小数
	 * 
	 * @param d
	 *            值
	 * @param fraction
	 *            指定的位
	 * @return
	 */
	public static String perc(double d, int fraction) {
		if (Double.isNaN(d))
			d = 0;
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMaximumFractionDigits(fraction);
		return nf.format(d);
	}

	/***
	 * 
	 * <br/>
	 * 方法描述：真正的四舍五入格式化double数据方法 <br/>
	 * 极限情况1：保留六位小数，那么被格式化不准确的限制为超过11位整数。 <br/>
	 * 极限情况2：保留两位小数，那么被格式化不准确的限制为超过13位整数。
	 * 
	 * @param amount
	 * @param decimalPlaces
	 * @return
	 */
	public static String newDecimal(Number amount, int decimalPlaces) {
		int ration = 1;
		for (int k = 0; k < decimalPlaces + 1; k++) {
			ration = ration * 10;
		}
		amount = amount.doubleValue() * ration;
		amount = amount.longValue() + 5;
		amount = amount.longValue() / 10;
		amount = amount.doubleValue() * 10 / ration;
		NumberFormat numberInstance = NumberFormat.getNumberInstance(Locale.CHINA);
		numberInstance.setMaximumFractionDigits(decimalPlaces);
		if (amount.doubleValue() < 0.0) {
			return numberInstance.format(amount.doubleValue() - 0.01);
		} else {
			return numberInstance.format(amount);
		}
	}

	/***
	 * 
	 * <br/>
	 * 方法描述：真正的四舍五入格式化double数据方法 <br/>
	 * 极限情况1：保留六位小数，那么被格式化不准确的限制为超过11位整数。 <br/>
	 * 极限情况2：保留两位小数，那么被格式化不准确的限制为超过13位整数。
	 * 
	 * @param amount
	 * @param decimalPlaces
	 * @return
	 */
	public static double newDecimalToDouble(Number amount, int decimalPlaces) {
		return Double.parseDouble(newDecimal(amount, decimalPlaces).replace(",", ""));
	}

	/***
	 * 
	 * <br/>
	 * 方法描述：真正的四舍五入格式化double数据方法 <br/>
	 * 极限情况1：保留六位小数，那么被格式化不准确的限制为超过11位整数。 <br/>
	 * 极限情况2：保留两位小数，那么被格式化不准确的限制为超过13位整数。
	 * 
	 * @param amount
	 * @param decimalPlaces
	 * @return
	 */
	public static String newDecimalToString(Number amount, int decimalPlaces) {
		return newDecimal(amount, decimalPlaces).replace(",", "");
	}

	/***
	 * 
	 * <br/>
	 * 方法描述：真正的四舍五入格式化double数据方法（欢迎大家补充极限情况） 极限情况1：保留六位小数，那么被格式化不准确的限制为超过8位整数。
	 * 
	 * @param amount
	 * @param decimalPlaces
	 *            保留小数位数
	 * @return
	 */
	public static String realRound4Double(double amount, int decimalPlaces) {
		return newDecimal(amount, decimalPlaces);
	}

	/**
	 * 
	 * <br/>
	 * 方法描述: 格式化金额
	 * 
	 * @param pattern
	 * @param value
	 * @return
	 */
	public static String formatMoney(String pattern, double value) {
		DecimalFormat df = new DecimalFormat(pattern);
		return df.format(value);
	}

	/**
	 * 
	 * <br/>
	 * 方法描述:方法描述: 格式化金额
	 * 
	 * @param value
	 * @return
	 */
	public static String formatMoney(double value) {
		DecimalFormat df = new DecimalFormat("#,###.##");
		return df.format(value);
	}

	/***************************************************************************
	 * 
	 * <br/>
	 * 方法描述：格式化数字
	 * 
	 * @param pattern
	 * @param source
	 * @return
	 */
	public static String formatData(String pattern, double source) {
		if (null != pattern) {
			DecimalFormat df = new DecimalFormat(pattern);
			return df.format(source);
		} else {
			return source + "";
		}
	}

	/***************************************************************************
	 * 
	 * <br/>
	 * 方法描述：格式化数字，仅显示整数部分
	 * 
	 * @param source
	 * @return
	 */
	public static String formatData(double source) {
		return formatData("0", source);
	}

	/***
	 * 
	 * <br/>
	 * 方法描述：四舍五入格式化数字，double类型不能超过18个整数位。
	 * 
	 * @param pattern
	 * @param source
	 * @return
	 */
	public static String format(String pattern, String source) {
		if (source != null && source.length() > 0) {
			if (source.indexOf("E") != -1) {
				char[] nums = source.substring(0, source.indexOf("E")).toCharArray();
				int count = Integer.parseInt(source.substring(source.indexOf("E") + 1));
				String temp = "";
				for (int i = 0; i < nums.length; i++) {
					if (nums[i] != '.') {
						temp += nums[i];
					}
				}
				String zero = "";
				if ((temp.length() - 1) < count) {
					for (int i = 0; i < (count - temp.length() + 1); i++) {
						zero += "0";
					}
				}
				temp = temp + zero;
				source = temp.substring(0, count + 1);
				if (temp.substring(count + 1).length() > 0) {
					source += "." + temp.substring(count + 1);
				} else {
					source += "." + "0";
				}
			}
			String[] datas = pattern.split("\\.");
			int len = datas[1].length();
			int pos = source.indexOf(".");
			if (pos == -1) {
				source = source + ".0";
			}
			int lastPos = source.split("\\.")[1].length();
			int p = lastPos < len ? source.length() : pos + len + 1;
			String suffix = "";
			if (lastPos < len) {
				for (int i = 0; i < len - lastPos; i++) {
					suffix += "0";
				}
			}
			source = source + suffix;
			int end = Integer.parseInt(source.substring(p, p + 1));
			String result = source.substring(0, p);
			int res = Integer.parseInt(result.substring(p - 1, p));
			if (end >= 5) {
				res++;
			}
			return source.substring(0, p - 1) + res + suffix;
		} else {
			return "";
		}
	}

	/**
	 * 
	 * <br/>
	 * 方法描述:四舍五入
	 * 
	 * @param amount
	 *            需要格式化的数据
	 * @param decimalPlaces
	 *            保留小数位数
	 * @return
	 */
	public static String decimal(Number amount, int decimalPlaces) {

		// NumberFormat numberInstance =
		// NumberFormat.getNumberInstance(Locale.CHINA);
		// numberInstance.setMaximumFractionDigits(decimalPlaces);
		// numberInstance.setGroupingUsed(false);

		return newDecimal(amount, decimalPlaces);
	}

	/**
	 * 
	 * <br/>
	 * 方法描述:四舍五入（去掉逗号分隔符）
	 * 
	 * @param amount
	 *            需要格式化的数据
	 * @param decimalPlaces
	 *            保留小数位数
	 * @return
	 */
	public static String formatDecimal(Number amount, int decimalPlaces) {
		return newDecimal(amount, decimalPlaces).replaceAll(",", "");
	}

	/**
	 * 
	 * <br/>
	 * 方法描述:四舍五入（去掉逗号分隔符）
	 * 
	 * @param amount
	 *            需要格式化的数据
	 * @param decimalPlaces
	 *            保留小数位数
	 * @return
	 */
	public static String formatManHour(Number amount) {
		if (null != amount) {
			double value = amount.doubleValue();
			value = value + 0.01 * Math.pow(10, -1);
			return new BigDecimal(value).setScale(1, BigDecimal.ROUND_HALF_UP) + "";
		} else {
			return "0.0";
		}
	}

	public static String formatDoubleNumber(double value, int point) {
		value = value + 0.01 * Math.pow(10, -point);
		return new BigDecimal(value).setScale(6, BigDecimal.ROUND_HALF_UP) + "";
	}

	public static String formatFloatNumber(double value, int point) {
		value = value + 0.01 * Math.pow(10, -point);
		return new BigDecimal(value).setScale(point, BigDecimal.ROUND_HALF_UP) + "";
	}

	/**
	 * 返回补位的数字
	 * 
	 * @param value
	 *            数字
	 * @param integerLen
	 *            整数长度
	 * @param decimalsLen
	 *            小数长度
	 * @return
	 */
	public static String plusZero(String value, int integerLen, int decimalsLen) {
		if (StringUtils.isBlank(value))
			return "";
		String[] amounts = value.split("\\.");
		String number = "";
		// 对金额进行补位，接口中长度为15(9)V99
		if (amounts != null) {
			if (amounts[0].length() < integerLen) {
				int length = integerLen - amounts[0].length();
				amounts[0] = EachNum(amounts[0], length, "L");// “L”左补0 “R” 右补0
			} else {
				amounts[0] = amounts[0].substring(amounts[0].length() - integerLen);
			}
			if (amounts.length > 1) {
				if (amounts[1].length() < decimalsLen) {
					int length = decimalsLen - amounts[1].length();
					amounts[1] = EachNum(amounts[1], length, "R");// “L”左补0 “R”
																	// 右补0
				} else {
					amounts[1] = amounts[1].substring(0, decimalsLen);
				}
				number = amounts[0] + amounts[1];
			} else {
				String temp = "0";
				amounts[0] = EachNum(amounts[0], decimalsLen - 1, "R");// “L”左补0
																		// “R”
																		// 右补0
				number = amounts[0] + temp;
			}
		}
		return number.trim();
	}

	public static String multiply(String a, String b) {
		BigDecimal m = BigDecimal.valueOf(Double.valueOf(a));
		BigDecimal n = BigDecimal.valueOf(Double.valueOf(b));
		return m.multiply(n).toString();

	}

	/**
	 * 左右补0
	 * 
	 * @param param
	 * @param count
	 * @param type
	 *            “L”左补0 “R” 右补0
	 * @return
	 */
	public static String EachNum(String param, int count, String type) {
		for (int i = 0; i < count; i++) {
			if ("L".equals(type)) {
				param = "0" + param;
			}
			if ("R".equals(type)) {
				param = param + "0";
			}
		}

		return param;
	}
}
