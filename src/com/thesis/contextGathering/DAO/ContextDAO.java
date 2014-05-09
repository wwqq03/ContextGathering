package com.thesis.contextGathering.DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;

import com.thesis.contextGathering.Server;
import com.thesis.contextGathering.common.ContextItem;

public class ContextDAO {

	public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	public static final String DB_URL = Server.DB_URL;
	
	public static final String USER = Server.DB_USER;
	public static final String PASSWORD = Server.DB_PASSWORD;
	
	private static final String table = Server.DB_CONTEXT_ITEM_TABLE;
	
	public static void store(ContextItem contextItem) {
		Connection conn = null;
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
			System.out.println("Database connected");
			
			Statement statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			String sql = "select * from " + table + " where "
					+ "subject='" + contextItem.getSubject() + "'"
					+ " and predicate='" + contextItem.getPredicate() + "'";
			ResultSet rs = statement.executeQuery(sql);
			if(rs.next()) {
				rs.updateString("object", contextItem.getObject());
				rs.updateRow();
			} else {
				rs.moveToInsertRow();
				rs.updateString("subject", contextItem.getSubject());
				rs.updateString("predicate", contextItem.getPredicate());
				rs.updateString("object", contextItem.getObject());
				rs.insertRow();
			}
			System.out.println("Database updated");
			
			rs.close();
			statement.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(conn != null){
				try{
					conn.close();
			    } catch(SQLException se) {
			    	se.printStackTrace();
			    }
			}
		}
	}
	
	public static ArrayList<ContextItem> read(String subject) {
		if(subject == null || subject.isEmpty()){
			return null;
		}
		
		return read(subject, null);
	}
	
	public static ArrayList<ContextItem> read(String subject, ArrayList<String> predicates) {
		if(subject == null || subject.isEmpty()){
			return null;
		}
		
		Connection conn = null;
		ArrayList<ContextItem> items = new ArrayList<ContextItem>();
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
			System.out.println("Database connected");
			
			Statement statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			String sql = "select * from " + table + " where "
					+ "subject='" + subject + "'";
			if(predicates != null && !predicates.isEmpty()) {
				Iterator<String> i = predicates.iterator();
				while(i.hasNext()){
					String predicate = i.next();
					sql += " and predicate='" + predicate + "'";
				}
			}
			ResultSet rs = statement.executeQuery(sql);
			
			while(rs.next()){
				ContextItem item = new ContextItem(
						rs.getString("subject"),
						rs.getString("predicate"),
						rs.getString("object"));
				item.setId(rs.getInt("id"));
				items.add(item);
			}
			
			rs.close();
			statement.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if(conn != null){
				try{
					conn.close();
			    } catch(SQLException se) {
			    	se.printStackTrace();
			    }
			}
		}
		
		return items;
	}
}
