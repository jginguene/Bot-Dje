package fr.ginguene.onepoint.hackathon.action;

import fr.ginguene.onepoint.hackathon.Carte;
import fr.ginguene.onepoint.hackathon.Constantes;
import fr.ginguene.onepoint.hackathon.Planete;
import fr.ginguene.onepoint.hackathon.PlaneteStatus;
import fr.ginguene.onepoint.hackathon.Response;
import fr.ginguene.onepoint.hackathon.ordre.EnvoiFlotte;

public class AideStrategie extends AbstractStrategie {

	public AideStrategie(boolean isDebug) {
		super(isDebug);
	}

	public AideStrategie() {
		super();
	}

	@Override
	public boolean execute(Response response, Planete source, Carte carte, boolean isOptimizingScore) {
		if (isOptimizingScore) {
			return false;
		}

		for (Planete aPlanete : carte.getPlanetesOrderByDistance(source)) {
			if (aPlanete.getStatus() == PlaneteStatus.Ennemie
					&& aPlanete.getPopulationMax() < carte.getMesFlottes(aPlanete) * 2) {

				int maFlotte = carte.getFlotte(Constantes.AMI, aPlanete.getId());
				if (maFlotte > 0) {

					int nbVaisseau = 5;
					EnvoiFlotte ordre = new EnvoiFlotte(source, aPlanete, nbVaisseau);
					source.remPopulation(nbVaisseau);
					response.addOrdre(ordre);
					carte.addFlotte(ordre.getFlotte());

					this.trace("aidePlanete: " + source + " -> " + aPlanete + " [" + nbVaisseau + "]");
					return true;

				}
			}
		}

		return false;
	}

}
