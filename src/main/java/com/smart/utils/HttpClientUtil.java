package com.smart.utils;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Properties;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HttpClientUtil {

	public static final String CHARACTER_ENCODING = "UTF-8";
	
	/**
	 * post 请求，返回String的数据包
	 * 
	 * @param url
	 * @param requestData
	 * @return
	 * @throws Exception
	 */
	public static String postRequest(String url, String requestData)
			throws Exception {
		return postRequest(url, requestData.getBytes(CHARACTER_ENCODING), null);
	}
	
	/**
	 * HTTPS 请求，返回String的数据包
	 * 
	 * @param url
	 * @param requestData
	 * @return
	 * @throws Exception
	 */
	public static String httpsPostRequest(String url, String requestData)
			throws Exception {
		return HttpsPostRequest(url, requestData.getBytes(CHARACTER_ENCODING), null);
	}

	/**
	 * post请求并返回数据包
	 * 
	 * @param url
	 *            请求url
	 * @param requestData
	 *            请求数据
	 * @param requestProperties
	 *            请求包体
	 * @return byte[] 数据包
	 * @throws Exception
	 */
	public static String postRequest(String url, byte[] requestData,
			Properties requestProperties) throws Exception {
		HttpURLConnection httpConn = null;
		StringBuffer sBuffer = new StringBuffer("");
		try {
			httpConn = (HttpURLConnection) new URL(url).openConnection();
			// 封住包体
			if (requestProperties != null) {

			}
			String length = "0";
			if (requestData != null) {
				length = Integer.toString(requestData.length);
			}
			httpConn.setConnectTimeout(15000);
			httpConn.setRequestMethod("POST");
			httpConn.setRequestProperty("Content-type",
					"application/x-www-form-urlencoded");

			httpConn.setRequestProperty("Connection", "close");
			httpConn.setRequestProperty("Content-Length", length);
			httpConn.setDoInput(true);
			httpConn.setDoOutput(true);
			OutputStream outStream = httpConn.getOutputStream();
			outStream.write(requestData);

			outStream.flush();
			outStream.close();

			BufferedReader in = null;
			String inputLine = null;

			in = new BufferedReader(new InputStreamReader(
					httpConn.getInputStream(), CHARACTER_ENCODING));
			while ((inputLine = in.readLine()) != null) {
				sBuffer.append(inputLine);
			}

			in.close();

		} catch (Exception e) {
			throw e;
		} finally {
			if (httpConn != null) {
				httpConn.disconnect();
				httpConn = null;
			}
		}
		return sBuffer.toString();
	}
	
	
	private static class TrustAnyTrustManager implements X509TrustManager {
		   
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
   
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
   
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    }
   
    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
    
    @SuppressWarnings("unused")
	public static String HttpsPostRequest(String url, byte[] requestData,
			Properties requestProperties) throws Exception {
    	
    	SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, new TrustManager[]{new TrustAnyTrustManager()}, new java.security.SecureRandom());
		HttpsURLConnection httpsConn = null;
		StringBuffer sBuffer = new StringBuffer("");
		try {
			httpsConn =(HttpsURLConnection)  new URL(url).openConnection();
			// 封住包体
			if (requestProperties != null) {

			}
			String length = "0";
			if (requestData != null) {
				length = Integer.toString(requestData.length);
			}
			httpsConn.setHostnameVerifier(new TrustAnyHostnameVerifier());
			httpsConn.setSSLSocketFactory(sc.getSocketFactory());
			httpsConn.setConnectTimeout(15000);
			httpsConn.setDoInput(true);
			httpsConn.setDoOutput(true);
			//OutputStream outStream = httpsConn.getOutputStream();
			BufferedOutputStream hurlBufOus=new BufferedOutputStream(httpsConn.getOutputStream());
			hurlBufOus.write(requestData);

			hurlBufOus.flush();
			hurlBufOus.close();
			httpsConn.connect();
			System.out.println(httpsConn.getResponseCode());
			BufferedReader in = null;
			String inputLine = null;

			in = new BufferedReader(new InputStreamReader(
					httpsConn.getInputStream(), CHARACTER_ENCODING));
			while ((inputLine = in.readLine()) != null) {
				sBuffer.append(inputLine);
			}

			in.close();

		} catch (Exception e) {
			throw e;
		} finally {
			if (httpsConn != null) {
				httpsConn.disconnect();
				httpsConn = null;
			}
		}
		return sBuffer.toString();
	}
    
}