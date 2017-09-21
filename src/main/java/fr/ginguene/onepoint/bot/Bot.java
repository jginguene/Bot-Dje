package fr.ginguene.onepoint.bot;

import java.util.ArrayList;
import java.util.List;

import fr.ginguene.onepoint.bot.ordre.EnvoiTroupe;
import fr.ginguene.onepoint.bot.ordre.Terraformation;

public class Bot implements IBot {

	List<Planete> planetesInterdite = new ArrayList<Planete>();

	public Response getResponse(Carte carte) {

		Response response = new Response();

		System.out.println("Nombre de planete:" + carte.getPlanetes().size());

		List<Planete> mesPlanetes = carte.getPlanetes(Constantes.MOI);

		for (Planete planete : mesPlanetes) {
			planetesInterdite.clear();

			if (planete.getTerraformation() > 0) {
				continue;
			}

			if (hasToBeTerraformed(carte, planete)) {
				planete.terraforme();
				Terraformation terraformation = new Terraformation();
				terraformation.setPlanete(planete);
				response.addOrdre(terraformation);
			} else {

				while (planete.getPopulation() > 10) {

					EnvoiTroupe ordre = new EnvoiTroupe();
					ordre.setOrigine(planete);

					Planete destination = selectPlanete(carte, planete);

					if (destination != null) {
						planetesInterdite.add(destination);

						System.out.println("==>Cible: " + destination + ";planete.getPopulation():"
								+ planete.getPopulation() + ";" + destination.getPopulation());

						int populationCible = Math.min(planete.getPopulation() - 10, destination.getPopulation());

						if (planete.getPopulation() > 100) {
							populationCible = 80;
						}

						System.out.println("==>Cible: " + destination + ";planete.getPopulation():"
								+ planete.getPopulation() + ";destination.getPopulation():"
								+ destination.getPopulation() + ";populationCible:" + populationCible);

						ordre.setPopulation(populationCible);
						ordre.setDestination(destination);
						planete.remPopulation(populationCible);
						response.addOrdre(ordre);
					} else {
						System.out.println("Aucune cible trouvée");
						break;
					}

				}

			}
		}

		return response;

	}

	public boolean hasToBeTerraformed(Carte carte, Planete planete) {
		List<Planete> mesTerraformation = carte.getMesTerraformations();
		List<Planete> mesPlanetes = carte.getMesPlanetes();

		return mesPlanetes.size() > 5 && mesTerraformation.size() == 0 && planete.isTerraformable();
	}

	public Planete selectPlanete(Carte carte, Planete planete) {

		Planete selectedPlanete = null;
		float distance = 0;
		for (Planete aPlanete : carte.getPlanetes()) {

			System.out.println("Analyse cible:" + aPlanete);
			// System.out.println("planetesInterdite:" + planetesInterdite);

			if (aPlanete.getProprietaire() != 1 && !planetesInterdite.contains(aPlanete)) {

				float aDistance = planete.calcDistance(aPlanete);
				System.out.println("aDistance:" + aDistance);
				System.out.println("selectedPlanete:" + selectedPlanete);
				System.out.println("distance:" + distance);

				if (selectedPlanete == null || aDistance < distance) {
					selectedPlanete = aPlanete;
					distance = aDistance;
				}
			}
		}
		return selectedPlanete;

	}

}
