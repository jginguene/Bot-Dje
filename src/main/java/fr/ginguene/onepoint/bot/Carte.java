package fr.ginguene.onepoint.bot;

import java.util.ArrayList;
import java.util.List;

public class Carte {
	
	private List<Planete> planetes = new ArrayList<Planete>();
	
	
	
	
	public void addPlanete(Planete planete){
	//	System.out.println("Ajout planete:" + planete.getId());
		this.planetes.add(planete);
	}
	
	public  List<Planete> getPlanetes(){
		return planetes;
	}

}
