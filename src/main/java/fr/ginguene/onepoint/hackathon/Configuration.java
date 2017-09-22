package fr.ginguene.onepoint.hackathon;

public class Configuration {

	private int tour = -1;
	private int toursMaximum;
	private long identifiantPartie;

	/**
	 * C <tour:int> <toursMaximum:int> <IdentifiantPartie:long>
	 * 
	 */
	public Configuration(String[] attributes) {
		this.tour = Integer.parseInt(attributes[1]);
		this.toursMaximum = Integer.parseInt(attributes[2]);
		this.identifiantPartie = Long.parseLong(attributes[3]);

	}

	public int getTour() {
		return tour;
	}

	public void setTour(int tour) {
		this.tour = tour;
	}

	public int getToursMaximum() {
		return toursMaximum;
	}

	public void setToursMaximum(int toursMaximum) {
		this.toursMaximum = toursMaximum;
	}

	public long getIdentifiantPartie() {
		return identifiantPartie;
	}

	public void setIdentifiantPartie(long identifiantPartie) {
		this.identifiantPartie = identifiantPartie;
	}

}
