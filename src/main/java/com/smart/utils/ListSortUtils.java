package com.smart.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.NullComparator;

/**
 * 列表类排序
 * 
 * @author gaowm
 * 
 * @date 2012-6-27 下午10:23:58
 * 
 */
public class ListSortUtils {
	/**
	 * 根据某个javabean属性对此javabean形成的列表进行排序，其中sortMode为0代表升序，为1代表降序
	 * 
	 * @param list
	 * @param propertys
	 * @param sortMode
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void sort(List list, final String propertys,
			final String sortMode) {
		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				if (o1 == null && o2 == null)
					return 0;
				if (o1 == null)
					return ("0".equals(sortMode) ? -1 : 1);
				if (o2 == null)
					return ("0".equals(sortMode) ? 1 : -1);
				String[] properties = propertys.split(",");
				for (int i = 0; i < properties.length; i++) {
					NullComparator b = new NullComparator();
					Comparator c = new BeanComparator(properties[i], b);
					int result = c.compare(o1, o2);
					if (result != 0)
						return ("0".equals(sortMode) ? result : -result);
				}
				return 0;
			}
		});
	}
}
