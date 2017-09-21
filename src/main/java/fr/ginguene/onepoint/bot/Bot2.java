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

		for (Planete planete : mesPlanetes) {

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

				while (planete.getPopulation() > 10) {

					Planete destination = null;
					List<Planete> exclues = new ArrayList<Planete>();

					while (destination == null) {

						Planete ennemie = carte.getEnnemiLaPlusProche(planete, exclues);

						if (carte.getMesFlottes(ennemie) > ennemie.getPopulation()) {
							exclues.add(ennemie);
						} else {
							destination = ennemie;
						}
					}

					EnvoiFlotte ordre = new EnvoiFlotte();
					ordre.setOrigine(planete);
					ordre.setDestination(destination);

					int nbVaisseaux = this.getNbVaisseauxAEnvoyer(planete, destination);

					ordre.setPopulation(nbVaisseaux);
					planete.remPopulation(nbVaisseaux);

					response.addOrdre(ordre);
					System.out.println(planete + ": Envoi de " + nbVaisseaux + "  vers la planete " + destination);

				}
			}

		}

		return response;

	}

	private int getNbVaisseauxAEnvoyer(Planete source, Planete destination) {

		if (source.getPopulation() > 5) {
			return Math.min(source.getPopulation() - 5, destination.getPopulation());
		} else {
			return 0;
		}

	}

	public boolean hasToBeTerraformed(Carte carte, Planete planete) {
		List<Planete> mesTerraformation = carte.getMesTerraformations();
		List<Planete> mesPlanetes = carte.getMesPlanetes();

		return mesPlanetes.size() > 5 && mesTerraformation.size() == 0 && planete.isTerraformable();
	}

}
