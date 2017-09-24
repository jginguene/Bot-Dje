package fr.ginguene.onepoint.hackathon.bot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.ginguene.onepoint.hackathon.Carte;
import fr.ginguene.onepoint.hackathon.Constantes;
import fr.ginguene.onepoint.hackathon.IBot;
import fr.ginguene.onepoint.hackathon.Planete;
import fr.ginguene.onepoint.hackathon.Response;
import fr.ginguene.onepoint.hackathon.ordre.EnvoiFlotte;

public class Bot4 implements IBot {

	private BotPremierTour botPremierTour = new BotPremierTour();
	private BotSecondTour botSecondTour = new BotSecondTour();

	private Planete lastDefaultDestination = null;

	private Planete getDefaultDestination(Carte carte) {

		if (lastDefaultDestination != null) {
			if (lastDefaultDestination.getPopulation() + carte.getFlottesEnnemies(lastDefaultDestination.getId())
					- carte.getMesFlottes(lastDefaultDestination.getId()) < -50) {
				return lastDefaultDestination;
			}

		}

		// 1er tri par ditance;
		Map<Planete, Integer> planeteDistance = new HashMap<>();
		for (Planete aPlanete : carte.getPlanetesEtrangere()) {
			for (Planete myPlanete : carte.getMesPlanetes()) {
				if (!planeteDistance.containsKey(aPlanete)) {
					planeteDistance.put(aPlanete, 0);
				}
				planeteDistance.put(aPlanete,
						planeteDistance.get(aPlanete) + carte.getTrajetNbTour(myPlanete, aPlanete));

			}
		}

		lastDefaultDestination = null;
		int minScore = 0;
		for (Planete aPlanete : carte.getPlanetesEtrangere()) {
			int score = aPlanete.getPopulation() + planeteDistance.get(aPlanete)
					+ carte.getFlottesEnnemies(aPlanete.getId());

			if (score < minScore || lastDefaultDestination == null) {
				lastDefaultDestination = aPlanete;
				minScore = score;
			}

		}

		return lastDefaultDestination;

	}

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

		if (carte.getConfiguration().getTour() == 1) {
			System.out.println("1eme tour");
			return botSecondTour.getResponse(carte);
		}

		List<Planete> mesPlanetes = carte.getPlanetes(Constantes.MOI);

		Planete defaultDestination = this.getDefaultDestination(carte);

		for (Planete source : mesPlanetes) {

			int i = 0;

			// Démarrage
			/*
			 * if (carte.getConfiguration().getTour() < 20) { for (Planete
			 * aPlanete : carte.getPlanetesOrderByDistance(source)) { if
			 * (aPlanete.getProprietaire() != Constantes.MOI && i < 5) { int
			 * nbSentVaisseau = carte.getMesFlottes(aPlanete.getId());
			 * 
			 * if (aPlanete.getPopulation() > nbSentVaisseau &&
			 * aPlanete.getPopulation() < source.getPopulation() +
			 * nbSentVaisseau + 1) {
			 * 
			 * int nbVaisseau = 1 + aPlanete.getPopulation() - nbSentVaisseau;
			 * 
			 * if (nbVaisseau >= source.getPopulation()) { nbVaisseau =
			 * source.getPopulation() - 1; }
			 * 
			 * Flotte flotte = new Flotte();
			 * flotte.setPlaneteDestination(aPlanete.getId());
			 * 
			 * flotte.setVaisseaux(nbVaisseau);
			 * flotte.setPlaneteSource(source.getId());
			 * 
			 * EnvoiFlotte ordre = new EnvoiFlotte(flotte);
			 * source.remPopulation(nbVaisseau); response.addOrdre(ordre);
			 * carte.addFlotte(flotte); }
			 * 
			 * i++; } break;
			 * 
			 * }
			 * 
			 * }
			 */

			if (source.getPopulation() > 5) {
				Planete destination = defaultDestination;

				// Mode territoire ennemi
				// On ne fait rien dans ce cas là
				int voisineEnnemie = 0;
				for (Planete voisine : carte.getVoisines(source, 3)) {
					if (voisine.getProprietaire() > Constantes.MOI) {
						voisineEnnemie++;
					}
					if (voisineEnnemie > 2) {
						break;
					}

				}

				// Mode Bombe
				int nbVaisseauEnnemi = carte.getFlotte(Constantes.Ennemi, source.getId());
				int nbVaisseau = source.getPopulation() - 1;
				if (nbVaisseauEnnemi > 0) {
					nbVaisseau = 0;
					if (source.getPopulation() > 140) {
						nbVaisseau = source.getPopulation() / 2;
						destination = getDestinationForBomb(carte, source, nbVaisseau);
					}

				}

				EnvoiFlotte ordre = new EnvoiFlotte(source, destination, nbVaisseau);
				source.remPopulation(nbVaisseau);
				response.addOrdre(ordre);
				carte.addFlotte(ordre.getFlotte());

			}

		}

		return response;

	}

}
