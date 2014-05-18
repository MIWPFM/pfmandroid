package com.miwpfm.weplay.model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.util.Base64;

public class User {

	private String username;
	private String password;
	private String plainPassword;
	private String salt;
	
	
	
	public User(String username, String plainPassword) {
		super();
		this.username = username;
		this.plainPassword = plainPassword;
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
	public String getPlainPassword() {
		return plainPassword;
	}
	public void setPlainPassword(String plainPassword) {
		this.plainPassword = plainPassword;
	}
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	
	public boolean hashPassword() {
		String hash = "";
		try {
			//Log.d("AuthProvider", "start hashing password...");
			String salted = null;
			if(salt == null || "".equals(salt)) {
				salted = plainPassword;
			} else {
				salted = plainPassword + "{" + salt + "}";
			}
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			byte sha[] = md.digest(salted.getBytes());
			for(int i = 1; i < 5000; i++) {
				byte c[] = new byte[sha.length + salted.getBytes().length];
				System.arraycopy(sha, 0, c, 0, sha.length);
				System.arraycopy(salted.getBytes(), 0, c, sha.length, salted.getBytes().length);
				sha = md.digest(c);
			}
			hash = new String(Base64.encode(sha,Base64.NO_WRAP));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			//do something with this exception
		}
		//Log.d("AuthProvider", "hashing password is done!");
		this.password= hash;
		return true;
	}

}
