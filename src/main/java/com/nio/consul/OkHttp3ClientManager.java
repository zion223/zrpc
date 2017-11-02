package com.nio.consul;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSONObject;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttp3ClientManager {
	private String TAG = this.getClass().getName();
	private static OkHttp3ClientManager mInstance;
	private OkHttpClient mOkHttpClient;

	private OkHttp3ClientManager() {
		mOkHttpClient = new OkHttpClient.Builder()
				.readTimeout(30, TimeUnit.SECONDS) // 读取超时
				.connectTimeout(10, TimeUnit.SECONDS) // 连接超时
				.writeTimeout(60, TimeUnit.SECONDS) // 写入超时
				.build();
	}

	public static OkHttp3ClientManager getInstance() {
		if (mInstance == null) {
			synchronized (OkHttp3ClientManager.class) {
				if (mInstance == null) {
					mInstance = new OkHttp3ClientManager();
				}
			}
		}
		return mInstance;
	}

	/**
	 * post请求参数
	 *
	 * @param BodyParams
	 * @return
	 */
	private RequestBody SetRequestBody(Map<String, Object> BodyParams) {
		RequestBody body = null;
		// 创建请求的参数body
		FormBody.Builder builder = new FormBody.Builder();

		// 遍历key
		if (null != BodyParams) {
			for (Map.Entry<String, Object> entry : BodyParams.entrySet()) {

				System.out.println("Key = " + entry.getKey() + ", Value = "
						+ entry.getValue());

				builder.add(entry.getKey(), entry.getValue().toString());

			}
		}

		body = builder.build();
		return body;
	}

	/**
	 * get方法连接拼加参数
	 *
	 * @param mapParams
	 * @return
	 */
	private String setUrlParams(Map<String, Object> mapParams) {
		String strParams = "";
		if (mapParams != null) {
			Iterator<String> iterator = mapParams.keySet().iterator();
			String key = "";
			while (iterator.hasNext()) {
				key = iterator.next().toString();
				strParams += "&" + key + "=" + mapParams.get(key);
			}
		}

		return strParams;
	}

	// public <T> T getBeanExecute(String url,Map<String, Object> map,final
	// Class<T> rspClass) throws IOException{
	// String UrlParams = setUrlParams(map);
	// String URL = url + "?" + UrlParams;
	// Request request = new Request.Builder().url(URL).build();
	// Response response = mOkHttpClient.newCall(request).execute();
	// String responseString =response.body().string();
	//
	// return responseString;
	// }
	public <T>  T getBeanExecute(String url, Map<String, Object> map,
			final Class<T> rspClass) throws IOException {
		String UrlParams = setUrlParams(map);
		String URL = url + "?" + UrlParams;
		Request request = new Request.Builder().url(URL).build();
		Response response = mOkHttpClient.newCall(request).execute();
		String responseString = response.body().string();
		Object parseObject = JSONObject.parseObject(responseString, rspClass);
		return (T)parseObject;
	}
}
