package fr.ginguene.onepoint.hackathon;

public class Flotte {

	// F <Propriétaire:int> <Vaisseaux:int> <IdSource:int> <IdDestination:int>
	// <ToursTotal:int> <ToursRestants:int>

	private int proprietaire;
	private Carte carte;

	private int vaisseaux;
	private int sourceId;
	private int destinationId;
	private int toursTotals;
	private int toursRestants;

	public Flotte(String[] attributes, Carte carte) {

		this.proprietaire = Integer.parseInt(attributes[1]);
		this.vaisseaux = Integer.parseInt(attributes[2]);
		this.sourceId = Integer.parseInt(attributes[3]);
		this.destinationId = Integer.parseInt(attributes[4]);
		this.toursTotals = Integer.parseInt(attributes[5]);
		this.toursRestants = Integer.parseInt(attributes[6]);

	}

	public Flotte(Carte carte) {
		this.carte = carte;
	}

	public void setProprietaire(int proprietaire) {
		this.proprietaire = proprietaire;
	}

	public void setVaisseaux(int vaisseaux) {
		this.vaisseaux = vaisseaux;
	}

	public void setSourceId(int sourceId) {
		this.sourceId = sourceId;
	}

	public void setDestinationId(int destinationId) {
		this.destinationId = destinationId;
	}

	public void setToursTotals(int toursTotals) {
		this.toursTotals = toursTotals;
	}

	public void setToursRestants(int toursRestants) {
		this.toursRestants = toursRestants;
	}

	public int getProprietaire() {
		return proprietaire;
	}

	public int getVaisseaux() {
		return vaisseaux;
	}

	public int getSourceId() {
		return sourceId;
	}

	public Planete getSource() {
		return carte.getPlanete(sourceId);
	}

	public int getDestinationId() {
		return destinationId;
	}

	public Planete getDestination() {
		return carte.getPlanete(destinationId);
	}

	public int getToursTotals() {
		return toursTotals;
	}

	public int getToursRestants() {
		return toursRestants;
	}

	public PlaneteStatus getSourceStatus() {
		return this.getSource().getStatus();
	}

	public PlaneteStatus getDestinationStatus() {
		return this.getDestination().getStatus();

	}
}
