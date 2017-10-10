package fr.ginguene.onepoint.hackathon.action;

import fr.ginguene.onepoint.hackathon.Carte;
import fr.ginguene.onepoint.hackathon.Flotte;
import fr.ginguene.onepoint.hackathon.Planete;
import fr.ginguene.onepoint.hackathon.PlaneteStatus;
import fr.ginguene.onepoint.hackathon.Response;
import fr.ginguene.onepoint.hackathon.ordre.Terraformation;

public class DefenseTerraformation extends AbstractStrategie {

	public DefenseTerraformation(boolean isDebug) {
		super(isDebug);
	}

	public DefenseTerraformation() {
		super();
	}

	@Override
	public boolean execute(Response response, Planete source, Carte carte, boolean isOptimizingScore) {
		if (source.getTerraformation() > 0 || !source.isTerraformable()) {
			return false;
		}

		for (Flotte flotte : carte.getFlottes(PlaneteStatus.Ennemie, source)) {
			if (flotte.getToursRestants() == 1
					&& flotte.getVaisseaux() > source.getPopulation() + source.getTauxCroissance()) {

				Terraformation terraformation = new Terraformation();
				terraformation.setPlanete(source);
				response.addOrdre(terraformation);

				trace("Lancement de la terraformation de " + source + " comme défense");
				return true;
			}
		}

		return false;

	}

}
