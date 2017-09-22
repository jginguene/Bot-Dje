package fr.ginguene.onepoint.hackathon.ordre;

import fr.ginguene.onepoint.hackathon.Planete;

public class Terraformation implements Ordre{
	
	public Planete getPlanete() {
		return planete;
	}



	public void setPlanete(Planete planete) {
		this.planete = planete;
	}


	private Planete planete;
	
	
	
	
	
	@Override
	public String asString(){
		//<PlanèteATerraformer:int>
		return Integer.toString(planete.getId());
	}

}
