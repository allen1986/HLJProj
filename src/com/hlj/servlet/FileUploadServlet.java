package com.hlj.servlet;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import com.hlj.entity.TbFile;
import com.hlj.service.TbFileService;
import com.hlj.util.FileNameUtil;
import com.hlj.util.PropsUtil;

/**
 * 文件上传
 * 
 * @author David
 *
 */
public class FileUploadServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final String THRESHOLD_KEY = "THRESHOLD";
	private static final String MAX_FILE_SIZE_KEY = "MAX_FILE_SIZE";
	private static final String MAX_REQUEST_SIZE_KEY = "MAX_REQUEST_SIZE";
	private static final String UPLOAD_PATH_KEY = "UPLOAD_PATH";
	private static final String TMP_DIR_KEY = "TMP_DIR";
	private static final String HEADER_ENCODING_KEY = "HEADER_ENCODING";
	private static final Logger LOGGER = Logger.getLogger(FileUploadServlet.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 检查表单类型
		if (!ServletFileUpload.isMultipartContent(request)) {
			throw new RuntimeException("表单类型错误！");
		}
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// 设置文件大小临界值和存储临时目录
		factory.setSizeThreshold(PropsUtil.getInt(THRESHOLD_KEY));
		factory.setRepository(new File(System.getProperty(PropsUtil.getString(TMP_DIR_KEY))));
		ServletFileUpload upload = new ServletFileUpload(factory);
		// 设置最大文件上传值和最大请求数据值
		upload.setFileSizeMax(PropsUtil.getLong(MAX_FILE_SIZE_KEY));
		upload.setSizeMax(PropsUtil.getLong(MAX_REQUEST_SIZE_KEY));
		upload.setHeaderEncoding(PropsUtil.getString(HEADER_ENCODING_KEY));
		// 设置上传进度
		upload.setProgressListener(new ProgressListener() {
			@Override
			public void update(long pBytesRead, long pContentLength, int pItems) {
				float rate = (pBytesRead / (float) pContentLength) * 100;
				request.getSession().setAttribute("rate", String.format("%.2f", rate));
			}
		});
		// 设置文件保存路径
		String basePath = PropsUtil.getString(UPLOAD_PATH_KEY);
		File file = new File(basePath);
		// 如果上传目录不存在，则新建
		if (!file.exists()) {
			file.mkdirs();
		}
		try {
			String fileName = "";
			List<FileItem> fileList = upload.parseRequest(request);
			for (FileItem fileItem : fileList) {
				if (fileItem.getSize() == 0) {
					LOGGER.warn("上传文件为空文件！");
					continue;
				}
				if (!fileItem.isFormField()) {
					// 如果是文件，写入磁盘
					fileName = FileNameUtil.createFileName(fileItem.getName());
					File fileSaved = new File(basePath, fileName);
					fileItem.write(fileSaved);
				}
			}
			// 将记录写入数据库
			TbFileService service = new TbFileService();
			TbFile tbFile = new TbFile();
			tbFile.setCreateTime(new Date());
			// tbFile.setRegionId(request.getSession().getAttribute("regionId") + "");
			// tbFile.setCreatorName(request.getSession().getAttribute("userName") + "");
			// 硬编码用于测试，稍后进行删除
			tbFile.setFileName(fileName);
			tbFile.setRegionId("HLJ");
			tbFile.setCreatorName("admin");
			service.add(tbFile);
		} catch (Exception e) {
			LOGGER.error("文件上传错误，原因：\\r\\n", e);
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
