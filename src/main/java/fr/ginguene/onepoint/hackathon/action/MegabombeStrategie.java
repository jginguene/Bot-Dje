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

		if (source.getPopulationMax() < 40) {
			return false;

		}

		if ((source.getPopulationMax() == source.getPopulation() || source.getPopulation() > 100)
				&& source.getPopulation() > source.getPopulationMax() / 2) {

			for (Planete aPlanete : carte.getPlanetesOrderByDistance(source)) {

				if (aPlanete.getStatus() == PlaneteStatus.Ennemie) {

					boolean bombeEnApproche = false;
					for (Flotte flotte : carte.getFlottes(PlaneteStatus.Amie, aPlanete)) {
						if (flotte.getVaisseaux() >= aPlanete.getPopulationMax()) {
							bombeEnApproche = true;
						}
					}

					if (!bombeEnApproche) {

						int nbVaisseau = Math.min(aPlanete.getPopulationMax() + 1, source.getPopulation() - 20);

						// Si on est au front on envoi une bombe moins forte
						if (carte.getNbEnnemie(source, 3) >= 2) {
							return false;
						} else {
							if (nbVaisseau >= aPlanete.getPopulationMax()) {
								EnvoiFlotte ordre = new EnvoiFlotte(carte, source, aPlanete, nbVaisseau);
								source.remPopulation(nbVaisseau);
								response.addOrdre(ordre);
								carte.addFlotte(ordre.getFlotte());
								return true;
							}

						}

					}

				}

			}

		}
		return false;
	}

}
