package fr.ginguene.onepoint.bot.ordre;

import fr.ginguene.onepoint.bot.Planete;

public class EnvoiTroupe implements Ordre {
	
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
	
	@Override
	public String asString(){
		if (population<=3){
			return "";
		}		
		
		
		//<PlanèteSource:int> <PlanèteCible:int> <NombreUnités:int>
		try{
			return origine.getId() +" " + destination.getId() + " " + this.population;
		}catch(Exception e){
			System.err.println("Failed to serialize order:" + origine+"-"+destination + "-"+population);
			return "";
		}
	}



	
	

}
