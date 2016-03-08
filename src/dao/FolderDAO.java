package dao;

import java.util.List;

import objs.FolderPOJO;

public interface FolderDAO extends DAO {
	public void createFolder(FolderPOJO folder);
	public String getPathOfFolderById(Integer id);
	public void update(FolderPOJO folder);
	public void deleteFolder(Integer folderID);
	public void copyFolder(FolderPOJO newFolder,FolderPOJO oldFolder);
	public List<FolderPOJO> getFoldersList(Integer userID);
	public FolderPOJO getFolderByPath(String path);
}