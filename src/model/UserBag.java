package model;

import java.io.Serializable;
import java.util.TreeMap;

import utils.BackUpRestoreTools;

public class UserBag implements Serializable {
	
	private TreeMap<String,User> bag;
	
	private User loggedInUser;
	
	
	public UserBag() {
		bag = new TreeMap<>();
	}
	
	public UserBag(User... users) {
		for(User user : users)
			addUser(user);
	}
	
	public boolean isLoggedIn() {
		return loggedInUser != null;
	}
	
	public boolean removeUser(User user) {
		
		String username = user.getUsername();
		if(isUsernameTaken(username)) {
			bag.remove(username);
			return true;
		}
		
		return false;

	}
	
	public boolean addUser(User user) {
		
		
		String username = user.getUsername();
		if(isUsernameTaken(username)|| !Password.validatePassword(user.getPassword())) return false;
		bag.put(username, user);
		setLoggedInUser(user);
		
		return true;
	}
	
	public boolean isUsernameTaken(String username) {
		return bag.containsKey(username.toLowerCase());
	}
	
	public boolean login(String username,String password) {
		
		User user = auth(username,password);
		
		if(user != null) {
			setLoggedInUser(user);
			return true;
		}
		
		return false;
	}
	
	public void logout() {
		loggedInUser = null;
	}
	
	private void setLoggedInUser(User user) {
		loggedInUser = user;
	}
	
	public User getLoggedInUser() {
		return loggedInUser;
	}
	
	private User auth(String username, String password) {
		
		User user = bag.get(username.toLowerCase());
		
		return (user != null && user.getPassword().equals(password)) ? user : null;

	}
	
	public static void main(String[] args) {
		
//		User user = new User("Umair","Allah@123");
//		UserBag bag = new UserBag();
//		bag.addUser(user);
//		
//		BackUpRestoreTools.backupUserBag(bag);
		
		UserBag bag = BackUpRestoreTools.restoreUserBag();
		System.out.println(bag.login("UmAir", "Allah@123"));
		
	}

	public boolean addUser(String username, String password) {
		
		return addUser(new User(username.toLowerCase(),password));
		 
	}
	
	
	
}
