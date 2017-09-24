package fr.ginguene.onepoint.hackathon.bot;

import java.util.List;

import fr.ginguene.onepoint.hackathon.Carte;
import fr.ginguene.onepoint.hackathon.Constantes;
import fr.ginguene.onepoint.hackathon.IBot;
import fr.ginguene.onepoint.hackathon.Planete;
import fr.ginguene.onepoint.hackathon.Response;
import fr.ginguene.onepoint.hackathon.ordre.EnvoiFlotte;

public class Bot4 implements IBot {

	private BotPremierTour botPremierTour = new BotPremierTour();
	private BotSecondTour botSecondTour = new BotSecondTour();

	private Planete getDestinationForBomb(Carte carte, Planete source, int bombSize) {

		for (Planete aPlanete : carte.getPlanetesOrderByDistance(source)) {
			if (aPlanete.getProprietaire() != Constantes.MOI && aPlanete.getPopulation() < bombSize) {
				return aPlanete;
			}
		}
		return null;

	}

	public Response getResponse(Carte carte) {

		Response response = new Response();

		System.out.println("Tour " + carte.getConfiguration().getTour());

		if (carte.getConfiguration().getTour() == 0) {
			System.out.println("1er tour");
			return botPremierTour.getResponse(carte);
		}

		List<Planete> mesPlanetes = carte.getPlanetes(Constantes.MOI);

		for (Planete source : mesPlanetes) {

			if (source.getPopulation() > 5) {

				// Mode Bombe
				int nbVaisseauEnnemi = carte.getFlotte(Constantes.Ennemi, source.getId());
				int nbVaisseau = source.getPopulation() - 1;
				if (nbVaisseauEnnemi > 0) {
					nbVaisseau = 0;
					if (source.getPopulation() > 140) {
						nbVaisseau = source.getPopulation() / 2;
						Planete destination = getDestinationForBomb(carte, source, nbVaisseau);
						EnvoiFlotte ordre = new EnvoiFlotte(source, destination, nbVaisseau);
						source.remPopulation(nbVaisseau);
						response.addOrdre(ordre);
						carte.addFlotte(ordre.getFlotte());
						break;
					}

				}

				if (source.getPopulation() < 30) {

					// Cas standards;
					Planete destination = null;
					int minScore = 0;
					for (Planete aPlanete : carte.getPlanetesEtrangeres()) {

						if (aPlanete.getPopulation() - carte.getMesFlottes(aPlanete.getId()) > -20) {

							int aScore = carte.getTrajetNbTour(source, aPlanete) + aPlanete.getPopulation()
									+ carte.getFlottesEnnemiesFrom(aPlanete.getId());
							;

							if (aScore < minScore || destination == null) {
								destination = aPlanete;
								minScore = aScore;
							}
						}

					}

					System.out.println("cible: " + destination);

					EnvoiFlotte ordre = new EnvoiFlotte(source, destination, nbVaisseau);
					source.remPopulation(nbVaisseau);
					response.addOrdre(ordre);
					carte.addFlotte(ordre.getFlotte());
				}

			}

		}

		return response;

	}

}
