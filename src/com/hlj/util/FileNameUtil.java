package com.hlj.util;

import java.util.Date;

import org.apache.commons.io.FilenameUtils;

public class FileNameUtil {

	/**
	 * �����ļ�ԭʼ���ƴ���һ��������ƣ����ֺ�׺ͳһ��
	 * 
	 * @param originalFileName
	 */
	public static String createFileName(String originalFileName) {
		String ext = FilenameUtils.getExtension(originalFileName);
		String baseName = FilenameUtils.getBaseName(originalFileName);
		return String.format("%s_%s.%s", baseName, new Date().getTime(), ext);
	}

}
