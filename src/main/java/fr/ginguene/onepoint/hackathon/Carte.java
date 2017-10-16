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

	private static float DEFAULT_RATIO = -1;

	// key: id proprietaire
	private Map<PlaneteStatus, List<Planete>> mapPlanete = new HashMap<PlaneteStatus, List<Planete>>();

	private List<Flotte> flottes = new ArrayList<Flotte>();

	private Map<PlaneteStatus, List<Flotte>> mapFlotte = new HashMap<PlaneteStatus, List<Flotte>>();

	public static void clear() {
		MAP_TRAJET = new HashMap<>();
		MAP_DISTANCE = new HashMap<>();
		DEFAULT_RATIO = -1;
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
		this.planetes.add(planete);

		if (!mapPlanete.containsKey(planete.getStatus())) {
			mapPlanete.put(planete.getStatus(), new ArrayList<>());
		}

		mapPlanete.get(planete.getStatus()).add(planete);
	}

	public void addFlotte(Flotte flotte) {
		this.flottes.add(flotte);

		if (!mapFlotte.containsKey(flotte.getSourceStatus())) {
			mapFlotte.put(flotte.getSourceStatus(), new ArrayList<>());
		}

		mapFlotte.get(flotte.getSourceStatus()).add(flotte);

		String key = this.getPlanetePairKey(flotte.getSource(), flotte.getDestination());
		if (!MAP_TRAJET.containsKey(key)) {
			MAP_TRAJET.put(key, flotte.getToursTotals());

			if (DEFAULT_RATIO == -1.0 && flotte.getToursTotals() > 0) {

				float distance = this.getDistance(flotte.getSource(), flotte.getDestination());

				DEFAULT_RATIO = distance / flotte.getToursTotals();

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
			// System.out.println("T==>" + MAP_TRAJET.get(key));
			return MAP_TRAJET.get(key);
		}

		// temps de trajet inconnu
		// Ajouter l'extrapolation
		float distance = this.getDistance(source, destination);

		// System.out.println("==>distance:" + distance);
		// System.out.println("==>defaultRatio:" + DEFAULT_RATIO);

		return (int) (distance / DEFAULT_RATIO);

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

	public List<Planete> getPlanetes(PlaneteStatus status) {

		if (mapPlanete.containsKey(status)) {
			return mapPlanete.get(status);
		} else {
			return new ArrayList<>();
		}
	}

	public List<Flotte> getFlottes(PlaneteStatus flotteStatus) {
		List<Flotte> ret = mapFlotte.get(flotteStatus);
		if (ret == null) {
			ret = new ArrayList<>();
		}
		return ret;
	}

	public List<Flotte> getFlottes(PlaneteStatus flotteStatus, Planete destination) {
		List<Flotte> ret = new ArrayList<>();
		for (Flotte flotte : getFlottes(flotteStatus)) {
			if (flotte.getDestination().equals(destination)) {
				ret.add(flotte);
			}
		}
		return ret;
	}

	public int getNbVoisine(Planete source, PlaneteStatus status, int nbVoisines) {
		int nb = 0;
		for (Planete aPlanete : this.getVoisines(source, nbVoisines)) {
			if (aPlanete.getStatus() == status) {
				nb++;
			}
		}
		return nb;
	}

	public int getNbEnnemie(Planete source, int nbVoisines) {
		int nbEnnemie = 0;
		for (Planete aPlanete : this.getVoisines(source, nbVoisines)) {
			if (aPlanete.getStatus() == PlaneteStatus.Ennemie) {
				nbEnnemie++;
			}
		}
		return nbEnnemie;
	}

	public int getNbVaisseauInFlotteFrom(PlaneteStatus flotteStatus, Planete source) {
		int res = 0;
		for (Flotte flotte : getFlottes(flotteStatus, source)) {
			res += flotte.getVaisseaux();
		}
		return res;

	}

	public int getNbVaisseauInFlotte(PlaneteStatus flotteStatus, Planete destination, int nbTour) {

		int res = 0;
		for (Flotte flotte : getFlottes(flotteStatus, destination)) {
			if (flotte.getToursRestants() <= nbTour) {
				res += flotte.getVaisseaux();
			}
		}
		return res;

	}

	public int getNbVaisseauInFlotte(PlaneteStatus flotteStatus, Planete destination) {
		int res = 0;
		for (Flotte flotte : getFlottes(flotteStatus, destination)) {
			res += flotte.getVaisseaux();
		}
		return res;

	}

	public int getNbPlanete() {
		return this.planetes.size();
	}

	public List<Planete> getMesTerraformations() {
		List<Planete> ret = new ArrayList<>();
		for (Planete planete : this.getPlanetes(PlaneteStatus.Amie)) {
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
			if (aPlanete.getStatus() != PlaneteStatus.Amie) {
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
