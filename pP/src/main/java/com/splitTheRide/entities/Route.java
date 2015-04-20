package com.splitTheRide.entities;

public class Route {

	private int id;
	private String name;
	
	public Route(int id, String name){
		
		this.id = id;
		this.name = name;
	}
	
	public int getID(){
		
		return this.id;
	}
	
	public String getName(){
		
		return this.name;
	}
	
	public String toString(){
		
		return this.name;
	}
	
}
