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

	private Planete getDestination(Carte carte, int population) {
		Planete defaultDesination = null;
		int minDist = -1;
		for (Planete aPlanete : carte.getPlanetes()) {

			int nbSentVaisseau = carte.getMesFlottes(aPlanete.getId());
			int nbOtherVaisseau = carte.getFlotte(Constantes.Ennemi, aPlanete.getId());

			if (aPlanete.getProprietaire() != Constantes.MOI && aPlanete.getPopulation() < population) {
				int dist = 0;
				for (Planete myPlanete : carte.getMesPlanetes()) {
					dist += aPlanete.calcDistance(myPlanete);
				}
				if ((defaultDesination == null || minDist > dist)
						&& nbSentVaisseau < aPlanete.getPopulation() + nbOtherVaisseau)

				{
					minDist = dist;
					defaultDesination = aPlanete;
				}

			}

		}
		return defaultDesination;
	}

	public Response getResponse(Carte carte) {

		Response response = new Response();

		// System.out.println("Nombre de planete:" +
		// carte.getPlanetes().size());

		List<Planete> mesPlanetes = carte.getPlanetes(Constantes.MOI);

		Planete defaultDestination = null;
		int pop = 10;

		while (defaultDestination == null) {
			defaultDestination = getDestination(carte, pop);
			pop += 20;
		}

		for (Planete source : mesPlanetes) {

			if (carte.getFlotte(Constantes.Ennemi, source.getId()) > 0) {
				break;
			}

			int i = 0;
			if (carte.getConfiguration().getTour() < 20) {
				for (Planete aPlanete : carte.getPlanetesOrderByDistance(source)) {
					if (aPlanete.getProprietaire() != Constantes.MOI && i < 5) {
						int nbSentVaisseau = carte.getMesFlottes(aPlanete.getId());

						if (aPlanete.getPopulation() > nbSentVaisseau
								&& aPlanete.getPopulation() < source.getPopulation() + nbSentVaisseau + 1) {

							int nbVaisseau = 1 + aPlanete.getPopulation() - nbSentVaisseau;

							if (nbVaisseau >= source.getPopulation()) {
								nbVaisseau = source.getPopulation() - 1;
							}

							Flotte flotte = new Flotte();
							flotte.setPlaneteDestination(aPlanete.getId());

							flotte.setVaisseaux(nbVaisseau);
							flotte.setPlaneteSource(source.getId());

							EnvoiFlotte ordre = new EnvoiFlotte(flotte);
							source.remPopulation(nbVaisseau);
							response.addOrdre(ordre);
							carte.addFlotte(flotte);
						}

						i++;
					}
				}

			}

			if (source.getPopulation() > 5) {
				int nbVaisseau = source.getPopulation() - 1;
				Flotte flotte = new Flotte();
				flotte.setPlaneteDestination(defaultDestination.getId());

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

		return response;

	}

	private int getNbVaisseauxAEnvoyer(Planete source, Planete destination) {
		if (source.getPopulation() < 3) {
			return 0;
		}

		return Math.min(source.getPopulation() - 1, destination.getPopulation() + 1);

	}

}
