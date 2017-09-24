package fr.ginguene.onepoint.hackathon.bot;

import fr.ginguene.onepoint.hackathon.Carte;
import fr.ginguene.onepoint.hackathon.IBot;
import fr.ginguene.onepoint.hackathon.Planete;
import fr.ginguene.onepoint.hackathon.Response;
import fr.ginguene.onepoint.hackathon.ordre.EnvoiFlotte;

public class BotSecondTour implements IBot {

	private final static int NB_VOISINE_TO_EXPLORE = 5;

	@Override
	public Response getResponse(Carte carte) {

		Response response = new Response();

		Planete source = carte.getMesPlanetes().get(0);

		int nbPopRestante = source.getPopulation() - 1;

		for (Planete destination : carte.getVoisines(source, NB_VOISINE_TO_EXPLORE)) {

			if (destination.getPopulation() + 1 < nbPopRestante) {

				int nbVaisseaux = Math.max(nbPopRestante, destination.getPopulation() + 1);
				EnvoiFlotte envoiFlotte = new EnvoiFlotte(source, destination, nbVaisseaux);
				response.addOrdre(envoiFlotte);
				nbPopRestante -= nbVaisseaux;
				source.remPopulation(nbVaisseaux);
			}

		}

		return response;
	}

}
