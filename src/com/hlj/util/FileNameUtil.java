package com.hlj.util;

import java.util.Date;

import org.apache.commons.io.FilenameUtils;

public class FileNameUtil {

	/**
	 * 根据文件原始名称创建一个随机名称（保持后缀统一）
	 * 
	 * @param originalFileName
	 */
	public static String createFileName(String originalFileName) {
		String ext = FilenameUtils.getExtension(originalFileName);
		String baseName = FilenameUtils.getBaseName(originalFileName);
		return String.format("%s_%s.%s", baseName, new Date().getTime(), ext);
	}

}
