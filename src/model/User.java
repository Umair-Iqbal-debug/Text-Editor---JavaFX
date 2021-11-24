package model;

import java.io.Serializable;
import java.util.Objects;

public class User implements Comparable<User>, Serializable {
	
	private String username;
	private String password;
	
	
	public User(String username, String password) {
		this.username = username.toLowerCase();
		this.password = password;
	}
	
	protected User(String username) {
		this.username = username;
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
		
		/*
		 * PASSWORD VALIDATION OVER HERE
		 * */
		this.password = password;
	}
	
	@Override
    public int hashCode() {
        return Objects.hash(username.toLowerCase());
    }

	
	
   @Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equalsIgnoreCase(other.username))
			return false;
		return true;
	}



	@Override
	public int compareTo(User o) {
		
		return this.username.compareToIgnoreCase(o.username);
	}

}
