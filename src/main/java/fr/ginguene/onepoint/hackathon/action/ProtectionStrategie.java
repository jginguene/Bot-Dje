package fr.ginguene.onepoint.hackathon.action;

import fr.ginguene.onepoint.hackathon.Carte;
import fr.ginguene.onepoint.hackathon.Planete;
import fr.ginguene.onepoint.hackathon.PlaneteStatus;
import fr.ginguene.onepoint.hackathon.Response;

public class ProtectionStrategie extends AbstractStrategie {

	@Override
	public boolean execute(Response response, Planete source, Carte carte, boolean isOptimizingScore) {

		int nbVaisseauEnnemie = carte.getNbVaisseauInFlotte(PlaneteStatus.Ennemie, source, 20);

		if (nbVaisseauEnnemie == 0 || source.getPopulation() > source.getPopulationMax() / 2) {
			return false;
		}

		return (source.getPopulation() < nbVaisseauEnnemie);
	}

}
