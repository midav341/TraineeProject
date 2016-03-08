package util;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import dao.DAO;
import dao.FileDAO;
import dao.FolderDAO;
import dao.UserDAO;
import db.HibernateFileDAO;
import db.HibernateFolderDAO;
import db.HibernateUserDAO;
import db.JdbcFileDAO;
import db.JdbcFolderDAO;
import db.JdbcUserDAO;
import db.SpringJdbcFileDAO;
import db.SpringJdbcFolderDAO;
import db.SpringJdbcUserDAO;

@Service
public class DAOUtil {
	public static final Logger LOG=Logger.getLogger(DAOUtil.class);
	
	@Autowired private
	UserDAO springJdbcUserDAO;
	
	@Autowired private
	FileDAO springJdbcFileDAO;
	
	@Autowired private
	FolderDAO springJdbcFolderDAO;
	
	static GetFromPropertyFile prop = new GetFromPropertyFile();
	
	String typeDAO;
	
	public enum Constants {
		USER_DAO,FILE_DAO,FOLDER_DAO
	}
	
	public enum DAOType {
		hib,jdbc,sjdbc
	}
	
	public DAO getDAO(Constants whichDAO){
		if(typeDAO==null){
			try {
				typeDAO = prop.getDAOType();
				LOG.info("DAOType = "+typeDAO);
			} catch (IOException e) {
				typeDAO = DAOType.jdbc.name();
				LOG.error(e);
			}
		}
		switch (whichDAO) {
	      case USER_DAO:
	    	  if(typeDAO.equals(DAOType.hib.name())){
	    		  return new HibernateUserDAO();
	    	  }else if(typeDAO.equals(DAOType.jdbc.name())){
	    		  return new JdbcUserDAO();
	    	  }else if(typeDAO.equals(DAOType.sjdbc.name())){
	    		  return springJdbcUserDAO;
	    	  }
	      case FILE_DAO:
	    	  if(typeDAO.equals(DAOType.hib.name())){
	    		  return new HibernateFileDAO();
	    	  }else if(typeDAO.equals(DAOType.jdbc.name())){
	    		  return new JdbcFileDAO();
	    	  }else if(typeDAO.equals(DAOType.sjdbc.name())){
	    		  return springJdbcFileDAO;
	    	  }
	      case FOLDER_DAO: 
	    	  if(typeDAO.equals(DAOType.hib.name())){
	    		  return new HibernateFolderDAO();
	    	  }else if(typeDAO.equals(DAOType.jdbc.name())){
	    		  return new JdbcFolderDAO();
	    	  }else if(typeDAO.equals(DAOType.sjdbc.name())){
	    		  return springJdbcFolderDAO;
	    	  }
	      default:
	    	  return null;
	    }
	}
}
