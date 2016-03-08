package logic;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dao.FileDAO;
import dao.FolderDAO;
import dao.UserDAO;
import db.HibernateFolderDAO;
import db.HibernateUserDAO;
import db.JdbcFolderDAO;
import db.JdbcUserDAO;
import objs.FolderPOJO;
import util.DAOUtil;
import util.GetFromPropertyFile;

@Component
public class CreateFolder {
	public static final Logger LOG=Logger.getLogger(CreateFolder.class);
	
	FolderDAO folderDAO;
	GetFromPropertyFile prop = new GetFromPropertyFile();
	 
	@Autowired private
	DAOUtil dAOUtil;
	 
	public  String doLogic(String folderName,String parentPath,String email,String contextPath){
		
		folderDAO = (FolderDAO) dAOUtil.getDAO(DAOUtil.Constants.FOLDER_DAO);
		
		String path=parentPath+"/"+folderName;
		String savePath = contextPath+path;
		
        File fileSaveDir = new File(savePath);
        
        String answerText = "mainPage.message.sucsess.create";
        
        if (!fileSaveDir.exists()) {
            fileSaveDir.mkdir();
            
            FolderPOJO folder = folderDAO.getFolderByPath(parentPath);
			folder.setName(folderName);
			folder.setPath(path);
			folder.setParentId(folder.getFolderId());
			folder.setFolderId(null);
            folderDAO.createFolder(folder);
        }else{
        	answerText = "mainPage.message.error.create";
        }
		LOG.debug(answerText);
		return answerText;
	}
}
