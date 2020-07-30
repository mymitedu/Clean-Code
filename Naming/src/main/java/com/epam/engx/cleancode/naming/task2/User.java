package com.epam.engx.cleancode.naming.task2;

import java.util.Arrays;
import java.util.Date;

public class User {

	private Date dateOfBirth;

	private String userName;

	private boolean isAdmin;

	private User[] userArray;

	private int rating;

	public User(String userName) {
		super();
		this.userName = userName;
	}

	@Override
	public String toString() {
		return "User [Date Of Birth=" + dateOfBirth + ", User Name=" + userName + ", IsAdmin=" + isAdmin + ", userArray="
				+ Arrays.toString(userArray) + ", Rating=" + rating + "]";
	}

}
