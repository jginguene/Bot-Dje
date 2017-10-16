package fr.ginguene.onepoint.hackathon.action;

import fr.ginguene.onepoint.hackathon.Carte;
import fr.ginguene.onepoint.hackathon.Flotte;
import fr.ginguene.onepoint.hackathon.Planete;
import fr.ginguene.onepoint.hackathon.PlaneteStatus;
import fr.ginguene.onepoint.hackathon.Response;
import fr.ginguene.onepoint.hackathon.ordre.EnvoiFlotte;

public class AttaquePlaneteEnnemie extends AbstractStrategie {

	public AttaquePlaneteEnnemie(boolean isDebug) {
		super(isDebug);
	}

	public AttaquePlaneteEnnemie() {
		super();
	}

	@Override
	public boolean execute(Response response, Planete source, Carte carte, boolean isOptimizingScore) {

		if (isOptimizingScore) {
			return false;
		}

		Planete ennemie = carte.getEnnemiLaPlusProche(source);
		int distance = carte.getTrajetNbTour(source, ennemie);
		if (distance <= 10) {
			return false;
		}

		int nbEnnemie = carte.getNbVaisseauInFlotte(PlaneteStatus.Ennemie, source, 20)
				- source.getTauxCroissance() * 20;

		int nbVaisseauRestant = 1;
		if (nbEnnemie > 0) {
			nbVaisseauRestant += nbEnnemie;
		}

		if (source.getPopulation() < nbVaisseauRestant) {
			return false;
		}

		for (Planete aPlanete : carte.getPlanetesOrderByDistance(source)) {
			if (aPlanete.getStatus() == PlaneteStatus.Ennemie) {

				boolean bombeEnApproche = false;
				for (Flotte flotte : carte.getFlottes(PlaneteStatus.Amie, aPlanete)) {
					if (flotte.getVaisseaux() >= aPlanete.getPopulationMax()) {
						bombeEnApproche = true;
					}
				}

				if (!bombeEnApproche) {

					if (aPlanete.getStatus() == PlaneteStatus.Ennemie) {

						int nbVaisseau = source.getPopulation() - nbVaisseauRestant;

						EnvoiFlotte ordre = new EnvoiFlotte(carte, source, aPlanete, nbVaisseau);
						source.remPopulation(nbVaisseau);
						response.addOrdre(ordre);
						carte.addFlotte(ordre.getFlotte());

						this.trace("AttaquePlaneteEnnemie: " + source + " -> " + aPlanete + " [" + nbVaisseau + "]");
						return true;
					}
				}
			}
		}

		return false;
	}

}
