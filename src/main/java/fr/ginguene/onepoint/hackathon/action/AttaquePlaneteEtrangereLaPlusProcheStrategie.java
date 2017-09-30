package fr.ginguene.onepoint.hackathon.action;

import fr.ginguene.onepoint.hackathon.Carte;
import fr.ginguene.onepoint.hackathon.Planete;
import fr.ginguene.onepoint.hackathon.PlaneteStatus;
import fr.ginguene.onepoint.hackathon.Response;
import fr.ginguene.onepoint.hackathon.ordre.EnvoiFlotte;

public class AttaquePlaneteEtrangereLaPlusProcheStrategie extends AbstractStrategie {

	public AttaquePlaneteEtrangereLaPlusProcheStrategie(boolean isDebug) {
		super(isDebug);
	}

	public AttaquePlaneteEtrangereLaPlusProcheStrategie() {
		super();
	}

	@Override
	public boolean execute(Response response, Planete source, Carte carte, boolean isOptimizingScore) {

		if (isOptimizingScore) {
			return false;
		}
		if (source.getPopulation() > 40) {

			for (Planete aPlanete : carte.getPlanetesOrderByDistance(source)) {
				if (aPlanete.getStatus() != PlaneteStatus.Amie
						&& aPlanete.getPopulationMax() < carte.getMesFlottes(aPlanete)) {

					int nbVaisseau = source.getPopulation() - 20;

					EnvoiFlotte ordre = new EnvoiFlotte(source, aPlanete, nbVaisseau);
					source.remPopulation(nbVaisseau);
					response.addOrdre(ordre);
					carte.addFlotte(ordre.getFlotte());

					this.trace("attaquePlaneteEtrangereLaPlusProche: " + source + " -> " + aPlanete + " [" + nbVaisseau
							+ "]");
					return true;
				}
			}
		}

		return false;
	}

}
