package util;

import java.util.ArrayList;
import java.util.List;

import entities.FileEntity;
import entities.FolderEntity;
import entities.UserEntity;
import objs.FilePOJO;
import objs.FolderPOJO;
import objs.UserPOJO;

public class ConvertUtil {

	public static Object entityToPOJO (Object entity){
		Object pojo = null;
		if(entity instanceof FileEntity){
			pojo = new FilePOJO(
					((FileEntity) entity).getFileId(),((FileEntity) entity).getFolderId(),
					((FileEntity) entity).getUserId(),((FileEntity) entity).getPath(),
					((FileEntity) entity).getName()
					);
		}else if(entity instanceof FolderEntity){
			pojo = new FolderPOJO(
					((FolderEntity) entity).getFolderId(),((FolderEntity) entity).getParentId(),
					((FolderEntity) entity).getUserId() ,((FolderEntity) entity).getPath(),
					((FolderEntity) entity).getName()
					);
		}else if(entity instanceof UserEntity){
			pojo = new UserPOJO(
					((UserEntity) entity).getUserId(),((UserEntity) entity).getRole(),
					((UserEntity) entity).getPassword(),((UserEntity) entity).getEmail()
					);
		}
		return pojo;
	}
	
	public static Object pojoToEntity (Object pojo){
		Object entity = null;
		if(pojo instanceof FilePOJO){
			entity = new FileEntity(
					((FilePOJO) pojo).getFileId(),((FilePOJO) pojo).getFolderId(),
					((FilePOJO) pojo).getUserId(),((FilePOJO) pojo).getPath(),
					((FilePOJO) pojo).getName()
					);
		}else if(pojo instanceof FolderPOJO){
			entity = new FolderEntity(
					((FolderPOJO) pojo).getFolderId(),((FolderPOJO) pojo).getParentId(),
					((FolderPOJO) pojo).getUserId() ,((FolderPOJO) pojo).getPath(),
					((FolderPOJO) pojo).getName()
					);
		}else if(pojo instanceof UserPOJO){
			entity = new UserEntity(
					((UserPOJO) pojo).getUserId(),((UserPOJO) pojo).getRole(),
					((UserPOJO) pojo).getPassword(),((UserPOJO) pojo).getEmail()
					);
		}
		return entity;
	}
	
	public static List<?> entitiesToPOJOs (List<?> entities){
		List<Object> pojos = new ArrayList<>();
		for(Object entity : entities){
			Object pojo = entityToPOJO(entity);
			pojos.add(pojo);
		}
		return pojos;
	}
	
	public static List<?> pojosToEntities (List<?> pojos){
		List<Object> entities = new ArrayList<>();
		for(Object pojo : pojos){
			Object entity = pojoToEntity(pojo);
			entities.add(entity);
		}
		return entities;
	}
}