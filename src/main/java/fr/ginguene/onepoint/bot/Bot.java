package fr.ginguene.onepoint.bot;

import java.util.ArrayList;
import java.util.List;

public class Bot {

	List<Planete> planetesInterdite = new ArrayList<Planete>();

	public Response getResponse(Carte carte) {

		Response response = new Response();
		
		planetesInterdite.clear();

		System.out.println("Nombre de planete:" + carte.getPlanetes().size());

		for (Planete planete : carte.getPlanetes()) {

			System.out.println("==>Planete " + planete.getId() + "; population:" + planete.getPopulation());
			
			if (planete.getProprietaire() == 1 ) {
				while (planete.getPopulation() > 10) {

					Ordre ordre = new Ordre();
					ordre.setOrigine(planete);					

					Planete destination = selectPlanete(carte, planete);
					
					if (destination != null) {
						System.out.println("==>Cible: " + destination);
						planetesInterdite.add(destination);
						int populationCible = Math.min(planete.getPopulation() - 10, destination.getPopulation());
						ordre.setPopulation(populationCible);
						planete.remPopulation(populationCible);
						response.addOrdre(ordre);
					}else{
						System.out.println("Aucune cible trouvée");
						break;
					}

				}
			}
		}

		return response;

	}

	public Planete selectPlanete(Carte carte, Planete planete) {

		Planete selectedPlanete = null;
		float distance = 0;
		for (Planete aPlanete : carte.getPlanetes()) {
			
			System.out.println("Analyse cible:" + aPlanete);
		    System.out.println("planetesInterdite:" + planetesInterdite);

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
