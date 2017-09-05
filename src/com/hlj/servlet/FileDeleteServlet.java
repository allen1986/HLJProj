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
 * ɾ���ļ�
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
			// ���Ȼ�ȡ���ļ���Ϣ
			TbFile tbFile = service.get(id);
			// ɾ�����ݿ�
			service.delete(id);
			// ɾ���ļ�
			String basePath = PropsUtil.getString(UPLOAD_PATH_KEY);
			File file = new File(basePath, tbFile.getFileName());
			if (file.exists()) {
				file.delete();
			}
		} catch (SQLException e) {
			LOGGER.error("�ļ�ɾ������ԭ��", e);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
