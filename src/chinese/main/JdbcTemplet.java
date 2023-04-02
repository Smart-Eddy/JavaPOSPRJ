package chinese.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
//Connection Pool
public class JdbcTemplet {
	
	private String url = "jdbc:oracle:thin:@localhost:1522:xe";
	private String user = "realpos";
	private String password = "1234";
	
	
	public JdbcTemplet() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
	
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public Connection connDB() {
		Connection conn = null;
		try {
			 conn	= DriverManager.getConnection(url,user,password);
		
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		return conn;
		
	}
	
	
	
	
	
}