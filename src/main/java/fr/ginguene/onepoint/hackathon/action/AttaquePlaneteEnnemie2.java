package fr.ginguene.onepoint.hackathon.action;

import fr.ginguene.onepoint.hackathon.Carte;
import fr.ginguene.onepoint.hackathon.Planete;
import fr.ginguene.onepoint.hackathon.PlaneteStatus;
import fr.ginguene.onepoint.hackathon.Response;
import fr.ginguene.onepoint.hackathon.ordre.EnvoiFlotte;

public class AttaquePlaneteEnnemie2 extends AbstractStrategie {

	public AttaquePlaneteEnnemie2(boolean isDebug) {
		super(isDebug);
	}

	public AttaquePlaneteEnnemie2() {
		super();
	}

	@Override
	public boolean execute(Response response, Planete source, Carte carte, boolean isOptimizingScore) {

		if (isOptimizingScore) {
			return false;
		}

		if (source.getPopulation() < 40) {
			return false;
		}

		int nbEnnemie = 0;
		for (Planete aPlanete : carte.getVoisines(source, 5)) {
			if (aPlanete.getStatus() != PlaneteStatus.Amie) {
				nbEnnemie++;
			}
		}

		if (nbEnnemie > 0) {
			return false;
		}

		for (Planete aPlanete : carte.getPlanetesOrderByDistance(source)) {
			if (aPlanete.getStatus() == PlaneteStatus.Ennemie) {
				int nbVaisseauxAmi = carte.getNbVaisseauInFlotte(PlaneteStatus.Amie, aPlanete);
				if (nbVaisseauxAmi == 0) {

					int nbVaisseau = source.getPopulation() - 20;

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
