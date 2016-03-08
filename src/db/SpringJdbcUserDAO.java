package db;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import dao.UserDAO;
import objs.UserPOJO;

public class SpringJdbcUserDAO extends JdbcDaoSupport implements UserDAO{
	
	public final Logger LOG=Logger.getLogger(SpringJdbcUserDAO.class);
	
	public void registerNewUser(UserPOJO user) {
		insert(user);
	}

	public int insert(UserPOJO user) {
		return getJdbcTemplate().update(
				"insert into users(email, password) values (?,?)",
				new Object[] { user.getEmail(), user.getPassword()});
	}

	public boolean userExists(UserPOJO userPOJO) {
		String query = "select * from users where email =?";
		Object[] object =new Object[] {userPOJO.getEmail()};
		List<UserPOJO> list = select(query,object);
		if(list.isEmpty()){
			return false;
		}else{
			return true;
		}
	}
	
	public boolean loginDataCorrect(UserPOJO userPOJO) {
		String query = "select * from users where password=? and email =?";
		Object[] object =new Object[] {userPOJO.getPassword(),userPOJO.getEmail()};
		List<UserPOJO> list = select(query,object);
		if(list.isEmpty()){
			return false;
		}else{
			return true;
		}
	}
	
	public Integer getUserIdByEmail(String email) {
		String query = "select * from users where email =?";
		Object[] object =new Object[] {email};
		List<UserPOJO> list = select(query,object);
		if(list.isEmpty()){
			return null;
		}else{
			return list.get(0).getUserId();
		}
	}
	
	public List<UserPOJO> select(String query, Object[] object) {
		return getJdbcTemplate().query(query, object, new BeanPropertyRowMapper<UserPOJO>(UserPOJO.class));
	}

}
