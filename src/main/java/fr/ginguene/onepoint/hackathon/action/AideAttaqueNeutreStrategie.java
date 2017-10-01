package fr.ginguene.onepoint.hackathon.action;

import fr.ginguene.onepoint.hackathon.Carte;
import fr.ginguene.onepoint.hackathon.Planete;
import fr.ginguene.onepoint.hackathon.PlaneteStatus;
import fr.ginguene.onepoint.hackathon.Response;
import fr.ginguene.onepoint.hackathon.ordre.EnvoiFlotte;

public class AideAttaqueNeutreStrategie extends AbstractStrategie {

	public AideAttaqueNeutreStrategie(boolean isDebug) {
		super(isDebug);
	}

	public AideAttaqueNeutreStrategie() {
		super();
	}

	@Override
	public boolean execute(Response response, Planete source, Carte carte, boolean isOptimizingScore) {
		if (isOptimizingScore) {
			return false;
		}

		if (source.getPopulation() < 5) {
			return false;
		}

		for (Planete aPlanete : carte.getPlanetesOrderByDistance(source)) {

			int nbVaisseauxAmi = carte.getNbVaisseauInFlotte(PlaneteStatus.Amie, aPlanete);
			int nbVaisseauxEnnemi = carte.getNbVaisseauInFlotte(PlaneteStatus.Ennemie, aPlanete);

			int nbVaisseauxManquant = aPlanete.getPopulation() - nbVaisseauxAmi + nbVaisseauxEnnemi;

			if (nbVaisseauxEnnemi > 0) {
				nbVaisseauxManquant += 20;
			}

			if (aPlanete.getStatus() == PlaneteStatus.Neutre && nbVaisseauxManquant > 0 && nbVaisseauxAmi > 0) {

				int nbVaisseau = Math.min(nbVaisseauxManquant, source.getPopulation() - 3);

				if (source.getPopulation() > 20) {
					nbVaisseau = source.getPopulation() - 20;
				}

				EnvoiFlotte ordre = new EnvoiFlotte(carte, source, aPlanete, nbVaisseau);
				source.remPopulation(nbVaisseau);
				response.addOrdre(ordre);
				carte.addFlotte(ordre.getFlotte());
				this.trace("AidePlanete: " + source + " -> " + aPlanete + " [" + nbVaisseau + "]");
				return true;
			}

		}

		return false;
	}

}
