package fr.ginguene.onepoint.hackathon.action;

import fr.ginguene.onepoint.hackathon.Carte;
import fr.ginguene.onepoint.hackathon.Planete;
import fr.ginguene.onepoint.hackathon.Response;
import fr.ginguene.onepoint.hackathon.ordre.EnvoiFlotte;

public class PremierTourStrategie extends AbstractStrategie {

	private final static int NB_VOISINE_TO_EXPLORE = 5;

	@Override
	public boolean execute(Response response, Planete source, Carte carte, boolean isOptimizingScore) {

		if (carte.getConfiguration().getTour() == 0) {

			int nbPopRestante = source.getPopulation() - 1;

			for (Planete destination : carte.getVoisines(source, 10)) {

				if (destination.getPopulation() + 1 < nbPopRestante && destination.getPopulation() < 20) {

					int nbVaisseaux = destination.getPopulation() + 1;
					EnvoiFlotte envoiFlotte = new EnvoiFlotte(carte, source, destination, nbVaisseaux);
					response.addOrdre(envoiFlotte);
					nbPopRestante -= nbVaisseaux;
					source.remPopulation(nbVaisseaux);
				}

			}

			for (Planete destination : carte.getVoisines(source, NB_VOISINE_TO_EXPLORE)) {

				if (destination.getPopulation() + 1 < nbPopRestante && destination.getPopulation() < 50) {

					int nbVaisseaux = destination.getPopulation() + 1;
					EnvoiFlotte envoiFlotte = new EnvoiFlotte(carte, source, destination, nbVaisseaux);
					response.addOrdre(envoiFlotte);
					nbPopRestante -= nbVaisseaux;
					source.remPopulation(nbVaisseaux);
				}

			}

			for (Planete destination : carte.getVoisines(source, NB_VOISINE_TO_EXPLORE)) {

				if (destination.getPopulation() + 1 < nbPopRestante) {

					int nbVaisseaux = destination.getPopulation() + 1;
					EnvoiFlotte envoiFlotte = new EnvoiFlotte(carte, source, destination, nbVaisseaux);
					response.addOrdre(envoiFlotte);
					nbPopRestante -= nbVaisseaux;
					source.remPopulation(nbVaisseaux);
				}

			}
			return true;
		} else {
			return false;
		}

	}

}
