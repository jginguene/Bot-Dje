package fr.ginguene.onepoint.bot;

import java.util.ArrayList;
import java.util.List;

import fr.ginguene.onepoint.bot.ordre.EnvoiFlotte;
import fr.ginguene.onepoint.bot.ordre.Terraformation;

public class Bot2 implements IBot {

	public Response getResponse(Carte carte) {

		Response response = new Response();

		System.out.println("Nombre de planete:" + carte.getPlanetes().size());

		List<Planete> mesPlanetes = carte.getPlanetes(Constantes.MOI);

		List<Planete> exclues = new ArrayList<>();

		for (Planete planete : mesPlanetes) {

			System.out.println("Analyse :" + planete);

			if (planete.getTerraformation() > 0) {
				System.out.println(planete + ": en cours de terraformation");
				continue;
			}

			if (hasToBeTerraformed(carte, planete)) {
				planete.terraforme();
				Terraformation terraformation = new Terraformation();
				terraformation.setPlanete(planete);
				response.addOrdre(terraformation);
				System.out.println(planete + ": lancement de la terraformation");
			} else {

				for (Planete aPlanete : carte.getPlanetesOrderByDistance(planete)) {

					if (planete.getPopulation() > aPlanete.getPopulation() + 1 && !exclues.contains(aPlanete)) {
						EnvoiFlotte ordre = new EnvoiFlotte();
						ordre.setOrigine(planete);
						ordre.setDestination(aPlanete);
						ordre.setPopulation(aPlanete.getPopulation() + 1);
						planete.remPopulation(aPlanete.getPopulation() + 1);
						response.addOrdre(ordre);
						exclues.add(aPlanete);
					}

				}

			}

		}

		return response;

	}

	private int getNbVaisseauxAEnvoyer(Planete source, Planete destination) {
		if (source.getPopulation() < 3) {
			return 0;
		}

		return Math.min(source.getPopulation(), destination.getPopulation());

	}

	public boolean hasToBeTerraformed(Carte carte, Planete planete) {
		List<Planete> mesTerraformation = carte.getMesTerraformations();
		List<Planete> mesPlanetes = carte.getMesPlanetes();

		return mesPlanetes.size() > 5 && mesTerraformation.size() == 0 && planete.isTerraformable();
	}

}
