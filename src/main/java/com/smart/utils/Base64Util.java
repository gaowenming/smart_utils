package com.smart.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.codec.binary.Base64;

/**
 * base64编码处理类
 * @author gaowm
 *
 */
public abstract class Base64Util {

	/**
	 * base64加密(或者叫编码、移位)
	 * 
	 * @param byte[] b
	 * @return byte[]
	 * @throws Exception
	 */
	public static byte[] encode(byte[] b) throws Exception {
		byte[] result = null;
		result = Base64.encodeBase64(b);
		return result;
	}

	/**
	 * base64解密(或者叫解码、复位)
	 * 
	 * @param b
	 * @return byte[]
	 * @throws Exception
	 */
	public static byte[] decode(byte[] b) throws Exception {
		byte[] result = null;
		result = Base64.decodeBase64(b);
		return result;
	}

	/**
	 * 对byte[]进行base64加密，生成指定字符集的字符串
	 * 
	 * @param b
	 * @param charset
	 * @return sting
	 * @throws Exception
	 */
	public static String encode(byte[] b, String charset) throws Exception {
		byte[] _b = Base64.encodeBase64(b);
		return new String(_b, charset);
	}

	/**
	 * base64解密，并生成指定字符集的字符串
	 * 
	 * @param byte[] b
	 * @param charset
	 * @return string
	 * @throws Exception
	 */
	public static String decode(byte[] b, String charset) throws Exception {
		String result = null;
		byte[] _b = Base64.decodeBase64(b);
		result = new String(_b, charset);
		return result;
	}

	/**
	 * 对Base64字符串解码，转成byte数组
	 * 
	 * @param s
	 * @return
	 */
	public static byte[] getFromBASE64(String base64String) throws Exception {
		if (base64String == null) {
			return null;
		}

		byte[] b = Base64.decodeBase64(base64String);
		return b;

	}

	/**
	 * 对字符串进行base64加密,默认字符串字符集UTF-8
	 * 
	 * @param str
	 * @return String
	 * @throws Exception
	 */
	public static String getBase64EncodeStr(String str) throws Exception {
		if (!StringUtil.isEmpty(str)) {
			return new String(encode(str.getBytes(CharsetUtil.UTF_8)), CharsetUtil.UTF_8);
		}
		return null;
	}

	/**
	 * 对指定字符集的字符串进行base64加密
	 * 
	 * @param str
	 * @param charset
	 * @return string
	 * @throws Exception
	 */
	public static String getBase64EncodeStr(String str, String charset) throws Exception {
		if (str != null && charset != null) {
			return new String(encode(str.getBytes(charset)), charset);
		}
		return null;
	}

	/**
	 * 对字符串进行base64解密操作
	 * 
	 * @param str
	 * @return string
	 * @throws Exception
	 */
	public static String getBase64DecodeStr(String str) throws Exception {
		if (!StringUtil.isEmpty(str)) {
			return new String(decode(str.getBytes(CharsetUtil.UTF_8)), CharsetUtil.UTF_8);
		}
		return null;
	}

	/**
	 * 对指定字符集的字符串进行base64解密操作
	 * 
	 * @param str
	 * @param charset
	 * @return string
	 * @throws Exception
	 */
	public static String getBase64DecodeStr(String str, String charset) throws Exception {
		if (str != null && charset != null) {
			return new String(decode(str.getBytes(charset)), charset);
		}
		return null;
	}

	/**
	 * 对文件进行base64加密，生成字符串，一般用于网络传输文件
	 * 
	 * @param file
	 * @return string
	 * @throws Exception
	 */
	public static String getBase64FromFile(File file) throws Exception {
		String result = null;
		if (file != null) {
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(file);
				if (fis != null) {
					int len = fis.available();
					byte[] xml = new byte[len];
					fis.read(xml);
					result = encode(xml, CharsetUtil.UTF_8);
				}
			} catch (FileNotFoundException e) {
				throw e;
			} catch (IOException e) {
				throw e;
			} finally {
				if (fis != null) {
					try {
						fis.close();
					} catch (IOException e) {
						throw e;
					}
				}
			}
		}
		return result;
	}
}
