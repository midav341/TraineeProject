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

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import dao.FileDAO;
import dao.FolderDAO;
import dao.UserDAO;
import entities.FolderEntity;
import objs.FilePOJO;
import objs.FolderPOJO;

public class JdbcFolderDAO implements FolderDAO {
	FileDAO fileDAO = new JdbcFileDAO();
	public final Logger LOG = Logger.getLogger(JdbcFolderDAO.class);
	Connection connection;

	private void createConnection() {
		if (connection == null) {
			connection = JdbcConnectUtil.getConnection();
		} else {
			try {
				if (connection.isClosed()) {
					connection = JdbcConnectUtil.getConnection();
				}
			} catch (SQLException e) {
				LOG.error(e);
			}
		}
	}

	public void createFolderInterior(FolderPOJO folder) {
		try {
			PreparedStatement statement = connection
					.prepareStatement("INSERT INTO folders (user_id, parent_id, path, name) VALUES (?,?,?,?)");
			statement.setInt(1, folder.getUserId());
			statement.setInt(2, folder.getParentId());
			statement.setString(3, folder.getPath());
			statement.setString(4, folder.getName());
			statement.executeUpdate();
			JdbcConnectUtil.close(statement);
		} catch (Exception e) {
			LOG.error(e);
			e.printStackTrace();
		}
	}

	public String getPathOfFolderById(Integer id) {
		String path = "";
		createConnection();
		try {
			PreparedStatement statement = connection.prepareStatement("SELECT path FROM folders WHERE folder_id = ?");
			statement.setInt(1, id);
			ResultSet result = statement.executeQuery();

			if (result.next()) {
				path = result.getString("path");
			}
			JdbcConnectUtil.close(result, statement, connection);
		} catch (Exception e) {
			LOG.error(e);
		}
		return path;
	}

	public void updateInterior(FolderPOJO folder) {
		try {
			PreparedStatement statement = connection
					.prepareStatement("UPDATE folders SET name= ?, path= ?, parent_id=? WHERE folder_id= ? ");
			statement.setString(1, folder.getName());
			statement.setString(2, folder.getPath());
			statement.setInt(3, folder.getParentId());
			statement.setInt(4, folder.getFolderId());
			statement.executeUpdate();
			childrenUpdate(folder);
		} catch (Exception e) {
			LOG.error(e);
			e.printStackTrace();
		}
	}

	private void childrenUpdate(FolderPOJO parent) {
		/*
		 * update folders
		 */
		List<FolderPOJO> listOfChildrenFolders = getChildrenFolders(parent.getFolderId());
		for (FolderPOJO folder : listOfChildrenFolders) {
			String newPath = parent.getPath() + "/" + folder.getName();
			folder.setPath(newPath);
			updateInterior(folder);
		}
		/*
		 * update files
		 */
		List<FilePOJO> listOfChildrenFilesPOJO = fileDAO.getChildrenFiles(parent.getFolderId());
		for (FilePOJO file : listOfChildrenFilesPOJO) {
			String newPath = parent.getPath() + "/" + file.getName();
			file.setPath(newPath);
			fileDAO.update(file);
		}
	}

	private List<FolderPOJO> getChildrenFolders(Integer parentId) {
		List<FolderPOJO> folders = new ArrayList<>();
		try {
			PreparedStatement statement = connection
					.prepareStatement("SELECT folder_id,user_id,name,path FROM folders WHERE parent_id= ?");
			statement.setInt(1, parentId);
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				FolderPOJO folder = new FolderPOJO();
				folder.setFolderId(result.getInt("folder_id"));
				folder.setUserId(result.getInt("user_id"));
				folder.setPath(result.getString("path"));
				folder.setName(result.getString("name"));
				folder.setParentId(parentId);
				folders.add(folder);
			}
		} catch (Exception e) {
			LOG.error(e);
			e.printStackTrace();
		}
		return folders;
	}

	public void copyFolderInterior(FolderPOJO newFolder, FolderPOJO oldFolder) {
		createFolderInterior(newFolder);
		newFolder.setFolderId(getFolderByPath(newFolder.getPath()).getFolderId());

		List<FolderPOJO> childyFoldersOfOldFolder = getChildrenFolders(oldFolder.getFolderId());
		for (FolderPOJO oldChild : childyFoldersOfOldFolder) {
			FolderPOJO newChild = new FolderPOJO();
			newChild.setName(oldChild.getName());
			newChild.setParentId(newFolder.getFolderId());
			newChild.setPath(newFolder.getPath() + "/" + oldChild.getName());
			newChild.setUserId(oldChild.getUserId());

			copyFolderInterior(newChild, oldChild);
		}

		List<FilePOJO> oldFilesPOJO = fileDAO.getChildrenFiles(oldFolder.getFolderId());
		for (FilePOJO file : oldFilesPOJO) {
			String newPath = newFolder.getPath() + "/" + file.getName();
			file.setPath(newPath);
			file.setFolderId(newFolder.getFolderId());
			fileDAO.createFile(file);
		}
	}

	private FolderPOJO getFolderById(Integer id) {
		String path = getPathOfFolderById(id);
		return getFolderByPath(path);
	}

	public void deleteFolderInterior(Integer folderId) {
		LOG.info("delete folder with folderId = " + folderId);
		try {
			PreparedStatement statement = connection.prepareStatement("DELETE FROM folders WHERE folder_id=?");
			statement.setInt(1, folderId);
			statement.executeUpdate();
			deleteChildren(folderId);
			JdbcConnectUtil.close(statement);
		} catch (Exception e) {
			LOG.error(e);
		}
	}

	private void deleteChildren(Integer parentId) {
		List<FolderPOJO> listOfChildrenFolders = getChildrenFolders(parentId);
		for (FolderPOJO folder : listOfChildrenFolders) {
			deleteFolderInterior(folder.getFolderId());
		}
		/*
		 * Here we may delete childy files
		 */
	}

	@Override
	public List<FolderPOJO> getFoldersList(Integer userId) {
		createConnection();
		List<FolderPOJO> folders = new ArrayList<>();
		try {
			PreparedStatement statement = connection.prepareStatement(
					"SELECT folder_id,parent_id,name,path FROM folders WHERE user_id= ? ORDER BY parent_id ASC");
			statement.setInt(1, userId);
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				FolderPOJO folder = new FolderPOJO();
				folder.setFolderId(result.getInt("folder_id"));
				folder.setParentId(result.getInt("parent_id"));
				folder.setName(result.getString("name"));
				folder.setPath(result.getString("path"));
				folders.add(folder);
			}
			JdbcConnectUtil.close(result, statement, connection);
		} catch (Exception e) {
			LOG.error(e);
		}
		return folders;
	}

	@Override
	public FolderPOJO getFolderByPath(String path) {
		createConnection();
		FolderPOJO folder = null;
		try {
			PreparedStatement statement = connection
					.prepareStatement("SELECT folder_id,parent_id,name,path,user_id FROM folders WHERE path=?");
			statement.setString(1, path);
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				folder = new FolderPOJO();
				folder.setFolderId(result.getInt("folder_id"));
				folder.setParentId(result.getInt("parent_id"));
				folder.setName(result.getString("name"));
				folder.setPath(result.getString("path"));
				folder.setUserId(result.getInt("user_id"));
			}
			JdbcConnectUtil.close(result, statement, null);
		} catch (Exception e) {
			LOG.error(e);
		}
		return folder;
	}

	@Override
	public void createFolder(FolderPOJO folder) {
		/*
		 * FACADE PATTERN
		 */
		createConnection();
		createFolderInterior(folder);
		JdbcConnectUtil.close(connection);
	}

	@Override
	public void update(FolderPOJO folder) {
		/*
		 * FACADE PATTERN
		 */
		createConnection();
		updateInterior(folder);
		JdbcConnectUtil.close(connection);
	}

	@Override
	public void deleteFolder(Integer folderID) {
		/*
		 * FACADE PATTERN
		 */
		createConnection();
		deleteFolderInterior(folderID);
		JdbcConnectUtil.close(connection);
	}

	@Override
	public void copyFolder(FolderPOJO newFolder, FolderPOJO oldFolder) {
		/*
		 * FACADE PATTERN
		 */
		createConnection();
		copyFolderInterior(newFolder, oldFolder);
		JdbcConnectUtil.close(connection);
	}

}
