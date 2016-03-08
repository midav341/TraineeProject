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
import entities.UserEntity;
import objs.FilePOJO;
import objs.FolderPOJO;
import util.ConvertUtil;

public class HibernateFolderDAO implements FolderDAO {
	FileDAO fileDAO;

	public static final Logger LOG = Logger.getLogger(HibernateFolderDAO.class);

	public void createFolder(FolderPOJO folder) {
		FolderEntity folderEntity = (FolderEntity) ConvertUtil.pojoToEntity(folder);
		create(folderEntity);
	}

	private void create(FolderEntity folder) {
		try {
			Session session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();

			session.save(folder);

			session.getTransaction().commit();
			HibernateUtil.shutdown();
		} catch (Exception e) {
			LOG.error(e);
		}
	}

	public FolderPOJO getFolderByPath(String path) {
		FolderEntity folderEntity = null;
		FolderPOJO folderPOJO = null;
		try {
			Session session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();

			Query query = session.createQuery("FROM entities.FolderEntity WHERE path= :path");
			LOG.info("setParameter path= " + path);
			query.setParameter("path", path);

			folderEntity = (FolderEntity) query.list().get(0);
			folderPOJO = (FolderPOJO) ConvertUtil.entityToPOJO(folderEntity);

			session.getTransaction().commit();
			HibernateUtil.shutdown();
		} catch (Exception e) {
			LOG.error(e);
		}
		LOG.info("folder.name = " + folderEntity.getName());
		return folderPOJO;
	}

	public FolderEntity getFolderById(Integer id) {
		FolderEntity folder = null;
		try {
			Session session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();

			Query query = session.createQuery("FROM entities.FolderEntity WHERE folder_id= :id");
			query.setParameter("id", id);

			folder = (FolderEntity) query.uniqueResult();

			session.getTransaction().commit();
			HibernateUtil.shutdown();
		} catch (Exception e) {
			LOG.error(e);
		}
		LOG.info("folder.name = " + folder.getName());
		return folder;
	}

	public String getPathOfFolderById(Integer id) {
		FolderEntity folder = getFolderById(id);
		String path = folder.getPath();
		return path;
	}

	public void update(FolderPOJO folder) {
		FolderEntity folderEntity = (FolderEntity) ConvertUtil.pojoToEntity(folder);
		innerUpdate(folderEntity);
		childrenUpdate(folder);
	}

	private void childrenUpdate(FolderPOJO parent) {

		/*
		 * update folders
		 */
		List<FolderEntity> listOfChildrenFolders = getChildrenFolders(parent.getFolderId());
		for (FolderEntity folder : listOfChildrenFolders) {
			String newPath = parent.getPath() + "/" + folder.getName();
			folder.setPath(newPath);
			FolderPOJO folderPOJO = (FolderPOJO) ConvertUtil.entityToPOJO(folder);
			update(folderPOJO);
		}

		/*
		 * update files
		 */
		fileDAO = new HibernateFileDAO();
		List<FilePOJO> listOfChildrenFilesPOJO = fileDAO.getChildrenFiles(parent.getFolderId());
		for (FilePOJO file : listOfChildrenFilesPOJO) {
			String newPath = parent.getPath() + "/" + file.getName();
			file.setPath(newPath);
			fileDAO.update(file);
		}
	}

	public void innerUpdate(FolderEntity folder) {
		try {
			Session session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();

			session.update(folder);

			session.getTransaction().commit();
			HibernateUtil.shutdown();
		} catch (Exception e) {
			LOG.error(e);
		}
	}

	private List<FolderEntity> getChildrenFolders(Integer parentId) {
		List<FolderEntity> listOfChildrenFolders = null;
		try {
			Session session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();

			Query query = session.createQuery("FROM entities.FolderEntity WHERE parent_id= :parentId");
			query.setParameter("parentId", parentId);

			listOfChildrenFolders = query.list();

			session.getTransaction().commit();
			HibernateUtil.shutdown();
		} catch (Exception e) {
			LOG.error(e);
		}
		LOG.info("listOfChildrenFolders.size = " + listOfChildrenFolders.size());
		return listOfChildrenFolders;
	}

	public void deleteFolder(Integer folderId) {
		FolderEntity folder = getFolderById(folderId);
		try {
			Session session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();

			session.delete(folder);

			session.getTransaction().commit();
			HibernateUtil.shutdown();

			deleteChildren(folder);
		} catch (Exception e) {
			LOG.error(e);
		}
	}

	private void deleteChildren(FolderEntity parent) {
		List<FolderEntity> listOfChildrenFolders = getChildrenFolders(parent.getFolderId());
		for (FolderEntity folder : listOfChildrenFolders) {
			deleteFolder(folder.getFolderId());
		}
		/*
		 * Here we may delete childy files
		 */
	}

	public void copyFolder(FolderPOJO newFolder, FolderPOJO oldFolder) {

		createFolder(newFolder);
		newFolder = getFolderByPath(newFolder.getPath());

		List<FolderEntity> childyFoldersOfOldFolder = getChildrenFolders(oldFolder.getFolderId());
		for (FolderEntity oldChild : childyFoldersOfOldFolder) {
			FolderEntity newChild = new FolderEntity();
			newChild.setName(oldChild.getName());
			newChild.setParentId(newFolder.getFolderId());
			newChild.setPath(newFolder.getPath() + "/" + oldChild.getName());
			newChild.setUserId(oldChild.getUserId());

			copyFolder((FolderPOJO) ConvertUtil.entityToPOJO(newChild),
					(FolderPOJO) ConvertUtil.entityToPOJO(oldChild));
		}

		fileDAO = new HibernateFileDAO();
		List<FilePOJO> oldFilesPOJO = fileDAO.getChildrenFiles(oldFolder.getFolderId());
		for (FilePOJO file : oldFilesPOJO) {
			String newPath = newFolder.getPath() + "/" + file.getName();
			file.setPath(newPath);
			file.setFolderId(newFolder.getFolderId());
			fileDAO.createFile(file);
		}

	}

	public List getFoldersList(Integer userId) {
		List<FolderEntity> folders = null;
		List<FolderPOJO> convertedFolders = null;

		try {
			Session session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();

			Query query = session.createQuery("FROM entities.FolderEntity WHERE user_id= :userId");
			query.setParameter("userId", userId);

			folders = (List<FolderEntity>) query.list();
			convertedFolders = (List<FolderPOJO>) ConvertUtil.entitiesToPOJOs(folders);

			session.getTransaction().commit();
			HibernateUtil.shutdown();
		} catch (Exception e) {
			LOG.error(e);
			e.printStackTrace();
		}
		return convertedFolders;
	}
}