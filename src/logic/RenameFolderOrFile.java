package logic;

import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dao.FileDAO;
import dao.FolderDAO;
import dao.UserDAO;
import db.HibernateFileDAO;
import db.HibernateFolderDAO;
import db.JdbcFileDAO;
import db.JdbcFolderDAO;
import objs.FilePOJO;
import objs.FolderPOJO;
import util.DAOUtil;
import util.GetFromPropertyFile;

@Component
public class RenameFolderOrFile {
	FolderDAO folderDAO;
	FileDAO fileDAO;
	UserDAO userDAO;
	 
	@Autowired private
	DAOUtil dAOUtil;
	
	public static final Logger LOG=Logger.getLogger(RenameFolderOrFile.class);
	
	GetFromPropertyFile prop = new GetFromPropertyFile();
	
	public String doLogic(String fileData,String folderData,String newName,String email,String contextPath){
		
		folderDAO = (FolderDAO) dAOUtil.getDAO(DAOUtil.Constants.FOLDER_DAO);
		fileDAO = (FileDAO) dAOUtil.getDAO(DAOUtil.Constants.FILE_DAO);
		userDAO = (UserDAO) dAOUtil.getDAO(DAOUtil.Constants.USER_DAO);
		
		String pathToTheOSFiles = contextPath;
		
		String answerText="mainPage.message.error";
		if(fileData!=null){
			String[] array=fileData.split("&%&");
			String name = array[0];
			String path = pathToTheOSFiles+array[3];
			String pathFromData = array[3];
			int folderID = Integer.parseInt(array[1]);
			int fileID = Integer.parseInt(array[2]);
			String []newNameArray = pathFromData.split(name);
			String newPath="";
			if(newNameArray.length==1){
				newPath=newNameArray[0]+newName;
			}else{
				for(int i=0;i<newNameArray.length;i++){
					if(i==newNameArray.length-1){
						newPath=newPath+array[i]+newName;
					}else{
						newPath=newPath+array[i]+name;
					}
				}
			}
			String newPathForDB=newPath;
			newPath= pathToTheOSFiles+ newPath;
			try{
				if(path.equals(newPath)){
					answerText = "mainPage.message.error.rename.file";
				}else{
					FileUtils.moveFile(
						      FileUtils.getFile(path), 
						      FileUtils.getFile(newPath)
						      );
					FilePOJO file = new FilePOJO();
					file.setFileId(fileID);
					file.setFolderId(folderID);
					file.setUserId(userDAO.getUserIdByEmail(email));
					file.setName(newName);
					file.setPath(newPathForDB);
					fileDAO.update(file);
					answerText = "mainPage.message.sucsess.rename.file";
				}
			}catch(Exception e){
				LOG.error(e);
			}
			
		}else
			
		if(folderData!=null){
			String[] arrayData=folderData.split("&%&");
			String oldName = arrayData[0];
			String oldPath = arrayData[3];
			int folderID = Integer.parseInt(arrayData[2]);
			int parentID = Integer.parseInt(arrayData[1]);
			String []array = oldPath.split(oldName);
			String newPath="";
			if(array.length==1){
				newPath=array[0]+newName;
			}else{
				for(int i=0;i<array.length;i++){
					if(i==array.length-1){
						newPath=newPath+array[i]+newName;
					}else{
						newPath=newPath+array[i]+oldName;
					}
				}
			}
			String newPathForDB=newPath;
			newPath= pathToTheOSFiles + newPath;
			oldPath = pathToTheOSFiles + oldPath;
			try{
				if(oldPath.equals(newPath)){
					answerText = "mainPage.message.error.rename.folder";
				}else{
					FileUtils.copyDirectory(
						      FileUtils.getFile(oldPath), 
						      FileUtils.getFile(newPath)
						      );
					try{
						FileUtils.deleteDirectory(FileUtils.getFile(oldPath));
					}catch(Exception e){
						forceDelete(oldPath);
						LOG.error(e);
					}
					FolderPOJO folder = new FolderPOJO();
					folder.setName(newName);
					folder.setPath(newPathForDB);
					folder.setParentId(parentID);
					folder.setFolderId(folderID);
					folder.setUserId(userDAO.getUserIdByEmail(email));
					folderDAO.update(folder);
					answerText = "mainPage.message.sucsess.rename.folder";
				}
			}catch(Exception e){
				LOG.error(e);
			}
		}
		return answerText;
	}
	
	private static void forceDelete(String oldPath){
		try{
			FileUtils.forceDelete(FileUtils.getFile(oldPath));
		}catch(Exception e){
			try {
				Thread.sleep(300);
			} catch (InterruptedException e1) {
				LOG.error(e1);
			}
			forceDelete(oldPath);
		}
	}
	
}
