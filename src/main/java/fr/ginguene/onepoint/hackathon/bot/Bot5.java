package fr.ginguene.onepoint.hackathon.bot;

import java.util.List;

import fr.ginguene.onepoint.hackathon.Carte;
import fr.ginguene.onepoint.hackathon.IBot;
import fr.ginguene.onepoint.hackathon.Planete;
import fr.ginguene.onepoint.hackathon.PlaneteStatus;
import fr.ginguene.onepoint.hackathon.Response;
import fr.ginguene.onepoint.hackathon.action.AbstractStrategie;
import fr.ginguene.onepoint.hackathon.action.AttaquePlaneteEnnemie;
import fr.ginguene.onepoint.hackathon.action.AttaquePlaneteNeutreStrategie;
import fr.ginguene.onepoint.hackathon.action.DefenseTerraformation;
import fr.ginguene.onepoint.hackathon.action.MegabombeStrategie;
import fr.ginguene.onepoint.hackathon.action.PremierTourStrategie;
import fr.ginguene.onepoint.hackathon.action.PreparationBombeStrategie;
import fr.ginguene.onepoint.hackathon.action.ProtectionStrategie;
import fr.ginguene.onepoint.hackathon.action.TerraformationStrategie;

public class Bot5 implements IBot {

	private AbstractStrategie[] strategies = new AbstractStrategie[] { new PremierTourStrategie(),
			new DefenseTerraformation(), new TerraformationStrategie(true), new MegabombeStrategie(),
			new PreparationBombeStrategie(), new ProtectionStrategie(), new AttaquePlaneteNeutreStrategie(true),
			// new Acharnement()
			// , new AttaquePlaneteEnnemieRapprochee()
			new AttaquePlaneteEnnemie(true)

	};

	public Response getResponse(Carte carte) {

		Response response = new Response();

		System.out.println("Tour " + carte.getConfiguration().getTour());

		List<Planete> mesPlanetes = carte.getPlanetes(PlaneteStatus.Amie);
		int nbPlanetesEnnemies = carte.getPlanetes(PlaneteStatus.Ennemie).size();

		boolean isScoreOptimizing = (nbPlanetesEnnemies <= 4 && carte.getConfiguration().getTour() > 100);
		// isScoreOptimizing = false;

		for (Planete source : mesPlanetes) {

			System.out.println(source + "=> population " + source.getPopulation() + "/" + source.getPopulationMax());

			if (isScoreOptimizing) {
				System.out.println("=> " + isScoreOptimizing);
			}

			for (AbstractStrategie strategie : strategies) {

				if (strategie.execute(response, source, carte, isScoreOptimizing)) {
					System.out.println(source + " use stategie " + strategie.getClass().getSimpleName());
					break;
				}

			}

		}

		return response;

	}

}