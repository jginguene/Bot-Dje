package fr.ginguene.onepoint.hackathon.ordre;

import fr.ginguene.onepoint.hackathon.Carte;
import fr.ginguene.onepoint.hackathon.Constantes;
import fr.ginguene.onepoint.hackathon.Flotte;
import fr.ginguene.onepoint.hackathon.Planete;

public class EnvoiFlotte implements Ordre {

	public Flotte getFlotte() {
		return flotte;
	}

	private Flotte flotte;

	public EnvoiFlotte(Carte carte, Planete source, Planete destination, int nbVaisseau) {

		if (source.getPopulation() < nbVaisseau) {
			nbVaisseau = source.getPopulation() - 1;
		}

		if (source.getProprietaire() != Constantes.AMI) {
			destination = null;
		}

		if (destination != null) {
			this.flotte = new Flotte(carte);
			flotte.setDestinationId(destination.getId());
			flotte.setSourceId(source.getId());
			flotte.setVaisseaux(nbVaisseau);
		}

	}

	public EnvoiFlotte(Flotte flotte) {
		this.flotte = flotte;

	}

	@Override
	public String asString() {

		if (flotte == null) {
			return "";
		}

		if (flotte.getVaisseaux() <= 3) {
			System.err.println("Essai d'envoi d'une flotte de " + flotte.getVaisseaux() + " vaisseaux (<3) de "
					+ flotte.getSource() + " vers " + flotte.getDestination());
			return "";
		}

		if (flotte.getDestination() == flotte.getSource()) {
			System.err.println("Essai d'envoi d'une flotte depuis et vers  " + flotte.getSource());
			return "";
		}

		if (flotte.getVaisseaux() > flotte.getSource().getPopulation() + 1) {
			System.err.println("Essai d'envoi d'une flotte de  " + flotte.getVaisseaux() + " depuis "
					+ flotte.getSource() + "[" + flotte.getSource().getPopulation() + "]");
			return "";
		}

		// <PlanèteSource:int> <PlanèteCible:int> <NombreUnités:int>
		try {
			return flotte.getSourceId() + " " + flotte.getDestinationId() + " " + flotte.getVaisseaux();
		} catch (Exception e) {
			System.err.println("Failed to serialize order:" + flotte.getSource() + "-" + flotte.getDestination() + "-"
					+ flotte.getVaisseaux());
			return "";
		}
	}

}
