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

				int nbVaisseauxAmi = carte.getNbVaisseauInFlotte(PlaneteStatus.Amie, aPlanete);
				int nbVaisseauxEnnemi = carte.getNbVaisseauInFlotte(PlaneteStatus.Ennemie, aPlanete);

				int aCout = aPlanete.getPopulation() - nbVaisseauxAmi + nbVaisseauxEnnemi + 1
						+ carte.getTrajetNbTour(source, aPlanete);

				nbPop = aPlanete.getPopulation() - nbVaisseauxAmi + nbVaisseauxEnnemi + 1;

				this.trace(aPlanete + " => aCout[" + aCout + "] = POP[" + aPlanete.getPopulation() + "]"
						+ " - nbVaisseauxAmi[" + nbVaisseauxAmi + "] + nbVaisseauxEnnemi[" + nbVaisseauxEnnemi + "] + "
						+ 1 + " NbTour[" + +carte.getTrajetNbTour(source, aPlanete) + "] ");
				this.trace("distanceEnnemi:" + distanceEnnemi);
				this.trace("distanceSource:" + distanceSource);
				this.trace("destination:" + destination);
				this.trace("aCout:" + aCout);
				this.trace("minCout:" + minCout);
				this.trace("nbPop:" + nbPop);

				if ((destination == null || aCout < minCout) && distanceEnnemi < distanceSource && nbPop > 0) {
					destination = aPlanete;
					minCout = aCout;
					nbPop = aPlanete.getPopulation() - nbVaisseauxAmi + nbVaisseauxEnnemi + 1;
				}
			}
		}

		if (destination != null) {
			int nbVaisseau = Math.max(nbPop, source.getPopulation() - 1);
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
