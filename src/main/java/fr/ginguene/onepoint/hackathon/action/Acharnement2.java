package fr.ginguene.onepoint.hackathon.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fr.ginguene.onepoint.hackathon.Carte;
import fr.ginguene.onepoint.hackathon.Flotte;
import fr.ginguene.onepoint.hackathon.Planete;
import fr.ginguene.onepoint.hackathon.PlaneteStatus;
import fr.ginguene.onepoint.hackathon.Response;
import fr.ginguene.onepoint.hackathon.ordre.EnvoiFlotte;

public class Acharnement2 extends AbstractStrategie {

	private HashMap<Integer, Integer> map = new HashMap<>();
	private HashMap<Integer, List<Integer>> reverseMap = new HashMap<>();

	public Acharnement2(boolean isDebug) {
		super(isDebug);
	}

	public Acharnement2() {
		super();
	}

	@Override
	public boolean execute(Response response, Planete source, Carte carte, boolean isOptimizingScore) {

		Integer destinationId = map.get(source.getId());
		Planete destination = null;

		// Nettoyer les listes
		for (Integer existingDestinationId : reverseMap.keySet()) {

			ArrayList<Integer> copy = new ArrayList<>(reverseMap.get(existingDestinationId));

			for (Integer aPlaneteId : copy) {
				Planete aPlanete = carte.getPlanete(aPlaneteId);
				if (aPlanete.getStatus() != PlaneteStatus.Amie) {
					reverseMap.get(existingDestinationId).remove(new Integer(aPlanete.getId()));
					map.remove(new Integer(aPlanete.getId()));
				}
			}
		}

		if (destinationId == null) {
			// Choisi une cible existante
			for (Integer existingDestinationId : reverseMap.keySet()) {
				if (reverseMap.get(existingDestinationId).size() < 4) {
					destinationId = existingDestinationId;
					map.put(source.getId(), existingDestinationId);
					reverseMap.get(existingDestinationId).add(source.getId());
					break;
				}
			}

		}

		// Controle de la cible
		if (destinationId != null) {

			destination = carte.getPlanete(destinationId);

			boolean bombeEnApproche = false;
			for (Flotte flotte : carte.getFlottes(PlaneteStatus.Amie, destination)) {
				if (flotte.getVaisseaux() >= destination.getPopulationMax()) {
					bombeEnApproche = true;
				}
			}

			int nbVaisseau = carte.getNbVaisseauInFlotte(PlaneteStatus.Amie, destination);

			if (destination.getStatus() == PlaneteStatus.Amie || bombeEnApproche
					|| nbVaisseau > destination.getPopulationMax() * 3) {
				reverseMap.remove(destinationId);
				map.remove(source.getId());
				destinationId = null;
			}

		}

		if (destinationId == null) {
			destination = chooseTarget(source, carte);
			destinationId = destination.getId();
			reverseMap.put(destinationId, new ArrayList<>());
			map.put(source.getId(), destinationId);
		} else {
			destination = carte.getPlanete(destinationId);
		}

		int nbVaisseau = source.getPopulation() - carte.getNbVaisseauInFlotte(PlaneteStatus.Ennemie, source, 20) - 10;

		if (nbVaisseau > 4) {
			EnvoiFlotte ordre = new EnvoiFlotte(carte, source, destination, nbVaisseau);
			source.remPopulation(nbVaisseau);
			response.addOrdre(ordre);
			return true;
		}
		return false;
	}

	private Planete chooseTarget(Planete source, Carte carte) {

		for (Planete aPlanete : carte.getPlanetesOrderByDistance(source)) {
			if (aPlanete.getStatus() != PlaneteStatus.Amie && !reverseMap.containsKey(aPlanete.getId())) {
				return aPlanete;
			}
		}
		// On choisit la planete ennemie la plus proche
		return carte.getEnnemiLaPlusProche(source);

	}

}
