package db;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

import dao.FileDAO;
import dao.FolderDAO;
import dao.UserDAO;
import entities.FileEntity;
import entities.FolderEntity;
import objs.FilePOJO;
import objs.FolderPOJO;
import util.ConvertUtil;

public class HibernateFileDAO implements FileDAO {
	FolderDAO folderDAO;

	public static final Logger LOG = Logger.getLogger(HibernateFolderDAO.class);

	public void deleteFile(Integer fileId) {
		FileEntity file = getFileById(fileId);
		try {
			Session session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();

			session.delete(file);

			session.getTransaction().commit();
			HibernateUtil.shutdown();
		} catch (Exception e) {
			LOG.error(e);
		}
	}

	public void createFile(FilePOJO file) {
		FileEntity fileEntity = (FileEntity) ConvertUtil.pojoToEntity(file);
		create(fileEntity);
	}

	public List getFilesList(Integer userId) {
		List<FileEntity> files = null;
		List<FilePOJO> convertedFiles = null;

		try {
			Session session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();

			Query query = session.createQuery("FROM entities.FileEntity WHERE user_id= :userId");
			query.setParameter("userId", userId);

			files = query.list();
			convertedFiles = (List<FilePOJO>) ConvertUtil.entitiesToPOJOs(files);

			session.getTransaction().commit();
			HibernateUtil.shutdown();
		} catch (Exception e) {
			LOG.error(e);
			e.printStackTrace();
		}
		return convertedFiles;
	}

	public List<FilePOJO> getChildrenFiles(Integer parentId) {
		List<FileEntity> listOfChildrenFiles = null;
		try {
			Session session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();

			Query query = session.createQuery("FROM entities.FileEntity WHERE folder_id= :parentId");
			query.setParameter("parentId", parentId);

			listOfChildrenFiles = query.list();

			session.getTransaction().commit();
			HibernateUtil.shutdown();
		} catch (Exception e) {
			LOG.error(e);
		}
		LOG.info("listOfChildrenFiles.size = " + listOfChildrenFiles.size());
		return (List<FilePOJO>) ConvertUtil.entitiesToPOJOs(listOfChildrenFiles);
	}

	private FileEntity getFileByPath(String path) {
		FileEntity file = null;
		try {
			Session session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();

			Query query = session.createQuery("FROM entities.FileEntity WHERE path= :path");
			LOG.info("setParameter path= " + path);
			query.setParameter("path", path);

			file = (FileEntity) query.uniqueResult();

			session.getTransaction().commit();
			HibernateUtil.shutdown();
		} catch (Exception e) {
			LOG.error(e);
		}
		LOG.info("file.name = " + file.getName());
		return file;
	}

	private FileEntity getFileById(int fileId) {
		FileEntity file = null;
		try {
			Session session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();

			Query query = session.createQuery("FROM entities.FileEntity WHERE file_id= :fileId");
			query.setParameter("fileId", fileId);

			file = (FileEntity) query.uniqueResult();

			session.getTransaction().commit();
			HibernateUtil.shutdown();
		} catch (Exception e) {
			LOG.error(e);
		}
		return file;
	}

	private void create(FileEntity file) {
		try {
			Session session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();

			session.save(file);

			session.getTransaction().commit();
			HibernateUtil.shutdown();
		} catch (Exception e) {
			LOG.error(e);
		}
	}

	public void update(FilePOJO file) {
		FileEntity fileEntity = (FileEntity) ConvertUtil.pojoToEntity(file);
		try {
			Session session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();

			session.update(fileEntity);

			session.getTransaction().commit();
			HibernateUtil.shutdown();
		} catch (Exception e) {
			LOG.error(e);
		}
	}

}