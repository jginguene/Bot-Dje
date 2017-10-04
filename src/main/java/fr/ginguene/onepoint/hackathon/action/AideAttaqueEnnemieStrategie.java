package fr.ginguene.onepoint.hackathon.action;

import fr.ginguene.onepoint.hackathon.Carte;
import fr.ginguene.onepoint.hackathon.Flotte;
import fr.ginguene.onepoint.hackathon.Planete;
import fr.ginguene.onepoint.hackathon.PlaneteStatus;
import fr.ginguene.onepoint.hackathon.Response;
import fr.ginguene.onepoint.hackathon.ordre.EnvoiFlotte;

public class AideAttaqueEnnemieStrategie extends AbstractStrategie {

	public AideAttaqueEnnemieStrategie(boolean isDebug) {
		super(isDebug);
	}

	public AideAttaqueEnnemieStrategie() {
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

			if (aPlanete.getStatus() == PlaneteStatus.Ennemie
					&& aPlanete.getPopulationMax() < carte.getNbVaisseauInFlotte(PlaneteStatus.Amie, aPlanete)) {

				boolean bombeEnApproche = false;
				for (Flotte flotte : carte.getFlottes(PlaneteStatus.Amie, aPlanete)) {
					if (flotte.getVaisseaux() >= aPlanete.getPopulationMax()) {
						bombeEnApproche = true;
					}
				}

				if (!bombeEnApproche) {

					int mesVaisseaux = carte.getNbVaisseauInFlotte(PlaneteStatus.Amie, aPlanete);

					if (mesVaisseaux > 0) {

						int nbVaisseau = source.getPopulation() - 1;

						if (source.getPopulation() > 20) {
							nbVaisseau = source.getPopulation() - 20;
						}

						EnvoiFlotte ordre = new EnvoiFlotte(carte, source, aPlanete, nbVaisseau);
						source.remPopulation(nbVaisseau);
						response.addOrdre(ordre);
						carte.addFlotte(ordre.getFlotte());
						this.trace(
								"AideAttaqueEnnemieStrategie: " + source + " -> " + aPlanete + " [" + nbVaisseau + "]");
						return true;
					}
				}
			}
		}

		return false;
	}

}
