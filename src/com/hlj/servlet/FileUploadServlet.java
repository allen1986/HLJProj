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
 * �ļ��ϴ�
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
		// ��������
		if (!ServletFileUpload.isMultipartContent(request)) {
			throw new RuntimeException("�����ʹ���");
		}
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// �����ļ���С�ٽ�ֵ�ʹ洢��ʱĿ¼
		factory.setSizeThreshold(PropsUtil.getInt(THRESHOLD_KEY));
		factory.setRepository(new File(System.getProperty(PropsUtil.getString(TMP_DIR_KEY))));
		ServletFileUpload upload = new ServletFileUpload(factory);
		// ��������ļ��ϴ�ֵ�������������ֵ
		upload.setFileSizeMax(PropsUtil.getLong(MAX_FILE_SIZE_KEY));
		upload.setSizeMax(PropsUtil.getLong(MAX_REQUEST_SIZE_KEY));
		upload.setHeaderEncoding(PropsUtil.getString(HEADER_ENCODING_KEY));
		// �����ϴ�����
		upload.setProgressListener(new ProgressListener() {
			@Override
			public void update(long pBytesRead, long pContentLength, int pItems) {
				float rate = (pBytesRead / (float) pContentLength) * 100;
				request.getSession().setAttribute("rate", String.format("%.2f", rate));
			}
		});
		// �����ļ�����·��
		String basePath = PropsUtil.getString(UPLOAD_PATH_KEY);
		File file = new File(basePath);
		// ����ϴ�Ŀ¼�����ڣ����½�
		if (!file.exists()) {
			file.mkdirs();
		}
		try {
			String fileName = "";
			List<FileItem> fileList = upload.parseRequest(request);
			for (FileItem fileItem : fileList) {
				if (fileItem.getSize() == 0) {
					LOGGER.warn("�ϴ��ļ�Ϊ���ļ���");
					continue;
				}
				if (!fileItem.isFormField()) {
					// ������ļ���д�����
					fileName = FileNameUtil.createFileName(fileItem.getName());
					File fileSaved = new File(basePath, fileName);
					fileItem.write(fileSaved);
				}
			}
			// ����¼д�����ݿ�
			TbFileService service = new TbFileService();
			TbFile tbFile = new TbFile();
			tbFile.setCreateTime(new Date());
			// tbFile.setRegionId(request.getSession().getAttribute("regionId") + "");
			// tbFile.setCreatorName(request.getSession().getAttribute("userName") + "");
			// Ӳ�������ڲ��ԣ��Ժ����ɾ��
			tbFile.setFileName(fileName);
			tbFile.setRegionId("HLJ");
			tbFile.setCreatorName("admin");
			service.add(tbFile);
		} catch (Exception e) {
			LOGGER.error("�ļ��ϴ�����ԭ��\\r\\n", e);
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
