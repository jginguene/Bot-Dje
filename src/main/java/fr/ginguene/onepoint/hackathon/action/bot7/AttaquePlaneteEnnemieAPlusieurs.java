package fr.ginguene.onepoint.hackathon.action.bot7;

import java.util.HashMap;
import java.util.Map;

import fr.ginguene.onepoint.hackathon.Carte;
import fr.ginguene.onepoint.hackathon.Planete;
import fr.ginguene.onepoint.hackathon.PlaneteStatus;
import fr.ginguene.onepoint.hackathon.Response;
import fr.ginguene.onepoint.hackathon.action.AbstractStrategie;
import fr.ginguene.onepoint.hackathon.ordre.EnvoiFlotte;

public class AttaquePlaneteEnnemieAPlusieurs extends AbstractStrategie {

	private Map<Planete, Integer> targetMap = new HashMap<Planete, Integer>();

	public AttaquePlaneteEnnemieAPlusieurs(boolean isDebug) {
		super(isDebug);
	}

	public AttaquePlaneteEnnemieAPlusieurs() {
		super();
	}

	@Override
	public boolean execute(Response response, Planete source, Carte carte, boolean isOptimizingScore) {

		Planete destination = null;

		trace("targetMap:" + targetMap);

		if (targetMap.containsKey(source)) {

			int destinationId = targetMap.get(source);
			destination = carte.getPlanete(destinationId);

			trace(source + " => " + destination);
			if (destination.getStatus() == PlaneteStatus.Amie) {
				trace(destination + " conquise");
				targetMap.remove(source);
				destination = null;
			}
		}

		if (destination == null) {
			destination = chooseTarget(source, carte);
			trace("choix de " + destination + " pour " + source);
			targetMap.put(source, destination.getId());
		}

		int nbVaisseau = source.getPopulation() - carte.getNbVaisseauInFlotte(PlaneteStatus.Ennemie, source) - 1;
		trace("nbVaisseau " + nbVaisseau + " = " + source.getPopulation() + " - "
				+ -carte.getNbVaisseauInFlotte(PlaneteStatus.Ennemie, source));

		if (nbVaisseau > 3) {
			EnvoiFlotte ordre = new EnvoiFlotte(carte, source, destination, nbVaisseau);
			source.remPopulation(nbVaisseau);
			response.addOrdre(ordre);
			return true;
		}

		return false;
	}

	private Planete chooseTarget(Planete source, Carte carte) {
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		for (Integer destinationId : targetMap.values()) {
			if (map.containsKey(destinationId)) {
				map.put(destinationId, map.get(destinationId) + 1);
			} else {
				map.put(destinationId, 1);
			}
		}

		// On touve déja une planète ciblée par moins de 3 planète
		for (Integer destinationId : map.keySet()) {
			int count = map.get(destinationId);
			if (count < 3) {

				trace("Planete déja visée trouvée");
				return carte.getPlanete(destinationId);
			}
		}

		// On choisit la planete ennemie la plus proche
		return carte.getEnnemiLaPlusProche(source);

	}

}
