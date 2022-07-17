package org.abc.wiki.util;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class CopyUtil {
	/**
	 * 单体复制
	 *
	 * @param source 源对象
	 * @param clazz  目标运行时类
	 * @param <T>    静态泛型方法
	 * @return
	 */
	public static <T> T copy(Object source, Class<T> clazz) {
		if (source == null) {
			return null;
		}
		T obj = null;
		try {
			obj = clazz.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		BeanUtils.copyProperties(source, obj);
		return obj;
	}

	/**
	 * 列表复制
	 *
	 * @param source 源对象集合
	 * @param clazz  目标对象运行时类
	 * @param <T>    静态泛型方法
	 * @return
	 */
	public static <T> List<T> copyList(List source, Class<T> clazz) {
		List<T> target = new ArrayList<>();
		if (!CollectionUtils.isEmpty(source)) {
			for (Object c : source) {
				T obj = copy(c, clazz);
				target.add(obj);
			}
		}
		return target;
	}
}
