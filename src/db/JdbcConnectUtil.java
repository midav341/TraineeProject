package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

public class JdbcConnectUtil {
	public static final Logger LOG=Logger.getLogger(JdbcConnectUtil.class);
	
	static Context ctx;
	static DataSource dataSource;
	static Connection connection;
	final static String DB = "java:comp/env/db_users";
	
	public static Connection getConnection(){
		try{
			ctx = new InitialContext();
			dataSource = (DataSource) ctx.lookup(DB);
			connection = dataSource.getConnection();
		}catch(Exception e){
			LOG.error(e);
		}
		LOG.info("connection = " + connection);
		return connection;
	}
	
	public static void close(ResultSet resultSet , PreparedStatement statement, Connection connection){
		close(resultSet);
		close(statement);
		close(connection);
	}
	
	public static void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                LOG.error(e);
            }
        }
    }
 
    public static void close(PreparedStatement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
            	LOG.error(e);
            }
        }
    }
 
    public static void close(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
            	LOG.error(e);
            }
        }
    }
}
