package fr.ginguene.onepoint.hackathon.action;

import fr.ginguene.onepoint.hackathon.Carte;
import fr.ginguene.onepoint.hackathon.Planete;
import fr.ginguene.onepoint.hackathon.PlaneteStatus;
import fr.ginguene.onepoint.hackathon.Response;
import fr.ginguene.onepoint.hackathon.ordre.EnvoiFlotte;

public class Acharnement extends AbstractStrategie {

	private int destinationId = -1;

	public Acharnement(boolean isDebug) {
		super(isDebug);
	}

	public Acharnement() {
		super();
	}

	@Override
	public boolean execute(Response response, Planete source, Carte carte, boolean isOptimizingScore) {

		Planete destination = carte.getEnnemiLaPlusProche(source);

		int nbVaisseau = 0;
		for (Planete maPlanete : carte.getPlanetes(PlaneteStatus.Amie)) {
			nbVaisseau += maPlanete.getPopulation() - carte.getNbVaisseauInFlotte(PlaneteStatus.Ennemie, maPlanete, 20);
		}

		if (nbVaisseau > destination.getPopulation()) {

			for (Planete maPlanete : carte.getPlanetes(PlaneteStatus.Amie)) {
				int aNbVaisseau = maPlanete.getPopulation()
						- carte.getNbVaisseauInFlotte(PlaneteStatus.Ennemie, maPlanete, 20) - 5;

				EnvoiFlotte ordre = new EnvoiFlotte(carte, source, destination, aNbVaisseau);
				source.remPopulation(nbVaisseau);
				response.addOrdre(ordre);
				carte.addFlotte(ordre.getFlotte());
			}

			return true;

		}

		return false;

	}

}
