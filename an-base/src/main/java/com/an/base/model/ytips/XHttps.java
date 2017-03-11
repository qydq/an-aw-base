package com.an.base.model.ytips;

import org.xutils.common.Callback.Cancelable;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.Map;

public class XHttps {
	/**
	 * 发送get请求
	 * @param <T>
	 */
	public static <T> Cancelable get(String url, Map<String,String> map, CommonCallback<T> callback){
		RequestParams params=new RequestParams(url);
		if(null!=map){
			for(Map.Entry<String, String> entry : map.entrySet()){
				params.addQueryStringParameter(entry.getKey(), entry.getValue());
			}
		}
		Cancelable cancelable = x.http().get(params, callback);
		return cancelable;
	}

	/**
	 * 发送post请求
	 * @param <T>
	 */
	public static <T> Cancelable post(String url, Map<String,Object> map, CommonCallback<T> callback){
		RequestParams params=new RequestParams(url);
		if(null!=map){
			for(Map.Entry<String, Object> entry : map.entrySet()){
				params.addParameter(entry.getKey(), entry.getValue());
			}
		}
		Cancelable cancelable = x.http().post(params, callback);
		return cancelable;
	}


	/**
	 * 上传文件
	 * @param <T>
	 */
	public static <T> Cancelable upLoadFile(String url, Map<String,Object> map, CommonCallback<T> callback){
		RequestParams params=new RequestParams(url);
		if(null!=map){
			for(Map.Entry<String, Object> entry : map.entrySet()){
				params.addParameter(entry.getKey(), entry.getValue());
			}
		}
		params.setMultipart(true);
		Cancelable cancelable = x.http().get(params, callback);
		return cancelable;
	}

	/**
	 * 下载文件
	 * @param <T>
	 */
	public static <T> Cancelable downLoadFile(String url, String filepath, CommonCallback<T> callback){
		RequestParams params=new RequestParams(url);
		//设置断点续传
		params.setAutoResume(true);
		params.setSaveFilePath(filepath);
		Cancelable cancelable = x.http().get(params, callback);
		return cancelable;
	}
}
