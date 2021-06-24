package com.pjh.DBUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class DBUtils {

	private static String URL = "jdbc:mysql://localhost:3306/yiqing?useUnicode=true&characterEncoding=GB18030&useSSL=false&serverTimezone=GMT&allowPublicKeyRetrieval=true";
	private static String username = "root";
	private static String password = "123456";
	
	public static Connection getConnection() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			return DriverManager.getConnection(URL, username, password);
		} catch (Exception e) {
			System.out.println("SQL CONNECTION FAILED!");
			e.printStackTrace();
		}
		return null;
	}
	
	public static String queryGlobalLatestData()
	{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		JSONArray jsonArray = new JSONArray();
		String sql = "select * from global";
		
		try {
			connection = getConnection();
			preparedStatement = connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("name", resultSet.getString(2));
				jsonObject.put("confirm", resultSet.getString(3));
				jsonObject.put("suspect", resultSet.getString(4));
				jsonObject.put("heal", resultSet.getString(5));
				jsonObject.put("dead", resultSet.getString(6));
				jsonObject.put("severe", resultSet.getString(7));
				jsonObject.put("idcode", resultSet.getString(8));
				jsonObject.put("lastupdatetime", resultSet.getString(9));
				jsonArray.add(jsonObject);
			}
			return jsonArray.toString();
		} catch (Exception e) {
			return null;
		}
	}
	
	public static String queryLatestData()
	{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		JSONArray jsonArray = new JSONArray();
		String sql = "select * from realtime";
		
		try {
			connection = getConnection();
			preparedStatement = connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("name", resultSet.getString(2));
				jsonObject.put("confirm", resultSet.getString(3));
				jsonObject.put("suspect", resultSet.getString(4));
				jsonObject.put("heal", resultSet.getString(5));
				jsonObject.put("dead", resultSet.getString(6));
				jsonObject.put("severe", resultSet.getString(7));
				jsonObject.put("idcode", resultSet.getString(8));
				jsonObject.put("lastupdatetime", resultSet.getString(9));
				jsonArray.add(jsonObject);
			}
			return jsonArray.toString();
		} catch (Exception e) {
			return null;
		}
	}
	
	public static String queryDataByDate(String date) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		JSONArray jsonArray = new JSONArray();
		
		String province = "";
		String sql = "select * from info where Date like '"+date+"%' and  City=''";
//		String sql = "select * from realtime";
		System.out.println(sql);
		try {
			connection = getConnection();
			preparedStatement = connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				province += resultSet.getString("Province")+";";
//				province += resultSet.getString("name")+";";
			}
			resultSet.close();
			String string[] = province.split(";");
			for (int i = 0; i < string.length; i++) {
				if (string[i].trim().equals("")) {
					continue;
				}
				sql = "select sum(Confirmed_num),sum(Yisi_num),sum(Cured_num),sum(Dead_num) from info where Date like '"
						+date+"%' and Province='"+string[i]+"'";
				preparedStatement = connection.prepareStatement(sql);
				resultSet = preparedStatement.executeQuery();
				resultSet.next();
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("name", string[i]);
				jsonObject.put("num", resultSet.getInt(1));
				jsonObject.put("yisi", resultSet.getString(2));
				jsonObject.put("cure", resultSet.getString(3));
				jsonObject.put("dead", resultSet.getString(4));
				resultSet.close();
				sql = "select * from info where Date like '"+date+"%' and Province='"+string[i]+"'";
				preparedStatement = connection.prepareStatement(sql);
				resultSet = preparedStatement.executeQuery();
				resultSet.next();
				jsonObject.put("code", resultSet.getString("Code"));
				resultSet.close();
				jsonArray.add(jsonObject);
			}
			return jsonArray.toString();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String queryGlobalLatestDataByDate(String date)
	{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		JSONArray jsonArray = new JSONArray();
		String sql = "select * from global where lastupdatetime like '"+date+"%' ";
		System.out.println(sql);
		
		try {
			connection = getConnection();
			preparedStatement = connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("name", resultSet.getString(2));
				jsonObject.put("confirm", resultSet.getString(3));
				jsonObject.put("suspect", resultSet.getString(4));
				jsonObject.put("heal", resultSet.getString(5));
				jsonObject.put("dead", resultSet.getString(6));
				jsonObject.put("severe", resultSet.getString(7));
				jsonObject.put("idcode", resultSet.getString(8));
				jsonObject.put("lastupdatetime", resultSet.getString(9));
				jsonArray.add(jsonObject);
			}
			return jsonArray.toString();
		} catch (Exception e) {
			return null;
		}
	}
	
	public static ArrayList<String> queryGlobalLatestDataByDate2(String date)
	{
		ArrayList<String> list = new ArrayList<String>();
		Connection conn = getConnection();
		PreparedStatement statement = null;
		String sql = "SELECT * FROM global where lastupdatetime like '"+date+"%'";  
        ResultSet rs = null;
        try {
        	statement = conn.prepareStatement(sql);
        	rs = statement.executeQuery();

        	while(rs.next())
        	{
        		list.add(rs.getString(2));
        		list.add(rs.getString(3));
        		list.add(rs.getString(4));
        		list.add(rs.getString(5));
        		list.add(rs.getString(6));
        		list.add(rs.getString(7));
        		list.add(rs.getString(8));
        		list.add(rs.getString(9));
        	} 
        }catch (SQLException e) {
        		e.printStackTrace();
        }
        return list;
	}
//	public static void main(String[] args) {
//		System.out.println(queryGlobalLatestDataByDate2("2020-03-18").toString());
//	}
}
