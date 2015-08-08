/**
 * Copyright (c) 2005-2009 springside.org.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * 
 * $Id: ReflectionUtils.java 384 2009-08-28 14:50:14Z calvinxiu $
 */
package com.smart.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 反射的Utils函数集合.
 * 
 * 提供访问私有变量,获取泛型类型Class,提取集合中元素的属性等Utils函数.
 * 
 * @author
 */
public class ReflectionUtils {

	private static Log logger = LogFactory.getLog(ReflectionUtils.class);

	/**
	 * 直接读取对象属性值,无视private/protected修饰符,不经过getter函数.
	 */
	public static Object getFieldValue(final Object object, final String fieldName) {
		Field field = getDeclaredField(object, fieldName);
		if (field == null)
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
		makeAccessible(field);
		Object result = null;
		try {
			result = field.get(object);
		} catch (IllegalAccessException e) {
			logger.error("不可能抛出的异常{}", e);
		}
		return result;
	}

	/**
	 * 直接设置对象属性值,无视private/protected修饰符,不经过setter函数.
	 */
	public static void setFieldValue(final Object object, final String fieldName, final Object value) {
		Field field = getDeclaredField(object, fieldName);
		if (field == null)
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
		makeAccessible(field);
		try {
			field.set(object, value);
		} catch (IllegalAccessException e) {
			logger.error("不可能抛出的异常:{}", e);
		}
	}

	/**
	 * 直接调用对象方法,无视private/protected修饰符.
	 */
	public static Object invokeMethod(final Object object, final String methodName, final Class<?>[] parameterTypes, final Object[] parameters) throws InvocationTargetException {
		Method method = getDeclaredMethod(object, methodName, parameterTypes);
		if (method == null)
			throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + object + "]");
		method.setAccessible(true);
		try {
			return method.invoke(object, parameters);
		} catch (IllegalAccessException e) {
			logger.error("不可能抛出的异常:{}", e);
		}

		return null;
	}

	/**
	 * 循环向上转型,获取对象的DeclaredField.
	 */
	protected static Field getDeclaredField(final Object object, final String fieldName) {
		for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
			// System.out.println("class:" + superClass);
			try {
				return superClass.getDeclaredField(fieldName);
			} catch (NoSuchFieldException e) {
				// Field不在当前类定义,继续向上转型
				// e.printStackTrace();
				// continue;
			}
		}
		return null;
	}

	/**
	 * 循环向上转型,获取对象的DeclaredField.
	 */
	protected static void makeAccessible(final Field field) {
		if (!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
			field.setAccessible(true);
		}
	}

	/**
	 * 循环向上转型,获取对象的DeclaredMethod.
	 */
	protected static Method getDeclaredMethod(Object object, String methodName, Class<?>[] parameterTypes) {

		for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				return superClass.getDeclaredMethod(methodName, parameterTypes);
			} catch (NoSuchMethodException e) {
				// Method不在当前类定义,继续向上转型
			}
		}
		return null;
	}

	/**
	 * 通过反射,获得Class定义中声明的父类的泛型参数的类型. eg. public UserDao extends
	 * HibernateDao<User>
	 * 
	 * @param clazz
	 *            The class to introspect
	 * @return the first generic declaration, or Object.class if cannot be
	 *         determined
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> getSuperClassGenricType(final Class clazz) {
		return getSuperClassGenricType(clazz, 0);
	}

	/**
	 * 通过反射,获得定义Class时声明的父类的泛型参数的类型.
	 * 
	 * 如public UserDao extends HibernateDao<User,Long>
	 * 
	 * @param clazz
	 *            clazz The class to introspect
	 * @param index
	 *            the Index of the generic ddeclaration,start from 0.
	 * @return the index generic declaration, or Object.class if cannot be
	 *         determined
	 */

	@SuppressWarnings("unchecked")
	public static Class getSuperClassGenricType(final Class clazz, final int index) {
		Type genType = clazz.getGenericSuperclass();
		if (!(genType instanceof ParameterizedType)) {
			logger.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
			return Object.class;
		}
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		if (index >= params.length || index < 0) {
			logger.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: " + params.length);
			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			logger.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
			return Object.class;
		}
		return (Class) params[index];
	}

	/**
	 * 提取集合中的对象的属性(通过getter函数),组合成List.
	 * 
	 * @param collection
	 *            来源集合.
	 * @param propertyName
	 *            要提取的属性名.
	 */
	@SuppressWarnings("unchecked")
	public static List fetchElementPropertyToList(final Collection collection, final String propertyName) {
		List list = new ArrayList();

		try {
			for (Object obj : collection) {
				list.add(PropertyUtils.getProperty(obj, propertyName));
			}
		} catch (Exception e) {
			convertToUncheckedException(e);
		}

		return list;
	}

	/**
	 * 提取集合中的对象的属性(通过getter函数),组合成由分割符分隔的字符串.
	 * 
	 * @param collection
	 *            来源集合.
	 * @param propertyName
	 *            要提取的属性名.
	 * @param separator
	 *            分隔符.
	 */
	@SuppressWarnings("unchecked")
	public static String fetchElementPropertyToString(final Collection collection, final String propertyName, final String separator) {
		List list = fetchElementPropertyToList(collection, propertyName);
		return StringUtils.join(list, separator);
	}

	/**
	 * 转换字符串类型到clazz的property类型的值.
	 * 
	 * @param value
	 *            待转换的字符串
	 * @param clazz
	 *            提供类型信息的Class
	 * @param propertyName
	 *            提供类型信息的Class的属性.
	 */
	public static Object convertValue(Object value, Class<?> toType) {
		try {
			DateConverter dc = new DateConverter();
			dc.setUseLocaleFormat(true);
			dc.setPatterns(new String[] { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss" });
			ConvertUtils.register(dc, Date.class);
			return ConvertUtils.convert(value, toType);
		} catch (Exception e) {
			throw convertToUncheckedException(e);
		}
	}

	/**
	 * 将反射时的checked exception转换为unchecked exception.
	 */
	public static IllegalArgumentException convertToUncheckedException(Exception e) {
		if (e instanceof IllegalAccessException || e instanceof IllegalArgumentException || e instanceof NoSuchMethodException)
			return new IllegalArgumentException("Refelction Exception.", e);
		else
			return new IllegalArgumentException(e);
	}

	/**
	 * 判断beanClass是否注册了指定Annotation
	 * 
	 * @return
	 */
	public static boolean isAnnotationPresent(String beanClass, Class annotation) {
		ClassLoader classLoader = annotation.getClassLoader();
		Class clz = null;
		try {
			clz = classLoader.loadClass(beanClass);
		} catch (ClassNotFoundException e) {
			logger.warn("无法找到类：" + beanClass);
			return false;
		}
		return clz.isAnnotationPresent(annotation);
	}

	/**
	 * 通过set方法设置值
	 * 
	 * @param bean
	 * @param fieldName
	 * @param setValue
	 */

	public static void setValueBySetMethod(Object bean, String fieldName, Object setValue) {
		if (bean == null || fieldName == null || "".equals(fieldName))
			return;
		Field field = getDeclaredField(bean, fieldName);
		if (field == null)
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + bean + "]");
		Method setMethod = getDeclaredMethod(bean, getSetMethodName(fieldName), new Class<?>[] { field.getType() });
		if (setMethod != null) {
			try {
				setMethod.invoke(bean, setValue);
			} catch (Exception e) {
				logger.error("执行方法时error methodName=" + setMethod.getName() + " " + e.getMessage(), e);
			}
		}

	}

	/**
	 * 得到某个pojo的get方法
	 * 
	 * @param fieldName
	 * @return
	 */
	public static String getGetMethodName(String fieldName) {
		StringBuffer result = new StringBuffer("get");
		String firstChar = fieldName.substring(0, 1);
		char firstCh = fieldName.charAt(0);
		// if(firstCh)
		if (Character.isUpperCase(firstCh)) {
			result.append(firstCh);
		} else {
			result.append(firstChar.toUpperCase());

		}
		result.append(fieldName.substring(1, fieldName.length()));
		return result.toString();
	}

	/**
	 * 得到某个pojo的set方法
	 * 
	 * @param fieldName
	 * @return
	 */
	public static String getSetMethodName(String fieldName) {
		StringBuffer result = new StringBuffer("set");
		String firstChar = fieldName.substring(0, 1);
		char firstCh = fieldName.charAt(0);
		// if(firstCh)
		if (Character.isUpperCase(firstCh)) {
			result.append(firstCh);
		} else {
			result.append(firstChar.toUpperCase());

		}
		result.append(fieldName.substring(1, fieldName.length()));
		return result.toString();
	}

	/**
	 * 得到某个class的所有声明的属性Field，返回一个list，list中的对象为Field
	 * 
	 * @param clz
	 * @return
	 */
	public static List<Field> getAllFields(Class clz) {
		List result = new ArrayList();
		Map allFields = new HashMap();
		// 继承树，儿子－父亲顺序
		List classFamilyTree = new LinkedList();
		classFamilyTree.add(clz);
		Class tempSuperClass = clz;
		while ((tempSuperClass = tempSuperClass.getSuperclass()) != null) {
			classFamilyTree.add(tempSuperClass);
		}
		// 从父类开始，存入Map，这样保证子类的field如果和父类重名时是子类覆盖父类，而不是父类覆盖子类
		for (int i = classFamilyTree.size() - 1; i >= 0; i--) {
			Class tempClass = (Class) classFamilyTree.get(i);
			Field[] fieldsOfTempClass = tempClass.getDeclaredFields();
			for (int j = 0; j < fieldsOfTempClass.length; j++) {
				Field field = fieldsOfTempClass[j];
				allFields.put(field.getName(), field);
			}
		}

		// 把map转换为list
		for (Iterator iter = allFields.entrySet().iterator(); iter.hasNext();) {
			Map.Entry entry = (Map.Entry) iter.next();
			result.add(entry.getValue());
		}
		return result;
	}

	/**
	 * 得到某个class的所有声明的属性String,返回一个list,list中的对象为String
	 * 
	 * @param clz
	 * @return
	 */
	public static List<String> getAllFieldNames(Class clz) {
		List result = new ArrayList();
		Map allFields = new HashMap();
		// 继承树，儿子－父亲顺序
		List classFamilyTree = new LinkedList();
		classFamilyTree.add(clz);
		Class tempSuperClass = clz;
		while ((tempSuperClass = tempSuperClass.getSuperclass()) != null) {
			classFamilyTree.add(tempSuperClass);
		}
		// 从父类开始，存入Map，这样保证子类的field如果和父类重名时是子类覆盖父类，而不是父类覆盖子类
		for (int i = classFamilyTree.size() - 1; i >= 0; i--) {
			Class tempClass = (Class) classFamilyTree.get(i);
			Field[] fieldsOfTempClass = tempClass.getDeclaredFields();
			for (int j = 0; j < fieldsOfTempClass.length; j++) {
				Field field = fieldsOfTempClass[j];
				allFields.put(field.getName(), field.getName());
			}
		}
		// 把map转换为list
		for (Iterator iter = allFields.entrySet().iterator(); iter.hasNext();) {
			Map.Entry entry = (Map.Entry) iter.next();
			result.add(entry.getValue());
		}
		return result;
	}

	/**
	 * 取出Class的所有父类(包括自己),List中存放Class对象
	 * 
	 * @param bean
	 * @return
	 */
	public static List<Class> getAllClassesOfClazz(Class clz) {

		List classFamilyTree = new LinkedList();
		classFamilyTree.add(clz);
		Class tempSuperClass = clz;
		while ((tempSuperClass = tempSuperClass.getSuperclass()) != null) {
			classFamilyTree.add(tempSuperClass);
		}
		return classFamilyTree;
	}

	/**
	 * 取出Class的所有父类的名字(包括自己),List中存放string对象
	 * 
	 * @param bean
	 * @return
	 */
	public static List<String> getAllClassNameOfClazz(Class clz) {

		List allClasses = getAllClassesOfClazz(clz);
		List result = new LinkedList();
		for (int i = allClasses.size() - 1; i >= 0; i--) {
			Class clazz = (Class) allClasses.get(i);
			result.add(clazz.getName());
		}
		return result;
	}

	/**
	 * 取出Class的所有接口(包括顶层接口),List中存放Class对象
	 * 
	 * @param bean
	 * @return
	 */
	public static List<Class> getAllInterfaceOfClazz(Class clz) {
		List allClass = getAllClassesOfClazz(clz);
		// 去掉重复的
		Set s = new HashSet();
		for (int i = 0; i < allClass.size(); i++) {
			Class clazz = (Class) allClass.get(i);
			List temp = getInterfaceOfClass(clazz);
			s.addAll(temp);
		}
		List result = new LinkedList();
		Iterator itrt = s.iterator();
		while (itrt.hasNext()) {
			Class c = (Class) itrt.next();
			result.add(c);
		}
		return result;

	}

	private static List<Class> getInterfaceOfClass(Class clz) {
		Class[] clazzs = clz.getInterfaces();
		List<Class> classFamilyTree = Arrays.asList(clazzs);
		return new LinkedList(classFamilyTree);
	}

	/**
	 * 取出Class的所有接口名称(包括顶层接口),List中存放string对象
	 * 
	 * @param bean
	 * @return
	 */
	public static List<String> getAllInterfaceNameOfClazz(Class clz) {

		List allInterfaces = getAllInterfaceOfClazz(clz);
		List result = new LinkedList();
		for (int i = allInterfaces.size() - 1; i >= 0; i--) {
			Class clazz = (Class) allInterfaces.get(i);
			result.add(clazz.getName());
		}
		return result;
	}

	/**
	 * 取getter的方法名称,danfo
	 * 
	 * @param attName
	 * @return
	 */
	public static String getGetterMethodNameOfAttribute(String attName) {
		attName = attName.trim();
		String firstLetter = attName.substring(0, 1);
		firstLetter = firstLetter.toUpperCase();
		// getter方法的名字
		String methodName = "get" + firstLetter + attName.substring(1, attName.length());
		return methodName;
	}

	/**
	 * 按照方法名和参数来取得方法,danfo
	 * 
	 * @param bean
	 * @param methodName
	 * @param args
	 * @return
	 */
	public static Method getMethodOfBeanByName(Object bean, String methodName, Object[] args) {

		if (bean == null || methodName == null) {
			return null;
		}
		if (args == null) {
			Object[] paras = {};
			args = paras;
		}
		Method method = null;
		Class beanClass = bean.getClass();
		Method[] methods = beanClass.getMethods();
		for (int i = methods.length - 1; i >= 0; i--) {
			Method methodTemp = (Method) methods[i];
			String methodNameTemp = methodTemp.getName();
			// 如果方法名称一样,则判断参数类型是否一样
			if (methodName.equals(methodNameTemp)) {
				Class[] paras = methodTemp.getParameterTypes();
				// 先判断参数个数是否相等
				if (paras.length != args.length) {
					continue;
				}
				// 如果相等,则判断每一个参数类型是否一致
				boolean isParaTheSame = true;
				for (int j = paras.length - 1; j >= 0; j--) {
					Class paraNeededClass = paras[j];
					Class paraGivenClass = args[j].getClass();
					if (!paraNeededClass.getName().equals(paraGivenClass.getName())) {
						isParaTheSame = false;
						break;
					}
				}
				if (isParaTheSame) {
					method = methodTemp;
				}
			}
		}
		return method;
		// end of 找到methodName和args确定的方法
	}
}
