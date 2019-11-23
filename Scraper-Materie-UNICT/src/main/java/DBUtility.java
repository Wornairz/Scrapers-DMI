import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtility {
	private final String DB_URL = "jdbc:mysql://localhost/UNICT";
	private final String USER = "root";
	private final String PASS = "";
	private Connection conn;
	private Statement stmt;
	
	private static final DBUtility uniqueInstance = new DBUtility();

	private DBUtility() {
		try {
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
		    stmt = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static DBUtility getInstance() {
		return uniqueInstance;
	}
	
	public void query(String sql) throws SQLException {
		//System.out.println(sql);
		if(sql.startsWith("SELECT")) {
			ResultSet rs = null;
			rs = stmt.executeQuery(sql);
		}
		else if(sql.startsWith("INSERT")) {
			stmt.executeUpdate(sql);
		}
	}
}
