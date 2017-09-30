package fr.ginguene.onepoint.hackathon.bot;

import java.util.List;

import fr.ginguene.onepoint.hackathon.Carte;
import fr.ginguene.onepoint.hackathon.IBot;
import fr.ginguene.onepoint.hackathon.Planete;
import fr.ginguene.onepoint.hackathon.Response;
import fr.ginguene.onepoint.hackathon.action.AbstractStrategie;
import fr.ginguene.onepoint.hackathon.action.AideStrategie;
import fr.ginguene.onepoint.hackathon.action.AttaquePlaneteEtrangereLaPlusProcheStrategie;
import fr.ginguene.onepoint.hackathon.action.AttaquePlaneteNeutreStrategie;
import fr.ginguene.onepoint.hackathon.action.BombardeStrategie;
import fr.ginguene.onepoint.hackathon.action.MegabombeStrategie;
import fr.ginguene.onepoint.hackathon.action.TerraformationStrategie;

public class Bot5 implements IBot {

	private BotPremierTour botPremierTour = new BotPremierTour();

	private AttaquePlaneteEtrangereLaPlusProcheStrategie attaquePlaneteEtrangereLaPlusProcheStrategie = new AttaquePlaneteEtrangereLaPlusProcheStrategie();

	private AbstractStrategie[] stategies = new AbstractStrategie[] { new TerraformationStrategie(true),
			new MegabombeStrategie(true), new AideStrategie(true), new BombardeStrategie(true),
			new AttaquePlaneteNeutreStrategie(true), new AttaquePlaneteEtrangereLaPlusProcheStrategie(true)

	};

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

			for (AbstractStrategie stategie : stategies) {

				if (stategie.execute(response, source, carte, isScoreOptimizing)) {
					continue;
				}

			}

		}

		return response;

	}

}