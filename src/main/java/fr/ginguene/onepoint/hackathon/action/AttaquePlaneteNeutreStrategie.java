package fr.ginguene.onepoint.hackathon.action;

import fr.ginguene.onepoint.hackathon.Carte;
import fr.ginguene.onepoint.hackathon.Planete;
import fr.ginguene.onepoint.hackathon.PlaneteStatus;
import fr.ginguene.onepoint.hackathon.Response;
import fr.ginguene.onepoint.hackathon.ordre.EnvoiFlotte;

public class AttaquePlaneteNeutreStrategie extends AbstractStrategie {

	public AttaquePlaneteNeutreStrategie(boolean isDebug) {
		super(isDebug);
	}

	public AttaquePlaneteNeutreStrategie() {
		super();
	}

	@Override
	public boolean execute(Response response, Planete source, Carte carte, boolean isOptimizingScore) {

		if (source.getPopulation() < 5) {
			return false;
		}

		Planete destination = null;
		int minCout = -1;
		int nbPop = 0;

		for (Planete aPlanete : carte.getPlanetesOrderByDistance(source)) {
			if (aPlanete.getStatus() == PlaneteStatus.Neutre) {

				Planete ennemiLaPlusProche = carte.getPlaneteLaPlusProche(aPlanete, PlaneteStatus.Ennemie);
				float distanceEnnemi = carte.getDistance(ennemiLaPlusProche, aPlanete);
				float distanceSource = carte.getDistance(source, aPlanete);

				int nbVaisseauxAmi = carte.getNbVaisseauInFlotte(PlaneteStatus.Amie, aPlanete);
				int nbVaisseauxEnnemi = carte.getNbVaisseauInFlotte(PlaneteStatus.Ennemie, aPlanete);

				int aCout = aPlanete.getPopulation() - nbVaisseauxAmi - nbVaisseauxEnnemi + 2
						+ carte.getTrajetNbTour(source, aPlanete) * 2 - aPlanete.getTauxCroissance() * 10;

				int aNbPop = aPlanete.getPopulation() - nbVaisseauxAmi + nbVaisseauxEnnemi + 1;

				if ((destination == null || aCout < minCout) && distanceSource < distanceEnnemi && aNbPop > 0) {
					destination = aPlanete;
					minCout = aCout;
					nbPop = aNbPop;
				}
			}
		}

		if (destination != null) {
			System.out.println("nbPop: " + nbPop + "; source.getPopulation():" + source.getPopulation());

			int mesEnnemies = carte.getNbVaisseauInFlotte(PlaneteStatus.Ennemie, source, 20);

			nbPop = Math.max(4, nbPop);
			int nbVaisseau = Math.min(nbPop, source.getPopulation() - 1 - mesEnnemies);

			EnvoiFlotte ordre = new EnvoiFlotte(carte, source, destination, nbVaisseau);
			source.remPopulation(nbVaisseau);
			response.addOrdre(ordre);
			carte.addFlotte(ordre.getFlotte());

			System.out.println("AttaquePlaneteNeutre: " + source + " -> " + destination + " [" + nbVaisseau + "]");
			return true;

		}
		return false;

	}
}
