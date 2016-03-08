package dao;

import org.springframework.stereotype.Repository;

import objs.UserPOJO;

@Repository
public interface UserDAO extends DAO {
	public void registerNewUser(UserPOJO userPOJO);
	public boolean userExists(UserPOJO userPOJO);
	public boolean loginDataCorrect(UserPOJO userPOJO);
	public Integer getUserIdByEmail(String email);
}