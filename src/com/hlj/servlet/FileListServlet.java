package com.hlj.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.hlj.service.TbFileService;
import com.hlj.util.JsonUtil;

/**
 * 获取文件列表
 * 
 * @author David
 *
 */
public class FileListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(FileListServlet.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		TbFileService service = new TbFileService();
		try {
			Integer page = request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page"));
			Integer rows = request.getParameter("rows") == null ? 10 : Integer.parseInt(request.getParameter("rows"));
			Map<String, Object> map = service.list("HEB", page, rows);
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(JsonUtil.toJson(map));
		} catch (SQLException e) {
			LOGGER.error("文件下载列表查询错误，原因：" + e);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
