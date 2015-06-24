package com.splitTheRide.entities;

public class Person {

	private int id;
	private String name;
    private int usual_route;

	public Person(int id, String name, int usual_route) {
		
		this.id = id;
		this.name = name;
        this.usual_route = usual_route;
		
	}

	public Person(int id, String name) {

        this.id = id;
        this.name = name;
    }
	
	
	public int getId(){
		
		return this.id;
	}

	public String getName() {

		return this.name;
	}

    public int getUsual_route(){

        return this.usual_route;
    }
	
	public String toString(){
		
		return this.name;
	}
}
