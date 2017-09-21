package fr.ginguene.onepoint.bot;

public class Planete {
	
	private int id;
	private float y;
	private int proprietaire;
	private int population;
	private int populationMax;
	private int tauxCroissance;
	private char systemeClassification;
	private int terraformation;
	private float x;
	
	/**
	 * P <Identifiant:int> <X:float> <Y:float> <Propriétaire:int> <Population:int> <PopulationMaximum:int> <TauxDeCroissance:int> <SystemeDeClassification:char> <Terraformation:int>
	 * 
	 */
	public Planete (String [] attributes){	
		
		for (int i = 0 ; i < attributes.length;i++){
			System.out.println("Planete: attr " + i+ " = " + attributes[i]);
		}
		
		 
		this.id=Integer.parseInt(attributes[1]);
		this.x= Float.parseFloat(attributes[2]);
		this.y= Float.parseFloat(attributes[3]);
		this.proprietaire= Integer.parseInt(attributes[4]);
		this.population= Integer.parseInt(attributes[5]);
		this.populationMax= Integer.parseInt(attributes[6]);
		this.tauxCroissance= Integer.parseInt(attributes[7]);		
		this.systemeClassification= attributes[8].charAt(0);		
		this.terraformation= Integer.parseInt(attributes[9]);		
	
		
	}
	
	public int getId() {
		return id;
	}


	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public int getProprietaire() {
		return proprietaire;
	}

	public int getPopulation() {
		return population;
	}

	public int getPopulationMax() {
		return populationMax;
	}

	public int getTauxCroissance() {
		return tauxCroissance;
	}

	public char getSystemeClassification() {
		return systemeClassification;
	}

	public int getTerraformation() {
		return terraformation;
	}


}
