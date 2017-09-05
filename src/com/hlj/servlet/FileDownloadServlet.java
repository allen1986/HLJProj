package com.hlj.servlet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.hlj.util.PropsUtil;

/**
 * �ļ�����
 * 
 * @author David
 *
 */
public class FileDownloadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String UPLOAD_PATH_KEY = "UPLOAD_PATH";
	private static final Logger LOGGER = Logger.getLogger(FileDownloadServlet.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// ��ȡ������ļ�����
		String fileName = request.getParameter("fileName");
		String basePath = PropsUtil.getString(UPLOAD_PATH_KEY);
		String fullName = String.format("%s/%s", basePath, fileName);

		// �����ļ�MIME���ͺ��ļ���
		response.setContentType(getServletContext().getMimeType(fileName));
		response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

		// ������������
		int len = 0;
		byte[] buff = new byte[2048];

		// �����ļ�
		InputStream is = null;
		OutputStream os = null;
		try {
			is = new FileInputStream(fullName);
			os = response.getOutputStream();
			while ((len = is.read(buff)) != -1) {
				os.write(buff, 0, len);
			}
		} catch (Exception e) {
			LOGGER.error("�ļ�����ʧ�ܣ�ԭ��", e);
		} finally {
			// �ر��ļ���
			if (is != null) {
				try {
					is.close();
				} catch (Exception e) {
					LOGGER.error("�������رմ���ԭ��", e);
				}
			}
			if (os != null) {
				try {
					os.close();
				} catch (Exception e) {
					LOGGER.error("������رմ���ԭ��", e);
				}
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
