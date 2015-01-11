/**
 * 
 */
package src;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import jdbchelper.JdbcHelper;
import jdbchelper.JdbcUtil;
import jdbchelper.SimpleDataSource;

/**
 * @author CKM
 * 
 */
public class JDBCHelper extends JdbcHelper {

	private static JDBCHelper instance = null;

	/**
	 * Constructor
	 */
	public JDBCHelper(DataSource dataSource) {
		super(dataSource);
		instance = this;
	}

	public static JDBCHelper getInstance() {
		if (instance == null) {
			DataSource dataSource = new SimpleDataSource(
					"com.mysql.jdbc.Driver",
					"jdbc:mysql://localhost:3306/fyp_db?useUnicode=yes&characterEncoding=UTF-8",
					"root", null);
			instance = new JDBCHelper(dataSource);
		}
		return instance;
	}

	/**
	 * @return the jdbcHelper
	 */
	public JDBCHelper getJDBCHelper() {
		return instance;
	}

	/**
	 * <p>
	 * This method may be used in executing statements like insert/update/delete
	 * on the server.
	 * </p>
	 * 
	 * <p>
	 * Example:
	 * </p>
	 * 
	 * <pre>
	 * int updatedUsers = jdbc.execute(
	 * 		&quot;update users set active = 0 where register_date &lt; ?&quot;, &quot;2009-01-01&quot;);
	 * </pre>
	 * <p>
	 * If something goes wrong with the execution of the statement, a
	 * JdbcException may be thrown
	 * </p>
	 * 
	 * @param sql
	 *            The sql statement to be executed on the server
	 * @param params
	 *            Optional parameteres that will be used as prepared statement
	 *            parameters
	 * @return Returns the number of affected rows
	 */
	public int execute(String sql, Object... params) {
		Connection con = null;
		Statement stmt = null;

		try {
			con = getConnection();

			if (params.length == 0) {
				stmt = con.createStatement();
				return stmt.executeUpdate(sql);
			} else {
				stmt = fillStatement(con.prepareStatement(sql), params);
				return ((PreparedStatement) stmt).executeUpdate();
			}
		} catch (SQLException e) {
			// throw new JdbcException("Error executing query:\n" + sql +
			// "\n\nError: " + e.getMessage(), e);
			return -1;
		} finally {
			JdbcUtil.close(stmt);
			freeConnection(con);
		}

	}
}
