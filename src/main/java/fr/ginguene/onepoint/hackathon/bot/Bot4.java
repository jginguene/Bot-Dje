package fr.ginguene.onepoint.hackathon.bot;

import java.util.List;

import fr.ginguene.onepoint.hackathon.Carte;
import fr.ginguene.onepoint.hackathon.Constantes;
import fr.ginguene.onepoint.hackathon.IBot;
import fr.ginguene.onepoint.hackathon.Planete;
import fr.ginguene.onepoint.hackathon.Response;
import fr.ginguene.onepoint.hackathon.ordre.EnvoiFlotte;
import fr.ginguene.onepoint.hackathon.ordre.Terraformation;

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
			return botPremierTour.getResponse(carte);
		}

		List<Planete> mesPlanetes = carte.getPlanetes(Constantes.MOI);

		for (Planete source : mesPlanetes) {

			if (source.getPopulation() > 5) {

				Planete voisine = carte.getVoisines(source, 1).get(0);

				if (source.getTerraformation() > 0) {
					System.out.println("Terraformation en cours " + source + " -> " + source.getTerraformation());
					break;
				}

				if (source.isTerraformable()) {
					int nbEnnemie = 0;
					for (Planete aPlanete : carte.getVoisines(source, 6)) {
						if (aPlanete.getProprietaire() != Constantes.MOI) {
							nbEnnemie++;
						}
					}
					if (nbEnnemie == 0) {
						Terraformation terraformation = new Terraformation();
						terraformation.setPlanete(source);
						response.addOrdre(terraformation);
						System.out.println("Lancement de la terraformation de " + source);
						break;

					}
				}

				// Soi la planete la plus proche est ennemie, on prépare une
				// megabombe
				if (voisine.getProprietaire() > Constantes.MOI) {

					if (source.getPopulation() < source.getPopulationMax() - 10) {
						System.out.println("Megabombe en cours " + source);
						break;
					} else {
						System.out.println("Lancement de la Megabombe: " + source);

						int nbVaisseau = source.getPopulation() / 2;
						EnvoiFlotte ordre = new EnvoiFlotte(source, voisine, nbVaisseau);
						source.remPopulation(nbVaisseau);
						response.addOrdre(ordre);
						carte.addFlotte(ordre.getFlotte());
						break;
					}
				}

				if (source.getPopulation() > 140 || source.getPopulation() == source.getPopulationMax()) {
					System.out.println("Lancement de la bombe: " + source);

					int nbVaisseau = source.getPopulation() / 2;
					Planete destination = getDestinationForBomb(carte, source, nbVaisseau);
					EnvoiFlotte ordre = new EnvoiFlotte(source, destination, nbVaisseau);
					source.remPopulation(nbVaisseau);
					response.addOrdre(ordre);
					carte.addFlotte(ordre.getFlotte());
					break;
				}

				// Mode Bombe
				int nbVaisseauEnnemi = carte.getFlotte(Constantes.Ennemi, source.getId());
				int nbVaisseau = source.getPopulation() - 1;
				if (nbVaisseauEnnemi > 0) {
					break;
				}

				// Cas standards;
				Planete destination = null;
				int minScore = 0;
				for (Planete aPlanete : carte.getPlanetesEtrangeres()) {

					// if (aPlanete.getPopulation() -
					// carte.getMesFlottes(aPlanete.getId()) > -20) {

					int aScore = carte.getTrajetNbTour(source, aPlanete) * 4 + aPlanete.getPopulation()
							+ carte.getFlottesEnnemiesFrom(aPlanete.getId());
					;

					if (aScore < minScore || destination == null) {
						destination = aPlanete;
						minScore = aScore;
					}
					// }

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
