package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import dao.UserDAO;
import entities.UserEntity;
import objs.UserPOJO;

public class JdbcUserDAO implements UserDAO{
	public final Logger LOG=Logger.getLogger(JdbcUserDAO.class);
	Connection connection;
	
	private void createConnection(){
		if(connection==null){
			connection = JdbcConnectUtil.getConnection();
		}else{
			try {
				if(connection.isClosed()){
					connection = JdbcConnectUtil.getConnection();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void registerNewUser(UserPOJO userPOJO) {
		createConnection();
		try{
			PreparedStatement preparedStatement = connection.prepareStatement(
					"INSERT INTO users (email, password) VALUES (?,?)"
					);
			preparedStatement.setString(1, userPOJO.getEmail());
			preparedStatement.setString(2, userPOJO.getPassword());
			preparedStatement.executeUpdate();
			JdbcConnectUtil.close(preparedStatement);
			JdbcConnectUtil.close(connection);
		}catch(Exception e){
			LOG.error(e);
		}
	}

	@Override
	public boolean userExists(UserPOJO userPOJO) {
		createConnection();
		boolean userExists = false;
		try{
			PreparedStatement preparedStatement = connection.prepareStatement(
					"SELECT * FROM users WHERE email=?"
					);
			preparedStatement.setString(1, userPOJO.getEmail());
			ResultSet result = preparedStatement.executeQuery();
			if(result.next()){
				userExists=true;
			}
			JdbcConnectUtil.close(result,preparedStatement,connection);
		}catch(Exception e){
			LOG.error(e);
		}
		LOG.info("userExists = "+userExists);
		return userExists;
	}

	@Override
	public boolean loginDataCorrect(UserPOJO userPOJO) {
		createConnection();
		boolean loginDataCorrect = false;
		try{
			PreparedStatement preparedStatement = connection.prepareStatement(
					"SELECT * FROM users WHERE email=? AND password=?"
					);
			preparedStatement.setString(1, userPOJO.getEmail());
			preparedStatement.setString(2, userPOJO.getPassword());
			ResultSet result = preparedStatement.executeQuery();
			if(result.next()){
				loginDataCorrect=true;
			}
			JdbcConnectUtil.close(result,preparedStatement,connection);
		}catch(Exception e){
			LOG.error(e);
		}
		LOG.info("loginDataCorrect = "+loginDataCorrect);
		return loginDataCorrect;
	}

	@Override
	public Integer getUserIdByEmail(String email) {
		createConnection();
		Integer userId = null;
		try{
			PreparedStatement preparedStatement = connection.prepareStatement(
					"SELECT user_id FROM users WHERE email=?"
					);
			preparedStatement.setString(1, email);
			ResultSet result = preparedStatement.executeQuery();
			if(result.next()){
				userId=result.getInt("user_id");
			}
			JdbcConnectUtil.close(result,preparedStatement,connection);
		}catch(Exception e){
			LOG.error(e);
		}
		LOG.info("userId = " + userId);
		return userId;
	}
}
