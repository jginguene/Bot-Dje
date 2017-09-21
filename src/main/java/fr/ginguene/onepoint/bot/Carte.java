package fr.ginguene.onepoint.bot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Carte {

	private List<Planete> planetes = new ArrayList<Planete>();
	private Map<Integer, List<Planete>> hashPlanete = new HashMap<Integer, List<Planete>>();

	private List<Flotte> flottes = new ArrayList<Flotte>();
	private Map<Integer, List<Flotte>> hashFlotte = new HashMap<Integer, List<Flotte>>();

	public void addPlanete(Planete planete) {
		// System.out.println("Ajout planete:" + planete.getId());
		this.planetes.add(planete);

		if (!hashPlanete.containsKey(planete.getProprietaire())) {
			hashPlanete.put(planete.getProprietaire(), new ArrayList<>());
		}

		hashPlanete.get(planete.getProprietaire()).add(planete);
	}

	public void addFlotte(Flotte flotte) {
		// System.out.println("Ajout planete:" + planete.getId());
		this.flottes.add(flotte);

		if (!hashFlotte.containsKey(flotte.getProprietaire())) {
			hashFlotte.put(flotte.getProprietaire(), new ArrayList<>());
		}

		hashFlotte.get(flotte.getProprietaire()).add(flotte);
	}

	public List<Planete> getPlanetes() {
		return planetes;
	}

	public List<Planete> getPlanetes(int proprietaire) {

		if (hashPlanete.containsKey(proprietaire)) {
			return hashPlanete.get(proprietaire);
		} else {
			return new ArrayList<>();
		}
	}

	public List<Flotte> getFlotte(int proprietaire) {

		if (hashFlotte.containsKey(proprietaire)) {
			return hashFlotte.get(proprietaire);
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
			;
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

}
