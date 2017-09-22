package fr.ginguene.onepoint.hackathon.ordre;

import fr.ginguene.onepoint.hackathon.Flotte;

public class EnvoiFlotte implements Ordre {

	private Flotte flotte;

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
