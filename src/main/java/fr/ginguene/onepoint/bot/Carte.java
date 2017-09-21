package fr.ginguene.onepoint.bot;

import java.util.ArrayList;
import java.util.List;

public class Carte {
	
	private List<Planete> planetes = new ArrayList<Planete>();
	
	
	
	
	public void addPlanete(Planete planet){
		this.planetes.add(planet);
	}
	
	public  List<Planete> getPlanetes(){
		return planetes;
	}

}
