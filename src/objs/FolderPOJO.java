package objs;

import java.io.Serializable;

public class FolderPOJO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer folderId;
	private Integer parentId;
	private Integer userId;
	private String path;
	private String name;
	
	public FolderPOJO(){
		
	}
	
	public FolderPOJO(Integer folderId,Integer parentId,Integer userId ,String path,String name){
		this.folderId=folderId;
		this.parentId=parentId;
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
	
	public void setParentId(Integer parentId){
		this.parentId=parentId;
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
	
	public Integer getUserId(){
		return userId;
	}
	
	public Integer getParentId(){
		return parentId;
	}
	
	public String getPath(){
		return path;
	}
	
	public String getName(){
		return name;
	}
	
}
