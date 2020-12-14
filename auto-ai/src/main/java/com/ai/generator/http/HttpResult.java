package com.ai.generator.http;

import com.ai.generator.enums.ResultEnum;
import com.alibaba.fastjson.JSON;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;

/**
 * HTTP结果封装
 * @author Louis
 * @date Oct 29, 2018
 */
public class HttpResult<T> implements Serializable {
	private Integer code;
	private String msg;
	private T data;

	public HttpResult(ResultEnum resultEnum) {
		this(resultEnum.getCode(),resultEnum.getMessage());
	}

	public HttpResult(String messages) {
		this(ResultEnum.FAIL.getCode(),messages);
	}

	public HttpResult(Integer code, String messages) {
		this.code = code;
		this.msg = messages;
	}
	public HttpResult(T t) {
		this.code = ResultEnum.SUCCESS.getCode();
		this.msg = ResultEnum.SUCCESS.getMessage();
		this.data = t;
	}

	public static HttpResult error() {
		return new HttpResult(ResultEnum.ERROR);
	}
	
	public static HttpResult error(String msg) {
		return new HttpResult(msg);
	}

	public static HttpResult error(int code,String msg) {
		return new HttpResult(msg);
	}
	
	public static HttpResult error(ResultEnum resultEnum) {
		return new HttpResult(resultEnum);
	}


	public static HttpResult faultTolerant() {
		return new HttpResult(ResultEnum.FAULT_TOLERANT);
	}

	/**
	 * 操作成功
	 * @return
	 */
	public static HttpResult success() {
		return new HttpResult(ResultEnum.SUCCESS);
	}



	public boolean isSuccess(){
		return ResultEnum.SUCCESS.getCode().equals(code);
	}

	/**
	 * 操作成功带参数
	 *
	 * @param t
	 * @param <T>
	 * @return
	 */
	public static <T> HttpResult success(T t) {
		HttpResult result = success();
		result.setData(t);
		return result ;
	}


	/**
	 * 手动返回
	 *
	 * @param response
	 * @param resultEnum
	 * @throws Exception
	 */
	public static void returnJson(HttpServletResponse response, ResultEnum resultEnum) throws Exception {
		PrintWriter writer = null;
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=utf-8");
		try {
			writer = response.getWriter();
			writer.print(JSON.toJSON(error(resultEnum)));
		} catch (IOException e) {
			e.printStackTrace();
//			log.error("response error", e);
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}

	/**
	 * 手动返回
	 *
	 * @param response
	 * @throws Exception
	 */
	public static void returnJson(HttpServletResponse response, int code, String message) throws Exception {
		PrintWriter writer = null;
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=utf-8");
		try {
			writer = response.getWriter();
			writer.print(JSON.toJSON(error(code,message)));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
