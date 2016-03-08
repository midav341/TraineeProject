package objs;

public class UserPOJO {
	private Integer userId;
	private String password;
	private String role;
	private String email;
	
	public UserPOJO(){
		
	}
	
	public UserPOJO(Integer userId,String role,String password,String email){
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
	
	public String toString() {
		return "UserPOJO [userId=" + userId + ", password=" + password + ","
				+ " role=" + role + ", email=" + email + "]";
	}
}
