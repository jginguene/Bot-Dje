package fr.ginguene.onepoint.hackathon.action;

import fr.ginguene.onepoint.hackathon.Carte;
import fr.ginguene.onepoint.hackathon.Flotte;
import fr.ginguene.onepoint.hackathon.Planete;
import fr.ginguene.onepoint.hackathon.PlaneteStatus;
import fr.ginguene.onepoint.hackathon.Response;
import fr.ginguene.onepoint.hackathon.ordre.EnvoiFlotte;

public class AttaquePlaneteEnnemieRapprochee extends AbstractStrategie {

	public AttaquePlaneteEnnemieRapprochee(boolean isDebug) {
		super(isDebug);
	}

	public AttaquePlaneteEnnemieRapprochee() {
		super();
	}

	@Override
	public boolean execute(Response response, Planete source, Carte carte, boolean isOptimizingScore) {

		Planete ennemie = carte.getEnnemiLaPlusProche(source);

		for (Flotte flotte : carte.getFlottes(PlaneteStatus.Amie, ennemie)) {
			if (flotte.getVaisseaux() > ennemie.getPopulationMax()) {
				return false;
			}

			if (flotte.getSource().equals(source)) {
				return false;
			}

		}

		int distance = carte.getTrajetNbTour(source, ennemie);

		if (distance > 10) {
			return false;
		}

		int coutAttaque = ennemie.getPopulation() + ennemie.getTauxCroissance() * distance;

		if (coutAttaque < source.getPopulation() + 10) {

			int nbVaisseau = coutAttaque;
			EnvoiFlotte ordre = new EnvoiFlotte(carte, source, ennemie, nbVaisseau);
			source.remPopulation(nbVaisseau);
			response.addOrdre(ordre);
			carte.addFlotte(ordre.getFlotte());
			return true;

		}

		return false;
	}

}
