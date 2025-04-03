package br.com.compass.config;

import java.sql.Connection;
import java.sql.DriverManager; 
import java.sql.SQLException;

// isso conecta o java ao Mysql, se der algum problema ele lança uma excessão 
public class DatabaseConnection {
	private static final String URL = "jdbc:mysql://localhost:3306/bank_system\";";
	private static final String USER = "bank_user";
	private static final String PASSWORD  = "Gv8437-8981";
	
	public static Connection getConnection() throws SQLException{
		return DriverManager.getConnection(URL,USER, PASSWORD);
	}
}
