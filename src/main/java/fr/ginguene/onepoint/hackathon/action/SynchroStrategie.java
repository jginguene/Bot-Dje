package fr.ginguene.onepoint.hackathon.action;

import fr.ginguene.onepoint.hackathon.Carte;
import fr.ginguene.onepoint.hackathon.Flotte;
import fr.ginguene.onepoint.hackathon.Planete;
import fr.ginguene.onepoint.hackathon.PlaneteStatus;
import fr.ginguene.onepoint.hackathon.Response;
import fr.ginguene.onepoint.hackathon.ordre.EnvoiFlotte;

public class SynchroStrategie extends AbstractStrategie {

	public SynchroStrategie(boolean isDebug) {
		super(isDebug);
	}

	public SynchroStrategie() {
		super();
	}

	@Override
	public boolean execute(Response response, Planete source, Carte carte, boolean isOptimizingScore) {
		if (isOptimizingScore) {
			return false;
		}

		if (source.getPopulation() < 3) {
			return false;
		}

		for (Planete aPlanete : carte.getPlanetesOrderByDistance(source)) {

			int distanceEnTour = carte.getTrajetNbTour(source, aPlanete);

			for (Flotte flotte : carte.getFlottes(PlaneteStatus.Amie, aPlanete)) {

				trace("distance " + source + "-" + aPlanete + ":" + distanceEnTour);
				trace("flotte: " + flotte.getToursRestants());

				if (flotte.getToursRestants() == distanceEnTour) {

					int nbVaisseau = source.getPopulation() - 3;

					EnvoiFlotte ordre = new EnvoiFlotte(carte, source, aPlanete, nbVaisseau);
					source.remPopulation(nbVaisseau);
					response.addOrdre(ordre);
					carte.addFlotte(ordre.getFlotte());
					return true;

				}

			}

		}
		return false;

	}

}
