package logic;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

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
public class UploadFile {
	public static final Logger LOG = Logger.getLogger(UploadFile.class);
	FileDAO fileDAO;
	FolderDAO folderDAO;

	@Autowired
	private DAOUtil dAOUtil;

	GetFromPropertyFile prop = new GetFromPropertyFile();

	public String doLogic(String email, String parentPath, Part requestPart, String contextPath) {

		fileDAO = (FileDAO) dAOUtil.getDAO(DAOUtil.Constants.FILE_DAO);
		folderDAO = (FolderDAO) dAOUtil.getDAO(DAOUtil.Constants.FOLDER_DAO);
		// constructs path of the directory to save uploaded file
		String savePath = contextPath + parentPath;

		// creates the save directory if it does not exists
		File fileSaveDir = new File(savePath);
		if (!fileSaveDir.exists()) {
			fileSaveDir.mkdir();
		}
		String answerText = "mainPage.message.error";
		try {
			String fileName = extractFileName(requestPart);
			if (new File(savePath + File.separator + fileName).exists()) {
				answerText = "mainPage.message.error.upload";
			} else {
				FilePOJO file = new FilePOJO();
				FolderPOJO folder = folderDAO.getFolderByPath(parentPath);
				file.setFolderId(folder.getFolderId());
				file.setUserId(folder.getUserId());
				file.setName(fileName);
				file.setPath(parentPath + "/" + fileName);
				fileDAO.createFile(file);
				answerText = "mainPage.message.sucsess.rename.upload";
			}

			requestPart.write(savePath + File.separator + fileName);
		} catch (Exception e) {
			LOG.error(e);
		}
		return answerText;
	}

	private static String extractFileName(Part part) {
		String contentDisp = part.getHeader("content-disposition");
		String[] items = contentDisp.split(";");
		for (String s : items) {
			if (s.trim().startsWith("filename")) {
				return s.substring(s.indexOf("=") + 2, s.length() - 1);
			}
		}
		return "";
	}
}
