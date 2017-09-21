package fr.ginguene.onepoint.bot;

public class Flotte {

	// F <Propriétaire:int> <Vaisseaux:int> <IdSource:int> <IdDestination:int>
	// <ToursTotal:int> <ToursRestants:int>

	private int proprietaire;
	private int vaisseaux;
	private int planeteSource;
	private int planeteDestination;
	private int toursTotals;
	private int toursRestants;

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
