package db;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import dao.FileDAO;
import dao.FolderDAO;
import objs.FilePOJO;
import objs.FolderPOJO;

@Repository
public class SpringJdbcFolderDAO extends JdbcDaoSupport implements FolderDAO {

	@Autowired
	private FileDAO fileDAO;

	public final Logger LOG = Logger.getLogger(SpringJdbcFolderDAO.class);

	public int update(String query, Object[] object) {
		return getJdbcTemplate().update(query, object);
	}

	public List<FolderPOJO> select(String query, Object[] object) {
		return getJdbcTemplate().query(query, object, new BeanPropertyRowMapper<FolderPOJO>(FolderPOJO.class));
	}

	public void createFolder(FolderPOJO folder) {
		String query = "INSERT INTO folders (user_id, parent_id, path, name) VALUES (?,?,?,?)";
		Object[] object = new Object[] { folder.getUserId(), folder.getParentId(), folder.getPath(), folder.getName() };
		update(query, object);
	}

	public String getPathOfFolderById(Integer id) {
		String query = "SELECT * FROM folders WHERE folder_id = ?";
		Object[] object = new Object[] { id };
		List<FolderPOJO> folders = select(query, object);
		String path = folders.get(0).getPath();
		return path;
	}

	public void update(FolderPOJO folder) {
		String query = "UPDATE folders SET name= ?, path= ?, parent_id=? WHERE folder_id= ? ";
		Object[] object = new Object[] { folder.getName(), folder.getPath(), folder.getParentId(),
				folder.getFolderId() };
		update(query, object);
		childrenUpdate(folder);
	}

	private void childrenUpdate(FolderPOJO parent) {
		/*
		 * update folders
		 */
		List<FolderPOJO> listOfChildrenFolders = getChildrenFolders(parent.getFolderId());
		for (FolderPOJO folder : listOfChildrenFolders) {
			String newPath = parent.getPath() + "/" + folder.getName();
			folder.setPath(newPath);
			update(folder);
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
		String query = "SELECT * FROM folders WHERE parent_id= ?";
		Object[] object = new Object[] { parentId };
		List<FolderPOJO> folders = select(query, object);
		return folders;
	}

	public void copyFolder(FolderPOJO newFolder, FolderPOJO oldFolder) {
		createFolder(newFolder);
		newFolder.setFolderId(getFolderByPath(newFolder.getPath()).getFolderId());

		List<FolderPOJO> childyFoldersOfOldFolder = getChildrenFolders(oldFolder.getFolderId());
		for (FolderPOJO oldChild : childyFoldersOfOldFolder) {
			FolderPOJO newChild = new FolderPOJO();
			newChild.setName(oldChild.getName());
			newChild.setParentId(newFolder.getFolderId());
			newChild.setPath(newFolder.getPath() + "/" + oldChild.getName());
			newChild.setUserId(oldChild.getUserId());

			copyFolder(newChild, oldChild);
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

	public void deleteFolder(Integer folderId) {
		String query = "DELETE FROM folders WHERE folder_id=?";
		Object[] object = new Object[] { folderId };
		update(query, object);
	}

	private void deleteChildren(Integer parentId) {
		List<FolderPOJO> listOfChildrenFolders = getChildrenFolders(parentId);
		for (FolderPOJO folder : listOfChildrenFolders) {
			deleteFolder(folder.getFolderId());
		}
		/*
		 * Here we may delete childy files
		 */
	}

	@Override
	public List<FolderPOJO> getFoldersList(Integer userId) {
		String query = "SELECT folder_id,parent_id,name,path FROM folders WHERE user_id= ? ORDER BY parent_id ASC";
		Object[] object = new Object[] { userId };
		List<FolderPOJO> folders = select(query, object);
		return folders;
	}

	@Override
	public FolderPOJO getFolderByPath(String path) {
		String query = "SELECT * FROM folders WHERE path=?";
		Object[] object = new Object[] { path };
		FolderPOJO folder = select(query, object).get(0);
		return folder;
	}

}
