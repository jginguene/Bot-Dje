package fr.ginguene.onepoint.hackathon.action;

import fr.ginguene.onepoint.hackathon.Carte;
import fr.ginguene.onepoint.hackathon.Planete;
import fr.ginguene.onepoint.hackathon.PlaneteStatus;
import fr.ginguene.onepoint.hackathon.Response;
import fr.ginguene.onepoint.hackathon.ordre.Terraformation;

public class TerraformationStrategie extends AbstractStrategie {

	public TerraformationStrategie(boolean isDebug) {
		super(isDebug);
	}

	public TerraformationStrategie() {
		super();
	}

	@Override
	public boolean execute(Response response, Planete source, Carte carte, boolean isOptimizingScore) {
		if (source.getTerraformation() > 0) {
			System.out.println("Terraformation en cours " + source + " -> " + source.getTerraformation());
			return true;
		}

		if (carte.getConfiguration().getTour() < 50) {
			return false;
		}

		for (Planete aPlanete : carte.getPlanetes(PlaneteStatus.Amie)) {
			if (aPlanete.getTerraformation() > 5) {
				return false;
			}
		}

		if (source.isTerraformable()) {
			int nbEnnemie = carte.getNbEnnemie(source, 6);

			if (nbEnnemie == 0) {
				Terraformation terraformation = new Terraformation();
				terraformation.setPlanete(source);
				response.addOrdre(terraformation);

				trace("Lancement de la terraformation de " + source);

				return true;
			} else {
				// trace(source + " terraformable => " + nbEnnemie + "
				// ennemies");

			}
		}

		return false;
	}

}
