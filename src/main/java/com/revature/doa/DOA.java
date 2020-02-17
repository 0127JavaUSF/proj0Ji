package com.revature.doa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DOA {

	private Connection db;
	private PreparedStatement prepState;
	private ResultSet result;
	
	public DOA(){
		//get the sql connected :D
		try{
			//jdbc:postgresql://host:port/database_name
			db = DriverManager.getConnection("jdbc:postgresql://database-1.cfgsjckjokdt.us-east-2.rds.amazonaws.com:5432/postgres","jdbc_bot", "1234" );
		}
		catch (SQLException ex) {
			System.out.println("Connection has Timed out, Please verify username or firewall on your database");
			System.out.println(ex);
		}
	}
	
	
	//set up prepared statement
	public void setPreparedStatement(String query) {
		
		try {
			prepState = db.prepareStatement(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//get result set
	public ResultSet queryStatements(String... query) {
		try{
			int i = 1;
			for(String e:query) {
				prepState.setString(i,e);
				i++;
			}
			result = prepState.executeQuery();
		}
		
		catch(SQLException ex) {
			System.out.println(ex);
		}
		return result;
	}
	
	//alter data
	public int alterDatabase(String... query) {
		try {
			int i = 1;
			for(String e:query) {
				prepState.setString(i,e);
				i++;
			}
			
			return prepState.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	//commit
	public void commit() {
		try {
			db.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//turn off commit auto
	public void setCommitFalse() {
		try {
			db.setAutoCommit(false);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	//turn on commit auto
	public void setCommitTrue() {
		try {
			db.setAutoCommit(true);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
