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

		if (isOptimizingScore) {
			return false;
		}

		Planete destination = null;
		int minCout = -1;
		int nbPop = -1;

		for (Planete aPlanete : carte.getPlanetesOrderByDistance(source)) {
			if (aPlanete.getStatus() == PlaneteStatus.Neutre) {

				Planete ennemiLaPlusProche = carte.getPlaneteLaPlusProche(aPlanete, PlaneteStatus.Ennemie);
				float distanceEnnemi = carte.getDistance(ennemiLaPlusProche, aPlanete);
				float distanceSource = carte.getDistance(source, aPlanete);
				int mesFlottes = carte.getMesFlottes(aPlanete);
				int flottesEnnemie = carte.getFlottesEnnemie(aPlanete);

				int aCout = aPlanete.getPopulation() - mesFlottes + flottesEnnemie + 1
						+ carte.getTrajetNbTour(source, aPlanete);

				this.trace(aPlanete + " => aCout[" + aCout + "] = POP[" + aPlanete.getPopulation() + "]" + " - "
						+ mesFlottes + " + " + flottesEnnemie + " + " + 1 + " NbTour["
						+ +carte.getTrajetNbTour(source, aPlanete) + "]");

				if ((destination == null || aCout < minCout && aCout > 0) && distanceEnnemi < distanceSource) {
					destination = aPlanete;
					minCout = aCout;
					nbPop = aPlanete.getPopulation() - mesFlottes + flottesEnnemie + 1;
				}
			}
		}

		if (destination != null) {
			int nbVaisseau = Math.min(nbPop, source.getPopulation() - 1);
			EnvoiFlotte ordre = new EnvoiFlotte(source, destination, nbVaisseau);
			source.remPopulation(nbVaisseau);
			response.addOrdre(ordre);
			carte.addFlotte(ordre.getFlotte());

			System.out.println("attaquePlaneteNeutre: " + source + " -> " + destination + " [" + nbVaisseau + "]");

		}
		return false;

	}
}
