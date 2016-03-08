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
@Table(name = "users", uniqueConstraints = { 
        @UniqueConstraint(columnNames = "user_id"), 
        @UniqueConstraint(columnNames = "email") }) 
public class UserEntity implements Serializable {
	private static final long serialVersionUID = 1L;
 
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Column(name = "user_id", unique = true, nullable = true) 
    private Integer userId; 
 
    @Column(name = "email", unique = true, nullable = false, length = 100) 
    private String email; 
 
    @Column(name = "password", unique = false, nullable = false, length = 100) 
    private String password; 
 
    @Column(name = "role", unique = false, nullable = false, length = 100) 
    private String role; 
    
    public UserEntity(){
		
	}
	
	public UserEntity(Integer userId,String role,String password,String email){
		this.userId=userId;
		this.password=password;
		this.role=role;
		this.email=email;
	}
    
 
    public void setUserId(Integer userId){
		this.userId=userId;
	}
	
	public void setPassword(String password){
		this.password=password;
	}
	
	public void setRole(String role){
		this.role=role;
	}
	
	public void setEmail(String email){
		this.email=email;
	}
	
	public Integer getUserId(){
		return userId;
	}
	
	public String getPassword(){
		return password;
	}
	
	public String getRole(){
		return role;
	}
	
	public String getEmail(){
		return email;
	}
} 
