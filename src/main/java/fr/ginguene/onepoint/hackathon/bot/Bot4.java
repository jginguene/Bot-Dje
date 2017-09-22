package fr.ginguene.onepoint.hackathon.bot;

import java.util.List;

import fr.ginguene.onepoint.hackathon.Carte;
import fr.ginguene.onepoint.hackathon.Constantes;
import fr.ginguene.onepoint.hackathon.Flotte;
import fr.ginguene.onepoint.hackathon.IBot;
import fr.ginguene.onepoint.hackathon.Planete;
import fr.ginguene.onepoint.hackathon.Response;
import fr.ginguene.onepoint.hackathon.ordre.EnvoiFlotte;

public class Bot4 implements IBot {

	public Response getResponse(Carte carte) {

		Response response = new Response();

		// System.out.println("Nombre de planete:" +
		// carte.getPlanetes().size());

		List<Planete> mesPlanetes = carte.getPlanetes(Constantes.MOI);

		for (Planete source : mesPlanetes) {

			Planete destination = null;

			for (Planete aPlanete : carte.getPlanetesOrderByDistance(source)) {
				if (aPlanete.getProprietaire() != Constantes.MOI) {
					int nbSentVaisseau = carte.getMesFlottes(aPlanete.getId());

					if (aPlanete.getPopulation() < source.getPopulation() + nbSentVaisseau) {

						int nbVaisseau = aPlanete.getPopulation() - nbSentVaisseau;
						Flotte flotte = new Flotte();
						flotte.setPlaneteDestination(aPlanete.getId());

						flotte.setVaisseaux(nbVaisseau);
						flotte.setPlaneteSource(source.getId());

						// int nbTour = carte.getTrajetNbTour(source,
						// destination);
						// flotte.setToursTotals(nbTour);
						// flotte.setToursRestants(nbTour);

						EnvoiFlotte ordre = new EnvoiFlotte(flotte);
						source.remPopulation(nbVaisseau);
						response.addOrdre(ordre);
						carte.addFlotte(flotte);
					}
				}

			}

			if (source.getPopulation() > 5) {
				for (Planete aPlanete : carte.getPlanetesOrderByDistance(source)) {
					if (aPlanete.getProprietaire() != Constantes.MOI) {
						int nbVaisseau = aPlanete.getPopulation() - 1;
						Flotte flotte = new Flotte();
						flotte.setPlaneteDestination(aPlanete.getId());

						flotte.setVaisseaux(nbVaisseau);
						flotte.setPlaneteSource(source.getId());
						// int nbTour = carte.getTrajetNbTour(source,
						// destination);
						// flotte.setToursTotals(nbTour);
						// flotte.setToursRestants(nbTour);

						EnvoiFlotte ordre = new EnvoiFlotte(flotte);
						source.remPopulation(nbVaisseau);
						response.addOrdre(ordre);
						carte.addFlotte(flotte);
						break;
					}
				}
			}

		}

		return response;

	}

	private int getNbVaisseauxAEnvoyer(Planete source, Planete destination) {
		if (source.getPopulation() < 3) {
			return 0;
		}

		return Math.min(source.getPopulation() - 1, destination.getPopulation() + 1);

	}

}
