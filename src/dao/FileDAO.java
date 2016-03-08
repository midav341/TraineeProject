package dao;

import java.util.List;

import objs.FilePOJO;

public interface FileDAO extends DAO {
	public  void createFile(FilePOJO file);
	public void deleteFile(Integer fileID);
	public void update(FilePOJO file);
	public List<FilePOJO> getFilesList(Integer userID);
	public List<FilePOJO> getChildrenFiles(Integer folderID);
}