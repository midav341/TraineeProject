package db;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

import dao.UserDAO;
import entities.UserEntity;
import objs.UserPOJO;
import util.ConvertUtil;

public class HibernateUserDAO implements UserDAO {
	public static final Logger LOG = Logger.getLogger(HibernateUserDAO.class);

	@Override
	public void registerNewUser(UserPOJO userPOJO) {
		try {
			Session session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();

			UserEntity emp = (UserEntity) ConvertUtil.pojoToEntity(userPOJO);

			session.save(emp);

			session.getTransaction().commit();
			HibernateUtil.shutdown();
		} catch (Exception e) {
			LOG.error(e);
		}
	}

	@Override
	public boolean loginDataCorrect(UserPOJO userPOJO) {
		boolean loginDataCorrect = false;
		try {
			Session session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();

			Query query = session.createQuery("FROM entities.UserEntity WHERE email= :email AND password= :password");
			query.setParameter("email", userPOJO.getEmail());
			query.setParameter("password", userPOJO.getPassword());
			List users = query.list();

			if (users.size() > 0) {
				loginDataCorrect = true;
			}

			session.getTransaction().commit();
			HibernateUtil.shutdown();
		} catch (Exception e) {
			LOG.error(e);
		}
		LOG.info("loginDataCorrect = " + loginDataCorrect);
		return loginDataCorrect;
	}

	@Override
	public boolean userExists(UserPOJO userPOJO) {
		boolean userExists = false;
		try {
			Session session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();

			Query query = session.createQuery("FROM entities.UserEntity WHERE email= :email");
			query.setParameter("email", userPOJO.getEmail());
			List users = query.list();

			if (users.size() > 0) {
				userExists = true;
			}

			session.getTransaction().commit();
			HibernateUtil.shutdown();
		} catch (Exception e) {
			LOG.error(e);
		}
		LOG.info("userExists = " + userExists);
		return userExists;
	}

	@Override
	public Integer getUserIdByEmail(String email) {
		Integer userId = null;
		try {
			Session session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();

			Query query = session.createQuery("FROM entities.UserEntity WHERE email= :email");
			query.setParameter("email", email);
			UserEntity user = (UserEntity) query.uniqueResult();

			userId = user.getUserId();

			session.getTransaction().commit();
			HibernateUtil.shutdown();
		} catch (Exception e) {
			LOG.error(e);
		}
		LOG.info("userId = " + userId);
		return userId;
	}

}