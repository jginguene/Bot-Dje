package fr.ginguene.onepoint.hackathon.bot;

import java.util.List;

import fr.ginguene.onepoint.hackathon.Carte;
import fr.ginguene.onepoint.hackathon.Constantes;
import fr.ginguene.onepoint.hackathon.IBot;
import fr.ginguene.onepoint.hackathon.Planete;
import fr.ginguene.onepoint.hackathon.PlaneteStatus;
import fr.ginguene.onepoint.hackathon.Response;
import fr.ginguene.onepoint.hackathon.action.MegabombeStrategie;
import fr.ginguene.onepoint.hackathon.action.TerraformationStrategie;
import fr.ginguene.onepoint.hackathon.ordre.EnvoiFlotte;
import fr.ginguene.onepoint.hackathon.ordre.Terraformation;

public class Bot5 implements IBot {

	private BotPremierTour botPremierTour = new BotPremierTour();

	private TerraformationStrategie terraformationStategie = new TerraformationStrategie();

	private MegabombeStrategie megabombeStrategie = new MegabombeStrategie();

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

		for (Planete source : mesPlanetes) {

			System.out.println(source + "=> population " + source.getPopulation() + "/" + source.getPopulationMax());

			boolean isScoreOptimizing = (carte.getPlanetesEnnemies().size() <= 4
					&& carte.getConfiguration().getTour() > 100);

			if (isScoreOptimizing) {
				System.out.println("=> " + isScoreOptimizing);
			}

			if (terraformationStategie.execute(response, source, carte, isScoreOptimizing, true)) {
				continue;
			}

			if (megabombeStrategie.execute(response, source, carte, isScoreOptimizing, true)) {
				continue;
			}

			// Mode Larguage de Bombe
			if (bombarde(response, source, carte, isScoreOptimizing)) {
				continue;
			}

			// Cas standards;
			if (aidePlanete(response, source, carte, isScoreOptimizing)) {
				continue;
			}

			if (attaquePlaneteNeutre(response, source, carte, isScoreOptimizing)) {
				continue;
			}

			if (attaquePlaneteEtrangereLaPlusProche(response, source, carte, isScoreOptimizing)) {
				continue;
			}

		}

		return response;

	}

	private boolean terraforme(Response response, Planete source, Carte carte, boolean isOptimizingScore) {
		if (source.getTerraformation() > 0) {
			System.out.println("Terraformation en cours " + source + " -> " + source.getTerraformation());
			return true;
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
				return true;
			} else {
				System.out.println(source + " terraformable => " + nbEnnemie + " ennemies");
			}
		}

		return false;

	}

	private boolean bombarde(Response response, Planete source, Carte carte, boolean isOptimizingScore) {

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
			if (isOptimizingScore) {
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
				return true;
			}
		}

		return false;
	}

	private boolean aidePlanete(Response response, Planete source, Carte carte, boolean isOptimizingScore) {
		if (isOptimizingScore) {
			return false;
		}

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

	private boolean attaquePlaneteEtrangereLaPlusProche(Response response, Planete source, Carte carte,
			boolean isOptimizingScore) {

		if (isOptimizingScore) {
			return false;
		}
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
					return true;
				}
			}
		}

		return false;
	}

	private boolean attaquePlaneteNeutre(Response response, Planete source, Carte carte, boolean isOptimizingScore) {

		if (isOptimizingScore) {
			return false;
		}

		Planete destination = null;
		int minCout = -1;
		int nbPop = -1;

		for (Planete aPlanete : carte.getPlanetesOrderByDistance(source)) {
			if (aPlanete.getStatus() == PlaneteStatus.Neutre) {

				Planete ennemiLaPlusProche = carte.getPlaneteLaPlusProche(aPlanete, PlaneteStatus.Ennemie);
				float distanceEnnemi = carte.getDistance(ennemiLaPlusProche, aPlanete);
				float distanceSource = carte.getDistance(source, aPlanete);
				int mesFlottes = carte.getMesFlottes(aPlanete);
				int flottesEnnemie = carte.getFlottesEnnemie(aPlanete);

				int aCout = aPlanete.getPopulation() - mesFlottes + flottesEnnemie + 1
						+ carte.getTrajetNbTour(source, aPlanete);

				System.out.println(aPlanete + " => aCout[" + aCout + "] = POP[" + aPlanete.getPopulation() + "]" + " - "
						+ mesFlottes + " + " + flottesEnnemie + " + " + 1 + " NbTour["
						+ +carte.getTrajetNbTour(source, aPlanete) + "]");

				if ((destination == null || aCout < minCout && aCout > 0) && distanceEnnemi < distanceSource) {
					destination = aPlanete;
					minCout = aCout;
					nbPop = aPlanete.getPopulation() - mesFlottes + flottesEnnemie + 1;
				}
			}
		}

		if (destination != null) {
			int nbVaisseau = Math.min(minCout, source.getPopulation() - 1);
			EnvoiFlotte ordre = new EnvoiFlotte(source, destination, nbVaisseau);
			source.remPopulation(nbVaisseau);
			response.addOrdre(ordre);
			carte.addFlotte(ordre.getFlotte());

			System.out.println("attaquePlaneteNeutre: " + source + " -> " + destination + " [" + nbVaisseau + "]");

		}
		return false;

	}

}