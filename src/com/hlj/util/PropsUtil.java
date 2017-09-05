package com.hlj.util;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * 工具类，用于读取Properties配置文件的值
 * @author David
 *
 */
public class PropsUtil {

	//默认返回值
	private final static int DEFAULTINT=-1;
	private final static long DEFAULTLONG=-1L;
	private final static float DEFAULTFLOAT=-1F; 
	private static Properties prop=null;
	private final static Logger LOGGER=Logger.getLogger(PropsUtil.class);
	
	static {
		try {
			prop =new Properties();
			prop.load(PropsUtil.class.getClassLoader().getResourceAsStream("prop.properties"));
		} catch (IOException e) {
			LOGGER.error("获取配置文件错误，原因：\\r\\n",e);
			e.printStackTrace();
		}
	} 
	
	/**
	 * 获取字符串
	 * @param key
	 * @return
	 */
	public static String getString(String key) {
		if(prop==null) {
			return null;
		}
		return prop.getProperty(key);
	}
	
	/**
	 * 获取Int值
	 * @param 要查询的键名 key
	 * @return
	 */
	public static int getInt(String key) {
		if(prop==null) {
			return DEFAULTINT;
		}
		return  prop.getProperty(key)==null?DEFAULTINT:Integer.parseInt(prop.getProperty(key));
	}
	
	/**
	 * 获取Int值
	 * @param	要查询的键名 key
	 * @param	默认值 val
	 * @return
	 */
	public static int getInt(String key,int val) {
		if(prop==null) {
			return val;
		}
		return  prop.getProperty(key)==null?val:Integer.parseInt(prop.getProperty(key));
	}
	
	/**
	 * 获取Long值
	 * @param 要查询的键名 key
	 * @return
	 */
	public static long getLong(String key ) {
		if(prop==null) {
			return DEFAULTLONG;
		}
		return  prop.getProperty(key)==null?DEFAULTLONG:Long.parseLong(prop.getProperty(key));
	}
	
	
	/**
	 * 获取Long值
	 * @param	要查询的键名 key
	 * @param	默认值 val
	 * @return
	 */
	public static long getLong(String key,long val) {
		if(prop==null) {
			return val;
		}
		return  prop.getProperty(key)==null?val:Long.parseLong(prop.getProperty(key));
	}
	
	/**
	 * 获取Long值
	 * @param 要查询的键名 key
	 * @return
	 */
	public static float getFloat(String key ) {
		if(prop==null) {
			return DEFAULTFLOAT;
		}
		return  prop.getProperty(key)==null?DEFAULTFLOAT:Float.parseFloat(prop.getProperty(key));
	}
	
	
	/**
	 * 获取Long值
	 * @param	要查询的键名 key
	 * @param	默认值 val
	 * @return
	 */
	public static float getFloat(String key,long val) {
		if(prop==null) {
			return val;
		}
		return  prop.getProperty(key)==null?val:Float.parseFloat(prop.getProperty(key));
	}
	
}
