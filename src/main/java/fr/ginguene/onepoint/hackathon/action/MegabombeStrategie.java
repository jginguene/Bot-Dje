package fr.ginguene.onepoint.hackathon.action;

import fr.ginguene.onepoint.hackathon.Carte;
import fr.ginguene.onepoint.hackathon.Planete;
import fr.ginguene.onepoint.hackathon.PlaneteStatus;
import fr.ginguene.onepoint.hackathon.Response;
import fr.ginguene.onepoint.hackathon.ordre.EnvoiFlotte;

public class MegabombeStrategie extends AbstractStrategie {

	public MegabombeStrategie(boolean isDebug) {
		super(isDebug);
	}

	public MegabombeStrategie() {
		super();
	}

	@Override
	public boolean execute(Response response, Planete source, Carte carte, boolean isOptimizingScore) {

		if (source.getPopulationMax() < 40) {
			return false;

		}

		if (source.getPopulation() == source.getPopulationMax()) {

			for (Planete aPlanete : carte.getPlanetesOrderByDistance(source)) {

				if (aPlanete.getStatus() == PlaneteStatus.Ennemie) {

					int nbVaisseau = Math.min(aPlanete.getPopulationMax(), source.getPopulation() - 20);

					EnvoiFlotte ordre = new EnvoiFlotte(carte, source, source, nbVaisseau);
					source.remPopulation(nbVaisseau);
					response.addOrdre(ordre);
					carte.addFlotte(ordre.getFlotte());

				}

			}

			return true;

		}
		return false;
	}

}
