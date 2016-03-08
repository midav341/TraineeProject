package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import dao.FileDAO;
import dao.UserDAO;
import objs.FilePOJO;
import objs.FolderPOJO;

public class JdbcFileDAO implements FileDAO{
	public static final Logger LOG=Logger.getLogger(JdbcFileDAO.class);
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
	
	public  void createFile(FilePOJO file) {
		createConnection();
		try{
			PreparedStatement statement = connection.prepareStatement(
					"INSERT INTO files (user_id, folder_id, path, name)"
							+ " VALUES (?,?,?,?)"
					);
			statement.setInt(1, file.getUserId());
			statement.setInt(2, file.getFolderId());
			statement.setString(3, file.getPath());
			statement.setString(4, file.getName());
			statement.executeUpdate();
			
			JdbcConnectUtil.close(statement);
			JdbcConnectUtil.close(connection);
		}catch(Exception e){
			LOG.error(e);
		}
	}
	
	public void update(FilePOJO file) {
		createConnection();
		try{
			PreparedStatement statement = connection.prepareStatement(
					 "UPDATE files SET folder_id=?,user_id=?, name=?, path=? WHERE file_id=?"
					);
			statement.setInt(1, file.getFolderId());
			statement.setInt(2, file.getUserId());
			statement.setString(3, file.getName());
			statement.setString(4, file.getPath());
			statement.setInt(5, file.getFileId());
			statement.executeUpdate();
			
			JdbcConnectUtil.close(statement);
			JdbcConnectUtil.close(connection);
			
		}catch(Exception e){
			LOG.error(e);
		}
	}
	

	public  void deleteFile(Integer fileId) {
		createConnection();
		LOG.info("fileId = "+fileId);
		try{
			PreparedStatement statement = connection.prepareStatement(
					"DELETE FROM files WHERE file_id=?"
					);
			statement.setInt(1, fileId);
			statement.executeUpdate();
			
			JdbcConnectUtil.close(statement);
			JdbcConnectUtil.close(connection);
			
		}catch(Exception e){
			LOG.error(e);
		}
	}
	
	public  List<FilePOJO> getFilesList(Integer userId){
		createConnection();
		List<FilePOJO> files = new ArrayList<>();
		try{
			PreparedStatement statement = connection.prepareStatement(
					"SELECT folder_id,file_id,name,path FROM files "
							+ "WHERE user_id=? ORDER BY folder_id ASC"
					);
			statement.setInt(1, userId);
			ResultSet result = statement.executeQuery();
			while(result.next()){
				FilePOJO file = new FilePOJO();
				file.setFolderId(result.getInt("folder_id"));
				file.setFileId(result.getInt("file_id"));
				file.setName(result.getString("name"));
				file.setPath(result.getString("path"));
				files.add(file);
			}
			JdbcConnectUtil.close(result,statement,connection);
			
		}catch(Exception e){
			LOG.error(e);
		}
		return files;
	}

	@Override
	public List<FilePOJO> getChildrenFiles(Integer folderID) {
		createConnection();
		List<FilePOJO> files = new ArrayList<>();
		try{
			PreparedStatement statement = connection.prepareStatement(
					"SELECT folder_id,file_id,name,path,user_id FROM files WHERE folder_id=?"
					);
			statement.setInt(1, folderID);
			ResultSet result = statement.executeQuery();
			while(result.next()){
				FilePOJO file = new FilePOJO();
				file.setFolderId(result.getInt("folder_id"));
				file.setFileId(result.getInt("file_id"));
				file.setName(result.getString("name"));
				file.setPath(result.getString("path"));
				file.setUserId(result.getInt("user_id"));
				files.add(file);
			}
			JdbcConnectUtil.close(result,statement,connection);
			
		}catch(Exception e){
			LOG.error(e);
		}
		return files;
	}
}
