package entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.OptimisticLockType;

@Entity 
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.ALL, dynamicUpdate=true) 
@Table(name = "files", uniqueConstraints = { 
        @UniqueConstraint(columnNames = "file_id"), 
        @UniqueConstraint(columnNames = "path") }) 
public class FileEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	@Column(name = "file_id", unique = true, nullable = false) 
	private Integer fileId;
	
	@Column(name = "folder_id", unique = false, nullable = false, length = 100) 
	private Integer folderId;
	
	@Column(name = "user_id", unique = false, nullable = false, length = 100) 
	private Integer userId;
	
	@Column(name = "path", unique = true, nullable = false) 
	private String path;
	
	@Column(name = "name", unique = false, nullable = false, length = 100)
	private String name;
	
	public FileEntity(){
	}
	
	public FileEntity(Integer fileId,Integer folderId,Integer userId,String path,String name){
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
	
	public Integer getUserId(){
		return userId;
	}
	
	public Integer getFolderId(){
		return folderId;
	}
	
	public Integer getFileId(){
		return fileId;
	}
	
	public String getPath(){
		return path;
	}
	
	public String getName(){
		return name;
	}
}
