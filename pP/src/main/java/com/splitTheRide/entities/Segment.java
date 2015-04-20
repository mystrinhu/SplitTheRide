package com.splitTheRide.entities;

public class Segment {

	private int id;
	private String name;
	private int distance;
	private double cost;
	
	public Segment(int id, String name, int distance, double cost){
		
		this.id = id;
		this.name = name;
		this.distance = distance;
		this.cost = cost;
	}
	
	public String getName(){
		
		return this.name;
	}
	
	public int getId(){
		
		return this.id;
	}
	
	public int getDistance(){
		
		return this.distance;
	}
	
	public double getCost(){
		
		return this.cost;
	}
}
