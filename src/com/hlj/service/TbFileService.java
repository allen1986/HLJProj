package com.hlj.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.hlj.entity.TbFile;
import com.hlj.util.DBUtils;
import com.hlj.util.PropsUtil;

public class TbFileService {

	// ���ԣ�MySQL��Oracle
	private static final String SQL_DIALECT_KEY = "SQL_DIALECT";
	private static final String dialect = PropsUtil.getString(SQL_DIALECT_KEY);

	// ����Id��ȡһ����¼
	public TbFile get(Integer id) throws SQLException {
		QueryRunner qr = new QueryRunner(DBUtils.getDatasource());
		StringBuilder sb = new StringBuilder();
		sb.append("select * from tb_file where id=?");
		return qr.query(sb.toString(), new BeanHandler<TbFile>(TbFile.class), id);
	}

	// �����ݿ�����һ����¼
	public void add(TbFile tbFile) throws SQLException {
		QueryRunner qr = new QueryRunner(DBUtils.getDatasource());
		StringBuilder sb = new StringBuilder();
		sb.append("insert into tb_file (fileName,creatorName,createTime,regionId) values (?,?,?,?)");
		qr.execute(sb.toString(), tbFile.getFileName(), tbFile.getCreatorName(), tbFile.getCreateTime(),
				tbFile.getRegionId());
	}

	// ����Ȩ�޻�ȡ�ļ��б�
	public Map<String, Object> list(String regionId, int page, int rows) throws SQLException {
		QueryRunner qr = new QueryRunner(DBUtils.getDatasource());
		StringBuilder sb = new StringBuilder();
		StringBuilder sbTotal = new StringBuilder();
		Long total = 0L;
		List<TbFile> fileList = new ArrayList<TbFile>();
		int start = (page - 1) * rows;
		int end = start + rows - 1;
		if (dialect.equalsIgnoreCase("oracle")) {
			sb.append(
					"SELECT * FROM ( SELECT A.*, ROWNUM RN FROM (SELECT * FROM tb_file WHERE REGEXP_LIKE(regionId,'(^|\\,)")
					.append(regionId).append("($|\\,)') ) A WHERE ROWNUM <= ? ) WHERE RN >= ?");
			fileList = qr.query(sb.toString(), new BeanListHandler<TbFile>(TbFile.class), end, start);
			sbTotal.append("SELECT count(1) FROM tb_file WHERE REGEXP_LIKE(regionId,'(^|\\,)").append(regionId)
					.append("($|\\,)')");
			total = qr.query(sbTotal.toString(), new ScalarHandler<Long>());
		} else {
			sb.append("SELECT * FROM tb_file WHERE regionId REGEXP '(^|\\,)").append(regionId)
					.append("($|\\,)' order by createTime desc limit ?,?");
			fileList = qr.query(sb.toString(), new BeanListHandler<TbFile>(TbFile.class), start, rows);
			sbTotal.append("SELECT count(1) FROM tb_file WHERE regionId REGEXP '(^|\\,)").append(regionId)
					.append("($|\\,)'");
			total = qr.query(sbTotal.toString(), new ScalarHandler<Long>());
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", fileList);
		map.put("total", total);
		return map;
	}

	// ɾ���ļ�
	public void delete(int id) throws SQLException {
		QueryRunner qr = new QueryRunner(DBUtils.getDatasource());
		StringBuilder sb = new StringBuilder();
		sb.append("delete from tb_file where id=?");
		qr.execute(sb.toString(), id);
	}

}
