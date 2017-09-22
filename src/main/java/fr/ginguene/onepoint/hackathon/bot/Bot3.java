package fr.ginguene.onepoint.hackathon.bot;

import java.util.List;

import fr.ginguene.onepoint.hackathon.Carte;
import fr.ginguene.onepoint.hackathon.Constantes;
import fr.ginguene.onepoint.hackathon.Flotte;
import fr.ginguene.onepoint.hackathon.IBot;
import fr.ginguene.onepoint.hackathon.Planete;
import fr.ginguene.onepoint.hackathon.Response;
import fr.ginguene.onepoint.hackathon.ordre.EnvoiFlotte;
import fr.ginguene.onepoint.hackathon.scoring.ExpensionPlaneteScoring;
import fr.ginguene.onepoint.hackathon.scoring.IPlaneteScoring;
import fr.ginguene.onepoint.hackathon.scoring.StarterPlaneteScoring;

public class Bot3 implements IBot {

	private IPlaneteScoring expensionScroring = new ExpensionPlaneteScoring();
	private IPlaneteScoring starterScroring = new StarterPlaneteScoring();

	public Response getResponse(Carte carte) {

		IPlaneteScoring scoring;
		if (carte.getMesPlanetes().size() == 1) {
			scoring = starterScroring;
		} else {
			scoring = expensionScroring;
		}

		Response response = new Response();

		// System.out.println("Nombre de planete:" +
		// carte.getPlanetes().size());

		List<Planete> mesPlanetes = carte.getPlanetes(Constantes.MOI);

		for (Planete source : mesPlanetes) {

			int maxScore = -1;
			Planete destination = null;

			for (Planete aPlanete : carte.getPlanetes()) {
				if (aPlanete.getId() != source.getId()) {

					int aScore = scoring.getScore(carte, source, aPlanete);
					if (aScore > maxScore) {
						maxScore = aScore;
						destination = aPlanete;
					}

				}

			}

			int nbVaisseau = getNbVaisseauxAEnvoyer(source, destination);
			Flotte flotte = new Flotte();
			flotte.setPlaneteDestination(destination.getId());

			flotte.setVaisseaux(nbVaisseau);
			flotte.setPlaneteSource(source.getId());
			int nbTour = carte.getTrajetNbTour(source, destination);
			flotte.setToursTotals(nbTour);
			flotte.setToursRestants(nbTour);

			EnvoiFlotte ordre = new EnvoiFlotte(flotte);
			source.remPopulation(nbVaisseau);
			response.addOrdre(ordre);
			carte.addFlotte(flotte);

		}

		return response;

	}

	private int getNbVaisseauxAEnvoyer(Planete source, Planete destination) {
		if (source.getPopulation() < 3) {
			return 0;
		}

		return Math.min(source.getPopulation(), destination.getPopulation());

	}

}
