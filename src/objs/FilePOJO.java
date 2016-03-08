package objs;

import java.io.Serializable;

public class FilePOJO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer folderId;
	private Integer fileId;
	private Integer userId;
	private String path;
	private String name;
	
	public FilePOJO(){
		
	}
	
	public FilePOJO(Integer fileId,Integer folderId,Integer userId,String path,String name){
		this.folderId=folderId;
		this.fileId=fileId;
		this.userId=userId;
		this.path=path;
		this.name=name;
	}
	
	public void setUserId(Integer userId){
		this.userId=userId;
	}
	
	public void setFolderId(Integer folderId){
		this.folderId=folderId;
	}
	
	public void setFileId(Integer fileId){
		this.fileId=fileId;
	}
	
	public void setPath(String path){
		this.path=path;
	}
	
	public void setName(String name){
		this.name=name;
	}
	
	public Integer getFolderId(){
		return folderId;
	}
	
	public Integer getFileId(){
		return fileId;
	}
	
	public Integer getUserId(){
		return userId;
	}
	
	public String getPath(){
		return path;
	}
	
	public String getName(){
		return name;
	}
}
