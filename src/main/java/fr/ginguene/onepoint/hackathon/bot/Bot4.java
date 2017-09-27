package fr.ginguene.onepoint.hackathon.bot;

import java.util.List;

import fr.ginguene.onepoint.hackathon.Carte;
import fr.ginguene.onepoint.hackathon.Constantes;
import fr.ginguene.onepoint.hackathon.IBot;
import fr.ginguene.onepoint.hackathon.Planete;
import fr.ginguene.onepoint.hackathon.PlaneteStatus;
import fr.ginguene.onepoint.hackathon.Response;
import fr.ginguene.onepoint.hackathon.ordre.EnvoiFlotte;
import fr.ginguene.onepoint.hackathon.ordre.Terraformation;

public class Bot4 implements IBot {

	private BotPremierTour botPremierTour = new BotPremierTour();

	private Planete getDestinationForBomb(Carte carte, Planete source, int bombSize) {

		if (carte.getPlanetes(Constantes.NEUTRE).isEmpty()) {
			for (Planete aPlanete : carte.getPlanetesOrderByDistance(source)) {
				if (aPlanete.getProprietaire() != Constantes.AMI) {
					return aPlanete;
				}
			}
		}

		for (Planete aPlanete : carte.getPlanetesOrderByDistance(source)) {
			if (aPlanete.getProprietaire() != Constantes.AMI
					&& carte.getMesFlottes(aPlanete.getId()) < aPlanete.getPopulationMax()) {
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

		boolean allowBomb = mesPlanetes.size() > 4;

		for (Planete source : mesPlanetes) {

			System.out.println(source + "=> population " + source.getPopulation() + "/" + source.getPopulationMax());

			if (source.getPopulation() > 5) {

				boolean scoreOptimisation = (carte.getPlanetesEnnemies().size() <= 4
						&& carte.getConfiguration().getTour() > 100);

				System.out.println("scoreOptimisation=> " + scoreOptimisation);

				Planete voisine = carte.getVoisines(source, 1).get(0);

				if (!scoreOptimisation) {

					if (source.getTerraformation() > 0) {
						System.out.println("Terraformation en cours " + source + " -> " + source.getTerraformation());
						continue;
					}

					if (source.isTerraformable()) {
						int nbEnnemie = 0;
						for (Planete aPlanete : carte.getVoisines(source, 6)) {
							if (aPlanete.getStatus() != PlaneteStatus.Amie) {
								nbEnnemie++;
							}
						}

						if (nbEnnemie == 0) {
							Terraformation terraformation = new Terraformation();
							terraformation.setPlanete(source);
							response.addOrdre(terraformation);
							System.out.println("Lancement de la terraformation de " + source);
							continue;
						} else {
							System.out.println(source + " terraformable => " + nbEnnemie + " ennemies");
						}
					}

					// Si la planete la plus proche est ennemie, on prépare une
					// megabombe
					if (voisine.getStatus() == PlaneteStatus.Ennemie) {

						if (source.getPopulation() < voisine.getPopulation() + 40
								&& source.getPopulation() < source.getPopulationMax()) {
							System.out.println("Megabombe en cours " + source);
							continue;
						} else if (!scoreOptimisation) {
							System.out.println("Lancement de la Megabombe: " + source);
							int nbVaisseau = Math.min(source.getPopulation() - 10, voisine.getPopulation() + 10);
							EnvoiFlotte ordre = new EnvoiFlotte(source, voisine, nbVaisseau);
							source.remPopulation(nbVaisseau);
							response.addOrdre(ordre);
							carte.addFlotte(ordre.getFlotte());
							continue;
						}
					}

					// Mode chargement de Bombe
					// Si on est visé par une attaque ou que l'on n'a plus
					// d'ennemi proche
					int nbVaisseauEnnemi = carte.getFlotteEnnemie(source.getId());
					List<Planete> voisines = carte.getVoisines(source, 5);
					int nbVoisinesEtrangeres = 0;
					for (Planete aPlanete : voisines) {
						if (aPlanete.getProprietaire() != Constantes.AMI) {
							nbVoisinesEtrangeres++;
						}

					}

					if (allowBomb && (nbVaisseauEnnemi > 10 || nbVoisinesEtrangeres == 0)
							&& source.getPopulation() < Math.min(160, source.getPopulationMax() - 1)) {
						System.out.println("Mode bombe: " + source + "=> nbVoisinesEtrangeres:" + nbVoisinesEtrangeres
								+ ";nbVaisseauEnnemi:" + nbVaisseauEnnemi);
						continue;
					}

				}

				// Mode Largage de Bombe
				if (source.getPopulation() > 140 || source.getPopulation() > source.getPopulationMax() - 20) {

					int nbEnnemie = 0;
					for (Planete aPlanete : carte.getVoisines(source, 6)) {
						if (aPlanete.getStatus() != PlaneteStatus.Amie) {
							nbEnnemie++;
						}
					}

					int nbVaisseau = source.getPopulation() - 60;
					if (nbEnnemie == 0 && carte.getFlotteEnnemie(source.getId()) == 0) {
						nbVaisseau = source.getPopulation() - 10;
					}

					System.out.println("Lancement de la bombe: " + source + " => nbVaisseau:" + nbVaisseau);

					Planete destination = null;
					if (scoreOptimisation) {
						List<Planete> neutres = carte.getPlanetes(Constantes.NEUTRE);
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

				// Une planete neutre proche a été trouvée
				Planete destination = this.getDestinationNeutre(source, carte);
				if (destination != null) {
					int nbVaisseau = source.getPopulation() - 1;
					EnvoiFlotte ordre = new EnvoiFlotte(source, destination, nbVaisseau);
					source.remPopulation(nbVaisseau);
					response.addOrdre(ordre);
					carte.addFlotte(ordre.getFlotte());
					continue;
				}

				// Attaque par défaut d'une planete proche
				for (Planete aPlanete : carte.getPlanetesOrderByDistance(source)) {
					if (aPlanete.getStatus() != PlaneteStatus.Amie) {
						int nbVaisseau = source.getPopulation() - 1;
						EnvoiFlotte ordre = new EnvoiFlotte(source, aPlanete, nbVaisseau);
						source.remPopulation(nbVaisseau);
						response.addOrdre(ordre);
						carte.addFlotte(ordre.getFlotte());
						break;
					}
				}

			}

		}

		return response;

	}

	private Planete getDestinationNeutre(Planete source, Carte carte) {

		Planete destination = null;
		int minPopulation = -1;

		for (Planete aPlanete : carte.getPlanetesOrderByDistance(source)) {
			if (aPlanete.getStatus() == PlaneteStatus.Neutre) {

				Planete ennemiLaPlusProche = carte.getPlaneteLaPlusProche(aPlanete, PlaneteStatus.Ennemie);
				float distanceEnnemi = carte.getDistance(ennemiLaPlusProche, aPlanete);
				float distanceSource = carte.getDistance(source, aPlanete);
				int mesFlottes = carte.getMesFlottes(aPlanete);

				if (carte.getFlotteEnnemie(aPlanete.getId()) == 0 && distanceSource < distanceEnnemi
						&& aPlanete.getPopulation() - mesFlottes < -5
						&& (destination == null || destination.getPopulation() - mesFlottes < minPopulation)) {
					destination = aPlanete;
					minPopulation = destination.getPopulation() - mesFlottes;
				}
			}
		}

		return destination;

	}

}