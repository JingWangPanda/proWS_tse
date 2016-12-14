package com.meetup.model;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="groupModel")
@XmlAccessorType(XmlAccessType.FIELD)
public class GroupModel {
	
	private int ID;
	private String name;
	private String description;
	private String admin;
	private ArrayList<String> members = new ArrayList<>();
	
	
	
	public GroupModel(String name, String description, String admin, ArrayList<String> members) {
		super();
		this.name = name;
		this.description = description;
		this.admin = admin;
		this.members = members;
	}
	
	public GroupModel() {
		super();
	}

	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getAdmin() {
		return admin;
	}
	public void setAdmin(String admin) {
		this.admin = admin;
	}
	public ArrayList<String> getMembers() {
		return members;
	}
	public void setMembers(ArrayList<String> members) {
		this.members = members;
	} 

}
