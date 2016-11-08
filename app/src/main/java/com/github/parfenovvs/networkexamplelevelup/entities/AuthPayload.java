package com.github.parfenovvs.networkexamplelevelup.entities;



public class AuthPayload {
	private String name;
	private String hash;

	public AuthPayload(String name, String hash) {
		this.name = name;
		this.hash = hash;
	}

}
