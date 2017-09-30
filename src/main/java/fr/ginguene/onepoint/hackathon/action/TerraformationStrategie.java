package fr.ginguene.onepoint.hackathon.action;

import fr.ginguene.onepoint.hackathon.Carte;
import fr.ginguene.onepoint.hackathon.Planete;
import fr.ginguene.onepoint.hackathon.PlaneteStatus;
import fr.ginguene.onepoint.hackathon.Response;
import fr.ginguene.onepoint.hackathon.ordre.Terraformation;

public class TerraformationStrategie implements IStrategie {

	@Override
	public boolean execute(Response response, Planete source, Carte carte, boolean isOptimizingScore, boolean debug) {
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
				if (debug) {
					System.out.println("Lancement de la terraformation de " + source);
				}
				return true;
			} else {
				if (debug) {
					System.out.println(source + " terraformable => " + nbEnnemie + " ennemies");
				}
			}
		}

		return false;
	}

}
