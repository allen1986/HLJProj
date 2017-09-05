package com.hlj.servlet;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.hlj.entity.TbFile;
import com.hlj.service.TbFileService;
import com.hlj.util.PropsUtil;

/**
 * 删除文件
 * 
 * @author David
 *
 */
public class FileDeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(FileUploadServlet.class);
	private static final String UPLOAD_PATH_KEY = "UPLOAD_PATH";

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		TbFileService service = new TbFileService();
		Integer id = request.getParameter("id") == null ? null : Integer.parseInt(request.getParameter("id"));
		try {
			// 首先获取该文件信息
			TbFile tbFile = service.get(id);
			// 删除数据库
			service.delete(id);
			// 删除文件
			String basePath = PropsUtil.getString(UPLOAD_PATH_KEY);
			File file = new File(basePath, tbFile.getFileName());
			if (file.exists()) {
				file.delete();
			}
		} catch (SQLException e) {
			LOGGER.error("文件删除错误，原因：", e);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
