package com.hlj.util;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DBUtils {

	static ComboPooledDataSource dataSource = null;

	public static synchronized ComboPooledDataSource getDatasource() {
		if (dataSource == null) {
			dataSource = new ComboPooledDataSource("def");
		}
		return dataSource;
	}

}
