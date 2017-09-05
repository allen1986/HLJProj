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
 * 文件下载
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
		// 获取请求的文件名称
		String fileName = request.getParameter("fileName");
		String basePath = PropsUtil.getString(UPLOAD_PATH_KEY);
		String fullName = String.format("%s/%s", basePath, fileName);

		// 设置文件MIME类型和文件名
		response.setContentType(getServletContext().getMimeType(fileName));
		response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

		// 创建缓存数组
		int len = 0;
		byte[] buff = new byte[2048];

		// 下载文件
		InputStream is = null;
		OutputStream os = null;
		try {
			is = new FileInputStream(fullName);
			os = response.getOutputStream();
			while ((len = is.read(buff)) != -1) {
				os.write(buff, 0, len);
			}
		} catch (Exception e) {
			LOGGER.error("文件下载失败，原因：", e);
		} finally {
			// 关闭文件流
			if (is != null) {
				try {
					is.close();
				} catch (Exception e) {
					LOGGER.error("输入流关闭错误，原因：", e);
				}
			}
			if (os != null) {
				try {
					os.close();
				} catch (Exception e) {
					LOGGER.error("输出流关闭错误，原因：", e);
				}
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
