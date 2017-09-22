package fr.ginguene.onepoint.hackathon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Carte {

	private static final Map<String, Integer> MAP_TRAJET = new HashMap<>();

	private List<Planete> planetes = new ArrayList<Planete>();

	private float defaultRatio = -1;

	// key: id proprietaire
	private Map<Integer, List<Planete>> mapPlanete = new HashMap<Integer, List<Planete>>();

	private List<Flotte> flottes = new ArrayList<Flotte>();

	private Map<Integer, List<Flotte>> mapFlotte = new HashMap<Integer, List<Flotte>>();

	public void addPlanete(Planete planete) {
		// System.out.println("Ajout planete:" + planete.getId());
		this.planetes.add(planete);

		if (!mapPlanete.containsKey(planete.getProprietaire())) {
			mapPlanete.put(planete.getProprietaire(), new ArrayList<>());
		}

		mapPlanete.get(planete.getProprietaire()).add(planete);
	}

	public void addFlotte(Flotte flotte) {
		// System.out.println("Ajout planete:" + planete.getId());
		this.flottes.add(flotte);

		if (!mapFlotte.containsKey(flotte.getProprietaire())) {
			mapFlotte.put(flotte.getProprietaire(), new ArrayList<>());
		}

		mapFlotte.get(flotte.getProprietaire()).add(flotte);

		String key = this.getTrajetKey(flotte.getPlaneteSource(), flotte.getPlaneteDestination());
		if (!MAP_TRAJET.containsKey(key)) {
			MAP_TRAJET.put(key, flotte.getToursTotals());

			if (defaultRatio == -1.0) {

				float distance = getPlanete(flotte.getPlaneteSource())
						.calcDistance(getPlanete(flotte.getPlaneteDestination()));

				this.defaultRatio = distance / flotte.getToursTotals();
			}

		}

	}

	public Planete getPlanete(int id) {
		for (Planete planete : planetes) {
			if (planete.getId() == id) {
				return planete;
			}
		}

		return null;
	}

	public int getTrajetNbTour(Planete source, Planete destination) {

		String key = getTrajetKey(source, destination);
		if (MAP_TRAJET.containsKey(key)) {
			return MAP_TRAJET.get(key);
		}

		// temps de trajet inconnu
		// Ajouter l'extrapolation

		float distance = source.calcDistance(destination);

		return (int) (distance / defaultRatio);

	}

	private String getTrajetKey(int source, int destination) {
		if (source < destination) {
			return source + "#" + destination;
		} else {
			return destination + "#" + source;
		}

	}

	private String getTrajetKey(Planete source, Planete destination) {
		return getTrajetKey(source.getId(), destination.getId());

	}

	public List<Planete> getPlanetes() {
		return planetes;
	}

	public List<Planete> getPlanetes(int proprietaire) {

		if (mapPlanete.containsKey(proprietaire)) {
			return mapPlanete.get(proprietaire);
		} else {
			return new ArrayList<>();
		}
	}

	public List<Flotte> getFlotte(int proprietaire) {

		if (mapFlotte.containsKey(proprietaire)) {
			return mapFlotte.get(proprietaire);
		} else {
			return new ArrayList<>();
		}
	}

	public List<Flotte> getMesFlottes() {
		return this.getFlotte(Constantes.MOI);
	}

	public int getMesFlottes(Planete destination) {
		int ret = 0;
		for (Flotte flotte : getMesFlottes()) {
			if (flotte.getPlaneteDestination() == destination.getId()) {
				ret += flotte.getVaisseaux();
			}
		}
		return ret;
	}

	public int getNbPlanete() {
		return this.planetes.size();
	}

	public List<Planete> getMesPlanetes() {
		return this.getPlanetes(Constantes.MOI);
	}

	public List<Planete> getMesTerraformations() {
		List<Planete> ret = new ArrayList<>();
		for (Planete planete : this.getPlanetes(Constantes.MOI)) {
			if (planete.getTerraformation() > 0) {
				ret.add(planete);
			}
		}
		return ret;
	}

	public List<Planete> getVoisines(Planete planete) {
		Map<Float, Planete> map = new HashMap<>();

		for (Planete aPlanete : this.planetes) {
			if (aPlanete.getId() != planete.getId()) {
				map.put(planete.calcDistance(aPlanete), aPlanete);
			}
		}

		List<Float> distances = new ArrayList<>(map.keySet());
		Collections.sort(distances);
		List<Planete> ret = new ArrayList<>();

		for (int i = 0; i < 5; i++) {
			ret.add(map.get(distances.get(i)));
		}
		return ret;

	}

	public List<Planete> getPlanetesOrderByDistance(Planete planete) {
		Map<Float, Planete> map = new HashMap<>();

		for (Planete aPlanete : this.planetes) {
			if (aPlanete.getId() != planete.getId()) {
				map.put(planete.calcDistance(aPlanete), aPlanete);
			}
		}

		List<Float> distances = new ArrayList<>(map.keySet());
		Collections.sort(distances);
		List<Planete> ret = new ArrayList<>();

		for (float distance : distances) {
			ret.add(map.get(distance));
		}
		return ret;

	}

	public Planete getEnnemiLaPlusProche(Planete planete) {

		float distance = -1;
		Planete ret = null;

		for (Planete aPlanete : this.planetes) {
			if (aPlanete.getProprietaire() != Constantes.MOI) {
				float aDistance = planete.calcDistance(aPlanete);
				if (ret == null || aDistance < distance) {
					ret = aPlanete;
					distance = aDistance;
				}
			}
		}
		return ret;

	}

	public Planete getEnnemiLaPlusProche(Planete planete, List<Planete> exclues) {

		float distance = -1;
		Planete ret = null;

		for (Planete aPlanete : this.planetes) {
			if (aPlanete.getProprietaire() != Constantes.MOI && !exclues.contains(aPlanete)) {
				float aDistance = planete.calcDistance(aPlanete);
				if (ret == null || aDistance < distance) {
					ret = aPlanete;
					distance = aDistance;
				}
			}
		}
		return ret;

	}

}