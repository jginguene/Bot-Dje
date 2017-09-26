package fr.ginguene.onepoint.hackathon;

public class Planete {

	public void setTerraformation(int terraformation) {
		this.terraformation = terraformation;
	}

	private int id = 0;
	private float y = -1;
	private int proprietaire = -1;
	private int population = -1;

	public void remPopulation(int population) {
		this.population -= population;
	}

	private int populationMax = -1;
	private int tauxCroissance = -1;
	private char systemeClassification;
	private int terraformation = -1;
	private float x = -1;

	/**
	 * P <Identifiant:int> <X:float> <Y:float> <Propriétaire:int>
	 * <Population:int> <PopulationMaximum:int> <TauxDeCroissance:int>
	 * <SystemeDeClassification:char> <Terraformation:int>
	 * 
	 */
	public Planete(String[] attributes) {

		/*
		 * for (int i = 0 ; i < attributes.length;i++){
		 * System.out.println("Planete: attr " + i+ " = " + attributes[i]); }
		 */

		this.id = Integer.parseInt(attributes[1]);
		this.x = Float.parseFloat(attributes[2]);
		this.y = Float.parseFloat(attributes[3]);
		this.proprietaire = Integer.parseInt(attributes[4]);
		this.population = Integer.parseInt(attributes[5]);

		this.populationMax = Integer.parseInt(attributes[6]);
		this.tauxCroissance = Integer.parseInt(attributes[7]);
		this.systemeClassification = attributes[8].charAt(0);

		if (attributes.length == 10) {
			this.terraformation = Integer.parseInt(attributes[9]);
		}

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

	public boolean isTerraformable() {
		return systemeClassification == 'H' || systemeClassification == 'K' || systemeClassification == 'L';
	}

	public int getTerraformation() {
		return terraformation;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Planete other = (Planete) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public String toString() {
		return "Planete " + this.id;
	}

	public void terraforme() {
		this.terraformation = 20 + (this.tauxCroissance * 2);
	}

	public PlaneteStatus getStatus() {

		switch (proprietaire) {
		case Constantes.AMI:
			return PlaneteStatus.Amie;

		case Constantes.NEUTRE:
			return PlaneteStatus.Neutre;

		default:
			return PlaneteStatus.Ennemie;

		}
	}

}
