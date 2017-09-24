package fr.ginguene.onepoint.hackathon.bot;

import fr.ginguene.onepoint.hackathon.Carte;
import fr.ginguene.onepoint.hackathon.IBot;
import fr.ginguene.onepoint.hackathon.Planete;
import fr.ginguene.onepoint.hackathon.Response;
import fr.ginguene.onepoint.hackathon.ordre.EnvoiFlotte;

public class BotPremierTour implements IBot {

	private final static int NB_VOISINE_TO_EXPLORE = 5;

	@Override
	public Response getResponse(Carte carte) {

		Response response = new Response();

		Planete source = carte.getMesPlanetes().get(0);

		int nbPopRestante = source.getPopulation() - 1;
		System.out.println("nbPopRestante: " + nbPopRestante);

		for (Planete destination : carte.getVoisines(source, NB_VOISINE_TO_EXPLORE)) {

			System.out.println("destination: " + destination);
			System.out.println("destination.getPopulation() + 1: " + destination.getPopulation() + 1);

			if (destination.getPopulation() + 1 < nbPopRestante) {

				int nbVaisseaux = destination.getPopulation();
				EnvoiFlotte envoiFlotte = new EnvoiFlotte(source, destination, nbVaisseaux);
				response.addOrdre(envoiFlotte);
				nbPopRestante -= nbVaisseaux;
				source.remPopulation(nbVaisseaux);
				System.out.println("nbVaisseaux: " + nbVaisseaux);
			}

		}

		return response;
	}

}
