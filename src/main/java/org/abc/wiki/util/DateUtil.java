package org.abc.wiki.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	// 两种字符串的日期转换成java.util.Date
	public static Date str2Date(String fmt) {
		Date date = null;
		SimpleDateFormat sdf = null;
		if (fmt.matches("^(\\d+)-(\\d+)-(\\d+)$")) {
			sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				date = sdf.parse(fmt);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else if (fmt.matches("(\\d+)-(\\d+)-(\\d+) (\\d+):(\\d+):(\\d+)$")) {
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				date = sdf.parse(fmt);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return date;
	}
}
