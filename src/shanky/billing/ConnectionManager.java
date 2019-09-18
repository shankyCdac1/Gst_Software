package shanky.billing;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionManager {
	
	private static String url = "jdbc:mysql://localhost:3306/Gst_Software";
	private static String driverName = "com.mysql.jdbc.Driver";
	private static String username = "root";
	private static String password = "";
	private static String urlString = "";
	private static Connection conn;
	
	public static Connection getConnection()  
	{
		try
		{
			Class.forName(driverName);
			conn = DriverManager.getConnection(url,username,password);
		}
		catch(Exception e) {e.printStackTrace(); System.out.println("Connection Refused");}  
		return conn;
	}
	

}
