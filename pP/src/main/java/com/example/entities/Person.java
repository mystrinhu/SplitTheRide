package com.example.entities;

public class Person {

	private int id;
	private String name;
	private String short_name;
    private int usual_route;
	
	public Person(int id, String name, String short_name, int usual_route){
		
		this.id = id;
		this.name = name;
		this.short_name = short_name;
        this.usual_route = usual_route;
		
	}

    public Person(int id, String name, String short_name){

        this.id = id;
        this.name = name;
        this.short_name = short_name;
    }
	
	
	public int getId(){
		
		return this.id;
	}
	
	public String getName(){
		
		return this.name;
	}
	
	public String getShort(){
		
		return this.short_name;
	}

    public int getUsual_route(){

        return this.usual_route;
    }
	
	public String toString(){
		
		return this.name;
	}
}
