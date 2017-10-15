package fr.ginguene.onepoint.hackathon.action;

import fr.ginguene.onepoint.hackathon.Carte;
import fr.ginguene.onepoint.hackathon.Planete;
import fr.ginguene.onepoint.hackathon.PlaneteStatus;
import fr.ginguene.onepoint.hackathon.Response;

public class PreparationBombeStrategie extends AbstractStrategie {

	public PreparationBombeStrategie(boolean isDebug) {
		super(isDebug);
	}

	public PreparationBombeStrategie() {
		super();
	}

	@Override
	public boolean execute(Response response, Planete source, Carte carte, boolean isOptimizingScore) {

		if (source.getPopulationMax() <= 80) {
			return false;
		}

		int nbEnnemie = carte.getNbEnnemie(source, 9);
		int nbNeutre = carte.getNbVoisine(source, PlaneteStatus.Neutre, 9);

		if ((nbEnnemie + nbNeutre) == 0 && source.getPopulation() < source.getPopulationMax()) {
			return true;
		}

		return false;
	}

}
