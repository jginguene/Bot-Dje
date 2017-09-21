package fr.ginguene.onepoint.bot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Carte {

	private List<Planete> planetes = new ArrayList<Planete>();
	private Map<Integer, List<Planete>> hashPlanete = new HashMap<Integer, List<Planete>>();

	public void addPlanete(Planete planete) {
		// System.out.println("Ajout planete:" + planete.getId());
		this.planetes.add(planete);

		if (!hashPlanete.containsKey(planete.getProprietaire())) {
			hashPlanete.put(planete.getProprietaire(), new ArrayList<>());
		}

		hashPlanete.get(planete.getProprietaire()).add(planete);
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
