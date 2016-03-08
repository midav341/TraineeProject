package logic;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dao.FileDAO;
import dao.FolderDAO;
import util.DAOUtil;
import util.GetFromPropertyFile;

@Component
public class DeleteFolderOrFile {
	public static final Logger LOG=Logger.getLogger(MoveFolderOrFile.class);
	
	GetFromPropertyFile prop = new GetFromPropertyFile();
	FolderDAO folderDAO;
	FileDAO fileDAO;
	 
	@Autowired private
	DAOUtil dAOUtil;

	public  String doLogic(String fileData,String folderData,String contextPath){
		
		folderDAO = (FolderDAO) dAOUtil.getDAO(DAOUtil.Constants.FOLDER_DAO);
		fileDAO = (FileDAO) dAOUtil.getDAO(DAOUtil.Constants.FILE_DAO);
		
		String pathToTheOSFiles = contextPath;
		
		String answerText="mainPage.message.error";
		if(fileData!=null){
			String[] array=fileData.split("&%&");
			String path = pathToTheOSFiles+array[3];
			int fileID = Integer.parseInt(array[2]);
			
			File file = new File(path);
			try{
				file.delete();
				fileDAO.deleteFile(fileID);
				answerText = "mainPage.message.sucsess.delete.file";
			}catch(Exception e){
				LOG.error(e);
			}
			
		}else{
			if(folderData!=null){
				
				String[] array=folderData.split("&%&");
				String path = pathToTheOSFiles+array[3];
				int folderID = Integer.parseInt(array[2]);
				
				File folder = new File(path);
				try{
					FileUtils.deleteDirectory(folder);
					folderDAO.deleteFolder(folderID);
					answerText = "mainPage.message.sucsess.delete.folder";
				}catch(Exception e){
					LOG.error(e);
				}
			}
		}
		return answerText;
	}
}
