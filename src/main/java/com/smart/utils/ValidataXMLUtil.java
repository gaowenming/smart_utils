package com.smart.utils;

import java.io.File;
import java.io.StringReader;
import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.SAXValidator;
import org.dom4j.io.XMLWriter;
import org.dom4j.util.XMLErrorHandler;

/**
 * 
 * <p>
 * 根据XML Schema 来验证SP传过来的XML文件是否符合接口文档规范
 * </p>
 * 
 * @ClassName: ValidataXML
 * @author: gaowm
 * @date 2009-11-20 下午03:25:53
 * 
 */
public class ValidataXMLUtil {

	/**
	 * 通过XSD（XML Schema）校验XML
	 * 
	 * @param xmlString
	 *            将xml拼成的String
	 * @param xsdFile
	 *            schema路径
	 * @return true 验证成功 , false 验证失败
	 */
	public static boolean validateXMLByXSD(String xmlString, String xsdFileName) throws Exception {
		// 创建默认的XML错误处理器
		XMLErrorHandler errorHandler = new XMLErrorHandler();
		// 获取基于 SAX 的解析器的实例
		SAXParserFactory factory = SAXParserFactory.newInstance();
		// 解析器在解析时验证 XML 内容。
		factory.setValidating(true);
		// 指定由此代码生成的解析器将提供对 XML 名称空间的支持。
		factory.setNamespaceAware(true);
		// 使用当前配置的工厂参数创建 SAXParser 的一个新实例。
		SAXParser parser = factory.newSAXParser();
		// 创建一个读取工具
		SAXReader xmlReader = new SAXReader();
		// 获取要校验xml文档实例
		Document xmlDocument = xmlReader.read(new StringReader(xmlString));
		// Document xmlDocument = (Document) xmlReader.read(new
		// File(xmlFileName));
		// 设置 XMLReader 的基础实现中的特定属性。核心功能和属性列表可以在
		// [url]http://sax.sourceforge.net/?selected=get-set[/url] 中找到。
		parser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
		parser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaSource", "file:" + getFilePath(xsdFileName));
		// 创建一个SAXValidator校验工具，并设置校验工具的属性
		SAXValidator validator = new SAXValidator(parser.getXMLReader());
		// 设置校验工具的错误处理器，当发生错误时，可以从处理器对象中得到错误信息。
		validator.setErrorHandler(errorHandler);
		// 校验
		validator.validate(xmlDocument);

		XMLWriter writer = new XMLWriter(OutputFormat.createPrettyPrint());
		// 如果返回false，说明校验失败，打印错误信息
		if (errorHandler.getErrors().hasContent()) {
			writer.write(errorHandler.getErrors());
			System.out.println("XML文件校验失败！错误信息是：" + errorHandler.getErrors());
			return false;
		} else {
			System.out.println("XML文件验证通过!  XSD文件校验成功！");
			return true;
		}
	}

	public static String getFilePath(String xsdFileName) {
		URL xsdFilePath = ValidataXMLUtil.class.getClassLoader().getResource("");
		String xsdFile = xsdFilePath.getFile();
		File file = new File(xsdFile);
		xsdFile = file.getPath();
		// System.out.println( xsdFile + File.separator + xsdFileName);
		return xsdFile + File.separator + xsdFileName;
	}

}
