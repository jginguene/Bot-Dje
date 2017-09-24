package fr.ginguene.onepoint.hackathon.ordre;

import fr.ginguene.onepoint.hackathon.Flotte;
import fr.ginguene.onepoint.hackathon.Planete;

public class EnvoiFlotte implements Ordre {

	public Flotte getFlotte() {
		return flotte;
	}

	private Flotte flotte;

	public EnvoiFlotte(Planete source, Planete destination, int nbVaisseau) {
		this.flotte = new Flotte();
		flotte.setPlaneteDestination(destination.getId());
		flotte.setPlaneteSource(source.getId());
		flotte.setVaisseaux(nbVaisseau);

	}

	public EnvoiFlotte(Flotte flotte) {
		this.flotte = flotte;

	}

	@Override
	public String asString() {

		if (flotte.getVaisseaux() <= 3) {
			return "";
		}

		// <PlanèteSource:int> <PlanèteCible:int> <NombreUnités:int>
		try {
			return flotte.getPlaneteSource() + " " + flotte.getPlaneteDestination() + " " + flotte.getVaisseaux();
		} catch (Exception e) {
			System.err.println("Failed to serialize order:" + flotte.getPlaneteSource() + "-"
					+ flotte.getPlaneteDestination() + "-" + flotte.getVaisseaux());
			return "";
		}
	}

}
