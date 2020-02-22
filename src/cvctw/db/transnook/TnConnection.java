/**
 * 
 */
package cvctw.db.transnook;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mysql.cj.jdbc.MysqlDataSource;

/**
 * A SINGLETON class
 * All clients will have one SQL connection
 * 
 * @author minge
 *
 */
public class TnConnection {

	public static final String SET_AUTOCOMMIT_0 = "SET autocommit=0";
	public static final String START_TRANSACTION = "START transaction";
	public static final String COMMIT = "COMMIT";
	public static final String ROLLBACK = "ROLLBACK";

	public static final String EXECUTE_QUERY = "executeQuery";
	public static final String EXECUTE_UPDATE = "executeUpdate";

	// Signal that the singleton has or has not been instantiated yet
	static TnConnection m_instance = null;

	private Connection theOnlyConn = null;
	public TnProp prop = null;
	private ResultSet theOnlyResultSet = null;

//	private TnRowReader rowReader = null;
//	private TnRowWriter rowWriter = null;

	private TnConnection() throws FileNotFoundException, IOException {
		prop = TnProp.getInstance(); //TnProp.readProp("tnProperties.prop");
	}

	private TnConnection(String path) throws FileNotFoundException, IOException {
		prop = TnProp.getInstance(path);
	}

	public static synchronized TnConnection getInstance() throws FileNotFoundException, IOException {
		return getInstance("tnProperties.prop");
	}

	public static synchronized TnConnection getInstance(String path) throws FileNotFoundException, IOException {
		if (m_instance == null) {
			m_instance = new TnConnection(path);
		}
		return m_instance;
	}

	@SuppressWarnings("deprecation")
	public Connection tnConnDriverMgr() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
		theOnlyConn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306?" +
				"user=root&password=PA65*18@du");
		System.out.println("connected by DriverManager");
		tnExecuteStatement(SET_AUTOCOMMIT_0, TnStatementType.UPDATE);
		return theOnlyConn;
	}

	public Connection tnConnDataSource() throws SQLException {
		MysqlDataSource ds = new MysqlDataSource();

		// Set dataSource Properties
		ds.setServerName(prop.getConnHost()); //"localhost");
		ds.setPortNumber(Integer.parseInt(prop.getConnPort())); //3306);
		ds.setDatabaseName(prop.getConnSchema()); //"transnook");
		ds.setUser(prop.getConnUser()); //"root");
		ds.setPassword(prop.getConnPwd()); //"PA65*18@du");
		theOnlyConn = ds.getConnection();
		System.out.println("connected by DataSource");
		tnExecuteStatement(SET_AUTOCOMMIT_0, TnStatementType.UPDATE);
		return theOnlyConn;
	}

	public Connection getConnection() {
		return theOnlyConn;
	}

	public void tnCloseConn() throws SQLException {
		if (theOnlyConn != null) {
			theOnlyConn.close();
			theOnlyConn = null;
		}
	}

	public ResultSet tnExecuteStatement(String stmnt, TnStatementType stmntType) throws SQLException {
		try {
			Statement st = theOnlyConn.createStatement();
			if (stmntType == TnStatementType.QUERY) {
				System.out.println("executing query: " + stmnt);
				theOnlyResultSet = st.executeQuery(stmnt);
			} else if (stmntType == TnStatementType.UPDATE) {
				System.out.println("executing update: " + stmnt);
				// Not necessary to check the int returned
				// Can't do anything with it in this multi-purpose methor.
				st.executeUpdate(stmnt);
			}
			return theOnlyResultSet;
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * Releasing the resource improves DB performance
	 * 
	 * @throws SQLException
	 */
	public void tnCloseResultSet() throws SQLException {
		theOnlyResultSet.close();
	}

}
