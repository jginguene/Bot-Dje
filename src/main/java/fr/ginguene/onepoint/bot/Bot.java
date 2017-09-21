package fr.ginguene.onepoint.bot;

import java.util.List;

import fr.ginguene.onepoint.bot.ordre.EnvoiFlotte;
import fr.ginguene.onepoint.bot.ordre.Terraformation;

public class Bot implements IBot {

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

				List<Planete> voisines = carte.getVoisines(planete);

				// On procède d'abords aux planète enemies
				for (Planete aPlanete : voisines) {
					if (aPlanete.getProprietaire() != Constantes.MOI) {

						int nbVaisseaux = getNbVaisseauxAEnvoyer(planete, aPlanete);

						EnvoiFlotte ordre = new EnvoiFlotte();
						ordre.setOrigine(planete);
						ordre.setDestination(aPlanete);
						ordre.setPopulation(nbVaisseaux);
						planete.remPopulation(nbVaisseaux);
						response.addOrdre(ordre);
						System.out.println(planete + ": envoi de " + nbVaisseaux + "  vers la planete " + aPlanete);
					}
				}

				// On envoi les renforts à la planete amis ayant l'ennemi le
				// plus proche
				if (planete.getPopulation() > 10) {
					Planete ennemie = carte.getEnnemiLaPlusProche(planete);
					Planete amie = null;
					float distance = -1;
					for (Planete aPlanete : voisines) {
						if (aPlanete.getProprietaire() == Constantes.MOI) {
							float aDistance = aPlanete.calcDistance(ennemie);
							if (amie == null || aDistance > distance) {
								distance = aDistance;
								amie = aPlanete;
							}

						}
					}

					if (amie != null) {
						EnvoiFlotte ordre = new EnvoiFlotte();
						ordre.setOrigine(planete);

						float distanceAmiEnnemi = amie.calcDistance(ennemie);
						float distancePlaneteEnnemi = planete.calcDistance(ennemie);
						if (distanceAmiEnnemi > distancePlaneteEnnemi) {
							ordre.setDestination(amie);
						} else {
							ordre.setDestination(ennemie);
						}

						ordre.setDestination(amie);
						int nbVaisseaux = Math.max(3, planete.getPopulation());
						ordre.setPopulation(nbVaisseaux);
						planete.remPopulation(nbVaisseaux);
						response.addOrdre(ordre);
						System.out.println(planete + ": renfort de " + nbVaisseaux + "  vers la planete " + amie);

					}
				}

			}

		}

		return response;

	}

	private int getNbVaisseauxAEnvoyer(Planete source, Planete destination) {
		int res = 0;

		res = Math.max(source.getPopulation(), destination.getPopulation());

		res = Math.min(source.getPopulation(), res);

		return res;
	}

	public boolean hasToBeTerraformed(Carte carte, Planete planete) {
		List<Planete> mesTerraformation = carte.getMesTerraformations();
		List<Planete> mesPlanetes = carte.getMesPlanetes();

		return mesPlanetes.size() > 5 && mesTerraformation.size() == 0 && planete.isTerraformable();
	}

}
