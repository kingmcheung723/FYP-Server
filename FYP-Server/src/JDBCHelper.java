/**
 * 
 */
package src;

import java.util.List;

import javax.sql.DataSource;

import jdbchelper.JdbcHelper;
import jdbchelper.SimpleDataSource;

/**
 * @author CKM
 * 
 */
public class JDBCHelper {

	private static JDBCHelper instance = null;
	private JdbcHelper jdbcHelper;

	/**
	 * Constructor
	 */
	public JDBCHelper() {
		super();

		DataSource dataSource = new SimpleDataSource("com.mysql.jdbc.Driver",
				"jdbc:mysql://localhost:3306/fyp_db?useUnicode=yes&characterEncoding=UTF-8", "root", null);
		jdbcHelper = new JdbcHelper(dataSource);
	}

	public static JDBCHelper getInstance() {
		if (instance == null) {
			instance = new JDBCHelper();
		}
		return instance;
	}

	/**
	 * @return the jdbcHelper
	 */
	public JdbcHelper getJdbcHelper() {
		return jdbcHelper;
	}

	/**
	 * @param jdbcHelper the jdbcHelper to set
	 */
	public void setJdbcHelper(JdbcHelper jdbcHelper) {
		this.jdbcHelper = jdbcHelper;
	}
}
