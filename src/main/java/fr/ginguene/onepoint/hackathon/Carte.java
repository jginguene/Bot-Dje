package fr.ginguene.onepoint.hackathon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Carte {

	private static Map<String, Integer> MAP_TRAJET = new HashMap<>();

	private static Map<String, Float> MAP_DISTANCE = new HashMap<>();

	private Configuration configuration;

	private List<Planete> planetes = new ArrayList<Planete>();

	private static float defaultRatio = -1;

	// key: id proprietaire
	private Map<Integer, List<Planete>> mapPlanete = new HashMap<Integer, List<Planete>>();

	private List<Flotte> flottes = new ArrayList<Flotte>();

	private Map<Integer, List<Flotte>> mapFlotte = new HashMap<Integer, List<Flotte>>();

	public static void clear() {
		MAP_TRAJET = new HashMap<>();
		MAP_DISTANCE = new HashMap<>();
		defaultRatio = -1;
	}

	public float getDistance(Planete planete1, Planete planete2) {
		String key = this.getPlanetePairKey(planete1, planete2);

		if (!MAP_DISTANCE.containsKey(key)) {
			float distance = (float) Math.sqrt(
					Math.pow(planete2.getX() - planete1.getX(), 2) + Math.pow(planete2.getY() - planete1.getY(), 2));
			MAP_DISTANCE.put(key, distance);
		}

		return MAP_DISTANCE.get(key);
	}

	public Stat getStatistique() {

		int nbAmies = 0;
		int nbEnnemies = 0;
		int nbNeutres = 0;

		for (Planete aPlanete : planetes) {
			switch (aPlanete.getStatus()) {

			case Amie:
				nbAmies++;
				break;

			case Neutre:
				nbNeutres++;

			case Ennemie:
				nbEnnemies++;

			}
		}

		Stat stat = new Stat();
		stat.setNbAmies(nbAmies);
		stat.setNbEnnemies(nbEnnemies);
		stat.setNbNeutres(nbNeutres);

		return stat;

	}

	public Stat getStatistique(Planete source, int nbVoisine) {

		int nbAmies = 0;
		int nbEnnemies = 0;
		int nbNeutres = 0;

		for (Planete aPlanete : this.getVoisines(source, nbVoisine)) {
			switch (aPlanete.getStatus()) {

			case Amie:
				nbAmies++;
				break;

			case Neutre:
				nbNeutres++;

			case Ennemie:
				nbEnnemies++;

			}
		}

		Stat stat = new Stat();
		stat.setNbAmies(nbAmies);
		stat.setNbEnnemies(nbEnnemies);
		stat.setNbNeutres(nbNeutres);

		return stat;

	}

	public void addPlanete(Planete planete) {
		// System.out.println("Ajout planete:" + planete.getId());
		this.planetes.add(planete);

		if (!mapPlanete.containsKey(planete.getProprietaire())) {
			mapPlanete.put(planete.getProprietaire(), new ArrayList<>());
		}

		mapPlanete.get(planete.getProprietaire()).add(planete);
	}

	public void addFlotte(Flotte flotte) {
		this.flottes.add(flotte);

		if (!mapFlotte.containsKey(flotte.getProprietaire())) {
			mapFlotte.put(flotte.getProprietaire(), new ArrayList<>());
		}

		mapFlotte.get(flotte.getProprietaire()).add(flotte);

		String key = this.getPlanetePairKey(flotte.getPlaneteSource(), flotte.getPlaneteDestination());
		if (!MAP_TRAJET.containsKey(key)) {
			MAP_TRAJET.put(key, flotte.getToursTotals());

			if (defaultRatio == -1.0) {

				float distance = this.getDistance(getPlanete(flotte.getPlaneteSource()),
						getPlanete(flotte.getPlaneteDestination()));

				defaultRatio = distance / flotte.getToursTotals();

				System.out.println("distance:" + distance + "; tour:" + flotte.getToursTotals() + "=> defaultRatio:"
						+ defaultRatio);
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

		String key = getPlanetePairKey(source, destination);
		if (MAP_TRAJET.containsKey(key)) {
			// System.out.println("==>" + MAP_TRAJET.get(key));
			return MAP_TRAJET.get(key);
		}

		// temps de trajet inconnu
		// Ajouter l'extrapolation
		float distance = this.getDistance(source, destination);

		// System.out.println("==>distance:" + distance);
		// System.out.println("==>defaultRatio:" + defaultRatio);

		return (int) (distance / defaultRatio);

	}

	private String getPlanetePairKey(int source, int destination) {
		if (source < destination) {
			return source + "#" + destination;
		} else {
			return destination + "#" + source;
		}

	}

	private String getPlanetePairKey(Planete source, Planete destination) {
		return getPlanetePairKey(source.getId(), destination.getId());
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

	public int getFlotte(int proprietaire, int destination) {
		List<Flotte> flottes = this.getFlotte(proprietaire);
		int ret = 0;
		for (Flotte flotte : flottes) {
			if (flotte.getPlaneteDestination() == destination) {
				ret += flotte.getVaisseaux();
			}
		}
		return ret;
	}

	public int getFlotteEnnemie(int destination) {
		int ret = 0;
		for (Flotte flotte : this.flottes) {
			if (flotte.getProprietaire() > Constantes.AMI && flotte.getPlaneteDestination() == destination) {
				ret += flotte.getVaisseaux();
			}
		}

		return ret;
	}

	public List<Flotte> getFlotte(int proprietaire) {

		List<Flotte> ret = new ArrayList<>();
		for (Flotte flotte : this.flottes) {
			if (flotte.getProprietaire() == proprietaire) {

			}
		}
		return ret;

		/*
		 * if (mapFlotte.containsKey(proprietaire)) { return
		 * mapFlotte.get(proprietaire); } else { return new ArrayList<>(); }
		 */
	}

	public List<Flotte> getFlotteEnnemies() {
		List<Flotte> flottes = new ArrayList<>();
		flottes.addAll(this.flottes);
		flottes.removeAll(this.getMesFlottes());
		return flottes;

	}

	public int getFlottesEnnemies(int destination) {
		int ret = 0;
		for (Flotte flotte : getFlotteEnnemies()) {
			if (flotte.getPlaneteDestination() == destination) {
				ret += flotte.getVaisseaux();
			}
		}
		return ret;
	}

	public int getFlottesEnnemiesFrom(int source) {
		int ret = 0;
		for (Flotte flotte : getFlotteEnnemies()) {
			if (flotte.getPlaneteSource() == source) {
				ret += flotte.getVaisseaux();
			}
		}
		return ret;
	}

	public List<Flotte> getMesFlottes() {
		return this.getFlotte(Constantes.AMI);
	}

	public int getMesFlottes(Planete destination) {
		return getMesFlottes(destination.getId());
	}

	public int getMesFlottes(int destination) {
		int ret = 0;
		for (Flotte flotte : getMesFlottes()) {
			if (flotte.getPlaneteDestination() == destination) {
				ret += flotte.getVaisseaux();
			}
		}
		return ret;
	}

	public int getNbPlanete() {
		return this.planetes.size();
	}

	public List<Planete> getMesPlanetes() {
		return this.getPlanetes(Constantes.AMI);
	}

	public List<Planete> getPlanetesEtrangeres() {
		List<Planete> ret = new ArrayList<>();
		ret.addAll(this.getPlanetes());
		ret.removeAll(this.getMesPlanetes());
		return ret;
	}

	public List<Planete> getPlanetesEnnemies() {
		List<Planete> ret = new ArrayList<>();
		for (Planete planete : this.planetes) {
			if (planete.getProprietaire() > Constantes.AMI) {
				ret.add(planete);
			}
		}

		ret.addAll(this.getPlanetes());
		ret.removeAll(this.getMesPlanetes());
		return ret;

	}

	public List<Planete> getMesTerraformations() {
		List<Planete> ret = new ArrayList<>();
		for (Planete planete : this.getPlanetes(Constantes.AMI)) {
			if (planete.getTerraformation() > 0) {
				ret.add(planete);
			}
		}
		return ret;
	}

	public List<Planete> getVoisines(Planete planete, int nbVoisine) {
		Map<Float, Planete> map = new HashMap<>();

		for (Planete aPlanete : this.planetes) {
			if (aPlanete.getId() != planete.getId()) {
				map.put(this.getDistance(planete, aPlanete), aPlanete);
			}
		}

		List<Float> distances = new ArrayList<>(map.keySet());
		Collections.sort(distances);
		List<Planete> ret = new ArrayList<>();

		for (int i = 0; i < nbVoisine; i++) {
			ret.add(map.get(distances.get(i)));
		}
		return ret;

	}

	public List<Planete> getPlanetesOrderByDistance(Planete planete) {
		Map<Float, Planete> map = new HashMap<>();

		for (Planete aPlanete : this.planetes) {
			if (aPlanete.getId() != planete.getId()) {
				float distance = this.getDistance(planete, aPlanete);
				map.put(distance, aPlanete);
			}
		}

		List<Float> distances = new ArrayList<>(map.keySet());
		Collections.sort(distances);

		Collections.sort(distances, new Comparator<Float>() {
			@Override
			public int compare(Float o1, Float o2) {
				return Float.compare(o1.floatValue(), o2.floatValue());
			}
		});

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
			if (aPlanete.getProprietaire() != Constantes.AMI) {
				float aDistance = this.getDistance(planete, aPlanete);
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
			if (aPlanete.getProprietaire() != Constantes.AMI && !exclues.contains(aPlanete)) {
				float aDistance = this.getDistance(planete, aPlanete);
				if (ret == null || aDistance < distance) {
					ret = aPlanete;
					distance = aDistance;
				}
			}
		}
		return ret;

	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;

	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public Planete getPlaneteLaPlusProche(Planete planete, PlaneteStatus status) {

		for (Planete aPlanete : this.getPlanetesOrderByDistance(planete)) {
			if (aPlanete.getStatus() == status) {
				return aPlanete;
			}
		}
		return null;

	}

}
