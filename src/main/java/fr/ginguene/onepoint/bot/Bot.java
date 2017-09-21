package fr.ginguene.onepoint.bot;

import java.util.ArrayList;
import java.util.List;

public class Bot {
	
	
	public Response getResponse(Carte carte){
		
		Response response = new Response();
		
		List<Ordre> ordres= new ArrayList<Ordre>();
		
		for (Planete planete: carte.getPlanetes() ){
			
			if (planete.getProprietaire() == 1 && planete.getPopulation()>1){
				Ordre ordre = new Ordre();
				ordre.setOrigine(planete);
				ordre.setPopulation(planete.getPopulation() - 1);		
				response.addOrdre(ordre);	
			}	
			
		}
		
		for (Ordre ordre: ordres){
			for (Planete planete: carte.getPlanetes() ){
				if (planete.getProprietaire() != 1){
					ordre.setDestination(planete);					
				}	
			}	
		}
		
		
		return response;
		
	}

}
