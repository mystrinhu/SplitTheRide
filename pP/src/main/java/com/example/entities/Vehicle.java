package com.example.entities;

public class Vehicle {

	private int id;
	private String name;
	private double consumption;
	private int person_id;
	
	public Vehicle(int id, String name, double c, int person){
		
		this.id = id;
		this.name = name;
		this.consumption = c;
		this.person_id = person;
		
	}
	
	
	public int getId(){
		
		return this.id;
	}
	
	public String getName(){
		
		return this.name;
	}
	
	public double getConsumption(){
		
		return this.consumption;
	}
	
	public int getPersonID(){
		
		return this.person_id;
	}
}
