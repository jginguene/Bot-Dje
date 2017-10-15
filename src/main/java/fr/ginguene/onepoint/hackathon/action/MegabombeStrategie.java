package fr.ginguene.onepoint.hackathon.action;

import fr.ginguene.onepoint.hackathon.Carte;
import fr.ginguene.onepoint.hackathon.Flotte;
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

		if (source.getPopulationMax() < 80) {
			return false;
		}

		for (Planete aPlanete : carte.getPlanetesOrderByDistance(source)) {

			if (aPlanete.getStatus() == PlaneteStatus.Ennemie) {

				boolean bombeEnApproche = false;
				for (Flotte flotte : carte.getFlottes(PlaneteStatus.Amie, aPlanete)) {
					if (flotte.getVaisseaux() >= aPlanete.getPopulationMax()) {
						bombeEnApproche = true;
					}
				}

				if (!bombeEnApproche) {

					int nbVaisseauEnnemie = carte.getNbVaisseauInFlotte(PlaneteStatus.Amie, source, 20);
					int nbVaisseau = -1;

					if (source.getPopulation() > aPlanete.getPopulationMax() + nbVaisseauEnnemie + 10) {
						nbVaisseau = aPlanete.getPopulationMax() + 1;
					}

					if (source.getPopulation() > aPlanete.getPopulation()
							+ aPlanete.getTauxCroissance() * carte.getTrajetNbTour(source, aPlanete) + 10) {
						nbVaisseau = aPlanete.getPopulation()
								+ aPlanete.getTauxCroissance() * carte.getTrajetNbTour(source, aPlanete) + 1;
					}

					if (nbVaisseau > -1) {

						EnvoiFlotte ordre = new EnvoiFlotte(carte, source, aPlanete, nbVaisseau);
						source.remPopulation(nbVaisseau);
						response.addOrdre(ordre);
						carte.addFlotte(ordre.getFlotte());
						return true;
					}

				}
			}
		}
		return false;
	}

}
