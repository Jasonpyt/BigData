package com.itstar.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;


/**
 * 类描述：数据库工具类
 * DbUtil
 * 创建�?ZengBin 
 * 时间�?017�?�?8�?下午9:47:02 
 * @version 1.0.0
 *
 */
public class DbUtil {
	//数据库驱�?
	private static String DRIVER;
	//数据库链接地�?
	private static String URL;
	//数据库用户名
	private static String USER;
	//数据库密�?
	private static String PASSWORD;
	/**
	 * 给数据库参数赋�?
	 */
	static{
		loadConfig();
	}
	/**
	 * (获取数据库配置文件并赋�?方法)
	 * 方法名：loadConfig
	 * 创建人：ZengBin 
	 * 时间�?017�?�?2�?下午10:53:16 
	 * 手机:1564545646464 void
	 * @exception 
	 * @since  1.0.0
	 */
	public static void loadConfig(){
		InputStream inputStream = null;
		try {
			Properties properties = new Properties();
			inputStream = DbUtil.class.getResourceAsStream("/db.properties");
			properties.load(inputStream);
			DRIVER = properties.getProperty("driver");
			URL = properties.getProperty("url");
			USER = properties.getProperty("user");
			PASSWORD = properties.getProperty("password");
		} catch (IOException e) {
			throw new RuntimeException("读取数据库配置文件异常！",e);
		}finally{
			try {
				if (inputStream!=null) {
					inputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * (获取驱动)
	 * 方法名：getConnection
	 * 创建人：ZengBin 
	 * 时间�?017�?�?8�?下午9:47:57 
	 * 手机:1564545646464
	 * @return Connection
	 * @exception 
	 * @since  1.0.0
	 */
	public static Connection getConnection(){
		Connection connection = null;
		try{
			Class.forName(DRIVER);
			connection = DriverManager.getConnection(URL, USER, PASSWORD);
		}catch (ClassNotFoundException e) {
			e.printStackTrace();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}
	
	
	/**
	 * (封装更新数据方法)
	 * 方法名：update
	 * 创建人：ZengBin 
	 * 时间�?017�?�?9�?下午7:33:18 
	 * 手机:1564545646464
	 * @param sql
	 * @param args
	 * @return int
	 * @exception 
	 * @since  1.0.0
	 */
	public static int update(String sql,String... args){
		int row = 0;
		Connection connection = getConnection();
		PreparedStatement ps = null;
		try {
			if (sql==null||sql.equals("")) {
				return row;
			}else {
				ps = connection.prepareStatement(sql);
				for (int i = 0; i < args.length; i++) {
					ps.setObject(i+1, args[i]);
				}
				row = ps.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			closeDb(ps,connection);
		}
		return row;
	}
	
	/**
	 * 动�?设置参数
	 * @throws SQLException 
	 */
	public static void setParams(PreparedStatement ps,Object...objects) throws SQLException{
		if (objects!=null&&objects.length>0) {
			for(int i=0;i<objects.length;i++){
				ps.setObject(i+1, objects[i]);
			}
		}
	}
	
	/**
	 * (关闭资源)
	 * 方法名：close
	 * 创建人：ZengBin 
	 * 时间�?017�?�?8�?下午10:24:27 
	 * 手机:1564545646464 void
	 * @exception 
	 * @since  1.0.0
	 */
	public static void closeDb(Object... args){
		if (args==null) {
			return;
		}
		try {
			for (int i = 0; i < args.length; i++) {
				if (args[i] instanceof PreparedStatement &&args[i]!=null) {
					((PreparedStatement)args[i]).close();
				}
				if (args[i] instanceof Connection &&args[i]!=null) {
					((Connection)args[i]).close();
				}
				if (args[i] instanceof ResultSet &&args[i]!=null) {
					((ResultSet)args[i]).close();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * (返回List<T>集合查询方法)
	 * 方法名：getresult
	 * 创建人：ZengBin 
	 * 时间�?017�?�?9�?下午7:33:31 
	 * 手机:1564545646464
	 * @param cls
	 * @param sql
	 * @param args
	 * @return List<T>
	 * @exception 
	 * @since  1.0.0
	 */
	public static <T>List<T> queryForList(Class<T> cls,String sql,String... args){
		List<T> list = new ArrayList<T>();
		if (sql==null||sql.equals("")) {
			return null;
		}
		Connection connection = getConnection();
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			//赋�?
			if (args!=null) {
				for (int i = 0; i < args.length; i++) {
					ps.setObject(i+1, args[i]);
				}
				ResultSet rSet = ps.executeQuery();	//执行查询方法
				ResultSetMetaData metaData = rSet.getMetaData();//获取结果集的结构
				int columnCount = metaData.getColumnCount();//获取列数
				while(rSet.next()){
					T t = (T)cls.newInstance();
					for (int i = 0; i < columnCount; i++) {
						//拿到�?
						Object val = rSet.getObject(i+1);
						//获取 列的名字 的别�?
						String name = metaData.getColumnLabel(i+1);
						Field filed = cls.getDeclaredField(name);
						//如果字段的类型不是公�?��，就暴力反射
						if (!Modifier.isPublic(filed.getModifiers())) {
							filed.setAccessible(true);
						}
						filed.set(t, val);
					}
					list.add(t);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}finally{
			closeDb(ps,connection);
		}
		return list;
	}
	/**
	 * (返回List<Map<String,Object>>集合查询方法)
	 * 方法名：queryForList
	 * 创建人：ZengBin 
	 * 时间�?017�?�?2�?下午10:58:27 
	 * 手机:1564545646464
	 * @param sql
	 * @param args
	 * @return List<Map<String,Object>>
	 * @exception 
	 * @since  1.0.0
	 */
	public static List<Map<String, Object>> queryForList(String sql,Object... args){
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if (sql==null||sql.equals("")) {
			return null;
		}
		Connection connection = getConnection();
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			//赋�?
			if (args!=null) {
				for (int i = 0; i < args.length; i++) {
					ps.setObject(i+1, args[i]);
				}
				ResultSet rSet = ps.executeQuery();	//执行查询方法
				ResultSetMetaData metaData = rSet.getMetaData();//获取结果集的结构
				int columnCount = metaData.getColumnCount();//获取列数
				while(rSet.next()){
					Map<String, Object> map = new HashMap<String, Object>();
					for (int i = 0; i < columnCount; i++) {
						//拿到�?
						Object val = rSet.getObject(i+1);
						//获取 列的名字 的别�?
						String name = metaData.getColumnLabel(i+1);
						map.put(name, val);
					}
					list.add(map);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}finally{
			closeDb(ps,connection);
		}
		return list;
	}
	
	
	/**
	 * 利用RowMapper接口获取每行记录以List<Object>返回
	 * @param mapper
	 * @param sql
	 * @param objects
	 * @return
	 */
	public static <T>List<T> queryForList2(RowMapper<T> mapper,String sql,Object...objects){
		List<T> list = new ArrayList<T>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		connection = getConnection();
		try {
			ps = connection.prepareStatement(sql);
			setParams(ps, objects);
			rs = ps.executeQuery();
			int rownum = 0;
			while(rs.next()){
				rownum++;
				list.add(mapper.mappingRow(rs, rownum));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			closeDb(rs,ps,connection);
		}
		
		return list;
		
	}
	
	
	
	
}
