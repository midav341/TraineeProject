package logic;

import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dao.FileDAO;
import dao.FolderDAO;
import dao.UserDAO;
import objs.FilePOJO;
import objs.FolderPOJO;
import util.DAOUtil;

@Component
public class FoldersAndFilesTreeJSON {
	public static final Logger LOG=Logger.getLogger(FoldersAndFilesTreeJSON.class);
	
	FileDAO filesDAO;
	FolderDAO foldersDAO;
	UserDAO userDAO;
	
	@Autowired private
	DAOUtil dAOUtil;
	
	public JsonObject doLogic(Integer userId, String email){
		userDAO = (UserDAO) dAOUtil.getDAO(DAOUtil.Constants.USER_DAO);
		foldersDAO = (FolderDAO) dAOUtil.getDAO(DAOUtil.Constants.FOLDER_DAO);
		filesDAO = (FileDAO) dAOUtil.getDAO(DAOUtil.Constants.FILE_DAO);
		
		if(userId==null){
			userId = userDAO.getUserIdByEmail(email);
		}
		
		List<FolderPOJO> folders = foldersDAO.getFoldersList(userId);
		List<FilePOJO> files = filesDAO.getFilesList(userId);
		
		JsonObjectBuilder  jsonBuilder = Json.createObjectBuilder();
		JsonArrayBuilder  filesJsonArrayBuilder = Json.createArrayBuilder();
		JsonArrayBuilder  foldersJsonArrayBuilder = Json.createArrayBuilder();
		
		
		for(FilePOJO file : files){
			filesJsonArrayBuilder.add(Json.createObjectBuilder()
					.add("name",file.getName())
					.add("fileID",file.getFileId())
					.add("folderID",file.getFolderId())
					.add("path",file.getPath())
					);
		}
		
		for(FolderPOJO folder : folders){
			foldersJsonArrayBuilder.add(Json.createObjectBuilder()
					.add("name",folder.getName())
					.add("folderID",folder.getFolderId())
					.add("parentID",folder.getParentId())
					.add("path",folder.getPath())
					);
		}
		
		JsonArray filesJsonArray = filesJsonArrayBuilder.build();
		JsonArray foldersJsonArray = foldersJsonArrayBuilder.build();
		
		jsonBuilder.add("files", filesJsonArray.toString());
		jsonBuilder.add("folders", foldersJsonArray.toString());
		
		JsonObject json = jsonBuilder.build();
		
		LOG.info("filesJsonArray = "+ filesJsonArray.toString());
		LOG.info("foldersJsonArray = "+ foldersJsonArray.toString());
		LOG.info("json = "+ json.toString());
		
		return json;
	}
	
}
