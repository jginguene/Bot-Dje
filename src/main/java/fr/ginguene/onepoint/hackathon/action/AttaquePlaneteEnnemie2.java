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

		int nbEnnemie = carte.getNbEnnemie(source, 4);

		int minNbVaisseauRestant = 1 + 20 * nbEnnemie;

		if (source.getPopulation() < minNbVaisseauRestant) {
			return false;
		}

		for (Planete aPlanete : carte.getPlanetesOrderByDistance(source)) {
			if (aPlanete.getStatus() == PlaneteStatus.Ennemie) {
				int nbVaisseauxAmi = carte.getNbVaisseauInFlotte(PlaneteStatus.Amie, aPlanete);
				if (nbVaisseauxAmi == 0) {

					int nbVaisseau = source.getPopulation() - minNbVaisseauRestant;

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
