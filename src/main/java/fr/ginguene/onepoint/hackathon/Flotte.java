package fr.ginguene.onepoint.hackathon;

public class Flotte {

	// F <Propriétaire:int> <Vaisseaux:int> <IdSource:int> <IdDestination:int>
	// <ToursTotal:int> <ToursRestants:int>

	private int proprietaire;

	public void setProprietaire(int proprietaire) {
		this.proprietaire = proprietaire;
	}

	public void setVaisseaux(int vaisseaux) {
		this.vaisseaux = vaisseaux;
	}

	public void setPlaneteSource(int planeteSource) {
		this.planeteSource = planeteSource;
	}

	public void setPlaneteDestination(int planeteDestination) {
		this.planeteDestination = planeteDestination;
	}

	public void setToursTotals(int toursTotals) {
		this.toursTotals = toursTotals;
	}

	public void setToursRestants(int toursRestants) {
		this.toursRestants = toursRestants;
	}

	private int vaisseaux;
	private int planeteSource;
	private int planeteDestination;
	private int toursTotals;
	private int toursRestants;

	public Flotte() {

	}

	public Flotte(String[] attributes) {

		this.proprietaire = Integer.parseInt(attributes[1]);
		this.vaisseaux = Integer.parseInt(attributes[2]);
		this.planeteSource = Integer.parseInt(attributes[3]);
		this.planeteDestination = Integer.parseInt(attributes[4]);
		this.toursTotals = Integer.parseInt(attributes[5]);
		this.toursRestants = Integer.parseInt(attributes[6]);

	}

	public int getProprietaire() {
		return proprietaire;
	}

	public int getVaisseaux() {
		return vaisseaux;
	}

	public int getPlaneteSource() {
		return planeteSource;
	}

	public int getPlaneteDestination() {
		return planeteDestination;
	}

	public int getToursTotals() {
		return toursTotals;
	}

	public int getToursRestants() {
		return toursRestants;
	}
}
