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

				int aNbPop = aPlanete.getPopulation() - nbVaisseauxAmi + nbVaisseauxEnnemi + 1;

				this.trace(aPlanete + " => aCout[" + aCout + "] = POP[" + aPlanete.getPopulation() + "]"
						+ " - nbVaisseauxAmi[" + nbVaisseauxAmi + "] + nbVaisseauxEnnemi[" + nbVaisseauxEnnemi + "] + "
						+ 1 + " NbTour[" + +carte.getTrajetNbTour(source, aPlanete) + "] ");
				this.trace("distanceEnnemi:" + distanceEnnemi);
				this.trace("distanceSource:" + distanceSource);
				this.trace("destination:" + destination);
				this.trace("aCout:" + aCout);
				this.trace("minCout:" + minCout);
				this.trace("aNbPop:" + aNbPop);

				/*
				 * 2017-10-01T12:39:36.787563+00:00 app[web.1]: Planete 23 =>
				 * aCout[7] = POP[36] - nbVaisseauxAmi[37] +
				 * nbVaisseauxEnnemi[0] + 1 NbTour[7]
				 * 2017-10-01T12:39:36.787609+00:00 app[web.1]:
				 * distanceEnnemi:504.76233 2017-10-01T12:39:36.787644+00:00
				 * app[web.1]: distanceSource:144.01389
				 * 2017-10-01T12:39:36.787646+00:00 app[web.1]: destination:null
				 * 2017-10-01T12:39:36.787660+00:00 app[web.1]: aCout:7
				 * 2017-10-01T12:39:36.787661+00:00 app[web.1]: minCout:-1
				 * 2017-10-01T12:39:36.787671+00:00 app[web.1]: nbPop:0
				 */

				if ((destination == null || aCout < minCout) && distanceEnnemi < distanceSource && aNbPop > 0) {
					System.out.println("===> " + aPlanete + " best choice for the moment");
					destination = aPlanete;
					minCout = aCout;
					nbPop = aNbPop;
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
