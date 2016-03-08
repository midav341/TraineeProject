package logic;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dao.UserDAO;
import util.DAOUtil;

@Component
public class GetUserIdByEmail {
	public static final Logger LOG=Logger.getLogger(GetUserIdByEmail.class);
	
	UserDAO userDAO;
	 
	@Autowired private
	DAOUtil dAOUtil;
	
	public Integer doLogic(String email){
		userDAO = (UserDAO) dAOUtil.getDAO(DAOUtil.Constants.USER_DAO);
		return userDAO.getUserIdByEmail(email);
	}

}
