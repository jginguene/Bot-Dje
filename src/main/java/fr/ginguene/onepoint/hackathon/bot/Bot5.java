package fr.ginguene.onepoint.hackathon.bot;

import java.util.List;

import fr.ginguene.onepoint.hackathon.Carte;
import fr.ginguene.onepoint.hackathon.Constantes;
import fr.ginguene.onepoint.hackathon.IBot;
import fr.ginguene.onepoint.hackathon.Planete;
import fr.ginguene.onepoint.hackathon.PlaneteStatus;
import fr.ginguene.onepoint.hackathon.Response;
import fr.ginguene.onepoint.hackathon.Stat;
import fr.ginguene.onepoint.hackathon.ordre.EnvoiFlotte;
import fr.ginguene.onepoint.hackathon.ordre.Terraformation;

public class Bot5 implements IBot {

	private BotPremierTour botPremierTour = new BotPremierTour();

	private Planete getDestinationForBomb(Carte carte, Planete source, int bombSize) {

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

			for (Planete planete : carte.getPlanetes()) {
				if (planete.getStatus() != PlaneteStatus.Amie) {
					System.out.println(
							carte.getDistance(planete, source) + " => " + carte.getTrajetNbTour(planete, source));
				}
			}

			if (source.getPopulation() > 5) {

				boolean scoreOptimisation = (carte.getPlanetesEnnemies().size() <= 4
						&& carte.getConfiguration().getTour() > 100);
				if (scoreOptimisation) {
					System.out.println("scoreOptimisation=> " + scoreOptimisation);
				}

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

					int nbVaisseauEnnemi = carte.getFlottesEnnemie(source.getId());
					if (voisine.getStatus() == PlaneteStatus.Ennemie && nbVaisseauEnnemi > 0) {

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
					/*
					 * List<Planete> voisines = carte.getVoisines(source, 5);
					 * int nbVoisinesEtrangeres = 0; for (Planete aPlanete :
					 * voisines) { if (aPlanete.getProprietaire() !=
					 * Constantes.AMI) { nbVoisinesEtrangeres++; }
					 * 
					 * }
					 */

					if (allowBomb && nbVaisseauEnnemi > source.getPopulation() - 10
							&& source.getPopulation() < Math.min(40, source.getPopulationMax() - 1)) {
						System.out.println("Mode bombe: " + source + "=> " + ";nbVaisseauEnnemi:" + nbVaisseauEnnemi);
						continue;
					}

				} else {
					if (source.getPopulation() == source.getPopulationMax()) {
						Planete destination = null;
						float distance = -1;

						for (Planete aPlanete : carte.getPlanetes()) {
							if (aPlanete.getStatus() != PlaneteStatus.Ennemie) {
								float aDistance = carte.getDistance(aPlanete, source);
								if (distance > aDistance || destination == null) {
									distance = aDistance;
									destination = aPlanete;
								}

							}
							int nbVaisseau = source.getPopulation() - 1;
							EnvoiFlotte ordre = new EnvoiFlotte(source, destination, nbVaisseau);
							source.remPopulation(nbVaisseau);
							response.addOrdre(ordre);
							carte.addFlotte(ordre.getFlotte());
							continue;
						}

					}
				}

				// Mode Larguage de Bombe
				if (source.getPopulation() > 140 || source.getPopulation() > source.getPopulationMax() - 20) {

					int nbEnnemie = 0;
					for (Planete aPlanete : carte.getVoisines(source, 6)) {
						if (aPlanete.getStatus() != PlaneteStatus.Amie) {
							nbEnnemie++;
						}
					}

					int nbVaisseau = source.getPopulation() - 60;

					if (nbVaisseau < 0 || (nbEnnemie == 0 && carte.getFlottesEnnemie(source.getId()) == 0)) {
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

				Stat stat = carte.getStatistique();

				// Mode guerre rangée
				/*
				 * if (stat.getNbAmies() > 8) {
				 * 
				 * // Les planete les plus loingtaines tirent par boulet de 20
				 * Stat sourceStat = carte.getStatistique(source, 4); if
				 * (sourceStat.getNbAmies() == 4 && source.getPopulation() > 20)
				 * { attaquePlaneteEtrangereLaPlusProche(response, source,
				 * carte); }
				 * 
				 * // Si on peut se joindre à une attaque en cours, on le fait
				 * for (Flotte flotte : carte.getFlotte(Constantes.AMI)) {
				 * Planete destination =
				 * carte.getPlanete(flotte.getPlaneteDestination()); int
				 * nbVaisseau =
				 * carte.getMesFlottes(flotte.getPlaneteDestination());
				 * 
				 * if (nbVaisseau < destination.getPopulationMax() + 20 &&
				 * flotte.getToursRestants() == carte.getTrajetNbTour(source,
				 * destination) + 1) { nbVaisseau = source.getPopulation() - 20;
				 * EnvoiFlotte ordre = new EnvoiFlotte(source, destination,
				 * nbVaisseau); source.remPopulation(nbVaisseau);
				 * response.addOrdre(ordre); carte.addFlotte(ordre.getFlotte());
				 * break; }
				 * 
				 * }
				 * 
				 * }
				 */

				// Une planete neutre proche a été trouvée
				if (!attaquePlaneteNeutre(response, source, carte)) {
					// Attaque par défaut d'une planete proche
					if (!aidePlanete(response, source, carte)) {
						attaquePlaneteEtrangereLaPlusProche(response, source, carte);
					}
				}

			}

		}

		return response;

	}

	private boolean aidePlanete(Response response, Planete source, Carte carte) {
		for (Planete aPlanete : carte.getPlanetesOrderByDistance(source)) {
			if (aPlanete.getStatus() == PlaneteStatus.Ennemie
					&& aPlanete.getPopulationMax() < carte.getMesFlottes(aPlanete) * 2) {

				int maFlotte = carte.getFlotte(Constantes.AMI, aPlanete.getId());
				if (maFlotte > 0) {

					int nbVaisseau = 5;
					EnvoiFlotte ordre = new EnvoiFlotte(source, aPlanete, nbVaisseau);
					source.remPopulation(nbVaisseau);
					response.addOrdre(ordre);
					carte.addFlotte(ordre.getFlotte());

					System.out.println("aidePlanete: " + source + " -> " + aPlanete + " [" + nbVaisseau + "]");
					return true;

				}
			}
		}

		return false;
	}

	private void attaquePlaneteEtrangereLaPlusProche(Response response, Planete source, Carte carte) {
		if (source.getPopulation() > 40) {

			for (Planete aPlanete : carte.getPlanetesOrderByDistance(source)) {
				if (aPlanete.getStatus() != PlaneteStatus.Amie
						&& aPlanete.getPopulationMax() < carte.getMesFlottes(aPlanete)) {

					int nbVaisseau = source.getPopulation() - 20;

					EnvoiFlotte ordre = new EnvoiFlotte(source, aPlanete, nbVaisseau);
					source.remPopulation(nbVaisseau);
					response.addOrdre(ordre);
					carte.addFlotte(ordre.getFlotte());

					System.out.println("attaquePlaneteEtrangereLaPlusProche: " + source + " -> " + aPlanete + " ["
							+ nbVaisseau + "]");
					break;
				}
			}
		}
	}

	private boolean attaquePlaneteNeutre(Response response, Planete source, Carte carte) {

		Planete destination = null;
		int minCount = -1;

		for (Planete aPlanete : carte.getPlanetesOrderByDistance(source)) {
			if (aPlanete.getStatus() == PlaneteStatus.Neutre) {

				Planete ennemiLaPlusProche = carte.getPlaneteLaPlusProche(aPlanete, PlaneteStatus.Ennemie);
				float distanceEnnemi = carte.getDistance(ennemiLaPlusProche, aPlanete);
				float distanceSource = carte.getDistance(source, aPlanete);
				int mesFlottes = carte.getMesFlottes(aPlanete);
				int flottesEnnemie = carte.getFlottesEnnemie(aPlanete);

				int aCout = aPlanete.getPopulation() - mesFlottes + flottesEnnemie + 1
						+ carte.getTrajetNbTour(source, aPlanete);

				if (aCout < minCount && aCout > 0 && distanceEnnemi < distanceSource) {
					destination = aPlanete;
					minCount = aCout;
				}
			}
		}

		if (destination != null) {
			int nbVaisseau = Math.min(minCount, source.getPopulation() - 1);
			EnvoiFlotte ordre = new EnvoiFlotte(source, destination, nbVaisseau);
			source.remPopulation(nbVaisseau);
			response.addOrdre(ordre);
			carte.addFlotte(ordre.getFlotte());

			System.out.println("attaquePlaneteNeutre: " + source + " -> " + destination + " [" + nbVaisseau + "]");

		}
		return false;

	}

}