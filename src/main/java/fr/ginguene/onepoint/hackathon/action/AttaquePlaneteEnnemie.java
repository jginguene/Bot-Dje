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

		int nbEnnemie = carte.getNbEnnemie(source, 4);

		int minNbVaisseauRestant = 1 + 20 * nbEnnemie;

		if (source.getPopulation() < minNbVaisseauRestant) {
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

					int nbVaisseauxAmi = carte.getNbVaisseauInFlotte(PlaneteStatus.Amie, aPlanete);
					int nbVaisseauxEnnemi = carte.getNbVaisseauInFlotte(PlaneteStatus.Ennemie, aPlanete);
					int nbVaisseauManquant;

					if (nbVaisseauxEnnemi > 0) {
						nbVaisseauManquant = aPlanete.getPopulationMax() - nbVaisseauxAmi + nbVaisseauxEnnemi + 100;
					} else {
						nbVaisseauManquant = aPlanete.getPopulationMax() - nbVaisseauxAmi;
					}

					if (aPlanete.getStatus() == PlaneteStatus.Ennemie && nbVaisseauManquant > 0) {

						int nbVaisseau = source.getPopulation() - minNbVaisseauRestant;

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
