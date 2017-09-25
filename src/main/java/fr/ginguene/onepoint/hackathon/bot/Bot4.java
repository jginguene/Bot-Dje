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

		List<Planete> mesPlanetes = carte.getMesPlanetes();

		for (Planete source : mesPlanetes) {

			System.out.println(source + "=> " + source.getPopulation());

			if (source.getPopulation() > 5) {

				boolean scoreOptimisation = (carte.getPlanetesEnnemies().size() == 1
						&& carte.getConfiguration().getTour() > 50);

				System.out.println(scoreOptimisation + "=> " + scoreOptimisation);

				Planete voisine = carte.getVoisines(source, 1).get(0);

				// Mode Bombe
				if (!scoreOptimisation) {
					int nbVaisseauEnnemi = carte.getFlotteEnnemie(source.getId());
					int nbVaisseau = source.getPopulation() - 1;
					if (nbVaisseauEnnemi > 0 && source.getPopulation() < Math.min(160, source.getPopulationMax())) {
						System.out.println("Mode bombe: " + source);
						continue;
					}

					// Mode protection
					int nbVaisseauEnnemiDuVoisin = carte.getFlotteEnnemie(voisine.getId());
					if (nbVaisseauEnnemiDuVoisin > 0) {
						System.out.println("Mode protection: " + source);
						EnvoiFlotte ordre = new EnvoiFlotte(source, voisine, nbVaisseau);
						source.remPopulation(nbVaisseau);
						response.addOrdre(ordre);
						carte.addFlotte(ordre.getFlotte());
						continue;

					}

					if (source.getTerraformation() > 0) {
						System.out.println("Terraformation en cours " + source + " -> " + source.getTerraformation());
						continue;
					}

					if (source.isTerraformable()) {
						int nbEnnemie = 0;
						for (Planete aPlanete : carte.getVoisines(source, 6)) {
							if (aPlanete.getProprietaire() > Constantes.MOI) {
								nbEnnemie++;
							}
						}
						if (nbEnnemie == 0) {
							Terraformation terraformation = new Terraformation();
							terraformation.setPlanete(source);
							response.addOrdre(terraformation);
							System.out.println("Lancement de la terraformation de " + source);
							continue;
						}
					}

					// Soi la planete la plus proche est ennemie, on prépare une
					// megabombe
					if (voisine.getProprietaire() > Constantes.MOI) {
						if (source.getPopulation() < source.getPopulationMax() - 10) {
							System.out.println("Megabombe en cours " + source);
							continue;
						} else if (!scoreOptimisation) {
							System.out.println("Lancement de la Megabombe: " + source);
							nbVaisseau = source.getPopulation() / 2;
							EnvoiFlotte ordre = new EnvoiFlotte(source, voisine, nbVaisseau);
							source.remPopulation(nbVaisseau);
							response.addOrdre(ordre);
							carte.addFlotte(ordre.getFlotte());
							continue;
						}
					}
				}

				if (source.getPopulation() > 140 || source.getPopulation() == source.getPopulationMax()) {
					System.out.println("Lancement de la bombe: " + source);

					int nbVaisseau = source.getPopulation() / 2;
					Planete destination = null;
					if (scoreOptimisation) {
						List<Planete> neutres = carte.getPlanetes(Constantes.Neutre);
						if (neutres.size() != 0) {
							destination = neutres.get(0);
						}
					} else {
						destination = getDestinationForBomb(carte, source, nbVaisseau);
					}

					if (destination != null) {
						EnvoiFlotte ordre = new EnvoiFlotte(source, destination, nbVaisseau);
						source.remPopulation(nbVaisseau);
						response.addOrdre(ordre);
						carte.addFlotte(ordre.getFlotte());
						continue;
					}
				}

				if (scoreOptimisation) {
					continue;
				}

				// Cas standards;
				Planete destination = null;
				int minScore = 0;
				for (Planete aPlanete : carte.getPlanetesEtrangeres()) {
					int aDistance = carte.getTrajetNbTour(source, aPlanete);

					if (aPlanete.getPopulation() - carte.getMesFlottes(aPlanete.getId()) > -1 * aDistance / 2) {

						int aScore = aDistance * 4 + aPlanete.getPopulation()
								+ carte.getFlottesEnnemiesFrom(aPlanete.getId());

						if (aScore < minScore || destination == null) {
							destination = aPlanete;
							minScore = aScore;
						}
					}

				}

				if (destination != null) {
					int nbVaisseau = source.getPopulation() - 1;
					System.out.println("Desination: " + source + ";nbVaisseau:" + nbVaisseau);
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