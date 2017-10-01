package fr.ginguene.onepoint.hackathon.action;

import fr.ginguene.onepoint.hackathon.Carte;
import fr.ginguene.onepoint.hackathon.Planete;
import fr.ginguene.onepoint.hackathon.PlaneteStatus;
import fr.ginguene.onepoint.hackathon.Response;
import fr.ginguene.onepoint.hackathon.ordre.EnvoiFlotte;

public class RenfortStrategie extends AbstractStrategie {

	public RenfortStrategie(boolean isDebug) {
		super(isDebug);
	}

	public RenfortStrategie() {
		super();
	}

	@Override
	public boolean execute(Response response, Planete source, Carte carte, boolean isOptimizingScore) {
		int nbEnnemie = getNbEnnemieVoisine(carte, source, 6);

		if (nbEnnemie == 0) {

			if (source.getPopulation() < 10) {
				return true;
			}

			for (Planete aPlanete : carte.getPlanetesOrderByDistance(source)) {
				if (aPlanete.getStatus() == PlaneteStatus.Amie) {
					nbEnnemie = getNbEnnemieVoisine(carte, aPlanete, 3);
					if (nbEnnemie > 0) {

						int nbVaisseau = source.getPopulation() - 10;

						EnvoiFlotte ordre = new EnvoiFlotte(carte, source, aPlanete, nbVaisseau);
						source.remPopulation(nbVaisseau);
						response.addOrdre(ordre);
						carte.addFlotte(ordre.getFlotte());

						this.trace("AttaquePlaneteEnnemie: " + source + " -> " + aPlanete + " [" + nbVaisseau + "]");
						return true;

					}

				}

			}

		}
		return false;
	}

	public int getNbEnnemieVoisine(Carte carte, Planete source, int voisinage) {
		int nbEnnemie = 0;
		for (Planete aPlanete : carte.getVoisines(source, 6)) {
			if (aPlanete.getStatus() != PlaneteStatus.Amie) {
				nbEnnemie++;
			}
		}
		return nbEnnemie;

	}

}
