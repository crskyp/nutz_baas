package com.justep.baas.servlet;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.alibaba.fastjson.JSONObject;
import com.justep.baas.action.ActionContext;
import com.justep.baas.action.Engine;
import com.justep.baas.data.DataUtils;

public class BaasServlet extends HttpServlet {
	private static final long serialVersionUID = -5873620616781916663L;
	private static final String METHOD_POST = "POST";
	private static final String METHOD_GET = "GET";
	private static final String MULTIPART_CONTENT_TYPE = "multipart/form-data";	
	private static final String JSON_CONTENT_TYPE = "application/json";
	private String Access_Control_Allow_Origin;
	
	public void service(ServletRequest request, ServletResponse response) throws ServletException {
		HttpServletRequest reg = (HttpServletRequest)request;
		HttpServletResponse resp = (HttpServletResponse)response;
		
		//设置跨域访问支持
		if(null!=Access_Control_Allow_Origin && !"".equals(Access_Control_Allow_Origin)){
			resp.addHeader("P3P", "CP=CAO PSA OUR");
			resp.addHeader("Access-Control-Allow-Origin", Access_Control_Allow_Origin);
			resp.addHeader("Access-Control-Allow-Credentials","true");
			if ("OPTIONS".equals(reg.getMethod())) {
				resp.addHeader("Access-Control-Allow-Methods", reg.getHeader("Access-Control-Request-Method"));
				resp.addHeader("Access-Control-Allow-Headers", reg.getHeader("Access-Control-Request-Headers"));
				return;
			}
		}
		
		execService(reg, resp);
	}

	private static String getRequestContentType(HttpServletRequest request) {
		return request.getContentType();
	}
	
	private static boolean isRequestMultipart(String type) {
		return null != type && -1 < type.indexOf(MULTIPART_CONTENT_TYPE);
	}

	private static boolean isJson(String type) {
		return null!=type && -1<type.indexOf(JSON_CONTENT_TYPE);
	}
	
	private static boolean isJson(HttpServletRequest request) {
		return isJson(getRequestContentType(request));
	}

	private static boolean isRequestMultipart(HttpServletRequest request) {
		return isRequestMultipart(getRequestContentType(request));
	}
	
	private void execService(HttpServletRequest reg, HttpServletResponse resp) throws ServletException {
		String URI = reg.getRequestURI();
		String contextPath = reg.getContextPath();
		if(URI.startsWith(contextPath)) URI = URI.substring(contextPath.length()+1);
		try {
			JSONObject params = (JSONObject) getParams(reg);
			params.put(ActionContext.REQUEST, reg);
			params.put(ActionContext.RESPONSE, resp);
			JSONObject ret = Engine.execAction(URI, params);
			if(null!=ret){
				DataUtils.writeJsonToResponse(resp, ret);
			}
		} catch (com.justep.baas.data.sql.SQLException e) {
			e.printStackTrace();
			throw new ServletException("执行Action："+URI+"失败，"+e.getMessage(), e);
		} catch (com.justep.baas.action.ActionException e) {
			e.printStackTrace();
			throw new ServletException("执行Action："+URI+"失败，"+e.getMessage(), e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServletException("执行Action："+URI+"失败，"+e.getMessage(), e);
		}
	}

	private String getBufferPath() {
		return System.getProperty("java.io.tmpdir");
	}
	
	private JSONObject getParams(ServletInputStream inputStream) throws Exception  {
		final int BUFFER_SIZE = 8 * 1024;
		byte[] buffer = new byte[BUFFER_SIZE];
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int bLen = 0;
		while ((bLen = inputStream.read(buffer)) > 0) {
			baos.write(buffer, 0, bLen);
		}
		String bodyData = new String(baos.toByteArray(), "UTF-8");
		JSONObject jo = JSONObject.parseObject(bodyData);
		return jo;
	}
	
	//目前暂时没有支持Multipart请求
	@SuppressWarnings("unused")
	private JSONObject getParamsByMultipart(HttpServletRequest request) throws Exception  {
		JSONObject params = new JSONObject();
		File tempPathFile = new File(getBufferPath());
		if (!tempPathFile.exists()) {
			tempPathFile.mkdirs();
		}
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(10 * 1024 * 1024);
		factory.setRepository(tempPathFile);
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setSizeMax(-1); // 设置最大文件尺寸，-1为不限制
		upload.setHeaderEncoding("UTF-8");
		List<?> items = upload.parseRequest(request);
		Iterator<?> iterator = items.iterator();
		while (iterator.hasNext()) {
			FileItem fi = (FileItem) iterator.next();
			if (fi.isFormField()) {
				String fieldName = fi.getFieldName();
				params.put(fieldName, fi.getString());
			} else {
				String fileName = fi.getName();
				if (fileName != null) {
					String fieldName = fi.getFieldName();
					params.put(fieldName, fi.getInputStream());
				}
			}
		}
		return params;
	}
	
	private JSONObject getParams(HttpServletRequest request) throws Exception {
		String method = request.getMethod();
		if(isRequestMultipart(request) || (!METHOD_GET.equalsIgnoreCase(method)&&!isJson(request))){
			JSONObject params = new JSONObject();
			return params;
		}else if(METHOD_POST.equalsIgnoreCase(method)&&isJson(request)) return getParams(request.getInputStream());
		else{
			JSONObject params = new JSONObject();
			for (Object k : request.getParameterMap().keySet()) {
				String key = (String)k;
				params.put(key, request.getParameter(key));
			}
			return params;
		}
	}
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		Access_Control_Allow_Origin = config.getInitParameter("Access-Control-Allow-Origin");
	}
	
	@Override
	public void destroy() {
		super.destroy();
	}	
}
