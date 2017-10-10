package fr.ginguene.onepoint.hackathon.action.bot7;

import java.util.HashMap;
import java.util.Map;

import fr.ginguene.onepoint.hackathon.Carte;
import fr.ginguene.onepoint.hackathon.Planete;
import fr.ginguene.onepoint.hackathon.PlaneteStatus;
import fr.ginguene.onepoint.hackathon.Response;
import fr.ginguene.onepoint.hackathon.action.AbstractStrategie;

public class AttaquePlaneteEnnemieAPlusieurs extends AbstractStrategie {

	private Map<Planete, Planete> targetMap = new HashMap<Planete, Planete>();

	public AttaquePlaneteEnnemieAPlusieurs(boolean isDebug) {
		super(isDebug);
	}

	public AttaquePlaneteEnnemieAPlusieurs() {
		super();
	}

	@Override
	public boolean execute(Response response, Planete source, Carte carte, boolean isOptimizingScore) {

		Planete destination = null;

		if (targetMap.containsKey(source)) {
			destination = targetMap.get(source);
			if (destination.getStatus() == PlaneteStatus.Amie) {
				targetMap.remove(source);
				destination = null;
			}
		}

		if (destination == null) {
			destination = chooseTarget(source, carte);
			targetMap.put(source, destination);
		}

		return false;
	}

	private Planete chooseTarget(Planete source, Carte carte) {
		Map<Planete, Integer> map = new HashMap<Planete, Integer>();
		for (Planete destination : targetMap.values()) {
			if (map.containsKey(destination)) {
				map.put(destination, map.get(destination) + 1);
			} else {
				map.put(destination, 1);
			}
		}

		// On touve déja une planète ciblée par moins de 3 planète
		for (Planete destination : map.keySet()) {
			int count = map.get(destination);
			if (count < 3) {
				return destination;
			}
		}

		// On choisit la planete ennemie la plus proche
		return carte.getEnnemiLaPlusProche(source);

	}

}
