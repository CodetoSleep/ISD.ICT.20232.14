package entity.user;

public class User {
	private int userId;
    private String username;
    private String password;
    private String roleName;


    public User() {
    }
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public User(int userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }
    public User(int userId, String username, String password, String roleName) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.setRoleName(roleName);
    }
    // Getters and setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

}