package db;


import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import dao.FileDAO;
import objs.FilePOJO;

public class SpringJdbcFileDAO extends JdbcDaoSupport implements FileDAO{
	public static final Logger LOG=Logger.getLogger(SpringJdbcFileDAO.class);
	
	public int update(String query, Object[] object) {
		return getJdbcTemplate().update(query, object);
	}
	
	public List<FilePOJO> select(String query, Object[] object) {
		return getJdbcTemplate().query(query, object, new BeanPropertyRowMapper<FilePOJO>(FilePOJO.class));
	}
	
	public void createFile(FilePOJO file) {
		String query = "INSERT INTO files (user_id, folder_id, path, name) VALUES (?,?,?,?)";
		Object[] object = new Object[] {file.getUserId(), file.getFolderId(),file.getPath(),file.getName()};
		update(query,object);
	}
	
	public void update(FilePOJO file) {
		String query = "UPDATE files SET folder_id=?,user_id=?, name=?, path=? WHERE file_id=?";
		Object[] object =new Object[] {
				file.getFolderId(),file.getUserId(),
				file.getName(),file.getPath(),file.getFileId()
				};
		update(query,object);
	}

	public  void deleteFile(Integer fileId) {
		String query = "DELETE FROM files WHERE file_id=?";
		Object[] object =new Object[] {fileId};
		update(query,object);
	}
	
	public  List<FilePOJO> getFilesList(Integer userId){
		String query = "SELECT folder_id,file_id,name,path FROM files "
				+ "WHERE user_id=? ORDER BY folder_id ASC";
		Object[] object =new Object[] {userId};
		select(query,object);
		
		List<FilePOJO> files = select(query,object);
		return files;
	}

	@Override
	public List<FilePOJO> getChildrenFiles(Integer folderID) {
		String query = "SELECT folder_id,file_id,name,path,user_id FROM files WHERE folder_id=?";
		Object[] object =new Object[] {folderID};
		select(query,object);
		
		List<FilePOJO> files = select(query,object);
		return files;
	}
}
