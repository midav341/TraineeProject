package logic;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dao.FileDAO;
import dao.FolderDAO;
import dao.UserDAO;
import objs.FilePOJO;
import objs.FolderPOJO;
import util.DAOUtil;
import util.GetFromPropertyFile;

@Component
public class CopyFolderOrFile {
	FolderDAO folderDAO;
	FileDAO fileDAO;
	UserDAO userDAO;

	@Autowired
	private DAOUtil dAOUtil;

	public static final Logger LOG = Logger.getLogger(CopyFolderOrFile.class);

	GetFromPropertyFile prop = new GetFromPropertyFile();

	public String doLogic(String fileData, String folderData, String newName, String email, String contextPath) {

		folderDAO = (FolderDAO) dAOUtil.getDAO(DAOUtil.Constants.FOLDER_DAO);
		fileDAO = (FileDAO) dAOUtil.getDAO(DAOUtil.Constants.FILE_DAO);
		userDAO = (UserDAO) dAOUtil.getDAO(DAOUtil.Constants.USER_DAO);

		String pathToTheOSFiles = contextPath;

		String answerText = "mainPage.message.error";
		if (fileData != null) {
			String[] arrayData = fileData.split("&%&");
			int folderId = Integer.parseInt(arrayData[1]);
			String oldFilePath = arrayData[3];
			String[] oldFilePathArray = oldFilePath.split("/");
			String newFilePath = oldFilePathArray[0];
			String parentPath = oldFilePathArray[0];
			for (int i = 1; i < oldFilePathArray.length; i++) {
				if (i == oldFilePathArray.length - 1) {
					newFilePath = newFilePath + "/" + newName;
				} else {
					newFilePath = newFilePath + "/" + oldFilePathArray[i];
					parentPath = parentPath + "/" + oldFilePathArray[i];
				}
			}

			String newFilePathForOS = pathToTheOSFiles + newFilePath;
			String oldFilePathForOS = pathToTheOSFiles + oldFilePath;

			try {
				if (oldFilePath.equals(newFilePath)) {
					answerText = "mainPage.message.error.copy.file";
				} else {
					FileUtils.copyFile(FileUtils.getFile(oldFilePathForOS), FileUtils.getFile(newFilePathForOS));
					FilePOJO file = new FilePOJO();
					file.setFolderId(folderId);
					file.setName(newName);
					file.setPath(newFilePath);
					file.setUserId(userDAO.getUserIdByEmail(email));
					fileDAO.createFile(file);
					answerText = "mainPage.message.sucsess.copy.file";
				}
			} catch (Exception e) {
				LOG.error(e);
			}
		} else {
			if (folderData != null) {
				String[] arrayData = folderData.split("&%&");
				String folderName = arrayData[0];
				String oldFolderPath = arrayData[3];
				int folderID = Integer.parseInt(arrayData[2]);
				int parentID = Integer.parseInt(arrayData[1]);
				String[] oldFolderPathArray = oldFolderPath.split("/");
				String newFolderPath = oldFolderPathArray[0];
				String parentPath = oldFolderPathArray[0];
				for (int i = 1; i < oldFolderPathArray.length; i++) {
					if (i == oldFolderPathArray.length - 1) {
						newFolderPath = newFolderPath + "/" + newName;
					} else {
						newFolderPath = newFolderPath + "/" + oldFolderPathArray[i];
						parentPath = parentPath + "/" + oldFolderPathArray[i];
					}
				}

				String newFolderPathForOS = pathToTheOSFiles + newFolderPath;
				String oldFolderPathForOS = pathToTheOSFiles + oldFolderPath;

				try {
					if (oldFolderPath.equals(newFolderPath)) {
						answerText = "mainPage.message.error.copy.folder";
					} else {
						FileUtils.copyDirectory(FileUtils.getFile(oldFolderPathForOS),
								FileUtils.getFile(newFolderPathForOS));

						FolderPOJO oldFolder = new FolderPOJO();
						oldFolder.setFolderId(folderID);
						oldFolder.setName(folderName);
						oldFolder.setPath(oldFolderPath);
						oldFolder.setParentId(parentID);
						oldFolder.setUserId(userDAO.getUserIdByEmail(email));

						FolderPOJO newFolder = new FolderPOJO();
						newFolder.setName(newName);
						newFolder.setPath(newFolderPath);
						newFolder.setParentId(parentID);
						newFolder.setUserId(userDAO.getUserIdByEmail(email));

						folderDAO.copyFolder(newFolder, oldFolder);
						answerText = "mainPage.message.sucsess.copy.folder";
					}
				} catch (Exception e) {
					LOG.error(e);
				}
			}
		}
		return answerText;
	}
}
