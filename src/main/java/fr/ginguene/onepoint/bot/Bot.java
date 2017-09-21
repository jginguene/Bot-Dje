package fr.ginguene.onepoint.bot;

import java.util.ArrayList;
import java.util.List;

public class Bot {
	
	
	public Response getResponse(Carte carte){
		
		Response response = new Response();
		
		
		System.out.println("Nombre de planete:" + carte.getPlanetes().size());
		
		for (Planete planete: carte.getPlanetes() ){			
			
			System.out.println("==>Planete " + planete.getId() + "; population:" + planete.getPopulation());
			if (planete.getProprietaire() == 1 && planete.getPopulation()>10){	
				System.out.println("==>Cible: Planete " + planete.getId());
				Ordre ordre = new Ordre();
				ordre.setOrigine(planete);
				ordre.setPopulation(planete.getPopulation() - 1);	
				response.addOrdre(ordre);	
			}	
			
		}
		
		for (Ordre ordre: response.getOrdres()){
			for (Planete planete: carte.getPlanetes() ){
				if (planete.getProprietaire() != 1){
					ordre.setDestination(planete);					
				}	
			}	
		}
		
		
		return response;
		
	}

}
