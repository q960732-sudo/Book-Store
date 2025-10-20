package po;

public class User {
	private int userId;
    private String accountName; 
    private String displayName; 
    private String password;    
    private String role;
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	public User(String accountName, String displayName, String password, String role) {
		super();
		this.accountName = accountName;
		this.displayName = displayName;
		this.password = password;
		this.role = role;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}   
    
    
}
