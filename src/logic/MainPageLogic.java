package logic;

import java.io.File;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.core.ApplicationContext;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import dao.FileDAO;
import dao.FolderDAO;
import dao.UserDAO;
import db.HibernateFolderDAO;
import db.HibernateUserDAO;
import db.JdbcFolderDAO;
import db.JdbcUserDAO;
import objs.FolderPOJO;
import objs.UserPOJO;
import util.DAOUtil;
import util.GetFromPropertyFile;

@Component
public class MainPageLogic {
	public static final Logger LOG=Logger.getLogger(MainPageLogic.class);
	static GetFromPropertyFile prop = new GetFromPropertyFile();
	
	private UserDAO userDAO;
	private FolderDAO folderDAO;
	 
	@Autowired private
	DAOUtil dAOUtil;
	
	public  String[] doLogic(String regEmail,String regPass, String contextPath){
		
		folderDAO = (FolderDAO) dAOUtil.getDAO(DAOUtil.Constants.FOLDER_DAO);
		userDAO = (UserDAO) dAOUtil.getDAO(DAOUtil.Constants.USER_DAO);
		
		String code;
		String success;
		
		UserPOJO userPOJO = new UserPOJO();
		userPOJO.setEmail(regEmail);
		userPOJO.setPassword(regPass);
		userPOJO.setRole("ROLE_USER");
		
		boolean userDoesNotExist = !userDAO.userExists(userPOJO);
		LOG.info("userDoesNotExist" + userDoesNotExist);
		if (userDoesNotExist) {
			try{
				userDAO.registerNewUser(userPOJO);
				createRootFolder(regEmail, contextPath);
			} catch (Exception e) {
				LOG.error(e);
			}
			code = "mainPage.message.sucsess.reg";
			success = "1";
			LOG.info(code);
		} else {
			code = "homePage.message.error.reg";
			LOG.info(code);
			success = "0";
		}
		
		String[] answerArray = new String[]{success,code};
		return answerArray;
	}
	
	private  void createRootFolder(String email,String contextPath) {
		FolderPOJO folder = new FolderPOJO();
		folder.setName(email);
		folder.setPath(email);
		folder.setParentId(0);
		folder.setUserId(userDAO.getUserIdByEmail(email));
        folderDAO.createFolder(folder);
        
		//creating folder on server
        String pathToTheOSFiles="";
		try {
			pathToTheOSFiles = prop.getPath();
		} catch (IOException e1) {
			LOG.error(e1);
		}
		String savePath = pathToTheOSFiles+email;
        File fileSaveDir = new File(savePath);
        if (!fileSaveDir.exists()) {
            fileSaveDir.mkdir();
        }
        File testFile = new File(contextPath+email);
        if (!testFile.exists()) {
        	boolean answer = testFile.mkdir();
        	LOG.error("testFile "+answer);
        }
	}
}
