package fr.ginguene.onepoint.hackathon.action;

import fr.ginguene.onepoint.hackathon.Carte;
import fr.ginguene.onepoint.hackathon.Planete;
import fr.ginguene.onepoint.hackathon.Response;

public class MegabombeStrategie extends AbstractStrategie {

	public MegabombeStrategie(boolean isDebug) {
		super(isDebug);
	}

	public MegabombeStrategie() {
		super();
	}

	@Override
	public boolean execute(Response response, Planete source, Carte carte, boolean isOptimizingScore) {

		if (isOptimizingScore) {
			/*
			 * if (source.getPopulation() == source.getPopulationMax()) {
			 * Planete destination = null; float distance = -1;
			 * 
			 * for (Planete aPlanete : carte.getPlanetes()) { if
			 * (aPlanete.getStatus() != PlaneteStatus.Ennemie) { float aDistance
			 * = carte.getDistance(aPlanete, source); if (distance > aDistance
			 * || destination == null) { distance = aDistance; destination =
			 * aPlanete; }
			 * 
			 * } int nbVaisseau = source.getPopulation() - 1; EnvoiFlotte ordre
			 * = new EnvoiFlotte(carte, source, destination, nbVaisseau);
			 * source.remPopulation(nbVaisseau); response.addOrdre(ordre);
			 * carte.addFlotte(ordre.getFlotte()); return true; }
			 * 
			 * }
			 */
		}
		return false;
	}

}
