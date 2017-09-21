package fr.ginguene.onepoint.bot;

public class Ordre {
	
	private Planete origine;
	private Planete destination;	
	private int population;
	
	
	public Planete getOrigine() {
		return origine;
	}
	public void setOrigine(Planete origine) {
		this.origine = origine;
	}
	public Planete getDestination() {
		return destination;
	}
	public void setDestination(Planete destination) {
		this.destination = destination;
	}
	public int getPopulation() {
		return population;
	}
	public void setPopulation(int population) {
		this.population = population;
	}
	
	public String toString(){
		//<PlanèteSource:int> <PlanèteCible:int> <NombreUnités:int>
		return origine.getId() +" " + destination.getId() + " " + this.population;
	}
	
	

}
