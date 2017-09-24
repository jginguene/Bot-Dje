package fr.ginguene.onepoint.hackathon.bot;

import fr.ginguene.onepoint.hackathon.Carte;
import fr.ginguene.onepoint.hackathon.IBot;
import fr.ginguene.onepoint.hackathon.Planete;
import fr.ginguene.onepoint.hackathon.Response;
import fr.ginguene.onepoint.hackathon.ordre.EnvoiFlotte;

public class BotPremierTour implements IBot {

	private final static int NB_VOISINE_TO_EXPLORE = 10;

	@Override
	public Response getResponse(Carte carte) {

		Response response = new Response();

		Planete source = carte.getMesPlanetes().get(0);

		for (Planete destination : carte.getVoisines(source, NB_VOISINE_TO_EXPLORE)) {
			EnvoiFlotte envoiFlotte = new EnvoiFlotte(source, destination, 1);
			response.addOrdre(envoiFlotte);
		}

		return response;
	}

}
