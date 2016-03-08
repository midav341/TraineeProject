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
public class MoveFolderOrFile {
	public static final Logger LOG = Logger.getLogger(MoveFolderOrFile.class);

	FolderDAO folderDAO;
	FileDAO fileDAO;

	@Autowired
	private DAOUtil dAOUtil;

	GetFromPropertyFile prop = new GetFromPropertyFile();

	public String doLogic(String fileData, String folderData, String parentPath, String contextPath) {

		folderDAO = (FolderDAO) dAOUtil.getDAO(DAOUtil.Constants.FOLDER_DAO);
		fileDAO = (FileDAO) dAOUtil.getDAO(DAOUtil.Constants.FILE_DAO);

		String pathToTheOSFiles = contextPath;

		String answerText = "mainPage.message.error";
		if (fileData != null) {
			String[] arrayData = fileData.split("&%&");
			String fileName = arrayData[0];
			String oldFilePath = arrayData[3];
			int fileID = Integer.parseInt(arrayData[2]);
			String newFilePath = parentPath + "/" + fileName;
			String newFilePathForOS = pathToTheOSFiles + newFilePath;
			String oldFilePathForOS = pathToTheOSFiles + oldFilePath;

			try {
				if (oldFilePath.equals(newFilePath)) {
					answerText = "mainPage.message.error.move.file";
				} else {
					FileUtils.moveFile(FileUtils.getFile(oldFilePathForOS), FileUtils.getFile(newFilePathForOS));
					FilePOJO file = new FilePOJO();
					FolderPOJO folder = folderDAO.getFolderByPath(parentPath);
					file.setFileId(fileID);
					file.setFolderId(folder.getFolderId());
					file.setUserId(folder.getUserId());
					file.setName(fileName);
					file.setPath(newFilePath);
					fileDAO.update(file);
					answerText = "mainPage.message.sucsess.move.file";
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
				String newFolderPath = parentPath + "/" + folderName;
				String newFolderPathForOS = pathToTheOSFiles + newFolderPath;
				String oldFolderPathForOS = pathToTheOSFiles + oldFolderPath;

				try {
					FileUtils.copyDirectory(FileUtils.getFile(oldFolderPathForOS),
							FileUtils.getFile(newFolderPathForOS));
					try {
						FileUtils.deleteDirectory(FileUtils.getFile(oldFolderPathForOS));
					} catch (Exception e) {
						forceDelete(oldFolderPathForOS);
						LOG.error(e);
					}
					FolderPOJO folder = folderDAO.getFolderByPath(parentPath);
					folder.setName(folderName);
					folder.setPath(newFolderPath);
					folder.setParentId(folder.getFolderId());
					folder.setFolderId(folderID);
					folderDAO.update(folder);
					answerText = "mainPage.message.sucsess.move.folder";
				} catch (Exception e) {
					LOG.error(e);
				}
			}
		}
		return answerText;
	}

	private static void forceDelete(String oldPath) {
		try {
			FileUtils.forceDelete(FileUtils.getFile(oldPath));
		} catch (Exception e) {
			try {
				Thread.sleep(300);
			} catch (InterruptedException e1) {
				LOG.error(e1);
			}
			forceDelete(oldPath);
		}
	}
}
