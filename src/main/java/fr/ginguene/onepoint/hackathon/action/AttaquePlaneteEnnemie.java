package fr.ginguene.onepoint.hackathon.action;

import fr.ginguene.onepoint.hackathon.Carte;
import fr.ginguene.onepoint.hackathon.Planete;
import fr.ginguene.onepoint.hackathon.PlaneteStatus;
import fr.ginguene.onepoint.hackathon.Response;
import fr.ginguene.onepoint.hackathon.ordre.EnvoiFlotte;

public class AttaquePlaneteEnnemie extends AbstractStrategie {

	public AttaquePlaneteEnnemie(boolean isDebug) {
		super(isDebug);
	}

	public AttaquePlaneteEnnemie() {
		super();
	}

	@Override
	public boolean execute(Response response, Planete source, Carte carte, boolean isOptimizingScore) {

		if (isOptimizingScore) {
			return false;
		}

		if (source.getPopulation() < 10) {
			return false;
		}

		int nbEnnemie = 0;
		for (Planete aPlanete : carte.getVoisines(source, 3)) {
			if (aPlanete.getStatus() != PlaneteStatus.Amie) {
				nbEnnemie++;
			}
		}

		if (nbEnnemie >= 2) {
			return false;
		}

		for (Planete aPlanete : carte.getPlanetesOrderByDistance(source)) {

			int nbVaisseauxAmi = carte.getNbVaisseauInFlotte(PlaneteStatus.Amie, aPlanete);
			int nbVaisseauxEnnemi = carte.getNbVaisseauInFlotte(PlaneteStatus.Ennemie, aPlanete);
			int nbVaisseauManquant;

			if (nbVaisseauxEnnemi > 0) {
				nbVaisseauManquant = aPlanete.getPopulationMax() - nbVaisseauxAmi + nbVaisseauxEnnemi + 100;
			} else {
				nbVaisseauManquant = aPlanete.getPopulationMax() - nbVaisseauxAmi;
			}

			if (aPlanete.getStatus() == PlaneteStatus.Ennemie && nbVaisseauManquant > 0) {

				int nbVaisseauxEnnemiSource = carte.getNbVaisseauInFlotte(PlaneteStatus.Ennemie, source);

				int nbVaisseau = Math.min(nbVaisseauManquant, source.getPopulation() - nbVaisseauxEnnemiSource - 1);

				EnvoiFlotte ordre = new EnvoiFlotte(carte, source, aPlanete, nbVaisseau);
				source.remPopulation(nbVaisseau);
				response.addOrdre(ordre);
				carte.addFlotte(ordre.getFlotte());

				this.trace("AttaquePlaneteEnnemie: " + source + " -> " + aPlanete + " [" + nbVaisseau + "]");
				return true;
			}
		}

		return false;
	}

}
